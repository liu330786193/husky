package com.lyl.husky.core.internal.failover;

import com.lyl.husky.core.config.LiteJobConfiguration;
import com.lyl.husky.core.internal.config.ConfigurationNode;
import com.lyl.husky.core.internal.config.ConfigurationService;
import com.lyl.husky.core.internal.config.LiteJobConfigurationGsonFactory;
import com.lyl.husky.core.internal.instance.InstanceNode;
import com.lyl.husky.core.internal.listener.AbstractJobListener;
import com.lyl.husky.core.internal.listener.AbstractListenerManager;
import com.lyl.husky.core.internal.schedule.JobRegistry;
import com.lyl.husky.core.internal.sharding.ShardingService;
import com.lyl.husky.core.reg.base.CoordinatorRegistryCenter;
import org.apache.curator.framework.recipes.cache.TreeCacheEvent;

import java.util.List;

/**
 * @Author lyl
 * @Description 失效转移监听管理器
 * @Date 2018/9/27 上午11:55
 */
public final class FailoverListenerManager extends AbstractListenerManager {

    private final String jobName;

    private final ConfigurationService configService;

    private final ShardingService shardingService;

    private final FailoverService failoverService;

    private final ConfigurationNode configNode;

    private final InstanceNode instanceNode;

    public FailoverListenerManager(CoordinatorRegistryCenter regCenter, String jobName) {
        super(regCenter, jobName);
        this.jobName = jobName;
        configService = new ConfigurationService(regCenter, jobName);
        shardingService = new ShardingService(regCenter, jobName);
        failoverService = new FailoverService(regCenter, jobName);
        configNode = new ConfigurationNode(jobName);
        instanceNode = new InstanceNode(jobName);
    }

    @Override
    public void start() {
        addDataListener(new JobCrashedJobListener());
        addDataListener(new FailoverSettingsChangedJobListener());
    }

    class JobCrashedJobListener extends AbstractJobListener {

        @Override
        protected void dataChanged(final String path, final TreeCacheEvent.Type eventType, final String data) {
            if (isFailoverEnabled() && TreeCacheEvent.Type.NODE_REMOVED == eventType && instanceNode.isInstancePath(path)) {
                String jobInstanceId = path.substring(instanceNode.getInstanceFullPath().length() + 1);
                if (jobInstanceId.equals(JobRegistry.getInstance().getJobInstance(jobName).getJobInstanceId())) {
                    return;
                }
                List<Integer> failoverItems = failoverService.getFailoverItems(jobInstanceId);
                if (!failoverItems.isEmpty()) {
                    for (int each : failoverItems) {
                        failoverService.setCrashedFailoverFlag(each);
                        failoverService.failoverIfNecessary();
                    }
                } else {
                    for (int each : shardingService.getShardingItems(jobInstanceId)) {
                        failoverService.setCrashedFailoverFlag(each);
                        failoverService.failoverIfNecessary();
                    }
                }
            }
        }
    }

    class FailoverSettingsChangedJobListener extends AbstractJobListener {

        @Override
        protected void dataChanged(final String path, final TreeCacheEvent.Type eventType, final String data) {
            if (configNode.isConfigPath(path) && TreeCacheEvent.Type.NODE_UPDATED == eventType && !LiteJobConfigurationGsonFactory.fromJson(data).isFailover()) {
                failoverService.removeFailoverInfo();
            }
        }
    }

    private boolean isFailoverEnabled() {
        LiteJobConfiguration jobConfig = configService.load(true);
        return null != jobConfig && jobConfig.isFailover();
    }
}
