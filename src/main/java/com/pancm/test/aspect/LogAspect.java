package com.pancm.test.aspect;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import com.github.pagehelper.StringUtil;
import com.pancm.test.annotation.LogAnnotation;
import com.pancm.util.HttpServletRequestUtil;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.MDC;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.UUID;


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
    // 使用ThreadLocal存储请求唯一ID
    private static final ThreadLocal<String> requestId = ThreadLocal.withInitial(() -> null);

    /**
     * @Title: logExecution
     * @Description: 切入点表达式
     */
    @Pointcut("@annotation(com.pancm.test.annotation.LogAnnotation)")
    public void ponitcut() {
    }

    /**
     * @param joinPoint
     * @Title: before
     * @Description: 前置通知处理方法
     */
    @Before("ponitcut()")
    public void before(JoinPoint joinPoint) throws NoSuchMethodException {
        //生成唯一ID并存储到ThreadLocal
        String uniqueId = UUID.randomUUID().toString().replaceAll("-", "");
        requestId.set(uniqueId);
        // 将唯一ID放入MDC，以便在日志中全局可见
        MDC.put("requestId", uniqueId);
        Class<?> className = joinPoint.getTarget().getClass();
        String methodName = joinPoint.getSignature().getName();
        String path = className.getName().concat("#").concat(methodName);
        log.info("{}|正常请求,请求IP:{},请求人:{},请求路径:{},\n请求内容:{}", uniqueId, HttpServletRequestUtil.getClientIp(), "", path, joinPoint.getArgs());
        Class<?>[] argClass = ((MethodSignature) joinPoint.getSignature()).getParameterTypes();
        Method method = className.getMethod(methodName, argClass);
        LogAnnotation annotation = method.getAnnotation(LogAnnotation.class);
        String businessNo = annotation.businessNo();
        if (StringUtil.isNotEmpty(businessNo)) {
            try {
                //非JSON获取
                int businessNoIndex = annotation.businessNoIndex();
                if (businessNoIndex > -1) {
                    String businessNoValue = String.valueOf(joinPoint.getArgs()[businessNoIndex]);
                } else {
                    //JSON获取,只取第一条
                    for (Object arg : joinPoint.getArgs()) {
                        JSONObject jsonObject = (JSONObject)  JSON.toJSON(arg);
                        if (jsonObject.containsKey(businessNo)) {
                            String businessNoValue = jsonObject.getString(businessNo);
                            break;
                        }
                    }
                }
            } catch (Exception e) {
                log.error("业务编号获取异常", e);
            }
        }

    }

    /**
     * @param joinPoint
     * @param
     * @throws SecurityException
     * @throws NoSuchMethodException
     * @Title: doAfterThrowing
     * @Description: 后置通知处理方法
     */
    @AfterReturning(pointcut = "ponitcut()", returning = "returnValue")
    public void doafterReturning(JoinPoint joinPoint, Object returnValue) {
        // 获取存储的唯一ID
        String uniqueId = requestId.get();
        Class<?> className = joinPoint.getTarget().getClass();
        String methodName = joinPoint.getSignature().getName();
        String path = className.getName().concat("#").concat(methodName);
        log.info("{}|正常返回,返回路径:{},\n请求内容:{},\n返回内容:{}", uniqueId,path, joinPoint.getArgs(), JSONObject.toJSONString(returnValue));
    }


    /**
     * @param joinPoint
     * @param e
     * @Title: doAfterThrowing
     * @Description: 异常通知处理方法
     */
    @AfterThrowing(pointcut = "ponitcut()", throwing = "e")
    public void doAfterThrowing(JoinPoint joinPoint, Throwable e) {
        String uniqueId = requestId.get();
        Class<?> className = joinPoint.getTarget().getClass();
        String methodName = joinPoint.getSignature().getName();
        String path = className.getName().concat("#").concat(methodName);
        log.error("{}|异常返回,请求IP:{},返回路径:{},\n返回内容:{},\n异常信息:{},",uniqueId, HttpServletRequestUtil.getClientIp(), path, joinPoint.getArgs(), e.getMessage());
    }




    /**
     * 清除ThreadLocal中的请求ID
     */
    @After("ponitcut()")
    public void cleanUp() {
        requestId.remove();
        MDC.remove("requestId");
    }

}
