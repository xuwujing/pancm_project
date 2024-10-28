package com.zans.mms.service.impl;

import com.zans.base.config.LocalCacheConfig;
import com.zans.base.exception.BusinessException;
import com.zans.base.service.impl.BaseServiceImpl;
import com.zans.base.util.PermissionHelper;
import com.zans.base.util.StringHelper;
import com.zans.base.vo.UserSession;
import com.zans.mms.dao.mms.SysPermissionMapper;
import com.zans.mms.model.SysModule;
import com.zans.mms.model.SysPermission;
import com.zans.mms.model.SysRolePerm;
import com.zans.mms.model.SysUser;
import com.zans.mms.service.IBaseOrgService;
import com.zans.mms.service.IModuleService;
import com.zans.mms.service.IPermissionService;
import com.zans.mms.service.ISysDataPermDefineService;
import com.zans.mms.util.Router;
import com.zans.mms.vo.SysRolePermVO;
import com.zans.mms.vo.baseorg.BaseOrgRepVO;
import com.zans.mms.vo.common.TreeSelect;
import com.zans.mms.vo.perm.DataPermCacheVO;
import com.zans.mms.vo.perm.DataPermMenuVO;
import com.zans.mms.vo.perm.DataPermVO;
import com.zans.mms.vo.perm.PermissionRespVO;
import com.zans.mms.vo.role.RoleEditVO;
import com.zans.mms.vo.role.RolePermVO;
import com.zans.mms.vo.user.MenuItem;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.*;

import static com.zans.base.config.GlobalConstants.*;


/**
 * 权限服务
 *
 * @author xv
 * @since 2020/3/6 13:28
 */
@Service("permissionService")
@Slf4j
public class PermissionServiceImpl extends BaseServiceImpl<SysPermission> implements IPermissionService {

    SysPermissionMapper permissionMapper;

    @Autowired
    IModuleService moduleService;

    @Autowired
    private LocalCacheConfig localCacheConfig;

    @Autowired
    PermissionHelper permissionHelper;

    @Autowired
    ISysDataPermDefineService sysDataPermDefineService;

    @Autowired
    private IBaseOrgService baseOrgService;

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
//    @Cacheable(cacheNames = "PERM_ROUTE_MAP", key = "#userId")
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
    public List<MenuItem> getMenuTreeByUserId(Integer userId,int status,Integer moduleType) {
        if(status == 0){
            List<MenuItem> itemList = localCacheConfig.getMenuItemCache(userId);
            if(itemList!=null && itemList.size()>0){
                return itemList;
            }
        }
        List<SysModule> menuList = moduleService.findMenuList(moduleType);
        Router router = this.getUserFrontRoute(userId);
        List<MenuItem> result = new LinkedList<>();

        menuList.removeIf(menu ->
                (menu.getMenuType() == SYS_MODULE_MENU_TYPE_MENU &&
                !menu.getRoute().equals(MENU_HOME_PATH) ) ||
                !router.isValid(menu.getRoute()) );

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
        localCacheConfig.putMenuItemCache(userId,result);
        return result;
    }

    @Override
    @CachePut(cacheNames = "PERM_API_MENU", key = "#userId")
    public List<MenuItem> getNoCacheMenuTreeByUserId(Integer userId) {
        List<SysModule> menuList = moduleService.findMenuList(null);

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
    public List<PermissionRespVO> findPermissionByRole(String roleId, Integer moduleType) {
        return permissionMapper.findPermissionByRole(roleId,moduleType);
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
       return this.findRolePermissionInTree(null,null);
    }


    @Override
    public List<TreeSelect> findAllModuleInTree() {
        List<SysModule> menuList = moduleService.findMenuList(null);
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
    @Transactional(rollbackFor = Exception.class)
    public void updateRolePermission(RoleEditVO vo) {
        this.permissionMapper.deletePermissionByRole(vo.getRoleId(),vo.getModuleType());
        for (Integer permId : vo.getCheckedPermission()) {
            this.permissionMapper.insertRolePerm(vo.getRoleId(), permId);
        }
        for (SysRolePermVO perm : vo.getCheckedDataPerm()) {
            String dataPerm = perm.getDataPermDesc();
            if (StringUtils.isEmpty(dataPerm)){
                continue;
            }
            Integer dataPermNum = 0;
            String[] permNums = dataPerm.split(",");
            for (String s : permNums) {
                dataPermNum += Integer.parseInt(s);
            }
            permissionMapper.updateDataPerm(vo.getRoleId(),perm.getPermId(), dataPermNum, dataPerm);
        }
        permissionHelper.removeRoleDataPerm(vo.getRoleId());
        permissionHelper.removeRoleRoute(vo.getRoleId());
        permissionHelper.removeRoleMenu(vo.getRoleId());
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

    @Override
    public DataPermVO getDataPermByUserAndPermId(UserSession userSession, Integer permId) {
        DataPermCacheVO permCacheVO = permissionHelper.getRoleDataPerm(userSession.getRoleId() , permId);
        if (null == permCacheVO ){
            List<DataPermCacheVO> rolePermList = this.findRolePermListByRoleId(userSession.getRoleId());
            permissionHelper.setRoleDataPerm(rolePermList);
            permCacheVO = permissionHelper.getRoleDataPerm(userSession.getRoleId() , permId);
        }
        if (null == permCacheVO || StringUtils.isEmpty(permCacheVO.getDataPermDesc())){
            throw new BusinessException("该角色无权限或未配置!!!");
        }
        DataPermVO vo = new DataPermVO();
        vo.setDataPerm(permCacheVO.getDataPerm());
//        vo.setDataPermDesc(permCacheVO.getDataPermDesc());
        vo.setDataPermList(permCacheVO.getDataPermDesc().split(","));
        return vo;
    }

    @Override
    public List<SysRolePerm> findDataPermList(String roleId) {
        RolePermVO vo = new RolePermVO();
        vo.setRoleId(roleId);
        List<SysRolePerm> allDataPermList = permissionMapper.findAllDataPermList();
        List<SysRolePerm> dataPermList = permissionMapper.findDataPermList(vo);
        if(dataPermList.size() == allDataPermList.size()) {
            return dataPermList;
        }
        Iterator<SysRolePerm> iterator = allDataPermList.iterator();
        while (iterator.hasNext()){
            SysRolePerm next = iterator.next();
            for (SysRolePerm rolePerm : dataPermList) {
                if (next.getPermId().equals(rolePerm.getPermId())){
                    iterator.remove();
                    continue;
                }
            }
        }
        dataPermList.addAll(allDataPermList);

        return dataPermList;
    }

    @Override
    public List<DataPermCacheVO> findRolePermListByRoleId(String roleNum) {
        return permissionMapper.findRolePermListByRoleId(roleNum);
    }

    @Override
    public List<String> getAllFrontRouteByUser(SysUser user) {
        List<String> roleRoute = permissionHelper.getRoleRoute(user.getRoleNum());
        if (null == roleRoute || roleRoute.size()==0) {
            List<String> routes = this.getAllFrontRouteByUserId(user.getId());
            permissionHelper.setRoleRoute(user.getRoleNum(),routes);
            return routes;
        }
        return roleRoute;
    }

    @Override
    public List<MenuItem> getMenuTreeByUser(SysUser user, int permCacheYes, Integer moduleType) {
        List<MenuItem> menus = permissionHelper.getRoleMenuItem(user.getRoleNum()+moduleType);
        if (null == menus || menus.size()==0){
            List<MenuItem> menuItems = this.getMenuTreeByUserId(user.getId(), permCacheYes, moduleType);
            permissionHelper.setRoleMenuItem(user.getRoleNum()+moduleType,menuItems);
            return menuItems;
        }
        return menus;
    }

    @Override
    public DataPermVO getTopDataPerm(UserSession userSession) {
        DataPermVO dataPermVO = new DataPermVO();
        BaseOrgRepVO baseOrgRepVO = baseOrgService.queryByOrgId(userSession.getOrgId());
        if(baseOrgRepVO == null){
            throw  new BusinessException("该角色无权限或未配置!!!");
        }
        //和北授确认 如果是运维系统，只能看自己单位的，否则可以看所有的
        if(StringHelper.equals(ROLE_MAINTAION,baseOrgRepVO.getOrgType())){
            dataPermVO.setDataPerm(2);
        }else {
            dataPermVO.setDataPerm(1);
        }
        return dataPermVO;
    }

    private final String ROLE_MAINTAION = "03";


    @Override
    public List<TreeSelect> findRolePermissionInTree(String roleId, Integer moduleType) {
        List<SysModule> menuList = moduleService.findMenuList(moduleType) ;
        List<PermissionRespVO> permList = this.findAllPermissions(null);
        List<TreeSelect> treeList = new ArrayList<>(menuList.size());

        // 初始化非叶子节点
        Map<Integer, TreeSelect> treeMap = new HashMap<>(menuList.size() + permList.size());
        for (SysModule module : menuList) {
            PermissionRespVO vo = new PermissionRespVO();
            vo.fromModule(module);
            TreeSelect node = TreeSelect.builder().id(module.getId()).label(module.getName()).data(vo).dataPermFlag(false).build();
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
            boolean dataPermFlag = false;
            List<DataPermMenuVO> defineList = null;
            //设置数据权限按钮
            if (1 == vo.getDataPermStatus().intValue()){
                dataPermFlag = true;
                defineList = sysDataPermDefineService.getListByCondition(new HashMap<String,Object>(){{
                    put("permId",vo.getPermId());
                    put("roleId",roleId);
                }});
            }
            TreeSelect node = TreeSelect.builder().id(vo.getPermId()).label(vo.getPermName()).data(vo).dataPermFlag(dataPermFlag).dataPermDesc(defineList).build();

//            if (DATA_PERM_SET.contains(vo.getPermId())){
//                node.setDataPermFlag(true);
//            }
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
}
