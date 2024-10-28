package com.zans.mms.vo.devicepoint;

import com.zans.base.vo.BasePage;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@ApiModel(value = "DevicePointQueryVo", description = "")
@Data
public class DevicePointQueryVO extends BasePage implements Serializable {
    private String pointName;

    private String pointCode;

    private List<String> areaId;

    private Long  subsetId;

    private List<String>  deviceType;

    /**
     * 所属单位id
     */
    private List<String> orgId;

    /**
     * 关键字
     */
    private String keyword;


    private List<String> keywordList;

    @ApiModelProperty(name = "pointIds", value = "已存在的编号集合",hidden = true)
    private List<Long> pointIds;


    //新加过滤字段
    private List<Integer> projectIdList;


    private String buildCompany;

    /**
     * 项目列表过滤
     */
    private List<Integer> itemList;

    /**
     * 导入id
     */
    private Long importId;


    /**
     * 操作 1新增 2修改
     */
    private Integer operation;


}
