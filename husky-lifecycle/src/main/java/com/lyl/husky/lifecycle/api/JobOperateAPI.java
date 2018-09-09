package com.lyl.husky.lifecycle.api;


import com.google.common.base.Optional;

/**
 * 操作作业的API
 */
public interface JobOperateAPI {

    /**
     * 作业立刻执行
     * 作业在不与上次运行中作用冲突的情况下才会启动 并在启动后自动清理此标志
     */
    void trigger(Optional<String> jobName, Optional<String> serverIp);

    /**
     * 作业禁用
     * 会重新分片
     */
    void disable(Optional<String> jobName, Optional<String> serverIp);

    /**
     * 作业启用
     */
    void enable(Optional<String> jobName, Optional<String> serverIp);

    /**
     * 作业关闭
     * 会重新分片
     */
    void shutdown(Optional<String> jobName, Optional<String> serverIp);

    /**
     * 作业删除
     * 会重新分片
     */
    void remove(Optional<String> jobName, Optional<String> serverIp);

}
