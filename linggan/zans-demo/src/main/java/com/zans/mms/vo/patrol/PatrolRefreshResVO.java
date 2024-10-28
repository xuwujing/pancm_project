package com.zans.mms.vo.patrol;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
* @Title: PatrolRefreshResVO
* @Description: 获取增量更新数据VO
* @Version:1.0.0
* @Since:jdk1.8
* @author beiming
* @date 4/15/21
*/
@Data
public class PatrolRefreshResVO implements Serializable {
    /**
     * 更新的巡检点位
     */
    private List<PatrolPointRefreshVO> points;

    /**
     * 统计信息
     */
    private Object  statistical;
}
