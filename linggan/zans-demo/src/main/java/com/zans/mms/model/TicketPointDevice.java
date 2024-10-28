package com.zans.mms.model;

import com.alibaba.fastjson.JSONObject;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

/**
 * (TicketPointDevice)实体类
 *
 * @author beixing
 * @since 2021-03-03 10:52:23
 */
@ApiModel(value = "TicketPointDevice", description = "")
@Data
@Table(name = "ticket_point_device")
public class TicketPointDevice implements Serializable {
    private static final long serialVersionUID = -37845171816092191L;
    @Column(name = "id")
    @ApiModelProperty(value = "${column.comment}")
    private Long id;
    @Column(name = "ticket_id")
    @ApiModelProperty(value = "${column.comment}")
    private Long ticketId;
    @Column(name = "point_id")
    @ApiModelProperty(value = "${column.comment}")
    private Long pointId;
    @Column(name = "asset_id")
    @ApiModelProperty(value = "${column.comment}")
    private Long assetId;
    @Column(name = "creator")
    @ApiModelProperty(value = "${column.comment}")
    private String creator;
    @Column(name = "create_time")
    @ApiModelProperty(value = "${column.comment}")
    private Date createTime;
    @Column(name = "update_time")
    @ApiModelProperty(value = "${column.comment}")
    private Date updateTime;


    @Override
    public String toString() {
        return JSONObject.toJSONString(this);
    }

}
