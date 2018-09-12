package com.lyl.husky.core.executor.handler.impl;

import com.lyl.husky.core.executor.handler.JobExceptionHandler;
import com.lyl.husky.core.util.concurrent.ExecutorServiceObject;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ExecutorService;

/**
 * 默认作业异常处理器
 */
@Slf4j
public final class DefaultJobExceptionHandler implements JobExceptionHandler {
    @Override
    public ExecutorService handlerException(String jobName, Throwable cause) {
        return new ExecutorServiceObject("inner-job-" + jobName, Runtime.getRuntime().availableProcessors() * 2).createExecutorService();

    }
}
