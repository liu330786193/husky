package com.lyl.husky.core.api;

import com.lyl.husky.core.config.LiteJobConfiguration;
import com.lyl.husky.core.reg.base.CoordinatorRegistryCenter;
import jdk.nashorn.internal.objects.annotations.Getter;

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
    private final ScheduleFaca
}
