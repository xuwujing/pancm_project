package com.zans.portal.controller;

import com.zans.base.util.DateHelper;
import com.zans.base.vo.ApiResult;
import com.zans.base.vo.PageResult;
import com.zans.portal.service.IStatsIpAliveService;
import com.zans.portal.vo.stats.IpAliveReportSearchVO;
import com.zans.portal.vo.stats.IpAliveReportVo;
import com.zans.portal.vo.stats.IpAliveResponseVO;
import com.zans.portal.vo.stats.IpAliveSearchVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Api(value = "/stats/alive", tags = {"/stats/alive ~ 网络质量管理"})
@RestController
@RequestMapping("/stats/alive")
@Validated
@Slf4j
public class StatsIpAliveController extends BasePortalController {



    @Autowired
    private IStatsIpAliveService ipAliveService;

    @ApiOperation(value = "/list", notes = "网络质量列表查询")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "req", value = "查询条件", required = true, dataType = "IpAliveSearchVO", paramType = "body")
    })
    @RequestMapping(value = "/list", method = {RequestMethod.POST})
    @ResponseBody
    public ApiResult getList(@RequestBody IpAliveSearchVO req) {
        super.checkPageParams(req, "create_time desc");
        List<String> dateRange = req.getDateRange();
        if (CollectionUtils.isNotEmpty(dateRange)) {
            String[] date = new String[2];
            date[0] = dateRange.get(0);
            date[1] = dateRange.get(1);
            req.setDate(date);
        }
        Date[] timeInterval = getTimeInterval(req, 6, 0);
        PageResult<IpAliveResponseVO> result = ipAliveService.getIpAlivePage(req, timeInterval);
        return ApiResult.success(result);
    }

    @ApiOperation(value = "/getStatsIpAliveReport", notes = "丢包率/平均响应时间 折线图表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "req", value = "查询条件", required = true, dataType = "IpAliveReportSearchVO", paramType = "body")
    })
    @RequestMapping(value = "/getStatsIpAliveReport", method = {RequestMethod.POST})
    @ResponseBody
    public ApiResult statsIpAliveReport(@RequestBody IpAliveReportSearchVO req) {
        IpAliveReportVo result = new IpAliveReportVo();

        IpAliveSearchVO reqVo = new IpAliveSearchVO();
        BeanUtils.copyProperties(req, reqVo);

        Date[] timeInterval = getTimeInterval(reqVo, 6, 1);
        String[] date = new String[timeInterval.length - 1];
        String[] value = new String[timeInterval.length - 1];

        for (int i = 0; i < timeInterval.length; i++) {
            if (i < timeInterval.length - 1) {
                value[i] = String.valueOf(ipAliveService.getPacket(req.getIpAddr(), timeInterval[i], timeInterval[i + 1], req.getType()));
                String time = new SimpleDateFormat("MM-dd HH:mm:ss").format(timeInterval[i + 1]);
                //date[i] = DateHelper.getDateTime(timeInterval[i + 1]);
                date[i] = time;
            }
        }
        result.setDate(date);
        result.setValue(value);
        return ApiResult.success(result);
    }

    /***
     * 把时间范围划分为 'qty' 等分
     * @param req
     * @param qty
     * @param type  0:返回查询时间区间  1：返回等分后的时间区间
     * @return
     */
    public static Date[] getTimeInterval(IpAliveSearchVO req, Integer qty, Integer type) {
        //查询的时间区间
        Date[] timeInterval = new Date[2];
        if (req.getTime() != null && req.getTime() != 0) {
            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.HOUR_OF_DAY, calendar.get(Calendar.HOUR_OF_DAY) - req.getTime());
            timeInterval[0] = calendar.getTime();
            timeInterval[1] = new Date();
        } else if (req.getDate() != null && req.getDate().length > 0) {
            timeInterval[0] = DateHelper.parseDatetime(req.getDate()[0]);
            timeInterval[1] = DateHelper.parseDatetime(req.getDate()[1]);
        } else {  //当不传参并且是查询报表数据时，默认查询最近1个月数据
            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.MONTH, calendar.get(Calendar.MONTH) - 1);
            timeInterval[0] = calendar.getTime();
            timeInterval[1] = new Date();
        }
        if (type == 0) {
            return timeInterval;
        }

        Date[] dates = new Date[qty + 1];
        long longs = timeInterval[1].getTime() - timeInterval[0].getTime();
        long single = longs / qty;
        dates[0] = new Date(timeInterval[0].getTime());
        for (int i = 0; i < qty; i++) {
            long a = timeInterval[0].getTime() + ((i + 1) * single);
            dates[i + 1] = new Date(a);
        }
        return dates;
    }

}
