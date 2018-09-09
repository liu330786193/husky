package com.lyl.husky.core.config;

import com.lyl.husky.core.api.JobType;

/**
 * 作业类型配置
 */
public interface JobTypeConfiguration {

    /**
     * 获取作业类型
     */
    JobType getJobType();

    /**
     * 获取作业实现类名称
     */
    String getJobClass();

    /**
     * 获取作业核心配置
     */
    JobCoreConfiguration getCoreConfig();

}
