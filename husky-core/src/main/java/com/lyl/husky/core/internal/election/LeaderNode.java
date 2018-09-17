package com.lyl.husky.core.internal.election;

import com.lyl.husky.core.internal.storage.JobNodePath;

/**
 * 主节点路径
 */
public final class LeaderNode {

    /**
     * 主节点根路径
     */
    public static final String ROOT = "leader";
    public static final String ELECTION_ROOT = ROOT + "/election";
    public static final String INSTANCE = ELECTION_ROOT + "/instance";
    public static final String LATCH = ELECTION_ROOT + "/latch";
    private final JobNodePath jobNodePath;

    public LeaderNode(final String jobName){
        jobNodePath = new JobNodePath(jobName);
    }

    boolean isLeaderInstancePath(final String path){
        return jobNodePath.getFullPath(INSTANCE).equals(path);
    }

}
