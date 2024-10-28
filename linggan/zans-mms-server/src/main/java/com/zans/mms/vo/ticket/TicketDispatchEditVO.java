package com.zans.mms.vo.ticket;

import com.alibaba.fastjson.JSONObject;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 工单派工单详情(TicketsDispatchViewRespVO)实体类
 *
 * @author beixing
 * @since 2021-01-16 12:20:44
 */
@ApiModel(value = "ticketDispatchEditVO", description = "工单结算表")
@Data
public class TicketDispatchEditVO implements Serializable {
    private static final long serialVersionUID = -27477630671959084L;

    /**
     * @Author pancm
     * @Description ticketId
     * 工单id
     * @Date  2021/3/7
     * @Param
     * @return
     **/
    @ApiModelProperty(value = "id")
    private Long id;


    private Integer opType;

    private String opPlatform;
    /**
     * ip地址
     */
    private String opIpaddr;
    /**
     * 创建者
     */
    private String creator;

    private Integer ticketStatus;
    /**
     * 派工状态
     */
    private Integer dispatchStatus;
    /**
     * 验证状态
     */
    private Integer acceptStatus;

    private List<TicketBaseMfRespVO> baseMfRespVOList;


    @Override
    public String toString() {
        return JSONObject.toJSONString(this);
    }

}
