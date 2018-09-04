package com.lyl.husky.console.service;

import com.lyl.husky.console.domain.RegistryCenterConfiguration;
import com.lyl.husky.console.domain.RegistryCenterConfigurations;

import java.util.Optional;

/**
 * 这个中心配置服务
 */
public interface RegistryCenterConfigurationService {

    /**
     * 读取全部注册中心配置
     */
    RegistryCenterConfigurations loadAll();

    /**
     * 读取注册中心配置
     */
    RegistryCenterConfiguration load(String name);

    /**
     * 查找注册中心配置
     */
    RegistryCenterConfiguration find(final String name, final RegistryCenterConfigurations configs);

    /**
     * 读取已连接的注册中心配置
     */
    Optional<RegistryCenterConfiguration> loadActivated();

    /**
     * 添加注册中心配置
     */
    boolean add(RegistryCenterConfiguration config);

    /**
     * 删除注册中心配置
     */
    void delete(String name);

}
