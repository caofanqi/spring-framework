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

package org.springframework.web.filter;

import java.io.IOException;

import javax.servlet.DispatcherType;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.context.request.async.WebAsyncManager;
import org.springframework.web.context.request.async.WebAsyncUtils;
import org.springframework.web.util.WebUtils;

/**
 * <p>过滤器基类，目的是保证在任何servlet容器上每个请求调度的单个执行。
 * 它提供了一个带有HttpServletRequest和HttpServletResponse参数的doFilterInternal方法。</p>
 * Filter base class that aims to guarantee a single execution per request
 * dispatch, on any servlet container. It provides a {@link #doFilterInternal}
 * method with HttpServletRequest and HttpServletResponse arguments.
 *
 * <p>在Servlet 3.0中，过滤器可以作为单独线程中的请求或异步调度的一部分被调用。
 * 可以在web.xml中配置过滤器是否应该参与异步调度。然而，在某些情况下servlet容器采用不同的默认配置。
 * 因此，子类可以覆盖shouldNotFilterAsyncDispatch()方法来静态声明，如果它们确实应该在两种类型的分派期间被调用一次，
 * 以便提供线程初始化、日志记录、安全性等等。这种机制是对配置需求的补充，而不是替代</p>
 * <p>As of Servlet 3.0, a filter may be invoked as part of a
 * {@link javax.servlet.DispatcherType#REQUEST REQUEST} or
 * {@link javax.servlet.DispatcherType#ASYNC ASYNC} dispatches that occur in
 * separate threads. A filter can be configured in {@code web.xml} whether it
 * should be involved in async dispatches. However, in some cases servlet
 * containers assume different default configuration. Therefore sub-classes can
 * override the method {@link #shouldNotFilterAsyncDispatch()} to declare
 * statically if they should indeed be invoked, <em>once</em>, during both types
 * of dispatches in order to provide thread initialization, logging, security,
 * and so on. This mechanism complements and does not replace the need to
 * configure a filter in {@code web.xml} with dispatcher types.
 *
 * <p>子类可以使用isAsyncDispatch(HttpServletRequest)来确定当调用filter作为异步分派的一部分,
 * 并使用isAsyncStarted(HttpServletRequest)来确定当请求被放置在异步模式下,因此当前调度不会是最后一个给定的请求。</p>
 * <p>Subclasses may use {@link #isAsyncDispatch(HttpServletRequest)} to
 * determine when a filter is invoked as part of an async dispatch, and use
 * {@link #isAsyncStarted(HttpServletRequest)} to determine when the request
 * has been placed in async mode and therefore the current dispatch won't be
 * the last one for the given request.
 *
 * <p>另一种分派类型也发生在它自己的线程中，那就是ERROR。子类可以覆盖shouldNotFilterErrorDispatch()，
 * 如果它们希望静态声明，如果它们应该在错误分派期间调用一次。</p>
 * <p>Yet another dispatch type that also occurs in its own thread is
 * {@link javax.servlet.DispatcherType#ERROR ERROR}. Subclasses can override
 * {@link #shouldNotFilterErrorDispatch()} if they wish to declare statically
 * if they should be invoked <em>once</em> during error dispatches.
 *
 * <p>getAlreadyFilteredAttributeName方法确定如何识别一个请求已经被过滤了。默认实现基于具体过滤器实例的配置名称。</p>
 * <p>The {@link #getAlreadyFilteredAttributeName} method determines how to
 * identify that a request is already filtered. The default implementation is
 * based on the configured name of the concrete filter instance.
 *
 * @author Juergen Hoeller
 * @author Rossen Stoyanchev
 * @since 06.12.2003
 */
public abstract class OncePerRequestFilter extends GenericFilterBean {

	/**
	 * <p>附加到“已过滤”请求属性的过滤器名称的后缀。</p>
	 * Suffix that gets appended to the filter name for the
	 * "already filtered" request attribute.
	 * @see #getAlreadyFilteredAttributeName
	 */
	public static final String ALREADY_FILTERED_SUFFIX = ".FILTERED";


	/**
	 * <p>这个doFilter实现存储了一个“已过滤”的请求属性，如果该属性已经存在，则无需再次过滤。</p>
	 * This {@code doFilter} implementation stores a request attribute for
	 * "already filtered", proceeding without filtering again if the
	 * attribute is already there.
	 * @see #getAlreadyFilteredAttributeName
	 * @see #shouldNotFilter
	 * @see #doFilterInternal
	 */
	@Override
	public final void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {

		if (!(request instanceof HttpServletRequest) || !(response instanceof HttpServletResponse)) {
			throw new ServletException("OncePerRequestFilter just supports HTTP requests");
		}
		HttpServletRequest httpRequest = (HttpServletRequest) request;
		HttpServletResponse httpResponse = (HttpServletResponse) response;

		// 获取已经处理的请求处理属性名称
		String alreadyFilteredAttributeName = getAlreadyFilteredAttributeName();
		// 通过判断当前请求是否包含该属性值，来判断
		boolean hasAlreadyFilteredAttribute = request.getAttribute(alreadyFilteredAttributeName) != null;

		if (skipDispatch(httpRequest) || shouldNotFilter(httpRequest)) {
			// 判断是否跳过调度，或不执行该Filter

			// Proceed without invoking this filter...
			// 不调用此Filter继续执行…
			filterChain.doFilter(request, response);
		}
		else if (hasAlreadyFilteredAttribute) {
			// 该Filter已经处理过一次了
			if (DispatcherType.ERROR.equals(request.getDispatcherType())) {
				// 如果请求调度是ERROR
				doFilterNestedErrorDispatch(httpRequest, httpResponse, filterChain);
				return;
			}

			// Proceed without invoking this filter...
			// 不调用此Filter继续执行…
			filterChain.doFilter(request, response);
		}
		else {
			// Do invoke this filter...
			// 请调用这个filter…
			// 设置已处理标识
			request.setAttribute(alreadyFilteredAttributeName, Boolean.TRUE);
			try {
				// 执行内部过滤反法
				doFilterInternal(httpRequest, httpResponse, filterChain);
			}
			finally {
				// Remove the "already filtered" request attribute for this request.
				// 删除此请求的“已过滤”请求属性。
				request.removeAttribute(alreadyFilteredAttributeName);
			}
		}
	}

	private boolean skipDispatch(HttpServletRequest request) {
		if (isAsyncDispatch(request) && shouldNotFilterAsyncDispatch()) {
			// 该请求是ASYNC的并且shouldNotFilterAsyncDispatch()返回true
			return true;
		}
		if (request.getAttribute(WebUtils.ERROR_REQUEST_URI_ATTRIBUTE) != null && shouldNotFilterErrorDispatch()) {
			// request中包含javax.servlet.error.request_uri属性值，并且shouldNotFilterErrorDispatch()返回true
			return true;
		}
		return false;
	}

	/**
	 * <p>Servlet 3.0中引入的dispatcher类型javax.servlet.DispatcherType.ASYNC意味着在单个请求过程中可以在多个线程中调用过滤器。
	 * 如果筛选器当前正在异步调度中执行，则此方法返回true。</p>
	 * The dispatcher type {@code javax.servlet.DispatcherType.ASYNC} introduced
	 * in Servlet 3.0 means a filter can be invoked in more than one thread over
	 * the course of a single request. This method returns {@code true} if the
	 * filter is currently executing within an asynchronous dispatch.
	 * @param request the current request
	 * @since 3.2
	 * @see WebAsyncManager#hasConcurrentResult()
	 */
	protected boolean isAsyncDispatch(HttpServletRequest request) {
		return DispatcherType.ASYNC.equals(request.getDispatcherType());
	}

	/**
	 * Whether request processing is in asynchronous mode meaning that the
	 * response will not be committed after the current thread is exited.
	 * @param request the current request
	 * @since 3.2
	 * @see WebAsyncManager#isConcurrentHandlingStarted()
	 */
	protected boolean isAsyncStarted(HttpServletRequest request) {
		return WebAsyncUtils.getAsyncManager(request).isConcurrentHandlingStarted();
	}

	/**
	 * <p>返回请求属性的名称，该属性标识一个请求已经被过滤。</p>
	 * <p>默认实现采用具体过滤器实例的配置名称，并附加“.FILTERED”。如果筛选器没有完全初始化，它将退回到它的类名。</p>
	 * Return the name of the request attribute that identifies that a request
	 * is already filtered.
	 * <p>The default implementation takes the configured name of the concrete filter
	 * instance and appends ".FILTERED". If the filter is not fully initialized,
	 * it falls back to its class name.
	 * @see #getFilterName
	 * @see #ALREADY_FILTERED_SUFFIX
	 */
	protected String getAlreadyFilteredAttributeName() {
		String name = getFilterName();
		if (name == null) {
			name = getClass().getName();
		}
		return name + ALREADY_FILTERED_SUFFIX;
	}

	/**
	 * <p>可以在自定义过滤控件的子类中重写，返回true以避免过滤给定的请求。</p>
	 * <p>默认实现总是返回false。</p>
	 * Can be overridden in subclasses for custom filtering control,
	 * returning {@code true} to avoid filtering of the given request.
	 * <p>The default implementation always returns {@code false}.
	 * @param request current HTTP request
	 * @return whether the given request should <i>not</i> be filtered
	 * @throws ServletException in case of errors
	 */
	protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
		return false;
	}

	/**
	 * <p>Servlet 3.0中引入的dispatcher类型javax.servlet.DispatcherType.ASYNC意味着在单个请求过程中可以在多个线程中调用过滤器。
	 * 一些过滤器只需要过滤初始线程(例如，请求包装)，而另一些可能需要在每个额外的线程中至少调用一次，例如设置线程局部变量或在最后执行最终处理。</p>
	 * <p>请注意，尽管可以通过web.xml或在Java中通过ServletContext映射过滤器来处理特定的调度器类型，
	 * 但servlet容器可能针对调度器类型强制执行不同的默认值。此标志强制执行过滤器的设计意图。
	 * 默认返回值是“true”，这意味着在后续的异步调度中不会调用过滤器。如果“false”，过滤器将在异步调度期间被调用，并保证在单个线程的请求中只被调用一次。</p>
	 * The dispatcher type {@code javax.servlet.DispatcherType.ASYNC} introduced
	 * in Servlet 3.0 means a filter can be invoked in more than one thread
	 * over the course of a single request. Some filters only need to filter
	 * the initial thread (e.g. request wrapping) while others may need
	 * to be invoked at least once in each additional thread for example for
	 * setting up thread locals or to perform final processing at the very end.
	 * <p>Note that although a filter can be mapped to handle specific dispatcher
	 * types via {@code web.xml} or in Java through the {@code ServletContext},
	 * servlet containers may enforce different defaults with regards to
	 * dispatcher types. This flag enforces the design intent of the filter.
	 * <p>The default return value is "true", which means the filter will not be
	 * invoked during subsequent async dispatches. If "false", the filter will
	 * be invoked during async dispatches with the same guarantees of being
	 * invoked only once during a request within a single thread.
	 * @since 3.2
	 */
	protected boolean shouldNotFilterAsyncDispatch() {
		return true;
	}

	/**
	 * <p>是否过滤错误分派，例如servlet容器何时处理和错误在web.xml中映射。默认返回值是“true”，这意味着在错误分派的情况下不会调用筛选器。</p>
	 * Whether to filter error dispatches such as when the servlet container
	 * processes and error mapped in {@code web.xml}. The default return value
	 * is "true", which means the filter will not be invoked in case of an error
	 * dispatch.
	 * @since 3.2
	 */
	protected boolean shouldNotFilterErrorDispatch() {
		return true;
	}


	/**
	 * <p>与doFilter相同的契约，但保证在单个请求线程中每个请求只调用一次。详情请参阅shouldNotFilterAsyncDispatch()。</p>
	 * <p>提供了HttpServletRequest和HttpServletResponse参数，而不是默认的ServletRequest和ServletResponse参数。</p>
	 * Same contract as for {@code doFilter}, but guaranteed to be
	 * just invoked once per request within a single request thread.
	 * See {@link #shouldNotFilterAsyncDispatch()} for details.
	 * <p>Provides HttpServletRequest and HttpServletResponse arguments instead of the
	 * default ServletRequest and ServletResponse ones.
	 */
	protected abstract void doFilterInternal(
			HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException;

	/**
	 * <p>通常在请求分派完成后，错误分派会发生，并且过滤器链会重新启动。
	 * 然而，在一些服务器上，错误调度可能嵌套在请求调度中，例如，由于在响应上调用sendError。
	 * 在这种情况下，我们仍然在过滤器链中，在同一个线程上，但是请求和响应已经切换到原始的、未包装的。</p>
	 * <p>子类可以使用此方法来过滤此类嵌套的错误分派，并对请求或响应重新应用包装。
	 * ThreadLocal上下文(如果有的话)应该仍然是活动的，因为我们仍然嵌套在过滤器链中。</p>
	 * Typically an ERROR dispatch happens after the REQUEST dispatch completes,
	 * and the filter chain starts anew. On some servers however the ERROR
	 * dispatch may be nested within the REQUEST dispatch, e.g. as a result of
	 * calling {@code sendError} on the response. In that case we are still in
	 * the filter chain, on the same thread, but the request and response have
	 * been switched to the original, unwrapped ones.
	 * <p>Sub-classes may use this method to filter such nested ERROR dispatches
	 * and re-apply wrapping on the request or response. {@code ThreadLocal}
	 * context, if any, should still be active as we are still nested within
	 * the filter chain.
	 * @since 5.1.9
	 */
	protected void doFilterNestedErrorDispatch(HttpServletRequest request, HttpServletResponse response,
			FilterChain filterChain) throws ServletException, IOException {

		filterChain.doFilter(request, response);
	}

}
