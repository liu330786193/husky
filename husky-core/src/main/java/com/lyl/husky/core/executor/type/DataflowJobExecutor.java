package com.lyl.husky.core.executor.type;

import com.lyl.husky.core.api.ShardingContext;
import com.lyl.husky.core.api.dataflow.DataflowJob;
import com.lyl.husky.core.config.dataflow.DataflowJobConfiguration;
import com.lyl.husky.core.executor.AbstractElasticJobExecutor;
import com.lyl.husky.core.executor.JobFacade;

import java.util.List;

/**
 * 数据流作业执行器
 */
public final class DataflowJobExecutor extends AbstractElasticJobExecutor {

    private final DataflowJob<Object> dataflowJob;

    public DataflowJobExecutor(final DataflowJob<Object> dataflowJob, final JobFacade jobFacade){
        super(jobFacade);
        this.dataflowJob = dataflowJob;
    }

    @Override
    protected void process(ShardingContext shardingContext) {
        DataflowJobConfiguration dataflowJobConfig = (DataflowJobConfiguration) getJobRootConfig().getTypeConfig();
        if (dataflowJobConfig.isStreamingProcess()){
            streamingExecute(shardingContext);
        } else {
            oneOffExecute(shardingContext);
        }
    }

    private void streamingExecute(ShardingContext shardingContext) {
        List<Object> data = fetchData(shardingContext);
        while (null != data && !data.isEmpty()){
            if (!getJobFacade().isEligibleForJobRunning()){
                break;
            }
            data = fetchData(shardingContext);
        }
    }

    private void oneOffExecute(ShardingContext shardingContext) {
        List<Object> data = fetchData(shardingContext);
        if (null != data && !data.isEmpty()){
            processData(shardingContext, data);
        }
    }

    private List<Object> fetchData(ShardingContext shardingContext) {
        return dataflowJob.fetchData(shardingContext);
    }

    private void processData(ShardingContext shardingContext, List<Object> data) {
        dataflowJob.processData(shardingContext, data);
    }

}
