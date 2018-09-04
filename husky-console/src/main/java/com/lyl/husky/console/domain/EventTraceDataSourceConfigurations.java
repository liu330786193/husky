package com.lyl.husky.console.domain;

import lombok.Getter;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * 时间追踪数据源配置根对象
 */
@Getter
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public final class EventTraceDataSourceConfigurations {
    private Set<EventTraceDataSourceConfiguration> eventTraceDataSourceConfigurationSet = new LinkedHashSet<>();
}
