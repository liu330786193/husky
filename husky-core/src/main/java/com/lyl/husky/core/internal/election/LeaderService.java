package com.lyl.husky.core.internal.election;

import com.lyl.husky.core.internal.server.ServerService;
import com.lyl.husky.core.internal.storage.JobNodeStorage;
import com.lyl.husky.core.reg.base.CoordinatorRegistryCenter;
import lombok.extern.slf4j.Slf4j;

/**
 * 主节点服务
 */
@Slf4j
public class LeaderService {

    public final String jobName;
    private final ServerService serverService;
    private final JobNodeStorage jobNodeStorage;

    public LeaderService(final CoordinatorRegistryCenter regCenter, final String jobName){
        this.jobName = jobName;
        jobNodeStorage = new JobNodeStorage(regCenter, jobName);
        serverService = new ServerService(regCenter, jobName);
    }

    /**
     * 选举主节点
     */
    public void electLeader(){
        log.debug("Elect a new leader now.");
        jobNodeStorage.executeInLeader();
    }

}
