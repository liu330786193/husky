package com.lyl.husky.core.event.rdb;

import com.lyl.husky.core.event.type.JobExecutionEvent;
import com.lyl.husky.core.event.type.JobStatusTraceEvent;

import javax.sql.DataSource;
import java.sql.SQLException;

/**
 * @Author lyl
 * @Description 运行痕迹事件数据库监听器.
 * @Date 2018/9/27 下午6:16
 */
public final class JobEventRdbListener extends JobEventRdbConfiguration{
    private final JobEventRdbStorage repository;

    public JobEventRdbListener(final DataSource dataSource) throws SQLException {
        repository = new JobEventRdbStorage(dataSource);
    }

    @Override
    public void listen(final JobExecutionEvent executionEvent) {
        repository.addJobExecutionEvent(executionEvent);
    }

    @Override
    public void listen(final JobStatusTraceEvent jobStatusTraceEvent) {
        repository.addJobStatusTraceEvent(jobStatusTraceEvent);
    }
}
