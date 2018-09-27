package com.lyl.husky.spring.job.handler;

import org.springframework.beans.factory.xml.NamespaceHandlerSupport;

/**
 * @Author lyl
 * @Description 分布式作业的命名空间处理器.
 * @Date 2018/9/27 下午5:34
 */
public final class JobNamespaceHandler extends NamespaceHandlerSupport {
    @Override
    public void init() {
        registerBeanDefinitionParser("simple", new SimpleJobBeanDefinitionParser);
    }
}
