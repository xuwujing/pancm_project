package com.zans.portal.vo.brand;

import com.alibaba.fastjson.annotation.JSONField;
import com.zans.base.vo.BasePage;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Column;
import javax.validation.constraints.NotNull;

@ApiModel
@Data
public class BrandMergeVO {

    @ApiModelProperty(name = "brand_id", value = "brand_id")
    @JSONField(name = "brand_id")
    private Integer brandId;

    @ApiModelProperty(name = "brand_name", value = "品牌名称", required = true)
    @NotNull(message = "设备型号")
    @JSONField(name = "brand_name")
    private String brandName;

    @ApiModelProperty(name = "company", value = "公司名称", required = true)
    @NotNull(message = "公司名称")
    private String company;

    @ApiModelProperty(name = "brand_desc", value = "品牌关键字", required = true)
    @NotNull(message = "品牌关键字")
    @JSONField(name = "brand_desc")
    private String brandDesc;

    @ApiModelProperty(name = "confirm", value = "是否允许被删除")
    @JSONField(name = "confirm")
    @NotNull(message = "品牌关键字")
    private Integer confirm;

    public Integer getBrandId() {
        return brandId;
    }

    public void setBrandId(Integer brandId) {
        this.brandId = brandId;
    }

    public String getBrandName() {
        return brandName;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getBrandDesc() {
        return brandDesc;
    }

    public void setBrandDesc(String brandDesc) {
        this.brandDesc = brandDesc;
    }
}
