package com.zans.mms.service.impl;

import com.zans.base.service.impl.BaseServiceImpl;
import com.zans.base.util.StringHelper;
import com.zans.base.vo.SelectVO;
import com.zans.mms.dao.mms.BaseAreaMapper;
import com.zans.mms.model.BaseArea;
import com.zans.mms.service.IBaseAreaService;
import com.zans.mms.vo.common.TreeSelect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

/**
 * (BaseArea)表服务实现类
 *
 * @author makejava
 * @since 2021-01-12 15:20:08
 */
@Service("baseAreaService")
public class BaseAreaServiceImpl extends BaseServiceImpl<BaseArea> implements IBaseAreaService {
    @Autowired
    private BaseAreaMapper baseAreaMapper;
    @Resource
    public void setBaseAreaMapper(BaseAreaMapper mapper) {
        super.setBaseMapper(mapper);
        this.baseAreaMapper = mapper;
    }

    @Override
    @Cacheable(cacheNames = "AREA_TREE_LIST")
    public List<TreeSelect> areaTreeList() {
        List<BaseArea> areaList = baseAreaMapper.getFindAll();
        List<TreeSelect> resultList = new ArrayList<>(areaList.size());
        // 初始化非叶子节点
        Map<String, TreeSelect> treeMap = new HashMap<>(areaList.size());
        for (BaseArea area : areaList) {
            TreeSelect node = TreeSelect.builder().id(area.getAreaId()).label(area.getAreaName()).data(area).build();
            treeMap.put(area.getAreaId(), node);
            String areaNum = area.getAreaId();
            if (areaNum.endsWith("0")) {
                resultList.add(node);
            } else {
                TreeSelect parent = treeMap.get(areaNum.substring(0,2)+"00");
                if (parent == null) {
                    resultList.add(node);
                } else {
                    parent.addChild(node);
                }
            }
        }

        return resultList;
    }

    @Override
    @Cacheable(cacheNames = "AREA_LIST")
    public List<SelectVO> areaList() {
        List<SelectVO> selectVOList = new ArrayList<>();
        List<BaseArea> areaList = baseAreaMapper.getFindAll();
        if(StringHelper.isEmpty(areaList)){
           return selectVOList;
        }
        areaList.forEach(baseArea -> {
            SelectVO selectVO = new SelectVO();
            String areaNum =  baseArea.getAreaId();
            selectVO.setItemKey(areaNum);
            selectVO.setItemValue(baseArea.getAreaName());
            selectVOList.add(selectVO);
        });
        return selectVOList;
    }

    @Override
    @Cacheable(cacheNames = "AREA_LIST_ALL")
    public List<SelectVO> findAreaToSelect() {
        return baseAreaMapper.findAreaToSelect();
    }

    @Override
    @Cacheable(cacheNames = "AREA_MAP")
    public Map<Object, String> findAreaToMap() {
        List<SelectVO> selectList = this.findAreaToSelect();
        return selectList.stream().collect(Collectors.toMap(SelectVO::getItemKey, SelectVO::getItemValue));
    }

    @Override
    @Cacheable(cacheNames = "ALL_AREA_TREE_LIST")
    public List<TreeSelect> allAreaTreeList() {
        List<BaseArea> levelOneList = baseAreaMapper.getAreaLevelOne();
        List<BaseArea> areaList = baseAreaMapper.getFindAll();

        levelOneList.addAll(areaList);

        List<TreeSelect> resultList = new ArrayList<>(levelOneList.size());
        // 初始化非叶子节点
        Map<String, TreeSelect> treeMap = new HashMap<>(levelOneList.size());
        for (BaseArea area : levelOneList) {
            TreeSelect node = TreeSelect.builder().id(area.getAreaId()).label(area.getAreaName()).data(area).build();
            treeMap.put(area.getAreaId(), node);
            String areaNum = area.getAreaId();
            if (areaNum.endsWith("0")) {
                resultList.add(node);
            } else {
                TreeSelect parent = treeMap.get(areaNum.substring(0,2)+"00");
                if (parent == null) {
                    resultList.add(node);
                } else {
                    parent.addChild(node);
                }
            }
        }

        return resultList;
    }
}
