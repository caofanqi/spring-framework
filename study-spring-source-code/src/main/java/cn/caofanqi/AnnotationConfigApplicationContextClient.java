package cn.caofanqi;

import cn.caofanqi.config.AppConfig;
import cn.caofanqi.pojo.User;
import cn.caofanqi.service.UserService;
import cn.caofanqi.service.EntitlementCalculationService;
import cn.caofanqi.service.impl.StubEntitlementCalculationService;
import cn.caofanqi.service.impl.UserServiceImpl;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class AnnotationConfigApplicationContextClient {

	public static void main(String[] args) {
		AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext(AppConfig.class);
		UserService userService = applicationContext.getBean(UserService.class);
		userService.save(new User("zhangsan", 23, User.MAN));


		EntitlementCalculationService entitlementCalculationService =
				(EntitlementCalculationService)applicationContext.getBean("entitlementCalculationService");
//				EntitlementCalculationService entitlementCalculationService =
//				new StubEntitlementCalculationService();
		entitlementCalculationService.calculateEntitlement();
	}
}
