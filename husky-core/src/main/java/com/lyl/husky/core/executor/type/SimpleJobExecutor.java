package com.lyl.husky.core.executor.type;

import com.lyl.husky.core.api.ShardingContext;
import com.lyl.husky.core.api.simple.SimpleJob;
import com.lyl.husky.core.executor.AbstractElasticJobExecutor;
import com.lyl.husky.core.executor.JobFacade;

/**
 * 简单作业执行器
 */
public final class SimpleJobExecutor extends AbstractElasticJobExecutor {

    private final SimpleJob simpleJob;

    public SimpleJobExecutor(final SimpleJob simpleJob, final JobFacade jobFacade){
        super(jobFacade);
        this.simpleJob = simpleJob;
    }

    @Override
    protected void process(ShardingContext shardingContext) {
        simpleJob.execute(shardingContext);
    }
}
