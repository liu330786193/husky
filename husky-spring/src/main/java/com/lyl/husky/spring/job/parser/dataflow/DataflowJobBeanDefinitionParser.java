package com.lyl.husky.spring.job.parser.dataflow;

import com.google.common.base.Strings;
import com.lyl.husky.core.config.dataflow.DataflowJobConfiguration;
import com.lyl.husky.spring.job.parser.common.AbstractJobBeanDefinitionParser;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.xml.ParserContext;
import org.w3c.dom.Element;

import static com.lyl.husky.spring.job.parser.common.BaseJobBeanDefinitionParserTag.CLASS_ATTRIBUTE;
import static com.lyl.husky.spring.job.parser.common.BaseJobBeanDefinitionParserTag.JOB_REF_ATTRIBUTE;

/**
 * 数据流作业的命名空间解析器.
 */
public final class DataflowJobBeanDefinitionParser extends AbstractJobBeanDefinitionParser {

    @Override
    protected BeanDefinition getJobTypeConfigurationBeanDefinition(ParserContext parserContext, BeanDefinition jobCoreConfigurationBeanDefinition, Element element) {
        BeanDefinitionBuilder result = BeanDefinitionBuilder.rootBeanDefinition(DataflowJobConfiguration.class);
        result.addConstructorArgValue(jobCoreConfigurationBeanDefinition);
        if (Strings.isNullOrEmpty(element.getAttribute(CLASS_ATTRIBUTE))) {
            result.addConstructorArgValue(parserContext.getRegistry().getBeanDefinition(element.getAttribute(JOB_REF_ATTRIBUTE)).getBeanClassName());
        } else {
            result.addConstructorArgValue(element.getAttribute(CLASS_ATTRIBUTE));
        }
        result.addConstructorArgValue(element.getAttribute(DataflowJobBeanDefinitionParserTag.STREAMING_PROCESS_ATTRIBUTE));
        return result.getBeanDefinition();
    }
}
