package org.jy.rpc.op;

/*
 * 接口的具体实现类，它具体实现了服务器能够提供何种服务
 * 将它进行注册后,client 就可以远程调用它了
 */
public class RemoteEcho implements Echo{
	public String echo(String echo) {
		return "from remote:"+echo;
	}
}
