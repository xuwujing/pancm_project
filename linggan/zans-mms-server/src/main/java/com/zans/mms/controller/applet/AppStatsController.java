package com.zans.mms.controller.applet;

import com.zans.base.controller.BaseController;
import com.zans.base.exception.BusinessException;
import com.zans.base.vo.ApiResult;
import com.zans.mms.service.ITicketService;
import com.zans.mms.vo.ticket.AppDispatchTicketChartVO;
import com.zans.mms.vo.ticket.AppFaultTicketChartVO;
import com.zans.mms.vo.ticket.AppTicketCharReqVO;
import com.zans.mms.vo.ticket.AppTicketChartVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * create by: beiming
 * create time: 2021/12/8 14:20
 */
@Api(tags = "小程序统计报表(app)")
@RestController
@RequestMapping("app/stats")
public class AppStatsController extends BaseController {
    /**
     * 服务对象
     */
    @Autowired
    private ITicketService ticketService;


    @Value("${api.export.folder}")
    String exportFolder;

    @Value("${api.upload.folder}")
    String uploadFolder;

    // --------------------------------------2021/07/22报表相关代码开始------------------------------------------------------

    @ApiOperation(value = "故障工单统计", notes = "故障工单统计")
    @RequestMapping(value = "/breakdownTicket", method = {RequestMethod.POST,RequestMethod.GET})
    public ApiResult breakdownTicket (@RequestBody AppTicketCharReqVO appTicketCharReqVO) {

        //今天 近三天 近一周 故障工单 工单类型为故障工单
        //指标  分 汉口片区 武昌片区 汉阳片区 故障工单总数 工单完工数  一周内完成工单数 及总计
        //工单完工：工单维修完成时间
        //一周内完成：工单创建时间和维修完成时间在同一周
        //工单完成率：一周内完成/故障工单总数*100%

        /**
         * 相比昨天
         * 汉口片区故障总数添加/减少XX，工单完工数增加/减少XX,工单完成了上升/降低XX
         */
        //type 1:日 2：3天  3：近一周
        //设置一个默认的统计时间段
        if(null==appTicketCharReqVO.getType()&& StringUtils.isEmpty(appTicketCharReqVO.getStartTime())
                &&StringUtils.isEmpty(appTicketCharReqVO.getEndTime())){
            appTicketCharReqVO.setType(1);
        }else{
            if(StringUtils.isEmpty(appTicketCharReqVO.getStartTime()) ||StringUtils.isEmpty(appTicketCharReqVO.getEndTime())){
                throw new BusinessException("开始时间或结束时间未传值！");
            }
            appTicketCharReqVO.setType(null);
        }
        AppTicketChartVO breakdownTicket = ticketService.breakdownTicket(appTicketCharReqVO);
        return ApiResult.success(breakdownTicket);
    }

    @ApiOperation(value = "故障工单统计Table", notes = "故障工单统计Table")
    @RequestMapping(value = "/breakdownTicketTable", method = {RequestMethod.POST,RequestMethod.GET})
    public ApiResult breakdownTicketTable (@RequestBody AppTicketCharReqVO appTicketCharReqVO) {
        //type 1:日 2：3天  3：近一周
        //设置一个默认的统计时间段
        if(null==appTicketCharReqVO.getType()&& StringUtils.isEmpty(appTicketCharReqVO.getStartTime())
                &&StringUtils.isEmpty(appTicketCharReqVO.getEndTime())){
            appTicketCharReqVO.setType(1);
        }else{
            if(StringUtils.isEmpty(appTicketCharReqVO.getStartTime()) ||StringUtils.isEmpty(appTicketCharReqVO.getEndTime())){
                throw new BusinessException("开始时间或结束时间未传值！");
            }
            appTicketCharReqVO.setType(null);
        }
        List<AppFaultTicketChartVO> map = ticketService.breakdownTicketTable(appTicketCharReqVO);
        return ApiResult.success(map);
    }



    @ApiOperation(value = "故障工单统计相比昨天", notes = "故障工单统计相比昨天")
    @RequestMapping(value = "/breakdownTicketCompare",method = {RequestMethod.POST,RequestMethod.GET})
    public ApiResult breakdownTicketCompare (@RequestBody AppTicketCharReqVO appTicketCharReqVO) {
        //type 1:昨天 2：本周
        //设置一个默认的统计时间段
        if(null==appTicketCharReqVO.getType()&& StringUtils.isEmpty(appTicketCharReqVO.getStartTime())
                &&StringUtils.isEmpty(appTicketCharReqVO.getEndTime())){
            appTicketCharReqVO.setType(1);
        }else{
            if(StringUtils.isEmpty(appTicketCharReqVO.getStartTime()) ||StringUtils.isEmpty(appTicketCharReqVO.getEndTime())){
                throw new BusinessException("开始时间或结束时间未传值！");
            }
            appTicketCharReqVO.setType(null);
            //根据开始时间和结束时间 算出上次的开始时间和结束时间
            try {
                Date startTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(appTicketCharReqVO.getEndTime());
                Date endTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(appTicketCharReqVO.getEndTime());
                //得到相差天数
                int days = (int) (( startTime.getTime()-endTime.getTime() ) / (1000*3600*24))-1;
                Calendar beforeStartTime = Calendar.getInstance();
                beforeStartTime.setTime(startTime);
                beforeStartTime.add(Calendar.DATE,days);
                appTicketCharReqVO.setBeforeStartTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(beforeStartTime.getTime()));
                Calendar beforeEndTime = Calendar.getInstance();
                beforeEndTime.setTime(endTime);
                beforeEndTime.add(Calendar.DATE,days);
                appTicketCharReqVO.setBeforeEndTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(beforeEndTime.getTime()));
            }catch (Exception e){
                return ApiResult.error("时间格式传参错误，转换失败！");
            }

        }
        Map<String, Object> map = ticketService.breakdownTicketCompare(appTicketCharReqVO);
        return ApiResult.success(map);
    }




    @ApiOperation(value = "派工单据实结算工单柱状图统计", notes = "派工单据实结算工单柱状图统计")
    @RequestMapping(value = "/dispatchTicket", method = {RequestMethod.POST,RequestMethod.GET})
    public ApiResult dispatchTicket (@RequestBody AppTicketCharReqVO appTicketCharReqVO) {
        if(null==appTicketCharReqVO.getType()&& StringUtils.isEmpty(appTicketCharReqVO.getStartTime())
                &&StringUtils.isEmpty(appTicketCharReqVO.getEndTime())){
            appTicketCharReqVO.setType(1);
        }else{
            if(StringUtils.isEmpty(appTicketCharReqVO.getStartTime()) ||StringUtils.isEmpty(appTicketCharReqVO.getEndTime())){
                throw new BusinessException("开始时间或结束时间未传值！");
            }
            appTicketCharReqVO.setType(null);
        }
        AppTicketChartVO dispatchTicket = ticketService.dispatchTicket(appTicketCharReqVO);
        return ApiResult.success(dispatchTicket);
    }

    @ApiOperation(value = "派工单据实结算工单表格统计", notes = "派工单据实结算工单表格统计")
    @RequestMapping(value = "/dispatchTicketTable", method = {RequestMethod.POST,RequestMethod.GET})
    public ApiResult dispatchTicketTable (@RequestBody AppTicketCharReqVO appTicketCharReqVO) {
        if(null==appTicketCharReqVO.getType()&& StringUtils.isEmpty(appTicketCharReqVO.getStartTime())
                &&StringUtils.isEmpty(appTicketCharReqVO.getEndTime())){
            appTicketCharReqVO.setType(1);
        }else{
            if(StringUtils.isEmpty(appTicketCharReqVO.getStartTime()) ||StringUtils.isEmpty(appTicketCharReqVO.getEndTime())){
                throw new BusinessException("开始时间或结束时间未传值！");
            }
            appTicketCharReqVO.setType(null);
        }
        List<AppDispatchTicketChartVO> map = ticketService.dispatchTicketTable(appTicketCharReqVO);
        return ApiResult.success(map);
    }


    @ApiOperation(value = "派工单据实结算工单柱状图比较", notes = "派工单据实结算工单柱状图比较")
    @RequestMapping(value = "/dispatchTicketCompare", method = {RequestMethod.POST,RequestMethod.GET})
    public ApiResult dispatchTicketCompare (@RequestBody AppTicketCharReqVO appTicketCharReqVO) {
        if(null==appTicketCharReqVO.getType()&& StringUtils.isEmpty(appTicketCharReqVO.getStartTime())
                &&StringUtils.isEmpty(appTicketCharReqVO.getEndTime())){
            appTicketCharReqVO.setType(1);
        }else{
            if(StringUtils.isEmpty(appTicketCharReqVO.getStartTime()) ||StringUtils.isEmpty(appTicketCharReqVO.getEndTime())){
                throw new BusinessException("开始时间或结束时间未传值！");
            }
            appTicketCharReqVO.setType(null);
            //根据开始时间和结束时间 算出上次的开始时间和结束时间
            try {
                Date startTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(appTicketCharReqVO.getEndTime());
                Date endTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(appTicketCharReqVO.getEndTime());
                //得到相差天数
                int days = (int) (( startTime.getTime()-endTime.getTime() ) / (1000*3600*24))-1;
                Calendar beforeStartTime = Calendar.getInstance();
                beforeStartTime.setTime(startTime);
                beforeStartTime.add(Calendar.DATE,days);
                appTicketCharReqVO.setBeforeStartTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(beforeStartTime.getTime()));
                Calendar beforeEndTime = Calendar.getInstance();
                beforeEndTime.setTime(endTime);
                beforeEndTime.add(Calendar.DATE,days);
                appTicketCharReqVO.setBeforeEndTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(beforeEndTime.getTime()));
            }catch (Exception e){
                return ApiResult.error("时间格式传参错误，转换失败！");
            }
        }
        Map<String, Object> map = ticketService.dispatchTicketCompare(appTicketCharReqVO);
        return ApiResult.success(map);
    }


    @ApiOperation(value = "验收单据实结算工单柱状图统计", notes = "验收单据实结算工单柱状图统计")
    @RequestMapping(value = "/acceptanceTicket", method = {RequestMethod.POST,RequestMethod.GET})
    public ApiResult acceptanceTicket (@RequestBody AppTicketCharReqVO appTicketCharReqVO) {
        if(null==appTicketCharReqVO.getType()&& StringUtils.isEmpty(appTicketCharReqVO.getStartTime())
                &&StringUtils.isEmpty(appTicketCharReqVO.getEndTime())){
            appTicketCharReqVO.setType(1);
        }else{
            if(StringUtils.isEmpty(appTicketCharReqVO.getStartTime()) ||StringUtils.isEmpty(appTicketCharReqVO.getEndTime())){
                throw new BusinessException("开始时间或结束时间未传值！");
            }
            appTicketCharReqVO.setType(null);
        }
        AppTicketChartVO acceptanceTicket = ticketService.acceptanceTicket(appTicketCharReqVO);
        return ApiResult.success(acceptanceTicket);
    }

    @ApiOperation(value = "验收单据实结算工单表格统计", notes = "验收单据实结算工单表格统计")
    @RequestMapping(value = "/acceptanceTicketTable", method = {RequestMethod.POST,RequestMethod.GET})
    public ApiResult acceptanceTicketTable (@RequestBody AppTicketCharReqVO appTicketCharReqVO) {
        if(null==appTicketCharReqVO.getType()&& StringUtils.isEmpty(appTicketCharReqVO.getStartTime())
                &&StringUtils.isEmpty(appTicketCharReqVO.getEndTime())){
            appTicketCharReqVO.setType(1);
        }else{
            if(StringUtils.isEmpty(appTicketCharReqVO.getStartTime()) ||StringUtils.isEmpty(appTicketCharReqVO.getEndTime())){
                throw new BusinessException("开始时间或结束时间未传值！");
            }
            appTicketCharReqVO.setType(null);
        }
        List<AppDispatchTicketChartVO> map  = ticketService.acceptanceTicketTable(appTicketCharReqVO);
        return ApiResult.success(map);
    }



    @ApiOperation(value = "验收单据实结算工单比较", notes = "验收单据实结算工单比较")
    @RequestMapping(value = "/acceptanceTicketCompare", method = {RequestMethod.POST,RequestMethod.GET})
    public ApiResult acceptanceTicketCompare (@RequestBody AppTicketCharReqVO appTicketCharReqVO) {
        if(null==appTicketCharReqVO.getType()&& StringUtils.isEmpty(appTicketCharReqVO.getStartTime())
                &&StringUtils.isEmpty(appTicketCharReqVO.getEndTime())){
            appTicketCharReqVO.setType(1);
        }else{
            if(StringUtils.isEmpty(appTicketCharReqVO.getStartTime()) ||StringUtils.isEmpty(appTicketCharReqVO.getEndTime())){
                throw new BusinessException("开始时间或结束时间未传值！");
            }
            appTicketCharReqVO.setType(null);
            //根据开始时间和结束时间 算出上次的开始时间和结束时间
            try {
                Date startTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(appTicketCharReqVO.getEndTime());
                Date endTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(appTicketCharReqVO.getEndTime());
                //得到相差天数
                int days = (int) (( startTime.getTime()-endTime.getTime() ) / (1000*3600*24))-1;
                Calendar beforeStartTime = Calendar.getInstance();
                beforeStartTime.setTime(startTime);
                beforeStartTime.add(Calendar.DATE,days);
                appTicketCharReqVO.setBeforeStartTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(beforeStartTime.getTime()));
                Calendar beforeEndTime = Calendar.getInstance();
                beforeEndTime.setTime(endTime);
                beforeEndTime.add(Calendar.DATE,days);
                appTicketCharReqVO.setBeforeEndTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(beforeEndTime.getTime()));
            }catch (Exception e){
                return ApiResult.error("时间格式传参错误，转换失败！");
            }
        }
        Map<String, Object> map = ticketService.acceptanceTicketCompare(appTicketCharReqVO);
        return ApiResult.success(map);
    }


    @ApiOperation(value = "故障类型统计", notes = "故障类型统计")
    @RequestMapping(value = "/getTicketFaultType", method = {RequestMethod.POST,RequestMethod.GET})
    public ApiResult getTicketFaultType(@RequestBody AppTicketCharReqVO appTicketCharReqVO) {
        if(null==appTicketCharReqVO.getType()&& StringUtils.isEmpty(appTicketCharReqVO.getStartTime())
                &&StringUtils.isEmpty(appTicketCharReqVO.getEndTime())){
            appTicketCharReqVO.setType(1);
        }else{
            if(StringUtils.isEmpty(appTicketCharReqVO.getStartTime()) ||StringUtils.isEmpty(appTicketCharReqVO.getEndTime())){
                throw new BusinessException("开始时间或结束时间未传值！");
            }
            appTicketCharReqVO.setType(null);
        }
        AppTicketChartVO appTicketChartVO= ticketService.getAppFaultType(appTicketCharReqVO);
        return ApiResult.success(appTicketChartVO);
    }


    @ApiOperation(value = "故障来源统计", notes = "派工来源统计")
    @RequestMapping(value = "/getTicketSource", method = {RequestMethod.POST,RequestMethod.GET})
    public ApiResult getAppTicketSource(@RequestBody AppTicketCharReqVO appTicketCharReqVO) {
        if(null==appTicketCharReqVO.getType()&& StringUtils.isEmpty(appTicketCharReqVO.getStartTime())
                &&StringUtils.isEmpty(appTicketCharReqVO.getEndTime())){
            appTicketCharReqVO.setType(1);
        }else{
            if(StringUtils.isEmpty(appTicketCharReqVO.getStartTime()) ||StringUtils.isEmpty(appTicketCharReqVO.getEndTime())){
                throw new BusinessException("开始时间或结束时间未传值！");
            }
            appTicketCharReqVO.setType(null);
        }
        AppTicketChartVO appTicketChartVO = ticketService.getAppTicketSource(appTicketCharReqVO);
        return ApiResult.success(appTicketChartVO);
    }

    @ApiOperation(value = "故障设备类型统计", notes = "故障设备类型统计")
    @RequestMapping(value = "/getDeviceType", method = {RequestMethod.POST,RequestMethod.GET})
    public ApiResult getDeviceType(@RequestBody AppTicketCharReqVO appTicketCharReqVO) {
        if(null==appTicketCharReqVO.getType()&& StringUtils.isEmpty(appTicketCharReqVO.getStartTime())
                &&StringUtils.isEmpty(appTicketCharReqVO.getEndTime())){
            appTicketCharReqVO.setType(1);
        }else{
            if(StringUtils.isEmpty(appTicketCharReqVO.getStartTime()) ||StringUtils.isEmpty(appTicketCharReqVO.getEndTime())){
                throw new BusinessException("开始时间或结束时间未传值！");
            }
            appTicketCharReqVO.setType(null);
        }
        AppTicketChartVO appTicketChartVO = ticketService.getDeviceType(appTicketCharReqVO);
        return ApiResult.success(appTicketChartVO);
    }

    @ApiOperation(value = "故障处理方式统计", notes = "故障处理方式统计")
    @RequestMapping(value = "/getDealWay", method = {RequestMethod.POST,RequestMethod.GET})
    public ApiResult getDealWay(@RequestBody AppTicketCharReqVO appTicketCharReqVO) {
        if(null==appTicketCharReqVO.getType()&& StringUtils.isEmpty(appTicketCharReqVO.getStartTime())
                &&StringUtils.isEmpty(appTicketCharReqVO.getEndTime())){
            appTicketCharReqVO.setType(1);
        }else{
            if(StringUtils.isEmpty(appTicketCharReqVO.getStartTime()) ||StringUtils.isEmpty(appTicketCharReqVO.getEndTime())){
                throw new BusinessException("开始时间或结束时间未传值！");
            }
            appTicketCharReqVO.setType(null);
        }
        AppTicketChartVO appTicketChartVO = ticketService.getDealWay(appTicketCharReqVO);
        return ApiResult.success(appTicketChartVO);
    }


    // ------------------------------------2021/07/22报表相关代码结束--------------------------------------------------------

}
