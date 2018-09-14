package com.lyl.husky.core.internal.storage;

import com.lyl.husky.core.exception.JobSystemException;
import com.lyl.husky.core.reg.base.CoordinatorRegistryCenter;
import com.lyl.husky.core.reg.exception.RegException;
import com.lyl.husky.core.reg.exception.RegExceptionHandler;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.api.transaction.CuratorTransactionFinal;
import org.apache.curator.framework.recipes.cache.TreeCache;
import org.apache.curator.framework.recipes.cache.TreeCacheListener;
import org.apache.curator.framework.recipes.leader.LeaderLatch;
import org.apache.curator.framework.state.ConnectionStateListener;

import java.util.List;

/**
 * 作业节点数据访问类
 * <p>
 *     作业节点是在普通的节点前加上作业名称的前缀
 * </p>
 */
public final class JobNodeStorage {

    private final CoordinatorRegistryCenter regCenter;
    private final String jobName;
    private final JobNodePath jobNodePath;

    public JobNodeStorage(final CoordinatorRegistryCenter regCenter, final String jobName){
        this.regCenter = regCenter;
        this.jobName = jobName;
        jobNodePath = new JobNodePath(jobName);
    }

    /**
     * 判断作业节点是否存在
     */
    public boolean isJobNodeExisted(final String node){
        return regCenter.isExisted(jobNodePath.getFullPath(node));
    }

    /**
     * 获取作业节点数据
     */
    public String getJobNodeData(final String node){
        return regCenter.get(jobNodePath.getFullPath(node));
    }

    /**
     * 直接从注册中心而非本地缓存获取作业节点数据
     */
    public String getJobNodeDataDirectly(final String node){
        return regCenter.getDirectly(jobNodePath.getFullPath(node));
    }

    /**
     * 获取作业节点子节点名称列表
     */
    public List<String> getJobNodeChildrenKeys(final String node){
        return regCenter.getChildrenKeys(jobNodePath.getFullPath(node));
    }

    /**
     * 如果存在则创建作业节点
     * <p>
     *     如果作业根节点不存在表示作业已经停止 不再继续创建节点
     * </p>
     */
    public void createJobNodeIfNeeded(final String node){
        if (isJobRootNodeExisted() && !isJobNodeExisted(node)){
            regCenter.persist(jobNodePath.getFullPath(node), "");
        }
    }

    public boolean isJobRootNodeExisted(){
        return regCenter.isExisted("/" + jobName);
    }

    /**
     * 删除作业节点
     */
    public void removeJobNodeIfExisted(final String node){
        if (isJobNodeExisted(node)){
            regCenter.remove(jobNodePath.getFullPath(node));
        }
    }

    /**
     * 填充节点数据
     */
    public void fillJobNode(final String node, final Object value){
        regCenter.persist(jobNodePath.getFullPath(node), value.toString());
    }

    /**
     * 填充临时节点数据
     */
    public void fillEphemeralJobNode(final String node, final Object value){
        regCenter.persistEphemeral(jobNodePath.getFullPath(node), value.toString());
    }

    /**
     * 更新节点数据
     */
    public void updateJobNode(final String node, final Object value){
        regCenter.update(jobNodePath.getFullPath(node), value.toString());
    }

    /**
     * 替换作业节点数据
     */
    public void replaceJobNode(final String node, final Object value){
        regCenter.persist(jobNodePath.getFullPath(node), value.toString());
    }

    /**
     * 在事务中执行操作
     */
    public void executeInTransaction(final TransactionExecutionCallback callback){
        try {
            CuratorTransactionFinal curatorTransactionFinal = getClient().inTransaction().check().forPath("/").and();
            callback.execute(curatorTransactionFinal);
            curatorTransactionFinal.commit();
        } catch (final Exception ex){
            RegExceptionHandler.handleException(ex);
        }
    }

    private CuratorFramework getClient() {
        return (CuratorFramework) regCenter.getRawClient();
    }

    /**
     * 在主节点执行操作
     */
    public void executeInLeader(final String latchNode, final LeaderExecutionCallback callback){
        try (LeaderLatch latch = new LeaderLatch(getClient(), jobNodePath.getFullPath(latchNode))) {
            latch.start();
            latch.await();
            callback.execute();
        } catch (final Exception ex){
            handleException(ex);
        }
    }

    private void handleException(Exception ex) {
        if (ex instanceof InterruptedException){
            Thread.currentThread().interrupt();
        } else {
            throw new JobSystemException(ex);
        }
    }

    /**
     * 注册连接状态监听器
     */
    public void addConnectionStateListener(final ConnectionStateListener listener){
        getClient().getConnectionStateListenable().addListener(listener);
    }

    /**
     * 注册数据监听器
     */
    public void addDataListener(final TreeCacheListener listener){
        TreeCache cache = (TreeCache) regCenter.getRawCache("/".concat(jobName));
        cache.getListenable().addListener(listener);
    }

    /**
     * 获取注册中心当前时间
     */
    public long getRegistryCenterTime(){
        return regCenter.getRegistryCenterTime(jobNodePath.getFullPath("systemTime/current"));
    }

}
