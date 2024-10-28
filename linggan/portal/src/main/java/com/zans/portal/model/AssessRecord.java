package com.zans.portal.model;

import com.alibaba.fastjson.JSONObject;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.List;

/**
 * 审核申述表(AssessRecord)实体类
 *
 * @author beixing
 * @since 2021-11-03 17:40:27
 */
@Data
@Table(name = "assess_record")
public class AssessRecord implements Serializable {
    private static final long serialVersionUID = 728342706370915377L;
    @Column(name = "id")
    private Integer id;
    /**
     * 交换机IP
     */
    @Column(name = "sw_ip")
    private String swIp;
    /**
     * 申请理由
     */
    @Column(name = "state_remark")
    private String stateRemark;

    /**
     * 审批理由
     */
    @Column(name = "approve_remark")
    private String approveRemark;

    /**
     * 申请状态待审核 /  审核通过  / 驳回申请
     */
    @Column(name = "status")
    private Integer status;
    /**
     * 附件编号
     */
    @Column(name = "adjunct_id")
    private String adjunctId;

    /**
     * 状态开始时间
     */
    @Column(name = "begin_time")
    private String beginTime;
    /**
     * 状态结束时间
     */
    @Column(name = "end_time")
    private String endTime;
    /**
     * 创建时间
     */
    @Column(name = "create_time")
    private String createTime;
    /**
     * 更新时间
     */
    @Column(name = "update_time")
    private String updateTime;

    @Column(name = "link_remark")
    private String linkRemark;

    @Column(name = "network_scan_id")
    private Integer networkScanId;

    @Column(name = "state_user")
    private String stateUser;

    @Column(name = "approve_user")
    private String approveUser;

    private List<BaseVfs> adjunctList;

    @Override
    public String toString() {
        return JSONObject.toJSONString(this);
    }

}
