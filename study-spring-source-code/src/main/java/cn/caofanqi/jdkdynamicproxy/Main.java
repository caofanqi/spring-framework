package cn.caofanqi.jdkdynamicproxy;

public class Main {

	public static void main(String[] args) {
		// 动态代理生成的class文件保存到磁盘，具体用哪个参数，要看ProxyGenerator这个类里面的定义
		System.setProperty("sun.misc.ProxyGenerator.saveGeneratedFiles", "true");

		JdkDynamicProxySubject jdkDynamicProxySubject = new JdkDynamicProxySubject(new RealSubject());
		Subject subject = (Subject) jdkDynamicProxySubject.getProxy();
		subject.myMethod();

		System.out.println("subject.getClass()" + subject.getClass());

	}
}
