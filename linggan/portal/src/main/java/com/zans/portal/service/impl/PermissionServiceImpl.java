package com.zans.portal.service.impl;

import com.zans.base.service.impl.BaseServiceImpl;
import com.zans.base.util.StringHelper;
import com.zans.portal.config.LocalCacheConfig;
import com.zans.portal.dao.SysPermissionMapper;
import com.zans.portal.model.SysModule;
import com.zans.portal.model.SysPermission;
import com.zans.portal.service.IModuleService;
import com.zans.portal.service.IPermissionService;
import com.zans.portal.util.Router;
import com.zans.portal.vo.common.TreeSelect;
import com.zans.portal.vo.perm.PermissionRespVO;
import com.zans.portal.vo.user.MenuItem;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;

import static com.zans.portal.config.GlobalConstants.*;
import static com.zans.portal.config.GlobalConstants.MENU_ASSESS_PATH;

/**
 * 权限服务
 *
 * @author xv
 * @since 2020/3/6 13:28
 */
@Service
@Slf4j
public class PermissionServiceImpl extends BaseServiceImpl<SysPermission> implements IPermissionService {

    SysPermissionMapper permissionMapper;

    @Autowired
    IModuleService moduleService;

    @Autowired
    private  LocalCacheConfig localCacheConfig;


    @Resource
    public void setPermissionMapper(SysPermissionMapper permissionMapper) {
        super.setBaseMapper(permissionMapper);
        this.permissionMapper = permissionMapper;
    }

    @Override
    public boolean isFrontRouteValid(String route, Integer userId) {
        Router router = this.getUserFrontRoute(userId);
        return router.isValid(route);
    }

    @Override
    public boolean isApiValid(String api, Integer userId) {
        Router router = this.getUserApiRoute(userId);
        return router.isValid(api);
    }

    @Override
    @Cacheable(cacheNames = "PERM_ROUTE_MAP", key = "#userId")
    public Router getUserFrontRoute(Integer userId) {
        List<String> list = permissionMapper.findUserFrontRoutes(userId);
        return this.processPermission(list);
    }

    @Override
    @Cacheable(cacheNames = "PERM_API_MAP", key = "#userId")
    public Router getUserApiRoute(Integer userId) {
        List<String> list = permissionMapper.findUserApiRoutes(userId);
        return this.processPermission(list);
    }

    private Router processPermission(List<String> permissionList) {
        Router router = new Router();
        for (String row : permissionList) {
            if (StringHelper.isBlank(row)) {
                continue;
            }
            String[] array = row.split(",");
            for (String str : array) {
                router.route(str);
            }
        }
        return router;
    }

    @Override
    //@Cacheable(cacheNames = "PERM_API_MENU", key = "#userId")
    public List<MenuItem> getMenuTreeByUserId(Integer userId,int status) {

        if(status == 0){
            List<MenuItem> itemList = localCacheConfig.getMenuItemCache(userId);
            if(itemList!=null && itemList.size()>0){
                return itemList;
            }
        }
        List<SysModule> menuList = moduleService.findMenuList();
//        System.out.println("userId："+userId+" menuList.size:"+menuList.size());
        Router router = this.getUserFrontRoute(userId);
        List<MenuItem> result = new LinkedList<>();

//        System.out.println("userId："+userId+"route:"+router.getNormalRouteSet());
//        menuList.forEach(sysModule -> {
//
//            System.out.println("userId："+userId+"flag :"+router.isValid(sysModule.getRoute()) +" =="+sysModule.getRoute()+ "  "+sysModule.getName());
//        });

        // 保留个人中心
        // 2020-9-3 新增资产文件夹菜单保留
        menuList.removeIf(menu ->
                (menu.getMenuType() == SYS_MODULE_MENU_TYPE_MENU &&
                !menu.getRoute().equals(MENU_HOME_PATH) &&
                !menu.getRoute().startsWith(MENU_PERSON_PATH) &&
                !menu.getRoute().startsWith(MENU_ASSET_PATH) ) ||
                !router.isValid(menu.getRoute()) );
//        System.out.println("userId："+userId+" menuList.size2:"+ menuList.size());
//        if(menuList.size()<10){
//            System.out.println("userId："+ JSONObject.toJSONString(menuList));
//        }
        // 设置菜单的层次关系
        Map<Integer, MenuItem> menuMap = new HashMap<>(menuList.size());
        for (SysModule row : menuList) {
            MenuItem item = new MenuItem();
            item.fromSysModule(row);
            if (row.getParentId() == 0) {
                // 一级节点
                menuMap.put(item.getId(), item);
                result.add(item);
            }
        }

        for (MenuItem item : result) {
            List<MenuItem> child = getChild(item, menuList);
            item.setSubItem(child);
        }


        // 移除空菜单的文件夹菜单（主页保留）
        result.removeIf(menu -> menu.getMenuType() == SYS_MODULE_MENU_TYPE_FOLDER &&
                !menu.getPath().equals(MENU_HOME_PATH) &&
                !menu.getPath().startsWith(MENU_PERSON_PATH) &&
                !menu.getPath().startsWith(MENU_MAINTAIN_PATH) &&
                !menu.getPath().startsWith(MENU_ASSESS_PATH) &&
                (menu.getSubItem() == null || menu.getSubItem().size() == 0));
//        System.out.println("===="+ JSONObject.toJSONString(result));
        localCacheConfig.putMenuItemCache(userId,result);
        return result;
    }

    @Override
    @CachePut(cacheNames = "PERM_API_MENU", key = "#userId")
    public List<MenuItem> getNoCacheMenuTreeByUserId(Integer userId) {
        List<SysModule> menuList = moduleService.findMenuList();

        Router router = this.getUserFrontRoute(userId);
        List<MenuItem> result = new LinkedList<>();
        // 保留个人中心
        menuList.removeIf(menu -> menu.getMenuType() == SYS_MODULE_MENU_TYPE_MENU &&
                !menu.getRoute().equals(MENU_HOME_PATH) &&
                !menu.getRoute().startsWith(MENU_PERSON_PATH) &&
                !router.isValid(menu.getRoute()));
        // 设置菜单的层次关系
        Map<Integer, MenuItem> menuMap = new HashMap<>(menuList.size());
        for (SysModule row : menuList) {
            MenuItem item = new MenuItem();
            item.fromSysModule(row);
            if (row.getParentId() == 0) {
                // 一级节点
                menuMap.put(item.getId(), item);
                result.add(item);
            }
        }

        for (MenuItem item : result) {
            List<MenuItem> child = getChild(item, menuList);
            item.setSubItem(child);
        }


        // 移除空菜单的文件夹菜单（主页保留）
        result.removeIf(menu ->
                menu.getMenuType() == SYS_MODULE_MENU_TYPE_FOLDER &&
                !menu.getPath().equals(MENU_HOME_PATH) &&
                !menu.getPath().startsWith(MENU_PERSON_PATH) &&
                (menu.getSubItem() == null || menu.getSubItem().size() == 0));
        return result;
    }

    public List<MenuItem> getChild(MenuItem menuItem, List<SysModule> rootMenu) {
        List<MenuItem> subItem = new ArrayList<>();
        for (SysModule module : rootMenu) {
            if (module.getParentId().intValue() == menuItem.getId().intValue()) {
                MenuItem item = new MenuItem();
                item.fromSysModule(module);

                List<MenuItem> child = getChild(item, rootMenu);
                item.setSubItem(child);
                subItem.add(item);
                if (CollectionUtils.isEmpty(child)){
                    continue;
                }
            }
        }
        return subItem;
    }


    @Override
    public List<String> getAllFrontRouteByUserId(Integer userId) {
        Router router = this.getUserFrontRoute(userId);
        return router.toList();
    }

    @Override
    public List<PermissionRespVO> findAllPermissions(String permName) {
        return permissionMapper.findAllPermissions(permName);
    }

    @Override
    public List<PermissionRespVO> findPermissionByRole(Integer roleId) {
        return permissionMapper.findPermissionByRole(roleId);
    }

    @Override
    public PermissionRespVO findPermissionByName(String permName) {
        return permissionMapper.findPermissionByName(permName);
    }

    @Override
    public PermissionRespVO findPermissionByNameExceptId(String name, Integer id) {
        return null;
    }

    @Override
    public List<TreeSelect> findAllPermissionInTree() {
        return findAllPermissionInTree(null);
    }

    @Override
    public List<TreeSelect> findAllPermissionInTree(String permName) {
        List<SysModule> menuList = moduleService.findMenuListByLikeName(permName);
        List<PermissionRespVO> permList = this.findAllPermissions(permName);
        List<TreeSelect> treeList = new ArrayList<>(menuList.size());

        // 初始化非叶子节点
        Map<Integer, TreeSelect> treeMap = new HashMap<>(menuList.size() + permList.size());
        for (SysModule module : menuList) {
            PermissionRespVO vo = new PermissionRespVO();
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
                    log.error("module parent[{}] is null", pid);
                } else {
                    parent.addChild(node);
                }
            }
        }

        for (PermissionRespVO vo : permList) {
            TreeSelect node = TreeSelect.builder().id(vo.getPermId()).label(vo.getPermName()).data(vo).build();
            if (node != null && node.getChildren() == null) {
                node.setChildren(new LinkedList<>());
            }
            treeMap.put(vo.getPermId(), node);
            Integer pid = vo.getModule();
            if (pid == 0) {
                treeList.add(node);
                treeMap.put(vo.getPermId(), node);
            } else {
                TreeSelect parent = treeMap.get(pid);
                if (parent == null) {
                    log.error("perm parent[{}] is null", pid);
                } else {
                    parent.addChild(node);
                }
            }
        }
        return treeList;
    }

    @Override
    public List<TreeSelect> findAllModuleInTree() {
        List<SysModule> menuList = moduleService.findMenuList();
        List<TreeSelect> treeList = new ArrayList<>(menuList.size());

        // 初始化非叶子节点
        Map<Integer, TreeSelect> treeMap = new HashMap<>(menuList.size());
        for (SysModule module : menuList) {
            TreeSelect node = TreeSelect.builder().id(module.getId()).label(module.getName()).build();
            treeMap.put(module.getId(), node);
            Integer pid = module.getParentId();
            if (pid == 0) {
                treeList.add(node);
            } else {
                TreeSelect parent = treeMap.get(pid);
                if (parent == null) {
                    log.error("module parent[{}] is null", pid);
                } else {
                    parent.addChild(node);
                }
            }
        }
        return treeList;
    }

    @Override
    public void updateRolePermission(Integer roleId, List<Integer> permIds) {
        this.permissionMapper.deletePermissionByRole(roleId);
        for (Integer permId : permIds) {
            if (this.isPermissionNode(permId)) {
                this.permissionMapper.insertRolePerm(roleId, permId);
            }
        }
    }

    private boolean isPermissionNode(Integer permId) {
        return permId >= MIN_PERMISSION_ID;
    }

    @Override
    public int findByModuleId(Integer moduleId) {
        return permissionMapper.findByModuleId(moduleId);
    }

    @Override
    public String getpasswdPermission(String route) {
        return permissionMapper.findByRouteAndUserId(route);
    }

}
