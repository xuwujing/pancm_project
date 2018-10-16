package com.pancm.test.quartzTest.ram;

import java.util.Date;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RAMJob implements Job{

    private static Logger LOG = LoggerFactory.getLogger(RAMJob.class);

    @Override
    public void execute(JobExecutionContext arg0) throws JobExecutionException {

    	LOG.info("Say hello to Quartz" + new Date());
    }

}
