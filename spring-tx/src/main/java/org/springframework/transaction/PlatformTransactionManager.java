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

package org.springframework.transaction;

import org.springframework.lang.Nullable;

/**
 * <p>这是Spring命令式事务基础设施中的中心接口。应用程序可以直接使用它，但它主要不是作为API:
 * 通常，应用程序将通过AOP使用TransactionTemplate或声明式事务界定。</p>
 *
 * This is the central interface in Spring's imperative transaction infrastructure.
 * Applications can use this directly, but it is not primarily meant as an API:
 * Typically, applications will work with either TransactionTemplate or
 * declarative transaction demarcation through AOP.
 *
 * <p>对于实现者，建议从所提供的AbstractPlatformTransactionManager类派生，该类预实现已定义的传播行为，并负责事务同步处理。
 * 子类必须为基础事务的特定状态实现模板方法，例如:begin, suspend, resume, commit。</p>
 *
 * <p>For implementors, it is recommended to derive from the provided
 * {@link org.springframework.transaction.support.AbstractPlatformTransactionManager}
 * class, which pre-implements the defined propagation behavior and takes care
 * of transaction synchronization handling. Subclasses have to implement
 * template methods for specific states of the underlying transaction,
 * for example: begin, suspend, resume, commit.
 *
 * <p>这个策略接口的默认实现是JtaTransactionManager和DataSourceTransactionManager，它们可以作为其他事务策略的实现指南。</p>
 *
 * <p>The default implementations of this strategy interface are
 * {@link org.springframework.transaction.jta.JtaTransactionManager} and
 * {@link org.springframework.jdbc.datasource.DataSourceTransactionManager},
 * which can serve as an implementation guide for other transaction strategies.
 *
 * @author Rod Johnson
 * @author Juergen Hoeller
 * @since 16.05.2003
 * @see org.springframework.transaction.support.TransactionTemplate
 * @see org.springframework.transaction.interceptor.TransactionInterceptor
 * @see org.springframework.transaction.ReactiveTransactionManager
 */
public interface PlatformTransactionManager extends TransactionManager {

	/**
	 * <p>根据指定的传播行为，返回当前活动的事务或创建一个新的事务。</p>
	 * Return a currently active transaction or create a new one, according to
	 * the specified propagation behavior.
	 * <p>请注意，隔离级别或超时等参数将仅应用于新事务，因此在参与活动事务时将被忽略。</p>
	 * <p>Note that parameters like isolation level or timeout will only be applied
	 * to new transactions, and thus be ignored when participating in active ones.
	 * <p>而且，并不是每个事务管理器都支持所有的事务定义设置:当遇到不支持的设置时，适当的事务管理器实现应该抛出异常。</p>
	 * <p>Furthermore, not all transaction definition settings will be supported
	 * by every transaction manager: A proper transaction manager implementation
	 * should throw an exception when unsupported settings are encountered.
	 * <p>上述规则的一个例外是read-only标志，如果不支持显式的只读模式，则应该忽略该标志。从本质上讲，只读标志只是潜在优化的提示。</p>
	 * <p>An exception to the above rule is the read-only flag, which should be
	 * ignored if no explicit read-only mode is supported. Essentially, the
	 * read-only flag is just a hint for potential optimization.
	 * @param definition the TransactionDefinition instance (can be {@code null} for defaults),
	 * describing propagation behavior, isolation level, timeout etc.
	 * @return transaction status object representing the new or current transaction
	 * @throws TransactionException in case of lookup, creation, or system errors
	 * @throws IllegalTransactionStateException if the given transaction definition
	 * cannot be executed (for example, if a currently active transaction is in
	 * conflict with the specified propagation behavior)
	 * @see TransactionDefinition#getPropagationBehavior
	 * @see TransactionDefinition#getIsolationLevel
	 * @see TransactionDefinition#getTimeout
	 * @see TransactionDefinition#isReadOnly
	 */
	TransactionStatus getTransaction(@Nullable TransactionDefinition definition)
			throws TransactionException;

	/**
	 * <p>提交给定事务的状态。如果以编程方式将事务标记为rollback-only，则执行回滚。</p>
	 * Commit the given transaction, with regard to its status. If the transaction
	 * has been marked rollback-only programmatically, perform a rollback.
	 * <p>如果该事务不是一个新的事务，则忽略提交以适当地参与周围的事务。如果前一个事务已经被挂起，
	 * 以便能够创建一个新的事务，则在提交新事务后恢复前一个事务。</p>
	 * <p>If the transaction wasn't a new one, omit the commit for proper
	 * participation in the surrounding transaction. If a previous transaction
	 * has been suspended to be able to create a new one, resume the previous
	 * transaction after committing the new one.
	 * <p>请注意，当提交调用完成时，无论是否正常或抛出异常，事务都必须完全完成并清除。在这种情况下不应该预期回滚调用。</p>
	 * <p>Note that when the commit call completes, no matter if normally or
	 * throwing an exception, the transaction must be fully completed and
	 * cleaned up. No rollback call should be expected in such a case.
	 * <p>如果此方法抛出的异常不是TransactionException，则某些提交前错误导致提交尝试失败。
	 * 例如，O/R映射工具可能试图在提交之前刷新对数据库的更改，结果DataAccessException导致事务失败。
	 * 在这种情况下，原始异常将传播到此提交方法的调用者。</p>
	 * <p>If this method throws an exception other than a TransactionException,
	 * then some before-commit error caused the commit attempt to fail. For
	 * example, an O/R Mapping tool might have tried to flush changes to the
	 * database right before commit, with the resulting DataAccessException
	 * causing the transaction to fail. The original exception will be
	 * propagated to the caller of this commit method in such a case.
	 * @param status object returned by the {@code getTransaction} method
	 * @throws UnexpectedRollbackException in case of an unexpected rollback
	 * that the transaction coordinator initiated
	 * @throws HeuristicCompletionException in case of a transaction failure
	 * caused by a heuristic decision on the side of the transaction coordinator
	 * @throws TransactionSystemException in case of commit or system errors
	 * (typically caused by fundamental resource failures)
	 * @throws IllegalTransactionStateException if the given transaction
	 * is already completed (that is, committed or rolled back)
	 * @see TransactionStatus#setRollbackOnly
	 */
	void commit(TransactionStatus status) throws TransactionException;

	/**
	 * <p>执行给定事务的回滚。</p>
	 * Perform a rollback of the given transaction.
	 * <p>如果该事务不是一个新的事务，只需将其设置为rollback-only为了适当地参与周围的事务。
	 * 如果前一个事务已经被挂起，以便能够创建一个新事务，那么在回滚新事务后恢复前一个事务。</p>
	 * <p>If the transaction wasn't a new one, just set it rollback-only for proper
	 * participation in the surrounding transaction. If a previous transaction
	 * has been suspended to be able to create a new one, resume the previous
	 * transaction after rolling back the new one.
	 * <p>如果commit抛出异常，不要对事务调用rollback。当commit返回时，事务将已经完成并清理，
	 * 即使在commit异常的情况下也是如此。因此，提交失败后的回滚调用将导致IllegalTransactionStateException。</p>
	 * <p><b>Do not call rollback on a transaction if commit threw an exception.</b>
	 * The transaction will already have been completed and cleaned up when commit
	 * returns, even in case of a commit exception. Consequently, a rollback call
	 * after commit failure will lead to an IllegalTransactionStateException.
	 * @param status object returned by the {@code getTransaction} method
	 * @throws TransactionSystemException in case of rollback or system errors
	 * (typically caused by fundamental resource failures)
	 * @throws IllegalTransactionStateException if the given transaction
	 * is already completed (that is, committed or rolled back)
	 */
	void rollback(TransactionStatus status) throws TransactionException;

}
