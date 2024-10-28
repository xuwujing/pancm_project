package com.zans.portal.vo.network;

import com.alibaba.fastjson.annotation.JSONField;
import com.zans.base.office.excel.ExcelCell;
import com.zans.base.util.DateHelper;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author xv
 * @since 2020/6/4 17:06
 */
@ApiModel
@Data
@Slf4j
public class ProjectVO {

    private Integer id;

    @ApiModelProperty(value = "项目名称")
    @JSONField(name = "project_name")
    private String projectName;

    /**
     * 项目类型，局自建项目、新改扩建项目
     */
    @ApiModelProperty(value = "项目类型，编号")
    @JSONField(name = "project_type")
    private Integer projectType;

    @ApiModelProperty(value = "项目类型名称")
    @JSONField(name = "project_type_name")
    private String projectTypeName;

    /**
     * 项目状态，申请，通过，建设中，超期，已上线
     */
    @ApiModelProperty(value = "项目状态，编号")
    @JSONField(name = "project_status")
    private Integer projectStatus;

    @ApiModelProperty(value = "项目状态名称")
    @JSONField(name = "project_status_name")
    private String projectStatusName;

    /**
     * 接入片区
     */
    @ApiModelProperty(value = "接入片区编号")
    private Integer region;

    @ApiModelProperty(value = "接入片区名称，汉口/汉阳/武昌")
    @JSONField(name = "regionName")
    private String regionName;

    /**
     * 承建单位
     */
    @ApiModelProperty(value = "承建单位")
    private String contractor;

    /**
     * 承建联系人
     */
    @ApiModelProperty(value = "承建联系人")
    @JSONField(name = "contractor_person")
    private String contractorPerson;

    /**
     * 承建联系电话
     */
    @ApiModelProperty(value = "承建联系电话")
    @JSONField(name = "contractor_phone")
    private String contractorPhone;

    /**
     * 派单日期
     */
    @ApiModelProperty(value = "派单日期")
    @JSONField(name = "dispatch_date", format = "yyyy-MM-dd HH:mm:ss")
    private Date dispatchDate;

    /**
     * 建设时间，单位天
     */
    @ApiModelProperty(value = "建设周期，单位天")
    @JSONField(name = "build_day")
    private Integer buildDay;

    /**
     * 附件的文件名
     */
    @ApiModelProperty(value = "附件的文件名")
    @JSONField(name = "file_name")
    private String fileName;

    /**
     * 附件地址
     */
    @ApiModelProperty(value = "附件地址")
    @JSONField(name = "file_path")
    private String filePath;

    /**
     * 链路数量，填写
     */
    @ApiModelProperty(value = "链路数量，填写")
    @JSONField(name = "link_count")
    private Integer linkCount;

    /**
     * 点位总数，统计
     */
    @ApiModelProperty(value = "点位数量")
    @JSONField(name = "point_count")
    private Integer pointCount;

    /**
     * 申请人
     */
    @ApiModelProperty(value = "申请人ID")
    private Integer creator;

    @JSONField(name = "create_time", format = "yyyy-MM-dd")
    @ApiModelProperty(name = "create_time", value = "申请时间")
    private Date createTime;

    @ApiModelProperty(value = "申请人昵称")
    @JSONField(name = "creator_name")
    private String creatorName;

    @ApiModelProperty(value = "通讯链路子表")
    @JSONField(name = "link_list")
    private List<LinkVO> linkList;

    @ApiModelProperty(value = "汇聚点名称")
//    @JSONField(name = "link_names")
    private String linkName;


    public void setProjectTypeNameByMap(Map<Object, String> map) {
        Integer status = this.projectType;
        if (status == null || map == null || map.size() == 0) {
            return;
        }
        String name = map.get(status);
        this.setProjectTypeName(name);
    }

    public void setProjectStatusNameByMap(Map<Object, String> map) {
        Integer status = this.projectStatus;
        if (status == null || map == null || map.size() == 0) {
            return;
        }
        String name = map.get(status);
        this.setProjectStatusName(name);
    }

    public Map<ExcelCell, Object> toExcel(int pointCount) {
        Map<ExcelCell, Object> valueMap = new HashMap<>(5);
        String name = String.format("项目名称：%s", this.getProjectName());
        String linkCount = String.format("链路数量：%s", this.getLinkCount() == null ? "" : this.getLinkCount());
        String contractor = String.format("建设单位：%s %s(%s)",
                this.getContractor(), this.getContractorPerson(), this.getContractorPhone());
        String manager = String.format("管理人：");
        String submit = String.format("申请人：%s", creatorName);
        String create = String.format("申请日期：%s", DateHelper.formatDate(this.getCreateTime(), null));
        String dispatchDay = (this.getDispatchDate() == null) ? "" : DateHelper.formatDate(this.getDispatchDate(), null);
        String dispatch = String.format("派单日期：%s", dispatchDay);
        valueMap.put(ExcelCell.builder().row(1).column(0).build(), name);
        valueMap.put(ExcelCell.builder().row(1).column(6).build(), linkCount);
        valueMap.put(ExcelCell.builder().row(1).column(7).build(), contractor);
        int lastIndex = 3 + pointCount;
        valueMap.put(ExcelCell.builder().row(lastIndex).column(0).build(), manager);
        valueMap.put(ExcelCell.builder().row(lastIndex).column(3).build(), submit);
        valueMap.put(ExcelCell.builder().row(lastIndex).column(6).build(), create);
        valueMap.put(ExcelCell.builder().row(lastIndex).column(8).build(), dispatch);
        return valueMap;
    }

    public void resetRowIndexOfLinkList(int start) {
        int rowIndex = start;
        for (int i=0; i<this.linkList.size(); i++) {
            LinkVO linkVO = this.linkList.get(i);
            linkVO.setRowIndex(rowIndex);
            for(PointVO pointVO : linkVO.getPointList()) {
                pointVO.setRowIndex(rowIndex++);
            }
            linkVO.setEndIndex(rowIndex-1);
        }
    }

}
