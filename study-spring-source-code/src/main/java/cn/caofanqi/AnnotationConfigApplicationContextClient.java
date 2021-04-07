package cn.caofanqi;

import cn.caofanqi.config.AppConfig;
import cn.caofanqi.pojo.User;
import cn.caofanqi.service.UserService;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class AnnotationConfigApplicationContextClient {

	public static void main(String[] args) {
		AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext(AppConfig.class);
		UserService userService = applicationContext.getBean(UserService.class);
		userService.save(new User("zhangsan", 23));
	}
}
