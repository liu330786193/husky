package com.lyl.husky.lifecycle.api;

import com.lyl.husky.lifecycle.domain.JobBriefInfo;

import java.util.Collection;

/**
 * 作业状态展示的API
 */
public interface JobStatisticsAPI {

    /**
     * 获取作业总数
     */
    int getJobsTotalCount();

    /**
     * 获取所有作业简明信息
     */
    Collection<JobBriefInfo> getAllJobsBriefInfo();

    /**
     * 获取作业简明信息
     */
    JobBriefInfo getJobBriefInfo(String jobName);

    /**
     * 获取该IP下所有作业简明信息
     */
    Collection<JobBriefInfo> getJobsBriefInfo(String ip);

}
