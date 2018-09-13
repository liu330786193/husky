package com.lyl.husky.core.executor.type;

import com.google.common.base.Strings;
import com.lyl.husky.core.api.ShardingContext;
import com.lyl.husky.core.config.script.ScriptJobConfiguration;
import com.lyl.husky.core.exception.JobConfigurationException;
import com.lyl.husky.core.executor.AbstractElasticJobExecutor;
import com.lyl.husky.core.executor.JobFacade;
import com.lyl.husky.core.util.json.GsonFactory;
import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.DefaultExecutor;

import java.io.IOException;

/**
 * 脚本作业执行器
 */
public final class ScriptJobExecutor extends AbstractElasticJobExecutor {

    public ScriptJobExecutor(final JobFacade jobFacade){
        super(jobFacade);
    }

    @Override
    protected void process(final ShardingContext shardingContext) {
        final String scriptCommandLine = ((ScriptJobConfiguration)getJobRootConfig().getTypeConfig()).getScriptCommandLine();
        if (Strings.isNullOrEmpty(scriptCommandLine)){
            throw new JobConfigurationException("Cannot find script command line for job '%s', job is not executed.", shardingContext.getJobName());
    }
    }

    private void executeScript(final ShardingContext shardingContext, final String scriptCommandLine){
        CommandLine commandLine = CommandLine.parse(scriptCommandLine);
        commandLine.addArgument(GsonFactory.getGson().toJson(shardingContext));
        try {
            new DefaultExecutor().execute(commandLine);
        } catch (final IOException ex){
            throw new JobConfigurationException("Execute script failure.", ex);
        }
    }
}
