package com.lyl.husky.console.service;

import com.lyl.husky.lifecycle.api.*;

public interface JobAPIService {

    JobSettingsAPI getJobSettingsAPI();
    JobOperateAPI getJobOperatorAPI();
    ShardingOperateAPI getShardingOperateAPI();
    JobStatisticsAPI getJobStatisticsAPI();
    ServerStatisticsAPI getServerStatisticsAPI();
    ShardingStatisticsAPI getShardingStatisticsAPI();

}
