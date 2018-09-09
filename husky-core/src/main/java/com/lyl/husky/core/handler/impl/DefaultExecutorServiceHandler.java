package com.lyl.husky.core.handler.impl;


import com.lyl.husky.core.handler.ExecutorServiceHandler;
import com.lyl.husky.core.util.concurrent.ExecutorServiceObject;

import java.util.concurrent.ExecutorService;

/**
 * 默认线程池服务处理器.
 */
public final class DefaultExecutorServiceHandler implements ExecutorServiceHandler {
    @Override
    public ExecutorService createExecutorService(String jobName) {
        return new ExecutorServiceObject("inner-job-" + jobName, Runtime.getRuntime().availableProcessors() * 2).createExecutorService();
    }
}
