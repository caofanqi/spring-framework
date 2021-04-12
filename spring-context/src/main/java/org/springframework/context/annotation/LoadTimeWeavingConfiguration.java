/*
 * Copyright 2002-2020 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.springframework.context.annotation;

import org.springframework.beans.factory.BeanClassLoaderAware;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.EnableLoadTimeWeaving.AspectJWeaving;
import org.springframework.context.weaving.AspectJWeavingEnabler;
import org.springframework.context.weaving.DefaultContextLoadTimeWeaver;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.instrument.classloading.LoadTimeWeaver;
import org.springframework.lang.Nullable;
import org.springframework.util.Assert;

/**
 * <p>注册LoadTimeWeaver bean的@Configuration类</p>
 * <p>当使用@EnableLoadTimeWeaving注释时，会自动导入这个配置类。查看@EnableLoadTimeWeaving javadoc以获得完整的使用细节。</p>
 *
 * {@code @Configuration} class that registers a {@link LoadTimeWeaver} bean.
 *
 * <p>This configuration class is automatically imported when using the
 * {@link EnableLoadTimeWeaving} annotation. See {@code @EnableLoadTimeWeaving}
 * javadoc for complete usage details.
 *
 * @author Chris Beams
 * @since 3.1
 * @see LoadTimeWeavingConfigurer
 * @see ConfigurableApplicationContext#LOAD_TIME_WEAVER_BEAN_NAME
 */
@Configuration(proxyBeanMethods = false)
@Role(BeanDefinition.ROLE_INFRASTRUCTURE)
public class LoadTimeWeavingConfiguration implements ImportAware, BeanClassLoaderAware {

	@Nullable
	private AnnotationAttributes enableLTW;

	@Nullable
	private LoadTimeWeavingConfigurer ltwConfigurer;

	@Nullable
	private ClassLoader beanClassLoader;


	@Override
	public void setImportMetadata(AnnotationMetadata importMetadata) {
		this.enableLTW = AnnotationConfigUtils.attributesFor(importMetadata, EnableLoadTimeWeaving.class);
		if (this.enableLTW == null) {
			throw new IllegalArgumentException(
					"@EnableLoadTimeWeaving is not present on importing class " + importMetadata.getClassName());
		}
	}

	@Autowired(required = false)
	public void setLoadTimeWeavingConfigurer(LoadTimeWeavingConfigurer ltwConfigurer) {
		this.ltwConfigurer = ltwConfigurer;
	}

	@Override
	public void setBeanClassLoader(ClassLoader beanClassLoader) {
		this.beanClassLoader = beanClassLoader;
	}


	@Bean(name = ConfigurableApplicationContext.LOAD_TIME_WEAVER_BEAN_NAME)
	@Role(BeanDefinition.ROLE_INFRASTRUCTURE)
	public LoadTimeWeaver loadTimeWeaver() {
		Assert.state(this.beanClassLoader != null, "No ClassLoader set");
		LoadTimeWeaver loadTimeWeaver = null;

		if (this.ltwConfigurer != null) {
			// The user has provided a custom LoadTimeWeaver instance
			// 用户已经提供了一个自定义的LoadTimeWeaver实例
			loadTimeWeaver = this.ltwConfigurer.getLoadTimeWeaver();
		}

		if (loadTimeWeaver == null) {
			// No custom LoadTimeWeaver provided -> fall back to the default
			// 没有提供自定义LoadTimeWeaver ->退回到默认值
			loadTimeWeaver = new DefaultContextLoadTimeWeaver(this.beanClassLoader);
		}

		// 是否开启LoadTimeWeaver
		if (this.enableLTW != null) {
			AspectJWeaving aspectJWeaving = this.enableLTW.getEnum("aspectjWeaving");
			switch (aspectJWeaving) {
				case DISABLED:
					// AJ weaving is disabled -> do nothing
					// AJ weaving 被禁用 -> 啥也不用干
					break;
				case AUTODETECT:
					if (this.beanClassLoader.getResource(AspectJWeavingEnabler.ASPECTJ_AOP_XML_RESOURCE) == null) {
						// No aop.xml present on the classpath -> treat as 'disabled'
						// 类路径上没有aop.xml -> 按'disabled'处理
						break;
					}
					// aop.xml is present on the classpath -> enable
					// 类路径上存在aop.xml -> 开启
					AspectJWeavingEnabler.enableAspectJWeaving(loadTimeWeaver, this.beanClassLoader);
					break;
				case ENABLED:
					AspectJWeavingEnabler.enableAspectJWeaving(loadTimeWeaver, this.beanClassLoader);
					break;
			}
		}

		return loadTimeWeaver;
	}

}
