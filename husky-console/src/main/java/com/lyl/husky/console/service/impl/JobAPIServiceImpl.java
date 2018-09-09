package com.lyl.husky.console.service.impl;

import com.lyl.husky.console.domain.RegistryCenterConfiguration;
import com.lyl.husky.console.service.JobAPIService;
import com.lyl.husky.console.util.SessionRegistryCenterConfiguration;
import com.lyl.husky.lifecycle.api.*;

/**
 * 作业API服务实现类
 */
public final class JobAPIServiceImpl implements JobAPIService {

    @Override
    public JobSettingsAPI getJobSettingsAPI() {
        RegistryCenterConfiguration regCenterConfig = SessionRegistryCenterConfiguration.getRegistryCenterConfiguration();
        return JobAPIFactory.createJobSettingsAPI(regCenterConfig.getZkAddressList(), regCenterConfig.getNamespace(), com.google.common.base.Optional.fromNullable(regCenterConfig.getDigest()));
    }

    @Override
    public JobOperateAPI getJobOperatorAPI() {
        RegistryCenterConfiguration regCenterConfig = SessionRegistryCenterConfiguration.getRegistryCenterConfiguration();
        return JobAPIFactory.createJobOperateAPI(regCenterConfig.getZkAddressList(), regCenterConfig.getNamespace(), com.google.common.base.Optional.fromNullable(regCenterConfig.getDigest()));
    }

    @Override
    public ShardingOperateAPI getShardingOperateAPI() {
        RegistryCenterConfiguration regCenterConfig = SessionRegistryCenterConfiguration.getRegistryCenterConfiguration();
        return JobAPIFactory.createShardingOperateAPI(regCenterConfig.getZkAddressList(), regCenterConfig.getNamespace(), com.google.common.base.Optional.fromNullable(regCenterConfig.getDigest()));
    }

    @Override
    public JobStatisticsAPI getJobStatisticsAPI() {
        RegistryCenterConfiguration regCenterConfig = SessionRegistryCenterConfiguration.getRegistryCenterConfiguration();
        return JobAPIFactory.createJobStatisticsAPI(regCenterConfig.getZkAddressList(), regCenterConfig.getNamespace(), com.google.common.base.Optional.fromNullable(regCenterConfig.getDigest()));
    }

    @Override
    public ServerStatisticsAPI getServerStatisticsAPI() {
        RegistryCenterConfiguration regCenterConfig = SessionRegistryCenterConfiguration.getRegistryCenterConfiguration();
        return JobAPIFactory.createServerStatisticsAPI(regCenterConfig.getZkAddressList(), regCenterConfig.getNamespace(), com.google.common.base.Optional.fromNullable(regCenterConfig.getDigest()));
    }

    @Override
    public ShardingStatisticsAPI getShardingStatisticsAPI() {
        RegistryCenterConfiguration regCenterConfig = SessionRegistryCenterConfiguration.getRegistryCenterConfiguration();
        return JobAPIFactory.createShardingStatisticsAPI(regCenterConfig.getZkAddressList(), regCenterConfig.getNamespace(), com.google.common.base.Optional.fromNullable(regCenterConfig.getDigest()));
    }

}
