package com.zans.service.impl;

import com.zans.dao.RadiusEndpointProfileDao;
import com.zans.model.RadiusEndpointProfile;
import com.zans.model.SysConstant;
import com.zans.service.IAAAService;
import com.zans.service.IBaseLineService;
import com.zans.util.MyTools;
import com.zans.vo.ApiResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static com.zans.config.Constants.*;
import static com.zans.config.Constants.AAA_SYNC_ENDPOINT_DEST_NAME;
import static com.zans.config.SQLConstants.*;

/**
 * @author beixing
 * @Title: debug-api
 * @Description: aaa系列任务实现类
 * @Version:1.0.0
 * @Since:jdk1.8
 * @date 2022/3/19
 */
@Service
@Slf4j(topic = "aaa")
public class AAAServiceImpl extends AAABaseServiceImpl implements IAAAService {

    @Autowired
    private IBaseLineService baseLineService;

    @Resource
    @Qualifier("redisAcctTemplate")
    private RedisTemplate acctTemplate;

    @Resource
    @Qualifier("redisAliveTemplate")
    private RedisTemplate aliveTemplate;


    @Resource
    private RadiusEndpointProfileDao radiusEndpointProfileDao;

    @Value("${spring.redis.acct.host:1}")
    private String acctHost;

    @Override
    public ApiResult syncQzTask() {
        List<Map<String, Object>> mapList = super.getSyncData(AAA_SYNC_QZ_MARK_NAME, AAA_SYNC_QZ_DEST_NAME, AAA_SYNC_QZ_TYPE);
        if (MyTools.isEmpty(mapList)) {
            return ApiResult.success();
        }
        List<String> macBlackList = super.getMacBlackList();
        String maxTime = null;
        for (Map<String, Object> map : mapList) {
            maxTime = String.valueOf(map.get(AAA_TIMESTAMP_COLUMN));
            String ip = (String) map.get("framed_ip_address");
            String mac = (String) map.get("username");
            String filterId = (String) map.get("filter_id");
            int policy = ACCESS_POLICY_QZ;
            final boolean flag = isMacValid(mac, macBlackList);
            if (!flag) {
                log.warn("qz invalid mac#{},ip#{},macBlackList#{}", mac, ip, macBlackList);
                continue;
            }
            // 白名单的list校验
            final boolean isIp = isIpValid(ip);
            if (!isIp) {
                log.warn("qz invalid ip#{},macBlackList#{}", ip, macBlackList);
                continue;
            }
            if (!MyTools.isEmpty(filterId)) {
                policy = Integer.valueOf(filterId);
            }
            processEndpoint(policy, map, mac, ip);
        }
        super.updateSyncData(AAA_SYNC_QZ_MARK_NAME, AAA_SYNC_QZ_DEST_NAME, maxTime);
        return ApiResult.success(maxTime);
    }


    private void processEndpoint(int policy, Map<String, Object> map, String mac, String ip) {
        boolean needAlert = false;
        Map<String, Object> endpoint = super.findRadiusEndpointByMac(mac);
        if (MyTools.isEmpty(endpoint)) {
            needAlert = true;
            long id = super.saveRadiusEndpoint(mac, policy);
            saveEndpointProfile(policy, map, mac, ip, id);
        } else {
            int id = (int) endpoint.get("id");
            String baseIp = (String) endpoint.get("baseIp");
            if (MyTools.isEmpty(baseIp)) {
                needAlert = true;
            } else {
                //基线处理
                baseLineService.checkBaseLine(map);
            }
            super.updateRadiusEndpoint(id, policy);
            Map<String, Object> endpointMap = super.findRadiusEndpointProfileById(id);
            if (MyTools.isEmpty(endpointMap)) {
                saveEndpointProfile(policy, map, mac, ip, id);
            }
            String nasIp = (String) map.get("nas_ip");
            String swIp = (String) map.get("sw_ip");
            String findNasIPSql = String.format(SELECT_SQL_NETWORK_NAS, nasIp);
            Map<String, Object> objectMap = super.queryForMap(findNasIPSql);
            int qzEnable = (int) objectMap.get("qz_enable");
            int assetEnable = (int) objectMap.get("asset_enable");
            final boolean isDoBaseLine = assetEnable == NETWORK_NAS_ASSET_ENABLE && qzEnable == NETWORK_NAS_QZ_UNABLE && needAlert;
            if (isDoBaseLine) {
                String nasPortId = (String) map.get("nas_port_id");
                String vlan = "";
                if (MyTools.isNotEmpty(nasPortId)) {
                    nasPortId = nasPortId.replace("=3D", "=").replace("=3B", ";");
                    vlan = super.getVlan(nasPortId);
                }
                //建立基线
                baseLineService.doBaseLine(swIp, nasPortId, vlan, ip, mac);
            }
            if (needAlert) {
                String nasName = (String) map.get("nas_identifier");
                super.sendAlertNewEndpoint(nasName, nasIp, ip, mac);
            }
        }

    }

    private void saveEndpointProfile(int policy, Map<String, Object> map, String mac, String ip, long id) {
        String nasPortId = (String) map.get("nas_port_id");
        String nasIp = (String) map.get("nas_ip");
        String swIp = (String) map.get("sw_ip");
        String replyMessage = (String) map.get("reply_message");
        String createTime = String.valueOf(map.get("create_time"));
        String acctSessionId = (String) map.get("acct_session_id");
        String filterId = (String) map.get("filter_id");
        String vlan = super.getVlan(nasPortId);
        super.saveRadiusEndpointProfile(id, mac, ip, nasPortId, nasIp, swIp, replyMessage, filterId, createTime, acctSessionId, vlan);
        String queueName = QUEUE_DEVICE_INSIGHT_PASS;
        String params = String.format("%s#%s#%s#%s#True", id, mac, ip, ALIVE_TYPE_ENDPOINT);
        if (policy == ACCESS_POLICY_QZ) {
            queueName = QUEUE_DEVICE_INSIGHT_QZ;
        }
        addQueueIpData(queueName, ip, params);
    }

    @Override
    public ApiResult syncAcctTask() {
        List<Map<String, Object>> mapList = super.getSyncData(AAA_SYNC_ACCT_MARK_NAME, AAA_SYNC_ACCT_DEST_NAME, AAA_SYNC_ACCT_TYPE);
        if (MyTools.isEmpty(mapList)) {
            return ApiResult.success();
        }

        for (Map<String, Object> map : mapList) {
            updateAcct(map);
            if (ENABLE_UPDATE_ENDPOINT) {
                updateEndpointProfile(map);
            }
            SysConstant sysConstant = super.sysConstantDao.queryByKey(ALIVE_MODE);
            if (ALIVE_MODE_ACCT.equals(sysConstant.getConstantValue())) {
                saveAliveHeartbeatToCache(map);
            }

        }

        return ApiResult.success();
    }


    /**
     * @return
     * @Author beixing
     * @Description 更新redis的acct的数据
     * @Date 2022/3/22
     * @Param
     **/
    private void updateAcct(Map<String, Object> map) {
        String countKey = "acct!" + MyTools.getNowTime(TIME_FORMAT3);
        acctTemplate.opsForValue().set(countKey, "radius#" + acctHost);
        String nasIp = (String) map.get("nas_ip_address");
        if (MyTools.isNotEmpty(nasIp)) {
            acctTemplate.opsForHash().increment(countKey, nasIp, 1L);
        }
        String mac = (String) map.get("username");
        mac = MyTools.formatMacTo12Bit(mac);
        String now = MyTools.getNowTime();
        String ip = (String) map.get("framed_ip_address");
        String acctSessionId = (String) map.getOrDefault("acct_session_id", "");
        String acctStopTime = (String) map.get("acct_stop_time");
        String acctStartTime = (String) map.getOrDefault("acct_start_time", "");
        String acctUpdateTime = (String) map.getOrDefault("acct_update_time", "");
        String acctTerminateCause = (String) map.get("acct_terminate_cause");
        String acctSessionTime = (String) map.getOrDefault("acct_session_time", 0);
        String name = "acct#" + mac;
        final boolean isExist = acctTemplate.hasKey(name);
        if (!isExist) {
            Map<String, Object> saveMap = new HashMap<>();
            saveMap.put("mac", mac);
            saveMap.put("ip", ip);
            saveMap.put("nas_ip", nasIp);
            saveMap.put("acct_update_time", acctUpdateTime);
            saveMap.put("acct_session_id", acctSessionId);
            saveMap.put("acct_start_time", acctStartTime);
            saveMap.put("acct_session_time", acctSessionTime);
            saveMap.put("rad_create_time", map.getOrDefault("create_time", ""));
            saveMap.put("rad_update_time", map.getOrDefault("update_time", ""));
            saveMap.put("radius_host", acctHost);
            saveMap.put("rad_acct_id", map.getOrDefault("rad_acct_id", ""));
            saveMap.put("create_time", now);
            saveMap.put("update_time", now);
            if (MyTools.isNotEmpty(acctStopTime)) {
                saveMap.put("acct_stop_time", acctStopTime);
            }
            if (MyTools.isNotEmpty(acctTerminateCause)) {
                saveMap.put("acct_terminate_cause", acctTerminateCause);
            }
            acctTemplate.opsForHash().putAll(name, saveMap);
            return;
        }
        //去掉时间比较的判断

        String acctSessionIdDb = (String) acctTemplate.opsForHash().get(name, "acct_session_id");
        Map<String, Object> updateMap = new HashMap<>();
        if (!acctSessionId.equals(acctSessionIdDb)) {
            updateMap.put("acct_stop_time", "");
            updateMap.put("acct_session_time", 0);
            updateMap.put("acct_terminate_cause", "");
        }
        updateMap.put("acct_start_time", acctStartTime);
        updateMap.put("acct_update_time", acctUpdateTime);
        updateMap.put("acct_session_time", acctSessionTime);
        updateMap.put("acct_session_id", acctSessionId);
        updateMap.put("rad_create_time", map.getOrDefault("create_time", ""));
        updateMap.put("rad_update_time", map.getOrDefault("update_time", ""));
        updateMap.put("radius_host", acctHost);
        updateMap.put("rad_acct_id", map.getOrDefault("rad_acct_id", ""));
        updateMap.put("update_time", now);
        if (MyTools.isNotEmpty(acctStopTime)) {
            updateMap.put("acct_stop_time", acctStopTime);
        }
        if (MyTools.isNotEmpty(acctTerminateCause)) {
            updateMap.put("acct_terminate_cause", acctTerminateCause);
        }
        if (MyTools.isNotEmpty(ip)) {
            updateMap.put("ip", ip);
        }
        if (MyTools.isNotEmpty(nasIp)) {
            updateMap.put("nas_ip", nasIp);
        }
        acctTemplate.opsForHash().putAll(name, updateMap);
    }

    /**
     * @return
     * @Author beixing
     * @Description 更新设备信息
     * @Date 2022/3/22
     * @Param
     **/
    private void updateEndpointProfile(Map<String, Object> map) {
        String mac = (String) map.get("username");
        mac = MyTools.formatMacTo12Bit(mac);
        String sql = String.format(SELECT_SQL_RADIUS_ENDPOINT_PROFILE_BY_MAC, mac);
        Map<String, Object> endpointMap = super.queryForMap(sql);
        if (MyTools.isEmpty(endpointMap)) {
            return;
        }

        String updateTimeDb = (String) endpointMap.get("acct_update_time");
        int endpointId = (int) endpointMap.get("endpoint_id");
        String updateTimeAcct = (String) map.get("update_time");
        String nasIp = (String) map.get("nas_ip_address");
        String ip = (String) map.get("framed_ip_address");
        String acctSessionId = (String) map.get("acct_session_id");

        String nasPortId = (String) map.get("nas_port_id");
        String vlan = "";
        String portShort = "";
        if (MyTools.isNotEmpty(nasPortId)) {
            nasPortId = nasPortId.replace("3D3D", "=").replace("3D3B", ";");
            vlan = super.getVlan(nasPortId);
            String[] portShorts = nasPortId.replace("subslot=", "").replace("slot=", "")
                    .replace("port=", "").split(TIME_SEPARATOR2);
            if (portShorts.length >= 3) {
                portShort = portShorts[0].concat(portShorts[1]).concat(portShorts[2]);
            }
        }
        Map<String, Object> updateMap = new HashMap();
        updateMap.put("endpoint_id", endpointId);
        updateMap.put("acct_update_time", updateTimeAcct);
        updateMap.put("acct_session_id", acctSessionId);
        if (MyTools.isNotEmpty(nasIp)) {
            updateMap.put("cur_nas_ip_address", nasIp);
        }
        if (MyTools.isNotEmpty(ip)) {
            updateMap.put("cur_ip_addr", ip);
        }
        if (MyTools.isNotEmpty(nasPortId)) {
            updateMap.put("cur_nas_port_id", nasPortId);
        }
        if (MyTools.isNotEmpty(vlan)) {
            updateMap.put("cur_vlan", vlan);
        }
        RadiusEndpointProfile radiusEndpointProfile = MyTools.toBean(MyTools.toString(map), RadiusEndpointProfile.class);
        radiusEndpointProfileDao.update(radiusEndpointProfile);
    }

    /**
     * @return
     * @Author beixing
     * @Description 写入存活队列
     * @Date 2022/3/23
     * @Param
     **/
    private void saveAliveHeartbeatToCache(Map<String, Object> map) {
        String mac = MyTools.formatMacTo12Bit((String) map.get("username"));
        String ipAddr = (String) map.get("framed_ip_address");
        String updateTimeAcct = (String) map.get("acct_update_time");
        String name = "alive#" + mac;
        Map<String, Object> updateMap = new HashMap<>();
        updateMap.put("mac", mac);
        updateMap.put("ip_addr", ipAddr);
        updateMap.put("alive_last_time", updateTimeAcct);
        int time = 1800;
        aliveTemplate.opsForHash().putAll(name, updateMap);
        aliveTemplate.expire(name, time, TimeUnit.SECONDS);
        String sql = String.format(SELECT_SQL_ALIVE_HEARTBEAT, mac);
        Map<String, Object> data = super.queryForMap(sql);
        if (MyTools.isEmpty(data)) {
            String saveSql = String.format(INSERT_SQL_ALIVE_HEARTBEAT, mac, ipAddr, ALIVE_ONLINE, updateTimeAcct);
            super.jdbcTemplate.execute(saveSql);
        }
    }


    /**
     * @return
     * @Author beixing
     * @Description 同步endpoint表的数据
     * @Date 2022/3/23
     * @Param
     **/
    @Override
    public ApiResult syncEndpointTask() {
        List<Map<String, Object>> mapList = super.getSyncData(AAA_SYNC_ENDPOINT_MARK_NAME, AAA_SYNC_ENDPOINT_DEST_NAME, AAA_SYNC_ENDPOINT_TYPE);
        if (MyTools.isEmpty(mapList)) {
            return ApiResult.success();
        }
        String maxTime = "";
        for (Map<String, Object> map : mapList) {
            String mac = (String) map.get("mac");
            maxTime = String.valueOf(map.get(AAA_TIMESTAMP_COLUMN));
            String radiusSql = String.format(SELECT_SQL_RADIUS_RADIUS_ENDPOINT, mac);
            Map<String, Object> radiusMap = super.queryForMap(radiusSql);
            int policy = (int) map.get("access_policy");
            String baseIp = (String) map.get("base_ip");
            int source = (int) map.get("source");
            if (MyTools.isEmpty(radiusMap)) {
                String saveSql = String.format(INSERT_SQL_RADIUS_RADIUS_ENDPOINT, mac, mac, policy, baseIp, 0, source);
                super.radiusJdbcTemplate.execute(saveSql);
            } else {
                String updateSql = String.format(UPDATE_SQL_RADIUS_RADIUS_ENDPOINT, policy, baseIp, 0, source, mac);
                super.radiusJdbcTemplate.execute(updateSql);
            }
        }

        return ApiResult.success(maxTime);
    }

    /**
     * @return
     * @Author beixing
     * @Description nas同步
     * @Date 2022/3/23
     * @Param
     **/
    @Override
    public ApiResult syncNasTask() {
        List<Map<String, Object>> mapList = super.getSyncData(AAA_SYNC_NAS_MARK_NAME, AAA_SYNC_NAS_DEST_NAME, AAA_SYNC_NAS_TYPE);
        if (MyTools.isEmpty(mapList)) {
            return ApiResult.success();
        }
        String maxTime = "";
        for (Map<String, Object> map : mapList) {
            String nasIp = (String) map.get("nas_ip");
            int deleteStatus = (int) map.get("delete_status");
            maxTime = String.valueOf(map.get(AAA_TIMESTAMP_COLUMN));
            if (deleteStatus == 1) {

            } else {
                String queryNasIpSql = String.format("",nasIp);
                Map<String, Object> queryForMap = super.queryForMap(queryNasIpSql);
                if (MyTools.isEmpty(queryForMap)) {
                    String saveIpSql = String.format("");
                    super.radiusJdbcTemplate.execute(saveIpSql);
                } else {
                    String updateIpSql = String.format("");
                    super.radiusJdbcTemplate.execute(updateIpSql);
                }
            }
        }

        return ApiResult.success(maxTime);
    }


}
