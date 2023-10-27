package com.pancm.test.quartzTest.ram;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;

/**
 * The type Ram job.
 */
public class RAMJob implements Job{

    private static Logger LOG = LoggerFactory.getLogger(RAMJob.class);

    @Override
    public void execute(JobExecutionContext arg0) throws JobExecutionException {

    	LOG.info("Say hello to Quartz" + new Date());
    }

}
