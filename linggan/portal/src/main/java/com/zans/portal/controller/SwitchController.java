package com.zans.portal.controller;

import com.zans.base.annotion.Record;
import com.zans.base.util.*;
import com.zans.base.vo.ApiResult;
import com.zans.base.vo.PageResult;
import com.zans.base.vo.SelectVO;
import com.zans.portal.model.RadiusNas;
import com.zans.portal.model.SysBrand;
import com.zans.portal.model.SysSwitcher;
import com.zans.portal.model.SysSwitcherBranch;
import com.zans.portal.service.*;
import com.zans.portal.vo.asset.AssetVO;
import com.zans.portal.vo.switcher.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import static com.zans.base.config.EnumErrorCode.CLIENT_PARAMS_ERROR;
import static com.zans.portal.config.GlobalConstants.*;
import static com.zans.portal.constants.PortalConstants.*;

@Api(value = "/switch", tags = {"/switch ~ 交换机"})
@RestController
@RequestMapping("/switch")
@Slf4j
@Validated
public class SwitchController extends BasePortalController {

    @Autowired
    ISwitcherService switchService;
    @Autowired
    ISwitcherBrunchService switcherBrunchService;
    @Autowired
    IAssetService assetService;
    @Autowired
    IAreaService areaService;
    @Autowired
    IRegionService regionService;
    @Autowired
    IConstantItemService constantItemService;
    @Autowired
    private ISysBrandService brandService;
    @Autowired
    private IRadiusNasService radiusNasService;

    @Autowired
    ILogOperationService logOperationService;

    @RequestMapping(value = "/splitVlanInfo", method = {RequestMethod.POST})
    @ResponseBody
    public ApiResult splitVlanInfo(){
        switchService.splitVlanInfoFromConfig();
        return ApiResult.success();
    }

    @ApiOperation(value = "/list", notes = "交换机查询")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "req", value = "查询条件", required = true,
                    dataType = "SwitchSearchVO", paramType = "body")
    })
    @RequestMapping(value = "/list", method = {RequestMethod.POST})
    @ResponseBody
    public ApiResult getList(@Valid @RequestBody SwitchSearchVO req) {
        String sortName = req.getSortName();
        if (StringUtils.isEmpty(sortName)) {
            sortName = " id desc ";
        } else {
            sortName = sortName + " ,id desc ";
        }
        super.checkPageParams(req, sortName);
        List<SelectVO> switcher = constantItemService.findItemsByDict(SWITCHER_TYPE);
        List<SelectVO> portocol = constantItemService.findItemsByDict(SWITCHER_PROTOCOL);

        List<SelectVO> brandList = new ArrayList<>();
        brandService.getAll().forEach(e -> {
            SelectVO vo = new SelectVO();
            vo.setItemKey(e.getBrandId());
            vo.setItemValue(e.getBrandName());
            brandList.add(vo);
        });

        PageResult<SwitchResVO> pageResult = switchService.getSwitchPage(req);

        Map<String, Object> result = MapBuilder.getBuilder()
                .put(INIT_DATA_TABLE, pageResult)
                .put(MODULE_SWITCHER, switcher)
                .put(MODULE_SWITCHER_PORTAL, portocol)
                .put("brand", brandList)
                .build();
        return ApiResult.success(result);
    }


    @ApiOperation(value = "/delete", notes = "删除交换机")
    @ApiImplicitParam(name = "id", value = "id", required = true, dataType = "int", paramType = "query")
    @RequestMapping(value = "/delete", method = {RequestMethod.POST})
    @Record(module = PORTAL_MODULE_SWITCH_BRANCH,operation = PORTAL_LOG_OPERATION_ONE_DELETE)
    public ApiResult delete(@NotNull(message = "id必填") Integer id, HttpServletRequest request) {
        SysSwitcher switcher = switchService.getById(id);
        if (switcher == null) {
            return ApiResult.error(CLIENT_PARAMS_ERROR).appendMessage("交换机不存在#" + id);
        }
        switchService.delete(switcher);

        // 记录日志

        return ApiResult.success().appendMessage("删除成功");
    }


    @ApiOperation(value = "/insert", notes = "新增交换机")
    @ApiImplicitParam(name = "mergeVO", value = "新增交换机", required = true,
            dataType = "SwitchMergeVO", paramType = "body")
    @RequestMapping(value = "/insert", method = {RequestMethod.POST})
    @Record(module = PORTAL_MODULE_SWITCH_BRANCH,operation = PORTAL_LOG_OPERATION_ADD)
    public ApiResult insert(@Valid @RequestBody SwitchMergeVO mergeVO, HttpServletRequest request) {
        Integer radiusConfig = mergeVO.getRadiusConfig();
        SysSwitcher switcher = switchService.findBySwHost(mergeVO.getSwHost(), null);
        if (switcher != null) {
            return ApiResult.error(CLIENT_PARAMS_ERROR).appendMessage("当前Ip已存在#" + mergeVO.getSwHost());
        }
        if (radiusConfig == 1) {
            RadiusNas radiusNas = radiusNasService.getByNasIpAndSwIP(mergeVO.getNasIp(), mergeVO.getSwHost());
            if (radiusNas != null) {
                return ApiResult.error(CLIENT_PARAMS_ERROR).appendMessage("当前NasIp已存在#" + radiusNas.getNasIp());
            }
            RadiusNas nas = new RadiusNas();
            BeanUtils.copyProperties(mergeVO, nas);
            radiusNasService.insert(nas);
        }
        SysSwitcher model = new SysSwitcher();
        BeanUtils.copyProperties(mergeVO, model);
        switchService.insert(model);

        return ApiResult.success(MapBuilder.getSimpleMap("id", model.getId())).appendMessage("请求成功");
    }

    @ApiOperation(value = "/update", notes = "修改交换机")
    @ApiImplicitParam(name = "mergeVO", value = "修改交换机", required = true,
            dataType = "SwitchDetailVO", paramType = "body")
    @RequestMapping(value = "/update", method = {RequestMethod.POST})
    @Record(module = PORTAL_MODULE_SWITCH_BRANCH,operation = PORTAL_LOG_OPERATION_EDIT)
    public ApiResult update(@Valid @RequestBody SwitchUpdateVO mergeVO, HttpServletRequest request) {
        SysSwitcher switcher = switchService.findBySwHost(mergeVO.getSwHost(), mergeVO.getId());
        if (switcher != null) {
            return ApiResult.error(CLIENT_PARAMS_ERROR).appendMessage("当前Ip已存在#" + mergeVO.getSwHost());
        }
        SysSwitcher sysSwitcher = switchService.getById(mergeVO.getId());
        if (sysSwitcher == null) {
            return ApiResult.error(CLIENT_PARAMS_ERROR).appendMessage("当前交换机不存在#" + mergeVO.getId());
        }
        sysSwitcher = SwitchUpdateVO.initSwitch(sysSwitcher, mergeVO);
        switchService.update(sysSwitcher);

        return ApiResult.success(MapBuilder.getSimpleMap("id", sysSwitcher.getId())).appendMessage("请求成功");
    }

    @ApiOperation(value = "/view", notes = "详情")
    @ApiImplicitParam(name = "id", value = "交换机id", required = true, dataType = "int", paramType = "query")
    @RequestMapping(value = "/view", method = {RequestMethod.POST})
    public ApiResult view(@NotNull Integer id, HttpServletRequest request) {
        SwitchDetailVO detailVO = new SwitchDetailVO();
        SysSwitcher switcher = switchService.getById(id);
        BeanUtils.copyProperties(switcher, detailVO);

        SwitcherScanReqVO req = new SwitcherScanReqVO();
        req.setSwId(id);
        SwitcherScanRespVO scan = switchService.getScanBySwId(req);
        String onlineSecond = secondToTime(scan.getOnlineTime());
        String offlineSecond = secondToTime(scan.getOfflineTime());
        scan.setOnlineTime(onlineSecond);
        scan.setOfflineTime(offlineSecond);
        detailVO.setScanRespVO(scan);

        //品牌
        if (switcher.getBrand() != null) {
            SysBrand brand = brandService.getById(switcher.getBrand());
            if (brand != null) {
                detailVO.setBrandName(brand.getBrandName());
            }
        }




        //验收
        SysSwitcherBranch switcherBranch = switcherBrunchService.findBySwHost(switcher.getSwHost(),null);
        AssetVO asset = assetService.findByIpAddr(switcher.getSwHost());
        if (switcherBranch != null){
            detailVO.setPointName(switcherBranch.getPointName());
            detailVO.setRegionName(regionService.getRegionNameById(switcherBranch.getRegion()));
            detailVO.setCommunity(switcherBranch.getCommunity());
            detailVO.setAcceptance(switcherBranch.getAcceptance());
            detailVO.setAcceptIdea(switcherBranch.getAcceptIdea());
            detailVO.setConsBatch(switcherBranch.getConsBatch());
            if (switcherBranch.getAcceptDate()!=null){
                detailVO.setAcceptDate(DateHelper.formatDate(switcherBranch.getAcceptDate(),"yyyy-MM-dd"));
            }
        }
        if (asset != null){
//            detailVO.setStatus(asset.getEnableStatus()==1?0:1);
            detailVO.setStatus(0);
            detailVO.setAlive(asset.getAlive());
            if (StringHelper.isBlank(detailVO.getPointName())){
                detailVO.setPointName(asset.getPointName());
            }
        }
        return ApiResult.success(detailVO);
    }



    @ApiOperation(value = "/test", notes = "1:ssh   2:telnet  3:snmp  4:AAA ")
    @ApiImplicitParam(name = "mergeVO", value = "新增交换机", required = true, dataType = "SwitchTestVO", paramType = "body")
    @RequestMapping(value = "/test", method = {RequestMethod.POST})
    public ApiResult test(@Valid @RequestBody SwitchTestVO mergeVO, HttpServletRequest request) {
        Integer testType = mergeVO.getTestType();
        if (testType == null) {
            return ApiResult.error("未传递测试类型参数");
        }
        Boolean bool = false;
        switch (testType) {
            case 1:
                Boolean config = SshClient.collet(mergeVO.getSwAccount(), mergeVO.getSwPassword(), mergeVO.getSwHost(), mergeVO.getSshPort(), 5000);
                return config ? ApiResult.success("连接成功") : ApiResult.error("连接SSH异常，请检查连接参数");
            case 2:
                bool = TelnetHelper.telnet(mergeVO.getSwHost(), mergeVO.getTelnetPort(), 5000);
                return bool ? ApiResult.success("连接成功") : ApiResult.error("连接Telnet异常，请检查连接参数");
            case 3:
                String oidval = "1.3.6.1.2.1.1.6.0";
                bool = SnmpHelper.snmpGet(mergeVO.getSwHost(), mergeVO.getSwCommunity(), oidval);
                return bool ? ApiResult.success("连接成功") : ApiResult.success("连接SNMP异常，请检查连接参数");
            case 4:
                return bool ? ApiResult.success("连接成功") : ApiResult.error("待研发中");
            default:
                return ApiResult.error("测试类型参数异常");
        }
    }

    @ApiOperation(value = "/scan/list", notes = "交换机扫描查询")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "req", value = "查询条件", required = true,
                    dataType = "SwitcherScanReqVO", paramType = "body")
    })
    @RequestMapping(value = "/scan/list", method = {RequestMethod.POST})
    @ResponseBody
    public ApiResult scan_list(@Valid @RequestBody SwitcherScanReqVO req) throws ParseException {
        //详情
        SwitcherScanRespVO scan = switchService.getScanBySwId(req);
        String onlineSecond = secondToTime(scan.getOnlineTime());
        String offlineSecond = secondToTime(scan.getOfflineTime());
        scan.setOnlineTime(onlineSecond);
        scan.setOfflineTime(offlineSecond);

        //列表
        super.checkPageParams(req, "id desc");
        PageResult<SwitcherScanListRespVO> pageResult = switchService.getSwitchScanPage(req);
        List<SwitcherScanListRespVO> list = pageResult.getList();
        for (SwitcherScanListRespVO respVO : list) {
            String beginTime = respVO.getBeginTime();
            String endTime = respVO.getEndTime();
            String scanTime = respVO.getScanTime();
            if (StringUtils.isNotEmpty(beginTime) && StringUtils.isNotEmpty(scanTime)) {
                beginTime = beginTime.substring(0, beginTime.length() - 2);
                endTime = StringUtils.isNotEmpty(endTime) ? endTime : scanTime;
                endTime = endTime.substring(0, endTime.length() - 2);
                Long time = (DateHelper.parseDatetime(endTime).getTime() - DateHelper.parseDatetime(beginTime).getTime()) / 1000;
                String second = secondToTime(String.valueOf(time));
                respVO.setDurationSeconds(second);
            } else {
                String second = secondToTime(respVO.getDurationSeconds());
                respVO.setDurationSeconds(second);
            }
        }
        pageResult.setList(list);

        //报表
        List<SwitcherScanListRespVO> scanList = switchService.findSwitchScanList(req);

        List<SwitcherScanListRespVO> scanDateHourList = switchService.findSwitchScanDateHourList(req);

        Map map = initDateRange(req.getDay(), req.getDate());
        Date[] dates = (Date[]) map.get("result");
        Integer type = (Integer) map.get("type");

        Map report = initAlive(type == 1 ? scanDateHourList : scanList, dates, type);


        Map<String, Object> result = MapBuilder.getBuilder()
                .put(INIT_DATA_TABLE, pageResult)
                .put("detail", scan)
                .put("report", report)
                .build();
        return ApiResult.success(result);
    }

    public String secondToTime(String durationSeconds) {
        if (StringUtils.isEmpty(durationSeconds)) {
            return "0";
        }
        Integer second = Integer.parseInt(durationSeconds);
        long d = second / (60 * 60 * 24);
        long h = (second - (60 * 60 * 24 * d)) / 3600;
        long m = (second - 60 * 60 * 24 * d - 3600 * h) / 60;
        long s = second - 60 * 60 * 24 * d - 3600 * h - 60 * m;
        String result = (d == 0 ? "" : d + "天") + (h == 0 ? "" : h + "时") + (m == 0 ? "" : m + "分") + (s == 0 ? "" : s + "秒");
        return result;
    }

    /**
     *
     * @param list
     * @param dates
     * @param type==1 小时查询
     * @return
     * @throws ParseException
     */
    public Map initAlive(List<SwitcherScanListRespVO> list, Date[] dates, Integer type) throws ParseException {
        Map report = new HashMap();
        String[] key = new String[dates.length];
        String[] online = new String[dates.length];
        String[] offline = new String[dates.length];
        String[] offlineDd = new String[dates.length];
        String[] offlineDg = new String[dates.length];

        SimpleDateFormat date = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat dateHour = new SimpleDateFormat("yyyy-MM-dd HH");

        List<List<SwitcherOnlineReportVO>> onLineOrOffLineData = getOnLineOrOffLineData(list, type);
        List<SwitcherOnlineReportVO> onlineArray = onLineOrOffLineData.get(0);
        List<SwitcherOnlineReportVO> offlineArray = onLineOrOffLineData.get(1);

        for (int i = 0; i < dates.length; i++) {
            key[i] = date.format(dates[i]);
            if (type == 1) {
                key[i] = dateHour.format(dates[i]);
            }

            for (int j = 0; j < onlineArray.size(); j++) {
                Date start = date.parse(onlineArray.get(j).getBeginTime());
                Date end = date.parse(onlineArray.get(j).getEndTime());
                if (type == 1) {
                    start = dateHour.parse(onlineArray.get(j).getBeginTime());
                    end = dateHour.parse(onlineArray.get(j).getEndTime());
                }
                int offlineType = onlineArray.get(j).getOfflineType();
                if (dates[i].getTime() >= start.getTime() && dates[i].getTime() <= end.getTime()) {
                    online[i] = "1";
                    offlineDd[i]="0";
                    offlineDg[i]="0";
                    break;
                }
                online[i] = "0";
                offlineDd[i]=offlineType==1?"1":"0";
                offlineDg[i]=offlineType==0?"1":"0";
            }

            if (type != 1) {
                //以离线状态为主，同样是17号在线，17号离线，以离线数据为主
                for (int j = 0; j < offlineArray.size(); j++) {
                    Date start = date.parse(offlineArray.get(j).getBeginTime());
                    Date end = date.parse(offlineArray.get(j).getEndTime());
                    int offlineType = offlineArray.get(j).getOfflineType();
                    if (dates[i].getTime() >= start.getTime() && dates[i].getTime() <= end.getTime()) {
                        online[i] = "0";
                        offlineDd[i]=offlineType==1?"1":"0";
                        offlineDg[i]=offlineType==0?"1":"0";
                        break;
                    }
                }
            }

        }

        report.put("x_coordinate", key);
        report.put("y_online", online);
        report.put("y_offline", offline);
        report.put("y_offline_dd", offlineDd);
        report.put("y_offline_dg", offlineDg);
        return report;
    }
    //获取在线和离线时间数组
    public List<List<SwitcherOnlineReportVO>> getOnLineOrOffLineData(List<SwitcherScanListRespVO> scan, Integer type) throws ParseException {
        List<List<SwitcherOnlineReportVO>> result = new ArrayList<>();
        List<SwitcherOnlineReportVO> onlineArray = new ArrayList<>();
        List<SwitcherOnlineReportVO> offArray = new ArrayList<>();

        SimpleDateFormat date = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat dateHour = new SimpleDateFormat("yyyy-MM-dd HH");

        for (int i = 0; i < scan.size(); i++) {
            SwitcherOnlineReportVO online = new SwitcherOnlineReportVO();
            SwitcherOnlineReportVO offline = new SwitcherOnlineReportVO();
            Boolean isAlive = scan.get(i).getAlive() == 1;
            online.setOfflineType(scan.get(i).getOfflineType());
            offline.setOfflineType(scan.get(i).getOfflineType());

            online.setBeginTime(  isAlive ? scan.get(i).getBeginTime() : null);
            offline.setBeginTime( !isAlive ? scan.get(i).getBeginTime() : null);

            if (i == scan.size() - 1) { //最后一条数据没有对比数据，如果endTime为空，取当前时间
                String lineDate = type == 1 ? dateHour.format(new Date()) : date.format(new Date());
                if (isAlive) {
                    online.setBeginTime(scan.size() == 1 ? lineDate : online.getBeginTime());
                    online.setEndTime(lineDate);
                    onlineArray.add(online);
                } else {
                    offline.setBeginTime(scan.size() == 1 ? lineDate : offline.getBeginTime());
                    offline.setEndTime(lineDate);
                    offArray.add(offline);
                }
            }

            for (int j = i + 1; j < scan.size(); j++) {
                String beginTime = scan.get(j).getBeginTime();
                String endTime = scan.get(j).getEndTime();

                //上一条数据和当前数据是同样的状态
                if (scan.get(i).getAlive().equals(scan.get(j).getAlive())) {
                    String useTime = StringUtils.isEmpty(endTime) ? beginTime : endTime;
                    online.setEndTime( isAlive ? useTime : online.getEndTime());
                    offline.setEndTime(!isAlive ? useTime : offline.getEndTime());;
                } else {
                    if (isAlive) {
                        online.setEndTime(beginTime);
                        onlineArray.add(online);
                        i = j - 1;
                        break;
                    } else {
                        offline.setEndTime(beginTime);
                        offArray.add(offline);
                        i = j - 1;
                        break;
                    }
                }

                //如果走到最后一条数据，并且最后一条数据的endTime为null，设置endTime为当前时间
                if (j == (scan.size() - 1) && endTime == null) {
                    String dates = type == 1 ? dateHour.format(new Date()) : date.format(new Date());
                    online.setEndTime( isAlive ? dates : online.getEndTime());;
                    offline.setEndTime( !isAlive ? dates : offline.getEndTime());
                    break;
                }

            }
        }
        result.add(onlineArray);
        result.add(offArray);
        return result;
    }


    public Map initDateRange(Integer day, String[] dates) {
        Map res = new HashMap();
        String[] date = new String[2];
        if (dates != null && dates.length > 0) {
            date = dates;
        } else {
            if (day == null) {
                //默认查7天的数据
                day = 6;
            }
            Calendar c = Calendar.getInstance();
            Date now = new Date();
            c.setTime(now);
            c.add(Calendar.DATE, -day);
            Date dateBefore = c.getTime();
            date[0] = DateHelper.formatDate(dateBefore, "yyyy-MM-dd");
            date[1] = DateHelper.formatDate(now, "yyyy-MM-dd");
        }
        Date[] result;
        Calendar c = Calendar.getInstance();
        //两个日期相差多少天
        int days = (int) ((DateHelper.parseDay(date[1]).getTime() - DateHelper.parseDay(date[0]).getTime()) / (1000 * 3600 * 24)) + 1;
        if (days < 2) {
            res.put("type", 1);
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH");
            result = new Date[24];
            for (int i = 0; i < 24; i++) {
                try {
                    result[i] = format.parse(date[1] + " " + i);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        } else {
            res.put("type", 2);
            result = new Date[days];
            c.setTime(DateHelper.parseDay(date[0]));
            result[0] = DateHelper.parseDay(date[0]);
            for (int i = 1; i < days; i++) {
                c.add(Calendar.DATE, +1);
                result[i] = c.getTime();
            }
        }
        res.put("result", result);
        return res;
    }


}
