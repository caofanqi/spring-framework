/*
 * Copyright 2002-2019 the original author or authors.
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

package org.springframework.beans.factory.config;

import org.springframework.beans.BeansException;
import org.springframework.lang.Nullable;

/**
 * <p>允许自定义修改新bean实例的工厂钩子 ——— 例如，检查标记接口或用代理包装bean。</p>
 *
 * <p>通常，通过标记接口或类似操作填充bean的后处理器将实现postProcessBeforeInitialization方法，而
 * 用代理包装bean的后处理器通常将实现postProcessAfterInitialization方法。</p>
 *
 * <h3>Registration</h3>
 * <p>ApplicationContext能够在其bean定义中自动检测BeanPostProcessor beans，并将
 * 这些后处理器应用于随后创建的任何bean。一个普通的BeanFactory允许以编程的方式注册后处理器，将它们
 * 应用在通过该bean工厂创建的bean上。
 * </p>
 *
 * <h3>Ordering</h3>
 * <p>在ApplicationContext中自动检测到的BeanPostProcessor beans将根据PriorityOrdered和Ordered语义进行
 * 排序。相反，以编程方式向BeanFactory注册的BeanPostProcessor beans将按照注册顺序应用。对于以编程方式注册
 * 的后处理器，通过实现PriorityOrdered和Ordered接口表达的任何语义都将被忽略。此外，BeanPostProcessor beans
 * 不考虑@Order注释。</p>
 *
 * Factory hook that allows for custom modification of new bean instances &mdash;
 * for example, checking for marker interfaces or wrapping beans with proxies.
 *
 * <p>Typically, post-processors that populate beans via marker interfaces
 * or the like will implement {@link #postProcessBeforeInitialization},
 * while post-processors that wrap beans with proxies will normally
 * implement {@link #postProcessAfterInitialization}.
 *
 * <h3>Registration</h3>
 * <p>An {@code ApplicationContext} can autodetect {@code BeanPostProcessor} beans
 * in its bean definitions and apply those post-processors to any beans subsequently
 * created. A plain {@code BeanFactory} allows for programmatic registration of
 * post-processors, applying them to all beans created through the bean factory.
 *
 * <h3>Ordering</h3>
 * <p>{@code BeanPostProcessor} beans that are autodetected in an
 * {@code ApplicationContext} will be ordered according to
 * {@link org.springframework.core.PriorityOrdered} and
 * {@link org.springframework.core.Ordered} semantics. In contrast,
 * {@code BeanPostProcessor} beans that are registered programmatically with a
 * {@code BeanFactory} will be applied in the order of registration; any ordering
 * semantics expressed through implementing the
 * {@code PriorityOrdered} or {@code Ordered} interface will be ignored for
 * programmatically registered post-processors. Furthermore, the
 * {@link org.springframework.core.annotation.Order @Order} annotation is not
 * taken into account for {@code BeanPostProcessor} beans.
 *
 * @author Juergen Hoeller
 * @author Sam Brannen
 * @since 10.10.2003
 * @see InstantiationAwareBeanPostProcessor
 * @see DestructionAwareBeanPostProcessor
 * @see ConfigurableBeanFactory#addBeanPostProcessor
 * @see BeanFactoryPostProcessor
 */
public interface BeanPostProcessor {

	/**
	 * <p>对于给定一个新bean实例，在任何初始化方法（例如InitializingBean的afterPropertiesSet方法或自定以的init-method）
	 * 回调之前，应用BeanPostProcessor的此方法。bean已经用属性值填充了。返回的bean实例可能是原始bean的包装器。</p>
	 * <p>默认实现按原样返回给定bean。</p>
	 *
	 * Apply this {@code BeanPostProcessor} to the given new bean instance <i>before</i> any bean
	 * initialization callbacks (like InitializingBean's {@code afterPropertiesSet}
	 * or a custom init-method). The bean will already be populated with property values.
	 * The returned bean instance may be a wrapper around the original.
	 * <p>The default implementation returns the given {@code bean} as-is.
	 * @param bean the new bean instance
	 * @param beanName the name of the bean
	 * @return the bean instance to use, either the original or a wrapped one;
	 * if {@code null}, no subsequent BeanPostProcessors will be invoked
	 * @throws org.springframework.beans.BeansException in case of errors
	 * @see org.springframework.beans.factory.InitializingBean#afterPropertiesSet
	 */
	@Nullable
	default Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
		return bean;
	}

	/**
	 * <p>对于给定一个新bean实例，在任何初始化方法（例如InitializingBean的afterPropertiesSet方法或自定以的init-method）
	 * 	回调之后，应用BeanPostProcessor的此方法。bean已经用属性值填充了。返回的bean实例可能是原始bean的包装器。</p>
	 * 	<p>对于FactoryBean，将被FactoryBean实例和通过FactoryBean创建的对象调用此回调方法（从Spring2.0开始）。后处理器
	 * 	可以通过相应的bean instanceof FactoryBean检查bean实例，来决定是应用于FactoryBean或它创建的对象，或者两者都用。</p>
	 * 	<p>与所有其他BeanPostProcessor回调不同，通过InstantiationAwareBeanPostProcessor的postProcessBeforeInstantiation方法
	 * 	短路实例化bean后也会触发此方法的回调。</p>
	 * <p>默认实现按原样返回给定bean。</p>
	 *
	 * Apply this {@code BeanPostProcessor} to the given new bean instance <i>after</i> any bean
	 * initialization callbacks (like InitializingBean's {@code afterPropertiesSet}
	 * or a custom init-method). The bean will already be populated with property values.
	 * The returned bean instance may be a wrapper around the original.
	 * <p>In case of a FactoryBean, this callback will be invoked for both the FactoryBean
	 * instance and the objects created by the FactoryBean (as of Spring 2.0). The
	 * post-processor can decide whether to apply to either the FactoryBean or created
	 * objects or both through corresponding {@code bean instanceof FactoryBean} checks.
	 * <p>This callback will also be invoked after a short-circuiting triggered by a
	 * {@link InstantiationAwareBeanPostProcessor#postProcessBeforeInstantiation} method,
	 * in contrast to all other {@code BeanPostProcessor} callbacks.
	 * <p>The default implementation returns the given {@code bean} as-is.
	 * @param bean the new bean instance
	 * @param beanName the name of the bean
	 * @return the bean instance to use, either the original or a wrapped one;
	 * if {@code null}, no subsequent BeanPostProcessors will be invoked
	 * @throws org.springframework.beans.BeansException in case of errors
	 * @see org.springframework.beans.factory.InitializingBean#afterPropertiesSet
	 * @see org.springframework.beans.factory.FactoryBean
	 */
	@Nullable
	default Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
		return bean;
	}

}
