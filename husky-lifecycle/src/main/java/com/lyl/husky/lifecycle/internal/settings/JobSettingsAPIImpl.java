package com.lyl.husky.lifecycle.internal.settings;

import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import com.lyl.husky.core.internal.config.LiteJobConfigurationGsonFactory;
import com.lyl.husky.core.internal.storage.JobNodePath;
import com.lyl.husky.core.reg.base.CoordinatorRegistryCenter;
import com.lyl.husky.lifecycle.api.JobSettingsAPI;
import com.lyl.husky.lifecycle.domain.JobSetting;
import lombok.RequiredArgsConstructor;

/**
 * 作业配置的实现类
 */
@RequiredArgsConstructor
public final class JobSettingsAPIImpl implements JobSettingsAPI {

    private final CoordinatorRegistryCenter regCenter;

    @Override
    public JobSetting getJobSettings(String jobName) {
        return null;
    }

    @Override
    public void updateJobSetting(JobSetting jobSettings) {
        Preconditions.checkArgument(!Strings.isNullOrEmpty(jobSettings.getJobName()), "jobName can not be empty.");
        Preconditions.checkArgument(!Strings.isNullOrEmpty(jobSettings.getCron()), "cron can not be empty.");
        Preconditions.checkArgument(jobSettings.getShardingTotalCount() > 0, "jobName can not be empty.");
        JobNodePath jobNodePath = new JobNodePath(jobSettings.getJobName());
        regCenter.update(jobNodePath.getConfigNodePath(), LiteJobConfigurationGsonFactory.toJsonForObject(jobSettings));
    }

    @Override
    public void removeJobSetting(String jobName) {
        regCenter.remove("/" + jobName);
    }
}
