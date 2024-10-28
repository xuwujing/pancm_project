package com.zans.portal.dao;

import com.zans.portal.model.SysPermission;
import com.zans.portal.vo.perm.PermissionRespVO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

@Repository
public interface SysPermissionMapper extends Mapper<SysPermission> {

    List<String> findUserFrontRoutes(@Param("userId") Integer userId);

    List<String> findUserApiRoutes(@Param("userId") Integer userId);

    List<PermissionRespVO> findPermissionByRole(@Param("roleId") Integer roleId);

    PermissionRespVO findPermissionByName(@Param("name") String permName);

    PermissionRespVO findPermissionByNameExceptId(@Param("name") String name, @Param("id") Integer id);

    List<PermissionRespVO> findAllPermissions(@Param("permName")String permName);

    int deletePermissionByRole(@Param("roleId") Integer roleId);

    int insertRolePerm(@Param("roleId") Integer roleId, @Param("permId") Integer permId);

    int findByModuleId(@Param("moduleId") Integer moduleId);

    String findByRouteAndUserId(@Param("route") String route);
}