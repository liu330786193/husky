package com.lyl.husky.core.event.rdb;

import com.google.common.base.CaseFormat;
import com.google.common.collect.Lists;
import com.lyl.husky.core.event.type.JobExecutionEvent;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.text.StrBuilder;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * 运行痕迹时间数据库检索
 */
@Slf4j
@RequiredArgsConstructor
public class JobEventRdbSearch {

    private static final String TABLE_JOB_EXECUTION_LOG = "JOB_EXECUTION_LOG";
    private static final String TABLE_JOB_STATUS_TRACE_LOG = "JOB_STATUS_TRACE_LOG";
    private static final List<String> FIELDS_JOB_EXECUTION_LOG = Lists.newArrayList("id", "hostname", "ip", "task_id", "job_name", "execution_source", "sharding_item", "start_time", "complete_time", "is_success", "failure_cause");
    private static final List<String> FIELDS_JOB_STATUS_TRACE_LOG = Lists.newArrayList("id", "job_name", "original_task_id", "task_id", "slave_id", "source", "execution_type", "sharding_item", "state", "message", "creation_time");
    private final DataSource dataSource;

    /**
     * 检索作业运行执行轨迹
     */
    public Result<JobExecutionEvent> findJobExecutionEvents(final Condition condition){
        return new Result<>(getEventCount(TABLE_JOB_STATUS_TRACE_LOG, FIELDS_JOB_STATUS_TRACE_LOG, condition), getJobStatusTraceEvents(condition));
    }

    private List<JobExecutionEvent> getJobStatusTraceEvents(final Condition condition) {

        List<JobExecutionEvent> result = new LinkedList<>();
        try {
            Connection conn = dataSource.getConnection();
            PreparedStatement preparedStatement = createDataPreparedStatement(conn, TABLE_JOB_EXECUTION_LOG, FIELDS_JOB_EXECUTION_LOG, condition);
        } catch (final SQLException ex){
            //TODO 记录失败直接输出日志 未来可考虑配置化
            log.error("Fetch JobExecutionEvent from DB error:", ex);
        }
        return result;
    }

    private PreparedStatement createDataPreparedStatement(final Connection conn, final String tableName, final List<String> tableFields, final Condition condition) {
        String sql = buildDataSql(tableName, tableFields, condition);
    }

    private String buildDataSql(String tableName, List<String> tableFields, Condition condition) {
        StringBuilder sqlBuilder = new StringBuilder();
        String selectSql = buildSelect(tableName, tableFields);
        String whereSql = buildWhere(tableName, tableFields, condition);
        String orderSql = buildOrder(tableFields, condition.getSort(), condition.getOrder());
        String limitSql = buildLimit(condition.getPage(), condition.getPerPage());
        sqlBuilder.append(selectSql).append(whereSql).append(orderSql).append(limitSql);
        return sqlBuilder.toString();
    }

    private String buildLimit(int page, int perPage) {
    }

    private String buildOrder(List<String> tableFields, String sort, String order) {
    }

    private String buildWhere(String tableName, List<String> tableFields, Condition condition) {
        StringBuilder sqlBuilder = new StringBuilder();
        sqlBuilder.append(" WHERE 1=1");
        if (null != condition.getFields() && !condition.getFields().isEmpty()){
            for (Map.Entry<String, Object> entry : condition.getFields().entrySet()){
                StringBuilder lowerUnderscore = CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, entry.getKey()){
                    if (null != entry.getValue() && tableFields.contains(lowerUnderscore)){
                        sqlBuilder.append(" AND ").append(lowerUnderscore).append("=?");
                    }
                }
            }
        }
        if (null != condition.getStartTime()){
            sqlBuilder.append(" AND ").append(getTableTimeField(tableName)).append(">=?");
        }
        if (null != condition.getEndTime()){
            sqlBuilder.append(" AND ").append(getTableTimeField(tableName)).append("<=?");
        }
        return sqlBuilder.toString();
    }

    private String getTableTimeField(final String tableName) {
        String result = "";
        if (TABLE_JOB_EXECUTION_LOG.equals(tableName)){
            result = "start_time";
        } else if (TABLE_JOB_STATUS_TRACE_LOG.equals(tableName)){
            result = "creation_time";
        }
        return result;
    }

    private String buildSelect(String tableName, List<String> tableFields) {
        StringBuilder sqlBuilder = new StringBuilder();
        sqlBuilder.append("SELECT ");
        for (String each : tableFields) {
            sqlBuilder.append(each).append(",");
        }
        sqlBuilder.deleteCharAt(sqlBuilder.length() - 1);
        sqlBuilder.append(" FROM ").append(tableName);
        return sqlBuilder.toString();
    }

    private Integer getEventCount(final String tableJobStatusTraceLog, final List<String> fieldsJobStatusTraceLog, final Condition condition) {
    }

    /**
     * 查询条件对象
     */
    @Getter
    @RequiredArgsConstructor
    public static class Condition {
        private static final int DEFAULT_PAGE_SIZE = 10;
        private final int perPage;
        private final int page;
        private final String sort;
        private final String order;
        private final Date startTime;
        private final Date endTime;
        private final Map<String, Object> fields;
    }

    @Getter
    @RequiredArgsConstructor
    public static class Result<T>{
        private final Integer total;
        private final List<T> rows;
    }
}
