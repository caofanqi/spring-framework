/*
 * Copyright 2002-2018 the original author or authors.
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

import org.springframework.lang.Nullable;

/**
 * <p>TargetSource用于获得AOP调用的当前“target”，如果没有around advice选择结束拦截器链本身，则通过反射调用该目标。</p>
 * <p>如果TargetSource是“static”的，它将始终返回相同的target，从而允许在AOP框架中进行优化。动态target sources可以支持池、热交换等。</p>
 * <p>应用程序开发人员通常不需要直接使用TargetSources：这是一个AOP框架接口。</p>
 *
 * A {@code TargetSource} is used to obtain the current "target" of
 * an AOP invocation, which will be invoked via reflection if no around
 * advice chooses to end the interceptor chain itself.
 *
 * <p>If a {@code TargetSource} is "static", it will always return
 * the same target, allowing optimizations in the AOP framework. Dynamic
 * target sources can support pooling, hot swapping, etc.
 *
 * <p>Application developers don't usually need to work with
 * {@code TargetSources} directly: this is an AOP framework interface.
 *
 * @author Rod Johnson
 * @author Juergen Hoeller
 */
public interface TargetSource extends TargetClassAware {

	/**
	 * <p>返回此TargetSource返回的目标类型。</p>
	 * <p>可以返回null，尽管TargetSource的某些用法可能只适用于预先确定的目标类。</p>
	 * Return the type of targets returned by this {@link TargetSource}.
	 * <p>Can return {@code null}, although certain usages of a {@code TargetSource}
	 * might just work with a predetermined target class.
	 * @return the type of targets returned by this {@link TargetSource}
	 */
	@Override
	@Nullable
	Class<?> getTargetClass();

	/**
	 * <p>所有调用getTarget()都会返回相同的对象吗?</p>
	 * <p>在这种情况下，就不需要调用releaseTarget(Object)， AOP框架可以缓存getTarget()的返回值。</p>
	 * Will all calls to {@link #getTarget()} return the same object?
	 * <p>In that case, there will be no need to invoke {@link #releaseTarget(Object)},
	 * and the AOP framework can cache the return value of {@link #getTarget()}.
	 * @return {@code true} if the target is immutable
	 * @see #getTarget
	 */
	boolean isStatic();

	/**
	 * <p>返回一个target实例。在AOP框架调用AOP方法调用的“target”之前立即调用。</p>
	 * Return a target instance. Invoked immediately before the
	 * AOP framework calls the "target" of an AOP method invocation.
	 * @return the target object which contains the joinpoint,
	 * or {@code null} if there is no actual target instance
	 * @throws Exception if the target object can't be resolved
	 */
	@Nullable
	Object getTarget() throws Exception;

	/**
	 * <p>释放从getTarget()方法获得的给定目标对象(如果有的话)。</p>
	 * Release the given target object obtained from the
	 * {@link #getTarget()} method, if any.
	 * @param target object obtained from a call to {@link #getTarget()}
	 * @throws Exception if the object can't be released
	 */
	void releaseTarget(Object target) throws Exception;

}
