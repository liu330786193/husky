package com.lyl.husky.console.service.impl;

import com.google.common.base.Optional;
import com.lyl.husky.console.domain.GlobalConfiguration;
import com.lyl.husky.console.domain.RegistryCenterConfiguration;
import com.lyl.husky.console.domain.RegistryCenterConfigurations;
import com.lyl.husky.console.respository.ConfigurationsXmlRepository;
import com.lyl.husky.console.respository.impl.ConfigurationsXmlRepositoryImpl;
import com.lyl.husky.console.service.RegistryCenterConfigurationService;

/**
 * 注册中心配置服务实现类
 */
public class RegistryCenterConfigurationServiceImpl implements RegistryCenterConfigurationService {

    private ConfigurationsXmlRepository configurationsXmlRepository = new ConfigurationsXmlRepositoryImpl();

    @Override
    public RegistryCenterConfigurations loadAll() {
        return loadGlobal().getRegistryCenterConfigurations();
    }

    @Override
    public RegistryCenterConfiguration load(String name) {
        GlobalConfiguration configs = loadGlobal();
        RegistryCenterConfiguration result = find(name, configs.getRegistryCenterConfigurations());
        setActivated(configs, result);
        return null;
    }

    private void setActivated(GlobalConfiguration configs, RegistryCenterConfiguration toBeConnectedConfig) {
        RegistryCenterConfiguration activatedConfig = findActivatedRegistryCenterConfiguration(configs);
        if (!toBeConnectedConfig.equals(activatedConfig)){
            if (null != activatedConfig){
                activatedConfig.setActivated(false);
            }
            toBeConnectedConfig.setActivated(true);
            configurationsXmlRepository.save(configs);
        }
    }

    @Override
    public RegistryCenterConfiguration find(String name, RegistryCenterConfigurations configs) {
        for (RegistryCenterConfiguration each : configs.getRegistryCenterConfigurationSet()){
            if (name.equals(each.getName())){
                return each;
            }
        }
        return null;
    }

    private RegistryCenterConfiguration findActivatedRegistryCenterConfiguration(final GlobalConfiguration configs) {
        for (RegistryCenterConfiguration each : configs.getRegistryCenterConfigurations().getRegistryCenterConfigurationSet()) {
            if (each.isActivated()) {
                return each;
            }
        }
        return null;
    }

    @Override
    public Optional<RegistryCenterConfiguration> loadActivated() {
        return Optional.fromNullable(findActivatedRegistryCenterConfiguration(loadGlobal()));
    }

    @Override
    public boolean add(RegistryCenterConfiguration config) {
        GlobalConfiguration configs = loadGlobal();
        boolean result = configs.getRegistryCenterConfigurations().getRegistryCenterConfigurationSet().add(config);
        if (result){
            configurationsXmlRepository.save(configs);
        }
        return result;
    }

    @Override
    public void delete(String name) {
        GlobalConfiguration configs = loadGlobal();
        RegistryCenterConfiguration toBeRemovedConfig = find(name, configs.getRegistryCenterConfigurations());
        if (null != toBeRemovedConfig){
            configs.getRegistryCenterConfigurations().getRegistryCenterConfigurationSet().remove(toBeRemovedConfig);
            configurationsXmlRepository.save(configs);
        }
    }

    private GlobalConfiguration loadGlobal() {
        GlobalConfiguration result = configurationsXmlRepository.load();
        if (null == result.getRegistryCenterConfigurations()){
            result.setRegistryCenterConfigurations(new RegistryCenterConfigurations());
        }
        return result;
    }
}
