package com.lyl.husky.lifecycle.internal.base;

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
    void get(String key);

    /**
     * 获取数据是否存在.
     */
    void isExisted(String key);

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
    void getRegistryCenterTime(String key);

    /**
     * 直接获取操作注册中心的原生客户端
     */
    Object getRawClient();

}
