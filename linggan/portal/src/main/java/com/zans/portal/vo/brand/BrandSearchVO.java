package com.zans.portal.vo.brand;

import com.alibaba.fastjson.annotation.JSONField;
import com.zans.base.vo.BasePage;
import com.zans.base.vo.MacPage;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Column;

@ApiModel
@Data
public class BrandSearchVO extends BasePage {

    @ApiModelProperty(name = "brand_name", value = "品牌名称")
    private String brandName;

    @ApiModelProperty(name = "company", value = "公司名称")
    private String company;

}
