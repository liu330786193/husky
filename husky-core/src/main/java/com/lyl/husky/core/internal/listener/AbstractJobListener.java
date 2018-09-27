package com.lyl.husky.core.internal.listener;

import com.google.common.base.Charsets;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.cache.ChildData;
import org.apache.curator.framework.recipes.cache.TreeCacheEvent;
import org.apache.curator.framework.recipes.cache.TreeCacheListener;

/**
 * @Author lyl
 * @Description 作业注册中心的监听器
 * @Date 2018/9/27 上午11:25
 */
public abstract class AbstractJobListener implements TreeCacheListener {

    @Override
    public void childEvent(CuratorFramework client, TreeCacheEvent event) {
        ChildData childData = event.getData();
        if (null == childData){
            return;
        }
        String path = childData.getPath();
        if (path.isEmpty()){
            return;
        }
        dataChanged(path, event.getType(), null == childData.getData() ? "" : new String(childData.getData(), Charsets.UTF_8));
    }

    protected abstract void dataChanged(final String path, final TreeCacheEvent.Type eventType, final String data);
}
