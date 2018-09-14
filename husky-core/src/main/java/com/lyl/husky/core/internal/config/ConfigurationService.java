package com.lyl.husky.core.internal.config;

import com.google.common.base.Optional;
import com.lyl.husky.core.config.LiteJobConfiguration;
import com.lyl.husky.core.exception.JobConfigurationException;
import com.lyl.husky.core.exception.JobExecutionEnvironmentException;
import com.lyl.husky.core.internal.storage.JobNodeStorage;
import com.lyl.husky.core.reg.base.CoordinatorRegistryCenter;
import com.lyl.husky.core.util.env.TimeService;

/**
 * 弹性化分布式作业配置服务
 */
public final class ConfigurationService {

    private final TimeService timeService;
    private final JobNodeStorage jobNodeStorage;

    public ConfigurationService(final CoordinatorRegistryCenter regCenter, final String jobName){
        jobNodeStorage = new JobNodeStorage(regCenter, jobName);
        timeService = new TimeService();
    }

    /**
     * 读取作业配置
     */
    public LiteJobConfiguration load(final boolean fromCache){
        String result;
        if (fromCache){
            result = jobNodeStorage.getJobNodeData(ConfigurationNode.ROOT);
            if (null == result){
                result = jobNodeStorage.getJobNodeDataDirectly(ConfigurationNode.ROOT);
            }
        } else {
            result = jobNodeStorage.getJobNodeDataDirectly(ConfigurationNode.ROOT);
        }
        return LiteJobConfigurationGsonFactory.fromJson(result);
    }

    /**
     * 持久化分布式作业配置信息
     */
    public void persist(final LiteJobConfiguration liteJobConfig){
        checkConflictJob(liteJobConfig);
        if (!jobNodeStorage.isJobNodeExisted(ConfigurationNode.ROOT) || liteJobConfig.isOverwrite()){
            jobNodeStorage.replaceJobNode(ConfigurationNode.ROOT, LiteJobConfigurationGsonFactory.toJson(liteJobConfig));
        }
    }

    private void checkConflictJob(final LiteJobConfiguration liteJobConfig) {
        Optional<LiteJobConfiguration> liteJobConfigFromZk = find();
        if (liteJobConfigFromZk.isPresent() && !liteJobConfigFromZk.get().getTypeConfig().getJobClass().equals(liteJobConfig.getTypeConfig().getJobClass())) {
            throw new JobConfigurationException("Job conflict with register center. The job '%s' in register center's class is '%s', your job class is '%s'",
                    liteJobConfig.getJobName(), liteJobConfigFromZk.get().getTypeConfig().getJobClass(), liteJobConfig.getTypeConfig().getJobClass());
        }
    }

    private Optional<LiteJobConfiguration> find() {
        if (!jobNodeStorage.isJobNodeExisted(ConfigurationNode.ROOT)){
            return Optional.absent();
        }
        LiteJobConfiguration result = LiteJobConfigurationGsonFactory.fromJson(jobNodeStorage.getJobNodeDataDirectly(ConfigurationNode.ROOT));
        if (null == result){
            //TODO 应该删除整个job node 并非仅仅删除config node
            jobNodeStorage.removeJobNodeIfExisted(ConfigurationNode.ROOT);
        }
        return Optional.fromNullable(result);
    }

    /**
     * 检查本机与注册中心的时间误差秒数是否在允许范围
     */
    public void checkMaxTimeDiffSecondsTolerable() throws JobExecutionEnvironmentException {
        int maxTimeDiffSeconds = load(true).getMaxTimeDiffSeconds();
        if (-1 == maxTimeDiffSeconds){
            return;
        }
        long timeDiff = Math.abs(timeService.getCurrentmills() - jobNodeStorage.getRegistryCenterTime());
        if (timeDiff > maxTimeDiffSeconds * 1000L) {
            throw new JobExecutionEnvironmentException(
                    "Time different between job server and register center exceed '%s' seconds, max time different is '%s' seconds.", timeDiff / 1000, maxTimeDiffSeconds);
        }
    }

}
