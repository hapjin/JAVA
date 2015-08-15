package org.jy.rpc.protocal;

import java.io.Serializable;
import java.util.Arrays;


public class Invocation implements Serializable{
	/**
	 * 供JAVA反射使用，封装服务的类型、方法、参数
	 */
	private static final long serialVersionUID = 1L;
	
	private Class interfaces;//待调用的接口类型
	private Method method;//调用该接口的哪个方法
	private Object[] params;//方法所需要的参数
	private Object result;//调用方法后返回的结果
	
	
	/**
	 * @return the result
	 */
	public Object getResult() {
		return result;
	}
	/**
	 * @param result the result to set
	 */
	public void setResult(Object result) {
		this.result = result;
	}
	/**
	 * @return the interfaces
	 */
	public Class getInterfaces() {
		return interfaces;
	}
	/**
	 * @param interfaces the interfaces to set
	 */
	public void setInterfaces(Class interfaces) {
		this.interfaces = interfaces;
	}
	/**
	 * @return the method
	 */
	public Method getMethod() {
		return method;
	}
	/**
	 * @param method the method to set
	 */
	public void setMethod(Method method) {
		this.method = method;
	}
	/**
	 * @return the params
	 */
	public Object[] getParams() {
		return params;
	}
	/**
	 * @param params the params to set
	 */
	public void setParams(Object[] params) {
		this.params = params;
	}
	@Override
	public String toString() {
		return interfaces.getName()+"."+method.getMethodName()+"("+Arrays.toString(params)+")";
	}
	
}
