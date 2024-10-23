package com.zans.service.impl;

import com.zans.config.RestTemplateHelper;
import com.zans.dao.SysConstantDao;
import com.zans.model.SysConstant;
import com.zans.service.IAlertService;
import com.zans.vo.AlertRecordVO;
import com.zans.vo.ApiResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.zans.config.Constants.*;


/**
 * @author beixing
 * @Title: debug-api
 * @Description:
 * @Version:1.0.0
 * @Since:jdk1.8
 * @date 2022/3/22
 */
@Service
@Slf4j
public class AlertServiceImpl implements IAlertService {

    @Autowired
    private RestTemplateHelper restTemplateHelper;

    @Resource
    private SysConstantDao sysConstantDao;

    @Override
    public ApiResult sendAlertNewEndpoint(int ruleRecover, Map<String, Object> map) {
        long strategyId = 7;
        Map<String, Object> alertMap = new HashMap<>();
        alertMap.put("ip_addr",map.get("ip"));
        alertMap.put("mac",map.get("mac"));
        alertMap.put("sw_point_name",map.get("nas_identifier"));
        alertMap.put("nas_ip_address",map.get("nas_ip"));
        return sendAlert(ruleRecover,strategyId,map);
    }

    @Override
    public ApiResult sendAlertBaselineChange(int ruleRecover, Map<String, Object> map) {
        long strategyId = 83;
        return sendAlert(ruleRecover,strategyId,map);
    }

    /**
     * @param ruleRecover
     * @param map
     * @return
     * @Author beixing
     * @Description ip冲突
     * @Date 2022/3/22
     * @Param
     */
    @Override
    public ApiResult sendAlertIpClash(int ruleRecover, Map<String, Object> map) {
        long strategyId = 9;
        Map<String, Object> alertMap = new HashMap<>();
        alertMap.put("base_ip",map.get("base_ip"));
        alertMap.put("mac",map.get("mac"));
        alertMap.put("cur_ip",map.get("ip"));
        return sendAlert(ruleRecover,strategyId,map);
    }


    private ApiResult sendAlert(int ruleRecover,long strategyId,Map<String, Object> map){
        SysConstant sysConstant = sysConstantDao.queryByKey(ALERT_API_HOST);
        String host = sysConstant.getConstantValue();
        String uri = ALERT_RULE_ADD_URI;
        if(ruleRecover == ALERT_RULE_RECOVER){
            uri = ALERT_RULE_RECOVER_URI;
        }
        String url = host + uri;
        List<Map<String, Object>> mapList = new ArrayList<>();
        mapList.add(map);
        AlertRecordVO alertRecordVO = new AlertRecordVO();
        alertRecordVO.setStrategyId(strategyId);
        alertRecordVO.setMapList(mapList);
        ApiResult apiResult = null;
        try {
             apiResult = restTemplateHelper.postForJsonString(url,alertRecordVO.toString());
        } catch (Exception e) {
            log.error("请求alert接口失败！请求url:{},请求参数:{}",url,alertRecordVO);
            return ApiResult.error("请求alert接口失败!");
        }

        return  apiResult;
    }

}
