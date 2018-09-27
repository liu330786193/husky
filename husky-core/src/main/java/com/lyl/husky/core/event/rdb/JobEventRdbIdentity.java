package com.lyl.husky.core.event.rdb;

import com.lyl.husky.core.event.JobEventIdentity;

/**
 * @Author lyl
 * @Description 关系型数据库作业事件标识.
 * @Date 2018/9/27 下午6:15
 */
public class JobEventRdbIdentity implements JobEventIdentity {

    @Override
    public String getIdentity() {
        return "rdb";
    }
}
