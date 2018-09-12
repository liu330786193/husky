package com.lyl.husky.core.executor.handler;

import java.util.concurrent.ExecutorService;

/**
 * 作业异常处理器
 */
public interface JobExceptionHandler {

    /**
     * 处理异常处理器
     */
    ExecutorService handlerException(String jobName, Throwable cause);

}
