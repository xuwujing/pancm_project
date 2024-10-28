package com.zans.mms.vo.saleproject;

import com.zans.base.vo.BasePage;
import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.io.Serializable;

@ApiModel(value = "SaleProjectQueryVO", description = "")
@Data
public class SaleProjectQueryVO extends BasePage implements Serializable {
    /**
     * 销售项目编号
     */
    private String projectId;

    /**
     * 销售项目名称
     */
    private String projectName;

}
