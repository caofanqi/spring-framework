package cn.caofanqi;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;


public class ClassPathXmlApplicationContextClient {

	public static void main(String[] args) {
		ApplicationContext applicationContext = new ClassPathXmlApplicationContext("beans.xml");
		System.out.println(applicationContext.getBean("student"));
	}

}
