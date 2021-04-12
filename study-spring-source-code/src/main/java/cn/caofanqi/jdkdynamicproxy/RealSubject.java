package cn.caofanqi.jdkdynamicproxy;

public class RealSubject implements Subject {

	@Override
	public void myMethod() {
		System.out.println("myMethod...");
	}

}
