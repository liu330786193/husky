package com.lyl.husky.core.internal.schedule;

import com.lyl.husky.core.internal.election.LeaderNode;
import com.lyl.husky.core.internal.storage.JobNodePath;

/**
 * 分片节点路径
 */
public final class ShardingNode {

    /**
     * 执行状态根节点.
     */
    public static final String ROOT = "sharding";

    public static final String INSTANCE_APPENDIX = "instance";

    public static final String INSTANCE = ROOT + "/%s/" + INSTANCE_APPENDIX;

    public static final String RUNNING_APPENDIX = "running";

    public static final String RUNNING = ROOT + "/%s/" + RUNNING_APPENDIX;

    public static final String MISFIRE = ROOT + "/%s/misfire";

    public static final String DISABLED = ROOT + "/%s/disabled";

    public static final String LEADER_ROOT = LeaderNode.ROOT + "/" + ROOT;

    public static final String NECESSARY = LEADER_ROOT + "/necessary";

    public static final String PROCESSING = LEADER_ROOT + "/processing";

    private final JobNodePath jobNodePath;

    public ShardingNode(final String jobName){
        jobNodePath = new JobNodePath(jobName);
    }

    public static String getInstanceNode(final int item) {
        return String.format(INSTANCE, item);
    }

    /**
     * 获取作业运行状态节点路径
     */
    public static String getRunningNode(final int item){
        return String.format(RUNNING, item);
    }

    static String getMisfireNode(final int item) {
        return String.format(MISFIRE, item);
    }

    static String getDisabledNode(final int item) {
        return String.format(DISABLED, item);
    }

    /**
     * 根据运行中的分片路径获取分片项
     */
    public Integer getItemByRunningItemPath(final String path) {
        if (!isRunningItemPath(path)) {
            return null;
        }
        return Integer.parseInt(path.substring(jobNodePath.getFullPath(ROOT).length() + 1, path.lastIndexOf(RUNNING_APPENDIX) - 1));
    }
    private boolean isRunningItemPath(final String path) {
        return path.startsWith(jobNodePath.getFullPath(ROOT)) && path.endsWith(RUNNING_APPENDIX);
    }

}
