package com.lyl.husky.core.internal.listener;

import com.lyl.husky.core.internal.instance.InstanceService;
import com.lyl.husky.core.internal.sharding.ExecutionService;
import com.lyl.husky.core.internal.schedule.JobRegistry;
import com.lyl.husky.core.internal.schedule.JobScheduleController;
import com.lyl.husky.core.internal.server.ServerService;
import com.lyl.husky.core.internal.sharding.ShardingService;
import com.lyl.husky.core.reg.base.CoordinatorRegistryCenter;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.state.ConnectionState;
import org.apache.curator.framework.state.ConnectionStateListener;

/**
 * @Author lyl
 * @Description 注册中心连接状态监听器
 * @Date 2018/9/27 下午2:44
 */
public final class RegistryCenterConnectionStateListener implements ConnectionStateListener {

    private final String jobName;
    private final ServerService serverService;
    private final InstanceService instanceService;
    private final ShardingService shardingService;
    private final ExecutionService executionService;

    public RegistryCenterConnectionStateListener(final CoordinatorRegistryCenter regCenter, final String jobName) {
        this.jobName = jobName;
        serverService = new ServerService(regCenter, jobName);
        instanceService = new InstanceService(regCenter, jobName);
        shardingService = new ShardingService(regCenter, jobName);
        executionService = new ExecutionService(regCenter, jobName);
    }

    @Override
    public void stateChanged(CuratorFramework client, ConnectionState newState) {
        if (JobRegistry.getInstance().isShutdown(jobName)) {
            return;
        }
        JobScheduleController jobScheduleController = JobRegistry.getInstance().getJobScheduleController(jobName);
        if (ConnectionState.SUSPENDED == newState || ConnectionState.LOST == newState) {
            jobScheduleController.pauseJob();
        } else if (ConnectionState.RECONNECTED == newState) {
            serverService.persistOnline(serverService.isEnableServer(JobRegistry.getInstance().getJobInstance(jobName).getIp()));
            instanceService.persistOnline();
            executionService.clearRunningInfo(shardingService.getLocalShardingItems());
            jobScheduleController.resumeJob();
        }
    }
}
