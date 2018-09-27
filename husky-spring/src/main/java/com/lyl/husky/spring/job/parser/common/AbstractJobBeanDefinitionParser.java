package com.lyl.husky.spring.job.parser.common;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.lyl.husky.core.config.JobCoreConfiguration;
import com.lyl.husky.core.config.LiteJobConfiguration;
import com.lyl.husky.core.event.rdb.JobEventRdbConfiguration;
import com.lyl.husky.core.executor.handler.JobProperties;
import com.lyl.husky.spring.api.SpringJobScheduler;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.ManagedList;
import org.springframework.beans.factory.xml.AbstractBeanDefinitionParser;
import org.springframework.beans.factory.xml.ParserContext;
import org.springframework.util.xml.DomUtils;
import org.w3c.dom.Element;

import java.util.EnumMap;
import java.util.List;

/**
 * @Author lyl
 * @Description 基本作业的命名空间解析器.
 * @Date 2018/9/27 下午5:40
 */
public abstract class AbstractJobBeanDefinitionParser extends AbstractBeanDefinitionParser {


    @Override
    protected AbstractBeanDefinition parseInternal(Element element, ParserContext parserContext) {
        BeanDefinitionBuilder factory = BeanDefinitionBuilder.rootBeanDefinition(SpringJobScheduler.class);
        factory.setInitMethodName("init");
        //TODO 抽象子类
        if ("".equals(element.getAttribute(BaseJobBeanDefinitionParserTag.JOB_REF_ATTRIBUTE))){
            if ("".equals(element.getAttribute(BaseJobBeanDefinitionParserTag.CLASS_ATTRIBUTE))){
                factory.addConstructorArgValue(null);
            }else {
                factory.addConstructorArgValue(BeanDefinitionBuilder.rootBeanDefinition(element.getAttribute(BaseJobBeanDefinitionParserTag.CLASS_ATTRIBUTE)).getBeanDefinition());
            }
        }else {
            factory.addConstructorArgReference(element.getAttribute(BaseJobBeanDefinitionParserTag.JOB_REF_ATTRIBUTE));
        }
        factory.addConstructorArgReference(element.getAttribute(BaseJobBeanDefinitionParserTag.REGISTRY_CENTER_REF_ATTRIBUTE));
        factory.addConstructorArgValue(createLiteJobConfiguration(parserContext, element));
        BeanDefinition jobEventConfig = createJobEventConfig(element);
        if (null != jobEventConfig) {
            factory.addConstructorArgValue(jobEventConfig);
        }
        factory.addConstructorArgValue(createJobListeners(element));
        return factory.getBeanDefinition();
    }

    protected abstract BeanDefinition getJobTypeConfigurationBeanDefinition(ParserContext parserContext, BeanDefinition jobCoreConfigurationBeanDefinition, Element element);


    private BeanDefinition createJobEventConfig(final Element element) {
        String eventTraceDataSourceName = element.getAttribute(BaseJobBeanDefinitionParserTag.EVENT_TRACE_RDB_DATA_SOURCE_ATTRIBUTE);
        if (Strings.isNullOrEmpty(eventTraceDataSourceName)){
            return null;
        }
        BeanDefinitionBuilder factory = BeanDefinitionBuilder.rootBeanDefinition(JobEventRdbConfiguration.class);
        factory.addConstructorArgReference(eventTraceDataSourceName);
        return factory.getBeanDefinition();
    }

    private BeanDefinition createLiteJobConfiguration(ParserContext parserContext, Element element) {
        return createLiteJobConfigurationBeanDefinition(parserContext, element, createJobCoreBeanDefinition(element));
    }

    private BeanDefinition createLiteJobConfigurationBeanDefinition(final ParserContext parserContext, final Element element, final BeanDefinition jobCoreBeanDefinition) {
        BeanDefinitionBuilder result = BeanDefinitionBuilder.rootBeanDefinition(LiteJobConfiguration.class);
        result.addConstructorArgValue(getJobTypeConfigurationBeanDefinition(parserContext, jobCoreBeanDefinition, element));
        result.addConstructorArgValue(element.getAttribute(BaseJobBeanDefinitionParserTag.MONITOR_EXECUTION_ATTRIBUTE));
        result.addConstructorArgValue(element.getAttribute(BaseJobBeanDefinitionParserTag.MAX_TIME_DIFF_SECONDS_ATTRIBUTE));
        result.addConstructorArgValue(element.getAttribute(BaseJobBeanDefinitionParserTag.MONITOR_PORT_ATTRIBUTE));
        result.addConstructorArgValue(element.getAttribute(BaseJobBeanDefinitionParserTag.JOB_SHARDING_STRATEGY_CLASS_ATTRIBUTE));
        result.addConstructorArgValue(element.getAttribute(BaseJobBeanDefinitionParserTag.RECONCILE_INTERVAL_MINUTES));
        result.addConstructorArgValue(element.getAttribute(BaseJobBeanDefinitionParserTag.DISABLED_ATTRIBUTE));
        result.addConstructorArgValue(element.getAttribute(BaseJobBeanDefinitionParserTag.OVERWRITE_ATTRIBUTE));
        return result.getBeanDefinition();
    }

    private BeanDefinition createJobCoreBeanDefinition(Element element) {
        BeanDefinitionBuilder jobCoreBeanDefinitionBuilder = BeanDefinitionBuilder.rootBeanDefinition(JobCoreConfiguration.class);
        jobCoreBeanDefinitionBuilder.addConstructorArgValue(element.getAttribute(ID_ATTRIBUTE));
        jobCoreBeanDefinitionBuilder.addConstructorArgValue(element.getAttribute(BaseJobBeanDefinitionParserTag.CRON_ATTRIBUTE));
        jobCoreBeanDefinitionBuilder.addConstructorArgValue(element.getAttribute(BaseJobBeanDefinitionParserTag.SHARDING_TOTAL_COUNT_ATTRIBUTE));
        jobCoreBeanDefinitionBuilder.addConstructorArgValue(element.getAttribute(BaseJobBeanDefinitionParserTag.SHARDING_ITEM_PARAMETERS_ATTRIBUTE));
        jobCoreBeanDefinitionBuilder.addConstructorArgValue(element.getAttribute(BaseJobBeanDefinitionParserTag.JOB_PARAMETER_ATTRIBUTE));
        jobCoreBeanDefinitionBuilder.addConstructorArgValue(element.getAttribute(BaseJobBeanDefinitionParserTag.FAILOVER_ATTRIBUTE));
        jobCoreBeanDefinitionBuilder.addConstructorArgValue(element.getAttribute(BaseJobBeanDefinitionParserTag.MISFIRE_ATTRIBUTE));
        jobCoreBeanDefinitionBuilder.addConstructorArgValue(element.getAttribute(BaseJobBeanDefinitionParserTag.DESCRIPTION_ATTRIBUTE));
        jobCoreBeanDefinitionBuilder.addConstructorArgValue(createJobPropertiesBeanDefinition(element));
        return jobCoreBeanDefinitionBuilder.getBeanDefinition();
    }

    private BeanDefinition createJobPropertiesBeanDefinition(Element element) {
        BeanDefinitionBuilder result = BeanDefinitionBuilder.rootBeanDefinition(JobProperties.class);
        EnumMap<JobProperties.JobPropertiesEnum, String> map = new EnumMap<>(JobProperties.JobPropertiesEnum.class);
        map.put(JobProperties.JobPropertiesEnum.EXECUTOR_SERVICE_HANDLER, element.getAttribute(BaseJobBeanDefinitionParserTag.EXECUTOR_SERVICE_HANDLER_ATTRIBUTE));
        map.put(JobProperties.JobPropertiesEnum.JOB_EXCEPTION_HANDLER, element.getAttribute(BaseJobBeanDefinitionParserTag.JOB_EXCEPTION_HANDLER_ATTRIBUTE));
        result.addConstructorArgValue(map);
        return result.getBeanDefinition();
    }

    private List<BeanDefinition> createJobListeners(final Element element) {
        Element listenerElement = DomUtils.getChildElementByTagName(element, BaseJobBeanDefinitionParserTag.LISTENER_TAG);
        Element distributedListenerElement = DomUtils.getChildElementByTagName(element, BaseJobBeanDefinitionParserTag.DISTRIBUTED_LISTENER_TAG);
        List<BeanDefinition> result = new ManagedList<>(2);
        if (null != listenerElement){
            BeanDefinitionBuilder factory = BeanDefinitionBuilder.rootBeanDefinition(listenerElement.getAttribute(BaseJobBeanDefinitionParserTag.CLASS_ATTRIBUTE));
            factory.setScope(BeanDefinition.SCOPE_PROTOTYPE);
            result.add(factory.getBeanDefinition());
        }
        if (null != distributedListenerElement){
            BeanDefinitionBuilder factory = BeanDefinitionBuilder.rootBeanDefinition(distributedListenerElement.getAttribute(BaseJobBeanDefinitionParserTag.CLASS_ATTRIBUTE));
            factory.setScope(BeanDefinition.SCOPE_PROTOTYPE);
            factory.addConstructorArgValue(distributedListenerElement.getAttribute(BaseJobBeanDefinitionParserTag.DISTRIBUTED_LISTENER_STARTED_TIMEOUT_MILLISECONDS_ATTRIBUTE));
            factory.addConstructorArgValue(distributedListenerElement.getAttribute(BaseJobBeanDefinitionParserTag.DISTRIBUTED_LISTENER_COMPLETED_TIMEOUT_MILLISECONDS_ATTRIBUTE));
            result.add(factory.getBeanDefinition());
        }
        return result;
    }

    @Override
    protected boolean shouldGenerateId() {
        return true;
    }
}
