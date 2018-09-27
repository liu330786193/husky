package com.lyl.husky.spring.job;

import com.lyl.husky.core.exception.JobSystemException;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.aop.framework.AdvisedSupport;
import org.springframework.aop.support.AopUtils;

import java.lang.reflect.Field;
import java.util.Objects;

/**
 * @Author lyl
 * @Description 基于Spring AOP获取目标对象.
 * @Date 2018/9/27 下午3:47
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class AopTargetUtils {

    /**
     * @Author lyl
     * @Description
     * @Date 2018/9/27 下午3:51
     * @param
     */
    public static Object getTarget(final Object proxy){
        if (!AopUtils.isAopProxy(proxy)){
            return proxy;
        }
        if (AopUtils.isJdkDynamicProxy(proxy)){
            return getProxyTargetObject(proxy, "h");
        } else {
            return getProxyTargetObject(proxy, "CGLIB$CALLBACK_0");
        }
    }

    private static Object getProxyTargetObject(Object proxy, String proxyType) {
        Field h;
        try {
            h = proxy.getClass().getSuperclass().getDeclaredField(proxyType);
        } catch (final NoSuchFieldException ex){
            return getProxyTargetObjectForCglibAndSpring4(proxy);
        }
        h.setAccessible(true);
        try {
            return getTargetObject(h.get(proxy));
        } catch (final IllegalAccessException ex){
            throw new JobSystemException(ex);
        }
    }

    private static Object getProxyTargetObjectForCglibAndSpring4(Object proxy) {
        Field h;
        try {
            h = proxy.getClass().getDeclaredField("CGLIB$CALLBACK_0");
            h.setAccessible(true);
            Object dynamicAdvisedInterceptor = h.get(proxy);
            Field advised = dynamicAdvisedInterceptor.getClass().getDeclaredField("advised");
            advised.setAccessible(true);
            return ((AdvisedSupport)advised.get(dynamicAdvisedInterceptor)).getTargetSource().getTarget();
        } catch (final Exception ex) {
            throw new JobSystemException(ex);
        }
    }

    private static Object getTargetObject(final Object object) {
        try {
            Field advised = object.getClass().getDeclaredField("advised");
            advised.setAccessible(true);
            return ((AdvisedSupport) advised.get(object)).getTargetSource().getTarget();
        } catch (final Exception ex) {
            throw new JobSystemException(ex);
        }
    }

}
