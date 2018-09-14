package com.lyl.husky.core.internal.storage;

/**
 * 主节点执行操作的回调接口
 */
public interface LeaderExecutionCallback {

    /**
     * 节点选中之后执行的回调方法
     */
    void execute();

}
