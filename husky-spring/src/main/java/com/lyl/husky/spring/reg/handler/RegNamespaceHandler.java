package com.lyl.husky.spring.reg.handler;

import org.springframework.beans.factory.xml.NamespaceHandlerSupport;

/**
 * 注册中心的命名空间处理器.
 */
public final class RegNamespaceHandler extends NamespaceHandlerSupport {
    @Override
    public void init() {
        registerBeanDefinitionParser("zookeeper", new ZookeeperBeanDefinitionParser());
    }
}
