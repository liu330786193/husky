package com.lyl.husky.core.internal.schedule;

import com.lyl.husky.core.internal.config.ConfigurationService;
import com.lyl.husky.core.internal.election.LeaderService;
import com.lyl.husky.core.internal.instance.InstanceService;
import com.lyl.husky.core.internal.monitor.MonitorService;
import com.lyl.husky.core.internal.sharding.ShardingService;

/**
 * 为调度器提供内部服务的门面类
 */
public final class SchedulerFacade {

    private final String jobName;
    private final ConfigurationService configService;
    private final LeaderService leaderService;
    private final InstanceService instanceService;
    private final ShardingService shardingService;
    private final ExecutionService executionService;
    private final MonitorService monitorService;

}
