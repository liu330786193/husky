package com.lyl.husky.spring.job.handler;

import com.lyl.husky.spring.job.parser.dataflow.DataflowJobBeanDefinitionParser;
import com.lyl.husky.spring.job.parser.script.ScriptJobBeanDefinitionParser;
import com.lyl.husky.spring.job.parser.simple.SimpleJobBeanDefinitionParser;
import org.springframework.beans.factory.xml.NamespaceHandlerSupport;

/**
 * @Author lyl
 * @Description 分布式作业的命名空间处理器.
 * @Date 2018/9/27 下午5:34
 */
public final class JobNamespaceHandler extends NamespaceHandlerSupport {
    @Override
    public void init() {
        registerBeanDefinitionParser("simple", new SimpleJobBeanDefinitionParser());
        registerBeanDefinitionParser("dataflow", new DataflowJobBeanDefinitionParser());
        registerBeanDefinitionParser("script", new ScriptJobBeanDefinitionParser());
    }
}
