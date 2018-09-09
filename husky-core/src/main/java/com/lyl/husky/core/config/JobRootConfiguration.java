package com.lyl.husky.core.config;

import com.lyl.husky.core.api.JobType;

/**
 * 作业配置根接口
 */
public interface JobRootConfiguration {

    /**
     * 获取作业类型配置
     */
    JobTypeConfiguration getTypeConfig();

}
