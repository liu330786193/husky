package com.lyl.husky.core.internal.election;

import com.lyl.husky.core.internal.schedule.JobRegistry;
import com.lyl.husky.core.internal.server.ServerService;
import com.lyl.husky.core.internal.storage.JobNodeStorage;
import com.lyl.husky.core.internal.storage.LeaderExecutionCallback;
import com.lyl.husky.core.reg.base.CoordinatorRegistryCenter;
import com.lyl.husky.core.util.concurrent.BlockUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.zookeeper.server.quorum.Leader;

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
        jobNodeStorage.executeInLeader(LeaderNode.LATCH, new LeaderElectionExecutionCallback());
    }

    /**
     * 判断您当前节点是否为主节点
     * <p>
     *     如果主节点正在选举中而导致取不到主节点，则阻塞至主节点选举完成再返回
     * </p>
     */
    public boolean isLeaderUntilBlock(){
        while (!hasLeader() && serverService.hasAvailableServers()){
            log.info("Leader is electing, waiting for {} ms", 100);
            BlockUtils.waitingShortTime();
            if (!JobRegistry.getInstance().isShutdown(jobName) && serverService.isAvailableServer(JobRegistry.getInstance().getJobInstance(jobName).getIp())){
                electLeader();
            }
        }
        return isLeader();
    }

    /**
     * 判断当前节点是否为主节点
     */
    public boolean isLeader(){
        return !JobRegistry.getInstance().isShutdown(jobName) && JobRegistry.getInstance()
                .getJobInstance(jobName)
                .getJobInstanceId()
                .equals(jobNodeStorage.getJobNodeData(LeaderNode.INSTANCE));
    }

    /**
     * 删除主节点重新选举
     */
    public void removeLeader(){
        jobNodeStorage.removeJobNodeIfExisted(LeaderNode.INSTANCE);
    }

    /**
     * 判断是否已经有主节点
     */
    public boolean hasLeader(){
        return jobNodeStorage.isJobNodeExisted(LeaderNode.INSTANCE);
    }

    public class LeaderElectionExecutionCallback implements LeaderExecutionCallback {
        @Override
        public void execute() {
            if (!hasLeader()){
                jobNodeStorage.fillEphemeralJobNode(LeaderNode.INSTANCE, JobRegistry.getInstance().getJobInstance(jobName).getJobInstanceId());
            }
        }
    }

}
