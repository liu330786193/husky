package com.lyl.husky.core.internal.config;

import com.lyl.husky.core.internal.storage.JobNodePath;

/**
 * 配置节点路径
 */
public final class ConfigurationNode {

    public static final String ROOT = "config";
    private final JobNodePath jobNodePath;

    public ConfigurationNode(final String jobName){
        jobNodePath = new JobNodePath(jobName);
    }

    /**
     * 判断是否为作业配置根路径
     */
    public boolean isConfigPath(final String path){
        return jobNodePath.getConfigNodePath().equals(path);
    }

}
