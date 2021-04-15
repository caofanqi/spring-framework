package cn.caofanqi;

import cn.caofanqi.config.RootConfig;
import cn.caofanqi.config.ServletConfig;
import org.springframework.lang.NonNull;
import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;

/**
 * 基于java的配置方式;
 * 如果不需要应用程序上下文层次结构，应用程序可以通过getRootConfigClasses()返回所有配置，并从getServletConfigClasses()返回null。
 */
public class MyWebBootstrap extends AbstractAnnotationConfigDispatcherServletInitializer {


	@Override
	protected Class<?>[] getRootConfigClasses() {
		return new Class<?>[]{RootConfig.class};
	}

	@Override
	protected Class<?>[] getServletConfigClasses() {
		return new Class<?>[]{ServletConfig.class};
	}

	@NonNull
	@Override
	protected String[] getServletMappings() {
		return new String[]{"/"};
	}

}