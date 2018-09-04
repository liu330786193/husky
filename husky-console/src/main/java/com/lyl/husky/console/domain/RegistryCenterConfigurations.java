package com.lyl.husky.console.domain;

import lombok.Getter;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.LinkedHashSet;
import java.util.Set;

@Getter
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class RegistryCenterConfigurations {

    private Set<RegistryCenterConfiguration> registryCenterConfigurationSet = new LinkedHashSet<>();

}
