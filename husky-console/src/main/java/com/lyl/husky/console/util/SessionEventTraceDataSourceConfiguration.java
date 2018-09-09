package com.lyl.husky.console.util;

import com.lyl.husky.console.domain.EventTraceDataSourceConfiguration;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * 事件追踪数据源配置的会话声明周期
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class SessionEventTraceDataSourceConfiguration {

    private static EventTraceDataSourceConfiguration eventTraceDataSourceConfiguration;

    /**
     * 从当前会话范围获取事件追踪数据源配置
     */
    public static EventTraceDataSourceConfiguration getEventTraceDataSourceConfiguration(){
        return eventTraceDataSourceConfiguration;
    }

    /**
     * 设置事件追踪数据源配置至当前会话范围
     */
    public static void setDataSourceConfiguration(final EventTraceDataSourceConfiguration eventTraceDataSourceConfiguration){
        SessionEventTraceDataSourceConfiguration.eventTraceDataSourceConfiguration = eventTraceDataSourceConfiguration;
    }

}
