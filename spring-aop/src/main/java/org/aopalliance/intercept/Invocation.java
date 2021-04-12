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
 * <p>这个接口表示程序中的调用。</p>
 * <p>调用是一个连接点，可以被拦截器拦截。</p>
 *
 * This interface represents an invocation in the program.
 *
 * <p>An invocation is a joinpoint and can be intercepted by an
 * interceptor.
 *
 * @author Rod Johnson
 */
public interface Invocation extends Joinpoint {

	/**
	 * <p>以数组对象的形式获取参数。可以通过改变数组中的元素值来改变参数。</p>
	 * Get the arguments as an array object.
	 * It is possible to change element values within this
	 * array to change the arguments.
	 * @return the argument of the invocation
	 */
	@Nonnull
	Object[] getArguments();

}
