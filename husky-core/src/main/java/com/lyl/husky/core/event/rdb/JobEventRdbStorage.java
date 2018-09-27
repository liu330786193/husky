package com.lyl.husky.core.event.rdb;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;

/**
 * @Author lyl
 * @Description 运行痕迹事件数据库存储.
 * @Date 2018/9/27 下午6:17
 */
public final class JobEventRdbStorage {

    private static final String TABLE_JOB_EXECUTION_LOG = "JOB_EXECUTION_LOG";
    private static final String TABLE_JOB_STATUS_TRACE_LOG = "JOB_STATUS_TRACE_LOG";
    private static final String TASK_ID_STATE_INDEX = "TASK_ID_STATE_INDEX";

    private final DataSource dataSource;
    private DatabaseType databaseType;

    JobEventRdbStorage(final DataSource dataSource) throws SQLException {
        this.dataSource = dataSource;
        initTablesAndIndexes();
    }

    private void initTablesAndIndexes() throws SQLException {
        try(Connection conn = dataSource.getConnection()) {
            createJobExecutionTableAndIndexIfNeeded(conn);
            createJobStatusTraceTableAndIndexIfNeeded(conn);
            databaseType = DatabaseType.valueFrom(conn.getMetaData().getDatabaseProductName());
        }
    }

    private void createJobExecutionTableAndIndexIfNeeded(Connection conn) {
        DatabaseMetaData dbMetaData = conn.getMetaData();
    }

    private void createJobStatusTraceTableAndIndexIfNeeded(Connection conn) {
    }


}
