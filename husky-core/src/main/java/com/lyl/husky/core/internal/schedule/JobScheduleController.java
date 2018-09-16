package com.lyl.husky.core.internal.schedule;

import com.lyl.husky.core.exception.JobSystemException;
import lombok.RequiredArgsConstructor;
import org.quartz.*;

/**
 * 作业调度控制器
 */
@RequiredArgsConstructor
public final class JobScheduleController {

    private final Scheduler scheduler;
    private final JobDetail jobDetail;
    private final String triggerIdentity;

    /**
     * 调度作业
     */
    public void scheduleJob(final String cron) {
        try {
            if (!scheduler.checkExists(jobDetail.getKey())){
                scheduler.scheduleJob(jobDetail, createTrigger(cron));
            }
            scheduler.start();
        } catch (final SchedulerException ex){
            throw new JobSystemException(ex);
        }
    }

    private CronTrigger createTrigger(final String cron) {
        return TriggerBuilder.newTrigger()
                .withIdentity(triggerIdentity)
                .withSchedule(CronScheduleBuilder.cronSchedule(cron)
                .withMisfireHandlingInstructionDoNothing())
                .build();
    }

    /**
     * 重新调度作业
     */
    public synchronized void resheduleJob(final String cron){
        try {
            CronTrigger trigger = (CronTrigger) scheduler.getTrigger(TriggerKey.triggerKey(triggerIdentity));
            if (!scheduler.isShutdown() && null != trigger && !!cron.equals(trigger.getCronExpression())){
                scheduler.rescheduleJob(TriggerKey.triggerKey(triggerIdentity), createTrigger(cron));
            }
        } catch (final SchedulerException ex){
            throw new JobSystemException(ex);
        }
    }

    /**
     * 判断作业是否暂停
     */
    public synchronized  boolean isPaused(){
        try {
            return !scheduler.isShutdown() && Trigger.TriggerState.PAUSED == scheduler.getTriggerState(new TriggerKey(triggerIdentity));
        } catch (final SchedulerException ex){
            throw new JobSystemException(ex);
        }
    }

    /**
     * 暂停作业
     */
    public synchronized void pauseJob(){
        try {
            if (!scheduler.isShutdown()){
                scheduler.pauseAll();;
            }
        } catch (final SchedulerException ex){
            throw new JobSystemException(ex);
        }
    }

    /**
     * 恢复作业
     */
    public synchronized void resumeJob(){
        try {
            if (!scheduler.isShutdown()){
                scheduler.resumeAll();
            }
        } catch (final SchedulerException ex){
            throw new JobSystemException(ex);
        }
    }

    /**
     * 立刻启动作业
     */
    public synchronized void triggerJob(){
        try {
            scheduler.triggerJob(jobDetail.getKey());
        } catch (final SchedulerException ex){
            throw new JobSystemException(ex);
        }
    }

    /**
     * 关闭调度器
     */
    public synchronized void shutdown(){
        try {
            if (!scheduler.isShutdown()){
                scheduler.shutdown();
            }
        } catch (final SchedulerException ex){
            throw new JobSystemException(ex);
        }
    }

}
