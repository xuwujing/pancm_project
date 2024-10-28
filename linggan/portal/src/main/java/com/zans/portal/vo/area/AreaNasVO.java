package com.zans.portal.vo.area;

import lombok.Data;

/**
 * @author xv
 * @since 2020/4/17 11:18
 */
@Data
public class AreaNasVO {

    private Integer id;

    private Integer area;

    private String areaName;

    private String nasIp;

    private String nasSwIp;

    private Integer nasSwBrand;

    private String nasSwBrandName;

    private String nasCommunity;

    private String nasDesc;

}