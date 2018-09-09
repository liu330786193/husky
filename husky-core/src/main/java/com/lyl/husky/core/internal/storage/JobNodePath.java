package com.lyl.husky.core.internal.storage;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public final class JobNodePath {

    private static final String LEADER_HOST_NODE = "leader/election/instance";
    private static final String CONFIG_NODE = "config";
    private static final String SERVERS_NODE = "servers";
    private static final String INSTANCES_NODE = "instances";
    private static final String SHARDING_NODE = "sharding";
    private final String jobName;

    /**
     * 获取节点全路径
     */
    public String getFullPath(final String node){
        return String.format("/%s/%s", jobName, node);
    }

    /**
     * 获取配置节点根路径
     */
    public String getConfigNodePath() {
        return String.format("/%s/%s", jobName, CONFIG_NODE);
    }

    /**
     * 获取leader选举地址节点路径
     */
    public String getLeaderHostNodePath(){
        return String.format("/%s/%s", jobName, LEADER_HOST_NODE);
    }

    /**
     * 获取节点IP地址根路径
     */
    public String getServerNodePath() {
        return String.format("/%s/%s", jobName, SERVERS_NODE);
    }

    /**
     * 根据IP地址获取作业节点路径
     */
    public String getServerNodePath(final String serverIp) {
        return String.format("%s/%s", getServerNodePath(), serverIp);
    }

    /**
     * 获取作业实例节点根路径
     */
    public String getInstancesNodePath() {
        return String.format("/%s/%s", jobName, INSTANCES_NODE);
    }

    /**
     * 根据作业实例ID获取作业实例节点路径
     */
    public String getInstanceNodePath(final String instanceId) {
        return String.format("%s/%s", getInstancesNodePath(), instanceId);
    }

    /**
     * 获取分片节点根路径
     */
    public String getShardingNodePath() {
        return String.format("/%s/%s", jobName, SHARDING_NODE);
    }

    /**
     * 获取分片节点路径
     */
    public String getShardingNodePath(final String item, final String nodeName) {
        return String.format("%s/%s/%s", getShardingNodePath(), item, nodeName);
    }

}
