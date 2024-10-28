package com.zans.task;


import com.zans.config.SpringBeanFactory;
import com.zans.dao.*;
import com.zans.model.DeviceFeatureModelRelevance;
import com.zans.model.DeviceFeatureModelRelevanceEx;
import com.zans.model.DeviceFeatureTypeRelevance;
import com.zans.model.DeviceFeatureTypeRelevanceEx;
import com.zans.utils.MyTools;
import com.zans.vo.ResultRespVO;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

import static com.zans.config.GlobalConstants.TASK_SUC_COUNT;


/**
 * @author beixing
 * @Title: AnalyzeTask
 * @Description: 设备识别分析的线程
 * @Version:1.0.0
 * @Since:jdk1.8
 * @Date 2021/7/14
 **/
@Slf4j
public class AnalyzeDeviceTask implements Runnable {

    private DeviceFeatureDao deviceFeatureDao = (DeviceFeatureDao) SpringBeanFactory.getBean("deviceFeatureDao");
    private DeviceFeatureModelRelevanceDao deviceFeatureModelRelevanceDao = (DeviceFeatureModelRelevanceDao) SpringBeanFactory.getBean("deviceFeatureModelRelevanceDao");
    private DeviceFeatureModelRelevanceExDao deviceFeatureModelRelevanceExDao = (DeviceFeatureModelRelevanceExDao) SpringBeanFactory.getBean("deviceFeatureModelRelevanceExDao");

    private DeviceFeatureTypeRelevanceDao deviceFeatureTypeRelevanceDao = (DeviceFeatureTypeRelevanceDao) SpringBeanFactory.getBean("deviceFeatureTypeRelevanceDao");
    private DeviceFeatureTypeRelevanceExDao deviceFeatureTypeRelevanceExDao = (DeviceFeatureTypeRelevanceExDao) SpringBeanFactory.getBean("deviceFeatureTypeRelevanceExDao");



    private ResultRespVO versionRespVO;

    public AnalyzeDeviceTask(ResultRespVO versionRespVO) {
        this.versionRespVO = versionRespVO;
    }

    @Override
    public void run() {

        log.info("设备分析启动！参数:{}", versionRespVO);
        try {
            analyzeModel(versionRespVO);
            analyzeType(versionRespVO);
            TASK_SUC_COUNT.incrementAndGet();
        } catch (Exception e) {
            log.error("异常:", e);
            return;
        }

    }

    private void analyzeModel(ResultRespVO versionRespVO) {
        long count = versionRespVO.getCount();
        String name = versionRespVO.getName();
        String fieldName = versionRespVO.getFieldName();
        List<ResultRespVO> respVOList = deviceFeatureDao.analyzeModel(name,fieldName);
        if (MyTools.isEmpty(respVOList)) {
            log.warn("name:{},模块分析无数据!", name);
            return;
        }
        List<DeviceFeatureModelRelevanceEx> modelReferences = new ArrayList<>();
        DeviceFeatureModelRelevance deviceFeatureModelRelevance = new DeviceFeatureModelRelevance();
        for (int i = 0; i < respVOList.size(); i++) {
            ResultRespVO resultRespVO = respVOList.get(i);
            DeviceFeatureModelRelevanceEx deviceFeatureModelRelevanceEx = new DeviceFeatureModelRelevanceEx();
            if (i == 0) {
                deviceFeatureModelRelevance.setStatus(1);
                if (respVOList.size() == 1 || versionRespVO.getMaxIp().equals(versionRespVO.getMinIp())) {
                    deviceFeatureModelRelevance.setStatus(2);
                }
                deviceFeatureModelRelevance.setBusinessId(name);
                deviceFeatureModelRelevance.setModel(resultRespVO.getName());
                deviceFeatureModelRelevance.setMaxIp(versionRespVO.getMaxIp());
                deviceFeatureModelRelevance.setMinIp(versionRespVO.getMinIp());
                deviceFeatureModelRelevance.setCount(versionRespVO.getCount());
            }
            deviceFeatureModelRelevanceEx.setBusinessId(name);
            deviceFeatureModelRelevanceEx.setModel(resultRespVO.getName());
            deviceFeatureModelRelevanceEx.setMaxIp(resultRespVO.getMaxIp());
            deviceFeatureModelRelevanceEx.setMinIp(resultRespVO.getMinIp());
            deviceFeatureModelRelevanceEx.setCount(resultRespVO.getCount());
            modelReferences.add(deviceFeatureModelRelevanceEx);
        }
        deviceFeatureModelRelevanceDao.insert(deviceFeatureModelRelevance);
        deviceFeatureModelRelevanceExDao.insertBatch(modelReferences);
        log.info("name:{},模块已分析完成!", name);
        return;
    }


    private void analyzeType(ResultRespVO versionRespVO) {
        long count = versionRespVO.getCount();
        String name = versionRespVO.getName();
        String fieldName = versionRespVO.getFieldName();
        List<ResultRespVO> respVOList = deviceFeatureDao.analyzeDeviceType(name,fieldName);
        if (MyTools.isEmpty(respVOList)) {
            log.warn("name:{},模块分析无数据!", name);
            return;
        }
        List<DeviceFeatureTypeRelevanceEx> modelReferences = new ArrayList<>();
        DeviceFeatureTypeRelevance deviceFeatureTypeRelevance = new DeviceFeatureTypeRelevance();
        for (int i = 0; i < respVOList.size(); i++) {
            ResultRespVO resultRespVO = respVOList.get(i);
            DeviceFeatureTypeRelevanceEx deviceFeatureTypeRelevanceEx = new DeviceFeatureTypeRelevanceEx();
            if (i == 0) {
                deviceFeatureTypeRelevance.setStatus(1);
                if (respVOList.size() == 1 || versionRespVO.getMaxIp().equals(versionRespVO.getMinIp())) {
                    deviceFeatureTypeRelevance.setStatus(2);
                }
                deviceFeatureTypeRelevance.setBusinessId(name);
                deviceFeatureTypeRelevance.setDeviceType(resultRespVO.getName());
                deviceFeatureTypeRelevance.setMaxIp(versionRespVO.getMaxIp());
                deviceFeatureTypeRelevance.setMinIp(versionRespVO.getMinIp());
                deviceFeatureTypeRelevance.setCount(versionRespVO.getCount());

            }
            deviceFeatureTypeRelevanceEx.setBusinessId(name);
            deviceFeatureTypeRelevanceEx.setDeviceType(resultRespVO.getName());
            deviceFeatureTypeRelevanceEx.setMaxIp(resultRespVO.getMaxIp());
            deviceFeatureTypeRelevanceEx.setMinIp(resultRespVO.getMinIp());
            deviceFeatureTypeRelevanceEx.setCount(resultRespVO.getCount());
            modelReferences.add(deviceFeatureTypeRelevanceEx);
        }
        deviceFeatureTypeRelevanceDao.insert(deviceFeatureTypeRelevance);
        deviceFeatureTypeRelevanceExDao.insertBatch(modelReferences);
        log.info("name:{},模块已分析完成!", name);
        return;
    }
}
