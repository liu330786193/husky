package com.lyl.husky.core.exception;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * 异常处理工具类
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ExceptionUtil {

    /**
     * 将Throwable异常转换为字符串
     */
    public static String transform(final Throwable cause){
        if (null == cause){
            return "";
        }
        StringWriter result = new StringWriter();
        try (PrintWriter writer = new PrintWriter(result)) {
            cause.printStackTrace(writer);
        }
        return result.toString();
    }

}
