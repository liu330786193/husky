package com.lyl.husky.console.domain;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;

@NoArgsConstructor
@Data
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public final class EventTraceDataSourceConfiguration implements Serializable {

    @XmlAttribute(required = true)
    private String name;

    @XmlAttribute(required = true)
    private String driver;

    @XmlAttribute
    private String url;

    @XmlAttribute
    private String username;

    @XmlAttribute
    private String password;

    @XmlAttribute
    private boolean activated;

    public EventTraceDataSourceConfiguration(final String driver, final String url, final String username) {
        this.driver = driver;
        this.url = url;
        this.username = username;
    }

}
