package com.quartz.job;

import java.util.Properties;

import org.apache.oozie.client.OozieClient;
import org.apache.oozie.client.OozieClientException;
import org.apache.oozie.client.WorkflowJob.Status;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;


public class MRJob extends BaseJob{
	private static final String OOZIE_ADDRESS = "http://192.168.121.35:11000/oozie";
	private static final String USER_NAME = "cdhfive";
	private static final String NAME_NODE = "hdfs://datanode1:8020";
	private static final String Job_Tracker = "datanode1:8032";
	
	OozieClient wc = new OozieClient(OOZIE_ADDRESS);// get a OozieClient for local Oozie
	Properties conf = wc.createConfiguration();//create workflow job configuration 
	
	public MRJob(){
		conf.setProperty(OozieClient.APP_PATH, copyAPP("abc", "def"));//get Model's hdfs address
		conf.setProperty("nameNode", NAME_NODE);
		conf.setProperty("jobTracker", Job_Tracker);
		conf.setProperty("inputDir", "/user/cdhfive/examples/input-data");
		conf.setProperty("outputDir", "/user/cdhfive/examples/output-data");
		conf.setProperty("queueName", "default");
		conf.setProperty("examplesRoot", "examples");
		conf.setProperty("user.name", USER_NAME);
	}

	/*
	 * execute method will be called by quartz scheduler automatically when trigger fired
	 */
	@Override
	public void execute(JobExecutionContext arg0) throws JobExecutionException {
		try{
			String jobId = wc.run(conf);//submit job to oozie and get the jobId
			System.out.println("Workflow job submitted");
			
			//wait until the workflow job finishes
			while(wc.getJobInfo(jobId).getStatus() == Status.RUNNING){
				System.out.println("Workflow job running...");
				try{
					Thread.sleep(10*1000);
				}catch(InterruptedException e){e.printStackTrace();}
			}
			System.out.println("Workflow job completed!");
			System.out.println(wc.getJobId(jobId));
		}catch(OozieClientException e){e.printStackTrace();}
	}
}
