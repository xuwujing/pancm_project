package com.zans.portal.vo.brand;

import com.zans.base.vo.BasePage;
import com.zans.base.vo.MacPage;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Column;

@ApiModel
@Data
public class BrandResponseVO extends BasePage {

    @ApiModelProperty(name = "brand_id", value = "id")
    private Integer brandId;

    @ApiModelProperty(name = "brand_name", value = "品牌名称")
    private String brandName;

    @ApiModelProperty(name = "company", value = "公司名称")
    private String company;

    @ApiModelProperty(name = "brand_desc", value = "品牌关键词")
    private String brandDesc;

    private Integer confirm;

}
