package com.lyl.husky.console.restful.config;

import com.google.common.base.Optional;
import com.lyl.husky.console.domain.RegistryCenterConfiguration;
import com.lyl.husky.console.service.RegistryCenterConfigurationService;
import com.lyl.husky.console.service.impl.RegistryCenterConfigurationServiceImpl;
import com.lyl.husky.console.util.SessionRegistryCenterConfiguration;
import com.lyl.husky.core.reg.exception.RegException;
import com.lyl.husky.lifecycle.internal.reg.RegistryCenterFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import java.util.Collection;

/**
 * 注册中心配置的RESTful API
 */
@Path("/registry-center")
public final class RegistryCenterRestfulApi{

    public static final String REG_CENTER_CONFIG_KEY = "reg_center_config_key";

    private RegistryCenterConfigurationService regCenterService = new RegistryCenterConfigurationServiceImpl();

    /**
     * 判断是否存在已连接的注册中心配置
     */
    @GET
    @Path("/activated")
    public boolean activated(final @Context HttpServletRequest request){
        return regCenterService.loadActivated().isPresent();
    }

    /**
     * 读取配置中心集合
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Collection<RegistryCenterConfiguration> load(final  @Context HttpServletRequest request){
        Optional<RegistryCenterConfiguration> regCenterConfig = regCenterService.loadActivated();
        if (regCenterConfig.isPresent()){
            setRegistryCenterNameToSession(regCenterConfig.get(), request.getSession());
        }
        return regCenterService.loadAll().getRegistryCenterConfigurationSet();
    }

    private boolean setRegistryCenterNameToSession(final RegistryCenterConfiguration regCenterConfig, final HttpSession session) {
        session.setAttribute(REG_CENTER_CONFIG_KEY, regCenterConfig);
        try {
            RegistryCenterFactory.createCoordinatorRegistryCenter(regCenterConfig.getZkAddressList(), regCenterConfig.getNamespace(), Optional.fromNullable(regCenterConfig.getDigest()));
            SessionRegistryCenterConfiguration.setRegistryCenterConfiguration((RegistryCenterConfiguration) session.getAttribute(REG_CENTER_CONFIG_KEY));
        } catch (final RegException ex){
            return false;
        }
        return true;
    }

    /**
     * 添加注册中心
     */
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public boolean add(final RegistryCenterConfiguration config){
        return regCenterService.add(config);
    }

    /**
     * 删除注册中心
     */
    @DELETE
    @Consumes(MediaType.APPLICATION_JSON)
    public void delete(final RegistryCenterConfiguration config){
        regCenterService.delete(config.getName());
    }

    /**
     * 连接
     */
    @POST
    @Path("/connect")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public boolean connect(final RegistryCenterConfiguration config, final @Context HttpServletRequest request) {
        boolean isConnected = setRegistryCenterNameToSession(regCenterService.find(config.getName(), regCenterService.loadAll()), request.getSession());
        if (isConnected) {
            regCenterService.load(config.getName());
        }
        return isConnected;
    }
}
