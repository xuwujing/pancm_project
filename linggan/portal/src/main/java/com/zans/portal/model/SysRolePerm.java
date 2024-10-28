package com.zans.portal.model;

import java.io.Serializable;
import javax.persistence.*;

@Table(name = "sys_role_perm")
public class SysRolePerm implements Serializable {
    @Id
    @Column(name = "role_id")
    private Integer roleId;

    @Id
    @Column(name = "perm_id")
    private Integer permId;

    private static final long serialVersionUID = 1L;

    /**
     * @return role_id
     */
    public Integer getRoleId() {
        return roleId;
    }

    /**
     * @param roleId
     */
    public void setRoleId(Integer roleId) {
        this.roleId = roleId;
    }

    /**
     * @return perm_id
     */
    public Integer getPermId() {
        return permId;
    }

    /**
     * @param permId
     */
    public void setPermId(Integer permId) {
        this.permId = permId;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", roleId=").append(roleId);
        sb.append(", permId=").append(permId);
        sb.append("]");
        return sb.toString();
    }
}