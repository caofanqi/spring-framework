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

/**
 * <p>事务当前状态的公共表示。作为TransactionStatus和ReactiveTransaction的基本接口。</p>
 * Common representation of the current state of a transaction.
 * Serves as base interface for {@link TransactionStatus} as well as
 * {@link ReactiveTransaction}.
 *
 * @author Juergen Hoeller
 * @since 5.2
 */
public interface TransactionExecution {

	/**
	 * <p>返回当前事务是否为新事务;否则，将参与现有事务，或者可能一开始就不在实际事务中运行。</p>
	 * Return whether the present transaction is new; otherwise participating
	 * in an existing transaction, or potentially not running in an actual
	 * transaction in the first place.
	 */
	boolean isNewTransaction();

	/**
	 * <p>设置事务rollback-only。这指示事务管理器，事务唯一可能的结果可能是回滚，作为抛出异常的替代，后者会触发回滚。</p>
	 * Set the transaction rollback-only. This instructs the transaction manager
	 * that the only possible outcome of the transaction may be a rollback, as
	 * alternative to throwing an exception which would in turn trigger a rollback.
	 */
	void setRollbackOnly();

	/**
	 * <p>返回事务是否被标记为rollback-only(应用程序或事务基础设施)。</p>
	 * Return whether the transaction has been marked as rollback-only
	 * (either by the application or by the transaction infrastructure).
	 */
	boolean isRollbackOnly();

	/**
	 * <p>返回该事务是否已完成，即是否已经提交或回滚。</p>
	 * Return whether this transaction is completed, that is,
	 * whether it has already been committed or rolled back.
	 */
	boolean isCompleted();

}
