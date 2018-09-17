package com.lyl.husky.core.internal.server;

import com.lyl.husky.core.internal.schedule.JobRegistry;
import com.lyl.husky.core.internal.storage.JobNodePath;
import com.lyl.husky.core.util.env.IpUtils;

import java.util.regex.Pattern;

/**
 * 服务器节点路径
 */
public final class ServerNode {

    /**
     * 服务器信息根节点
     */
    public static final String ROOT = "servers";
    public static final String SERVERS = ROOT + "/%s";
    private final String jobName;
    private final JobNodePath jobNodePath;

    public ServerNode(final String jobName){
        this.jobName = jobName;
        jobNodePath = new JobNodePath(jobName);
    }

    /**
     * 判断给定路径是否为服务作业器路径
     */
    public boolean isServerPath(final String path){
        return Pattern.compile(jobNodePath.getFullPath(ServerNode.ROOT) + "/" + IpUtils.IP_REGEX).matcher(path).matches();
    }

    /**
     * 判断给定路径是否为本地作业服务器路径
     */
    public boolean isLocalServerPath(final String path){
        return path.equals(jobNodePath.getFullPath(String.format(SERVERS, JobRegistry.getInstance().getJobInstance(jobName).getIp())));
    }

    public String getServerNode(final String ip){
        return String.format(SERVERS, ip);
    }

}
