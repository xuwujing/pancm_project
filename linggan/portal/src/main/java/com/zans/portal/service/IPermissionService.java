package com.zans.portal.service;

import com.zans.base.service.BaseService;
import com.zans.portal.model.SysPermission;
import com.zans.portal.util.Router;
import com.zans.portal.vo.common.TreeSelect;
import com.zans.portal.vo.perm.PermissionRespVO;
import com.zans.portal.vo.user.MenuItem;

import java.util.List;

/**
 * @author xv
 * @since 2020/3/6 13:27
 */
public interface IPermissionService extends BaseService<SysPermission> {

    /**
     * 前端路径是否合法
     * @param route 前端路由
     * @param userId 用户id
     * @return true，合法；false，无权限
     */
    boolean isFrontRouteValid(String route, Integer userId);

    /**
     * 后端路径是否合法
     * @param api 后端api路径
     * @param userId 用户id
     * @return true，合法；false，无权限
     */
    boolean isApiValid(String api, Integer userId);

    /**
     * 获得用户的前端合法路由
     * @param userId 用户id
     * @return 路由集合
     */
    Router getUserFrontRoute(Integer userId);

    /**
     * 获得用户的后端合法路由
     * @param userId 用户id
     * @return 路由集合
     */
    Router getUserApiRoute(Integer userId);

    /**
     * 生成用的权限树
     * @param userId 用户id
     * @return 菜单对象
     */
    List<MenuItem> getMenuTreeByUserId(Integer userId,int status);

    List<String> getAllFrontRouteByUserId(Integer userId);

    List<PermissionRespVO> findAllPermissions(String permName);

    List<PermissionRespVO> findPermissionByRole(Integer roleId);

    PermissionRespVO findPermissionByName(String permName);

    PermissionRespVO findPermissionByNameExceptId(String name, Integer id);

    List<TreeSelect> findAllPermissionInTree();

    List<TreeSelect> findAllPermissionInTree(String permName);

    List<TreeSelect> findAllModuleInTree();

    /**
     * 更新角色、权限
     * @param roleId 角色编号
     * @param permIds 权限编号列表
     */
    void updateRolePermission(Integer roleId, List<Integer> permIds);

    int findByModuleId(Integer moduleId);

    String getpasswdPermission(String route);

    List<MenuItem> getNoCacheMenuTreeByUserId(Integer userId);

}
