package com.lyl.husky.console.restful.config;

import com.google.common.base.Optional;
import com.lyl.husky.console.domain.EventTraceDataSourceConfiguration;
import com.lyl.husky.console.domain.EventTraceDataSourceFactory;
import com.lyl.husky.console.service.EventTraceDataSourceConfigurationService;
import com.lyl.husky.console.service.impl.EventTraceDataSourceConfigurationServiceImpl;
import com.lyl.husky.console.util.SessionEventTraceDataSourceConfiguration;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import java.util.Collection;

/**
 * 时间追踪数据源配置的RESTful API
 */
@Path("/data-source")
public final class EventTraceDataSourceRestfulApi {

    public static final String DATA_SOURCE_CONFIG_KEY = "data_source_config_key";
    private EventTraceDataSourceConfigurationService eventTraceDataSourceConfigurationService = new EventTraceDataSourceConfigurationServiceImpl();

    /**
     * 判断是否存在已连接的事件追踪数据源配置.
     *
     * @param request HTTP请求
     * @return 是否存在已连接的事件追踪数据源配置
     */
    @GET
    @Path("/activated")
    public boolean activated(final @Context HttpServletRequest request) {
        return eventTraceDataSourceConfigurationService.loadActivated().isPresent();
    }

    /**
     * 读取事件追踪数据源配置.
     *
     * @param request HTTP请求对象
     * @return 事件追踪数据源配置集合
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Collection<EventTraceDataSourceConfiguration> load(final @Context HttpServletRequest request) {
        Optional<EventTraceDataSourceConfiguration> dataSourceConfig = eventTraceDataSourceConfigurationService.loadActivated();
        if (dataSourceConfig.isPresent()) {
            setDataSourceNameToSession(dataSourceConfig.get(), request.getSession());
        }
        return eventTraceDataSourceConfigurationService.loadAll().getEventTraceDataSourceConfigurationSet();
    }

    /**
     * 添加事件追踪数据源配置.
     *
     * @param config 事件追踪数据源配置
     * @return 是否添加成功
     */
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public boolean add(final EventTraceDataSourceConfiguration config) {
        return eventTraceDataSourceConfigurationService.add(config);
    }

    /**
     * 删除事件追踪数据源配置.
     *
     * @param config 事件追踪数据源配置
     */
    @DELETE
    @Consumes(MediaType.APPLICATION_JSON)
    public void delete(final EventTraceDataSourceConfiguration config) {
        eventTraceDataSourceConfigurationService.delete(config.getName());
    }

    /**
     * 连接事件追踪数据源测试.
     *
     * @param config 事件追踪数据源配置
     * @param request HTTP请求对象
     * @return 是否连接成功
     */
    @POST
    @Path("/connectTest")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public boolean connectTest(final EventTraceDataSourceConfiguration config, final @Context HttpServletRequest request) {
        return setDataSourceNameToSession(config, request.getSession());
    }

    /**
     * 连接事件追踪数据源.
     *
     * @param config 事件追踪数据源配置
     * @param request HTTP请求对象
     * @return 是否连接成功
     */
    @POST
    @Path("/connect")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public boolean connect(final EventTraceDataSourceConfiguration config, final @Context HttpServletRequest request) {
        boolean isConnected = setDataSourceNameToSession(eventTraceDataSourceConfigurationService.find(config.getName(), eventTraceDataSourceConfigurationService.loadAll()), request.getSession());
        if (isConnected) {
            eventTraceDataSourceConfigurationService.load(config.getName());
        }
        return isConnected;
    }

    private boolean setDataSourceNameToSession(final EventTraceDataSourceConfiguration dataSourceConfig, final HttpSession session) {
        session.setAttribute(DATA_SOURCE_CONFIG_KEY, dataSourceConfig);
        try {
            EventTraceDataSourceFactory.createEventTraceDataSource(dataSourceConfig.getDriver(), dataSourceConfig.getUrl(),
                    dataSourceConfig.getUsername(), Optional.fromNullable(dataSourceConfig.getPassword()));
            SessionEventTraceDataSourceConfiguration.setDataSourceConfiguration((EventTraceDataSourceConfiguration) session.getAttribute(DATA_SOURCE_CONFIG_KEY));
            // CHECKSTYLE:OFF
        } catch (final Exception ex) {
            // CHECKSTYLE:ON
            return false;
        }
        return true;
    }

}
