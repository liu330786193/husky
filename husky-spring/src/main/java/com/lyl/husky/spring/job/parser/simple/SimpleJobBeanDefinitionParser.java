package com.lyl.husky.spring.job.parser.simple;

import com.google.common.base.Strings;
import com.lyl.husky.core.config.simple.SimpleJobConfiguration;
import com.lyl.husky.spring.job.parser.common.AbstractJobBeanDefinitionParser;
import com.lyl.husky.spring.job.parser.common.BaseJobBeanDefinitionParserTag;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.xml.ParserContext;
import org.w3c.dom.Element;

/**
 * @Author lyl
 * @Description 简单作业的命名空间解析器.
 * @Date 2018/9/27 下午5:38
 */
public final class SimpleJobBeanDefinitionParser extends AbstractJobBeanDefinitionParser {

    @Override
    protected BeanDefinition getJobTypeConfigurationBeanDefinition(ParserContext parserContext, BeanDefinition jobCoreConfigurationBeanDefinition, Element element) {
        BeanDefinitionBuilder result = BeanDefinitionBuilder.rootBeanDefinition(SimpleJobConfiguration.class);
        result.addConstructorArgValue(jobCoreConfigurationBeanDefinition);
        if (Strings.isNullOrEmpty(element.getAttribute(BaseJobBeanDefinitionParserTag.CLASS_ATTRIBUTE))){
            result.addConstructorArgValue(parserContext.getRegistry().getBeanDefinition(element.getAttribute(BaseJobBeanDefinitionParserTag.JOB_REF_ATTRIBUTE)).getBeanClassName());

        } else {
            result.addConstructorArgValue(element.getAttribute(BaseJobBeanDefinitionParserTag.CLASS_ATTRIBUTE));
        }
        return result.getBeanDefinition();
    }

}
