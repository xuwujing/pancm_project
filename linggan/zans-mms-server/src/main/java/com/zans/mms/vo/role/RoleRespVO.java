package com.zans.mms.vo.role;

import com.zans.mms.model.SysRolePerm;
import com.zans.mms.vo.perm.PermissionRespVO;
import lombok.Data;

import javax.persistence.Column;
import java.util.List;

/**
 * @author xv
 * @since 2020/3/6 23:31
 */
@Data
public class RoleRespVO {

    @Column(name = "role_id")
    private String roleId;

    @Column(name = "jurisdiction_id")
    private String jurisdictionId;

    @Column(name = "role_name")
    private String roleName;

    @Column(name = "role_desc")
    private String roleDesc;
    @Column(name = "remark")
    private String remark;

    @Column(name = "perm_list")
    private List<PermissionRespVO> permList;

    @Column(name = "checked_keys")
    private List<Integer> checkedPermission;

    @Column(name = "checkedDataPerm")
    private List<SysRolePerm> checkedDataPerm;



}
