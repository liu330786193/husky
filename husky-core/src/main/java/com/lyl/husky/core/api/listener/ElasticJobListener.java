package com.lyl.husky.core.api.listener;

import com.lyl.husky.core.executor.ShardingContexts;

/**
 * @Author lyl
 * @Description 弹性化分布式作业监听器接口.
 * @Date 2018/9/27 下午2:00
 */
public interface ElasticJobListener {

    /**
     * 作业执行前的执行的方法.
     *
     * @param shardingContexts 分片上下文
     */
    void beforeJobExecuted(final ShardingContexts shardingContexts);

    /**
     * 作业执行后的执行的方法.
     *
     * @param shardingContexts 分片上下文
     */
    void afterJobExecuted(final ShardingContexts shardingContexts);

}

