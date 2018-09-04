package com.lyl.husky.console;

import com.lyl.husky.console.filter.GlobalConfigurationFilter;
import com.lyl.husky.lifecycle.restful.RestfulServer;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@NoArgsConstructor
@Slf4j
public final class HusklyConsoleBootstrap {

    private static final String CONSOLE_PATH = "console";

    /**
     * 启动Resultful服务并加载页面
     */
    public static void main(String[] args) {
        int port = 8899;
        if (1 == args.length){
            try {
                port = Integer.parseInt(args[0]);
            } catch (final NumberFormatException ex){
                log.warn("Wrong port format, using default port 8899 instead.");
            }
        }
        RestfulServer restfulServer = new RestfulServer(port);
        restfulServer.addFilter(GlobalConfigurationFilter.class)
    }

}
