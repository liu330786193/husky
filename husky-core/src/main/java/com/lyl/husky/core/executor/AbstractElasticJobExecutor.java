package com.lyl.husky.core.executor;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.terracotta.quartz.wrappers.JobFacade;

/**
 * 弹性化分布式作业执行器
 */
@Slf4j
public abstract class AbstractElasticJobExecutor {

    @Getter(AccessLevel.PROTECTED)
    private final JobFacade

}
