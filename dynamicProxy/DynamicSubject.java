package dynamicProxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

public class DynamicSubject implements InvocationHandler{
	private Object sub;//被 代理的对象,它是一个Object类型的对象，说明，被 代理的对象是可以动态改变的
	
	public DynamicSubject(){
		
	}
	
	//给构造方法传递不同的类型的 被代理的对象，就可以 实现 动态代理--即在运行时确定被代理的对象的类型
	public DynamicSubject(Object obj){
		sub = obj;
	}
	
	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable{
		System.out.println("before calling " + method);
		
		/*
		 * 在构造方法中获得了被代理的对象RealSubject sub
		 */
		method.invoke(sub, args);//调用 实际 的方法，调用被代理的对象的方法，即RealSubject.request()
		
		System.out.println("after calling " + method);
		return null;
	}
}
