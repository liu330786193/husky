package com.lyl.husky.core.util.env;

import java.io.IOException;

/**
 * 网络主机异常
 */
public final class HostException extends RuntimeException {

    public HostException(final IOException cause){
        super(cause);
    }

}
