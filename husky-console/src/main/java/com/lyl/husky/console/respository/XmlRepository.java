package com.lyl.husky.console.respository;

/**
 * 基于XML的数据访问器
 */
public interface XmlRepository<E> {

    /**
     * 读取数据
     */
    E load();

    /**
     * 存储数据
     */
    void save(E entity);

}
