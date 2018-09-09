package com.lyl.husky.lifecycle.domain;

import lombok.Data;

import java.io.Serializable;

/**
 * 作业简明信息对象
 */
@Data
public final class JobBriefInfo implements Serializable, Comparable<JobBriefInfo> {

    private String jobName;
    private JobStatus status;
    private String description;
    private String cron;
    private int instanceCount;
    private int shardingTotalCount;

    @Override
    public int compareTo(final JobBriefInfo o) {
        return getJobName().compareTo(o.getJobName());
    }

    /**
     * 作业状态
     */
    public enum JobStatus{
        OK,
        CRASHED,
        DISABLED,
        SHARDING_FLAG
    }

}
