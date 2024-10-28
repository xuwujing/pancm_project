package com.zans.mms.websocket;

import com.zans.base.vo.ApiResult;
import com.zans.mms.config.SpringBeanFactory;
import com.zans.mms.service.IAlertRuleService;
import com.zans.mms.service.IAssetService;
import com.zans.mms.vo.alert.AlertRecordRespVO;
import com.zans.mms.vo.asset.SwitcherMacInterfaceResVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.lang.reflect.Field;
import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@ServerEndpoint("/demoWs")
@Component
@Slf4j
public class WsServerEndpoint {



    IAssetService assetService =(IAssetService) SpringBeanFactory.getBean("assetService");
    IAlertRuleService alertRuleService =(IAlertRuleService) SpringBeanFactory.getBean("alertRuleService");

    /**
     * 连接成功
     *
     * @param session
     */
    @OnOpen
    public void onOpen(Session session) {
//        String ip = getRemoteAddress(session).toString();
        log.info("连接成功!");
        WsSessionCacheManager.add(WsSessionCacheManager.SESSION_KEY,session);
        List<SwitcherMacInterfaceResVO> list =  assetService.getSwitchMacInterface();
        List<AlertRecordRespVO> alertList =  alertRuleService.getAlertRecordData();
        Map<String,Object> map = new HashMap<>();
        map.put("mac_data",list);
        map.put("alert_data",alertList);
        String data = ApiResult.success(map).toString();
        WsSessionCacheManager.sendMsg(WsSessionCacheManager.SESSION_KEY,data);
    }

    /**
     * 连接关闭
     *
     * @param session
     */
    @OnClose
    public void onClose(Session session) {
//        String ip = getRemoteAddress(session).toString();
//        log.info("IP:{},连接退出!",ip);
        log.info("连接退出!");
        WsSessionCacheManager.removeAndClose(WsSessionCacheManager.SESSION_KEY);
    }

    /**
     * 接收到消息
     *
     * @param text
     */
    @OnMessage
    public String onMsg(String text) throws IOException {
        log.info("收到消息!text:{}",text);
        return "suc";
    }

    public static InetSocketAddress getRemoteAddress(Session session) {
        if (session == null) {
            return null;
        }
        RemoteEndpoint.Async async = session.getAsyncRemote();

        //在Tomcat 8.0.x版本有效
//		InetSocketAddress addr2 = (InetSocketAddress) getFieldInstance(async,"base#sos#socketWrapper#socket#sc#remoteAddress");
        //在Tomcat 8.5以上版本有效
        InetSocketAddress addr = (InetSocketAddress) getFieldInstance(async, "base#socketWrapper#socket#sc#remoteAddress");
        return addr;
    }

    private static Object getFieldInstance(Object obj, String fieldPath) {
        String fields[] = fieldPath.split("#");
        for (String field : fields) {
            obj = getField(obj, obj.getClass(), field);
            if (obj == null) {
                return null;
            }
        }

        return obj;
    }

    private static Object getField(Object obj, Class<?> clazz, String fieldName) {
        for (; clazz != Object.class; clazz = clazz.getSuperclass()) {
            try {
                Field field;
                field = clazz.getDeclaredField(fieldName);
                field.setAccessible(true);
                return field.get(obj);
            } catch (Exception e) {
            }
        }

        return null;
    }

}
