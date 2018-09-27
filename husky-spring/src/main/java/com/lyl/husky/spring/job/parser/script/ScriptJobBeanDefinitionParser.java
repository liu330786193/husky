package com.lyl.husky.spring.job.parser.script;

import com.lyl.husky.core.config.script.ScriptJobConfiguration;
import com.lyl.husky.spring.job.parser.common.AbstractJobBeanDefinitionParser;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.xml.ParserContext;
import org.w3c.dom.Element;

/**
 * 脚本作业的命名空间解析器.
 */
public final class ScriptJobBeanDefinitionParser extends AbstractJobBeanDefinitionParser {

    @Override
    protected BeanDefinition getJobTypeConfigurationBeanDefinition(ParserContext parserContext, BeanDefinition jobCoreConfigurationBeanDefinition, Element element) {
        BeanDefinitionBuilder result = BeanDefinitionBuilder.rootBeanDefinition(ScriptJobConfiguration.class);
        result.addConstructorArgValue(jobCoreConfigurationBeanDefinition);
        result.addConstructorArgValue(element.getAttribute(ScriptJobBeanDefinitionParserTag.SCRIPT_COMMAND_LINE_ATTRIBUTE));
        return result.getBeanDefinition();
    }
}
