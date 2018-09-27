package com.lyl.husky.core.executor;

import com.lyl.husky.core.api.ElasticJob;
import com.lyl.husky.core.api.dataflow.DataflowJob;
import com.lyl.husky.core.api.simple.SimpleJob;
import com.lyl.husky.core.exception.JobConfigurationException;
import com.lyl.husky.core.executor.type.DataflowJobExecutor;
import com.lyl.husky.core.executor.type.ScriptJobExecutor;
import com.lyl.husky.core.executor.type.SimpleJobExecutor;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * 作业执行器工厂
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class JobExecutorFactory {

    /**
     * 获取作业执行器.
     *
     * @param elasticJob 分布式弹性作业
     * @param jobFacade 作业内部服务门面服务
     * @return 作业执行器
     */
    @SuppressWarnings("unchecked")
    public static AbstractElasticJobExecutor getJobExecutor(final ElasticJob elasticJob, final JobFacade jobFacade){
        if (null == elasticJob){
            return new ScriptJobExecutor(jobFacade);
        }
        if (elasticJob instanceof SimpleJob){
            return new SimpleJobExecutor((SimpleJob) elasticJob, jobFacade);
        }
        if (elasticJob instanceof DataflowJob){
            return new DataflowJobExecutor((DataflowJob<Object>) elasticJob, jobFacade);
        }
        throw new JobConfigurationException("Cannot support job type '%s'", elasticJob.getClass().getCanonicalName());
    }

}
