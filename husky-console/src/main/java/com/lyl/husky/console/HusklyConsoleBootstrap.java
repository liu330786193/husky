package com.lyl.husky.console;

import com.google.common.base.Optional;
import com.lyl.husky.console.filter.GlobalConfigurationFilter;
import com.lyl.husky.console.restful.JobOperationRestfulApi;
import com.lyl.husky.lifecycle.restful.RestfulServer;
import com.lyl.husky.lifecycle.security.WwwAuthFilter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@NoArgsConstructor
@Slf4j
public final class HusklyConsoleBootstrap {

    private static final String CONSOLE_PATH = "console";

    /**
     * 启动Resultful服务并加载页面
     */
    public static void main(String[] args) throws Exception {
        int port = 8899;
        if (1 == args.length){
            try {
                port = Integer.parseInt(args[0]);
            } catch (final NumberFormatException ex){
                log.warn("Wrong port format, using default port 8899 instead.");
            }
        }
        RestfulServer restfulServer = new RestfulServer(port);
        restfulServer.addFilter(GlobalConfigurationFilter.class, ".html")
                .addFilter(WwwAuthFilter.class, "/")
                .addFilter(WwwAuthFilter.class, "*.html")
                .start(JobOperationRestfulApi.class.getPackage().getName(), Optional.of(CONSOLE_PATH));
    }

}
