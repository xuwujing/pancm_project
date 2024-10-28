package com.zans.mms.vo.area;

import com.alibaba.fastjson.annotation.JSONField;
import io.swagger.annotations.ApiModel;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @author yhj
 */
@Data
@ApiModel
public class RegionRespVO {

    @JSONField(name = "region_id")
    private Integer regionId;

    @JSONField(name = "parent_id")
    @NotNull
    private Integer parentId;

    @JSONField(name = "city_id")
    private Integer cityId;

    @JSONField(name = "region_path")
    private String regionPath;

    @JSONField(name = "region_name")
    private String regionName;


/*    public void fromRegion(SysRegion region) {
        if (region == null) {
            return;
        }
        this.regionId = region.getRegionId();
        this.parentId = region.getParentId();
        this.cityId = region.getCityId();
        this.regionPath = region.getRegionPath();
        this.regionName = region.getRegionName();
    }*/

}
