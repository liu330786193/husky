package com.lyl.husky.console.domain;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;

@Data
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
@NoArgsConstructor
public final class RegistryCenterConfiguration implements Serializable {

    @XmlAttribute
    private String name;

    @XmlAttribute
    private String zkAddressList;

    @XmlAttribute
    private String namespace;

    @XmlAttribute
    private String digest;

    @XmlAttribute
    private boolean activated;

}
