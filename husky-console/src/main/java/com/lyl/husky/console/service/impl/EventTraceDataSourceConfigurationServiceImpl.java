package com.lyl.husky.console.service.impl;

import com.lyl.husky.console.domain.EventTraceDataSourceConfiguration;
import com.lyl.husky.console.domain.EventTraceDataSourceConfigurations;
import com.lyl.husky.console.service.EventTraceDataSourceConfigurationService;

import java.util.Optional;

/**
 * 事件追踪数据源配置服务实现类
 */
public class EventTraceDataSourceConfigurationServiceImpl implements EventTraceDataSourceConfigurationService {

    @Override
    public EventTraceDataSourceConfigurations loadAll() {
        return ;
    }

    @Override
    public EventTraceDataSourceConfiguration load(String name) {
        return null;
    }

    @Override
    public EventTraceDataSourceConfiguration find(String name, EventTraceDataSourceConfigurations configs) {
        return null;
    }

    @Override
    public Optional<EventTraceDataSourceConfiguration> loadActivated() {
        return Optional.empty();
    }

    @Override
    public boolean add(EventTraceDataSourceConfiguration config) {
        return false;
    }

    @Override
    public void delete(String name) {

    }
}
