package com.lyl.husky.core.api.simple;

import com.lyl.husky.core.api.ElasticJob;
import com.lyl.husky.core.api.ShardingContext;

/**
 * 简单分布式作业接口
 */
public interface SimpleJob extends ElasticJob {

    /**
     * 执行作业
     */
    void execute(ShardingContext shardingContext);

}
