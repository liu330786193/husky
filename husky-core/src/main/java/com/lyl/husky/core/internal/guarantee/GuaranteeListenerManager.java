package com.lyl.husky.core.internal.guarantee;

import com.lyl.husky.core.api.listener.AbstractDistributeOnceElasticJobListener;
import com.lyl.husky.core.api.listener.ElasticJobListener;
import com.lyl.husky.core.internal.listener.AbstractJobListener;
import com.lyl.husky.core.internal.listener.AbstractListenerManager;
import com.lyl.husky.core.reg.base.CoordinatorRegistryCenter;
import org.apache.curator.framework.recipes.cache.TreeCacheEvent;

import java.util.List;

/**
 * @Author lyl
 * @Description 保证分布式事务全怒开始和结束状态监听管理器
 * @Date 2018/9/27 下午1:56
 */
public class GuaranteeListenerManager extends AbstractListenerManager {

    private final GuaranteeNode guaranteeNode;
    private final List<ElasticJobListener> elasticJobListeners;

    public GuaranteeListenerManager(CoordinatorRegistryCenter regCenter, String jobName, final List<ElasticJobListener> elasticJobListeners) {
        super(regCenter, jobName);
        this.guaranteeNode = new GuaranteeNode(jobName);
        this.elasticJobListeners = elasticJobListeners;
    }

    @Override
    public void start() {
        addDataListener(new StartedNodeRemovedJobListener());
        addDataListener(new CompletedNodeRemovedJobListener());
    }

    class StartedNodeRemovedJobListener extends AbstractJobListener {

        @Override
        protected void dataChanged(final String path, final TreeCacheEvent.Type eventType, final String data) {
            if (TreeCacheEvent.Type.NODE_REMOVED == eventType && guaranteeNode.isStartedRootNode(path)) {
                for (ElasticJobListener each : elasticJobListeners) {
                    if (each instanceof AbstractDistributeOnceElasticJobListener) {
                        ((AbstractDistributeOnceElasticJobListener) each).notifyWaitingTaskStart();
                    }
                }
            }
        }
    }

    class CompletedNodeRemovedJobListener extends AbstractJobListener {

        @Override
        protected void dataChanged(final String path, final TreeCacheEvent.Type eventType, final String data) {
            if (TreeCacheEvent.Type.NODE_REMOVED == eventType && guaranteeNode.isCompletedRootNode(path)) {
                for (ElasticJobListener each : elasticJobListeners) {
                    if (each instanceof AbstractDistributeOnceElasticJobListener) {
                        ((AbstractDistributeOnceElasticJobListener) each).notifyWaitingTaskComplete();
                    }
                }
            }
        }
    }

}
