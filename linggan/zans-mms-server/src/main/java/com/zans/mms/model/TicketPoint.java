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
 * (TicketPoint)实体类
 *
 * @author beixing
 * @since 2021-03-02 17:24:47
 */
@ApiModel(value = "TicketPoint", description = "")
@Data
@Table(name = "ticket_point")
public class TicketPoint implements Serializable {
    private static final long serialVersionUID = 555561100257317179L;
    @Column(name = "id")
    @ApiModelProperty(value = "${column.comment}")
    private Long id;
    @Column(name = "ticket_id")
    @ApiModelProperty(value = "${column.comment}")
    private Long ticketId;
    @Column(name = "point_id")
    @ApiModelProperty(value = "${column.comment}")
    private Long pointId;
    @Column(name = "creator")
    @ApiModelProperty(value = "${column.comment}")
    private String creator;
    @Column(name = "create_time")
    @ApiModelProperty(value = "${column.comment}")
    private Date createTime;
    @Column(name = "update_time")
    @ApiModelProperty(value = "${column.comment}")
    private Date updateTime;

    private String pointName;
    @Override
    public String toString() {
        return JSONObject.toJSONString(this);
    }

}
