package com.lyl.husky.core.internal.server;

import com.lyl.husky.core.internal.instance.InstanceNode;
import com.lyl.husky.core.internal.schedule.JobRegistry;
import com.lyl.husky.core.internal.storage.JobNodeStorage;
import com.lyl.husky.core.reg.base.CoordinatorRegistryCenter;
import com.mysql.fabric.Server;

import java.util.List;

/**
 * 作业服务器服务
 */
public final class ServerService {

    public final String jobName;
    private final JobNodeStorage jobNodeStorage;
    private final ServerNode serverNode;

    public ServerService(final CoordinatorRegistryCenter regCenter, final String jobName){
        this.jobName = jobName;
        jobNodeStorage = new JobNodeStorage(regCenter, jobName);
        serverNode = new ServerNode(jobName);
    }

    /**
     * 持久化作业服务器上线信息
     */
    public void persistOnline(final boolean enable){
        if (!JobRegistry.getInstance().isShutdown(jobName)){
            jobNodeStorage.fillJobNode(serverNode.getServerNode(JobRegistry.getInstance().getJobInstance(jobName).getIp()), enable ? "" : ServerStatus.DISABLED.name());
        }
    }

    /**
     * 获取是否还有可用的作业服务器
     */
    public boolean hasAvailableServers(){
        List<String> servers = jobNodeStorage.getJobNodeChildrenKeys(ServerNode.ROOT);
        for (String each :servers){
            if (isAvailableServer(each)){
                return true;
            }
        }
        return false;
    }

    /**
     * 判断作业服务器是否可用
     */
    public boolean isAvailableServer(final String ip){
        return isEnableServer(ip) && hasOnlineInstances(ip);
    }

    private boolean hasOnlineInstances(final String ip) {
        for (String each : jobNodeStorage.getJobNodeChildrenKeys(InstanceNode.ROOT)){
            if (each.startsWith(ip)){
                return true;
            }
        }
        return false;
    }

    /**
     * 判断服务器是否可用
     * @param ip
     * @return
     */
    private boolean isEnableServer(String ip) {
        return !ServerStatus.DISABLED.name().equals(jobNodeStorage.getJobNodeData(serverNode.getServerNode(ip)));
    }

    /**
     * 服务器状态
     */
    public enum ServerStatus {
        DISABLED
    }

}
