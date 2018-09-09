package com.lyl.husky.lifecycle.domain;

import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 服务器维度简明信息对象
 */
@RequiredArgsConstructor
@Data
public final class ServerBriefInfo implements Serializable, Comparable<ServerBriefInfo> {

    private final String serverIp;
    private final Set<String> instances = new HashSet<>();
    private final Set<String> jobNames = new HashSet<>();
    private int instancesNum;
    private int jobsNum;
    private AtomicInteger disabledJobsNum = new AtomicInteger();

    @Override
    public int compareTo(ServerBriefInfo o) {
        return getServerIp().compareTo(o.getServerIp());
    }
}
