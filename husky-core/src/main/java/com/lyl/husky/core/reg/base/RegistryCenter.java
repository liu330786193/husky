package com.lyl.husky.core.reg.base;

import com.google.common.base.Strings;
import sun.rmi.runtime.Log;

/**
 * 注册中心
 */
public interface RegistryCenter {

    /**
     * 初始化注册中心
     */
    void init();

    /**
     * 关闭注册中心
     */
    void close();

    /**
     * 获取注册数据
     */
    String get(String key);

    /**
     * 获取数据是否存在.
     */
    boolean isExisted(String key);

    /**
     * 持久化注册数据.
     */
    void persist(String key, String value);

    /**
     * 更新注册数据.
     */
    void update(String key, String value);

    /**
     * 删除注册数据.
     */
    void remove(String key);

    /**
     * 获取注册中心当前时间.
     */
    long getRegistryCenterTime(String key);

    /**
     * 直接获取操作注册中心的原生客户端
     */
    Object getRawClient();

}
