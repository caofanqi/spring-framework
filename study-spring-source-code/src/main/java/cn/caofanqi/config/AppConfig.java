package cn.caofanqi.config;

import cn.caofanqi.pojo.Student;
import cn.caofanqi.service.EntitlementCalculationService;
import cn.caofanqi.service.impl.StubEntitlementCalculationService;
import org.springframework.context.annotation.*;

@Configuration
@EnableAspectJAutoProxy
@EnableLoadTimeWeaving
@Import(JdbcConfig.class)
@ComponentScan(basePackages = "cn.caofanqi")
public class AppConfig {

	@Bean
	public Student student() {
		return new Student();
	}

	@Bean
	public EntitlementCalculationService entitlementCalculationService() {
		return new StubEntitlementCalculationService();
	}

}
