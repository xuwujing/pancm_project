package com.zans.mms.vo.patrol;

import com.zans.mms.model.BaseVfs;
import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.util.List;
/**
 * @Title: PatrolLogRespVO
 * @Description: 巡检日志返还体
 * @Version:1.0.0
 * @Since:jdk1.8
 * @author beiming
 * @date 4/20/21
 */
@ApiModel(value = "PatrolLogRespVO", description = "")
@Data
public class PatrolLogRespVO {
    private Long id;
    private String areaId;
    private String pointName;
    private String pointId;
    private String pointCode;
    private String deviceType;
    private String checkResult;
    private String prevCheckResult;
    private String nickName;
    private String checkTime;
    private String adjunctUuid;
    private String orgId;

    private String checkRemark;
    List<BaseVfs> baseVfsList;

    private Long ticketId;

    private String ticketCode;
}
