package com.lyl.husky.core.internal.schedule;

import com.lyl.husky.core.api.ElasticJob;
import com.lyl.husky.core.executor.JobExecutorFactory;
import com.lyl.husky.core.executor.JobFacade;
import lombok.Setter;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

/**
 * Lite调度作业
 */

public final class LiteJob implements Job {

    @Setter
    private ElasticJob elasticJob;
    @Setter
    private JobFacade jobFacade;

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        JobExecutorFactory.getJobExecutor(elasticJob, jobFacade).execute();
    }
}
