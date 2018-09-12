package com.lyl.husky.core.executor.handler;

import java.util.concurrent.ExecutorService;

/**
 * 线程池服务处理器
 * 用于作业内部的线程池处理使用
 */
public interface ExecutorServiceHandler {

    /**
     * 创建线程池服务使用
     */
    ExecutorService createExecutorService(final String jobName);

}
