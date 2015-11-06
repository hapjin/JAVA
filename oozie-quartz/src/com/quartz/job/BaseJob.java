package com.quartz.job;

import org.quartz.Job;

public abstract class BaseJob implements Job{
	
	public String getAPPHDFSPath(String modelId){
		return null;//just for test...You can implement it by yourself
	}
	
	public String copyAPP(String from, String to){
		return "hdfs://datanode1:8020/user/cdhfive/examples/apps/map-reduce";
	}
}