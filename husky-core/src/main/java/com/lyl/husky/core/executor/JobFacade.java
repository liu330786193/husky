package com.lyl.husky.core.executor;

import com.lyl.husky.core.config.JobRootConfiguration;
import com.lyl.husky.core.event.type.JobExecutionEvent;
import com.lyl.husky.core.event.type.JobStatusTraceEvent;
import com.lyl.husky.core.exception.JobExecutionEnvironmentException;

import java.util.Collection;

/**
 * 作业内部服务门面服务
 */
public interface JobFacade {

    /**
     * 读取作业配置
     * @param fromCache 是否从缓存中读取
     * @return 作业配置
     */
    JobRootConfiguration loadJobRootConfiguration(boolean fromCache);

    /**
     * 检查作业执行环境
     */
    void checkJobExecutionEnvironment() throws JobExecutionEnvironmentException;

    /**
     * 如果需要失效转移 则执行作业失效转移
     */
    void failoverIfNecessary();

    /**
     * 注册作业启动信息
     * @param shardingContexts 分片上下文
     */
    void registerJobBegin(ShardingContexts shardingContexts);

    /**
     * 注册作业完成信息
     * @param shardingContexts
     */
    void registerJobCompleted(ShardingContexts shardingContexts);

    /**
     * 获取当前作业服务器分片上下文
     */
    ShardingContexts getShardingContexts();

    /**
     * 设置任务被错误执行的标记
     * @param shardingItems
     * @return
     */
    boolean misfireIfRunning(Collection<Integer> shardingItems);

    /**
     * 清除任务被错过执行的标记
     * @param shardingItems 需要清除错误执行的任务分片项
     */
    void clearMisfire(Collection<Integer> shardingItems);

    /**
     * 判断作业是否需要执行错误的任务
     * @param shardingItems 任务分片集合项
     * @return 作业是否需要执行错过的任务
     */
    boolean isExecuteMisfired(Collection<Integer> shardingItems);

    /**
     * 判断作业是否符合继续运行的条件
     * @return 是否需要重分片
     */
    boolean isEligibleForJobRunning();

    /**
     * 判断是否需要重分片
     * @return
     */
    boolean isNeedSharding();

    /**
     * 作业执行前的执行的方法
     * @param shardingContexts
     */
    void beforeJobExecuted(ShardingContexts shardingContexts);

    /**
     * 作业执行后的执行的方法
     * @param shardingContexts
     */
    void afterJobExecuted(ShardingContexts shardingContexts);

    /**
     * 发布执行事件
     * @param jobExecutionEvent
     */
    void postJobExecutionEvent(JobExecutionEvent jobExecutionEvent);

    /**
     * 发布作业状态追踪事件
     * @param taskId
     * @param state
     * @param message
     */
    void postJobStatusTraceEvent(String taskId, JobStatusTraceEvent.State state, String message);

}
