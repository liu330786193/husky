package com.lyl.husky.console.filter;

import com.lyl.husky.console.service.EventTraceDataSourceConfigurationService;
import com.lyl.husky.console.service.RegistryCenterConfigurationService;
import com.lyl.husky.console.service.impl.RegistryCenterConfigurationServiceImpl;

import javax.servlet.*;
import java.io.IOException;

public class GlobalConfigurationFilter implements Filter {

    private final RegistryCenterConfigurationService regCenterService = new RegistryCenterConfigurationServiceImpl();

    private final EventTraceDataSourceConfigurationService rdbService = new Eve

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {

    }

    @Override
    public void destroy() {

    }
}
