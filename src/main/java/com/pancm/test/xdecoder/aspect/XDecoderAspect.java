package com.pancm.test.xdecoder.aspect;


import com.alibaba.fastjson.JSONObject;

import com.github.pagehelper.StringUtil;
import com.pancm.test.xdecoder.AESUtil;
import com.pancm.test.xdecoder.vo.Result;
import com.pancm.test.xdecoder.vo.XDecoderVO;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.MDC;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;


/**
 * @Author pancm
 * @Description 加解密注解
 * @Date 2020/8/13
 * @Param
 * @return
 **/
@Aspect
@Component
@Slf4j
@Order(999)
public class XDecoderAspect {
    // 使用ThreadLocal存储请求唯一ID
    private static final ThreadLocal<String> traceId = ThreadLocal.withInitial(() -> null);
    private static final ThreadLocal<String> encryptedKey = ThreadLocal.withInitial(() -> null);

    private static final String ALIAS = "insurance_frontend";
    private static final String AES_ENCRYPTED_KEY = "X-Encrypted-Key";

    private static final String TRACE_ID = "trace_id";

    @Resource
//    private KmsClient kmsClient;

    /**
     * @Title: logExecution
     * @Description: 切入点表达式
     */
    @Pointcut("@annotation(com.pancm.test.xdecoder.annotation.XDecoderAnnotation)")
    public void ponitcut() {
    }

    /**
     * @param joinPoint
     * @Title: before
     * @Description: 前置通知处理方法
     */
    @Before("ponitcut()")
    public void before(JoinPoint joinPoint) {
        //生成唯一ID并存储到ThreadLocal
        setTraceId();
        decoder(joinPoint);
    }




    @Around("ponitcut()")
    public Object doAround(ProceedingJoinPoint joinPoint) {
        Object returnValue = null;
        try {
            returnValue = joinPoint.proceed();
            String uniqueId = traceId.get();
            if (returnValue instanceof Result) {
                Result result = (Result) returnValue;
                Object data = result.getData();
                String repData = JSONObject.toJSONString(data);
                String aesKey = encryptedKey.get();
                String encryptedData = encryptedData(aesKey, repData);
                result = Result.ok(encryptedData);
                Map<String, String> map = new HashMap<>();
                map.put(TRACE_ID, uniqueId);
                result.meta(map);
                return result;
            }

        } catch (Throwable e) {
            log.error("处理请求失败", e);
            throw new RuntimeException("处理请求失败");
        } finally {
            cleanUp();
        }

        return returnValue;
    }





    private void decoder(JoinPoint joinPoint) {
        for (Object arg : joinPoint.getArgs()) {
            if (arg instanceof XDecoderVO) {
                XDecoderVO vo = (XDecoderVO) arg;
                String encryptionJson = vo.getEncryptionJson();
                if (StringUtil.isEmpty(encryptionJson)) {
                    throw new RuntimeException("加密字段为空");
                }
                try {
                    String encryptAesKey = getEncryptAesKey();
//                    String aesKey = kmsClient.decrypt(encryptAesKey, ALIAS);
                    String aesKey = "";
                    encryptedKey.set(aesKey);
                    String json = decryptedData(aesKey, encryptionJson);
                    JSONObject jsonObject = JSONObject.parseObject(json);
                    setFields(vo, jsonObject);
                } catch (Exception e) {
                    log.error("前端接口解密失败", e);
                    throw new RuntimeException("解密失败！请求参数数据不符");
                }
            }
        }
    }

    private void setFields(XDecoderVO xDecoderVO, JSONObject jsonObject) {
        Field[] fields = xDecoderVO.getClass().getDeclaredFields();
        for (Field field : fields) {
            if (jsonObject.containsKey(field.getName())) {
                boolean accessible = field.isAccessible();
                try {
                    field.setAccessible(true);
                    field.set(xDecoderVO, jsonObject.get(field.getName()));
                } catch (IllegalAccessException e) {
                    throw new RuntimeException("Error setting field value: " + field.getName(), e);
                } finally {
                    field.setAccessible(accessible);
                }
            }
        }
    }

    private void setTraceId() {
        String uniqueId = getHttpServletRequest().getHeader(TRACE_ID);
        traceId.set(uniqueId);
        // 将唯一ID放入MDC，以便在日志中全局可见
        MDC.put(TRACE_ID, uniqueId);
    }

    private String getEncryptAesKey() {
        HttpServletRequest request = getHttpServletRequest();
        String encryptAesKey = request.getHeader(AES_ENCRYPTED_KEY);
        return encryptAesKey;
    }

    private HttpServletRequest getHttpServletRequest() {
        ServletRequestAttributes attrs = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attrs.getRequest();
        return request;
    }


    private static String encryptedData(String key, String data) {
        return AESUtil.encrypt(data, key);
    }

    private String decryptedData(String key, String data) {
        return AESUtil.decrypt(data, key);
    }


    /**
     * 清除ThreadLocal中的请求ID
     */
    public void cleanUp() {
        traceId.remove();
        encryptedKey.remove();
        MDC.remove(TRACE_ID);
    }


}
