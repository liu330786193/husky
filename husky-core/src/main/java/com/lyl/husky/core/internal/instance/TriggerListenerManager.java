package com.lyl.husky.core.internal.instance;

import com.lyl.husky.core.internal.listener.AbstractJobListener;
import com.lyl.husky.core.internal.listener.AbstractListenerManager;
import com.lyl.husky.core.internal.schedule.JobRegistry;
import com.lyl.husky.core.reg.base.CoordinatorRegistryCenter;
import org.apache.curator.framework.recipes.cache.TreeCacheEvent;

/**
 * @Author lyl
 * @Description 作业触发监听管理器
 * @Date 2018/9/27 下午1:43
 */
public class TriggerListenerManager extends AbstractListenerManager {

    private final String jobName;

    private final InstanceNode instanceNode;

    private final InstanceService instanceService;

    public TriggerListenerManager(final CoordinatorRegistryCenter regCenter, final String jobName) {
        super(regCenter, jobName);
        this.jobName = jobName;
        instanceNode = new InstanceNode(jobName);
        instanceService = new InstanceService(regCenter, jobName);
    }

    @Override
    public void start() {
        addDataListener(new JobTriggerStatusJobListener());
    }

    class JobTriggerStatusJobListener extends AbstractJobListener {

        @Override
        protected void dataChanged(final String path, final TreeCacheEvent.Type eventType, final String data) {
            if (!InstanceOperation.TRIGGER.name().equals(data) || !instanceNode.isLocalInstancePath(path) || TreeCacheEvent.Type.NODE_UPDATED != eventType) {
                return;
            }
            instanceService.clearTriggerFlag();
            if (!JobRegistry.getInstance().isShutdown(jobName) && !JobRegistry.getInstance().isJobRunning(jobName)) {
                // TODO 目前是作业运行时不能触发, 未来改为堆积式触发
                JobRegistry.getInstance().getJobScheduleController(jobName).triggerJob();
            }
        }
    }

}
