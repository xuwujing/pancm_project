package com.zans.portal.vo.area;

import com.zans.base.vo.SelectVO;
import lombok.Data;

/**
 * @author xv
 * @since 2020/5/27 16:09
 */
@Data
public class RegionVO {

    private Integer regionId;

    private Integer parentId;

    private String regionName;

    public SelectVO toSelectVO() {
        return new SelectVO(regionId, regionName);
    }
}
