package com.lyl.husky.core.exception;

/**
 * 作业执行环境异常
 */
public final class JobExecutionEnvironmentException extends Exception {
    public JobExecutionEnvironmentException(final String errorMessage, final Object... args){
        super(String.format(errorMessage, args));
    }
}
