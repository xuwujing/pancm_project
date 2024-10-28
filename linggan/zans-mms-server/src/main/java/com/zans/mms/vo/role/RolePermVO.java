package com.zans.mms.vo.role;

import lombok.Data;

import java.util.Set;

@Data
public class RolePermVO {
    private String roleId;

    private String jurisdictionId;
    private Set<Integer> permIds;
}
