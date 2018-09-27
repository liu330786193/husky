package com.lyl.husky.spring.reg.handler;

import com.google.common.base.Strings;
import com.lyl.husky.core.reg.zookeeeper.ZookeeperConfiguration;
import com.lyl.husky.core.reg.zookeeeper.ZookeeperRegistryCenter;
import com.lyl.husky.spring.job.parser.common.AbstractJobBeanDefinitionParser;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.xml.ParserContext;
import org.w3c.dom.Element;

/**
 * 基于Zookeeper注册中心的命名空间解析器.
 */
public final class ZookeeperBeanDefinitionParser extends AbstractJobBeanDefinitionParser {
    @Override
    protected BeanDefinition getJobTypeConfigurationBeanDefinition(ParserContext parserContext, BeanDefinition jobCoreConfigurationBeanDefinition, Element element) {
        BeanDefinitionBuilder result = BeanDefinitionBuilder.rootBeanDefinition(ZookeeperRegistryCenter.class);
        result.addConstructorArgValue(buildZookeeperConfigurationBeanDefinition(element));
        result.setInitMethodName("init");
        return result.getBeanDefinition();
    }

    private Object buildZookeeperConfigurationBeanDefinition(Element element) {
        BeanDefinitionBuilder configuration = BeanDefinitionBuilder.rootBeanDefinition(ZookeeperConfiguration.class);
        configuration.addConstructorArgValue(element.getAttribute("server-lists"));
        configuration.addConstructorArgValue(element.getAttribute("namespace"));
        addPropertyValueIfNotEmpty("base-sleep-time-milliseconds", "baseSleepTimeMilliseconds", element, configuration);
        addPropertyValueIfNotEmpty("max-sleep-time-milliseconds", "maxSleepTimeMilliseconds", element, configuration);
        addPropertyValueIfNotEmpty("max-retries", "maxRetries", element, configuration);
        addPropertyValueIfNotEmpty("session-timeout-milliseconds", "sessionTimeoutMilliseconds", element, configuration);
        addPropertyValueIfNotEmpty("connection-timeout-milliseconds", "connectionTimeoutMilliseconds", element, configuration);
        addPropertyValueIfNotEmpty("digest", "digest", element, configuration);
        return configuration.getBeanDefinition();
    }

    private void addPropertyValueIfNotEmpty(final String attributeName, final String propertyName, final Element element, final BeanDefinitionBuilder factory) {
        String attributeValue = element.getAttribute(attributeName);
        if (!Strings.isNullOrEmpty(attributeValue)) {
            factory.addPropertyValue(propertyName, attributeValue);
        }
    }
}
