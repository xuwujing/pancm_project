package com.zans.mms.vo.ticket;

import com.alibaba.fastjson.JSONObject;
import com.zans.base.vo.BasePage;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 工单表(Tickets)实体类
 *
 * @author beixing
 * @since 2021-01-13 18:14:25
 */
@ApiModel(value = "ticketSearchReqVO", description = "工单表")
@Data
public class TicketSearchReqVO extends BasePage implements Serializable {
    private static final long serialVersionUID = -95213275681902737L;

    @ApiModelProperty(value = "自增id")
    private Long id;
    /**
     * 工单编码
     */
    @ApiModelProperty(value = "工单编码")
    private String ticketCode;
    /**
     * 工单类型
     */
    @ApiModelProperty(value = "工单类型")
    private Integer ticketType;
    /**
     * 辖区id
     */
    @ApiModelProperty(value = "辖区id")
    private List<String> areaId;
    /**
     * 设备类型
     */
    @ApiModelProperty(value = "设备类型")
    private String deviceType;
    /**
     * 故障类型
     */
    @ApiModelProperty(value = "故障类型")
    private List<Integer> issueType;


    /**
     * 分配单位代码
     */
    @ApiModelProperty(value = "分配单位代码")
    private String allocDepartmentNum;

    /**
     * 工单状态
     */
    @ApiModelProperty(value = "工单状态")
    private Integer ticketStatus;

    @ApiModelProperty(value = "维修状态")
    private Integer maintenanceStatus;

    @ApiModelProperty(value = "数据权限  1 全部  2单位  3个人 ",name = " 数据权限  1 全部  2单位  3个人 ")
    private Integer dataPerm;

    private String orgId;

    private Integer editStatus;


    private String creator;

    private String searchLike;

    private String longitude;

    private String latitude;

    /** 分配权限 组织，1是，0否*/
    private Integer permOrg;
    /** 分配权限 个人 1是，0否*/
    private Integer permSelf;
    /** 分配权限 工单未分配 1是，0否*/
    private Integer permUnAlloc;

    /**
     * 权限sql注入
     */
    private String permSql;

    @Override
    public String toString() {
        return JSONObject.toJSONString(this);
    }



}
