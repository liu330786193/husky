package com.lyl.husky.core.event.rdb;

import com.google.common.base.CaseFormat;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.lyl.husky.core.event.type.JobExecutionEvent;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.text.StrBuilder;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.concurrent.locks.Condition;

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

    private PreparedStatement createDataPreparedStatement(final Connection conn, final String tableName, final Collection<String> tableFields, final Condition condition) {
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

    private String buildLimit(final int page, final int perPage) {
        StringBuilder sqlBuilder = new StringBuilder();
        if (page > 0 && perPage > 0){
            sqlBuilder.append(" LIMIT ").append((page - 1) * perPage).append(",").append(perPage);
        } else {
            sqlBuilder.append(" LIMIT ").append(Condition.DEFAULT_PAGE_SIZE);
        }
        return sqlBuilder.toString();
    }

    private String buildOrder(final List<String> tableFields, final String sortName, final String sortOrder) {
        if (Strings.isNullOrEmpty(sortName)){
            return "";
        }
        String lowerUnderscore = CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE,sortName);
        if (!tableFields.contains(lowerUnderscore)){
            return "";
        }
        StringBuilder sqlBuilder = new StringBuilder();
        sqlBuilder.append(" ORDER BY ").append(lowerUnderscore);
        switch (sortOrder.toUpperCase()){
            case "ASC":
                sqlBuilder.append(" ASC");
                break;
            case "DESC":
                sqlBuilder.append(" DESC");
                break;
            default :
                sqlBuilder.append(" ASC");
        }
        return sqlBuilder.toString();
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

    private int getEventCount(final String tableName, final Collection<String> tableFields, final Condition condition) {
        int result = 0;
        try {
            Connection conn = dataSource.getConnection();
            PreparedStatement preparedStatement = createDataPreparedStatement(conn, tableName, tableFields, condition);
            ResultSet resultSet = preparedStatement.executeQuery(){
                resultSet.next();
                result = resultSet.getInt(1);
            } catch (final SQLException ex){
                log.error("Fetch EventCount from DB error:", ex);
            }

        }
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
