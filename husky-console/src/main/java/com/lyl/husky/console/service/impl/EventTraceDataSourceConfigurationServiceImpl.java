package com.lyl.husky.console.service.impl;

import com.google.common.base.Optional;
import com.lyl.husky.console.domain.EventTraceDataSourceConfiguration;
import com.lyl.husky.console.domain.EventTraceDataSourceConfigurations;
import com.lyl.husky.console.domain.GlobalConfiguration;
import com.lyl.husky.console.respository.ConfigurationsXmlRepository;
import com.lyl.husky.console.respository.impl.ConfigurationsXmlRepositoryImpl;
import com.lyl.husky.console.service.EventTraceDataSourceConfigurationService;

/**
 * 事件追踪数据源配置服务实现类
 */
public class EventTraceDataSourceConfigurationServiceImpl implements EventTraceDataSourceConfigurationService {

    private ConfigurationsXmlRepository configurationsXmlRepository = new ConfigurationsXmlRepositoryImpl();

    @Override
    public EventTraceDataSourceConfigurations loadAll() {
        return loadGlobal().getEventTraceDataSourceConfigurations();
    }

    @Override
    public EventTraceDataSourceConfiguration load(String name) {
        GlobalConfiguration configs = loadGlobal();
        EventTraceDataSourceConfiguration result = find(name, configs.getEventTraceDataSourceConfigurations());
        setActivated(configs, result);
        return result;
    }

    private void setActivated(final GlobalConfiguration configs, EventTraceDataSourceConfiguration toBeConnectedConfig) {
        EventTraceDataSourceConfiguration activatedConfig = findActivatedDataSourceConfiguration(configs);
        if (!toBeConnectedConfig.equals(activatedConfig)){
            if (null != activatedConfig){
                activatedConfig.setActivated(false);
            }
            toBeConnectedConfig.setActivated(true);
            configurationsXmlRepository.save(configs);
        }
    }

    @Override
    public EventTraceDataSourceConfiguration find(String name, EventTraceDataSourceConfigurations configs) {
        for (EventTraceDataSourceConfiguration each : configs.getEventTraceDataSourceConfigurationSet()){
            if (name.equals(each.getName())){
                return each;
            }
        }
        return null;
    }

    private EventTraceDataSourceConfiguration findActivatedDataSourceConfiguration(final GlobalConfiguration configs) {
        for (EventTraceDataSourceConfiguration each : configs.getEventTraceDataSourceConfigurations().getEventTraceDataSourceConfigurationSet()){
            if (each.isActivated()){
                return each;
            }
        }
        return null;
    }

    @Override
    public Optional<EventTraceDataSourceConfiguration> loadActivated() {
        return Optional.fromNullable(findActivatedDataSourceConfiguration(loadGlobal()));
    }

    @Override
    public boolean add(EventTraceDataSourceConfiguration config) {
        GlobalConfiguration configs = loadGlobal();
        boolean result = configs.getEventTraceDataSourceConfigurations().getEventTraceDataSourceConfigurationSet().add(config);
        if (result){
            configurationsXmlRepository.save(configs);
        }
        return result;
    }

    @Override
    public void delete(String name) {
        GlobalConfiguration configs = loadGlobal();
        EventTraceDataSourceConfiguration toBeRemovedConfig = find(name, configs.getEventTraceDataSourceConfigurations());
        if (null != toBeRemovedConfig){
            configs.getEventTraceDataSourceConfigurations().getEventTraceDataSourceConfigurationSet();
            configurationsXmlRepository.save(configs);
        }
    }

    private GlobalConfiguration loadGlobal(){
        GlobalConfiguration result = configurationsXmlRepository.load();
        if (null == result.getEventTraceDataSourceConfigurations()){
            result.setEventTraceDataSourceConfigurations(new EventTraceDataSourceConfigurations());
        }
        return result;
    }
}
