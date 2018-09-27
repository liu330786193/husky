package com.lyl.husky.core.internal.listener;

import com.lyl.husky.core.api.listener.ElasticJobListener;
import com.lyl.husky.core.internal.config.RescheduleListenerManager;
import com.lyl.husky.core.internal.election.ElectionListenerManager;
import com.lyl.husky.core.internal.failover.FailoverListenerManager;
import com.lyl.husky.core.internal.guarantee.GuaranteeListenerManager;
import com.lyl.husky.core.internal.instance.ShutdownListenerManager;
import com.lyl.husky.core.internal.instance.TriggerListenerManager;
import com.lyl.husky.core.internal.sharding.MonitorExecutionListenerManager;
import com.lyl.husky.core.internal.sharding.ShardingListenerManager;
import com.lyl.husky.core.internal.storage.JobNodeStorage;
import com.lyl.husky.core.reg.base.CoordinatorRegistryCenter;

import java.util.List;

/**
 * @Author lyl
 * @Description 作业注册中心的监听器管理者
 * @Date 2018/9/27 上午11:12
 */
public final class ListenerManager {

    private final JobNodeStorage jobNodeStorage;
    private final ElectionListenerManager electionListenerManager;
    private final ShardingListenerManager shardingListenerManager;
    private final FailoverListenerManager failoverListenerManager;
    private final MonitorExecutionListenerManager monitorExecutionListenerManager;
    private final ShutdownListenerManager shutdownListenerManager;
    private final TriggerListenerManager triggerListenerManager;
    private final RescheduleListenerManager rescheduleListenerManager;
    private final GuaranteeListenerManager guaranteeListenerManager;
    private final RegistryCenterConnectionStateListener regCenterConnectionStateListener;


    public ListenerManager(final CoordinatorRegistryCenter regCenter, final String jobName, final List<ElasticJobListener> elasticJobListeners) {
        jobNodeStorage = new JobNodeStorage(regCenter, jobName);
        electionListenerManager = new ElectionListenerManager(regCenter, jobName);
        shardingListenerManager = new ShardingListenerManager(regCenter, jobName);
        failoverListenerManager = new FailoverListenerManager(regCenter, jobName);
        monitorExecutionListenerManager = new MonitorExecutionListenerManager(regCenter, jobName);
        shutdownListenerManager = new ShutdownListenerManager(regCenter, jobName);
        triggerListenerManager = new TriggerListenerManager(regCenter, jobName);
        rescheduleListenerManager = new RescheduleListenerManager(regCenter, jobName);
        guaranteeListenerManager = new GuaranteeListenerManager(regCenter, jobName, elasticJobListeners);
        regCenterConnectionStateListener = new RegistryCenterConnectionStateListener(regCenter, jobName);
    }


    /**
     * 开启所有监听器.
     */
    public void startAllListeners() {
        electionListenerManager.start();
        shardingListenerManager.start();
        failoverListenerManager.start();
        monitorExecutionListenerManager.start();
        shutdownListenerManager.start();
        triggerListenerManager.start();
        rescheduleListenerManager.start();
        guaranteeListenerManager.start();
        jobNodeStorage.addConnectionStateListener(regCenterConnectionStateListener);
    }

}
