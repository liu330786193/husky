package com.lyl.husky.spring.api;

import com.google.common.base.Optional;
import com.lyl.husky.core.api.ElasticJob;
import com.lyl.husky.core.api.JobScheduler;
import com.lyl.husky.core.api.listener.ElasticJobListener;
import com.lyl.husky.core.config.LiteJobConfiguration;
import com.lyl.husky.core.event.JobEventConfiguration;
import com.lyl.husky.core.reg.base.CoordinatorRegistryCenter;
import com.lyl.husky.spring.job.AopTargetUtils;

/**
 * @Author lyl
 * @Description 基于Spring的作业启动器.
 * @Date 2018/9/27 下午3:42
 */
public final class SpringJobScheduler extends JobScheduler {

    private final ElasticJob elasticJob;

    public SpringJobScheduler(final ElasticJob elasticJob, final CoordinatorRegistryCenter regCenter, final LiteJobConfiguration jobConfig, final ElasticJobListener... elasticJobListeners) {
        super(regCenter, jobConfig, getTargetElasticJobListeners(elasticJobListeners));
        this.elasticJob = elasticJob;
    }

    public SpringJobScheduler(final ElasticJob elasticJob, final CoordinatorRegistryCenter regCenter, final LiteJobConfiguration jobConfig,
                              final JobEventConfiguration jobEventConfig, final ElasticJobListener... elasticJobListeners) {
        super(regCenter, jobConfig, jobEventConfig, getTargetElasticJobListeners(elasticJobListeners));
        this.elasticJob = elasticJob;
    }

    private static ElasticJobListener[] getTargetElasticJobListeners(final ElasticJobListener[] elasticJobListeners) {
        final ElasticJobListener[] result = new ElasticJobListener[elasticJobListeners.length];
        for (int i = 0; i < elasticJobListeners.length; i++) {
            result[i] = (ElasticJobListener) AopTargetUtils.getTarget(elasticJobListeners[i]);
        }
        return result;
    }

    @Override
    protected Optional<ElasticJob> createElasticJobInstance() {
        return Optional.fromNullable(elasticJob);
    }
}
