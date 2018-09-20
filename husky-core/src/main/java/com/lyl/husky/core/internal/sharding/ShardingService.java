package com.lyl.husky.core.internal.sharding;

import com.lyl.husky.core.api.strategy.JobInstance;
import com.lyl.husky.core.internal.config.ConfigurationService;
import com.lyl.husky.core.internal.election.LeaderService;
import com.lyl.husky.core.internal.instance.InstanceService;
import com.lyl.husky.core.internal.schedule.ExecutionService;
import com.lyl.husky.core.internal.schedule.ShardingNode;
import com.lyl.husky.core.internal.server.ServerService;
import com.lyl.husky.core.internal.storage.JobNodePath;
import com.lyl.husky.core.internal.storage.JobNodeStorage;
import com.lyl.husky.core.reg.base.CoordinatorRegistryCenter;

import java.util.List;
import java.util.concurrent.ExecutorService;

/**
 * 作业分片服务
 */
public final class ShardingService {

    private final String jobName;
    private final JobNodeStorage jobNodeStorage;
    private final LeaderService leaderService;
    private final ConfigurationService configService;
    private final InstanceService instanceService;
    private final ServerService serverService;
    private final ExecutionService executionService;
    private final JobNodePath jobNodePath;

    public ShardingService(final CoordinatorRegistryCenter regCenter, final String jobName){
        this.jobName = jobName;
        jobNodeStorage = new JobNodeStorage(regCenter, jobName);
        leaderService = new LeaderService(regCenter, jobName);
        configService = new ConfigurationService(regCenter, jobName);
        instanceService = new InstanceService(regCenter, jobName);
        serverService = new ServerService(regCenter, jobName);
        executionService = new ExecutionService(regCenter, jobName);
        jobNodePath = new JobNodePath(jobName);
    }

    /**
     * 设置需要重新分片的标记.
     */
    public void setReshardingFlag(){
        jobNodeStorage.createJobNodeIfNeeded(ShardingNode.NECESSARY);
    }

    /**
     * 如果需要分片且当前节点为主节点, 则作业分片.
     *
     * <p>
     * 如果当前无可用节点则不分片.
     * </p>
     */
    public void shardingIfNecessary(){
        List<JobInstance> availableJobInstances = instanceService.getAvailableJobInstances();
        if (!isNeedSharding() || availableJobInstances.isEmpty()){

        }
    }

}
