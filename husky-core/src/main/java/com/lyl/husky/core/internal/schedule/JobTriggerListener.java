package com.lyl.husky.core.internal.schedule;

import com.lyl.husky.core.internal.sharding.ExecutionService;
import com.lyl.husky.core.internal.sharding.ShardingService;
import lombok.RequiredArgsConstructor;
import org.quartz.Trigger;
import org.quartz.listeners.TriggerListenerSupport;

/**
 * @Author lyl
 * @Description 作业触发监听器.
 * @Date 2018/9/27 下午2:57
 */
@RequiredArgsConstructor
public class JobTriggerListener extends TriggerListenerSupport {

    private final ExecutionService executionService;
    private final ShardingService shardingService;

    @Override
    public String getName() {
        return "JobTriggerListener";
    }

    @Override
    public void triggerMisfired(final Trigger trigger) {
        if (null != trigger.getPreviousFireTime()) {
            executionService.setMisfire(shardingService.getLocalShardingItems());
        }
    }

}
