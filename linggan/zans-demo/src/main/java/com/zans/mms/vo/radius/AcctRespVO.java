package com.zans.mms.vo.radius;


import com.alibaba.fastjson.annotation.JSONField;
import com.zans.base.util.DateHelper;
import com.zans.base.util.StringHelper;
import com.zans.mms.model.RadiusAcct;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.beans.BeanUtils;
import org.springframework.util.StringUtils;

@ApiModel
@Data
public class AcctRespVO {

    @ApiModelProperty(name = "rad_acct_id", value = "rad_acct_id")
    @JSONField(name = "rad_acct_id")
    private Long radAcctId;

    @ApiModelProperty(name = "server_id", value = "server_id")
    @JSONField(name = "server_id")
    private Integer serverId;

    @ApiModelProperty(name = "acct_session_id", value = "会话id")
    @JSONField(name = "acct_session_id")
    private String acctSessionId;

    @ApiModelProperty(name = "username", value = "mac地址")
    @JSONField(name = "username")
    private String username;

    @ApiModelProperty(name = "nas_ip_address", value = "nas ip")
    @JSONField(name = "nas_ip_address")
    private String nasIpAddress;

    @ApiModelProperty(name = "nas_port_id", value = "nas端口")
    @JSONField(name = "nas_port_id")
    private String nasPortId;

    @ApiModelProperty(name = "acct_start_time", value = "计费开始时间")
    @JSONField(name = "acct_start_time", format = "yyyy-MM-dd HH:mm:ss")
    private String acctStartTime;

    @ApiModelProperty(name = "acct_update_time", value = "更新时间")
    @JSONField(name = "acct_update_time", format = "yyyy-MM-dd HH:mm:ss")
    private String acctUpdateTime;

    @ApiModelProperty(name = "acct_stop_time", value = "结束时间")
    @JSONField(name = "acct_stop_time", format = "yyyy-MM-dd HH:mm:ss")
    private String acctStopTime;

    @ApiModelProperty(name = "acct_session_time", value = "在线时长")
    @JSONField(name = "acct_session_time")
    private String acctSessionTime;

    @ApiModelProperty(name = "acct_input_octets", value = "流入流量")
    @JSONField(name = "acct_input_octets")
    private String acctInputOctets;

    @ApiModelProperty(name = "acct_output_octets", value = "流出流量")
    @JSONField(name = "acct_output_octets")
    private String acctOutputOctets;

    @ApiModelProperty(name = "framed_ip_address", value = "IP地址")
    @JSONField(name = "framed_ip_address")
    private String framedIpAddress;

    @ApiModelProperty(name = "acct_terminate_cause", value = "结束原因")
    @JSONField(name = "acct_terminate_cause")
    private String acctTerminateCause;

    @ApiModelProperty(name = "called_station_id", value = "NAS mac地址")
    @JSONField(name = "called_station_id")
    private String calledStationId;

    @ApiModelProperty(name = "nas_port_type", value = "NAS 接口类型")
    @JSONField(name = "nas_port_type")
    private String nasPortType;

    @ApiModelProperty(name = "nas_name", value = "NAS 名称")
    @JSONField(name = "nas_name")
    private String nasName;

    public static AcctRespVO init(RadiusAcct acct) {
        AcctRespVO respVO = new AcctRespVO();
//        respVO.setRadAcctId(acct.getRadAcctId());
//        respVO.setServerId(acct.getServerId());
//        respVO.setAcctSessionId(acct.getAcctSessionId());
//        respVO.setUsername(acct.getUsername());
//        respVO.setNasIpAddress(acct.getNasIpAddress());
//        respVO.setNasPortId(acct.getNasPortId());
        BeanUtils.copyProperties(acct, respVO);

        respVO.setAcctStartTime(DateHelper.getDateTime(acct.getAcctStartTime()));
        respVO.setAcctUpdateTime(DateHelper.getDateTime(acct.getAcctUpdateTime()));
        respVO.setAcctStopTime(DateHelper.getDateTime(acct.getAcctStopTime()));

        if (!StringUtils.isEmpty(acct.getAcctSessionTime())) {
            String second = StringHelper.secondToTime(String.valueOf(acct.getAcctSessionTime()));
            respVO.setAcctSessionTime(second);
        }
        Long inputOctets = acct.getAcctInputOctets();
        if (inputOctets != null) {
            String acctInput = StringHelper.getNetFileSizeDescription(inputOctets);
            respVO.setAcctInputOctets(acctInput);
        }
        Long outputOctets = acct.getAcctOutputOctets();
        if (outputOctets != null) {
            String acctOutput = StringHelper.getNetFileSizeDescription(outputOctets);
            respVO.setAcctOutputOctets(acctOutput);
        }
        return respVO;
    }

}
