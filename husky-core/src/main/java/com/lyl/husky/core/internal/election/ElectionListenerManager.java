package com.lyl.husky.core.internal.election;

import com.lyl.husky.core.internal.listener.AbstractJobListener;
import com.lyl.husky.core.internal.listener.AbstractListenerManager;
import com.lyl.husky.core.internal.schedule.JobRegistry;
import com.lyl.husky.core.internal.server.ServerNode;
import com.lyl.husky.core.internal.server.ServerService;
import com.lyl.husky.core.reg.base.CoordinatorRegistryCenter;
import org.apache.curator.framework.recipes.cache.TreeCacheEvent;

/**
 * @Author lyl
 * @Description 主节点选举监听管理器
 * @Date 2018/9/27 上午11:14
 */
public final class ElectionListenerManager extends AbstractListenerManager {

    private final String jobName;
    private final LeaderNode leaderNode;
    private final ServerNode serverNode;
    private final LeaderService leaderService;
    private final ServerService serverService;

    public ElectionListenerManager(final CoordinatorRegistryCenter regCenter, final String jobName){
        super(regCenter, jobName);
        this.jobName = jobName;
        leaderNode = new LeaderNode(jobName);
        serverNode = new ServerNode(jobName);
        leaderService = new LeaderService(regCenter, jobName);
        serverService = new ServerService(regCenter, jobName);
    }

    @Override
    public void start() {
        addDataListener(new LeaderElectionJobListener());
        addDataListener(new LeaderAbdicationJobListener());
    }

    class LeaderElectionJobListener extends AbstractJobListener {

        @Override
        protected void dataChanged(String path, TreeCacheEvent.Type eventType, String data) {
            if (!JobRegistry.getInstance().isShutdown(jobName) && (isActiveElection(path, data) || isPassiveElection(path, eventType))) {
                leaderService.electLeader();
            }
        }

        private boolean isActiveElection(final String path, final String data) {
            return !leaderService.hasLeader() && isLocalServerEnabled(path, data);
        }

        private boolean isPassiveElection(final String path, final TreeCacheEvent.Type eventType) {
            return isLeaderCrashed(path, eventType) && serverService.isAvailableServer(JobRegistry.getInstance().getJobInstance(jobName).getIp());
        }

        private boolean isLeaderCrashed(final String path, final TreeCacheEvent.Type eventType) {
            return leaderNode.isLeaderInstancePath(path) && TreeCacheEvent.Type.NODE_REMOVED == eventType;
        }

        private boolean isLocalServerEnabled(final String path, final String data) {
            return serverNode.isLocalServerPath(path) && !ServerService.ServerStatus.DISABLED.name().equals(data);
        }
    }

    class LeaderAbdicationJobListener extends AbstractJobListener {

        @Override
        protected void dataChanged(String path, TreeCacheEvent.Type eventType, String data) {
            if (leaderService.isLeader() && isLocalServerDisabled(path, data)){
                leaderService.removeLeader();
            }
        }

        private boolean isLocalServerDisabled(final String path, final String data) {
            return serverNode.isLocalServerPath(path) && ServerService.ServerStatus.DISABLED.name().equals(data);
        }
    }
}
