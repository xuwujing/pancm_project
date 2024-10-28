package com.zans.portal.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.zans.base.util.MapBuilder;
import com.zans.base.vo.ApiResult;
import com.zans.base.vo.PageResult;
import com.zans.base.vo.SelectVO;
import com.zans.portal.config.GlobalConstants;
import com.zans.portal.dao.AssessRecordDao;
import com.zans.portal.dao.AssetMapper;
import com.zans.portal.dao.AssetScanDayMapper;
import com.zans.portal.dao.AssetScanMapper;
import com.zans.portal.model.AssessRecord;
import com.zans.portal.model.AssetScan;
import com.zans.portal.model.AssetScanDay;
import com.zans.portal.service.*;
import com.zans.portal.util.RestTemplateHelper;
import com.zans.portal.vo.asset.req.AssetAssessSearchVO;
import com.zans.portal.vo.asset.resp.AssetAssessResVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.zans.portal.config.GlobalConstants.*;

/**
 * @author beixing
 * @Title: portal
 * @Description:
 * @Version:1.0.0
 * @Since:jdk1.8
 * @date 2021/11/2
 */
@Service
@Slf4j
public class AssessServiceImpl implements IAssessService {

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
    ILogOperationService logOperationService;

//    @Resource
//    private SysSwitcherMapper switcherMapper;

    @Resource
    private AssessRecordDao assessRecordDao;

//    @Resource
//    private NetworkSwitcherScanMapper networkSwitcherScanMapper;

    @Resource
    private AssetScanMapper assetScanMapper;

    @Resource
    private AssetScanDayMapper assetScanDayMapper;

    @Resource
    private AssetMapper assetMapper;

//    @Resource
//    private NetworkSwitcherScanDayDao networkSwitcherScanDayDao;

    @Autowired
    IDeviceTypeService deviceTypeService;

    @Value("${assess.startDate:2022-06-13 00:00:00}")
    private String assessStartTime;



    @Value("${spring.profiles.active}")
    String active;

    @Autowired
    RestTemplateHelper restTemplateHelper;

    @Override
    public ApiResult list(AssetAssessSearchVO reqVO) {

        List<SelectVO> switcher = constantItemService.findItemsByDict(SWITCHER_TYPE);
        List<SelectVO> deviceList = deviceTypeService.findDeviceTypeToSelect();
        List<SelectVO> regionSecondList = areaService.findRegionToSelect(REGION_LEVEL_TWO);
        Integer[] statusArray = {0, 1};
        switcher.forEach(it -> it.setItemValue(it.getItemValue().replace("层", "")));

        List<SelectVO> brandList = new ArrayList<>();
        brandService.getAll().forEach(e -> {
            SelectVO vo = new SelectVO();
            vo.setItemKey(e.getBrandId());
            vo.setItemValue(e.getBrandName());
            brandList.add(vo);
        });

        Page page = PageHelper.startPage(reqVO.getPageNum(), reqVO.getPageSize());

        //如果离线时长有值的话
        if(reqVO.getLastTime()!=null ){
            reqVO.setOnline(0);
        }

        List<AssetAssessResVO> list = assetScanMapper.findAssetAndAssessList(reqVO);
        setAssessRate(reqVO, list);

        //        getSwitchAssessData(reqVO);
        List<SelectVO> maintainStatusList = constantItemService.findItemsByDict(GlobalConstants.SYS_DICT_KEY_MAINTAIN_STATUS);
        Map<String, Object> result = MapBuilder.getBuilder()
                .put(INIT_DATA_TABLE, new PageResult<AssetAssessResVO>(page.getTotal(), page.getResult(), reqVO.getPageSize(), reqVO.getPageNum()))
                .put(MODULE_AREA, new ArrayList<>())
                .put("online", getStickSelectVOS(new Integer[]{0, 1}, new String[]{"离线（断光）",  "在线"}))
                .put(MODULE_SWITCHER, switcher)
                .put(MODULE_REGION_SECOND, regionSecondList)
                .put(MODULE_DEVICE, deviceList)
//                .put(MODULE_STATE_STATUS, stateStatus)
                .put("brand", brandList)
                .put("acceptance", getStickSelectVOS(statusArray, new String[]{"未验收", "已验收"}))
                .put("status", getStickSelectVOS(statusArray, new String[]{"启用", "停用"}))
                .put("maintainStatus", maintainStatusList)
                .put("consBatch", getStickSelectVOS(new Integer[]{1, 2, 3,4,5}, new String[]{"一期", "二期", "新改扩", "新改扩自建", "存量改造"}))
                .build();
//        this.calculateByDay();
        return ApiResult.success(result);
    }

    private void setAssessRate(AssetAssessSearchVO reqVO, List<AssetAssessResVO> list) {
        //需要遍历查询得到断光，断电，申述时长以及考核时长的数据
        String startTime = reqVO.getApproveStartTime();
        String endTime = reqVO.getApproveEndTime();
        int size = getDiffDay(startTime, endTime);
        for (AssetAssessResVO assetAssessResVO : list) {
            String beginTime = assetAssessResVO.getBeginTime();
            String et = assetAssessResVO.getEndTime();
            assetAssessResVO.setLastTime(0);
            if (StringUtils.isEmpty(beginTime) || StringUtils.isEmpty(et)) {
                assetAssessResVO.setLastTime(0);
            } else {
                if(assetAssessResVO.getAlive() ==2){
                    int hour = getDiffHour(beginTime, et);
                    assetAssessResVO.setLastTime(hour);
                }
            }

            AssetAssessResVO scanDayIpStatistics = assetScanMapper.getScanDayByIp(assetAssessResVO.getIpAddr(),reqVO.getApproveStartTime(),reqVO.getApproveEndTime());

            if (scanDayIpStatistics == null){
                scanDayIpStatistics = new AssetAssessResVO();
            }
            assetAssessResVO.setAfflineDuration(scanDayIpStatistics.getAfflineDuration()==null?0:scanDayIpStatistics.getAfflineDuration());
            assetAssessResVO.setAliveDuration(scanDayIpStatistics.getAliveDuration()==null?0:scanDayIpStatistics.getAliveDuration());
            assetAssessResVO.setOfflineDuration(scanDayIpStatistics.getOfflineDuration()==null?0:scanDayIpStatistics.getOfflineDuration());

            Integer afflineDuration = assetAssessResVO.getAfflineDuration();
            // 在线，断光，断电，申述
            Map<String, String> onlineMap = new HashMap<>();
            Map<String, String> afflineMap = new HashMap<>();
            Map<String, String> offlineMap = new HashMap<>();
            Map<String, String> stateMap = new HashMap<>();
            reqVO.setIpAddr(assetAssessResVO.getIpAddr());
            getNetworkSwitcherScans(reqVO, startTime,endTime, onlineMap, afflineMap, offlineMap, stateMap);

            //总时长
            int sumTime = (size + 1) * 24;
            // 考核时长=总时长-(断光时长-申述时长)
            int assessDuration = sumTime - (afflineDuration );
            assetAssessResVO.setAssessDuration(assessDuration);
            //计算考核率
            BigDecimal assessBig = new BigDecimal(assessDuration);
            //这个计算，相等的情况会有浮动的小数点
            BigDecimal assessRate = assessBig.divide(BigDecimal.valueOf(sumTime), 4, RoundingMode.HALF_UP);
            if (sumTime == assessDuration) {
                assessRate = new BigDecimal(1);
            }
            assetAssessResVO.setAssessRate(assessRate.doubleValue());
        }
    }


    /**
     * 获取相差天数,相同天为0
     *
     * @param startTime yyyy-MM-dd hh:mm:ss
     * @param endTime   yyyy-MM-dd hh:mm:ss
     * @return
     */
    private int getDiffDay(String startTime, String endTime) {
        LocalDate l1 = LocalDate.parse(formatDate(startTime));
        LocalDate l2 = LocalDate.parse(formatDate(endTime));
        return (int) ChronoUnit.DAYS.between(l1, l2);
    }

    private List<SelectVO> getStickSelectVOS(Integer[] id, String[] name) {
        List<SelectVO> onlineVo = new ArrayList<>();
        for (int i = 0; i < id.length; i++) {
            SelectVO online1 = new SelectVO();
            online1.setItemKey(id[i]);
            online1.setItemValue(name[i]);
            onlineVo.add(online1);
        }
        return onlineVo;
    }


    @Override
    public ApiResult view(AssetAssessSearchVO reqVO) {
        //需要遍历查询得到断光，断电，申述时长以及考核时长的数据
        String startTime = reqVO.getApproveStartTime();
        String endTime = reqVO.getApproveEndTime();
        int size = getDiffDay(startTime, endTime);
        // 在线，断光，断电，申述
        Map<String, String> onlineMap = new HashMap<>();
        Map<String, String> afflineMap = new HashMap<>();
        Map<String, String> offlineMap = new HashMap<>();
        Map<String, String> stateMap = new HashMap<>();
        //组装数据
        List<AssetScan> networkSwitcherScan = getNetworkSwitcherScans(reqVO, startTime, endTime,onlineMap, afflineMap, offlineMap, stateMap);
        //获取柱状图的数据
        Map<String, Object> map = getBar(size, formatDate(startTime), onlineMap, afflineMap, offlineMap, stateMap);
        Map<String, Object> result = MapBuilder.getBuilder()
                .put("barList", map)
                .put("dataList", networkSwitcherScan)
                .build();
        return ApiResult.success(result);
    }


    public String getOnlineName(Integer alive, Integer offlineType) {
        if (alive != null && alive == 1) {
            return "在线";
        }
        if (offlineType == null || offlineType == 0) {
            return "离线(断光)";
        } else {
            return "离线(掉电)";
        }
    }

    /**
     * @return
     * @Author beixing
     * @Description 根据时间对比，超过30分钟就四舍五入
     * @Date 2021/11/4
     * @Param
     **/
    private int getDiffHour(String startTime, String endTime) {
        LocalDateTime startDate = LocalDateTime.parse(startTime, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        LocalDateTime endDate = LocalDateTime.parse(endTime, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        int hour = (int) ChronoUnit.HOURS.between(startDate, endDate);
        int minutes = (int) ChronoUnit.MINUTES.between(startDate, endDate);
        int t = minutes % 60;
        if (t >= 30) {
            hour++;
        }
        return hour;
    }

    /**
     * @return
     * @Author beixing
     * @Description 计算两个时间间隔多少秒
     * @Date 2021/11/4
     * @Param
     **/
    private int getDiffSeconds(String startTime, String endTime) {
        LocalDateTime startDate = LocalDateTime.parse(startTime, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        LocalDateTime endDate = LocalDateTime.parse(endTime, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        int minutes = (int) ChronoUnit.SECONDS.between(startDate, endDate);
        return minutes;
    }


    /**
     * @return
     * @Author beixing
     * @Description 获取总时长
     * @Date 2021/11/5
     * @Param
     **/
    private Integer getSumHour(int size, String startDate, Map<String, String> map) {
        int sum = 0;
        String startTime = getStartTime(0, formatDate(startDate));
        String endTime = getEndTime(0, formatDate(startDate));
        for (int i = 0; i < size; i++) {
            //计算 在线，断光，断电，申述各个时间的情况
            Integer duration = calculateTime(startTime, endTime, map);
            sum += duration;
            if(sum>=24){
                return 24;
            }
        }
        return sum;
    }




    //获取柱状图的数据
    private Map<String, Object> getBar(int size, String
            startDate, Map<String, String> onlineMap, Map<String, String> afflineMap, Map<String, String> offlineMap, Map<String, String> stateMap) {
        // 在线，断光，断电，申述
        List<Integer> onlineRate = new ArrayList<Integer>();
        List<Integer> afflineRate = new ArrayList<Integer>();
        List<Integer> offlineRate = new ArrayList<Integer>();
        List<Integer> stateRate = new ArrayList<Integer>();
        for (int i = 0; i <= size; i++) {
            String startTime = getStartTime(i, startDate);
            String endTime = getEndTime(i, startDate);
            //计算 在线，断光，断电，申述各个时间的情况
            Integer online = getSumHour(onlineMap.size(), startTime, onlineMap);
            Integer affline = getSumHour(afflineMap.size(), startTime, afflineMap);
            Integer offline = getSumHour(offlineMap.size(), startTime, offlineMap);
            Integer state = getSumHour(stateMap.size(), startTime, stateMap);
            onlineRate.add(online);
            afflineRate.add(affline);
            offlineRate.add(offline);
            stateRate.add(state);
        }
        // 得到x轴的日期
        List<String> withDate = getDaysBetween(size, startDate);
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("onlineRate", onlineRate);
        map.put("afflineRate", afflineRate);
        map.put("offlineRate", offlineRate);
        map.put("stateRate", stateRate);
        map.put("withDate", withDate);
        return map;
    }

    private String getMinusHours(int hour, String time, String format) {
        return LocalDateTime.parse(time, DateTimeFormatter.ofPattern(format)).minusHours(hour).format(DateTimeFormatter.ofPattern(format));
    }

    private String getStartTime(int i, String startDate) {
        String time = LocalDate.parse(startDate).plusDays(i).format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        return time.concat(" 00:00:00");
    }

    private String getEndTime(int i, String startDate) {
        String time = LocalDate.parse(startDate).plusDays(i).format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        return time.concat(" 23:59:59");
    }

    /**
     * 计算每天时间的在线/离线时间
     *
     * @param startTime 一天开始时间 00:00:00
     * @param endTime   一天结束时间  23:59:59
     * @param map       时间区间开始,时间区间结束
     * @return
     */
    private Integer calculateTime(String startTime, String endTime, Map<String, String> map) {
        for (Map.Entry<String, String> entry : map.entrySet()) {
            String startDate = entry.getKey();
            String endDate = entry.getValue();
            //判断是否是同一天
            boolean isToday = formatDate(startTime).equals(formatDate(startDate)) || formatDate(startTime).equals(formatDate(endDate));
            boolean isStart = startTime.compareTo(startDate) > -1 && !startTime.equals(startDate);
            boolean isEnd = endDate.compareTo(endTime) > -1;
            //包含就是一整天
            if (isStart && isEnd) {
//                map.remove(startDate);
                return 24;
            }
            //非包含就是不到一天，需要计算，分钟四舍五入，30分钟进一
            if (isToday && !isStart && isEnd) {
                int hour = getDiffHour(startDate, endTime);
                map.remove(startDate);
                String newStartTime = getStartTime(1,formatDate(startDate));
                map.put(newStartTime,endDate);
                return hour;
            }

            if (isToday && !isEnd && isStart) {
                int hour = getDiffHour(startTime, endDate);
                map.remove(startDate);
                String newEndTime = getEndTime(-1,formatDate(startTime));
                map.put(startDate,newEndTime);
                return hour;
            }
            //起止和结束在改天内
            if (isToday && !isEnd && !isStart) {
                int hour = getDiffHour(startDate, endDate);
                map.remove(startDate);
                return hour;
            }

        }
        return 0;
    }


    /**
     * @return
     * @Author beixing
     * @Description 获取相邻的天
     * @Date 2021/11/4
     * @Param
     **/
    private static List<String> getDaysBetween(int days, String startDate) {
        List<String> dayss = new ArrayList<>();
        String formatDate = "MM-dd";
        for (int i = 0; i <= days; i++) {
            LocalDate localDate = LocalDate.parse(startDate, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            localDate = localDate.plusDays(i);
            dayss.add(localDate.format(DateTimeFormatter.ofPattern(formatDate)));
        }
        return dayss;
    }


    private String formatDate(String time) {
        return time.substring(0, 10);
    }


    private static List<Integer> getHour(int hour) {
        List<Integer> hours = new ArrayList<>();
        for (int i = 1; i <= hour; i++) {
            hours.add(i);
        }
        return hours;
    }

    @Override
    public ApiResult chartView(AssetAssessSearchVO req) {
        Map<String, Object> params = new HashMap<>();
        params.put("withHour", getHour(24));
        String startTime = req.getApproveStartTime();
        String endTime = req.getApproveEndTime();
        // 在线，断光，断电，申述
        List<Integer> onlineRate = new ArrayList<Integer>();
        List<Integer> afflineRate = new ArrayList<Integer>();
        List<Integer> offlineRate = new ArrayList<Integer>();
        List<Integer> stateRate = new ArrayList<Integer>();

        // 在线，断光，断电，申述
        Map<String, String> onlineMap = new HashMap<>();
        Map<String, String> afflineMap = new HashMap<>();
        Map<String, String> offlineMap = new HashMap<>();
        Map<String, String> stateMap = new HashMap<>();
        getNetworkSwitcherScans(req, startTime,endTime, onlineMap, afflineMap, offlineMap, stateMap);


        for (int i = 0; i < 1; i++) {
            Integer online = calculateTime(startTime, endTime, onlineMap);
            Integer affline = calculateTime(startTime, endTime, afflineMap);
            Integer offline = calculateTime(startTime, endTime, offlineMap);
            Integer state = calculateTime(startTime, endTime, stateMap);
            onlineRate.add(online);
            afflineRate.add(affline);
            offlineRate.add(offline);
            stateRate.add(state);
        }
        params.put("onlineRate", onlineRate);
        params.put("afflineRate", afflineRate);
        params.put("offlineRate", offlineRate);
        params.put("stateRate", stateRate);

        Map<String, Object> result = MapBuilder.getBuilder()
                .put("barNodeList", params)
                .build();
        return ApiResult.success(result);
    }

    @Override
    public ApiResult stateApply(AssessRecord req) {
        return null;
    }

    @Override
    public ApiResult stateView(AssessRecord req) {
        return null;
    }

    @Override
    public ApiResult approve(AssessRecord req) {
        return null;
    }

    @Override
    public ApiResult calculateByDay() {
        String nowDay = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:ss"));
        AssetScanDay networkSwitcherScanDay = assetScanDayMapper.queryLast();
        String startDay = assessStartTime;
//        String startDay = "2022-06-01 00:00:00";
        if (networkSwitcherScanDay != null) {
            startDay = networkSwitcherScanDay.getYyyymmdd();
        }
        int diffDays = getDiffDay(startDay, nowDay);
        long stt = System.currentTimeMillis();
        log.info("考核开始计算！开始时间:{}，结束时间:{}", startDay, nowDay);
        for (int i = 0; i <= diffDays; i++) {
            String day = formatDate(startDay);
            String startTime = getStartTime(i, day);
            String endTime = getEndTime(i, day);
            long st = System.currentTimeMillis();
            AssetAssessSearchVO searchVO = new AssetAssessSearchVO();
            searchVO.setApproveStartTime(startTime);
            searchVO.setApproveEndTime(endTime);
            if (deal(searchVO)) {
                continue;
            }
            log.info("考核已完成:{} 数据的计算！花费时长:{}s", startTime, (System.currentTimeMillis() - st) / 1000);
        }
        log.info("考核计算完毕！花费总时长:{}s", (System.currentTimeMillis() - stt) / 1000);

        clearIp();

        return ApiResult.success();
    }

    @Override
    public PageResult<AssetAssessResVO> getAssessExport(AssetAssessSearchVO reqVO) {
        Page page = PageHelper.startPage(reqVO.getPageNum(), reqVO.getPageSize());

        //如果离线时长有值的话
        if(reqVO.getLastTime()!=null ){
            reqVO.setOnline(0);
        }
        List<AssetAssessResVO> list = assetScanMapper.findAssetAndAssessList(reqVO);
        setAssessRate(reqVO, list);
        return new PageResult<AssetAssessResVO>(page.getTotal(), page.getResult(), reqVO.getPageSize(), reqVO.getPageNum());
    }

    private boolean deal(AssetAssessSearchVO searchVO) {

        try {
            List<AssetScanDay> networkSwitcherScanDayArrayList = getSwitchAssessData(searchVO);
            assetScanDayMapper.insertOrUpdateBatch(networkSwitcherScanDayArrayList);
        } catch (Exception e) {
            log.error("考核任务定时更新失败!请求参数:{}", searchVO, e);
            return true;
        }
        return false;
    }

    // 删除重复的ip数据
    private void clearIp() {
        List<AssetScan> list = assetScanMapper.findRepetitionIp();
        if (list != null && list.size()>0){
            for (AssetScan networkSwitcherScan : list) {
                String ip = networkSwitcherScan.getIpAddr();
                assetScanMapper.deleteRepetitionIp(ip, networkSwitcherScan.getBeginTime());
                log.warn("存在在同一时间重复的写入的IP:{},已删除!",ip);
            }
        }
    }

    /**
     * 获取断光，断电申述时长
     *
     * @param reqVO
     */
    private List<AssetScanDay> getSwitchAssessData(AssetAssessSearchVO reqVO) {

        List<AssetAssessResVO> list = assetScanMapper.findAssetAndAssessList(reqVO);

        //需要遍历查询得到断光，断电，申述时长以及考核时长的数据
        String startTime = reqVO.getApproveStartTime();
        String endTime = reqVO.getApproveEndTime();
        int size = getDiffDay(startTime, endTime);
        List<AssetScanDay> scanDayArrayList = new ArrayList<>();

        for (AssetAssessResVO assessResVO : list) {

            AssetScanDay day = new AssetScanDay();

            String ip = assessResVO.getIpAddr();
            // 在线，断光，断电，申述
            Map<String, String> onlineMap = new HashMap<>();
            Map<String, String> afflineMap = new HashMap<>();
            Map<String, String> offlineMap = new HashMap<>();
            Map<String, String> stateMap = new HashMap<>();
            reqVO.setIpAddr(ip);
            getNetworkSwitcherScans(reqVO, startTime,endTime, onlineMap, afflineMap, offlineMap, stateMap);


            //获取离线断电时长的统计
            Integer offlineDuration = getSumHour(offlineMap.size(), formatDate(startTime), offlineMap);
            assessResVO.setOfflineDuration(offlineDuration == null ? 0 : offlineDuration);
            //获取断光时长的统计
            Integer afflineDuration = getSumHour(afflineMap.size(), formatDate(startTime), afflineMap);
//            Integer afflineDuration = getSumHour(size,formatDate(startTime),afflineMap)+approveDuration;
            assessResVO.setAfflineDuration(afflineDuration == null ? 0 : afflineDuration);
            //获取在线时长的统计
            Integer aliveDuration = getSumHour(onlineMap.size(), formatDate(startTime), onlineMap);
            assessResVO.setAliveDuration(aliveDuration == null ? 0 : aliveDuration);
            //总时长
            int sumTime = (size + 1) * 24;
            // 考核时长=总时长-(断光时长-申述时长)
            int assessDuration = sumTime - afflineDuration;
            assessResVO.setAssessDuration(assessDuration);
            //计算考核率
            BigDecimal assessBig = new BigDecimal(assessDuration);
            //这个计算，相等的情况会有浮动的小数点
            BigDecimal assessRate = assessBig.divide(BigDecimal.valueOf(sumTime), 4, RoundingMode.HALF_UP);
            if (sumTime == assessDuration) {
                assessRate = new BigDecimal(1);
            }
            assessResVO.setAssessRate(assessRate.doubleValue());
//            try{
//                //微信推送
//                wechatPush(switchAssessResVO, ip);
//            }catch (Exception e){
//                log.error("微信发送失败!ip:{}",ip,e);
//            }
            //将数据copy出来

            BeanUtils.copyProperties(assessResVO, day);
            day.setYyyymmdd(formatDate(startTime));
            day.setIpAddr(ip);
            scanDayArrayList.add(day);
        }
        return scanDayArrayList;
    }

    /**
     * @return
     * @Author beixing
     * @Description 根据时间对数据进行计算切分
     * @Date 2021/11/5
     * @Param
     **/
    private List<AssetScan> getNetworkSwitcherScans(AssetAssessSearchVO reqVO, String
            startTime,String endTime, Map<String, String> onlineMap, Map<String, String> afflineMap, Map<String, String> offlineMap, Map<String, String> stateMap) {
        //获取列表的数据
        List<AssetScan> scanList = assetScanMapper.findLastScanRecordByIpAndTime(reqVO.getIpAddr(), startTime);
        if (scanList == null) {
            scanList = new ArrayList<>();
        }
        for (AssetScan assetScan : scanList) {

            String beginTime = assetScan.getBeginTime();
            String endTime2 = assetScan.getEndTime();
            // 条件的开始时间比查询的开始时间大
            boolean isStart = formatDate(beginTime).compareTo(endTime)>-1;
            // 查询的开始时间比条件的开始时间大
            if (startTime == null || endTime2 == null) {
                continue;
            }
            boolean isEnd = formatDate(startTime).compareTo(endTime2)>-1 && formatDate(endTime2).compareTo(formatDate(endTime))>-1;
            //排除非当天的起止时间
            if(isStart || isEnd ){
                continue;
            }

            if (startTime.compareTo(beginTime) > 0) {
                beginTime = startTime;
            }


            int diffHour = getDiffHour(beginTime, endTime2);
            assetScan.setDiffHour(diffHour);
            //开始时间取大值，因为考虑到切分的问题
            assetScan.setBeginTime(beginTime);

            Integer alive = assetScan.getAlive();
            Integer offlineType = 0;
            assetScan.setOnlineName(getOnlineName(alive, offlineType));
            String bt = assetScan.getBeginTime();
            String et = assetScan.getEndTime();
            //在线
            if (alive == 1) {
                onlineMap.put(bt, et);
                continue;
            }

            // 离线(断光)
            if (alive == 2 && offlineType == 0) {
                afflineMap.put(bt, et);
//                //如果进行了申述，那么申述就需要添加
//                if (approveDuration > 0) {
//                    String net = getMinusHours(approveDuration, et, "yyyy-MM-dd HH:mm:ss");
//                    afflineMap.put(bt, net);
//                    stateMap.put(net, et);
//                }
                continue;
            }

            // 离线(断电)
            if (alive == 2 && offlineType == 1) {
                offlineMap.put(bt, et);
                continue;
            }
        }
        return scanList;
    }


    public static void main(String[] args) {
        String startTime = "2021-11-03 12:00:00";
        String endTime = "2021-11-04 13:10:00";
        String endTime2 = "2021-11-04 14:30:00";
        LocalDateTime startDate = LocalDateTime.parse(startTime, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        LocalDateTime endDate = LocalDateTime.parse(endTime2, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        int days = (int) ChronoUnit.DAYS.between(startDate, endDate);
        int hour = (int) ChronoUnit.HOURS.between(startDate, endDate);
        int minutes = (int) ChronoUnit.MINUTES.between(startDate, endDate);
        int t = minutes % 60;
        if (t >= 30) {
            hour++;
        }
        System.out.println("days:" + days + " 小时：" + hour + " 分钟：" + minutes + "  t:" + t);
    }
}
