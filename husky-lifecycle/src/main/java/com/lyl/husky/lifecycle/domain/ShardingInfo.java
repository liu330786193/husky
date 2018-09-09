package com.lyl.husky.lifecycle.domain;

import lombok.Data;

import java.io.Serializable;

/**
 * 作业分片信息对象
 */
@Data
public class ShardingInfo implements Serializable, Comparable<ShardingInfo> {

    private int item;
    private String instanceId;
    private String serverIp;
    private ShardingStatus status;
    private boolean failover;

    @Override
    public int compareTo(ShardingInfo o) {
        return getItem() - o.getItem();
    }

    /**
     * 作业分片状态
     */
    public enum ShardingStatus{
        DISABLE,
        RUNNING,
        SHARDING_FLAG,
        PENDING;

        /**
         * 获取分片状态
         */
        public static ShardingStatus getShardingStatus(final boolean isDisable, final boolean isRunning, final boolean isShardingFlag){
            if (isDisable){
                return DISABLE;
            }
            if (isRunning){
                return RUNNING;
            }
            if (isShardingFlag){
                return SHARDING_FLAG;
            }
            return PENDING;

        }

    }

}
