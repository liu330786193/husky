package com.lyl.husky.core.exception;

/**
 * 作业配置异常
 */
public final class JobConfigurationException extends RuntimeException {

    public JobConfigurationException(final String errorMessage, final Object... args){
        super(String.format(errorMessage, args));
    }

    public JobConfigurationException(final Throwable cause){
        super(cause);
    }
}
