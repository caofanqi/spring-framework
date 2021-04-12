/*
 * Copyright 2002-2015 the original author or authors.
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

package org.springframework.aop;

import org.aopalliance.intercept.MethodInvocation;

import org.springframework.lang.Nullable;

/**
 * <p>AOP联盟MethodInvocation接口的扩展，允许访问方法调用所通过的代理。</p>
 * <p>如果有必要，可以用代理替换返回值，例如调用目标本身返回时。</p>
 *
 * Extension of the AOP Alliance {@link org.aopalliance.intercept.MethodInvocation}
 * interface, allowing access to the proxy that the method invocation was made through.
 *
 * <p>Useful to be able to substitute return values with the proxy,
 * if necessary, for example if the invocation target returned itself.
 *
 * @author Juergen Hoeller
 * @author Adrian Colyer
 * @since 1.1.3
 * @see org.springframework.aop.framework.ReflectiveMethodInvocation
 * @see org.springframework.aop.support.DelegatingIntroductionInterceptor
 */
public interface ProxyMethodInvocation extends MethodInvocation {

	/**
	 * <p>返回进行此方法调用的代理。</p>
	 * Return the proxy that this method invocation was made through.
	 * @return the original proxy object
	 */
	Object getProxy();

	/**
	 * <p>创建该对象的克隆。如果在对该对象调用proceed()之前进行了克隆，则可以在每个克隆上调用proceed()一次，
	 * 以多次调用连接点(以及通知链的其余部分)。</p>
	 *
	 * Create a clone of this object. If cloning is done before {@code proceed()}
	 * is invoked on this object, {@code proceed()} can be invoked once per clone
	 * to invoke the joinpoint (and the rest of the advice chain) more than once.
	 * @return an invocable clone of this invocation.
	 * {@code proceed()} can be called once per clone.
	 */
	MethodInvocation invocableClone();

	/**
	 * <p>创建该对象的克隆。如果在对该对象调用proceed()之前进行了克隆，则可以在每个克隆上调用proceed()一次，
	 * 以多次调用连接点(以及通知链的其余部分)。</p>
	 *
	 * Create a clone of this object. If cloning is done before {@code proceed()}
	 * is invoked on this object, {@code proceed()} can be invoked once per clone
	 * to invoke the joinpoint (and the rest of the advice chain) more than once.
	 * @param arguments the arguments that the cloned invocation is supposed to use,
	 * overriding the original arguments
	 * @return an invocable clone of this invocation.
	 * {@code proceed()} can be called once per clone.
	 */
	MethodInvocation invocableClone(Object... arguments);

	/**
	 * <p>在此链中的任何advice中设置要在后续调用中使用的参数。</p>
	 * Set the arguments to be used on subsequent invocations in the any advice
	 * in this chain.
	 * @param arguments the argument array
	 */
	void setArguments(Object... arguments);

	/**
	 * <p>将具有给定值的指定用户属性添加到此调用。</p>
	 * <p>AOP框架本身不使用这些属性。它们只是作为调用对象的一部分，在特殊的拦截器中使用。</p>
	 *
	 * Add the specified user attribute with the given value to this invocation.
	 * <p>Such attributes are not used within the AOP framework itself. They are
	 * just kept as part of the invocation object, for use in special interceptors.
	 * @param key the name of the attribute
	 * @param value the value of the attribute, or {@code null} to reset it
	 */
	void setUserAttribute(String key, @Nullable Object value);

	/**
	 * <p>返回指定用户属性的值。</p>
	 * Return the value of the specified user attribute.
	 * @param key the name of the attribute
	 * @return the value of the attribute, or {@code null} if not set
	 * @see #setUserAttribute
	 */
	@Nullable
	Object getUserAttribute(String key);

}
