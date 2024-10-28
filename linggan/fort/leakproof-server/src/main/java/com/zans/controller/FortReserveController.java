package com.zans.controller;

import com.zans.service.IFortReserveService;
import com.zans.utils.ApiResult;
import com.zans.vo.FortReserveVO;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * @author qiyi
 * @Title: leakproof-server
 * @Description:
 * @Version:1.0.0
 * @Since:jdk1.8
 * @date 2021/6/25
 */
@Api(tags = "预约表")
@RestController
@RequestMapping("reserve")
@EnableScheduling
@Slf4j
public class FortReserveController {

    @Autowired
    private IFortReserveService iReserveService;
    /**
     * 当前预约
     * @param reserveVO
     * @return
     */
    @RequestMapping(value = "currentAppointment", method = RequestMethod.POST)
    public ApiResult currentAppointment(@RequestBody FortReserveVO reserveVO,HttpServletRequest request) {
        return iReserveService.currentAppointment(reserveVO,request);
    }

    //进入远程时重新请求时间
//    @RequestMapping(value = "enterRemote",method = RequestMethod.GET)
//    public ApiResult enterRemote(@RequestParam("ip") String ip,HttpServletRequest request){
//        return iReserveService.enterRemote(ip,request);
//    }

    /**
     * 预约数
     * @return
     */
    @RequestMapping(value = "appointmentNum", method = RequestMethod.GET)
    public ApiResult appointmentNum() {
        return iReserveService.appointmentNum();
    }

    /**
     * 申请机时
     * @param reserveVO
     * @return
     */
    @RequestMapping(value = "applyForMachineHour", method = RequestMethod.POST)
    public ApiResult applyForMachineHour(@RequestBody FortReserveVO reserveVO, HttpServletRequest request) {
        return iReserveService.applyForMachineHour(reserveVO,request);
    }

    /**
     * 预约时间是否冲突
     */
    @RequestMapping(value = "timeConflict",method = RequestMethod.POST)
    public ApiResult timeConflict(@RequestBody FortReserveVO reserveVO, HttpServletRequest request){
        return iReserveService.timeConflict(reserveVO,request);
    }

    /**
     * 选择主机
     */
    @RequestMapping(value = "chooseServer",method = RequestMethod.GET)
    public ApiResult chooseServer(){
        return iReserveService.chooseServer();
    }

    /**
     * 查询是否可预约
     */
    @RequestMapping(value = "queryReserve",method = RequestMethod.GET)
    public ApiResult queryReserver(@RequestParam("serverIp") String serverIp,@RequestParam("mouths") int mouths){
        return iReserveService.queryReserve(serverIp,mouths);
    }

    /**
     * 查询指定天指定主机是否可预约
     *
     */
    @RequestMapping(value = "queryReserveDay",method = RequestMethod.GET)
    public ApiResult queryReserveDay(@RequestParam("serverIp") String serverIp,@RequestParam("startTime") String startTime){
        return iReserveService.queryReserveDay(serverIp,startTime);
    }

    /**
     * 查看周预约情况
     */
    @RequestMapping(value = "queryReserveWeek",method = RequestMethod.GET)
    public ApiResult queryReserveWeek(@RequestParam("serverIp") String serverIp,@RequestParam("startTime") String startTime){
        return iReserveService.queryReserveWeek(serverIp,startTime);
    }

    /**
     * 待审批列表
     */
    @RequestMapping(value = "approveList",method = RequestMethod.POST)
    public ApiResult approveList(@RequestBody FortReserveVO fortReserveVO){
        return iReserveService.approveList(fortReserveVO);
    }

    /**
     * 待审批列表头
     */
    @RequestMapping(value = "approveListNum",method = RequestMethod.GET)
    public ApiResult approveListNum(){
        return iReserveService.approveListNum(new FortReserveVO());
    }

    /**
     * 审批
     */
    @RequestMapping(value = "approve",method = RequestMethod.POST)
    public ApiResult approve(@RequestBody FortReserveVO fortReserveVO,HttpServletRequest request){
        return iReserveService.approve(fortReserveVO,request);
    }

    /**
     * 检查是否能进入远程
     */
    @RequestMapping(value = "check",method = RequestMethod.GET)
    public ApiResult check(@RequestParam("id") Integer id,HttpServletRequest request){
        return iReserveService.check(id,request);
    }

    /**
     * 审批详情
     * @param id
     * @return
     */
    @RequestMapping(value = "approvalDetail",method = RequestMethod.GET)
    public ApiResult approvalDetail(@RequestParam("id") Integer id){
        return iReserveService.approvalDetail(id);
    }

    @RequestMapping(value = "terminationNow",method = RequestMethod.GET)
    public ApiResult terminationNow(@RequestParam("id") Integer id){
        return iReserveService.terminationNow(id);
    }

}
