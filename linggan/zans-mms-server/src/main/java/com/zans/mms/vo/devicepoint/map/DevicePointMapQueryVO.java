package com.zans.mms.vo.devicepoint.map;

import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
* @Title: DevicePointMapQueryVO
* @Description: 点位地图QueryVO
* @Version:1.0.0
* @Since:jdk1.8
* @author beiming
* @date 4/27/21
*/
@ApiModel(value = "DevicePointMapQueryVO", description = "")
@Data
public class DevicePointMapQueryVO implements Serializable {
    private String pointName;

    private String pointCode;

    private Long  subsetId;

    private Long  pointId;

    List<Long> subsetIdList;

    private List<String> areaIdList;

    private List<String>  deviceTypeList;

    /**
     * 所属单位id
     */
    private List<String> orgIdList;

    //新加过滤字段
    private List<Integer> projectIdList;


    private String buildCompany;


    /**
     * 建设单位列表
     */
    private List<String> buildCompanyList;

    /**
     * 项目列表过滤
     */
    private List<Integer> itemList;


    /**
     * 关键字
     */
    private String keyword;

}
