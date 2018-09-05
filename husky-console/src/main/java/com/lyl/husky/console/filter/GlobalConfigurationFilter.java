package com.lyl.husky.console.filter;

import com.lyl.husky.console.service.EventTraceDataSourceConfigurationService;
import com.lyl.husky.console.service.RegistryCenterConfigurationService;
import com.lyl.husky.console.service.impl.EventTraceDataSourceConfigurationServiceImpl;
import com.lyl.husky.console.service.impl.RegistryCenterConfigurationServiceImpl;

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
        if (null = httpSession.getAttribute(Registrce))
    }

    @Override
    public void destroy() {

    }
}
