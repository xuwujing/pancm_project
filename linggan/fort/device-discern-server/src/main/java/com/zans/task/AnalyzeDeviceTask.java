package com.zans.task;


import com.zans.config.SpringBeanFactory;
import com.zans.dao.*;
import com.zans.model.HikFeatureModelRelevance;
import com.zans.model.HikFeatureModelRelevanceEx;
import com.zans.model.HikFeatureTypeRelevance;
import com.zans.model.HikFeatureTypeRelevanceEx;
import com.zans.utils.MyTools;
import com.zans.vo.ResultRespVO;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;


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

    private HikFeatureDao hikFeatureDao = (HikFeatureDao) SpringBeanFactory.getBean("hikFeatureDao");
    private HikFeatureModelRelevanceDao hikFeatureModelRelevanceDao = (HikFeatureModelRelevanceDao) SpringBeanFactory.getBean("hikFeatureModelRelevanceDao");
    private HikFeatureModelRelevanceExDao hikFeatureModelRelevanceExDao = (HikFeatureModelRelevanceExDao) SpringBeanFactory.getBean("hikFeatureModelRelevanceExDao");

    private HikFeatureTypeRelevanceDao hikFeatureTypeRelevanceDao = (HikFeatureTypeRelevanceDao) SpringBeanFactory.getBean("hikFeatureTypeRelevanceDao");
    private HikFeatureTypeRelevanceExDao hikFeatureTypeRelevanceExDao = (HikFeatureTypeRelevanceExDao) SpringBeanFactory.getBean("hikFeatureTypeRelevanceExDao");

    private ResultRespVO versionRespVO;

    public AnalyzeDeviceTask(ResultRespVO versionRespVO) {
        this.versionRespVO = versionRespVO;
    }

    @Override
    public void run() {

        log.info("设备分析启动！参数:{}", versionRespVO);
        try {
            long count = versionRespVO.getCount();
            String name = versionRespVO.getName();
            analyzeModel(count, name);
            analyzeType(count, name);
        } catch (Exception e) {
            log.error("异常:", e);
            return;
        }

    }

    private void analyzeModel(long count, String name) {
        List<ResultRespVO> respVOList = hikFeatureDao.analyzeModel(count);
        if (MyTools.isEmpty(respVOList)) {
            log.warn("name:{},模块分析无数据!",name);
            return;
        }
        List<HikFeatureModelRelevanceEx> modelReferences = new ArrayList<>();
        HikFeatureModelRelevance hikFeatureModelRelevance = new HikFeatureModelRelevance();
        for (int i = 0; i < respVOList.size(); i++) {
            ResultRespVO resultRespVO = respVOList.get(i);
            HikFeatureModelRelevanceEx hikFeatureModelRelevanceEx = new HikFeatureModelRelevanceEx();
            if (i == 0) {
                hikFeatureModelRelevance.setBusinessId(name);
                hikFeatureModelRelevance.setModel(resultRespVO.getName());
            }
            hikFeatureModelRelevanceEx.setBusinessId(name);
            hikFeatureModelRelevanceEx.setModel(resultRespVO.getName());
            modelReferences.add(hikFeatureModelRelevanceEx);
        }
        hikFeatureModelRelevanceDao.insert(hikFeatureModelRelevance);
        hikFeatureModelRelevanceExDao.insertBatch(modelReferences);
        log.info("name:{},模块已分析完成!",name);
        return ;
    }


    private void analyzeType(long count, String name) {
        List<ResultRespVO> respVOList = hikFeatureDao.analyzeDeviceType(count);
        if (MyTools.isEmpty(respVOList)) {
            log.warn("name:{},模块分析无数据!",name);
            return;
        }
        List<HikFeatureTypeRelevanceEx> modelReferences = new ArrayList<>();
        HikFeatureTypeRelevance hikFeatureTypeRelevance = new HikFeatureTypeRelevance();
        for (int i = 0; i < respVOList.size(); i++) {
            ResultRespVO resultRespVO = respVOList.get(i);
            HikFeatureTypeRelevanceEx hikFeatureTypeRelevanceEx = new HikFeatureTypeRelevanceEx();
            if (i == 0) {
                hikFeatureTypeRelevance.setBusinessId(name);
                hikFeatureTypeRelevance.setDeviceType(resultRespVO.getName());
            }
            hikFeatureTypeRelevanceEx.setBusinessId(name);
            hikFeatureTypeRelevanceEx.setDeviceType(resultRespVO.getName());
            modelReferences.add(hikFeatureTypeRelevanceEx);
        }
        hikFeatureTypeRelevanceDao.insert(hikFeatureTypeRelevance);
        hikFeatureTypeRelevanceExDao.insertBatch(modelReferences);
        log.info("name:{},模块已分析完成!",name);
        return ;
    }
}
