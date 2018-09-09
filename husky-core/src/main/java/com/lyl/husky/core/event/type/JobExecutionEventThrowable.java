package com.lyl.husky.core.event.type;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

/**
 * 作业执行事件Throwable
 */
@RequiredArgsConstructor
@AllArgsConstructor
@Getter
@ToString(of = "plainText")
public final class JobExecutionEventThrowable {

    private final Throwable throwable;
    private String plainText;

}
