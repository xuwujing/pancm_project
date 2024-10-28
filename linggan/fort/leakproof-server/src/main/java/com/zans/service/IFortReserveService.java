package com.zans.service;

import com.zans.model.FortReserve;
import com.zans.utils.ApiResult;
import com.zans.vo.FortReserveVO;

import javax.servlet.http.HttpServletRequest;

/**
 * @author qiyi
 * @Title: leakproof-server
 * @Description:
 * @Version:1.0.0
 * @Since:jdk1.8
 * @date 2021/6/23
 */
public interface IFortReserveService {

    ApiResult currentAppointment(FortReserveVO reserveVO,HttpServletRequest request);

//    ApiResult enterRemote(String ip,HttpServletRequest request);

//    ApiResult appointmentHistory(FortReserveVO reserveVO);

    ApiResult appointmentNum();

    ApiResult applyForMachineHour(FortReserveVO reserveVO, HttpServletRequest request);

    ApiResult timeConflict(FortReserveVO reserveVO, HttpServletRequest request);

    ApiResult chooseServer();

    ApiResult queryReserve(String serverIp,int mouths);

    ApiResult queryReserveDay(String serverIp,String startTime);

    ApiResult queryReserveWeek(String serverIp,String startTime);

    FortReserve findFortReserveByIp(String ip);

    ApiResult approve(FortReserveVO fortReserveVO,HttpServletRequest request);

    ApiResult approveList(FortReserveVO fortReserveVO);

    ApiResult approveListNum(FortReserveVO fortReserveVO);

    ApiResult check(Integer id,HttpServletRequest request);

    ApiResult approvalDetail(Integer id);

    ApiResult terminationNow(Integer id);

}
