package com.lyl.husky.core.internal.sharding;

import com.lyl.husky.core.internal.config.ConfigurationNode;
import com.lyl.husky.core.internal.config.LiteJobConfigurationGsonFactory;
import com.lyl.husky.core.internal.instance.InstanceNode;
import com.lyl.husky.core.internal.listener.AbstractJobListener;
import com.lyl.husky.core.internal.listener.AbstractListenerManager;
import com.lyl.husky.core.internal.schedule.JobRegistry;
import com.lyl.husky.core.internal.server.ServerNode;
import com.lyl.husky.core.reg.base.CoordinatorRegistryCenter;
import org.apache.curator.framework.recipes.cache.TreeCacheEvent;

/**
 * @Author lyl
 * @Description
 * @Date 2018/9/27 上午11:42
 */
public final class ShardingListenerManager extends AbstractListenerManager {

    private final String jobName;
    private final ConfigurationNode configNode;
    private final InstanceNode instanceNode;
    private final ServerNode serverNode;
    private final ShardingService shardingService;


    public ShardingListenerManager(final CoordinatorRegistryCenter regCenter, final String jobName) {
        super(regCenter, jobName);
        this.jobName = jobName;
        configNode = new ConfigurationNode(jobName);
        instanceNode = new InstanceNode(jobName);
        serverNode = new ServerNode(jobName);
        shardingService = new ShardingService(regCenter, jobName);
    }
    @Override
    public void start() {
        addDataListener(new ShardingTotalCountChangedJobListener());
        addDataListener(new ListenServersChangedJobListener());
    }

    class ShardingTotalCountChangedJobListener extends AbstractJobListener {

        @Override
        protected void dataChanged(final String path, final TreeCacheEvent.Type eventType, final String data) {
            if (configNode.isConfigPath(path) && 0 != JobRegistry.getInstance().getCurrentShardingTotalCount(jobName)) {
                int newShardingTotalCount = LiteJobConfigurationGsonFactory.fromJson(data).getTypeConfig().getCoreConfig().getShardingTotalCount();
                if (newShardingTotalCount != JobRegistry.getInstance().getCurrentShardingTotalCount(jobName)) {
                    shardingService.setReshardingFlag();
                    JobRegistry.getInstance().setCurrentShardingTotalCount(jobName, newShardingTotalCount);
                }
            }
        }
    }

    class ListenServersChangedJobListener extends AbstractJobListener {

        @Override
        protected void dataChanged(final String path, final TreeCacheEvent.Type eventType, final String data) {
            if (!JobRegistry.getInstance().isShutdown(jobName) && (isInstanceChange(eventType, path) || isServerChange(path))) {
                shardingService.setReshardingFlag();
            }
        }

        private boolean isInstanceChange(final TreeCacheEvent.Type eventType, final String path) {
            return instanceNode.isInstancePath(path) && TreeCacheEvent.Type.NODE_UPDATED != eventType;
        }

        private boolean isServerChange(final String path) {
            return serverNode.isServerPath(path);
        }
    }

}
