package com.lyl.husky.core.internal.config;

import com.apple.eawt.AppEvent;
import com.lyl.husky.core.internal.listener.AbstractJobListener;
import com.lyl.husky.core.internal.listener.AbstractListenerManager;
import com.lyl.husky.core.internal.schedule.JobRegistry;
import com.lyl.husky.core.reg.base.CoordinatorRegistryCenter;
import org.apache.curator.framework.recipes.cache.TreeCacheEvent;

/**
 * @Author lyl
 * @Description 重调度监听管理器
 * @Date 2018/9/27 下午1:53
 */
public class RescheduleListenerManager extends AbstractListenerManager {

    private final ConfigurationNode configNode;
    private final String jobName;

    public RescheduleListenerManager(CoordinatorRegistryCenter regCenter, String jobName) {
        super(regCenter, jobName);
        this.jobName = jobName;
        configNode = new ConfigurationNode(jobName);
    }

    @Override
    public void start() {

    }

    class CronSettingAndJobEventChangedJobListener extends AbstractJobListener {

        @Override
        protected void dataChanged(final String path, final TreeCacheEvent.Type eventType, final String data) {
            if (configNode.isConfigPath(path) && TreeCacheEvent.Type.NODE_UPDATED == eventType && !JobRegistry.getInstance().isShutdown(jobName)) {
                JobRegistry.getInstance().getJobScheduleController(jobName).rescheduleJob(LiteJobConfigurationGsonFactory.fromJson(data).getTypeConfig().getCoreConfig().getCron());
            }
        }
    }

}
