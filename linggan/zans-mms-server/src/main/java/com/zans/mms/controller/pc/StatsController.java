package com.zans.mms.controller.pc;

import com.zans.base.controller.BaseController;
import com.zans.base.exception.BusinessException;
import com.zans.base.util.DateHelper;
import com.zans.base.vo.ApiResult;
import com.zans.mms.service.IAssetSubsetStatsService;
import com.zans.mms.service.ITicketService;
import com.zans.mms.vo.ticket.AppDispatchTicketChartVO;
import com.zans.mms.vo.ticket.AppTicketCharReqVO;
import com.zans.mms.vo.ticket.AppTicketChartVO;
import com.zans.mms.vo.ticket.chart.ApprovedTicketRepVO;
import com.zans.mms.vo.ticket.chart.TicketApprovedChartRepVO;
import com.zans.mms.vo.ticket.chart.TicketChartReqVO;
import com.zans.mms.vo.ticket.chart.TicketFaultTicketChartRepVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

/**
* @Title: StatsController
* @Description: 报表统计 controller
* @Version:1.0.0
* @Since:jdk1.8
* @author beiming
* @date 4/22/21
*/
@Api(value = "/stats", tags = {"/报表统计"})
@RestController
@RequestMapping("/stats")
@Validated
@Slf4j
public class StatsController extends BaseController {


    @Autowired
    IAssetSubsetStatsService assetSubsetStatsService;


    @Autowired
    ITicketService ticketService;


    @ApiOperation(value = "/statsTwoWeekOnline", notes = "")
    @RequestMapping(value = "/statsTwoWeekOnline", method = {RequestMethod.GET})
    public ApiResult statsTwoWeekOnline(@RequestParam( value = "weekDay" ,defaultValue = "1") Integer weekDay ) {
        return assetSubsetStatsService.statsTwoWeek(weekDay);
    }


    @ApiOperation(value = "/faultTicket", notes = "故障工单统计")
    @RequestMapping(value = "/faultTicket", method = {RequestMethod.POST})
    public ApiResult faultTicket(@RequestBody TicketChartReqVO ticketChartReqVO) {
        return ticketService.faultTicket(ticketChartReqVO);
    }

    @ApiOperation(value = "/faultTicketTable", notes = "故障工单统计表格")
    @RequestMapping(value = "/faultTicketTable", method = {RequestMethod.POST})
    public ApiResult faultTicketTable(@RequestBody TicketChartReqVO ticketChartReqVO) {
        return ticketService.faultTicketTable(ticketChartReqVO);
    }

    @ApiOperation(value = "/ticketPie", notes = "工单统计饼图")
    @RequestMapping(value = "/ticketPie", method = {RequestMethod.POST})
    public ApiResult ticketPie(@RequestBody TicketChartReqVO ticketChartReqVO) {
        return ticketService.ticketPie(ticketChartReqVO);
    }

    @ApiOperation(value = "导出工单报表", notes = "导出工单报表")
    @RequestMapping(value = "ticket/export", method = RequestMethod.POST)
    public void ticketExport(@RequestBody TicketChartReqVO ticketChartReqVO, HttpServletRequest request, HttpServletResponse response ) throws Exception {
        String date = DateHelper.formatDate(new Date(), "yyyyMMddHHmmss");
        String fileName = "工单报表（"+ticketChartReqVO.getStartTime()+"至"+ticketChartReqVO.getEndTime()+")" +"导出序列号："+ date+".xls";
        String filePath=ticketService.exportTicketReport(ticketChartReqVO,fileName);
        this.download(filePath, fileName, request, response);
    }

    @ApiOperation(value = "派工单据实结算工单柱状图统计", notes = "派工单据实结算工单柱状图统计")
    @RequestMapping(value = "/dispatchTicket", method = {RequestMethod.POST,RequestMethod.GET})
    public ApiResult dispatchTicket (@RequestBody AppTicketCharReqVO appTicketCharReqVO) {
        if(StringUtils.isEmpty(appTicketCharReqVO.getStartTime())||StringUtils.isEmpty(appTicketCharReqVO.getEndTime())){
            return ApiResult.error("时间传值错误！");
        }
        String startTime = appTicketCharReqVO.getStartTime()+" 00:00:00";
        String EndTime = appTicketCharReqVO.getEndTime()+" 23:59:59";
        appTicketCharReqVO.setStartTime(startTime);
        appTicketCharReqVO.setEndTime(EndTime);
        appTicketCharReqVO.setType(null);
        AppTicketChartVO dispatchTicket = ticketService.dispatchTicket(appTicketCharReqVO);
        //转换为source sourceData格式
        TicketApprovedChartRepVO ticketApprovedChartRepVO  = this.appTicketChart2TicketChartRep(dispatchTicket);
        return ApiResult.success(ticketApprovedChartRepVO);
    }

    /**
     * 小程序数据格式转pc图表数据格式
     * @param dispatchTicket
     * @return
     */
    private TicketApprovedChartRepVO appTicketChart2TicketChartRep(AppTicketChartVO dispatchTicket) {
        TicketApprovedChartRepVO ticketApprovedChartRepVO = new TicketApprovedChartRepVO();
        // 汉口片区 武昌片区 汉阳片区
        List<String> categories = dispatchTicket.getCategories();
        //相关数据 工单申请数量 工单审批数量 单笔>3000 数量 单笔大于10000数量
        List<Map<String, Object>> series = dispatchTicket.getSeries();
        List<Integer> totalList = (List<Integer>) series.get(0).get("data");
        List<Integer> approvedList = (List<Integer>) series.get(1).get("data");
        List<Integer> minList = (List<Integer>) series.get(2).get("data");
        List<Integer> maxList = (List<Integer>) series.get(3).get("data");
        List<ApprovedTicketRepVO> source = new ArrayList<>();
        List<List<Object>> sourceDate = new ArrayList<>();
        List<Object> productList = new ArrayList<>();
        Collections.addAll(productList,"product","汉口片区","武昌片区","汉阳片区");
        sourceDate.add(productList);
        List<String> rowName = new ArrayList<>();
        Collections.addAll(rowName,"工单申请数量","工单审批数量","单笔>3000","单笔>10000");
        for (int i =0;i< categories.size(); i++){
            ApprovedTicketRepVO approvedTicketRepVO = new ApprovedTicketRepVO();
            approvedTicketRepVO.setProduct(categories.get(i));
            approvedTicketRepVO.setTotal(totalList.get(i));
            approvedTicketRepVO.setComplete(approvedList.get(i));
            approvedTicketRepVO.setMin(minList.get(i));
            approvedTicketRepVO.setMax(maxList.get(i));
            source.add(approvedTicketRepVO);

        }
        for(int j=0;j<4;j++){
            List<Object> objectList = new ArrayList<>();
            objectList.add(rowName.get(j));
            objectList.addAll((List<Object>)series.get(j).get("data"));


            sourceDate.add(objectList);
        }


        ticketApprovedChartRepVO.setSource(source);
        ticketApprovedChartRepVO.setSourceData(sourceDate);

        return ticketApprovedChartRepVO;
    }

    @ApiOperation(value = "派工单据实结算工单表格统计", notes = "派工单据实结算工单表格统计")
    @RequestMapping(value = "/dispatchTicketTable", method = {RequestMethod.POST,RequestMethod.GET})
    public ApiResult dispatchTicketTable (@RequestBody AppTicketCharReqVO appTicketCharReqVO) {
        if(StringUtils.isEmpty(appTicketCharReqVO.getStartTime())||StringUtils.isEmpty(appTicketCharReqVO.getEndTime())){
            return ApiResult.error("时间传值错误！");
        }
        String startTime = appTicketCharReqVO.getStartTime()+" 00:00:00";
        String EndTime = appTicketCharReqVO.getEndTime()+" 23:59:59";
        appTicketCharReqVO.setStartTime(startTime);
        appTicketCharReqVO.setEndTime(EndTime);
        appTicketCharReqVO.setType(null);
        List<AppDispatchTicketChartVO> map = ticketService.dispatchTicketTable(appTicketCharReqVO);
        return ApiResult.success(map);
    }


    @ApiOperation(value = "验收单据实结算工单柱状图统计", notes = "验收单据实结算工单柱状图统计")
    @RequestMapping(value = "/acceptanceTicket", method = {RequestMethod.POST,RequestMethod.GET})
    public ApiResult acceptanceTicket (@RequestBody AppTicketCharReqVO appTicketCharReqVO) {
        if(StringUtils.isEmpty(appTicketCharReqVO.getStartTime())||StringUtils.isEmpty(appTicketCharReqVO.getEndTime())){
            return ApiResult.error("时间传值错误！");
        }
        String startTime = appTicketCharReqVO.getStartTime()+" 00:00:00";
        String EndTime = appTicketCharReqVO.getEndTime()+" 23:59:59";
        appTicketCharReqVO.setStartTime(startTime);
        appTicketCharReqVO.setEndTime(EndTime);
        appTicketCharReqVO.setType(null);
        AppTicketChartVO acceptanceTicket = ticketService.acceptanceTicket(appTicketCharReqVO);
        //转换为source sourceData格式
        TicketApprovedChartRepVO ticketApprovedChartRepVO  = this.appTicketChart2TicketChartRep(acceptanceTicket);
        return ApiResult.success(ticketApprovedChartRepVO);
    }

    @ApiOperation(value = "验收单据实结算工单表格统计", notes = "验收单据实结算工单表格统计")
    @RequestMapping(value = "/acceptanceTicketTable", method = {RequestMethod.POST,RequestMethod.GET})
    public ApiResult acceptanceTicketTable (@RequestBody AppTicketCharReqVO appTicketCharReqVO) {
        if(StringUtils.isEmpty(appTicketCharReqVO.getStartTime())||StringUtils.isEmpty(appTicketCharReqVO.getEndTime())){
            return ApiResult.error("时间传值错误！");
        }
        String startTime = appTicketCharReqVO.getStartTime()+" 00:00:00";
        String EndTime = appTicketCharReqVO.getEndTime()+" 23:59:59";
        appTicketCharReqVO.setStartTime(startTime);
        appTicketCharReqVO.setEndTime(EndTime);
        appTicketCharReqVO.setType(null);
        List<AppDispatchTicketChartVO> map  = ticketService.acceptanceTicketTable(appTicketCharReqVO);
        return ApiResult.success(map);
    }




}