package com.lyl.husky.core.api;

import com.google.common.base.Optional;
import com.lyl.husky.core.api.listener.AbstractDistributeOnceElasticJobListener;
import com.lyl.husky.core.api.listener.ElasticJobListener;
import com.lyl.husky.core.api.script.ScriptJob;
import com.lyl.husky.core.api.strategy.JobInstance;
import com.lyl.husky.core.config.LiteJobConfiguration;
import com.lyl.husky.core.event.JobEventBus;
import com.lyl.husky.core.event.JobEventConfiguration;
import com.lyl.husky.core.exception.JobConfigurationException;
import com.lyl.husky.core.exception.JobSystemException;
import com.lyl.husky.core.executor.JobFacade;
import com.lyl.husky.core.internal.guarantee.GuaranteeService;
import com.lyl.husky.core.internal.schedule.*;
import com.lyl.husky.core.reg.base.CoordinatorRegistryCenter;
import lombok.Getter;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.impl.StdSchedulerFactory;

import java.util.Arrays;
import java.util.List;
import java.util.Properties;

/**
 * 作业调度器
 */
public class JobScheduler {

    public static final String ELASTIC_JOB_DATA_MAP_KEY = "elasticJob";
    private static final String JOB_FACADE_DATA_MAP_KEY = "jobFacade";
    private final LiteJobConfiguration liteJobConfig;
    private final CoordinatorRegistryCenter regCenter;
    //TODO 为测试使用 测试用例不能反复new monitor service 以后需要把MonitorService重构为单例
    @Getter
    private final SchedulerFacade schedulerFacade;
    private final JobFacade jobFacade;

    public JobScheduler(final CoordinatorRegistryCenter regCenter, final LiteJobConfiguration liteJobConfig, final ElasticJobListener... elasticJobListeners) {
        this(regCenter, liteJobConfig, new JobEventBus(), elasticJobListeners);
    }

    public JobScheduler(final CoordinatorRegistryCenter regCenter, final LiteJobConfiguration liteJobConfig, final JobEventConfiguration jobEventConfig,
                        final ElasticJobListener... elasticJobListeners) {
        this(regCenter, liteJobConfig, new JobEventBus(jobEventConfig), elasticJobListeners);
    }

    private JobScheduler(final CoordinatorRegistryCenter regCenter, final LiteJobConfiguration liteJobConfig, final JobEventBus jobEventBus, final ElasticJobListener... elasticJobListeners) {
        JobRegistry.getInstance().addJobInstance(liteJobConfig.getJobName(), new JobInstance());
        this.liteJobConfig = liteJobConfig;
        this.regCenter = regCenter;
        List<ElasticJobListener> elasticJobListenerList = Arrays.asList(elasticJobListeners);
        setGuaranteeServiceForElasticJobListeners(regCenter, elasticJobListenerList);
        schedulerFacade = new SchedulerFacade(regCenter, liteJobConfig.getJobName(), elasticJobListenerList);
        jobFacade = new LiteJobFacade(regCenter, liteJobConfig.getJobName(), Arrays.asList(elasticJobListeners), jobEventBus);
    }

    private void setGuaranteeServiceForElasticJobListeners(final CoordinatorRegistryCenter regCenter, final List<ElasticJobListener> elasticJobListeners) {
        GuaranteeService guaranteeService = new GuaranteeService(regCenter, liteJobConfig.getJobName());
        for (ElasticJobListener each : elasticJobListeners) {
            if (each instanceof AbstractDistributeOnceElasticJobListener) {
                ((AbstractDistributeOnceElasticJobListener) each).setGuaranteeService(guaranteeService);
            }
        }
    }

    /**
     * 初始化作业.
     */
    public void init() {
        LiteJobConfiguration liteJobConfigFromRegCenter = schedulerFacade.updateJobConfiguration(liteJobConfig);
        JobRegistry.getInstance().setCurrentShardingTotalCount(liteJobConfigFromRegCenter.getJobName(), liteJobConfigFromRegCenter.getTypeConfig().getCoreConfig().getShardingTotalCount());
        JobScheduleController jobScheduleController = new JobScheduleController(
                createScheduler(), createJobDetail(liteJobConfigFromRegCenter.getTypeConfig().getJobClass()), liteJobConfigFromRegCenter.getJobName());
        JobRegistry.getInstance().registerJob(liteJobConfigFromRegCenter.getJobName(), jobScheduleController, regCenter);
        schedulerFacade.registerStartUpInfo(!liteJobConfigFromRegCenter.isDisabled());
        jobScheduleController.scheduleJob(liteJobConfigFromRegCenter.getTypeConfig().getCoreConfig().getCron());
    }

    private JobDetail createJobDetail(final String jobClass) {
        JobDetail result = JobBuilder.newJob(LiteJob.class).withIdentity(liteJobConfig.getJobName()).build();
        result.getJobDataMap().put(JOB_FACADE_DATA_MAP_KEY, jobFacade);
        Optional<ElasticJob> elasticJobInstance = createElasticJobInstance();
        if (elasticJobInstance.isPresent()) {
            result.getJobDataMap().put(ELASTIC_JOB_DATA_MAP_KEY, elasticJobInstance.get());
        } else if (!jobClass.equals(ScriptJob.class.getCanonicalName())) {
            try {
                result.getJobDataMap().put(ELASTIC_JOB_DATA_MAP_KEY, Class.forName(jobClass).newInstance());
            } catch (final ReflectiveOperationException ex) {
                throw new JobConfigurationException("Elastic-Job: Job class '%s' can not initialize.", jobClass);
            }
        }
        return result;
    }

    protected Optional<ElasticJob> createElasticJobInstance() {
        return Optional.absent();
    }

    private Scheduler createScheduler() {
        Scheduler result;
        try {
            StdSchedulerFactory factory = new StdSchedulerFactory();
            factory.initialize(getBaseQuartzProperties());
            result = factory.getScheduler();
            result.getListenerManager().addTriggerListener(schedulerFacade.newJobTriggerListener());
        } catch (final SchedulerException ex) {
            throw new JobSystemException(ex);
        }
        return result;
    }

    private Properties getBaseQuartzProperties() {
        Properties result = new Properties();
        result.put("org.quartz.threadPool.class", org.quartz.simpl.SimpleThreadPool.class.getName());
        result.put("org.quartz.threadPool.threadCount", "1");
        result.put("org.quartz.scheduler.instanceName", liteJobConfig.getJobName());
        result.put("org.quartz.jobStore.misfireThreshold", "1");
        result.put("org.quartz.plugin.shutdownhook.class", JobShutdownHookPlugin.class.getName());
        result.put("org.quartz.plugin.shutdownhook.cleanShutdown", Boolean.TRUE.toString());
        return result;
    }

}
