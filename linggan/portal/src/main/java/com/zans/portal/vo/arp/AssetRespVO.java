package com.zans.portal.vo.arp;

import com.alibaba.fastjson.annotation.JSONField;
import com.zans.portal.vo.deny.DenyVO;
import com.zans.portal.vo.ip.IpVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;
import java.util.List;
import java.util.Map;

@ApiModel
@Data
public class AssetRespVO {

    private Integer id;

    @ApiModelProperty(name = "endpoint_id", value = "扫描设备ID")
//    @JSONField(name = "endpoint_id")
    private Integer endpointId;

    @ApiModelProperty(name = "ip_addr", value = "IP地址")
//    @JSONField(name = "ip_addr")
    private String ipAddr;

    @ApiModelProperty(name = "mac", value = "MAC地址")
//    @JSONField(name = "mac_addr")
    private String mac;

    @ApiModelProperty(name = "region_first", value = "大区ID")
//    @JSONField(name = "region_first")
    private Integer regionFirst;


//    @JSONField(name = "region_first_name")
    @ApiModelProperty(name = "region_first_name", value = "大区名称")
    private String regionFirstName;

    @ApiModelProperty(name = "region_second", value = "小区ID")
//    @JSONField(name = "region_second")
    private Integer regionSecond;

//    @JSONField(name = "region_second_name")
    @ApiModelProperty(name = "region_second_name", value = "小区名称")
    private String regionSecondName;

//    @JSONField(name = "device_type")
    @ApiModelProperty(name = "deviceType", value = "设备类型编号")
    private Integer deviceType;

    @ApiModelProperty(name = "deviceTypeDetect", value = "识别设备类型编号")
    private Integer deviceTypeDetect;



//    @JSONField(name = "device_type_name")
    @ApiModelProperty(name = "deviceTypeName", value = "设备类型名称")
    private String deviceTypeName;

    @ApiModelProperty(name = "deviceTypeNameDetect", value = "识别设备类型编号")
    private String deviceTypeNameDetect;

//    @JSONField(name = "model_des")
    @ApiModelProperty(name = "modelDes", value = "设备型号")
    private String modelDes;

    @ApiModelProperty(name = "modelDesDetect", value = "识别设备型号")
    private String modelDesDetect;


    @ApiModelProperty(name = "brand",value = "品牌")
    private String brandName;

    @ApiModelProperty(name = "brandDetect",value = "识别品牌")
    private String brandNameDetect;

    @ApiModelProperty(name = "detectType",value = "识别方式")
    private Integer detectType;

    @ApiModelProperty(name = "alive", value = "在线情况")
    private Integer alive;

//    @JSONField(name = "alive_name")
    @ApiModelProperty(name = "alive_name", value = "在线情况名称")
    private String aliveName;

    @ApiModelProperty(name = "enable_status", value = "状态")
    private Integer enableStatus;

//    @JSONField(name = "enable_status_name")
    @ApiModelProperty(name = "enable_status_name", value = "状态名称")
    private String enableStatusName;

    @ApiModelProperty(name = "source", value = "来源")
    private Integer source;

//    @JSONField(name = "source_name")
    @ApiModelProperty(name = "source_name", value = "来源名称")
    private String sourceName;

    @JSONField(name = "mute")
    @ApiModelProperty(name = "mute", value = "是否哑终端")
    private Integer mute;

//    @JSONField(name = "mute_name")
    @ApiModelProperty(name = "mute_name", value = "哑终端/活跃终端")
    private String muteName;

//    @JSONField(name = "dis_status")
    @ApiModelProperty(name = "dis_status", value = "地址分配")
    private Integer disStatus;

//    @JSONField(name = "dis_status_name")
    @ApiModelProperty(name = "dis_status_name", value = "地址分配名称")
    private String disStatusName;

    @ApiModelProperty(name = "company",value = "厂商")
    private String company;



//    @JSONField(name = "online_time", format = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(name = "online_time",value = "上线时间")
    private Date onlineTime;

    @JSONField(name = "aliveLastTime", format = "yyyy-MM-dd HH:mm:ss")
//    @ApiModelProperty(name = "alive_last_time",value = "最后上线时间")
    private Date aliveLastTime;

//    @JSONField(name = "arp_out_desc")
    @ApiModelProperty(name = "arp_out_desc",value = "入网路径")
    private String arpOutDesc;

//    @JSONField(name = "open_port")
    @ApiModelProperty(name = "open_port",value = "开放端口")
    private String openPort;

//    @JSONField(name = "cur_arp_out_desc")
    @ApiModelProperty(name = "cur_arp_out_desc",value = "当前入网路径")
    private String curArpOutDesc;

//    @JSONField(name = "cur_open_port")
    @ApiModelProperty(name = "cur_open_port",value = "当前开放端口")
    private String curOpenPort;

//    @JSONField(name = "cur_mac")
    @ApiModelProperty(name = "cur_mac",value = "当前Mac地址")
    private String curMacAddr;

//    @JSONField(name = "cur_model_des")
    @ApiModelProperty(name = "cur_model_des",value = "当前设备型号")
    private String curModelDes;

//    @JSONField(name = "cur_company")
    @ApiModelProperty(name = "cur_company",value = "当前公司")
    private String curCompany;

//    @JSONField(name = "server_os")
    @ApiModelProperty(name = "server_os",value = "服务器操作系统")
    private String serverOs;

    private String curServerOs;

//    @JSONField(name = "exception_not_deal")
    @ApiModelProperty(name = "exception_not_deal",value = "未处理异常记录")
    private String exceptionNotDeal;

//    @JSONField(name = "exception_block")
    @ApiModelProperty(name = "exception_block",value = "已处理异常记录")
    private String exceptionBlock;

//    @JSONField(name = "ip_status")
    @ApiModelProperty(name = "ip_status",value = "异常类型")
    private Integer ipStatus;

//    @JSONField(name = "ip_status_name")
    @ApiModelProperty(name = "ip_status_name",value = "异常类型名称")
    private String ipStatusName;

    @JSONField(name = "checkLastTime", format = "yyyy-MM-dd HH:mm:ss")
//    @ApiModelProperty(name = "check_last_time",value = "异常时间")
    private Date checkLastTime;

//    @JSONField(name = "arp_out_change")
    @ApiModelProperty(name = "arp_out_change",value = "入网路径异常")
    private Integer arpOutChange;

//    @JSONField(name = "mac_change")
    @ApiModelProperty(name = "mac_change",value = "MAC异常")
    private Integer macChange;

//    @JSONField(name = "open_port_change")
    @ApiModelProperty(name = "open_port_change",value = "端口异常")
    private Integer openPortChange;

//    @JSONField(name = "model_des_change")
    @ApiModelProperty(name = "model_des_change",value = "模型异常")
    private Integer modelDesChange;

//    @JSONField(name = "change_brief")
    @ApiModelProperty(name = "change_brief",value = "异常信息摘要")
    private String changeBrief;

//    @JSONField(name = "mac_addr_block")
    @ApiModelProperty(name = "mac_addr_block",value = "实际阻断MAC")
    private String macAddrBlock;

//    @JSONField(name = "dealLastTime" , format = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(name = "deal_last_time",value = "处置时间")
    private Date dealLastTime;

//    @JSONField(name = "deal_last_mark")
    @ApiModelProperty(name = "deal_last_mark",value = "处置意见")
    private String dealLastMark;

//    @JSONField(name = "risk_type")
    @ApiModelProperty(name = "risk_type", value = "风险类型")
    private Integer riskType;

//    @JSONField(name = "risk_type_name")
    @ApiModelProperty(name = "risk_type_name", value = "风险类型名称")
    private String riskTypeName;

//    @JSONField(name = "project_name")
    @ApiModelProperty(name = "project_name", value = "项目名称")
    private String projectName;

//    @JSONField(name = "point_name")
    @ApiModelProperty(name = "point_name", value = "点位名称")
    private String pointName;

//    @JSONField(name = "project_status")
    @ApiModelProperty(name = "project_status", value = "项目维护状态")
    private Integer projectStatus;

//    @JSONField(name = "project_status_name")
    @ApiModelProperty(name = "project_status_name", value = "项目维护状态名称")
    private String projectStatusName;

//    @JSONField(name = "confirm_status")
    @ApiModelProperty(name = "confirm_status", value = "档案确认状态")
    private Integer confirmStatus;

//    @JSONField(name = "confirm_status_name")
    @ApiModelProperty(name = "confirm_status_name", value = "档案确认状态名称")
    private String confirmStatusName;

    @JSONField(name = "contractor")
    @ApiModelProperty(name = "contractor", value = "承建单位")
    private String contractor;

//    @JSONField(name = "contractor_person")
    @ApiModelProperty(name = "contractor_person", value = "承建联系人")
    private String contractorPerson;

//    @JSONField(name = "contractor_phone")
    @ApiModelProperty(name = "contractor_phone", value = "承建电话")
    private String contractorPhone;

//    @JSONField(name = "maintain_company")
    @ApiModelProperty(name = "maintain_company", value = "维护公司")
    private String maintainCompany;

//    @JSONField(name = "maintain_person")
    @ApiModelProperty(name = "maintain_person", value = "维护联系人")
    private String maintainPerson;

//    @JSONField(name = "maintain_phone")
    @ApiModelProperty(name = "maintain_phone", value = "维护电话")
    private String maintainPhone;

//    @JSONField(name = "exception_not_deal_list")
    @ApiModelProperty(name = "exception_not_deal_list",value = "未处理异常记录")
    private List<DenyVO> exceptionNotDealList;

//    @JSONField(name = "exception_block_list")
    @ApiModelProperty(name = "exception_block_list",value = "已处理异常记录")
    private List<DenyVO> exceptionBlockList;

    @JSONField(name = "createTime", format = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(name = "create_time",value = "创建时间")
    private Date createTime;

//    @JSONField(name = "create_person")
    @ApiModelProperty(name = "create_person",value = "创建人")
    private String createPerson;

//    @JSONField(name = "maintain_status")
    @ApiModelProperty(name = "maintain_status",value = "维护状态")
    private String maintainStatus;

//    @JSONField(name = "maintain_status_name")
    @ApiModelProperty(name = "maintain_status_name",value = "维护状态名称")
    private String maintainStatusName;

    @JSONField(name = "ipVO")
    @ApiModelProperty(name = "ipVO", value = "ipVO")
    private IpVO ipVO;

    /**
     * 当前使用改IP设备数量
     */
//    @JSONField(name = "cur_alive_device_num")
    @ApiModelProperty(name = "cur_alive_device_num", value = "当前使用改IP设备数量")
    private Integer curAliveDeviceNum = 0;

    @JSONField(name = "col1")
    @ApiModelProperty(name = "col1", value = "col1")
    private String col1;

    @JSONField(name = "col2")
    @ApiModelProperty(name = "col2", value = "col2")
    private String col2;

    @JSONField(name = "col13")
    @ApiModelProperty(name = "col13", value = "col13")
    private String col13;

    @JSONField(name = "col4")
    @ApiModelProperty(name = "col4", value = "col4")
    private String col4;

    @JSONField(name = "col5")
    @ApiModelProperty(name = "col5", value = "col5")
    private String col5;

    @JSONField(name = "col6")
    @ApiModelProperty(name = "col6", value = "col6")
    private String col6;

    @JSONField(name = "col7")
    @ApiModelProperty(name = "col7", value = "col7")
    private String col7;

    @JSONField(name = "col8")
    @ApiModelProperty(name = "col8", value = "col8")
    private String col8;

    @JSONField(name = "col9")
    @ApiModelProperty(name = "col9", value = "col9")
    private String col9;

    @JSONField(name = "col10")
    @ApiModelProperty(name = "col10", value = "col10")
    private String col10;

    /**
     *         a.department_id,
     *         a.longitude,
     *         a.latitude,
     */
//    @JSONField(name = "department_id")
    @ApiModelProperty(name = "department_id", value = "单位id")
    private Integer departmentId;

    @JSONField(name = "departmentName")
    @ApiModelProperty(name = "departmentName", value = "单位名称")
    private String departmentName;

    @JSONField(name = "longitude")
    @ApiModelProperty(name = "longitude", value = "经度")
    private String longitude;

    @JSONField(name = "latitude")
    @ApiModelProperty(name = "latitude", value = "纬度")
    private String latitude;


    private String username;
    private String vlan;

    private Integer assetSource;

    private Integer assetManage;

    private Integer policy;

    private String assetSourceName;

    private String assetManageName;


    public void resetDepartName(Map<Object, String> map) {
        Integer status = this.departmentId;
        if (status == null || map == null || map.size() == 0) {
            return;
        }
        String name = map.get(status);
        this.setDepartmentName(name);
    }

    public void resetChange() {
        StringBuilder builder = new StringBuilder();
        if (this.macChange != null && macChange == 1) {
            builder.append("MAC异常 | ");
        }
        // @北辰 最新情况，入网路径异常这个指标先不用了
        /*if (this.arpOutChange != null && arpOutChange == 1) {
            builder.append("入网路径异常 | ");
        }*/
        if (this.openPortChange != null && openPortChange == 1) {
            builder.append("端口异常 | ");
        }
        if (this.modelDesChange != null && modelDesChange == 1) {
            builder.append("模型异常 | ");
        }
        if (builder.length() != 0) {
            this.changeBrief  = builder.substring(0, builder.length() - 3);
        } else {
            this.changeBrief = "";
        }
    }

    public void setAliveByMap(Map<Object, String> map) {
        Integer status = this.getAlive();
        if (status == null || map == null || map.size() == 0) {
            return;
        }
        String name = map.get(status);
        this.setAliveName(name);
    }

    public void setDisStatusByMap(Map<Object, String> map) {
        Integer status = this.getDisStatus();
        if (status == null || map == null || map.size() == 0) {
            return;
        }
        String name = map.get(status);
        this.setDisStatusName(name);
    }

    public void setMuteByMap(Map<Object, String> map) {
        Integer status = this.getMute();
        if (status == null || map == null || map.size() == 0) {
            return;
        }
        String name = map.get(status);
        this.setMuteName(name);
    }

    public void setIpStatusByMap(Map<Object, String> map) {
        Integer status = this.getIpStatus();
        if (status == null || map == null || map.size() == 0) {
            return;
        }
        String name = map.get(status);
        this.setIpStatusName(name);
    }

    public void setRiskTypeNameByMap(Map<Object, String> map) {
        Integer status = this.riskType;
        if (status == null || map == null || map.size() == 0) {
            return;
        }
        String name = map.get(status);
        this.setRiskTypeName(name);
    }

    public void setProjectStatusNameByMap(Map<Object, String> map) {
        Integer status = this.projectStatus;
        if (status == null || map == null || map.size() == 0) {
            return;
        }
        String name = map.get(status);
        this.setProjectStatusName(name);
    }

    public void setConfirmStatusNameByMap(Map<Object, String> map) {
        Integer status = this.confirmStatus;
        if (status == null || map == null || map.size() == 0) {
            return;
        }
        String name = map.get(status);
        this.setConfirmStatusName(name);
    }

    /**
     * 设置资产状态所对应的名称
     * @param map
     */
    public void setEnableStatusNameByMap(Map<Object, String> map){
        Integer status = this.enableStatus;
        if (status == null || map == null || map.size() == 0) {
            return;
        }
        String name = map.get(status);
        this.setEnableStatusName(name);
    }

    /**
     * 设置资产录入来源所对应的名称
     * @param map
     */
    public void setSourceNameByMap(Map<Object, String> map){
        Integer status = this.source;
        if (status == null || map == null || map.size() == 0) {
            return;
        }
        if (status.intValue() == 1 || status.intValue() == 2 || status.intValue() == 3){
            status = 0;
        }else if (status.intValue() == 4){
            status = 1;
        }else if (status.intValue() == 5){
            status = 2;
        }else{
            status = -1;
        }
        String name = map.get(status);
        this.setSourceName(name);
    }

    public void setMaintainStatusNameByMap(Map<Object, String> map) {
        String status = this.getMaintainStatus();
        if (status == null || map == null || map.size() == 0) {
            return;
        }
//        String name = map.get(status);
//        this.setMaintainStatusName(name);
        this.setMaintainStatusName(status);
    }

    public void setAssetSourceNameByMap(Map<Object, String> map) {
        Integer status = this.getAssetSource();
        if (status == null || map == null || map.size() == 0) {
            return;
        }
        String name = map.get(status);
        this.setAssetSourceName(name);
    }

    public void setAssetManageNameByMap(Map<Object, String> map) {
        Integer status = this.getAssetManage();
        if (status == null || map == null || map.size() == 0) {
            return;
        }
        String name = map.get(status);
        this.setAssetManageName(name);
    }
}
