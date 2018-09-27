package com.lyl.husky.core.internal.guarantee;

import com.google.common.base.Joiner;
import com.lyl.husky.core.internal.storage.JobNodePath;

/**
 * @Author lyl
 * @Description 保证分布式任务全部开始和结束状态节点路径
 * @Date 2018/9/27 下午1:57
 */
public final class GuaranteeNode {

    static final String ROOT = "guarantee";

    static final String STARTED_ROOT = ROOT + "/started";

    static final String COMPLETED_ROOT = ROOT + "/completed";

    private final JobNodePath jobNodePath;

    GuaranteeNode(final String jobName) {
        jobNodePath = new JobNodePath(jobName);
    }

    static String getStartedNode(final int shardingItem) {
        return Joiner.on("/").join(STARTED_ROOT, shardingItem);
    }

    static String getCompletedNode(final int shardingItem) {
        return Joiner.on("/").join(COMPLETED_ROOT, shardingItem);
    }

    boolean isStartedRootNode(final String path) {
        return jobNodePath.getFullPath(STARTED_ROOT).equals(path);
    }

    boolean isCompletedRootNode(final String path) {
        return jobNodePath.getFullPath(COMPLETED_ROOT).equals(path);
    }

}
