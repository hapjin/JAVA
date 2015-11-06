package com.oozie_quartz.run;

import static org.quartz.JobBuilder.newJob;
import static org.quartz.TriggerBuilder.newTrigger;

import java.util.Date;

import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerFactory;
import org.quartz.SimpleTrigger;
import org.quartz.impl.StdSchedulerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.quartz.job.MRJob;


public class QuartzOozieJobTest {
	public static void main(String[] args) throws Exception{
		QuartzOozieJobTest test = new QuartzOozieJobTest();
		test.run();
	}
	
	public void run() throws Exception{
		Logger log = LoggerFactory.getLogger(QuartzOozieJobTest.class);

		log.info("------- Initializing ----------------------");

		SchedulerFactory sf = new StdSchedulerFactory();
		Scheduler sched = sf.getScheduler();
		
		long startTime = System.currentTimeMillis() + 20000L;
		Date startTriggerTime = new Date(startTime);
		
		JobDetail jobDetail = newJob(MRJob.class).withIdentity("job", "group1").build();
	    SimpleTrigger trigger = (SimpleTrigger) newTrigger().withIdentity("trigger", "group1").startAt(startTriggerTime).build();
	    
	    Date ft = sched.scheduleJob(jobDetail, trigger);
	    
	    log.info(jobDetail.getKey() + " will submit at " + ft + " only once.");
	    
	    sched.start();
	    
//	    sched.shutdown(true);
	}
	
}
