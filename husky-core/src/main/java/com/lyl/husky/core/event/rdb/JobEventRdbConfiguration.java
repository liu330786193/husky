package com.lyl.husky.core.event.rdb;

import com.lyl.husky.core.event.JobEventConfiguration;
import com.lyl.husky.core.event.JobEventListener;
import com.lyl.husky.core.event.JobEventListenerConfigurationException;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import javax.sql.DataSource;
import java.io.Serializable;
import java.sql.SQLException;

/**
 * @Author lyl
 * @Description 作业数据库事件配置.
 * @Date 2018/9/27 下午6:14
 */
@RequiredArgsConstructor
@Getter
public final class JobEventRdbConfiguration extends JobEventRdbIdentity implements JobEventConfiguration, Serializable {

    private static final long serialVersionUID = 3344410699286435226L;

    private final transient DataSource dataSource;

    @Override
    public JobEventListener createJobEventListener() throws JobEventListenerConfigurationException {
        try {
            return new JobEventRdbListener(dataSource);
        } catch (final SQLException ex) {
            throw new JobEventListenerConfigurationException(ex);
        }
    }
}
