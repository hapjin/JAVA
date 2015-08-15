package org.jy.rpc;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.HashMap;
import java.util.Map;

import org.jy.rpc.protocal.Invocation;
import org.jy.rpc.support.Client;
import org.jy.rpc.support.Listener;
import org.jy.rpc.support.Server;


public class RPC {
	public static <T> T getProxy(final Class<T> clazz,String host,int port) {
		
		final Client client = new Client(host,port);//
		InvocationHandler handler = new InvocationHandler() {
			
			@Override
			public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
				Invocation invo = new Invocation();
				invo.setInterfaces(clazz);
				
				//利用反射机制将java.lang.reflect.Method 所代表的方法名,参数 封装到 Invocation invo对象中
				invo.setMethod(new org.jy.rpc.protocal.Method(method.getName(),method.getParameterTypes()));
				invo.setParams(args);
				
				/*
				 * 当把需要调用的远程server端的方法名和参数封装到invo之后，Client 对象 就可以把 invo 作为参数 传递给服务器了.
				 * 为什么需要这样做呢？InvocationHandler 的invoke方法是自动执行的，在该方法里面，它根据生成的代理对象 proxy (第一个参数)
				 * 所实现的接口(由 Proxy.newProxyInstance()的第二个参数指定) 就可以知道这个接口中定义了哪些方法
				 * InvocationHandler 的 invoke 方法的第二个参数Method method 就可以解析出接口中的方法名和参数了
				 * 把它们封装进Invocation invo对象中,再将 invo 作为 client.invoke(invo)的参数 发送到服务器方
				 */
				client.invoke(invo);//invoke 先调用init发起一个Socket连接,再将invo 发送至输出流中
				return invo.getResult();
			}
		};
		
		/*
		 * @param Class[]{} 该参数声明了动态生成的代理对象实现了的接口,即 clazz 所代表的接口类型 .
		 * 这表明了生成的代理对象它是一个它所实现了的接口类型的对象
		 * 从而就可以用它来调用它所实现的接口中定义的方法
		 * 
		 * @param handler 生成代理实例对象时需要传递一个handler参数
		 * 这样当该 代理实例对象调用接口中定义的方法时,将会委托给InvocationHandler 接口中声明的invoke方法
		 * 此时,InvocationHandler 的invoke 方法将会被自动调用
		 */
		T t = (T) Proxy.newProxyInstance(RPC.class.getClassLoader(), new Class[] {clazz}, handler);
		return t;
	}
	
/*
 * 相当于服务器存根
 */
	public static class RPCServer implements Server{
		private int port = 20382;
		private Listener listener; 
		private boolean isRuning = true;
		
		/**
		 * @param isRuning the isRuning to set
		 */
		public void setRuning(boolean isRuning) {
			this.isRuning = isRuning;
		}

		/**
		 * @return the port
		 */
		public int getPort() {
			return port;
		}

		/**
		 * @param port the port to set
		 */
		public void setPort(int port) {
			this.port = port;
		}

		private Map<String ,Object> serviceEngine = new HashMap<String, Object>();
		
		/*
		 * 服务器端的 call 方法用来
		 */
		
		@Override
		public void call(Invocation invo) {
			System.out.println(invo.getClass().getName());
			Object obj = serviceEngine.get(invo.getInterfaces().getName());
			if(obj!=null) {
				try {
					Method m = obj.getClass().getMethod(invo.getMethod().getMethodName(), invo.getMethod().getParams());
					/*
					 * 利用JAVA反射机制来执行java.lang.reflect.Method 所代表的方法
					 * @param result : 执行实际方法后 得到的 服务的执行结果
					 */
					Object result = m.invoke(obj, invo.getParams());
					invo.setResult(result);//将服务的执行结果封装到invo对象中。在后面的代码中，将该对象写入到输出流中
				} catch (Throwable th) {
					th.printStackTrace();
				}
			} else {
				throw new IllegalArgumentException("has no these class");
			}
		}

		/*
		 * @param interfaceDefiner 需要注册的接口
		 * @param impl 注册的接口的实现类 
		 */
		@Override
		public void register(Class interfaceDefiner, Class impl) {
			try {
				this.serviceEngine.put(interfaceDefiner.getName(), impl.newInstance());
				System.out.println(serviceEngine);
			} catch (Throwable e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
		}

		@Override
		public void start() {
			System.out.println("启动服务器");
			
			/*
			 * server 启动时,需要Listener监听是否有client的请求连接
			 * listener 是一个线程,由它来监听连接
			 */
			listener = new Listener(this);
			this.isRuning = true;
			listener.start();//listener 是一个线程类,start()后会执行线程的run方法
		}

		@Override
		public void stop() {
			this.setRuning(false);
		}

		@Override
		public boolean isRunning() {
			return isRuning;
		}
	}
}