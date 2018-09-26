package com.lyl.husky.core.internal.monitor;

import com.google.common.base.Joiner;
import com.lyl.husky.core.internal.config.ConfigurationService;
import com.lyl.husky.core.internal.util.SensitiveInfoUtils;
import com.lyl.husky.core.reg.base.CoordinatorRegistryCenter;
import jdk.nashorn.internal.ir.WhileNode;
import lombok.extern.slf4j.Slf4j;
import org.apache.curator.framework.recipes.cache.ChildData;
import org.apache.curator.framework.recipes.cache.TreeCache;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

/**
 * 作业监控服务
 */
@Slf4j
public final class MonitorService {
    
    public static final String DUMP_COMMAND = "dump";
    private final String jobName;
    private final CoordinatorRegistryCenter regCenter;
    private final ConfigurationService configServer;
    private ServerSocket serverSocket;
    private volatile boolean closed;
    
    public MonitorService(final CoordinatorRegistryCenter regCenter, final String jobName){
        this.jobName = jobName;
        this.regCenter = regCenter;
        configServer = new ConfigurationService(regCenter, jobName);
    }

    /**
     * 初始化作业监听服务.
     */
    public void listen(){
        int port = configServer.load(true).getMonitorPort();
        if (port < 0){
            return;
        }
        try {
            log.info("Elastic job: Monitor service is running, the port is '{}'", port);
            openSocketForMonitor(port);
        } catch (final IOException ex){
            log.error("Elastic job: Monitor service listen failure, error is: ", ex);
        }    
    }

    private void openSocketForMonitor(final int port) throws IOException {
        serverSocket = new ServerSocket(port);
        new Thread(){
            @Override
            public void run() {
                while (!closed){
                    try {
                        process(serverSocket.accept());
                    } catch (final IOException ex){
                        log.error("Elastic job: Monitor service open socket for monitor failure, error is: ", ex);
                    }
                }
            }
        }.start();
    }

    private void process(final Socket socket) throws IOException {
        try (
                BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
                Socket autoCloseSocket = socket){
            String cmdLine = reader.readLine();
            if (null != cmdLine && DUMP_COMMAND.equalsIgnoreCase(cmdLine)){
                List<String> result = new ArrayList<>();
                dumpDirectly("/" + jobName, result);
                outputMessage(writer, Joiner.on("\n").join(SensitiveInfoUtils.filterSensitiveIps(result)) + "\n");
            }
        }
    }

    private void dumpDirectly(final String path, final List<String> result) {
        for (String each : regCenter.getChildrenKeys(path)){
            String zkPath = path + "/" + each;
            String zkValue = regCenter.get(zkPath);
            if (null == zkValue){
                zkValue = "";
            }
            TreeCache treeCache = (TreeCache) regCenter.getRawCache("/" + jobName);
            ChildData treeCacheData = treeCache.getCurrentData(zkPath);
            String treeCachePath = null == treeCacheData ? "" : treeCacheData.getPath();
            String treeCacheValue = null == treeCacheData ? "" : new String(treeCacheData.getData());
            if (zkValue.equals(treeCacheValue) && zkPath.equals(treeCachePath)){
                result.add(Joiner.on(" | ").join(zkPath, zkValue));
            }else {
                result.add(Joiner.on(" | ").join(zkPath, zkValue, treeCachePath, treeCacheValue));
            }
            dumpDirectly(zkPath, result);
        }
    }

    private void outputMessage(final BufferedWriter outputWriter, final String msg) throws IOException {
        outputWriter.append(msg);
        outputWriter.flush();
    }

    /**
     * 关闭作业监听服务
     */
    public void close(){
        closed = true;
        try {
            serverSocket.close();
        } catch (final IOException ex){
            log.error("Elastic job: Monitor service close failure, error is: ", ex);
        }
    }
}
