package com.lyl.husky.lifecycle.internal.reg;

import com.google.common.base.Charsets;
import com.google.common.base.Optional;
import com.google.common.hash.HashCode;
import com.google.common.hash.Hasher;
import com.google.common.hash.Hashing;
import com.lyl.husky.core.reg.base.CoordinatorRegistryCenter;
import com.lyl.husky.core.reg.zookeeeper.ZookeeperConfiguration;
import com.lyl.husky.core.reg.zookeeeper.ZookeeperRegistryCenter;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.concurrent.ConcurrentHashMap;

/**
 * 注册中心工厂
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class RegisterCenterFactory {

    private static final ConcurrentHashMap<HashCode, CoordinatorRegistryCenter> REG_CENTER_REGISTRY = new ConcurrentHashMap<>();

    /**
     * 创建注册中心
     * @param connectString 注册中心连接字符串
     * @param namespace 命名空间
     * @param digest 注册中心凭证
     * @return 注册中心对象
     */
    public static CoordinatorRegistryCenter createCoordinatorRegistryCenter(final String connectString, final String namespace, final Optional<String> digest){
        Hasher hasher = Hashing.md5().newHasher().putString(connectString, Charsets.UTF_8).putString(namespace, Charsets.UTF_8);
        if (digest.isPresent()){
            hasher.putString(digest.get(), Charsets.UTF_8);
        }
        HashCode hashCode = hasher.hash();
        CoordinatorRegistryCenter result = REG_CENTER_REGISTRY.get(hashCode);
        if (null != result){
            return result;
        }
        ZookeeperConfiguration zkConfig = new ZookeeperConfiguration(connectString, namespace);
        if (digest.isPresent()){
            zkConfig.setDigest(digest.get());
        }
        result = new ZookeeperRegistryCenter(zkConfig);
        result.init();
        REG_CENTER_REGISTRY.put(hashCode, result);
        return result;

    }

}
