package com.lyl.husky.lifecycle.api;

import com.lyl.husky.lifecycle.domain.JobSetting;

/**
 * 作业配置的API
 */
public interface JobSettingsAPI {

    /**
     * 获取作业设置
     */
    JobSetting getJobSettings(String jobName);

    /**
     * 更新作业设置
     */
    void updateJobSetting(JobSetting jobSetting);

    /**
     * 删除作业设置
     */
    void removeJobSetting(final String jobName);
}
