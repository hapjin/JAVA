package org.jy.rpc.support;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

import org.jy.rpc.protocal.Invocation;

/*
 * 这是一个线程类,服务器端启动一个该实例来监听Client连接
 */
public class Listener extends Thread {
	private ServerSocket server_socket;
	private Server server;

	public Listener(Server server) {
		this.server = server;
	}

	@Override
	public void run() {

		System.out.println("启动服务器中，打开端口" + server.getPort());
		try {
			server_socket = new ServerSocket(server.getPort());//创建一个监听特定端口的ServerSocket
		} catch (IOException e1) {
			e1.printStackTrace();
			return;
		}
		while (server.isRunning()) {
			try {
				System.out.println("等待请求");
				/*
				 * accept()是一个阻塞方法,server_socket 一直等待client 是否有连接到来
				 */
				Socket client = server_socket.accept();//建立一条TCP连接
				System.out.println("请求到来");
				//执行到该语句时,表明已经建立了一条连接, 该连接由 Socket client 对象 来表示
				ObjectInputStream ois = new ObjectInputStream(client.getInputStream());//读取client 发来的数据
				/*
				 * 从输入流中解析出client 发送过来的数据
				 * client 将它需要调用的远程server上的服务名称(方法)(包括参数) 通过Socket发送到server
				 * client 发送的内容由自定义的Invocation类封装,
				 */
				Invocation invo = (Invocation) ois.readObject();
				
				System.out.println("远程调用:" + invo);

				server.call(invo);//将客户端传递的调用请求代理给实际的对象调用
				
				ObjectOutputStream oos = new ObjectOutputStream(client.getOutputStream());
				/*
				 * 在call(invo)中将调用结果 封装到invo 对象中
				 * 将带有服务调用结果的 invo对象写入到输出流中
				 * 这样，client 就可以根据 socket 建立的连接从该输出流中获得调用结果
				 */
				oos.writeObject(invo);
				oos.flush();
				oos.close();
				ois.close();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		try {
			if (server_socket != null && !server_socket.isClosed())
				server_socket.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
