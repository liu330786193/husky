package com.lyl.husky.lifecycle.restful;

import com.google.common.base.Joiner;
import com.sun.jersey.api.core.PackagesResourceConfig;
import com.sun.jersey.spi.container.servlet.ServletContainer;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.servlet.DefaultServlet;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.eclipse.jetty.util.resource.Resource;

import javax.servlet.DispatcherType;
import javax.servlet.Filter;
import java.util.EnumSet;
import java.util.Optional;

@Slf4j
public final class RestfulServer {

    private final Server server;

    private final ServletContextHandler servletContextHandler;

    public RestfulServer(final int port){
        server = new Server(port);
        servletContextHandler = buildServletContextHandler();
    }

    /**
     * 启动内嵌的RESTful服务器
     */
    public void start(final String packages, final Optional<String> resourcePath){
        start();
    }

    /**
     * 启动内嵌的RESTful服务器.
     *
     * @param packages RESTful实现类所在包
     * @param resourcePath 资源路径
     * @param servletPath servlet路径
     * @throws Exception 启动服务器异常
     */
    public void start(final String packages, final Optional<String> resourcePath, final Optional<String> servletPath) throws Exception {
        log.info("Elastic Job: Start RESTful server");
        HandlerList handlers = new HandlerList();
        if (resourcePath.isPresent()){
            servletContextHandler.setBaseResource(Resource.newClassPathResource(resourcePath.get()));
            servletContextHandler.addServlet(new ServletHolder(DefaultServlet.class), "/*");
        }
        String servletPathStr = (servletPath.isPresent() ? servletPath.get() : "").concat("/*");
        servletContextHandler.addServlet(getServletHolder(packages), servletPathStr);
        handlers.addHandler(servletContextHandler);
        server.setHandler(handlers);
        server.start();
    }

    /**
     * 添加Filter.
     *
     * @param filterClass filter实现类
     * @param urlPattern 过滤的路径
     * @return RESTful服务器
     */
    public RestfulServer addFilter(final Class<? extends Filter> filterClass, final  String urlPattern){
        servletContextHandler.addFilter(filterClass, urlPattern, EnumSet.of(DispatcherType.REQUEST));
        return this;
    }

    private ServletContextHandler buildServletContextHandler() {
        ServletContextHandler result = new ServletContextHandler(ServletContextHandler.SESSIONS);
        result.setContextPath("/");
        return result;
    }

    private ServletHolder getServletHolder(final String packages){
        ServletHolder result = new ServletHolder(ServletContainer.class);
        result.setInitParameter(PackagesResourceConfig.PROPERTY_PACKAGES, Joiner.on(",").join(RestfulServer.class.getPackage().getName(), packages));
        result.setInitParameter("com.sun.jersey.config.property.resourceConfigClass", PackagesResourceConfig.class.getName());
        result.setInitParameter("com.sun.jersey.api.json.POJOMappingFeature", Boolean.TRUE.toString());
        result.setInitParameter("resteasy.scan.providers", Boolean.TRUE.toString());
        result.setInitParameter("resteasy.use.builtin.providers", Boolean.FALSE.toString());
        return result;
    }

    /**
     * 优雅关机
     */
    public void stop(){
        log.info("Elastic Job: Stop RESTful server");
        try {
            server.stop();
        } catch (final Exception e){
            log.error("Elastic Job: Stop RESTful server error", e);
        }
    }
}
