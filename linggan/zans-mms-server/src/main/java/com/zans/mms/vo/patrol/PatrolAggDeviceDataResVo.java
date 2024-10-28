package com.zans.mms.vo.patrol;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * @author beiso
 * @date 2021/3/10 20:44
 */

/** 
* @Title: PatrolAggDeviceDataResVo
* @Description:  
* @Version:1.0.0   
* @Since:jdk1.8  
* @author beiso
* @Date  2021/3/10
**/ 
@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class PatrolAggDeviceDataResVo extends AbstractPatrolAggData {
    private String deviceName;
    private String deviceId;

    public PatrolAggDeviceDataResVo() {
        super();
    }

}
