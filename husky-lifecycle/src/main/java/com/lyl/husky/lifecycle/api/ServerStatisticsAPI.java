package com.lyl.husky.lifecycle.api;

import com.lyl.husky.lifecycle.domain.ServerBriefInfo;

import java.util.Collection;

/**
 * 作业服务器状态展示的API
 */
public interface ServerStatisticsAPI {

    /**
     * 获取作业服务器总数
     */
    int getServersTotalCount();

    /**
     * 获取所有作业服务器简明信息
     */
    Collection<ServerBriefInfo> getAllServersBriefInfo();

}
