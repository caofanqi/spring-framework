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

package org.springframework.context;

/**
 * <p>用于在刷新之前初始化Spring ConfigurableApplicationContext的回调接口。</p>
 * Callback interface for initializing a Spring {@link ConfigurableApplicationContext}
 * prior to being {@linkplain ConfigurableApplicationContext#refresh() refreshed}.
 *
 * <p>通常用于需要对应用程序上下文进行一些编程初始化的web应用程序中。
 * 例如，根据上下文环境注册属性源或激活profiles文件。请参阅ContextLoader和FrameworkServlet对声明"contextInitializerClasses"
 * context-param和init-param的支持。</p>
 * <p>Typically used within web applications that require some programmatic initialization
 * of the application context. For example, registering property sources or activating
 * profiles against the {@linkplain ConfigurableApplicationContext#getEnvironment()
 * context's environment}. See {@code ContextLoader} and {@code FrameworkServlet} support
 * for declaring a "contextInitializerClasses" context-param and init-param, respectively.
 *
 * <p>我们鼓励ApplicationContextInitializer处理器检测Spring的Ordered接口是否已经实现，或者@Order注释是否存在，并在调用之前对实例进行相应的排序。</p>
 * <p>{@code ApplicationContextInitializer} processors are encouraged to detect
 * whether Spring's {@link org.springframework.core.Ordered Ordered} interface has been
 * implemented or if the {@link org.springframework.core.annotation.Order @Order}
 * annotation is present and to sort instances accordingly if so prior to invocation.
 *
 * @author Chris Beams
 * @since 3.1
 * @param <C> the application context type
 * @see org.springframework.web.context.ContextLoader#customizeContext
 * @see org.springframework.web.context.ContextLoader#CONTEXT_INITIALIZER_CLASSES_PARAM
 * @see org.springframework.web.servlet.FrameworkServlet#setContextInitializerClasses
 * @see org.springframework.web.servlet.FrameworkServlet#applyInitializers
 */
@FunctionalInterface
public interface ApplicationContextInitializer<C extends ConfigurableApplicationContext> {

	/**
	 * <p>初始化给定的应用程序上下文。</p>
	 * Initialize the given application context.
	 * @param applicationContext the application to configure
	 */
	void initialize(C applicationContext);

}
