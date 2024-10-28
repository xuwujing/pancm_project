package com.zans.portal.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.zans.base.service.impl.BaseServiceImpl;
import com.zans.base.util.DateHelper;
import com.zans.base.vo.ApiResult;
import com.zans.portal.config.GlobalConstants;
import com.zans.portal.dao.RadiusEndpointMapper;
import com.zans.portal.dao.VulHostDao;
import com.zans.portal.dao.VulHostVulnDao;
import com.zans.portal.dao.VulTaskMapper;
import com.zans.portal.enums.VulLevelEnum;
import com.zans.portal.model.VulHost;
import com.zans.portal.model.VulHostVuln;
import com.zans.portal.model.VulTask;
import com.zans.portal.service.IAlertRuleService;
import com.zans.portal.service.IVulTaskService;
import com.zans.portal.util.RedisUtil;
import com.zans.portal.util.RestTemplateHelper;
import com.zans.portal.vo.alert.AddAlertForVulVO;
import com.zans.portal.vo.vul.HostVO;
import com.zans.portal.vo.vul.ReportVO;
import com.zans.portal.vo.vul.VulHostVul;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * create by: beiming
 * create time: 2021/11/26 11:08
 */
@Service
@Slf4j
public class VulTaskServiceImpl extends BaseServiceImpl<VulTask> implements IVulTaskService {
    @Resource
    VulHostDao vulHostDao;

    @Resource
    VulHostVulnDao vulHostVulnDao;

    @Resource
    VulTaskMapper vulTaskMapper;

    @Autowired
    RestTemplateHelper templateHelper;

    @Autowired
    RedisUtil redisUtil;

    @Autowired
    RadiusEndpointMapper radiusEndpointMapper;

    @Autowired
    IAlertRuleService alertRuleService;



    @Resource
    public void setVulTaskMapper(VulTaskMapper vulTaskMapper) {
        super.setBaseMapper(vulTaskMapper);
        this.vulTaskMapper = vulTaskMapper;
    }

    @Override
    public List<String> getVulTaskIpAddr(Integer policy, Integer limit) {
        return vulTaskMapper.getVulTaskIpAddr(policy,limit);
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public Boolean addVulTask(String ipAddr, Integer policy, Boolean priorityFlag) {
        VulTask vulTask = vulTaskMapper.getUniqueByIpAddr(ipAddr);
        if (vulTask != null){
           int i = vulTaskMapper.deleteById(vulTask.getId());
        }
        String name = DateHelper.getDateTime(new Date())+ "-"+ipAddr;
        String createVulReqStr = this.dealCreateAndStartReq(ipAddr,name);
        String url = "http://192.168.6.89:9632/api/api/vul/createAndStart";
        ApiResult result = templateHelper.postForJsonString(url, createVulReqStr);
        if (result.getCode() != 0){
            return Boolean.FALSE;
        }
        Object data = result.getData();
        String jobId = String.valueOf(data);
        VulTask addTask = new VulTask();
        addTask.setIpAddr(ipAddr);
        addTask.setCreateTime(new Date());
        addTask.setName(name);
        addTask.setProgress("0");
        addTask.setRetGetCount(0);
        addTask.setStatus("0");
        addTask.setJobId(jobId);
        vulTaskMapper.insertSelective(addTask);
        pushToQueue(policy, priorityFlag, jobId);
        return Boolean.TRUE;
    }

    private String dealCreateAndStartReq(String ipAddr, String name) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("name",name);
        jsonObject.put("targets",ipAddr);
        return jsonObject.toJSONString();
    }

    private void pushToQueue(Integer policy, Boolean priorityFlag, String jobId) {
        if (policy.intValue()==1){
            if (priorityFlag){
                //放入队列首部
                redisUtil.leftPush(GlobalConstants.QZ_SUCCESS_QUEUE, jobId);
            } else {
                //放入队列尾部
                redisUtil.lSet(GlobalConstants.QZ_SUCCESS_QUEUE, jobId);
            }
        } else {
            if (priorityFlag){
                redisUtil.leftPush(GlobalConstants.PASS_SUCCESS_QUEUE, jobId);
            } else {
                redisUtil.lSet(GlobalConstants.PASS_SUCCESS_QUEUE, jobId);
            }
        }
    }

    @Override
    public int deleteRetGetCountMore() {
        return vulTaskMapper.deleteRetGetCountMore();
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public Boolean getVulTaskRet(String jobId, Integer policy) {
        VulTask vulTask = vulTaskMapper.getByJobId(jobId);
        if (vulTask == null){
            log.info("消费第三方任务出错#"+jobId);
            return Boolean.TRUE;
        }
        String queryTaskReqStr = this.dealQueryScanReq(jobId);
        String url = "http://192.168.6.89:9632/api/api/vul/queryResult";
        ApiResult result = templateHelper.postForJsonString(url, queryTaskReqStr);
        if (result.getCode() == 0){
            String dataStr = JSONObject.toJSONString(result.getData());
            ReportVO reportVO = JSONObject.parseObject(dataStr, ReportVO.class);
            if (reportVO == null){
                return Boolean.FALSE;
            }
            Integer status = reportVO.getStatus();
            if (status.intValue() !=6){
                log.info("漏扫未完成#{}",jobId);
                return Boolean.FALSE;
            }
            List<HostVO> hosts = reportVO.getHosts();
            for (HostVO host : hosts) {
                String hostIp = host.getHostIp();
                int i = vulTaskMapper.deleteByIpAddr(hostIp);
                vulHostDao.deleteByIpAddr(hostIp);
                vulHostVulnDao.deleteByIpAddr(hostIp);
            }
            for (HostVO host : hosts) {
                VulHost vulHost = new VulHost();
                BeanUtils.copyProperties(host,vulHost);
                vulHost.setIpAddr(host.getHostIp());
                vulHost.setCreateTime(new Date());
                vulHost.setTotal(host.getCritical()+host.getHigh()+host.getMedium()+host.getLow()+host.getInfo());
                vulHost.setJobId(jobId);
                vulHost.setPort("");
                vulHostDao.insert(vulHost);

                vulTaskMapper.updateStatus(jobId,host.getHostIp(),6);

                //根据告警等级 更改是否建议放行
                dealOtherScanStatus(host.getHostIp(), host.getHigh(), host.getMedium());
                //发送漏洞告警
                sendMissionAlert(host.getHostIp(), host.getHigh(), host.getMedium(), host.getLow(), host.getLevel(), host.getScore());

            }

            List<VulHostVul> hostVulList = reportVO.getVulHostVulList();
            for (VulHostVul hostVul : hostVulList) {
                VulHostVuln vuln = new  VulHostVuln();
                BeanUtils.copyProperties(hostVul,vuln);
                vuln.setVulLevel(hostVul.getVulLevel());
                vulHostVulnDao.insert(vuln);
            }
            return Boolean.TRUE;
        }

        //没有获取到扫描漏洞结果,放入队尾,重新消费
        if (policy.intValue()==1){
            redisUtil.lSet(GlobalConstants.QZ_SUCCESS_QUEUE,jobId);
        } else {
            redisUtil.lSet(GlobalConstants.PASS_SUCCESS_QUEUE,jobId);
        }
        return Boolean.FALSE;

    }

    private void sendMissionAlert(String hostIp, Integer high, Integer middle, Integer low, String level, Integer score) {
        if (high>0 || middle>0 || low>0){
            String mac =  radiusEndpointMapper.getMacByIp(hostIp);

            AddAlertForVulVO vulVO = new AddAlertForVulVO();
            vulVO.setIpAddr(hostIp);
            vulVO.setMac(mac);
            vulVO.setScanTime(DateHelper.getNow());
            String alertDesc = this.dealAlertDesc(high,middle,low,level,score);
            vulVO.setDescJson(alertDesc);
            String alertLevel= this.dealAlertLevel(level);
            vulVO.setAlertLevel(alertLevel);
            alertRuleService.addFromVul(vulVO);
        }

    }

    private String dealAlertLevel(String level) {
        return VulLevelEnum.getAlertLevelByCn(level)+"";
    }

    private void dealOtherScanStatus(String hostIp, Integer high, Integer medium) {
        if (high>0 || medium>0){
            // 1 扫描后ok 可以放行  2.扫描后有风险不能放行
            radiusEndpointMapper.updateOtherScanStatusByMac(2,hostIp);
        } else {
            // 1 扫描后ok 可以放行  2.扫描后有风险不能放行
            radiusEndpointMapper.updateOtherScanStatusByMac(1,hostIp);
        }

    }

    private String dealQueryScanReq(String jobId) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("jobId",jobId);
        return jsonObject.toJSONString();
    }

    private String dealAlertDesc(Integer high, Integer middle, Integer low, String level, Integer score) {
        return "风险等级--" + level + ",评分--" + score + ",高风险" + high + "个,中风险" + middle + "个," + "低风险" + low + "个";
    }



}
