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

package org.springframework.context.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.context.weaving.DefaultContextLoadTimeWeaver;
import org.springframework.instrument.classloading.LoadTimeWeaver;

/**
 *
 * <p>为此应用上下文激活一个Spring LoadTimeWeaver，作为名为“loadTimeWeaver”的bean提供，
 * 类似于Spring XML{@code <context:load-time-weaver>}元素。</p>
 *
 * Activates a Spring {@link LoadTimeWeaver} for this application context, available as
 * a bean with the name "loadTimeWeaver", similar to the {@code <context:load-time-weaver>}
 * element in Spring XML.
 *
 * <p>用于@Configuration类;最简单的例子如下:</p>
 * <p>To be used on @{@link org.springframework.context.annotation.Configuration Configuration} classes;
 * the simplest possible example of which follows:
 *
 * <pre class="code">
 * &#064;Configuration
 * &#064;EnableLoadTimeWeaving
 * public class AppConfig {
 *
 *     // application-specific &#064;Bean definitions ...
 * }</pre>
 *
 * <p>上面的例子等价于下面的Spring XML配置:</p>
 * The example above is equivalent to the following Spring XML configuration:
 *
 * <pre class="code">
 * &lt;beans&gt;
 *
 *     &lt;context:load-time-weaver/&gt;
 *
 *     &lt;!-- application-specific &lt;bean&gt; definitions --&gt;
 *
 * &lt;/beans&gt;
 * </pre>
 *
 * <h2>The {@code LoadTimeWeaverAware} interface</h2>
 * <p>任何实现LoadTimeWeaverAware接口的bean都会自动接收LoadTimeWeaver引用;例如，Spring的JPA引导支持。</p>
 * Any bean that implements the {@link
 * org.springframework.context.weaving.LoadTimeWeaverAware LoadTimeWeaverAware} interface
 * will then receive the {@code LoadTimeWeaver} reference automatically; for example,
 * Spring's JPA bootstrap support.
 *
 * <h2>Customizing the {@code LoadTimeWeaver}</h2>
 * <p>默认weaver是自动确定的:参见DefaultContextLoadTimeWeaver。</p>
 * The default weaver is determined automatically: see {@link DefaultContextLoadTimeWeaver}.
 * <p>要自定义使用的weaver，用@EnableLoadTimeWeaving注释的@Configuration类还可以实现LoadTimeWeavingConfigurer接口，
 * 并通过getLoadTimeWeaver方法返回自定义LoadTimeWeaver实例：</p>
 * <p>To customize the weaver used, the {@code @Configuration} class annotated with
 * {@code @EnableLoadTimeWeaving} may also implement the {@link LoadTimeWeavingConfigurer}
 * interface and return a custom {@code LoadTimeWeaver} instance through the
 * {@code #getLoadTimeWeaver} method:
 *
 * <pre class="code">
 * &#064;Configuration
 * &#064;EnableLoadTimeWeaving
 * public class AppConfig implements LoadTimeWeavingConfigurer {
 *
 *     &#064;Override
 *     public LoadTimeWeaver getLoadTimeWeaver() {
 *         MyLoadTimeWeaver ltw = new MyLoadTimeWeaver();
 *         ltw.addClassTransformer(myClassFileTransformer);
 *         // ...
 *         return ltw;
 *     }
 * }</pre>
 *
 * <p>The example above can be compared to the following Spring XML configuration:
 *
 * <pre class="code">
 * &lt;beans&gt;
 *
 *     &lt;context:load-time-weaver weaverClass="com.acme.MyLoadTimeWeaver"/&gt;
 *
 * &lt;/beans&gt;
 * </pre>
 *
 * <p>代码示例不同于XML示例，它实际实例化了MyLoadTimeWeaver类型，这意味着它也可以配置实例，
 * 例如调用#addClassTransformer方法。这演示了如何通过直接编程访问使基于代码的配置方法更加灵活</p>
 * <p>The code example differs from the XML example in that it actually instantiates the
 * {@code MyLoadTimeWeaver} type, meaning that it can also configure the instance, e.g.
 * calling the {@code #addClassTransformer} method. This demonstrates how the code-based
 * configuration approach is more flexible through direct programmatic access.
 *
 * <h2>Enabling AspectJ-based weaving</h2>
 * <p>可以使用aspectjWeaving属性启用AspectJ加载时编织，这将导致通过LoadTimeWeaver.addTransformer注册AspectJ类转换器。
 * 如果类路径上存在“META-INF/aop.xml”资源，则默认情况下将激活AspectJ编织。例子:</p>
 * AspectJ load-time weaving may be enabled with the {@link #aspectjWeaving()}
 * attribute, which will cause the {@linkplain
 * org.aspectj.weaver.loadtime.ClassPreProcessorAgentAdapter AspectJ class transformer} to
 * be registered through {@link LoadTimeWeaver#addTransformer}. AspectJ weaving will be
 * activated by default if a "META-INF/aop.xml" resource is present on the classpath.
 * Example:
 *
 * <pre class="code">
 * &#064;Configuration
 * &#064;EnableLoadTimeWeaving(aspectjWeaving=ENABLED)
 * public class AppConfig {
 * }</pre>
 *
 * <p>The example above can be compared to the following Spring XML configuration:
 *
 * <pre class="code">
 * &lt;beans&gt;
 *
 *     &lt;context:load-time-weaver aspectj-weaving="on"/&gt;
 *
 * &lt;/beans&gt;
 * </pre>
 * <p>这两个示例等价，但有一个重要的例外:在XML情况下，当aspectj编织“on”时，{@code <context:spring-configured>}的功能将隐式启用。
 * 当使用@EnableLoadTimeWeaving(aspectjWeaving=ENABLED)时不会发生这种情况。相反，必须显式添加@EnableSpringConfigured(包含在spring-aspects模块中)。</p>
 * <p>The two examples are equivalent with one significant exception: in the XML case,
 * the functionality of {@code <context:spring-configured>} is implicitly enabled when
 * {@code aspectj-weaving} is "on".  This does not occur when using
 * {@code @EnableLoadTimeWeaving(aspectjWeaving=ENABLED)}. Instead you must explicitly add
 * {@code @EnableSpringConfigured} (included in the {@code spring-aspects} module)
 *
 * @author Chris Beams
 * @since 3.1
 * @see LoadTimeWeaver
 * @see DefaultContextLoadTimeWeaver
 * @see org.aspectj.weaver.loadtime.ClassPreProcessorAgentAdapter
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import(LoadTimeWeavingConfiguration.class)
public @interface EnableLoadTimeWeaving {

	/**
	 * <p>是否应该启用AspectJ编织。</p>
	 * Whether AspectJ weaving should be enabled.
	 */
	AspectJWeaving aspectjWeaving() default AspectJWeaving.AUTODETECT;


	/**
	 * <p>AspectJ编织支持选项</p>
	 * AspectJ weaving enablement options.
	 */
	enum AspectJWeaving {

		/**
		 * <p>打开基于spring基础上的AspectJ加载时编织</p>
		 * Switches on Spring-based AspectJ load-time weaving.
		 */
		ENABLED,

		/**
		 * <p>关闭基于spring基础上的AspectJ加载时编织(即使类路径中存在“META-INF/aop.xml”资源)</p>
		 * Switches off Spring-based AspectJ load-time weaving (even if a
		 * "META-INF/aop.xml" resource is present on the classpath).
		 */
		DISABLED,

		/**
		 * <p>如果类路径中存在“META-INF/aop.xml”资源，则打开AspectJ加载时编织。如果没有这样的资源，那么AspectJ加载时编织将被关闭。</p>
		 * Switches on AspectJ load-time weaving if a "META-INF/aop.xml" resource
		 * is present in the classpath. If there is no such resource, then AspectJ
		 * load-time weaving will be switched off.
		 */
		AUTODETECT;
	}

}
