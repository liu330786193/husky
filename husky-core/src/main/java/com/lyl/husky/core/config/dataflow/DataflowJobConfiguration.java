package com.lyl.husky.core.config.dataflow;

import com.lyl.husky.core.api.JobType;
import com.lyl.husky.core.config.JobCoreConfiguration;
import com.lyl.husky.core.config.JobTypeConfiguration;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * 数据流作业配置信息
 */
@Getter
@RequiredArgsConstructor
public final class DataflowJobConfiguration implements JobTypeConfiguration {

    private final JobCoreConfiguration coreConfig;
    private final JobType jobType = JobType.DATAFLOW;
    private final String jobClass;
    private final boolean streamingProcess;

}
