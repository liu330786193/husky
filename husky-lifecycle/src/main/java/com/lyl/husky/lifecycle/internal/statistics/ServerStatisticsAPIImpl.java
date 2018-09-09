package com.lyl.husky.lifecycle.internal.statistics;

import com.lyl.husky.core.internal.storage.JobNodePath;
import com.lyl.husky.core.reg.base.CoordinatorRegistryCenter;
import com.lyl.husky.lifecycle.api.ServerStatisticsAPI;
import com.lyl.husky.lifecycle.domain.ServerBriefInfo;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 作业服务器状态展示的实现类
 */
@RequiredArgsConstructor
@Data
public final class ServerStatisticsAPIImpl implements ServerStatisticsAPI {

    private final CoordinatorRegistryCenter regCenter;

    @Override
    public int getServersTotalCount() {
        Set<String> servers = new HashSet<>();
        for (String jobName : regCenter.getChildrenKeys("/")) {
            JobNodePath jobNodePath = new JobNodePath(jobName);
            for (String each : regCenter.getChildrenKeys(jobNodePath.getServerNodePath())){
                servers.add(each);
            }
        }
        return 0;
    }

    @Override
    public Collection<ServerBriefInfo> getAllServersBriefInfo() {
        ConcurrentHashMap<String, ServerBriefInfo> servers = new ConcurrentHashMap<>();
        for (String jobName : regCenter.getChildrenKeys("/")) {
            JobNodePath jobNodePath = new JobNodePath(jobName);
            for (String each : regCenter.getChildrenKeys(jobNodePath.getServerNodePath())) {
                servers.putIfAbsent(each, new ServerBriefInfo(each));
                ServerBriefInfo serverInfo = servers.get(each);
                if ("DISABLED".equalsIgnoreCase(regCenter.get(jobNodePath.getServerNodePath(each)))) {
                    serverInfo.getDisabledJobsNum().incrementAndGet();
                }
                serverInfo.getJobNames().add(jobName);
                serverInfo.setJobsNum(serverInfo.getJobNames().size());
            }
            List<String> instances = regCenter.getChildrenKeys(jobNodePath.getInstancesNodePath());
            for (String each : instances) {
                String serverIp = each.split("@-@")[0];
                ServerBriefInfo serverInfo = servers.get(serverIp);
                if (null != serverInfo) {
                    serverInfo.getInstances().add(each);
                    serverInfo.setInstancesNum(serverInfo.getInstances().size());
                }
            }
        }
        List<ServerBriefInfo> result = new ArrayList<>(servers.values());
        Collections.sort(result);
        return result;
    }
}
