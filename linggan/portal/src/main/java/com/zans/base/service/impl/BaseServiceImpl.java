package com.zans.base.service.impl;

import com.zans.base.service.BaseService;
import org.javatuples.Pair;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.common.Mapper;

import java.util.Collection;
import java.util.List;

@Service
public class BaseServiceImpl<T> implements BaseService<T> {

    Mapper<T> baseMapper;

    public void setBaseMapper(Mapper<T> baseMapper) {
        this.baseMapper = baseMapper;
    }

    @Override
    public T getById(Integer id) {
        if (id == null) {
            return null;
        }
        return baseMapper.selectByPrimaryKey(id);
    }

    @Override
    public T getById(Long id) {
        if (id == null) {
            return null;
        }
        return baseMapper.selectByPrimaryKey(id);
    }

    @Override
    public List<T> getAll() {
        return baseMapper.selectAll();
    }

    @Override
    public int save(T t) {
        return baseMapper.insert(t);
    }

    @Override
    public int saveSelective(T t) {
        return baseMapper.insertSelective(t);
    }
    @Override
    public void insert(T t) {
        baseMapper.insert(t);
    }

    @Override
    public void update(T t) {
        baseMapper.updateByPrimaryKey(t);
    }
    @Override
    public void updateSelective(T t) {
        baseMapper.updateByPrimaryKeySelective(t);
    }

    @Override
    public void delete(T t) {
        baseMapper.delete(t);
    }

    @Override
    public void deleteAll(Collection<T> list) {
        for (T t : list) {
            baseMapper.delete(t);
        }
    }

    @Override
    public Pair<Boolean, String> errorResult(String message) {
        return new Pair<Boolean, String>(false, message);
    }

    @Override
    public Pair<Boolean, String> errorResult() {
        return new Pair<Boolean, String>(false, "");
    }

    @Override
    public Pair<Boolean, String> successResult() {
        return new Pair<Boolean, String>(true, "");
    }

    @Override
    public Pair<Boolean, String> successResult(String message) {
        return new Pair<Boolean, String>(true, message);
    }
}

