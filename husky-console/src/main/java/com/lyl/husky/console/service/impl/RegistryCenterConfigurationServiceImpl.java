package com.lyl.husky.console.service.impl;

import com.lyl.husky.console.domain.RegistryCenterConfiguration;
import com.lyl.husky.console.domain.RegistryCenterConfigurations;
import com.lyl.husky.console.service.RegistryCenterConfigurationService;

import java.util.Optional;

/**
 * 注册中心配置服务实现类
 */
public class RegistryCenterConfigurationServiceImpl implements RegistryCenterConfigurationService {

    private Configuraionxml

    @Override
    public RegistryCenterConfigurations loadAll() {
        return null;
    }

    @Override
    public RegistryCenterConfiguration load(String name) {
        return null;
    }

    @Override
    public RegistryCenterConfiguration find(String name, RegistryCenterConfigurations configs) {
        return null;
    }

    @Override
    public Optional<RegistryCenterConfiguration> loadActivated() {
        return Optional.empty();
    }

    @Override
    public boolean add(RegistryCenterConfiguration config) {
        return false;
    }

    @Override
    public void delete(String name) {

    }

}
