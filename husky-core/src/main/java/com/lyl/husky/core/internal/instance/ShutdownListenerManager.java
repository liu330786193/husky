package com.lyl.husky.core.internal.instance;

import com.lyl.husky.core.internal.listener.AbstractJobListener;
import com.lyl.husky.core.internal.listener.AbstractListenerManager;
import com.lyl.husky.core.internal.schedule.JobRegistry;
import com.lyl.husky.core.internal.schedule.SchedulerFacade;
import com.lyl.husky.core.reg.base.CoordinatorRegistryCenter;
import org.apache.curator.framework.recipes.cache.TreeCacheEvent;

/**
 * @Author lyl
 * @Description 运行实例关闭监听管理器
 * @Date 2018/9/27 下午1:40
 */
public final class ShutdownListenerManager extends AbstractListenerManager {

    private final String jobName;
    private final InstanceNode instanceNode;
    private final InstanceService instanceService;
    private final SchedulerFacade schedulerFacade;

    public ShutdownListenerManager(CoordinatorRegistryCenter regCenter, String jobName) {
        super(regCenter, jobName);
        this.jobName = jobName;
        instanceNode = new InstanceNode(jobName);
        instanceService = new InstanceService(regCenter, jobName);
        schedulerFacade = new SchedulerFacade(regCenter, jobName);
    }

    @Override
    public void start() {

    }

    class InstanceShutdownStatusJobListener extends AbstractJobListener {

        @Override
        protected void dataChanged(final String path, final TreeCacheEvent.Type eventType, final String data) {
            if (!JobRegistry.getInstance().isShutdown(jobName) && !JobRegistry.getInstance().getJobScheduleController(jobName).isPaused()
                    && isRemoveInstance(path, eventType) && !isReconnectedRegistryCenter()) {
                schedulerFacade.shutdownInstance();
            }
        }

        private boolean isRemoveInstance(final String path, final TreeCacheEvent.Type eventType) {
            return instanceNode.isLocalInstancePath(path) && TreeCacheEvent.Type.NODE_REMOVED == eventType;
        }

        private boolean isReconnectedRegistryCenter() {
            return instanceService.isLocalJobInstanceExisted();
        }
    }

}
