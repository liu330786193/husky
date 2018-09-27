package com.lyl.husky.core.internal.listener;

import com.lyl.husky.core.internal.storage.JobNodeStorage;
import com.lyl.husky.core.reg.base.CoordinatorRegistryCenter;
import org.apache.curator.framework.recipes.cache.TreeCacheListener;

/**
 * @Author lyl
 * @Description 作业注册中心的监听器管理者的抽象类
 * @Date 2018/9/27 上午11:15
 */
public abstract class AbstractListenerManager {

    private final JobNodeStorage jobNodeStorage;

    protected AbstractListenerManager(final CoordinatorRegistryCenter regCenter, final String jobName){
        jobNodeStorage = new JobNodeStorage(regCenter, jobName);
    }

    /**
     * 开启监听器
     */
    public abstract void start();

    protected  void addDataListener(final TreeCacheListener listener){
        jobNodeStorage.addDataListener(listener);
    }

}
