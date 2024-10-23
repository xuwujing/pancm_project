package com.zans.service.impl;

import com.zans.config.RestTemplateHelper;
import com.zans.dao.SysConstantDao;
import com.zans.model.SysConstant;
import com.zans.service.IAlertService;
import com.zans.service.IBaseLineService;
import com.zans.util.MyTools;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Map;

import static com.zans.config.Constants.*;
import static com.zans.config.SQLConstants.*;

/**
 * @author beixing
 * @Title: debug-api
 * @Description:
 * @Version:1.0.0
 * @Since:jdk1.8
 * @date 2022/3/22
 */
@Service
public class BaseLineServiceImpl extends AAABaseServiceImpl implements IBaseLineService {

    @Autowired
    private RestTemplateHelper restTemplateHelper;

    @Resource
    private SysConstantDao sysConstantDao;

    @Autowired
    private IAlertService alertService;

    /**
     * @return
     * @Author beixing
     * @Description 建立基线数据
     * @Date 2022/3/21
     * @Param
     **/
    @Override
    public void doBaseLine(String swIp, String nasPortId, String vlan, String ip, String mac) {
        String findBaseLineSql = String.format(SELECT_SQL_ASSET_BASELINE, ip);
        Map<String, Object> map = queryForMap(findBaseLineSql);
        if (MyTools.isNotEmpty(map)) {
            int bindStatus = (int) map.get("bind_status");
            if (BASELINE_UNBIND == bindStatus) {
                String updateBaseLineSql = String.format(UPDATE_SQL_ASSET_BASELINE, swIp, nasPortId, vlan, mac, bindStatus);
                jdbcTemplate.execute(updateBaseLineSql);
                String updateEndpointBaseIp = String.format(UPDATE_SQL_RADIUS_ENDPOINT_BASE_IP, ip, mac);
                jdbcTemplate.execute(updateEndpointBaseIp);
                return;
            } else {
//                log.info("ip:{},mac:{}已经进行绑定！无需更新！", ip, mac);
            }
        } else {
            String saveBaseLineSql = String.format(INSERT_SQL_ASSET_BASELINE, swIp, nasPortId, vlan, mac, BASELINE_BIND);
            jdbcTemplate.execute(saveBaseLineSql);
            String updateEndpointBaseIp = String.format(UPDATE_SQL_RADIUS_ENDPOINT_BASE_IP, ip, mac);
            jdbcTemplate.execute(updateEndpointBaseIp);
        }

        int userId = 18;
        String queryAssetProfileSql = String.format(SELECT_SQL_ASSET_PROFILE, ip);
        Map<String, Object> assetProfile = queryForMap(queryAssetProfileSql);
        if (MyTools.isEmpty(assetProfile)) {
            String saveAssetProfileSql = String.format(INSERT_SQL_ASSET_PROFILE, ip, mac, swIp, userId, userId,
                    ENDPOINT_SOURCE_ARP_RADIUS_NO);
            jdbcTemplate.execute(saveAssetProfileSql);
        } else {
            long assetId = (long) assetProfile.get("id");
            String updateAssetSql = String.format(UPDATE_SQL_ASSET_PROFILE, mac, swIp, assetId);
            jdbcTemplate.execute(updateAssetSql);
        }

    }

    /**
     * @param map
     * @return
     * @Author beixing
     * @Description 基线检查服务
     * 如果基线以及正常，需要更改回他自己的区域
     * //todo radius模式实现了更改(radius的配置)，acl模式需要实现
     * @Date 2022/3/22
     * @Param
     */
    @Override
    public void checkBaseLine(Map<String, Object> map) {
        SysConstant edgeAccessModeConstant = sysConstantDao.queryByKey(ACCESS_MODE);
        String edgeAccess = edgeAccessModeConstant.getConstantValue();
        String baseIp = (String) map.get("base_ip");
        String ip = (String) map.get("ip");
        String mac = (String) map.get("mac");
        //acl模式的处理
        if(ACCESS_ACCESS_MODE_ACL.equals(edgeAccess)){

            return;
        }
        //radius模式的处理

        //进行ip冲突告警
        if(!ip.equals(baseIp)){
            alertService.sendAlertIpClash(ALERT_RULE_ADD,map);
        }
        String findBaseLineSql = String.format(SELECT_SQL_ASSET_BASELINE, ip);
        Map<String, Object> baseLineMap = queryForMap(findBaseLineSql);
        if(MyTools.isEmpty(baseLineMap)){
            String baseMac = (String) baseLineMap.get("mac");
            if(MyTools.isNotEmpty(baseMac) && !mac.equals(baseMac) ){
                alertService.sendAlertIpClash(ALERT_RULE_ADD,map);
            }
        }
    }
}
