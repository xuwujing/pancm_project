package com.zans.portal.dao;

import com.zans.portal.model.SysSwitcher;
import com.zans.portal.vo.switcher.*;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

@Repository
public interface SysSwitcherMapper extends Mapper<SysSwitcher> {

    List<SwitchResVO> findSwitchList(@Param("reqVo") SwitchSearchVO reqVo);

    SysSwitcher findBySwHost(@Param("swHost") String swHost, @Param("id") Integer id);





    SwitcherScanRespVO getScanBySwId(@Param("reqVo") SwitcherScanReqVO reqVO);

    SwitcherScanRespVO getLastScanBySwId(@Param("ipAddr") String ipAddr);

    List<SwitcherScanListRespVO> findSwitchScanList(@Param("reqVo") SwitcherScanReqVO reqVo);

    List<SwitcherScanListRespVO> findSwitchScan(@Param("reqVo") SwitcherScanReqVO reqVo);

    List<SwitcherScanListRespVO> findSwitchDateHourScan(@Param("reqVo") SwitcherScanReqVO reqVo);

    List<Integer> getSwitchScanReportByDate(@Param("start") Date start, @Param("end") Date end, @Param("swId") Integer swId);

    List<Integer> getSwitchScanReportByDay(@Param("dates") Date dates, @Param("swId") Integer swId, @Param("alive") Integer alive);

    List<Integer> getSwitchScanReportByHour(@Param("dates") Date dates, @Param("swId") Integer swId, @Param("alive") Integer alive);

    List<SwitchVlanSplitVO> findAllArpEnabledSwitchWithConfig();

    List<SysSwitcher> getListCondition(HashMap<String, Object> map);


    SysSwitcher findSysSwitcherByIp(@Param("ip") String ip);

    SwitchAssessResVO getScanDayByIp(@Param("ipAddr") String ipAddr,@Param("approveStartTime") String approveStartTime, @Param("approveEndTime") String approveEndTime);


    List<SwitchAssessResVO> findSwitchAndAssessList3(@Param("reqVo") SwitchAssessSearchVO reqVO);

}
