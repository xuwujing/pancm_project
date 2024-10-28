package com.zans.mms.vo.asset;

import com.zans.base.vo.BasePage;
import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@ApiModel(value = "AssetQueryVo", description = "")
@Data
public class AssetQueryVO extends BasePage implements Serializable {
    private String pointName;
    private String pointCode;
    private List<String> areaId;
    private String assetCode;
    private List<String> maintainStatus;
    private String networkIp;
    private String projectName;
    private String deviceType;
    private Long   pointId;
    private String deviceSubType;
    private String deviceModelBrand;

    /**
     * 所属单位id
     */
    private List<String> orgId;

    private List<Integer> diagnosisResult;

    private String buildCompany;

    //新加过滤字段
    private List<Integer> projectIdList;


    /**
     * 项目列表过滤
     */
    private List<Integer> itemList;

    /**
     * 导入id
     */
    private Long importId;

    /**
     * 操作
     */
    private Integer operation;
}
