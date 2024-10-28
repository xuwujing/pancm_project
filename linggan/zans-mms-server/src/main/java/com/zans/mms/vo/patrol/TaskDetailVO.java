package com.zans.mms.vo.patrol;

import com.zans.mms.model.BaseVfs;
import lombok.Data;

import java.util.List;

@Data
public class TaskDetailVO {
    private String areaId;
    private String deviceType;
    private String pointCode;
    private Long   pointId;
    private String pointName;
    private String prevCheckResult;
    private String checkResult;
    private String checkTime;
    private String adjunctUuid;
    private Long   id;
    private String nickName;

    private String checkRemark;
    List<BaseVfs> baseVfsList;
}

