package org.jy.rpc;

import org.jy.rpc.op.AnOtherEchoService;
import org.jy.rpc.op.AnOtherEchoServiceImpl;
import org.jy.rpc.op.Echo;
import org.jy.rpc.op.RemoteEcho;
import org.jy.rpc.support.Server;

public class MainServer {
	public static void main(String[] args) {
		Server server = new RPC.RPCServer();

		/*
		 * server 启动后,需要注册server端能够提供的服务,这样client使用 服务的名字、
		 * 服务器的IP、以及服务所运行的端口 来调用 server 的服务
		 */
		server.register(Echo.class, RemoteEcho.class);//注册服务的名字
		server.register(AnOtherEchoService.class, AnOtherEchoServiceImpl.class);
		
		server.start();//启动server
	}
}
