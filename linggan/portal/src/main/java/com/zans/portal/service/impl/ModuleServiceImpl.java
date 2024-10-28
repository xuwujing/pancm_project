package com.zans.portal.service.impl;

import com.zans.base.service.impl.BaseServiceImpl;
import com.zans.portal.dao.SysModuleMapper;
import com.zans.portal.model.SysModule;
import com.zans.portal.service.IModuleService;
import com.zans.base.vo.SelectVO;
import com.zans.portal.vo.common.TreeSelect;
import com.zans.portal.vo.perm.ModuleRespVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;

/**
 * @author xv
 * @since 2020/3/7 0:04
 */
@Slf4j
@Service
public class ModuleServiceImpl extends BaseServiceImpl<SysModule> implements IModuleService {

    SysModuleMapper moduleMapper;

    @Resource
    public void setModuleMapper(SysModuleMapper moduleMapper) {
        super.setBaseMapper(moduleMapper);
        this.moduleMapper = moduleMapper;
    }

    @Override
    public List<SysModule> findMenuList() {
        return moduleMapper.findMenuList();
    }

    @Override
    public List<SysModule> findMenuListByLikeName(String moduleName) {
        return moduleMapper.findMenuListByLikeName(moduleName);
    }

    @Override
    public List<SelectVO> findModuleToSelect() {
        return moduleMapper.findModuleToSelect();
    }

    @Override
    public List<TreeSelect> getMenuTree(String menuName) {
        List<SysModule> menuList = moduleMapper.findMenuByName(menuName);
        List<TreeSelect> treeList = new ArrayList<>(menuList.size());
        // 初始化非叶子节点
        Map<Integer, TreeSelect> treeMap = new HashMap<>(menuList.size());
        for (SysModule module : menuList) {
            ModuleRespVO vo = new ModuleRespVO();
            vo.fromModule(module);
            TreeSelect node = TreeSelect.builder().id(module.getId()).label(module.getName()).data(vo).build();
            if (node != null && node.getChildren() == null) {
                node.setChildren(new LinkedList<>());
            }
            treeMap.put(module.getId(), node);
            Integer pid = module.getParentId();
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
    public int findByName(String name, Integer id) {
        return moduleMapper.findByName(name, id);
    }

    @Override
    public int findByRoute(String route, Integer id) {
        return moduleMapper.findByRoute(route, id);
    }

    @Override
    public int findSeq(Integer parentId) {
        return moduleMapper.findSeq(parentId);
    }

}
