package com.zans.mms.vo.patrol;

import com.zans.mms.model.PatrolAggData;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

/**
* @Title: PatrolAggTopDataResVO
* @Description:
* @Version:1.0.0
* @Since:jdk1.8
* @author beiso
* @Date  2021/3/10
**/
@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class PatrolAggTopDataResVO extends AbstractPatrolAggData {
    private String name;
    private Integer completedStatus;
    private List<PatrolAggAreaDataResVO> areaData;

    public PatrolAggTopDataResVO() {
        this.name = "武汉市交管局";
        this.areaData = new ArrayList<>();
    }

    public void processModelData(PatrolAggData item) {
        String areaId = item.getAreaId();
        PatrolAggAreaDataResVO data = new PatrolAggAreaDataResVO();
        areaData.add(data);
        data.processModelData(item);
        doAggregation(data);
    }

    protected void doAggregation(AbstractPatrolAggData data) {
        this.addTotal(data.getTotal());
        this.addCompleted(data.getCompleted());
        this.addTodayCompleted(data.getTodayCompleted());
    }

    public void doCalcCompletedStatus() {
        this.setCompletedStatus(this.calcCompletedStatus());
    }

}
