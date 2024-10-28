package com.zans.vo;

import com.alibaba.fastjson.JSONObject;
import lombok.Data;

import java.io.Serializable;

/**
 * (DeviceCipherApprove)实体类
 *
 * @author beixing
 * @since 2021-08-23 16:23:48
 */
@Data
public class DeviceCipherApprove extends BasePage implements Serializable {
    private static final long serialVersionUID = -29775760662591109L;
    private Integer id;
    /**
     * 设备ip
     */
    private String ip;
    /**
     * 审批人
     */
    private String proposUser;
    /**
     * 审批人
     */
    private String approveUser;
    /**
     * 1申请中，2，通过，3，驳回
     */
    private Integer approveStatus;
    /**
     * 备注
     */
    private String remark;
    private String createTime;
    private String updateTime;


    @Override
    public String toString() {
        return JSONObject.toJSONString(this);
    }

}
