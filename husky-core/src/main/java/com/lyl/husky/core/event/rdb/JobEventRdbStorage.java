package com.lyl.husky.core.event.rdb;

import javax.sql.DataSource;
import java.sql.*;

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

    private void createJobExecutionTableAndIndexIfNeeded(final Connection conn) throws SQLException {
        DatabaseMetaData dbMetaData = conn.getMetaData();
        try (ResultSet resultSet = dbMetaData.getTables(null, null, TABLE_JOB_EXECUTION_LOG, new String[]{"TABLE"})){
            if (!resultSet.next()){
                createJobExecutionTable(conn);
            }
        }
    }

    private void createJobStatusTraceTableAndIndexIfNeeded(final Connection conn) throws SQLException {
        DatabaseMetaData dbMetaData = conn.getMetaData();
        try (ResultSet resultSet = dbMetaData.getTables(null, null, TABLE_JOB_STATUS_TRACE_LOG, new String[]{"TABLE"})) {
            if (!resultSet.next()) {
                createJobStatusTraceTable(conn);
            }
        }
        createTaskIdIndexIfNeeded(conn, TABLE_JOB_STATUS_TRACE_LOG, TASK_ID_STATE_INDEX);
    }

    private void createTaskIdIndexIfNeeded(Connection conn, String tableJobStatusTraceLog, String taskIdStateIndex) {
    }

    private void createJobExecutionTable(Connection conn) throws SQLException {
        String dbSchema = "CREATE TABLE `" + TABLE_JOB_EXECUTION_LOG + "` ("
                + "`id` VARCHAR(40) NOT NULL, "
                + "`job_name` VARCHAR(100) NOT NULL, "
                + "`task_id` VARCHAR(255) NOT NULL, "
                + "`hostname` VARCHAR(255) NOT NULL, "
                + "`ip` VARCHAR(50) NOT NULL, "
                + "`sharding_item` INT NOT NULL, "
                + "`execution_source` VARCHAR(20) NOT NULL, "
                + "`failure_cause` VARCHAR(4000) NULL, "
                + "`is_success` INT NOT NULL, "
                + "`start_time` TIMESTAMP NULL, "
                + "`complete_time` TIMESTAMP NULL, "
                + "PRIMARY KEY (`id`));";
        try (PreparedStatement preparedStatement = conn.prepareStatement(dbSchema)){
            preparedStatement.execute();
        }

    }

    private void createJobStatusTraceTable(final Connection conn) throws SQLException {
        String dbSchema = "CREATE TABLE `" + TABLE_JOB_STATUS_TRACE_LOG + "` ("
                + "`id` VARCHAR(40) NOT NULL, "
                + "`job_name` VARCHAR(100) NOT NULL, "
                + "`original_task_id` VARCHAR(255) NOT NULL, "
                + "`task_id` VARCHAR(255) NOT NULL, "
                + "`slave_id` VARCHAR(50) NOT NULL, "
                + "`source` VARCHAR(50) NOT NULL, "
                + "`execution_type` VARCHAR(20) NOT NULL, "
                + "`sharding_item` VARCHAR(100) NOT NULL, "
                + "`state` VARCHAR(20) NOT NULL, "
                + "`message` VARCHAR(4000) NULL, "
                + "`creation_time` TIMESTAMP NULL, "
                + "PRIMARY KEY (`id`));";
        try (PreparedStatement preparedStatement = conn.prepareStatement(dbSchema)) {
            preparedStatement.execute();
        }
    }


}
