/*
 * Copyright 2002-2021 the original author or authors.
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

import org.aopalliance.aop.Advice;

/**
 * <p>AOP联盟Advice的子接口，允许通过Advice实现额外的接口，并通过使用该拦截器的代理可用。这是一个基本的AOP概念，称为introduction。</p>
 * <p>Introductions通常是mixin，可以构建复合对象，从而实现Java中多重继承的许多目标。</p>
 * <p>与IntroductionInfo相比，这个接口允许advice实现一系列预先不需要知道的接口。
 * 因此，可以使用IntroductionAdvisor来指定将在被通知对象中公开哪些接口。</p>
 *
 * Subinterface of AOP Alliance Advice that allows additional interfaces
 * to be implemented by an Advice, and available via a proxy using that
 * interceptor. This is a fundamental AOP concept called <b>introduction</b>.
 *
 * <p>Introductions are often <b>mixins</b>, enabling the building of composite
 * objects that can achieve many of the goals of multiple inheritance in Java.
 *
 * <p>Compared to {@link IntroductionInfo}, this interface allows an advice to
 * implement a range of interfaces that is not necessarily known in advance.
 * Thus an {@link IntroductionAdvisor} can be used to specify which interfaces
 * will be exposed in an advised object.
 *
 * @author Rod Johnson
 * @since 1.1.1
 * @see IntroductionInfo
 * @see IntroductionAdvisor
 */
public interface DynamicIntroductionAdvice extends Advice {

	/**
	 * <p>这个引入建议实现了给定的接口吗?</p>
	 * Does this introduction advice implement the given interface?
	 * @param intf the interface to check
	 * @return whether the advice implements the specified interface
	 */
	boolean implementsInterface(Class<?> intf);

}
