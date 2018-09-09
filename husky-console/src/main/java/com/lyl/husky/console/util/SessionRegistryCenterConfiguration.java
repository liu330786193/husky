package com.lyl.husky.console.util;

import com.lyl.husky.console.domain.RegistryCenterConfiguration;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * 注册中心配置的会话声明周期
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class SessionRegistryCenterConfiguration {

    private static RegistryCenterConfiguration regCenterConfig;

    /**
     * 从当前会话范围获取注册中心配置
     */
    public static RegistryCenterConfiguration getRegistryCenterConfiguration(){
        return regCenterConfig;
    }

    /**
     * 设置注册中心配置至当前会话范围
     */
    public static void setRegistryCenterConfiguration(final RegistryCenterConfiguration regCenterConfig){
        SessionRegistryCenterConfiguration.regCenterConfig = regCenterConfig;
    }

}
