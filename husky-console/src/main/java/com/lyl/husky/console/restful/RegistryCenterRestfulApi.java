package com.lyl.husky.console.restful;

import com.lyl.husky.console.domain.RegistryCenterConfiguration;
import com.lyl.husky.console.service.RegistryCenterConfigurationService;
import com.lyl.husky.console.service.impl.RegistryCenterConfigurationServiceImpl;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import java.util.Collection;
import java.util.Optional;

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
            setRe
        }
    }

    private boolean setRegistryCenterNameToSession(final RegistryCenterConfiguration regCenterConfig, final HttpSession session) {
        session.setAttribute(REG_CENTER_CONFIG_KEY, regCenterConfig);
        try {
            Regicen
        }
    }

}
