package com.lyl.husky.console.respository.impl;

import com.lyl.husky.console.domain.GlobalConfiguration;
import com.lyl.husky.console.respository.ConfigurationsXmlRepository;

/**
 * 基于XML的数据访问器实现类
 */
public final class ConfigurationsXmlRepositoryImpl extends AbstractXmlRepositoryImpl<GlobalConfiguration> implements ConfigurationsXmlRepository {

    public ConfigurationsXmlRepositoryImpl() {
        super("Configurations.xml", GlobalConfiguration.class);
    }

}
