package com.zans.vo;

import com.alibaba.fastjson.JSONObject;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * (DeviceCipherApprove)实体类
 *
 * @author beixing
 * @since 2021-08-23 16:23:49
 */
@Data
@Table(name = "device_cipher_approve")
@NoArgsConstructor
@AllArgsConstructor
public class DeviceCipherApproveVO extends BasePage implements Serializable {
    private static final long serialVersionUID = -94011297352996301L;
    @Column(name = "id")
    private Integer id;
    /**
     * 设备ip
     */
    @Column(name = "ip")
    private String ip;
    /**
     * 审批人
     */
    @Column(name = "propos_user")
    private String proposUser;
    /**
     * 审批人
     */
    @Column(name = "approve_user")
    private String approveUser;
    /**
     * 1申请中，2，通过，3，驳回
     */
    @Column(name = "approve_status")
    private Integer approveStatus;
    /**
     * 备注
     */
    @Column(name = "remark")
    private String remark;
    @Column(name = "create_time")
    private String createTime;
    @Column(name = "update_time")
    private String updateTime;

    private String timeExplain;


    @Override
    public String toString() {
        return JSONObject.toJSONString(this);
    }

    public DeviceCipherApproveVO(String ip, String approveUser) {
        this.ip = ip;
        this.approveUser = approveUser;
    }
}
