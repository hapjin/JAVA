package org.jy.rpc;

import org.jy.rpc.op.AnOtherEchoService;
import org.jy.rpc.op.Echo;

public class MainClient {
	public static void main(String[] args) {
		/*
		 * 动态代理,通过getProxy方法动态地生成了一个 Echo类型的代理对象echo,该代理对象实现了Echo接口
		 * 因此，可以使用该动态返回得到的echo 对象来调用  Echo 接口中定义的echo 方法
		 * 
		 * 在客户端只需要提供需要调用的服务名称:Echo ,该服务所在的服务器地址:127.0.0.1,以及端口:20382
		 * 就可以调用服务器上的方法了.
		 */
		Echo echo = RPC.getProxy(Echo.class, "127.0.0.1", 20382);
		
		/*
		 * 当执行 echo.echo("hello,hello") 时,它会委托给动态代理InvocationHandler执行invoke方法
		 * 
		 */
		System.out.println(echo.echo("hello,hello"));//使用代理对象调用服务器的服务.并将结果输出
		
		AnOtherEchoService otherEcho = RPC.getProxy(AnOtherEchoService.class, "127.0.0.1", 20382);
		System.out.println(otherEcho.anOtherEcho("hello, world"));
	}
}
