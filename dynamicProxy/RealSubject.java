package dynamicProxy;

public class RealSubject implements Subject{
	public RealSubject(){
		
	}
	
	public void request(){
		System.out.println("From Real Subject");
	}
}
 