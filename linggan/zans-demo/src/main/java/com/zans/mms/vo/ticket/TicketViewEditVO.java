package com.zans.mms.vo.ticket;

import com.alibaba.fastjson.JSONObject;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 工单详情(TicketsDispatchViewRespVO)实体类
 *
 * @author beixing
 * @since 2021-01-16 12:20:44
 */
@ApiModel(value = "TicketViewEditVO", description = "工单详情")
@Data
public class TicketViewEditVO implements Serializable {
    private static final long serialVersionUID = -27477630671959084L;

    @ApiModelProperty(value = "id")
    private Long id;

    @ApiModelProperty(value = "实际故障类型")
    private Integer practicalIssueType;




    @Override
    public String toString() {
        return JSONObject.toJSONString(this);
    }

}
