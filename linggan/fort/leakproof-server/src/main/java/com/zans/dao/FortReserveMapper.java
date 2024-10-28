package com.zans.dao;


import com.zans.model.FortPlayBack;
import com.zans.model.FortReserve;
import com.zans.vo.FortPlayBackVO;
import com.zans.vo.FortReserveVO;
import com.zans.vo.SessionAuditVO;
import org.springframework.stereotype.Component;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

@org.apache.ibatis.annotations.Mapper
@Component
public interface FortReserveMapper extends Mapper<FortReserve> {

    List<FortReserveVO> selectReserve(FortReserveVO reserveVO);

    List<FortReserveVO> sessionAudit(FortReserveVO reserveVO);

    int updateStatus(FortReserveVO reserveVO);

    Integer queryTimeStampCount(FortReserveVO reserveVO);

    List<FortReserve> queryReserveDay(FortReserveVO fortReserveVO);

    FortReserve findFortReserveByIp(FortReserveVO fortReserveVO);

    List<FortPlayBack> replyAuditVideoSql(FortPlayBackVO fortPlayBackVO);

    List<SessionAuditVO> queryNoEmptyAudit(FortReserveVO reserveVO);

    List<FortReserveVO> queryTimeConflict(FortReserve fortReserve);

//    List<FortReserveVO> selectApprove(FortReserveVO fortReserveVO);

    FortReserve checkIfSelect(FortReserveVO fortReserveVO);

    List<FortReserve> queryIfReserve(FortReserveVO fortReserveVO);

    Integer videoStatus(FortReserveVO fortReserveVO);

    Integer approve(FortReserveVO fortReserveVO);

    List<FortReserve> selectAllReserve(FortReserveVO fortReserveVO);

    List<FortReserve> queryIfExist(FortReserveVO fortReserveVO);


}
