package com.lyl.husky.console.filter;

import com.google.common.base.Optional;
import com.lyl.husky.console.domain.EventTraceDataSourceConfiguration;
import com.lyl.husky.console.domain.EventTraceDataSourceFactory;
import com.lyl.husky.console.domain.RegistryCenterConfiguration;
import com.lyl.husky.console.restful.config.EventTraceDataSourceRestfulApi;
import com.lyl.husky.console.restful.config.RegistryCenterRestfulApi;
import com.lyl.husky.console.service.EventTraceDataSourceConfigurationService;
import com.lyl.husky.console.service.RegistryCenterConfigurationService;
import com.lyl.husky.console.service.impl.EventTraceDataSourceConfigurationServiceImpl;
import com.lyl.husky.console.service.impl.RegistryCenterConfigurationServiceImpl;
import com.lyl.husky.console.util.SessionEventTraceDataSourceConfiguration;
import com.lyl.husky.console.util.SessionRegistryCenterConfiguration;
import com.lyl.husky.lifecycle.internal.reg.RegistryCenterFactory;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;

public class GlobalConfigurationFilter implements Filter {

    private final RegistryCenterConfigurationService regCenterService = new RegistryCenterConfigurationServiceImpl();

    private final EventTraceDataSourceConfigurationService rdbService = new EventTraceDataSourceConfigurationServiceImpl();

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void doFilter(final ServletRequest servletRequest, final ServletResponse servletResponse, final FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) servletRequest;
        HttpSession httpSession = httpRequest.getSession();
        if (null == httpSession.getAttribute(RegistryCenterRestfulApi.REG_CENTER_CONFIG_KEY)){
            loadActivatedRegCenter(httpSession);
        }
        if (null == httpSession.getAttribute(EventTraceDataSourceRestfulApi.DATA_SOURCE_CONFIG_KEY)){
            loadActivatedEventTraceDataSource(httpSession);
        }
        filterChain.doFilter(servletRequest, servletResponse);
    }

    private void loadActivatedEventTraceDataSource(final HttpSession httpSession) {

        Optional<EventTraceDataSourceConfiguration> config = rdbService.loadActivated();
        if (config.isPresent()) {
            String configName = config.get().getName();
            boolean isConnected = setEventTraceDataSourceNameToSession(rdbService.find(configName, rdbService.loadAll()), httpSession);
            if (isConnected) {
                rdbService.load(configName);
            }
        }

    }

    private boolean setEventTraceDataSourceNameToSession(final EventTraceDataSourceConfiguration dataSourceConfig, final HttpSession session) {
        session.setAttribute(EventTraceDataSourceRestfulApi.DATA_SOURCE_CONFIG_KEY, dataSourceConfig);
        try {
            EventTraceDataSourceFactory.createEventTraceDataSource(dataSourceConfig.getDriver(), dataSourceConfig.getUrl(),
                    dataSourceConfig.getUsername(), Optional.fromNullable(dataSourceConfig.getPassword()));
            SessionEventTraceDataSourceConfiguration.setDataSourceConfiguration((EventTraceDataSourceConfiguration) session.getAttribute(EventTraceDataSourceRestfulApi.DATA_SOURCE_CONFIG_KEY));
            // CHECKSTYLE:OFF
        } catch (final Exception ex) {
            // CHECKSTYLE:ON
            return false;
        }
        return true;
    }

    private void loadActivatedRegCenter(final HttpSession httpSession) {
        Optional<RegistryCenterConfiguration> config = regCenterService.loadActivated();
        if (config.isPresent()){
            String configName = config.get().getName();
            boolean isConnected = setRegistryCenterNameToSession(regCenterService.find(configName, regCenterService.loadAll()), httpSession);
            if (isConnected){
                regCenterService.load(configName);
            }
        }
    }

    private boolean setRegistryCenterNameToSession(final RegistryCenterConfiguration regCenterConfig, final HttpSession session) {
        session.setAttribute(RegistryCenterRestfulApi.REG_CENTER_CONFIG_KEY, regCenterConfig);
        try {
            RegistryCenterFactory.createCoordinatorRegistryCenter(regCenterConfig.getZkAddressList(), regCenterConfig.getNamespace(), com.google.common.base.Optional.fromNullable(regCenterConfig.getDigest()));
            SessionRegistryCenterConfiguration.setRegistryCenterConfiguration((RegistryCenterConfiguration) session.getAttribute(RegistryCenterRestfulApi.REG_CENTER_CONFIG_KEY));
        } catch (final Exception ex){
            return false;
        }
        return true;
    }

    @Override
    public void destroy() {

    }
}
