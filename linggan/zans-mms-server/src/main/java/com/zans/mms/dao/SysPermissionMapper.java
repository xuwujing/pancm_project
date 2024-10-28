package com.zans.mms.dao;

import com.zans.mms.model.SysPermission;
import com.zans.mms.model.SysRolePerm;
import com.zans.mms.vo.perm.DataPermCacheVO;
import com.zans.mms.vo.perm.DataPermVO;
import com.zans.mms.vo.perm.PermissionRespVO;
import com.zans.mms.vo.role.RolePermVO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

@Repository
public interface SysPermissionMapper extends Mapper<SysPermission> {

    List<String> findUserFrontRoutes(@Param("userId") Integer userId);

    List<String> findUserApiRoutes(@Param("userId") Integer userId);

    List<PermissionRespVO> findPermissionByRole(@Param("jurisdictionId") String jurisdictionId, @Param("moduleType") Integer moduleType);

    PermissionRespVO findPermissionByName(@Param("name") String permName);

    PermissionRespVO findPermissionByNameExceptId(@Param("name") String name, @Param("id") Integer id);

    List<PermissionRespVO> findAllPermissions(@Param("permName")String permName);

    int deletePermissionByRole(@Param("jurisdictionId") String jurisdictionId,@Param("moduleType") Integer moduleType);

    int insertRolePerm(@Param("jurisdictionId") String jurisdictionId, @Param("permId") Integer permId);

    int findByModuleId(@Param("moduleId") Integer moduleId);

    String findByRouteAndUserId(@Param("route") String route);

    DataPermVO getDataPermByUserNameAndPermId(@Param("userName")String userName, @Param("permId")Integer permId);

    List<SysRolePerm> findDataPermList(RolePermVO vo);

    void updateDataPerm(@Param("jurisdictionId") String jurisdictionId, @Param("permId") Integer permId, @Param("dataPerm") Integer dataPerm, @Param("dataPermDesc")String perm);

    List<DataPermCacheVO> findRolePermListByRoleId(@Param("jurisdictionId") String jurisdictionId);

    List<SysRolePerm> findAllDataPermList();


}
