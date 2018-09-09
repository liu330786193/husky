package com.lyl.husky.lifecycle.internal.operate;

import com.lyl.husky.core.internal.storage.JobNodePath;
import com.lyl.husky.core.reg.base.CoordinatorRegistryCenter;
import com.lyl.husky.lifecycle.api.ShardingOperateAPI;

/**
 * 操作分片的实现类
 */
public final class ShardingOperateAPIImpl implements ShardingOperateAPI {

    private final CoordinatorRegistryCenter regCenter;

    public ShardingOperateAPIImpl(final CoordinatorRegistryCenter regCenter){
        this.regCenter = regCenter;
    }

    @Override
    public void disable(String jobName, String item) {
        disableOrEnableJobs(jobName, item, true);
    }

    @Override
    public void enable(String jobName, String item) {
        disableOrEnableJobs(jobName, item, false);
    }

    private void disableOrEnableJobs(final String jobName, final String item, final boolean disabled) {
        JobNodePath jobNodePath = new JobNodePath(jobName);
        String shardingDisabledNodePath = jobNodePath.getShardingNodePath(item, "disabled");
        if (disabled) {
            regCenter.persist(shardingDisabledNodePath, "");
        } else {
            regCenter.remove(shardingDisabledNodePath);
        }
    }

}
