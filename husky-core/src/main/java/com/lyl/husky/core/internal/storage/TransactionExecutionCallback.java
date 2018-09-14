package com.lyl.husky.core.internal.storage;

import org.apache.curator.framework.api.transaction.CuratorTransactionFinal;

/**
 * 事务执行操作的回调接口
 */
public interface TransactionExecutionCallback {

    /**
     * 事务执行的回调方法
     */
    void execute(CuratorTransactionFinal curatorTransactionFinal) throws Exception;
}
