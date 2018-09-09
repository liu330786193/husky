package com.lyl.husky.lifecycle.api;

import com.lyl.husky.lifecycle.domain.ShardingInfo;

import java.util.Collection;

/**
 * 作业分片状态展示的API
 */
public interface ShardingStatisticsAPI {

    /**
     * 获取作业分片信息集合
     */
    Collection<ShardingInfo> getShardingInfo(String jobName);

}
