package com.zans.mms.vo.patrol;

import com.zans.mms.model.PatrolAggData;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

/**
 * @author beiso
 * @date 2021/3/10 19:34
 */
@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class PatrolAggAreaDataResVO extends AbstractPatrolAggData {

    private String areaId;
    private String areaName;
    private Integer completedStatus;
    private String longitude;
    private String latitude;

    private List<PatrolAggDeviceDataResVo> deviceData;

    public PatrolAggAreaDataResVO(){
        super();
        this.deviceData = new ArrayList<>();
    }

    public void processModelData(PatrolAggData item) {
        setAreaData(item);
        processDeviceData(item);
        this.doCalcCompletedStatus();
    }

    private void setAreaData(PatrolAggData item) {
        this.setAreaId(item.getAreaId());
        this.setAreaName(item.getAreaName());
        this.setCompleted(item.getCompleted());
        this.setTotal(item.getTotal());
        this.setTodayCompleted(item.getTodayCompleted());
        this.setLatitude(item.getLatitude());
        this.setLongitude(item.getLongitude());
    }

    private void processDeviceData(PatrolAggData item) {
        this.processDeviceData02(item);
        this.processDeviceData03(item);
        this.processDeviceData04(item);
        this.processDeviceData24(item);
        this.processDeviceData17(item);
    }

    public void processDeviceData02(PatrolAggData item) {
        PatrolAggDeviceDataResVo data = new PatrolAggDeviceDataResVo();
        data.setDeviceId("02");
        data.setDeviceName("电警");
        data.setTotal(item.getTotal02());
        data.setCompleted(item.getCompleted02());
        data.setTodayCompleted(item.getTodayCompleted02());
        this.getDeviceData().add(data);
    }

    public void processDeviceData03(PatrolAggData item) {
        PatrolAggDeviceDataResVo data = new PatrolAggDeviceDataResVo();
        data.setDeviceId("03");
        data.setDeviceName("卡口");
        data.setTotal(item.getTotal03());
        data.setCompleted(item.getCompleted03());
        data.setTodayCompleted(item.getTodayCompleted03());
        this.getDeviceData().add(data);
    }


    public void processDeviceData04(PatrolAggData item) {
        PatrolAggDeviceDataResVo data = new PatrolAggDeviceDataResVo();
        data.setDeviceId("04");
        data.setDeviceName("监控");
        data.setTotal(item.getTotal04());
        data.setCompleted(item.getCompleted04());
        data.setTodayCompleted(item.getTodayCompleted04());
        this.getDeviceData().add(data);
    }


    public void processDeviceData24(PatrolAggData item) {
        PatrolAggDeviceDataResVo data = new PatrolAggDeviceDataResVo();
        data.setDeviceId("24");
        data.setDeviceName("信号机");
        data.setTotal(item.getTotal24());
        data.setCompleted(item.getCompleted24());
        data.setTodayCompleted(item.getTodayCompleted24());
        this.getDeviceData().add(data);
    }


    public void processDeviceData17(PatrolAggData item) {
        PatrolAggDeviceDataResVo data = new PatrolAggDeviceDataResVo();
        data.setDeviceId("17");
        data.setDeviceName("诱导屏");
        data.setTotal(item.getTotal17());
        data.setCompleted(item.getCompleted17());
        data.setTodayCompleted(item.getTodayCompleted17());
        this.getDeviceData().add(data);
    }


    public void doCalcCompletedStatus() {
        this.setCompletedStatus(this.calcCompletedStatus());
    }
}
