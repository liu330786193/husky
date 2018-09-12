package com.lyl.husky.core.api;

import com.lyl.husky.core.executor.ShardingContexts;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class ShardingContext {
    /**
     * 作业名称
     */
    private final String jobName;
    /**
     * 作业任务ID
     */
    private final String taskId;
    /**
     * 分片总数
     */
    private final int shardingTotalCount;
    /**
     * 作业自定义参数
     * 可以配置多个相同的作业 但是用不同的参数作为不同的调度实例
     */
    private final String jobParameter;
    /**
     * 分配与本作业实例的分片项
     */
    private final int shardingItem;
    /**
     * 分配于本作业实例的分片参数
     */
    private final String shardingParameter;

    public ShardingContext(final ShardingContexts shardingContexts, final int shardingItem) {
        this.jobName = shardingContexts.getJobName();
        this.taskId = shardingContexts.getTaskId();
        this.shardingTotalCount = shardingContexts.getShardingTotalCount();
        this.jobParameter = shardingContexts.getJobParameter();
        this.shardingItem = shardingItem;
        this.shardingParameter = shardingContexts.getShardingItemParameters().get(shardingItem);
    }
}
