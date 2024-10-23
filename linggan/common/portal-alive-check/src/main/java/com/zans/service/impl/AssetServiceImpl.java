package com.zans.service.impl;

import com.zans.dao.AssetMapper;
import com.zans.model.Asset;
import com.zans.service.IAssetService;
import com.zans.util.PingUtil;
import com.zans.vo.AliveHeartbeatVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.ListUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


@Service("assetService")
@Slf4j
public class AssetServiceImpl implements IAssetService {
    @Autowired
    AssetMapper assetMapper;


    @Override
    public void checkAlive() {
        long s1 = System.currentTimeMillis();
        List<String> ipList = new ArrayList<>();
        String uuid = UUID.randomUUID().toString();
        log.info("uuid:{},开始进行资产存活数据扫描...", uuid);
        saveCount = 0;
        updateCount = 0;
        try {
            ipList = handleAliveData();
        } catch (Exception e) {
            log.error("处理失败!", e);
        }
        long s2 = System.currentTimeMillis();
        log.info("uuid:{},资产存活数据扫描:{}条数据,新增:{}条，更新:{}条,耗时time#{}s", uuid, ipList.size(), saveCount, updateCount, (s2 - s1) / 1000);

//        handleAliveData();
    }

    private List<String> handleAliveData() {
        List<String> ipLists = assetMapper.getIpAll();
//        List<String> ipLists = assetMapper.getIpAllByRadiusPoint();
        List<List<String>> partitionList = ListUtils.partition(ipLists, pingSize);
        for (List<String> list : partitionList) {
            if (isPing == 1) {
                pingDeal(list);
                continue;
            }

            if (isFping == 1) {
                fpingDeal(list);
            }
        }

        return ipLists;
}

    private void fpingDeal(List<String> list) {
        List<String> fpingRet = PingUtil.fping(list, pingCount, pingTimeout);
        log.info("fpingRet:{}", fpingRet);
        for (String ret : fpingRet) {
            String[] split = ret.split(";");
            String ip = split[0];
            Integer rcv = Integer.parseInt(split[1]);
            int nowOnlineStr = rcv > 0 ? 1 : 2;
            AliveHeartbeatVO heartbeatVO = assetMapper.findAliveHeartBeatByIp(ip);

            if(nowOnlineStr!=1){
                Asset asset = assetMapper.getEndpointByIp(ip);
                if(asset!=null && asset.getDeviceType()!=null){
                    if("18".equals(asset.getDeviceType())){
                        log.warn("检测到交换机类型18,确认为在线!");
                        nowOnlineStr =1;
                    }
                }
            }


            if (heartbeatVO == null) {
                assetMapper.saveAliveHeartBeat(ip, nowOnlineStr);
                saveCount++;
            } else {
                assetMapper.updateAliveHeartBeat(ip, nowOnlineStr);
                updateCount++;
            }
        }
    }

    private void pingDeal(List<String> list) {
        for (int i = 0; i < list.size(); i++) {
            String ip = list.get(i);
            boolean isAlive = PingUtil.ping(list.get(0), pingCount, pingTimeout);
            log.info("ip:{},isAlive:{}",ip, isAlive);
            int nowOnlineStr = isAlive ? 1 : 2;
            AliveHeartbeatVO heartbeatVO = assetMapper.findAliveHeartBeatByIp(ip);
            if (heartbeatVO == null) {
                assetMapper.saveAliveHeartBeat(ip, nowOnlineStr);
                saveCount++;
            } else {
                assetMapper.updateAliveHeartBeat(ip, nowOnlineStr);
                updateCount++;
            }
        }
    }


    @Value("${ping.timeout:3000}")
    int pingTimeout;
    @Value("${ping.size:100}")
    int pingSize;
    @Value("${ping.count:5}")
    int pingCount;
    @Value("${ping.isPing:1}")
    int isPing;
    @Value("${ping.isFping:0}")
    int isFping;

    static int saveCount = 0;
    static int updateCount = 0;


}
