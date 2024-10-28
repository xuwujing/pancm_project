package com.zans.portal.service.impl;

import com.zans.base.service.impl.BaseServiceImpl;
import com.zans.portal.dao.SysRegionMapper;
import com.zans.portal.model.SysModule;
import com.zans.portal.model.SysRegion;
import com.zans.portal.service.IRegionService;
import com.zans.portal.vo.area.RegionRespVO;
import com.zans.portal.vo.common.TreeSelect;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;

/**
 * @author yhj
 */
@Service
@Slf4j
public class RegionServiceImpl extends BaseServiceImpl<SysRegion> implements IRegionService {

    SysRegionMapper regionMapper;

    @Resource
    public void setRegionMapper(SysRegionMapper regionMapper) {
        super.setBaseMapper(regionMapper);
        this.regionMapper = regionMapper;
    }

    @Override
    public List<TreeSelect> getRegionTree(String regionName) {
        List<SysRegion> regionList = regionMapper.findRegionByName(regionName);
        List<TreeSelect> treeList = new ArrayList<>(regionList.size());
        // 初始化非叶子节点
        Map<Integer, TreeSelect> treeMap = new HashMap<>(regionList.size());
        for (SysRegion region : regionList) {
            RegionRespVO vo = new RegionRespVO();
            vo.fromRegion(region);
            TreeSelect node = TreeSelect.builder().id(region.getRegionId()).label(region.getRegionName()).data(vo).build();
            if (node != null && node.getChildren() == null) {
                node.setChildren(new LinkedList<>());
            }
            treeMap.put(region.getRegionId(), node);
            Integer pid = region.getParentId();
            if (pid == 0) {
                treeList.add(node);
            } else {
                TreeSelect parent = treeMap.get(pid);
                if (parent == null) {
                    treeList.add(node);
                } else {
                    parent.addChild(node);
                }
            }
        }
        return treeList;
    }

    @Override
    public int getByRegionName(String regionName, Integer regionId) {
        return regionMapper.findByRegionName(regionName, regionId);
    }

    @Override
    public String getRegionNameById(Integer id) {
        return regionMapper.getRegionNameById(id);
    }
}
