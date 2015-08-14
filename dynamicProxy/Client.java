package dynamicProxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;

public class Client {
	public static void main(String[] args) throws Exception{
		RealSubject rs = new RealSubject();// 需要 被 代理的类,即客户端现在需要的代理一个类型为 RealSubject 的对象
		
		/*
		 * 注意，代理类的构造方法的参数为Object类型，说明它可以代理 任意类型的对象---即程序运行前DynamicSubject并不知道需要代理的对象
		 * AnOtherRealSubject anotherRs = new AnOtherRealSubject();
		 * InvocationHandler handler = new DynamicSubject(anotherRs);
		 * 
		 */
		InvocationHandler handler = new DynamicSubject(rs);// 代理类，将需要 被 代理的类 作为 代理类的构造 函数的参数传入
		
		
		Class cls = rs.getClass();
		
		/*
		 * Class c = Proxy.getProxyClass(cls.getClassLoader(), cls.getInterfaces());
		   Constructor ct = c.getConstructor(new Class[]{InvocationHandler.class});
		   Subject subject = (Subject) ct.newInstance(new Object[]{handler});
		 */
		
		
		/*
		 * 
		 */
		Subject subject = (Subject)Proxy.newProxyInstance(cls.getClassLoader(), cls.getInterfaces(), handler);
		
		
		/*
		 * 当该语句执行时，它会委托到InvocationHandler 类中的 invoke方法，并执行 method.invoke()进行实际调用
		 * 注意：InvocationHandler.invoke()中有两条输出语句，运行Client后在控制台中看到了其输出结果，说明该方法被委托执行了
		 */
		subject.request();
		
		/*
		 * 输出：com.sun.proxy.$Proxy0
		 * 这表明，subject 对象类型是 $Proxy0,而不是 Subject 或 InvocationHandler 类型
		 * 但是在 22行语句中，却可以将之进行强制类型转换，转成Subject类型
		 * 原因是：Proxy.newProxyInstance生成的是一个动态对象，即在JVM运行时生成的。在newProxyInstance()的第二个参数上，给它提供了一组接口
		 * 该代理对象就会实现这组接口，因此也就可以将该对象强制转化为这组接口中的任意一个
		 */
		System.out.println(subject.getClass().getName());
	}
}
