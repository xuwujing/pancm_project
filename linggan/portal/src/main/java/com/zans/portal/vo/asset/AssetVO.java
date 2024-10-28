package com.zans.portal.vo.asset;

import com.zans.portal.model.Asset;
import lombok.Data;

import java.util.Date;

/**
 * @author qiyi
 * @Title: portal
 * @Description:
 * @Version:1.0.0
 * @Since:jdk1.8
 * @date 2021/8/5
 */
@Data
public class AssetVO extends Asset {

    private Integer alive;

    private Date aliveLastTime;

    private String mac;
    private Integer enableStatus;

}
