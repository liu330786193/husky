package com.lyl.husky.core.internal.reconcile;

import com.google.common.util.concurrent.AbstractScheduledService;
import com.lyl.husky.core.config.LiteJobConfiguration;
import com.lyl.husky.core.internal.config.ConfigurationService;
import com.lyl.husky.core.internal.election.LeaderService;
import com.lyl.husky.core.internal.sharding.ShardingService;
import com.lyl.husky.core.reg.base.CoordinatorRegistryCenter;
import com.mysql.jdbc.TimeUtil;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;

/**
 * @Author lyl
 * @Description 调解分布式作业不一致状态服务
 * @Date 2018/9/27 上午10:41
 */
@Slf4j
public final class ReconcileService extends AbstractScheduledService {

    private long lastReconcileTime;
    private final ConfigurationService configService;
    private final ShardingService shardingService;
    private final LeaderService leaderService;

    public ReconcileService(final CoordinatorRegistryCenter regCenter, final String jobName){
        lastReconcileTime = System.currentTimeMillis();
        configService = new ConfigurationService(regCenter, jobName);
        shardingService = new ShardingService(regCenter, jobName);
        leaderService = new LeaderService(regCenter, jobName);
    }

    @Override
    protected void runOneIteration() throws Exception {
        LiteJobConfiguration config = configService.load(true);
        int reconcileIntercalMinutes = null == config ? -1 : config.getReconcileIntervalMinutes();
        if (reconcileIntercalMinutes > 0 && (System.currentTimeMillis() - lastReconcileTime >= reconcileIntercalMinutes * 60 * 1000)){
            lastReconcileTime = System.currentTimeMillis();
            if (leaderService.isLeaderUntilBlock() && !shardingService.isNeedSharding() && shardingService.hasShardingInfoInOfflineServers()){
                log.warn("Elastic Job: job status node has inconsistent value,start reconciling...");
                shardingService.setReshardingFlag();
            }
        }
    }

    @Override
    protected Scheduler scheduler() {
        return Scheduler.newFixedDelaySchedule(0, 1 , TimeUnit.MINUTES);
    }

}
