package com.zans.task;


import com.zans.config.SpringBeanFactory;
import com.zans.dao.AsynQueueDao;
import com.zans.model.AsynQueue;
import com.zans.util.MyTools;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.concurrent.TimeUnit;

import static com.zans.config.AsynConstants.tpx;


/**
 * @author pancm
 * @Title: asyn-server
 * @Description: 管理线程
 * @Version:1.0.0
 * @Since:jdk1.8
 * @date 2020/9/1
 */
@Slf4j
public class ManageTask extends Thread {


    private AsynQueueDao dbDao;

    private long count;

    private String lastUpdateTime;

    public ManageTask() {
        dbDao = (AsynQueueDao) SpringBeanFactory.getBean("asynQueueDao");
    }


    public void run() {
        try {
            List<AsynQueue> mapList = dbDao.queryAll(new AsynQueue());
            if (MyTools.isEmpty(mapList)) {
                log.info("策略表暂无数据更新，进行休眠！");
                return;
            }
            handle(mapList);
        } catch (Exception e) {
            log.error("加载线程运行失败！原因是:", e);
            try {
                TimeUnit.SECONDS.sleep(3);
            } catch (InterruptedException e1) {
                log.error("线程暂停失败！", e1);
            }

        }

    }


    /**
     * @return void
     * @Author pancm
     * @Description 数据处理
     * @Date 2020/9/1
     * @Param [mapList]
     **/
    private void handle(List<AsynQueue> mapList) {
        for (AsynQueue asynQueue : mapList) {
            tpx.execute(new QueueTask(asynQueue.getQueueName()));
        }
    }


}
