package com.zans.mms.vo.patrol;

import lombok.Data;

import java.io.Serializable;

/**
* @Title: PatrolPointRefreshVO
* @Description: 更新的点位
* @Version:1.0.0
* @Since:jdk1.8
* @author beiming
* @date 4/15/21
*/
@Data
public class PatrolPointRefreshVO implements Serializable {
    private Long pointId;
    private String deviceType;
}
