package com.lyl.husky.core.internal.instance;

import com.lyl.husky.core.api.strategy.JobInstance;
import com.lyl.husky.core.internal.server.ServerService;
import com.lyl.husky.core.internal.storage.JobNodeStorage;
import com.lyl.husky.core.reg.base.CoordinatorRegistryCenter;

import java.util.LinkedList;
import java.util.List;

/**
 * 作业运行实例服务
 */
public class InstanceService {

    private final JobNodeStorage jobNodeStorage;
    private final InstanceNode instanceNode;
    private final ServerService serverService;

    public InstanceService (final CoordinatorRegistryCenter regCenter, final String jobName){
        jobNodeStorage = new JobNodeStorage(regCenter, jobName);
        instanceNode = new InstanceNode(jobName);
        serverService = new ServerService(regCenter, jobName);
    }

    /**
     * 持久化作业运行实例上线相关信息
     */
    public void persistOnline(){
        jobNodeStorage.fillEphemeralJobNode(instanceNode.getLocalInstanceNode(), "");
    }

    /**
     * 删除作业运行状态
     */
    public void removeInstance(){
        jobNodeStorage.removeJobNodeIfExisted(instanceNode.getLocalInstanceNode());
    }

    /**
     * 清理作业出发标记
     */
    public void clearTriggerFlag(){
        jobNodeStorage.updateJobNode(instanceNode.getLocalInstanceNode(), "");
    }

    /**
     * 获取可分片的作业运行实例
     */
    public List<JobInstance> getAvailableJobInstances(){
        List<JobInstance> result = new LinkedList<>();
        for (String each : jobNodeStorage.getJobNodeChildrenKeys(InstanceNode.ROOT)){
            JobInstance jobInstance = new JobInstance(each);
            if (serverService.isEnableServer(jobInstance.getIp())){
                result.add(new JobInstance(each));
            }
        }
        return result;
    }

    /**
     * 判断当前作业运行实例的节点是否仍然存在
     */
    public boolean isLocalJobInstanceExisted(){
        return jobNodeStorage.isJobNodeExisted(instanceNode.getLocalInstanceNode());
    }

}
