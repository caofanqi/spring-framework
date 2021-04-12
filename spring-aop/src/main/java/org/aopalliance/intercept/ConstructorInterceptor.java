/*
 * Copyright 2002-2016 the original author or authors.
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

package org.aopalliance.intercept;

import javax.annotation.Nonnull;

/**
 * <p>拦截一个新对象的构造。</p>
 * <p>用户应该实现construct(ConstructorInvocation)方法来修改原始的行为。
 * 例如，以下类实现了一个单例拦截器(只允许被拦截类的唯一实例):</p>
 *
 * Intercepts the construction of a new object.
 *
 * <p>The user should implement the {@link
 * #construct(ConstructorInvocation)} method to modify the original
 * behavior. E.g. the following class implements a singleton
 * interceptor (allows only one unique instance for the intercepted
 * class):
 *
 * <pre class=code>
 * class DebuggingInterceptor implements ConstructorInterceptor {
 *   Object instance=null;
 *
 *   Object construct(ConstructorInvocation i) throws Throwable {
 *     if(instance==null) {
 *       return instance=i.proceed();
 *     } else {
 *       throw new Exception("singleton does not allow multiple instance");
 *     }
 *   }
 * }
 * </pre>
 *
 * @author Rod Johnson
 */
public interface ConstructorInterceptor extends Interceptor  {

	/**
	 * <p>实现此方法可以在构造新对象之前和之后执行额外的处理。优雅的实现当然会调用Joinpoint.proceed()。</p>
	 * Implement this method to perform extra treatments before and
	 * after the construction of a new object. Polite implementations
	 * would certainly like to invoke {@link Joinpoint#proceed()}.
	 * @param invocation the construction joinpoint
	 * @return the newly created object, which is also the result of
	 * the call to {@link Joinpoint#proceed()}; might be replaced by
	 * the interceptor
	 * @throws Throwable if the interceptors or the target object
	 * throws an exception
	 */
	@Nonnull
	Object construct(ConstructorInvocation invocation) throws Throwable;

}
