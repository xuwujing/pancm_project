package com.zans.task;


import com.zans.config.SpringBeanFactory;
import com.zans.dao.HikFeatureDao;
import com.zans.utils.MyTools;
import com.zans.vo.ResultRespVO;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

import static com.zans.config.GlobalConstants.tpx;


/**
 * @author beixing
 * @Title: AnalyzeTask
 * @Description: 任务分析线程
 * @Version:1.0.0
 * @Since:jdk1.8
 * @Date 2021/7/14
 **/
@Slf4j
public class AnalyzeTask extends Thread {

    private HikFeatureDao hikFeatureDao = (HikFeatureDao) SpringBeanFactory.getBean("hikFeatureDao");

    @Override
    public void run() {
        /**
         * 1.分析hik_feature的版本，进行group by操作；
         * 2.得到每个group by的值，开启多线程执行特征码分析任务；
         * 3.特征码分析任务，根据目前现有匹配的特征得到设备类别和设备类型;
         * 4.匹配的设备类别和设备类型，不确定唯一值的取最大值，并将此值存储起来到样本表中;
         * 5.后续在维护最新样本表；
         */
        try {
            List<ResultRespVO> versionRespVOS = hikFeatureDao.groupByVersion();
            if(MyTools.isEmpty(versionRespVOS)){
                log.warn("hik_feature表无数据，不进行分析!");
                return;
            }
            versionRespVOS.forEach(versionRespVO -> {
                  tpx.execute(new AnalyzeDeviceTask(versionRespVO));
            });

        } catch (Exception e) {
            log.error("处理失败！",e);
            return;
        }

    }

}
