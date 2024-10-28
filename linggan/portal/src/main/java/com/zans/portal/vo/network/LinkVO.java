package com.zans.portal.vo.network;

import com.alibaba.fastjson.annotation.JSONField;
import com.zans.base.office.annotation.ExcelProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.LinkedList;
import java.util.List;

/**
 * @author xv
 * @since 2020/6/4 20:04
 */
@ApiModel
@Data
public class LinkVO {

    @ApiModelProperty(value = "链路主键")
    private Integer id;

    @ApiModelProperty(value = "项目主键")
    @JSONField(name = "projectId")
    private Integer projectId;

    @ApiModelProperty(value = "链路名称")
    @JSONField(name = "link_name")
    @ExcelProperty(value = {"汇聚点名称"}, validate = {"not_empty"}, index = 1)
    private String linkName;

    @ApiModelProperty(value = "接入区域，大队")
    @JSONField(name = "link_area")
    private Integer linkArea;

    @ApiModelProperty(value = "接入区域名称")
    @JSONField(name = "link_area_name")
    @ExcelProperty(value = {"区域"},
            validate = {"not_empty",
                    "com.zans.base.office.validator.ValueInValidator#[\"东湖\",\"东湖高新\",\"东西湖\",\"二桥\",\"化工\"," +
                    "\"大桥\",\"市局\",\"新洲\",\"武昌\",\"汉阳\",\"江夏\",\"江岸\",\"江汉\",\"沌口\",\"洪山\"," +
                    "\"白沙洲\",\"直属\",\"硚口\",\"蔡甸\",\"车管所\",\"金银湖机房\",\"青山\",\"高管\",\"黄陂\"]"},
            index = 2)
    private String linkAreaName;

    @ApiModelProperty(value = "接入点的IP")
    @ExcelProperty(value = "交换机ip", index = 3)
    private String ip;

    @ApiModelProperty(value = "子网掩码")
    @ExcelProperty(value = "子网掩码", index = 4)
    private String mask;

    @ApiModelProperty(value = "VLAN")
    @ExcelProperty(value = "VLAN", index = 5)
    private String vlan;

    @ApiModelProperty(value = "接入点的经度")
    @ExcelProperty(value = "经度", index = 6)
    private String longitude;

    @ApiModelProperty(value = "接入点的纬度")
    @ExcelProperty(value = "纬度", index = 7)
    private String latitude;

    @ApiModelProperty(value = "序号")
    @ExcelProperty(value = "序号", type="Integer", index = 0)
    private Integer seq;

    @ApiModelProperty(value = "行号")
    @JSONField(name = "row_index")
    private Integer rowIndex;

    private Integer endIndex;

    @ApiModelProperty(value = "点位子表")
    @JSONField(name = "point_list")
    private List<PointVO> pointList;

    public void addPoint(PointVO pointVO) {
        if (pointList == null) {
            pointList = new LinkedList<>();
        }
        pointList.add(pointVO);
    }

}
