package com.lyl.husky.core.internal.schedule;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.Map;

/**
 * 作业注册表
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class JobRegistry {

    private static volatile JobRegistry instance;
    private Map<String, job>

}
