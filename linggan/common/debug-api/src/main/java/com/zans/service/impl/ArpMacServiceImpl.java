package com.zans.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.zans.config.Constants;
import com.zans.config.redis.RedisUtil;
import com.zans.config.RestTemplateHelper;
import com.zans.dao.NetworkSwitcherInterfaceDao;
import com.zans.dao.RadiusEndpointDao;
import com.zans.dao.SysConstantDao;
import com.zans.model.NetworkSwitcherInterface;
import com.zans.model.RadiusEndpoint;
import com.zans.model.SysConstant;
import com.zans.service.IArpMacService;
import com.zans.util.MyTools;
import com.zans.vo.ApiResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.zans.config.Constants.ACCESS_ACCESS_MODE_ACL;
import static com.zans.config.Constants.API_SW_ARP;

/**
 * @author beixing
 * @Title: debug-api
 * @Description:
 * @Version:1.0.0
 * @Since:jdk1.8
 * @date 2022/3/15
 */
@Service
@Slf4j
public class ArpMacServiceImpl implements IArpMacService {

    @Resource
    private SysConstantDao sysConstantDao;

    @Resource
    private NetworkSwitcherInterfaceDao networkSwitcherInterfaceDao;

    @Resource
    private RadiusEndpointDao radiusEndpointDao;

    @Resource
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private RedisUtil redisUtil;


    @Autowired
    private RestTemplateHelper restTemplateHelper;


    @Override
    public void processResult() {
        /**
         1.

         */
        List<JSONObject> arpIpLists = new ArrayList<>();
        String now = MyTools.getNowTime();
        SysConstant sysConstant = sysConstantDao.queryByKey(Constants.NETWORK_API_HOST);
        SysConstant edgeAccessModeConstant = sysConstantDao.queryByKey(Constants.ACCESS_MODE);
        String url = sysConstant.getConstantValue().concat(API_SW_ARP);
        ApiResult api = restTemplateHelper.getForObject(url);
        if (api.getCode() != 0) {
            log.error("请求network_api失败！请求参数:{}", url);
            return;
        }
        JSONObject jsonObject = MyTools.toJson(api.getData());
        JSONArray jsonArray = jsonObject.getJSONArray("data");
        for (Object o : jsonArray) {
            JSONObject jsonObject1 = (JSONObject) o;
            String ip = jsonObject1.getString("ip");
            String mac = formatMacTo12Bit(jsonObject1.getString("mac"));
            String vlan = jsonObject1.getString("vlan");
            String dynamic = jsonObject1.getString("dynamic");
            String itfDetail = jsonObject1.getString("interface");
            if (StringUtils.isEmpty(dynamic)) {
                continue;
            }
            if (StringUtils.isEmpty(ip)) {
                continue;
            }
            if (StringUtils.isEmpty(mac) || mac.length() < 12) {
                continue;
            }
            JSONObject object = new JSONObject();
            object.put("mac", mac);
            object.put("ip", ip);
            object.put("vlan", vlan);
            object.put("itfDetail", itfDetail);
            arpIpLists.add(object);
        }
        String swIp = "";
        List<NetworkSwitcherInterface> interfaceList = networkSwitcherInterfaceDao.findItfLimitList(swIp);
        List<RadiusEndpoint> radiusEndpoints = radiusEndpointDao.findAllEndpointOfSwitcher(swIp);
        List<String> macList = new ArrayList<>();
        for (RadiusEndpoint radiusEndpoint : radiusEndpoints) {
            macList.add(radiusEndpoint.getMac());
        }

        for (JSONObject arpIpList : arpIpLists) {
            String mac = arpIpList.getString("mac");
            String ip = arpIpList.getString("ip");
            String itfDetail = arpIpList.getString("itfDetail");
            String vlan = arpIpList.getString("vlan");
            if (macList.contains(mac)) {
                macList.remove(mac);
            }
            // todo 黑名单逻辑

            //保存到 network_arp表
            processArp(swIp,ip,mac,itfDetail,vlan,now);
            String nasIpAddress = null;
            String nasPortId = null;
            Integer qzEnable = null;
            Integer assetEnable = null;
            //判断是否是acl模式
            String edgeAccessMode = edgeAccessModeConstant.getConstantValue();
            if(ACCESS_ACCESS_MODE_ACL.equals(edgeAccessMode)){
                String networkSql = String.format("SELECT nsi.sw_ip nas_ip ,nsi.interface_detail," +
                        " nn.qz_enable,nn.asset_enable " +
                        "   FROM  network_switcher_mac nsi " +
                        "     INNER JOIN network_nas nn ON nsi.sw_ip = nn.nas_ip" +
                        "      WHERE nsi.mac = %s and nsi.mac_alive=1 " +
                        "       ORDER BY nn.weight asc,nsi.update_time desc LIMIT 1 ");
               Map<String, Object>  networkMap =  jdbcTemplate.queryForMap(networkSql,mac);
               if(!MyTools.isEmpty(networkMap)){
                   nasIpAddress = (String) networkMap.get("nas_ip");
                   nasPortId = (String) networkMap.get("interface_detail");
                   qzEnable = (Integer) networkMap.get("qz_enable");
                   assetEnable = (Integer) networkMap.get("asset_enable");
                   if(StringUtils.isEmpty(nasIpAddress)){
                       log.warn("nas_ip is null sw_ip#{}, mac#{}, ip#{}",swIp,mac,ip);
                       continue;
                   }
               }
                processAsset(swIp,ip,mac,itfDetail,interfaceList);


            }
        }

    }


    private void processAsset(String swIp, String ip, String mac, String itfDetail, List<NetworkSwitcherInterface> interfaceList){

    }



    private void processArp(String swIp, String ip, String mac, String inf, String vlan, String now) {
        String networkArp = String.format("select * from network_arp where mac=%s", mac);
        Map<String, Object> map = jdbcTemplate.queryForMap(networkArp);
        String arp = "arp";
        //默认是arp，不知道值从哪来，先放着
        String aliveMode = "";
        //todo
        if(arp.equals(aliveMode)){
            //写到redis中 scan-woker 的arp_mac_task,187行

        }



        if (MyTools.isEmpty(map)) {
            //存储network_arp的数据和network_arp_change的数据
            String saveNetworkArp = String.format("insert into network_arp(mac, arp_ip, interface_detail, vlan, sw_ip, " +
                    "              alive_arp, alive_last_time) " +
                    "              values(%s,%s,%s,%s,%s, %s,%s)", mac, ip, inf, vlan, swIp, 1, now);
            jdbcTemplate.execute(saveNetworkArp);
            //存储network_arp的数据和network_arp_change的数据
            saveNetWorkArpChange(swIp, ip, mac, inf, vlan, now);
            return;
        }
        String baseIp = (String) map.get("arp_ip");
        String baseVlan = (String) map.get("vlan");
        String baseSwIp = (String) map.get("sw_ip");
        String baseInf = (String) map.get("interface_detail");
        final boolean flag = ip.equals(baseIp) && inf.equals(baseInf)
                && vlan.equals(baseVlan) && swIp.equals(baseSwIp);
        if (flag) {
            updateNetWorkArp(mac, now);
            return;
        }
        int hour = 6;
        String countArpSql =  String.format("select count(1) as c from network_arp_change where mac=%s " +
                "       and create_time>=DATE_SUB(NOW(), INTERVAL %d HOUR)",mac,hour);
        Map<String, Object> countMap = jdbcTemplate.queryForMap(countArpSql);
        if(!MyTools.isEmpty(countMap)){
            saveNetWorkArpChange(swIp, ip, mac, inf, vlan, now);
            notifyArpScan(mac,ip);
        }else {
            log.error("arp#{}, {}, {} change quickly",mac,ip,swIp);
        }
        updateNetWorkArp(mac, now);
    }



    private  void notifyArpScan(String mac,String ip){
        String arpSql =  String.format("select p.asset_id as id, p.* from asset_profile p where p.ip_addr=%s",ip);
        Map<String, Object> countMap = jdbcTemplate.queryForMap(arpSql);
        if(MyTools.isEmpty(countMap) || countMap.get("asset_id") == null){
            return;
        }
        boolean baseChange = false;
        String assetId = (String) countMap.get("asset_id");
        String queueDeviceInsightArp = "device_insight_arp";
        String aliveTypeAssetStatic = "asset";
        String parm = assetId+"#"+mac+"#"+ip+"#"+aliveTypeAssetStatic+"#"+baseChange;
        String queueSql =  String.format("select id from queue_ip where name=%s and ip_addr=%s and status=%s",
                queueDeviceInsightArp,ip,0);
        Map<String, Object> queueMap = jdbcTemplate.queryForMap(queueSql);
        if(MyTools.isEmpty(queueMap)){
            return;
        }
        String saveSql = String.format("insert into queue_ip(name, ip_addr, params, status) values(%s, %s, %s, %s)",
                queueDeviceInsightArp,ip,parm,0);
        jdbcTemplate.execute(saveSql);
    }


    private void updateNetWorkArp(String mac, String nowTime) {
        String updateSql = String.format("update network_arp set alive_arp=%s, alive_last_time=%s where mac=%s",
                1, nowTime, mac);
        jdbcTemplate.execute(updateSql);
    }

    private void saveNetWorkArpChange(String swIp, String ip, String mac, String inf, String vlan, String now) {
        String saveNetworkArpChange = String.format("insert into network_arp_change(mac, arp_ip, interface_detail, " +
                        "vlan, sw_ip, cur_arp_ip, cur_interface_detail, cur_vlan, cur_sw_ip," +
                        "  create_time, update_time) " +
                        "  values(%s,%s,%s,%s,%s, %s,%s,%s,%s, %s,%s)", mac, ip, inf, vlan, swIp,
                ip, inf, vlan, swIp,
                now, now);
        jdbcTemplate.execute(saveNetworkArpChange);
    }


    private String formatMacTo12Bit(String mac) {
        return mac.replace(" ", "")
                .replace("-", "")
                .replace(":", "")
                .toLowerCase();
    }

}
