package com.lyl.husky.core.api.dataflow;

import com.lyl.husky.core.api.ElasticJob;
import com.lyl.husky.core.api.ShardingContext;

import java.util.List;

/**
 * 数据流分布式作业接口
 */
public interface DataflowJob<T> extends ElasticJob {

    /**
     * 获取待处理数据
     */
    List<T> fetchData(ShardingContext shardingContext);

    /**
     * 处理数据
     */
    void processData(ShardingContext shardingContext, List<T> data);

}
