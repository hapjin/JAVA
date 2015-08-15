package org.jy.rpc.support;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

import org.jy.rpc.protocal.Invocation;

/*
 * 相当于"客户存根",它封装RPC的底层传输：网络连接的建立、序列化(这里的序列化通过Socket连接封装了)
 * Client.java 的invoke方法将在代理中被调用,它用Invocation 对象保存需要调用的远程服务名和参数
 */
public class Client {
	private String host;
	private int port;
	private Socket socket;
	private ObjectOutputStream oos;
	private ObjectInputStream ois;

	public String getHost() {
		return host;
	}


	public void setHost(String host) {
		this.host = host;
	}

	public int getPort() {
		return port;
	}
	public void setPort(int port) {
		this.port = port;
	}

	public Client(String host, int port) {
		this.host = host;
		this.port = port;
	}

	public void init() throws UnknownHostException, IOException {
		socket = new Socket(host, port);//建立 client --- server 的连接
		oos = new ObjectOutputStream(socket.getOutputStream());
	}

	public void invoke(Invocation invo) throws UnknownHostException, IOException, ClassNotFoundException {
		init();
		System.out.println("写入数据");
		oos.writeObject(invo);//将Client 需要调用的Server的 接口、方法、参数 封装起来 发给服务器
		oos.flush();
		ois = new ObjectInputStream(socket.getInputStream());//用来接收从 server 返回 回来的执行结果 的输入流
		Invocation result = (Invocation) ois.readObject();
		invo.setResult(result.getResult());//将结果 保存到 Invocation result对象中
	}
}
