package com.zans.mms.service;

import com.zans.base.service.BaseService;
import com.zans.base.vo.UserSession;
import com.zans.mms.model.SysPermission;
import com.zans.mms.model.SysRolePerm;
import com.zans.mms.model.SysUser;
import com.zans.mms.util.Router;
import com.zans.mms.vo.common.TreeSelect;
import com.zans.mms.vo.perm.DataPermCacheVO;
import com.zans.mms.vo.perm.DataPermVO;
import com.zans.mms.vo.perm.PermissionRespVO;
import com.zans.mms.vo.role.RoleEditVO;
import com.zans.mms.vo.user.MenuItem;

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
    List<MenuItem> getMenuTreeByUserId(Integer userId,int status,Integer moduleType);

    List<String> getAllFrontRouteByUserId(Integer userId);

    List<PermissionRespVO> findAllPermissions(String permName);

    List<PermissionRespVO> findPermissionByRole(String roleId, Integer moduleType);

    PermissionRespVO findPermissionByName(String permName);

    PermissionRespVO findPermissionByNameExceptId(String name, Integer id);

    List<TreeSelect> findAllPermissionInTree();



    List<TreeSelect> findAllModuleInTree();

    /**
     * 更新角色、权限
     */
    void updateRolePermission(RoleEditVO vo);

    int findByModuleId(Integer moduleId);

    String getpasswdPermission(String route);

    List<MenuItem> getNoCacheMenuTreeByUserId(Integer userId);

    DataPermVO getDataPermByUserAndPermId(UserSession userSession, Integer permId);

    List<SysRolePerm> findDataPermList(String roleId);

    List<DataPermCacheVO> findRolePermListByRoleId(String roleNum);

    List<String> getAllFrontRouteByUser(SysUser user);

    List<MenuItem> getMenuTreeByUser(SysUser user, int permCacheYes, Integer moduleTypePc);


    DataPermVO getTopDataPerm(UserSession userSession);

    List<TreeSelect> findRolePermissionInTree(String id, Integer moduleType);
}
