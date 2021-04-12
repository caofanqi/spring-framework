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

package org.springframework.aop.framework;

import org.aopalliance.aop.Advice;

import org.springframework.aop.Advisor;
import org.springframework.aop.TargetClassAware;
import org.springframework.aop.TargetSource;

/**
 * <p>接口，由持有AOP代理工厂配置的类实现。该配置包括拦截器和其他advice、Advisors和代理interfaces。</p>
 * <p>从Spring获得的任何AOP代理都可以被转换到这个接口，以允许对其AOP advice进行操作。</p>
 *
 * Interface to be implemented by classes that hold the configuration
 * of a factory of AOP proxies. This configuration includes the
 * Interceptors and other advice, Advisors, and the proxied interfaces.
 *
 * <p>Any AOP proxy obtained from Spring can be cast to this interface to
 * allow manipulation of its AOP advice.
 *
 * @author Rod Johnson
 * @author Juergen Hoeller
 * @since 13.03.2003
 * @see org.springframework.aop.framework.AdvisedSupport
 */
public interface Advised extends TargetClassAware {

	/**
	 * <p>返回Advised的配置是否被冻结，在这种情况下无法更改通知。</p>
	 * Return whether the Advised configuration is frozen,
	 * in which case no advice changes can be made.
	 */
	boolean isFrozen();

	/**
	 * <p>我们是否代理了完整的目标类而不是指定的接口?</p>
	 * Are we proxying the full target class instead of specified interfaces?
	 */
	boolean isProxyTargetClass();

	/**
	 * <p>返回由AOP代理代理的接口。</p>
	 * <p>将不包括目标类，它也可以被代理。</p>
	 * Return the interfaces proxied by the AOP proxy.
	 * <p>Will not include the target class, which may also be proxied.
	 */
	Class<?>[] getProxiedInterfaces();

	/**
	 * <p>确定给定的接口是否被代理。</p>
	 * Determine whether the given interface is proxied.
	 * @param intf the interface to check
	 */
	boolean isInterfaceProxied(Class<?> intf);

	/**
	 * <p>更改此Advised对象使用的TargetSource。</p>
	 * <p>只有在配置没有冻结的情况下才有效。</p>
	 *
	 * Change the {@code TargetSource} used by this {@code Advised} object.
	 * <p>Only works if the configuration isn't {@linkplain #isFrozen frozen}.
	 * @param targetSource new TargetSource to use
	 */
	void setTargetSource(TargetSource targetSource);

	/**
	 * <p>返回Advised对象使用的TargetSource。</p>
	 * Return the {@code TargetSource} used by this {@code Advised} object.
	 */
	TargetSource getTargetSource();

	/**
	 * <p>设置代理是否应该由AOP框架作为ThreadLocal公开，以便通过AopContext类进行检索。</p>
	 * <p>如果advised对象需要在应用通知的情况下调用自己的方法，则有必要公开代理。否则，如果advised对象调用自己方法，则不会应用任何通知。</p>
	 * <p>默认为false，以获得最佳性能。</p>
	 *
	 * Set whether the proxy should be exposed by the AOP framework as a
	 * {@link ThreadLocal} for retrieval via the {@link AopContext} class.
	 * <p>It can be necessary to expose the proxy if an advised object needs
	 * to invoke a method on itself with advice applied. Otherwise, if an
	 * advised object invokes a method on {@code this}, no advice will be applied.
	 * <p>Default is {@code false}, for optimal performance.
	 */
	void setExposeProxy(boolean exposeProxy);

	/**
	 * <p>返回工厂是否应该将代理公开为ThreadLocal。</p>
	 * Return whether the factory should expose the proxy as a {@link ThreadLocal}.
	 * <p>It can be necessary to expose the proxy if an advised object needs
	 * to invoke a method on itself with advice applied. Otherwise, if an
	 * advised object invokes a method on {@code this}, no advice will be applied.
	 * <p>Getting the proxy is analogous to an EJB calling {@code getEJBObject()}.
	 * @see AopContext
	 */
	boolean isExposeProxy();

	/**
	 * <p>设置此代理配置是否进行预过滤，以便它只包含适用的advisors(与此代理的目标类匹配)。</p>
	 * <p>默认设置是“false”。如果advisor已经进行了预过滤，则将此设置为“true”，
	 * 这意味着在为代理调用构建实际的advisor链时可以跳过ClassFilter检查。</p>
	 *
	 * Set whether this proxy configuration is pre-filtered so that it only
	 * contains applicable advisors (matching this proxy's target class).
	 * <p>Default is "false". Set this to "true" if the advisors have been
	 * pre-filtered already, meaning that the ClassFilter check can be skipped
	 * when building the actual advisor chain for proxy invocations.
	 * @see org.springframework.aop.ClassFilter
	 */
	void setPreFiltered(boolean preFiltered);

	/**
	 * <p>返回此代理配置是否经过预过滤，以便只包含适用的advisors(与此代理的目标类匹配)。</p>
	 * Return whether this proxy configuration is pre-filtered so that it only
	 * contains applicable advisors (matching this proxy's target class).
	 */
	boolean isPreFiltered();

	/**
	 * <p>返回应用到该代理的advisors。</p>
	 * Return the advisors applying to this proxy.
	 * @return a list of Advisors applying to this proxy (never {@code null})
	 */
	Advisor[] getAdvisors();

	/**
	 * <p>返回应用到该代理的advisors的数量。</p>
	 * Return the number of advisors applying to this proxy.
	 * <p>The default implementation delegates to {@code getAdvisors().length}.
	 * @since 5.3.1
	 */
	default int getAdvisorCount() {
		return getAdvisors().length;
	}

	/**
	 * <p>在advisor链的末端添加advisor。</p>
	 * <p>这个Advisor可以是IntroductionAdvisor，当下一次从相关工厂获得代理时，新的接口将可用。</p>
	 *
	 * Add an advisor at the end of the advisor chain.
	 * <p>The Advisor may be an {@link org.springframework.aop.IntroductionAdvisor},
	 * in which new interfaces will be available when a proxy is next obtained
	 * from the relevant factory.
	 * @param advisor the advisor to add to the end of the chain
	 * @throws AopConfigException in case of invalid advice
	 */
	void addAdvisor(Advisor advisor) throws AopConfigException;

	/**
	 * <p>在链中的指定位置添加Advisor。</p>
	 * Add an Advisor at the specified position in the chain.
	 * @param advisor the advisor to add at the specified position in the chain
	 * @param pos position in chain (0 is head). Must be valid.
	 * @throws AopConfigException in case of invalid advice
	 */
	void addAdvisor(int pos, Advisor advisor) throws AopConfigException;

	/**
	 * <p>删除给定的advisor。</p>
	 * Remove the given advisor.
	 * @param advisor the advisor to remove
	 * @return {@code true} if the advisor was removed; {@code false}
	 * if the advisor was not found and hence could not be removed
	 */
	boolean removeAdvisor(Advisor advisor);

	/**
	 * <p>删除给定索引处的顾advisor。</p>
	 * Remove the advisor at the given index.
	 * @param index the index of advisor to remove
	 * @throws AopConfigException if the index is invalid
	 */
	void removeAdvisor(int index) throws AopConfigException;

	/**
	 * <p>返回给定advisor的索引(从0开始)，如果没有这样的advisor应用到这个代理，则返回-1。</p>
	 * <p>此方法的返回值可用于在advisor数组中建立索引。</p>
	 *
	 * Return the index (from 0) of the given advisor,
	 * or -1 if no such advisor applies to this proxy.
	 * <p>The return value of this method can be used to index into the advisors array.
	 * @param advisor the advisor to search for
	 * @return index from 0 of this advisor, or -1 if there's no such advisor
	 */
	int indexOf(Advisor advisor);

	/**
	 * <p>替换给定的advisor。</p>
	 * <p>注意:如果advisor是一个IntroductionAdvisor，而替换的不是或者不是实现了不同的接口，那么需要重新获得代理，
	 * 或者旧的接口将不被支持，新的接口将不会被实现。</p>
	 * Replace the given advisor.
	 * <p><b>Note:</b> If the advisor is an {@link org.springframework.aop.IntroductionAdvisor}
	 * and the replacement is not or implements different interfaces, the proxy will need
	 * to be re-obtained or the old interfaces won't be supported and the new interface
	 * won't be implemented.
	 * @param a the advisor to replace
	 * @param b the advisor to replace it with
	 * @return whether it was replaced. If the advisor wasn't found in the
	 * list of advisors, this method returns {@code false} and does nothing.
	 * @throws AopConfigException in case of invalid advice
	 */
	boolean replaceAdvisor(Advisor a, Advisor b) throws AopConfigException;

	/**
	 * <p>将给定的AOP联盟advice添加到advice(拦截器)链的尾部。</p>
	 * <p>这将被包装在DefaultPointcutAdvisor中，并带有一个始终适用的切入点，并在这个包装的表单中从getAdvisors()方法返回。</p>
	 * <p>注意，给定的advice将适用于代理上的所有调用，甚至适用于toString()方法!使用适当的advice实现或指定适当的切入点来应用于更窄的方法集。</p>
	 *
	 * Add the given AOP Alliance advice to the tail of the advice (interceptor) chain.
	 * <p>This will be wrapped in a DefaultPointcutAdvisor with a pointcut that always
	 * applies, and returned from the {@code getAdvisors()} method in this wrapped form.
	 * <p>Note that the given advice will apply to all invocations on the proxy,
	 * even to the {@code toString()} method! Use appropriate advice implementations
	 * or specify appropriate pointcuts to apply to a narrower set of methods.
	 * @param advice the advice to add to the tail of the chain
	 * @throws AopConfigException in case of invalid advice
	 * @see #addAdvice(int, Advice)
	 * @see org.springframework.aop.support.DefaultPointcutAdvisor
	 */
	void addAdvice(Advice advice) throws AopConfigException;

	/**
	 * <p>在通知链的指定位置添加给定的AOP联盟Advice。</p>
	 * Add the given AOP Alliance Advice at the specified position in the advice chain.
	 * <p>This will be wrapped in a {@link org.springframework.aop.support.DefaultPointcutAdvisor}
	 * with a pointcut that always applies, and returned from the {@link #getAdvisors()}
	 * method in this wrapped form.
	 * <p>Note: The given advice will apply to all invocations on the proxy,
	 * even to the {@code toString()} method! Use appropriate advice implementations
	 * or specify appropriate pointcuts to apply to a narrower set of methods.
	 * @param pos index from 0 (head)
	 * @param advice the advice to add at the specified position in the advice chain
	 * @throws AopConfigException in case of invalid advice
	 */
	void addAdvice(int pos, Advice advice) throws AopConfigException;

	/**
	 * <p>删除包含给定advice的Advisor。</p>
	 * Remove the Advisor containing the given advice.
	 * @param advice the advice to remove
	 * @return {@code true} of the advice was found and removed;
	 * {@code false} if there was no such advice
	 */
	boolean removeAdvice(Advice advice);

	/**
	 * Return the index (from 0) of the given AOP Alliance Advice,
	 * or -1 if no such advice is an advice for this proxy.
	 * <p>The return value of this method can be used to index into
	 * the advisors array.
	 * @param advice the AOP Alliance advice to search for
	 * @return index from 0 of this advice, or -1 if there's no such advice
	 */
	int indexOf(Advice advice);

	/**
	 * <p>由于toString()通常被委托给target，这将返回AOP代理的等效值。</p>
	 * As {@code toString()} will normally be delegated to the target,
	 * this returns the equivalent for the AOP proxy.
	 * @return a string description of the proxy configuration
	 */
	String toProxyConfigString();

}
