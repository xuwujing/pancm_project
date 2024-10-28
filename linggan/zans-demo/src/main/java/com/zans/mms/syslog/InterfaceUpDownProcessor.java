package com.zans.mms.syslog;

import com.zans.base.vo.ApiResult;
import com.zans.mms.service.IAlertRuleService;
import com.zans.mms.service.IAssetService;
import com.zans.mms.vo.alert.AlertRecordRespVO;
import com.zans.mms.vo.asset.SwitcherMacInterfaceResVO;
import com.zans.mms.websocket.WsSessionCacheManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static com.zans.mms.config.DemoKitConstants.*;

/**
 * @author xv
 * @since 2022/6/29 9:40
 */
@Service
@Slf4j
public class InterfaceUpDownProcessor implements ISyslogProcessor {

    @Value("${syslog.filter.keyword}")
    private String filterKeyword;

    @Value("${syslog.filter.ip}")
    private String filterIp;

    @Value("${syslog.time.offWait:2}")
    private long offWait;

    @Value("${syslog.time.onlineWait:3}")
    private long onlineWait;

    @Value("${syslog.time.onlineWaitCount:3}")
    private long onlineWaitCount;



    @Autowired
    private IAssetService assetService;

    @Autowired
    private IAlertRuleService alertRuleService;

    /**
     *
     * @param syslogEntity
     * @return true, match and process
     */
    @Override
    public boolean processMessage(SyslogEntity syslogEntity) {

        String sourceIp = syslogEntity.getSourceIp();
        if (!filterIp.equals(syslogEntity.getSourceIp())) {
            log.info("接收到日志：ip#{}, ignore", sourceIp);
            return false;
        }

        String msg = syslogEntity.getMessage();
        if (!msg.contains(filterKeyword)) {
//            log.info("接收到日志：ip#{}, ignore msg filterKeyword, {}", sourceIP, msg);
            return false;
        }

        if (!msg.contains(INTERFACE_TURN)) {
//            log.info("接收到日志：ip#{}, ignore msg INTERFACE_TURN, {}", sourceIP, msg);
            return false;
        }

        int idxBegin = msg.indexOf(filterKeyword);
        String prefix = msg.substring(idxBegin + filterKeyword.length());
        int idxEnd = prefix.indexOf(" ");
        String itf = prefix.substring(0, idxEnd);
        String pId = getPortId(itf);

        // TODO add business logic, to send websocket msg to front
        if (msg.contains(INTERFACE_UP)) {
            log.info("接收到日志：interface#{} UP, {}", itf, msg);
            SwitcherMacInterfaceResVO switcherMacInterfaceResVO = assetService.getSwitchMacInterfaceByPort(pId);
            if(switcherMacInterfaceResVO == null || StringUtils.isEmpty(switcherMacInterfaceResVO.getIpAddr()) ){
                for (long i = 1; i <= onlineWaitCount; i++) {
                    log.info("未查询到该数据，进行休眠重试:{} 次 !!! 当前count:{}",onlineWaitCount,i);
                    try {
                        TimeUnit.SECONDS.sleep(onlineWait);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    switcherMacInterfaceResVO = assetService.getSwitchMacInterfaceByPort(pId);
                    if(switcherMacInterfaceResVO!=null && !StringUtils.isEmpty(switcherMacInterfaceResVO.getIpAddr())){
                        log.info("第:{}次查询到了数据！循环结束！数据:{}",i,switcherMacInterfaceResVO);
                        break;
                    }
                }

                if(switcherMacInterfaceResVO == null || StringUtils.isEmpty(switcherMacInterfaceResVO.getIpAddr())){
                    log.error("准入没有该PID:{}数据,不做处理!msg:{}",pId,msg);
                    return false;
                }

            }
            String ipAddr = switcherMacInterfaceResVO.getIpAddr();
            String mac = switcherMacInterfaceResVO.getMac();
            String alertMsg = "端口 %s 的设备已插入，ip:%s,mac: %s 设备已恢复在线！请检查！";
            List<SwitcherMacInterfaceResVO> list =assetService.getSwitchMacInterface();
            int ruleId = 32;
            String businessId = ruleId+"-"+ipAddr;
            alertMsg = String.format(alertMsg,itf,ipAddr,mac);
            alertRuleService.saveAlertData(ruleId,businessId,ipAddr,mac,alertMsg);
            List<AlertRecordRespVO> alertList =  alertRuleService.getAlertRecordData();
            Map<String,Object> map = new HashMap<>();
            map.put("mac_data",list);
            map.put("alert_data",alertList);
            map.put("alert_msg",alertMsg);
            String data = ApiResult.success(map).toString();
            //数据写入和ws推送
            WsSessionCacheManager.sendMsg(WsSessionCacheManager.SESSION_KEY,data);

        } else if (msg.contains(INTERFACE_DOWN)) {
            log.info("接收到日志：interface#{} DOWN, {}", itf, msg);
            SwitcherMacInterfaceResVO switcherMacInterfaceResVO = assetService.getSwitchMacInterfaceByPort(pId);
            if(switcherMacInterfaceResVO == null || StringUtils.isEmpty(switcherMacInterfaceResVO.getIpAddr()) ){
                log.error("DOWN操作，没有数据直接结束! 准入没有该PID:{}数据,不做处理!msg:{}",pId,msg);
                return false;
            }
            String ipAddr = switcherMacInterfaceResVO.getIpAddr();
            String mac = switcherMacInterfaceResVO.getMac();

            String alertMsg = "端口 %s 的设备已拔出，ip:%s ,mac: %s 设备离线！请检查！";

            //离线
            try {
                TimeUnit.SECONDS.sleep(offWait);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            assetService.resetStatusByMac(mac);

            //数据写入和ws推送

            List<SwitcherMacInterfaceResVO> list =assetService.getSwitchMacInterface();

            list.removeIf(sVO -> sVO.getPId().equals(pId));
            Map<String,Object> map = new HashMap<>();
            map.put("mac_data",list);
            alertMsg = String.format(alertMsg,itf,ipAddr,mac);
            map.put("alert_msg",alertMsg);
            int ruleId = 31;
            String businessId = ruleId+"-"+ipAddr;
            alertRuleService.saveAlertData(ruleId,businessId,ipAddr,mac,alertMsg);
            List<AlertRecordRespVO> alertList =  alertRuleService.getAlertRecordData();
            map.put("alert_data",alertList);
            String data = ApiResult.success(map).toString();
            //数据写入和ws推送
            WsSessionCacheManager.sendMsg(WsSessionCacheManager.SESSION_KEY,data);

        } else {
            return false;
        }

        return true;
    }

    private String getPortId(String id){
        String portId= "port="+id;
        return portId;
    }


}
