package com.lyl.husky.lifecycle.api;

/**
 * 操作分片的API
 */
public interface ShardingOperateAPI {

    /**
     * 禁用作业分片
     */
    void disable(String jobName, String item);

    /**
     * 启用作业片
     */
    void enable(String jobName, String item);

}
