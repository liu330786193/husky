package com.lyl.husky.core.reg.base;

import java.util.List;

/**
 * 用于协调分布式服务的注册中心
 */
public interface CoordinatorRegistryCenter extends RegistryCenter {

    /**
     * 直接从注册中心而非本地缓存获取数据
     */
    String getDirectly(String key);

    /**
     * 获取子节点名称集合
     */
    List<String> getChildrenKeys(String key);

    /**
     * 获取子节点数量.
     *
     * @param key 键
     * @return 子节点数量
     */
    int getNumChildren(String key);

    /**
     * 持久化临时注册数据.
     */
    void persistEphemeral(String key, String value);

    /**
     * 持久化顺序注册数据
     */
    String persistSequential(String key, String value);

    /**
     * 持久化临时顺序注册数据
     * 包含10位顺序数字的znode名称
     */
    void persistEphemeralSequential(String key);

    /**
     * 添加本地缓存
     */
    void addCacheData(String cachePath);

    /**
     * 释放本地缓存
     */
    void evictCacheData(String cachePath);

    /**
     * 获取注册中心数据缓存对象
     */
    Object getRawCache(String cachePath);

}
