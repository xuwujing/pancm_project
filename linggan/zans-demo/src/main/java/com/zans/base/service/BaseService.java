package com.zans.base.service;

import org.javatuples.Pair;

import java.util.Collection;
import java.util.List;

public interface BaseService<T> {
    T getById(Integer id);

    T getById(Long id);

    List<T> getAll();

    int save(T t);

    int saveSelective(T t);

    /**
     * 插入
     *
     * @param t
     */
    void insert(T t);

    /**
     * 更新
     *
     * @param t
     */
    void update(T t);

    void updateSelective(T t);
    /**
     * 删除
     *
     * @param t
     */
    void delete(T t);

    /**
     * 删除
     *
     * @param list
     */
    void deleteAll(Collection<T> list);

    Pair<Boolean, String> errorResult(String message);

    Pair<Boolean, String> errorResult();

    Pair<Boolean, String> successResult();

    Pair<Boolean, String> successResult(String message);
}
