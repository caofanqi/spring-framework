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

package org.springframework.web;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;

/**
 * <p>接口，在Servlet 3.0+环境中实现，以便以编程方式配置ServletContext——这与(或可能与)传统的基于web.xml的方法相反。</p>
 * Interface to be implemented in Servlet 3.0+ environments in order to configure the
 * {@link ServletContext} programmatically -- as opposed to (or possibly in conjunction
 * with) the traditional {@code web.xml}-based approach.
 *
 * <p>这个SPI的实现将由SpringServletContainerInitializer自动检测，
 * 而SpringServletContainerInitializer本身是由任何Servlet 3.0容器自动引导的。有关这个引导机制的详细信息，请参阅其Javadoc。</p>
 * <p>Implementations of this SPI will be detected automatically by {@link
 * SpringServletContainerInitializer}, which itself is bootstrapped automatically
 * by any Servlet 3.0 container. See {@linkplain SpringServletContainerInitializer its
 * Javadoc} for details on this bootstrapping mechanism.
 *
 * <h2>Example</h2>
 * <h3>The traditional, XML-based approach</h3>
 * <p>大多数构建web应用程序的Spring用户都需要注册Spring的DispatcherServlet。作为参考，在WEB-INF/web.xml中，这通常是这样做的:</p>
 * Most Spring users building a web application will need to register Spring's {@code
 * DispatcherServlet}. For reference, in WEB-INF/web.xml, this would typically be done as
 * follows:
 * <pre class="code">
 * &lt;servlet&gt;
 *   &lt;servlet-name>dispatcher&lt;/servlet-name&gt;
 *   &lt;servlet-class&gt;
 *     org.springframework.web.servlet.DispatcherServlet
 *   &lt;/servlet-class&gt;
 *   &lt;init-param>
 *     &lt;param-name>contextConfigLocation&lt;/param-name&gt;
 *     &lt;param-value>/WEB-INF/spring/dispatcher-config.xml&lt;/param-value&gt;
 *   &lt;/init-param&gt;
 *   &lt;load-on-startup>1&lt;/load-on-startup&gt;
 * &lt;/servlet&gt;
 *
 * &lt;servlet-mapping&gt;
 *   &lt;servlet-name&gt;dispatcher&lt;/servlet-name&gt;
 *   &lt;url-pattern&gt;/&lt;/url-pattern&gt;
 * &lt;/servlet-mapping&gt;</pre>
 *
 * <h3>The code-based approach with {@code WebApplicationInitializer}</h3>
 * <p>下面是等价的DispatcherServlet注册逻辑，WebApplicationInitializer样式:</p>
 * Here is the equivalent {@code DispatcherServlet} registration logic,
 * {@code WebApplicationInitializer}-style:
 * <pre class="code">
 * public class MyWebAppInitializer implements WebApplicationInitializer {
 *
 *    &#064;Override
 *    public void onStartup(ServletContext container) {
 *      XmlWebApplicationContext appContext = new XmlWebApplicationContext();
 *      appContext.setConfigLocation("/WEB-INF/spring/dispatcher-config.xml");
 *
 *      ServletRegistration.Dynamic dispatcher =
 *        container.addServlet("dispatcher", new DispatcherServlet(appContext));
 *      dispatcher.setLoadOnStartup(1);
 *      dispatcher.addMapping("/");
 *    }
 *
 * }</pre>
 *
 * <p>作为上述方法的替代，你也可以从org.springframework.web.servlet.support.AbstractDispatcherServletInitializer进行扩展。
 * 正如您所看到的，这多亏了Servlet 3.0的新ServletContext。我们实际上注册了DispatcherServlet的一个实例，这意味着现在可以像对待任何
 * 其他对象一样对待DispatcherServlet--在这种情况下，接收其应用上下文的构造函数注入。
 * </p>
 * As an alternative to the above, you can also extend from {@link
 * org.springframework.web.servlet.support.AbstractDispatcherServletInitializer}.
 *
 * As you can see, thanks to Servlet 3.0's new {@link ServletContext#addServlet} method
 * we're actually registering an <em>instance</em> of the {@code DispatcherServlet}, and
 * this means that the {@code DispatcherServlet} can now be treated like any other object
 * -- receiving constructor injection of its application context in this case.
 *
 * <p>这种风格既简单又简洁。无需处理初始化参数等，只需处理普通的javabean样式的属性和构造函数参数。
 * 在将Spring应用程序上下文注入到DispatcherServlet之前，您可以根据需要自由地创建和使用它们。</p>
 * <p>This style is both simpler and more concise. There is no concern for dealing with
 * init-params, etc, just normal JavaBean-style properties and constructor arguments. You
 * are free to create and work with your Spring application contexts as necessary before
 * injecting them into the {@code DispatcherServlet}.
 *
 * <p>大多数主要的Spring Web组件已经更新以支持这种注册风格。您会发现DispatcherServlet、FrameworkServlet、
 * ContextLoaderListener和DelegatingFilterProxy现在都支持构造函数参数。即使组件(例如，非spring，其他第三方)
 * 没有被特别更新以在WebApplicationInitializer中使用，它们仍然可以在任何情况下使用。
 * Servlet 3.0 ServletContext API允许通过编程方式设置初始化参数、上下文参数等</p>
 *
 * <p>Most major Spring Web components have been updated to support this style of
 * registration.  You'll find that {@code DispatcherServlet}, {@code FrameworkServlet},
 * {@code ContextLoaderListener} and {@code DelegatingFilterProxy} all now support
 * constructor arguments. Even if a component (e.g. non-Spring, other third party) has not
 * been specifically updated for use within {@code WebApplicationInitializers}, they still
 * may be used in any case. The Servlet 3.0 {@code ServletContext} API allows for setting
 * init-params, context-params, etc programmatically.
 *
 * <h2>A 100% code-based approach to configuration</h2>
 * <p>在上面的例子中，WEB-INF/web.xml被成功地替换为WebApplicationInitializer形式的代码，
 * 但是实际的dispatcher-config.xml Spring配置仍然是基于xml的。WebApplicationInitializer
 * 非常适合与Spring基于代码的@Configuration类一起使用。请参阅@Configuration Javadoc以获得完整的细节，
 * 但是下面的示例演示了重构，以使用Spring的AnnotationConfigWebApplicationContext来代替XmlWebApplicationContext，
 * 以及用户定义的@Configuration类AppConfig和DispatcherConfig来代替Spring的XML文件。
 * 这个例子也超越了上面的例子，演示了“根”应用上下文的典型配置和ContextLoaderListener的注册:
 * </p>
 *
 * In the example above, {@code WEB-INF/web.xml} was successfully replaced with code in
 * the form of a {@code WebApplicationInitializer}, but the actual
 * {@code dispatcher-config.xml} Spring configuration remained XML-based.
 * {@code WebApplicationInitializer} is a perfect fit for use with Spring's code-based
 * {@code @Configuration} classes. See @{@link
 * org.springframework.context.annotation.Configuration Configuration} Javadoc for
 * complete details, but the following example demonstrates refactoring to use Spring's
 * {@link org.springframework.web.context.support.AnnotationConfigWebApplicationContext
 * AnnotationConfigWebApplicationContext} in lieu of {@code XmlWebApplicationContext}, and
 * user-defined {@code @Configuration} classes {@code AppConfig} and
 * {@code DispatcherConfig} instead of Spring XML files. This example also goes a bit
 * beyond those above to demonstrate typical configuration of the 'root' application
 * context and registration of the {@code ContextLoaderListener}:
 * <pre class="code">
 * public class MyWebAppInitializer implements WebApplicationInitializer {
 *
 *    &#064;Override
 *    public void onStartup(ServletContext container) {
 *      // Create the 'root' Spring application context
 *      AnnotationConfigWebApplicationContext rootContext =
 *        new AnnotationConfigWebApplicationContext();
 *      rootContext.register(AppConfig.class);
 *
 *      // Manage the lifecycle of the root application context
 *      container.addListener(new ContextLoaderListener(rootContext));
 *
 *      // Create the dispatcher servlet's Spring application context
 *      AnnotationConfigWebApplicationContext dispatcherContext =
 *        new AnnotationConfigWebApplicationContext();
 *      dispatcherContext.register(DispatcherConfig.class);
 *
 *      // Register and map the dispatcher servlet
 *      ServletRegistration.Dynamic dispatcher =
 *        container.addServlet("dispatcher", new DispatcherServlet(dispatcherContext));
 *      dispatcher.setLoadOnStartup(1);
 *      dispatcher.addMapping("/");
 *    }
 *
 * }</pre>
 *
 * <p>作为上述方法的替代方案，你也可以从AbstractAnnotationConfigDispatcherServletInitializer扩展。
 * 请记住，WebApplicationInitializer实现是自动检测到的——因此您可以根据需要将它们打包到应用程序中。</p>
 * As an alternative to the above, you can also extend from {@link
 * org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer}.
 *
 * Remember that {@code WebApplicationInitializer} implementations are <em>detected
 * automatically</em> -- so you are free to package them within your application as you
 * see fit.
 *
 * <h2>Ordering {@code WebApplicationInitializer} execution</h2>
 * <p>WebApplicationInitializer实现可以在类级上使用Spring的@Order注释，也可以实现Spring的Ordered接口。
 * 如果是这样，初始化器将在调用之前排序。这为用户提供了一种机制，以确保servlet容器初始化发生的顺序。
 * 预计很少使用此特性，因为典型的应用程序可能会将所有容器初始化集中在一个WebApplicationInitializer中。</p>
 * {@code WebApplicationInitializer} implementations may optionally be annotated at the
 * class level with Spring's @{@link org.springframework.core.annotation.Order Order}
 * annotation or may implement Spring's {@link org.springframework.core.Ordered Ordered}
 * interface. If so, the initializers will be ordered prior to invocation. This provides
 * a mechanism for users to ensure the order in which servlet container initialization
 * occurs. Use of this feature is expected to be rare, as typical applications will likely
 * centralize all container initialization within a single {@code WebApplicationInitializer}.
 *
 * <h2>Caveats</h2>
 *
 * <h3>web.xml versioning</h3>
 * <p>WEB-INF/web.xml和WebApplicationInitializer使用不是互斥的;
 * 例如，web.xml可以注册一个servlet，而WebApplicationInitializer可以注册另一个servlet。初始化器甚至
 * 可以通过ServletContext.getServletRegistration(String)等方法修改在web.xml中执行的注册。
 * 但是，如果WEB-INF/web.xml存在于应用程序中，它的version属性必须设置为"3.0"或更高，否则ServletContainerInitializer引导将被servlet容器忽略。</p>
 * <p>{@code WEB-INF/web.xml} and {@code WebApplicationInitializer} use are not mutually
 * exclusive; for example, web.xml can register one servlet, and a {@code
 * WebApplicationInitializer} can register another. An initializer can even
 * <em>modify</em> registrations performed in {@code web.xml} through methods such as
 * {@link ServletContext#getServletRegistration(String)}. <strong>However, if
 * {@code WEB-INF/web.xml} is present in the application, its {@code version} attribute
 * must be set to "3.0" or greater, otherwise {@code ServletContainerInitializer}
 * bootstrapping will be ignored by the servlet container.</strong>
 *
 * <h3>Mapping to '/' under Tomcat</h3>
 * <p>Apache Tomcat将其内部的DefaultServlet映射到"/"，在Tomcat版本<= 7.0.14上，这个servlet映射不能以编程方式覆盖。
 * 7.0.15修复此问题。覆盖“/”servlet映射也在GlassFish 3.1中成功测试过。</p>
 * <p>Apache Tomcat maps its internal {@code DefaultServlet} to "/", and on Tomcat versions
 * &lt;= 7.0.14, this servlet mapping <em>cannot be overridden programmatically</em>.
 * 7.0.15 fixes this issue. Overriding the "/" servlet mapping has also been tested
 * successfully under GlassFish 3.1.<p>
 *
 * @author Chris Beams
 * @since 3.1
 * @see SpringServletContainerInitializer
 * @see org.springframework.web.context.AbstractContextLoaderInitializer
 * @see org.springframework.web.servlet.support.AbstractDispatcherServletInitializer
 * @see org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer
 */
public interface WebApplicationInitializer {

	/**
	 * <p>使用初始化此web应用程序所需的任何servlet、过滤器、侦听器上下文参数和属性配置给定的ServletContext。参见上面的例子。</p>
	 *
	 * Configure the given {@link ServletContext} with any servlets, filters, listeners
	 * context-params and attributes necessary for initializing this web application. See
	 * examples {@linkplain WebApplicationInitializer above}.
	 * @param servletContext the {@code ServletContext} to initialize
	 * @throws ServletException if any call against the given {@code ServletContext}
	 * throws a {@code ServletException}
	 */
	void onStartup(ServletContext servletContext) throws ServletException;

}
