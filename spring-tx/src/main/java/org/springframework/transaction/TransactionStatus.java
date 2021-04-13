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

package org.springframework.transaction;

import java.io.Flushable;

/**
 * <p>事务状态的表示。</p>
 * <p>事务代码可以使用它来检索状态信息，并以编程方式请求回滚(而不是抛出导致隐式回滚的异常)。</p>
 * <p>包括SavepointManager接口，以提供对保存点管理工具的访问。请注意，只有在底层事务管理器支持的情况下，保存点管理才可用。</p>
 *
 * Representation of the status of a transaction.
 *
 * <p>Transactional code can use this to retrieve status information,
 * and to programmatically request a rollback (instead of throwing
 * an exception that causes an implicit rollback).
 *
 * <p>Includes the {@link SavepointManager} interface to provide access
 * to savepoint management facilities. Note that savepoint management
 * is only available if supported by the underlying transaction manager.
 *
 * @author Juergen Hoeller
 * @since 27.03.2003
 * @see #setRollbackOnly()
 * @see PlatformTransactionManager#getTransaction
 * @see org.springframework.transaction.support.TransactionCallback#doInTransaction
 * @see org.springframework.transaction.interceptor.TransactionInterceptor#currentTransactionStatus()
 */
public interface TransactionStatus extends TransactionExecution, SavepointManager, Flushable {

	/**
	 * <p>返回该事务是否在内部携带保存点，也就是说，已根据保存点创建嵌套事务。</p>
	 * <p>这个方法主要用于诊断，还有isNewTransaction()。对于自定义保存点的编程处理，请使用SavepointManager提供的操作。</p>
	 *
	 * Return whether this transaction internally carries a savepoint,
	 * that is, has been created as nested transaction based on a savepoint.
	 * <p>This method is mainly here for diagnostic purposes, alongside
	 * {@link #isNewTransaction()}. For programmatic handling of custom
	 * savepoints, use the operations provided by {@link SavepointManager}.
	 * @see #isNewTransaction()
	 * @see #createSavepoint()
	 * @see #rollbackToSavepoint(Object)
	 * @see #releaseSavepoint(Object)
	 */
	boolean hasSavepoint();

	/**
	 * <p>如果适用的话，将底层会话刷新到数据存储区:例如，所有受影响的Hibernate/JPA会话。</p>
	 * <p>这实际上只是一个提示，如果底层事务管理器没有flush概念，这可能是一个空操作。刷新信号可能应用于主资源或事务同步，这取决于底层资源。</p>
	 * Flush the underlying session to the datastore, if applicable:
	 * for example, all affected Hibernate/JPA sessions.
	 * <p>This is effectively just a hint and may be a no-op if the underlying
	 * transaction manager does not have a flush concept. A flush signal may
	 * get applied to the primary resource or to transaction synchronizations,
	 * depending on the underlying resource.
	 */
	@Override
	void flush();

}
