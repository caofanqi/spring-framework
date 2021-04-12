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

package org.springframework.core;

/**
 * <p>接口，通过装饰代理来实现，特别是Spring AOP代理，但也可能使用装饰语义定制代理。</p>
 * <p>请注意，如果被装饰的类不属于最初的代理类的层次结构，则应该只实现这个接口。
 * 特别是，像Spring AOP CGLIB代理这样的“目标类”代理不应该实现它，因为在目标类上的任何查找都可以简单地在那里的代理类上执行。</p>
 * <p>为了允许AnnotationAwareOrderComparator(以及其他潜在的没有spring-aop依赖关系的候选对象)用于自检目的，特别是用于注释查找。</p>
 *
 * Interface to be implemented by decorating proxies, in particular Spring AOP
 * proxies but potentially also custom proxies with decorator semantics.
 *
 * <p>Note that this interface should just be implemented if the decorated class
 * is not within the hierarchy of the proxy class to begin with. In particular,
 * a "target-class" proxy such as a Spring AOP CGLIB proxy should not implement
 * it since any lookup on the target class can simply be performed on the proxy
 * class there anyway.
 *
 * <p>Defined in the core module in order to allow
 * {@link org.springframework.core.annotation.AnnotationAwareOrderComparator}
 * (and potential other candidates without spring-aop dependencies) to use it
 * for introspection purposes, in particular annotation lookups.
 *
 * @author Juergen Hoeller
 * @since 4.3
 */
public interface DecoratingProxy {

	/**
	 * <p>返回该代理背后的(最终)修饰类。</p>
	 * <p>对于AOP代理，这将是最终的目标类，而不仅仅是直接的目标(在多个嵌套代理的情况下)。</p>
	 * Return the (ultimate) decorated class behind this proxy.
	 * <p>In case of an AOP proxy, this will be the ultimate target class,
	 * not just the immediate target (in case of multiple nested proxies).
	 * @return the decorated class (never {@code null})
	 */
	Class<?> getDecoratedClass();

}
