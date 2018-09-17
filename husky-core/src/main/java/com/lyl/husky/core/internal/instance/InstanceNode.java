package com.lyl.husky.core.internal.instance;

import com.lyl.husky.core.internal.schedule.JobRegistry;
import com.lyl.husky.core.internal.storage.JobNodePath;

import java.util.regex.Pattern;

/**
 * 运行实例节点路径
 */
public final class InstanceNode {

    public static final String ROOT = "instances";

    private static final String INSTANCES = ROOT + "/%s";

    private final String jobName;

    private final JobNodePath jobNodePath;


    public InstanceNode(final String jobName){
        this.jobName = jobName;
        jobNodePath = new JobNodePath(jobName);
    }

    /**
     * 获取作业运行实例全路径
     */
    public String getInstanceFullPath(){
        return jobNodePath.getFullPath(InstanceNode.ROOT);
    }

    /**
     * 判断给定路径是否为作业运行实例路径
     */
    public boolean isInstancePath(final String path){
        return path.startsWith(jobNodePath.getFullPath(InstanceNode.ROOT));
    }

    public boolean isLocalInstancePath(final String path){
        return path.equals(jobNodePath.getFullPath(String.format(INSTANCES, JobRegistry.getInstance().getJobInstance(jobName).getJobInstanceId())));
    }

    public String getLocalInstanceNode(){
        return String.format(INSTANCES, JobRegistry.getInstance().getJobInstance(jobName).getJobInstanceId());
    }

}
