package cn.caofanqi.jdkdynamicproxy;


import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class JdkDynamicProxySubject implements InvocationHandler {

	private final Object subject;

	public JdkDynamicProxySubject(Object subject) {
		this.subject = subject;
	}


	@Override
	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
		System.out.println("proxy.getClass() : " + proxy.getClass());

		System.out.println("before method...");
		Object result = method.invoke(subject, args);
		System.out.println("after method...");
		return result;
	}

	public Object getProxy() {
		return Proxy.newProxyInstance(Thread.currentThread().getContextClassLoader(), subject.getClass().getInterfaces(), this);
	}


}
