package com.lyl.husky.core.event.type;


import com.lyl.husky.core.context.ExecutionType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.UUID;

@Getter
@AllArgsConstructor
@RequiredArgsConstructor
public class JobStatusTraceEvent {

    private String id = UUID.randomUUID().toString();
    private final String jobName;
    @Setter
    private String originalTaskId = "";
    private final String taskId;
    private final String slaveId;
    private final Source source;
    private final ExecutionType executionType;
    private final String shardingItems;
    private final State state;
    private final String message;
    private Date creation = new Date();

    public enum State {
        TASK_STAGING,
        TASK_RUNNING,
        TASK_FINISHED,
        TASK_KILL,
        TASK_LOST,
        TASK_FAILED,
        TASK_ERROR,
        TASK_DROPPED,
        TASK_GONE,
        TASK_GONE_BY_OPERATOR,
        TASK_UNREACHABLE,
        TASK_UNKNOWN
    }

    public enum Source {
        CLOUD_SCHEDULER,
        CLOUD_EXECUTOR,
        LITE_EXECUTOR
    }

}
