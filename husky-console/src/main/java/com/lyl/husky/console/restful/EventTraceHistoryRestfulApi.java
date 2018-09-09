package com.lyl.husky.console.restful;

import com.google.common.base.Strings;
import com.lyl.husky.console.domain.EventTraceDataSourceConfiguration;
import com.lyl.husky.console.service.EventTraceDataSourceConfigurationService;
import com.lyl.husky.console.service.impl.EventTraceDataSourceConfigurationServiceImpl;
import com.lyl.husky.console.util.SessionEventTraceDataSourceConfiguration;
import org.apache.commons.dbcp.BasicDataSource;

import javax.sql.DataSource;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriInfo;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 事件追踪历史记录的RESTful API.
 */
@Path("/event-trace")
public final class EventTraceHistoryRestfulApi {

    private EventTraceDataSourceConfiguration eventTraceDataSourceConfiguration = SessionEventTraceDataSourceConfiguration.getEventTraceDataSourceConfiguration();
    private EventTraceDataSourceConfigurationService eventTraceDataSourceConfigurationService = new EventTraceDataSourceConfigurationServiceImpl();

    /**
     * 查询作业执行事件.
     *
     * @param uriInfo 查询条件
     * @return 运行痕迹事件结果集
     * @throws ParseException 解析异常
     */
    @GET
    @Path("/execution")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public JobEventRdbSearch.Result<JobExecutionEvent> findJobExecutionEvents(@Context final UriInfo uriInfo) throws ParseException {
        if (!eventTraceDataSourceConfigurationService.loadActivated().isPresent()) {
            return new JobEventRdbSearch.Result<>(0, new ArrayList<JobExecutionEvent>());
        }
        JobEventRdbSearch jobEventRdbSearch = new JobEventRdbSearch(setUpEventTraceDataSource());
        return jobEventRdbSearch.findJobExecutionEvents(buildCondition(uriInfo, new String[]{"jobName", "ip", "isSuccess"}));
    }

    /**
     * 查询作业状态事件.
     *
     * @param uriInfo 查询条件
     * @return 运行痕迹事件结果集
     * @throws ParseException 解析异常
     */
    @GET
    @Path("/status")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public JobEventRdbSearch.Result<JobStatusTraceEvent> findJobStatusTraceEvents(@Context final UriInfo uriInfo) throws ParseException {
        if (!eventTraceDataSourceConfigurationService.loadActivated().isPresent()) {
            return new JobEventRdbSearch.Result<>(0, new ArrayList<JobStatusTraceEvent>());
        }
        JobEventRdbSearch jobEventRdbSearch = new JobEventRdbSearch(setUpEventTraceDataSource());
        return jobEventRdbSearch.findJobStatusTraceEvents(buildCondition(uriInfo, new String[]{"jobName", "source", "executionType", "state"}));
    }

    private DataSource setUpEventTraceDataSource() {
        BasicDataSource result = new BasicDataSource();
        result.setDriverClassName(eventTraceDataSourceConfiguration.getDriver());
        result.setUrl(eventTraceDataSourceConfiguration.getUrl());
        result.setUsername(eventTraceDataSourceConfiguration.getUsername());
        result.setPassword(eventTraceDataSourceConfiguration.getPassword());
        return result;
    }

    private JobEventRdbSearch.Condition buildCondition(final UriInfo info, final String[] params) throws ParseException {
        int perPage = 10;
        int page = 1;
        if (!Strings.isNullOrEmpty(info.getQueryParameters().getFirst("per_page"))) {
            perPage = Integer.parseInt(info.getQueryParameters().getFirst("per_page"));
        }
        if (!Strings.isNullOrEmpty(info.getQueryParameters().getFirst("page"))) {
            page = Integer.parseInt(info.getQueryParameters().getFirst("page"));
        }
        String sort = info.getQueryParameters().getFirst("sort");
        String order = info.getQueryParameters().getFirst("order");
        Date startTime = null;
        Date endTime = null;
        Map<String, Object> fields = getQueryParameters(info, params);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        if (!Strings.isNullOrEmpty(info.getQueryParameters().getFirst("startTime"))) {
            startTime = simpleDateFormat.parse(info.getQueryParameters().getFirst("startTime"));
        }
        if (!Strings.isNullOrEmpty(info.getQueryParameters().getFirst("endTime"))) {
            endTime = simpleDateFormat.parse(info.getQueryParameters().getFirst("endTime"));
        }
        return new JobEventRdbSearch.Condition(perPage, page, sort, order, startTime, endTime, fields);
    }

    private Map<String, Object> getQueryParameters(final UriInfo info, final String[] params) {
        final Map<String, Object> result = new HashMap<>();
        for (String each : params) {
            if (!Strings.isNullOrEmpty(info.getQueryParameters().getFirst(each))) {
                result.put(each, info.getQueryParameters().getFirst(each));
            }
        }
        return result;
    }

}
