package cn.caofanqi.config;

import cn.caofanqi.pojo.Student;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackages = "cn.caofanqi")
public class AppConfig {

	@Bean
	public Student student() {
		return new Student();
	}

}
