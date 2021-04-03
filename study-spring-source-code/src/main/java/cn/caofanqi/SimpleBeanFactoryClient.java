package cn.caofanqi;

import cn.caofanqi.bean.Student;
import org.springframework.beans.factory.support.BeanDefinitionReader;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.beans.factory.xml.XmlBeanDefinitionReader;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

public class SimpleBeanFactoryClient {

	public static void main(String[] args) throws InterruptedException {
		//可用的BeanFactory核心实现，并且实现了BeanDefinitionRegistry接口。
		DefaultListableBeanFactory beanFactory = new DefaultListableBeanFactory();
		//读取资源文件并转换为BeanDefinition，注册到BeanDefinitionRegistry中。
		BeanDefinitionReader reader = new XmlBeanDefinitionReader(beanFactory);
		//Spring对资源的抽象,我们这里使用的是ClassPathResource,用于将beans.xml抽象为Resource。
		Resource resource = new ClassPathResource("beans.xml");
		reader.loadBeanDefinitions(resource);
//		reader.loadBeanDefinitions("beans.xml");
		//从工厂中获取Bean实例
		Student student = (Student) beanFactory.getBean("student");
		System.out.println(student);

	}

}
