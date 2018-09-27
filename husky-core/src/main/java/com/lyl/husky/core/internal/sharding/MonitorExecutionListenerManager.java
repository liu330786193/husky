package com.lyl.husky.core.internal.sharding;

import com.lyl.husky.core.internal.config.ConfigurationNode;
import com.lyl.husky.core.internal.config.LiteJobConfigurationGsonFactory;
import com.lyl.husky.core.internal.listener.AbstractJobListener;
import com.lyl.husky.core.internal.listener.AbstractListenerManager;
import com.lyl.husky.core.reg.base.CoordinatorRegistryCenter;
import org.apache.curator.framework.recipes.cache.TreeCacheEvent;

/**
 * @Author lyl
 * @Description 幂等性监听管理器
 * @Date 2018/9/27 下午12:23
 */
public final class MonitorExecutionListenerManager extends AbstractListenerManager {

    private final ExecutionService executionService;
    private final ConfigurationNode configNode;

    public MonitorExecutionListenerManager(CoordinatorRegistryCenter regCenter, String jobName) {
        super(regCenter, jobName);
        executionService = new ExecutionService(regCenter, jobName);
        configNode = new ConfigurationNode(jobName);
    }

    @Override
    public void start() {
        addDataListener(new MonitorExecutionSettingsChangedJobListener());
    }

    class MonitorExecutionSettingsChangedJobListener extends AbstractJobListener {

        @Override
        protected void dataChanged(final String path, final TreeCacheEvent.Type eventType, final String data) {
            if (configNode.isConfigPath(path) && TreeCacheEvent.Type.NODE_UPDATED == eventType && !LiteJobConfigurationGsonFactory.fromJson(data).isMonitorExecution()) {
                executionService.clearAllRunningInfo();
            }
        }
    }

}
