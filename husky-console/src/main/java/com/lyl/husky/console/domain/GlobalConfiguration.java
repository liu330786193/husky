package com.lyl.husky.console.domain;

import lombok.Data;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * 全局配置
 */
@Data
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public final class GlobalConfiguration {

    private RegistryCenterConfigurations registryCenterConfigurations;

    private EventTraceDataSourceConfigurations eventTraceDataSourceConfigurations;

}
