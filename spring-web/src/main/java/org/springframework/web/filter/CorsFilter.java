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

package org.springframework.web.filter;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.util.Assert;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.CorsProcessor;
import org.springframework.web.cors.CorsUtils;
import org.springframework.web.cors.DefaultCorsProcessor;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

/**
 * <p>过滤处理CORS pre-flight请求，并使用CorsProcessor拦截CORS简单和实际的请求，
 * 并以及根据通过提供的CorsConfigurationSource匹配的策略更新响应，例如使用CORS响应头。</p>
 * {@link javax.servlet.Filter} to handle CORS pre-flight requests and intercept
 * CORS simple and actual requests with a {@link CorsProcessor}, and to update
 * the response, e.g. with CORS response headers, based on the policy matched
 * through the provided {@link CorsConfigurationSource}.
 *
 * <p>这是在Spring MVC Java配置和Spring MVC XML名称空间中配置CORS的另一种选择。
 * 对于仅依赖spring-web(而不是spring-webmvc)的应用程序，或者对于需要在Filter级别执行CORS检查的安全约束，它很有用。</p>
 * <p>This is an alternative to configuring CORS in the Spring MVC Java config
 * and the Spring MVC XML namespace. It is useful for applications depending
 * only on spring-web (not on spring-webmvc) or for security constraints that
 * require CORS checks to be performed at {@link javax.servlet.Filter} level.
 *
 * <p>该过滤器可以与DelegatingFilterProxy一起使用，以帮助进行初始化。</p>
 * <p>This filter could be used in conjunction with {@link DelegatingFilterProxy}
 * in order to help with its initialization.
 *
 * @author Sebastien Deleuze
 * @since 4.2
 * @see <a href="https://www.w3.org/TR/cors/">CORS W3C recommendation</a>
 * @see UrlBasedCorsConfigurationSource
 */
public class CorsFilter extends OncePerRequestFilter {

	private final CorsConfigurationSource configSource;

	private CorsProcessor processor = new DefaultCorsProcessor();


	/**
	 * <p>构造函数接受一个CorsConfigurationSource，过滤器使用它来查找用于每个传入请求的CorsConfiguration。</p>
	 * Constructor accepting a {@link CorsConfigurationSource} used by the filter
	 * to find the {@link CorsConfiguration} to use for each incoming request.
	 * @see UrlBasedCorsConfigurationSource
	 */
	public CorsFilter(CorsConfigurationSource configSource) {
		Assert.notNull(configSource, "CorsConfigurationSource must not be null");
		this.configSource = configSource;
	}


	/**
	 * <p>配置一个自定义CorsProcessor，用于为请求应用匹配的CorsConfiguration。</p>
	 * <p>默认情况下使用DefaultCorsProcessor。</p>
	 * Configure a custom {@link CorsProcessor} to use to apply the matched
	 * {@link CorsConfiguration} for a request.
	 * <p>By default {@link DefaultCorsProcessor} is used.
	 */
	public void setCorsProcessor(CorsProcessor processor) {
		Assert.notNull(processor, "CorsProcessor must not be null");
		this.processor = processor;
	}


	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
			FilterChain filterChain) throws ServletException, IOException {

		CorsConfiguration corsConfiguration = this.configSource.getCorsConfiguration(request);
		boolean isValid = this.processor.processRequest(corsConfiguration, request, response);
		if (!isValid || CorsUtils.isPreFlightRequest(request)) {
			return;
		}
		filterChain.doFilter(request, response);
	}

}
