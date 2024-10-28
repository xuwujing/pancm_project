package com.zans.base.aspect;


import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.zans.base.annotion.Record;
import com.zans.base.vo.UserSession;
import com.zans.portal.config.GlobalConstants;
import com.zans.portal.dao.RadiusEndpointMapper;
import com.zans.portal.model.LogOperation;
import com.zans.portal.model.RadiusEndpoint;
import com.zans.portal.service.ILogOperationService;
import com.zans.portal.util.HttpHelper;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Date;

import static com.zans.portal.constants.PortalConstants.PORTAL_MODULE_TYPE_JUDGE;


/**
 * @Author pancm
 * @Description 日志记录切面
 * @Date 2020/8/13
 * @Param
 * @return
 **/
@Aspect
@Component
@Slf4j
@Order(-1)
public class LogAspect {

    @Autowired
    protected HttpHelper httpHelper;

    @Autowired
    private ILogOperationService logOperationService;

    @Resource
    private RadiusEndpointMapper endpointMapper;


    /**
     * @Title: logExecution
     * @Description: 切入点表达式
     */
    @Pointcut("@annotation(com.zans.base.annotion.Record)")
    public void pointcut() {
        // done
    }

    /**
     * @param joinPoint
     * @Title: before
     * @Description: 前置通知处理方法
     */
    @Before("pointcut()")
    public void before(JoinPoint joinPoint) {
        LogOperation logOperation = new LogOperation();
        try {
            Class<?> className = joinPoint.getTarget().getClass();
            String methodName = joinPoint.getSignature().getName();
            Class<?>[] argClass = ((MethodSignature) joinPoint.getSignature()).getParameterTypes();
            Method method = className.getMethod(methodName, argClass);
            Record annotation = method.getAnnotation(Record.class);
            final boolean isLog = annotation.log();
            if (!isLog) {
                return;
            }
            String traceId = getTraceId();
            String user = getUser();
            String ip = getIp();
            String arguments;
            try {
                arguments = JSONObject.toJSONString(joinPoint.getArgs()[0], SerializerFeature.IgnoreNonFieldGetter);
            } catch (JSONException e) {
                arguments = "";
            }
            log.info("{}|请求用户:{},请求ip:{},请求类:{},请求方法:{},请求内容:{}|", traceId, user, ip, className.getName(), methodName, arguments);
            if (method.isAnnotationPresent(Record.class)) {
                if (StringUtils.isEmpty(traceId)) {
                    return;
                }
                logOperation.setUserName(user);
                logOperation.setOpPlatform(annotation.opPlatform());
                logOperation.setModule(annotation.module());
                if (PORTAL_MODULE_TYPE_JUDGE.equals(annotation.type())) {
                    String mac = (String) joinPoint.getArgs()[3];
                    Integer policy = (Integer) joinPoint.getArgs()[1];
                    String policyName = getPolicyName(policy);
                    Integer originalPolicy = policy;
                    RadiusEndpoint ed =  endpointMapper.findEndpointByMacMin(mac);
                    if(ed!=null){
                        originalPolicy = ed.getAccessPolicy();
                    }
                    String originalPolicyName= getPolicyName(originalPolicy);
                    String remark = String.format("操作对象:%s,由 %s -> %s ",mac,policyName,originalPolicyName);
                    logOperation.setRemark(remark);
                }
                //批量审核
//                if (PORTAL_MODULE_TYPE_JUDGE_BATCH.equals(annotation.type())) {
//                    String mac = (String) joinPoint.getArgs()[3];
//                    Integer policy = (Integer) joinPoint.getArgs()[1];
//                    String policyName = getPolicyName(policy);
//                    Integer originalPolicy = policy;
//                    RadiusEndpoint ed =  endpointMapper.findEndpointByMacMin(mac);
//                    if(ed!=null){
//                        originalPolicy = ed.getAccessPolicy();
//                    }
//                    String originalPolicyName= getPolicyName(originalPolicy);
//                    String remark = String.format("操作对象:%s,由 %s -> %s ",mac,policyName,originalPolicyName);
//                    logOperation.setRemark(remark);
//                }

                logOperation.setOperation(annotation.operation());
                logOperation.setFromIp(ip);
                logOperation.setMethodName(methodName);
                logOperation.setClassName(className.getName());
                logOperation.setReqData(arguments);
                logOperation.setTraceId(traceId);
                logOperation.setCreateTime(new Date());
                logOperation.setUpdateTime(new Date());
                logOperation.setResult("成功");
                logOperationService.insert(logOperation);
            }
        } catch (Exception e) {
            logOperation.setResult("失败");
            log.error("前置通知日志记录失败! class:{} ", joinPoint.getTarget().getClass().getName(), e);
        }
    }


    private String getPolicyName(int policy){
        if(policy == 0){
            return "阻断";
        }
        if(policy == 1){
            return "检疫";
        }
        return "放行";
    }

    /**
     * @param joinPoint
     * @param
     * @throws SecurityException
     * @throws NoSuchMethodException
     * @Title: doAfterThrowing
     * @Description: 后置通知处理方法
     */
    @AfterReturning(pointcut = "pointcut()", returning = "returnValue")
    public void doAfterReturning(JoinPoint joinPoint, Object returnValue) {
        try {
            Class<?> className = joinPoint.getTarget().getClass();
            String methodName = joinPoint.getSignature().getName();
            Class<?>[] argClass = ((MethodSignature) joinPoint.getSignature()).getParameterTypes();
            Method method = className.getMethod(methodName, argClass);
            Record annotation = method.getAnnotation(Record.class);
            final boolean isLog = annotation.log();
            if (!isLog) {
                return;
            }
            String traceId = getTraceId();
            String user = getUser();
            String ip = getIp();
            log.info("{}|请求用户:{},返回ip:{},响应内容:{}|", traceId, user, ip, returnValue);
            if (method.isAnnotationPresent(Record.class)) {
                if (checkAndUpdateLog(method, traceId, JSONObject.toJSONString(returnValue), "成功")) {
                    return;
                }
            }
        } catch (Exception e) {
            log.error("后置通知日志记录失败", e);
        }
    }

    private boolean checkAndUpdateLog(Method method, String traceId, String s, String logResultSuccess) {
        Record annotation = method.getAnnotation(Record.class);
        final boolean log = annotation.log();
        if (!log) {
            return true;
        }
        if (StringUtils.isEmpty(traceId)) {
            return true;
        }
        LogOperation logOperation = new LogOperation();
        logOperation.setRepData(s);
        logOperation.setResult(logResultSuccess);
        logOperation.setTraceId(traceId);
        logOperationService.updateByTraceId(logOperation);
        return false;
    }


    /**
     * @param joinPoint
     * @param e
     * @Title: doAfterThrowing
     * @Description: 异常通知处理方法
     */
    @AfterThrowing(pointcut = "pointcut()", throwing = "e")
    public void doAfterThrowing(JoinPoint joinPoint, Throwable e) {
        try {
            Class<?> className = joinPoint.getTarget().getClass();
            String methodName = joinPoint.getSignature().getName();
            Class<?>[] argClass = ((MethodSignature) joinPoint.getSignature()).getParameterTypes();
            Method method = className.getMethod(methodName, argClass);
            String traceId = getTraceId();
            String user = getUser();
            String ip = getIp();
            log.warn("{}|请求用户:{},异常返回ip:{},class:{},method:{},异常内容:{}|", traceId, user, ip, className.getName(), methodName, e.getMessage());
            if (method.isAnnotationPresent(Record.class)) {
                if (checkAndUpdateLog(method, traceId, e.getMessage(), "失败")) {
                    return;
                }
            }
        } catch (Exception e1) {
            log.error("日志记录失败", e1);
        }
    }


    public String getUser() {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        if (request == null) {
            return "admin9";
        }
        String account = request.getParameter("passport");
        if (StringUtils.isEmpty(account)) {
            UserSession userSession = getUserSession(request);
            if (userSession != null) {
                return userSession.getUserName();
            }
            return "";
        }

        return request.getParameter("passport");
    }

    public String getIp() {
        return ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest().getRemoteAddr();
    }

    private String getTraceId() {
        return ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest().getHeader(GlobalConstants.TRACE_ID);
    }

    public UserSession getUserSession(HttpServletRequest request) {
        return this.httpHelper.getUser(request);
    }




}
