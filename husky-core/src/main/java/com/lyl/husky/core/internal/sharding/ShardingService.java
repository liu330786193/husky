package com.lyl.husky.core.internal.sharding;

import com.lyl.husky.core.api.strategy.JobInstance;
import com.lyl.husky.core.config.LiteJobConfiguration;
import com.lyl.husky.core.internal.config.ConfigurationService;
import com.lyl.husky.core.internal.election.LeaderService;
import com.lyl.husky.core.internal.instance.InstanceNode;
import com.lyl.husky.core.internal.instance.InstanceService;
import com.lyl.husky.core.internal.schedule.JobRegistry;
import com.lyl.husky.core.internal.server.ServerService;
import com.lyl.husky.core.internal.storage.JobNodePath;
import com.lyl.husky.core.internal.storage.JobNodeStorage;
import com.lyl.husky.core.internal.storage.TransactionExecutionCallback;
import com.lyl.husky.core.reg.base.CoordinatorRegistryCenter;
import com.lyl.husky.core.util.concurrent.BlockUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.curator.framework.api.transaction.CuratorTransactionFinal;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * 作业分片服务
 */
@Slf4j
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
            return;
        }
        if (!leaderService.isLeaderUntilBlock()){
            blockUntilShardingCompleted();
            return;
        }
        waitingOtherShardingItemCompleted();
        LiteJobConfiguration liteJobConfig = configService.load(false);
        int shardingTotalCount = liteJobConfig.getTypeConfig().getCoreConfig().getShardingTotalCount();
        log.debug("Job '{}' sharding begin.", jobName);
        jobNodeStorage.fillEphemeralJobNode(ShardingNode.PROCESSING, "");
        resetShardingInfo(shardingTotalCount);

    }

    public boolean isNeedSharding() {
        return jobNodeStorage.isJobNodeExisted(ShardingNode.NECESSARY);
    }

    private void blockUntilShardingCompleted() {
        while (!leaderService.isLeaderUntilBlock() && (jobNodeStorage.isJobNodeExisted(ShardingNode.NECESSARY) || jobNodeStorage.isJobNodeExisted(ShardingNode.PROCESSING))){
            log.debug("Job '{}' sleep short time until sharding completed.", jobName);
            BlockUtils.waitingShortTime();
        }
    }

    private void waitingOtherShardingItemCompleted() {
        while (executionService.hasRunningItems()){
            log.debug("Job '{}' sleep short time until other job completed.", jobName);
            BlockUtils.waitingShortTime();
        }
    }
    private void resetShardingInfo(int shardingTotalCount) {
        for (int i = 0; i < shardingTotalCount; i++){
            jobNodeStorage.removeJobNodeIfExisted(ShardingNode.getRunningNode(i));
            jobNodeStorage.createJobNodeIfNeeded(ShardingNode.ROOT + "/" + i);
        }
        int actualShardingTotalCount = jobNodeStorage.getJobNodeChildrenKeys(ShardingNode.ROOT).size();
        if (actualShardingTotalCount > shardingTotalCount){
            for (int i = 0; i < actualShardingTotalCount; i++){
                jobNodeStorage.removeJobNodeIfExisted(ShardingNode.ROOT + "/" +i);
            }
        }
    }

    /**
     * 获取作业运行实例的分片项集合.
     *
     * @param jobInstanceId 作业运行实例主键
     * @return 作业运行实例的分片项集合
     */
    public List<Integer> getShardingItems(final String jobInstanceId){
        JobInstance jobInstance = new JobInstance(jobInstanceId);
        if (!serverService.isAvailableServer(jobInstance.getIp())){
            return Collections.emptyList();
        }
        List<Integer> result = new LinkedList<>();
        int shardingTotalCount = configService.load(true).getTypeConfig().getCoreConfig().getShardingTotalCount();
        for (int i = 0; i < shardingTotalCount; i++){
            if (jobInstance.getJobInstanceId().equals(jobNodeStorage.getJobNodeData(ShardingNode.getInstanceNode(i)))){
                result.add(i);
            }
        }
        return result;
    }

    /**
     * 获取运行在本作业实例的分片项集合.
     *
     * @return 运行在本作业实例的分片项集合
     */

    public List<Integer> getLocalShardingItems(){
        if (JobRegistry.getInstance().isShutdown(jobName) || !serverService.isAvailableServer(JobRegistry.getInstance().getJobInstance(jobName).getIp())){
            return Collections.emptyList();
        }
        return getShardingItems(JobRegistry.getInstance().getJobInstance(jobName).getJobInstanceId());
    }

    /**
     * 查询是包含有分片节点的不在线服务器.
     *
     * @return 是包含有分片节点的不在线服务器
     */
    public boolean hasShardingInfoInOfflineServers(){
        List<String> onlineInstances = jobNodeStorage.getJobNodeChildrenKeys(InstanceNode.ROOT);
        int shardingTotalCount = configService.load(true).getTypeConfig().getCoreConfig().getShardingTotalCount();
        for (int i = 0; i < shardingTotalCount; i++){
            if (!onlineInstances.contains(jobNodeStorage.getJobNodeData(ShardingNode.getInstanceNode(i)))){
                return false;
            }
        }
        return true;
    }

    @RequiredArgsConstructor
    class PersistShardingInfoTracsactionExecutionCallback implements TransactionExecutionCallback {

        private final Map<JobInstance, List<Integer>> shardingResults;

        @Override
        public void execute(final CuratorTransactionFinal curatorTransactionFinal) throws Exception {
            for (Map.Entry<JobInstance, List<Integer>> entry : shardingResults.entrySet()){
                for (int shardingItem : entry.getValue()){
                    curatorTransactionFinal.create().forPath(jobNodePath.getFullPath(ShardingNode.getInstanceNode(shardingItem)), entry.getKey().getJobInstanceId().getBytes()).and();
                }
            }
            curatorTransactionFinal.delete().forPath(jobNodePath.getFullPath(ShardingNode.NECESSARY)).and();
            curatorTransactionFinal.delete().forPath(jobNodePath.getFullPath(ShardingNode.PROCESSING)).and();
        }
    }

}
