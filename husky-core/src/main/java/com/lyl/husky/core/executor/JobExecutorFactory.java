package com.lyl.husky.core.executor;

import com.lyl.husky.core.api.ElasticJob;
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
            return new
        }
    }

}
