package com.zans.mms.dao.mms;

import com.zans.base.vo.SelectVO;
import com.zans.mms.model.BaseOrgRole;
import com.zans.mms.vo.role.RoleRespVO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

@Repository
public interface BaseOrgRoleMapper extends Mapper<BaseOrgRole> {
    List<SelectVO> orgRoleList();

    List<SelectVO> getRoleBMaintain(String maintainNum);

    RoleRespVO findRoleById(String roleId);

    List<RoleRespVO> findAllRoles(@Param("roleName")String roleName);
}
