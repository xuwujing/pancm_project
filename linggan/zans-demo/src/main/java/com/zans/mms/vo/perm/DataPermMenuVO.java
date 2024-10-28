package com.zans.mms.vo.perm;

import lombok.Data;

@Data
public class DataPermMenuVO {
    /**
     * 	p.perm_id permId,
     * 	data_name dataName,
     * 	data_value dataValue,
     * 	data_group dataGroup,
     * 	role_id roleId,
     * 	data_perm dataPerm,
     * 	data_perm_desc,
     * 	FIND_IN_SET( data_value, data_perm_desc ) checkStatus
     */
    private Integer permId;
    private String dataName;
    private Integer dataValue;
    private Integer dataGroup;
    private String roleId;
    private Integer dataPerm;
    private String data_perm_desc;
    private Integer checkStatus;

}
