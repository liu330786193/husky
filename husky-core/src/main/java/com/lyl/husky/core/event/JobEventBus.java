package com.lyl.husky.core.event;

import com.google.common.eventbus.AsyncEventBus;
import com.google.common.eventbus.EventBus;
import com.lyl.husky.core.util.concurrent.ExecutorServiceObject;
import lombok.extern.slf4j.Slf4j;

/**
 * @Author lyl
 * @Description 运行痕迹事件总线.
 * @Date 2018/9/27 下午3:10
 */
@Slf4j
public final class JobEventBus {

    private final JobEventConfiguration jobEventConfig;

    private final ExecutorServiceObject executorServiceObject;

    private final EventBus eventBus;

    private boolean isRegistered;

    public JobEventBus() {
        jobEventConfig = null;
        executorServiceObject = null;
        eventBus = null;
    }

    public JobEventBus(final JobEventConfiguration jobEventConfig) {
        this.jobEventConfig = jobEventConfig;
        executorServiceObject = new ExecutorServiceObject("job-event", Runtime.getRuntime().availableProcessors() * 2);
        eventBus = new AsyncEventBus(executorServiceObject.createExecutorService());
        register();
    }

    private void register() {
        try {
            eventBus.register(jobEventConfig.createJobEventListener());
            isRegistered = true;
        } catch (final JobEventListenerConfigurationException ex) {
            log.error("Elastic job: create JobEventListener failure, error is: ", ex);
        }
    }

    /**
     * 发布事件.
     *
     * @param event 作业事件
     */
    public void post(final JobEvent event) {
        if (isRegistered && !executorServiceObject.isShutdown()) {
            eventBus.post(event);
        }
    }

}
