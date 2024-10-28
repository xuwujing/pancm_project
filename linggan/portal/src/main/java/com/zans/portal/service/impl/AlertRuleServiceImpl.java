package com.zans.portal.service.impl;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.zans.base.util.MapBuilder;
import com.zans.base.vo.ApiResult;
import com.zans.base.vo.PageResult;
import com.zans.base.vo.SelectVO;
import com.zans.portal.config.PoolTaskConfig;
import com.zans.portal.controller.RadiusQzController;
import com.zans.portal.dao.*;
import com.zans.portal.model.VulHostVuln;
import com.zans.portal.model.VulHost;
import com.zans.portal.service.IAlertRuleService;
import com.zans.portal.service.IRadiusEndPointService;
import com.zans.portal.service.ISysConstantService;
import com.zans.portal.vo.AlertRuleStrategyReqVO;
import com.zans.portal.vo.alert.*;
import com.zans.portal.vo.asset.guardline.resp.AssetGuardLineStrategyRespVO;
import com.zans.portal.vo.chart.CircleUnit;
import com.zans.portal.vo.chart.CountUnit;
import com.zans.portal.vo.radius.EndPointViewVO;
import com.zans.portal.vo.radius.QzRespVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author pancm
 * @Title: portal
 * @Description:
 * @Version:1.0.0
 * @Since:jdk1.8
 * @date 2020/8/27
 */
@Service
@Slf4j
public class AlertRuleServiceImpl implements IAlertRuleService {
    public static final String ALERT_RECOVER_URI = "/alert/record/recover";
    public static final String ALERT_RECOVER_ADD_URI="/alert/record/add";

    @Autowired
    private AlertRuleMapper mapper;

    @Autowired
    private RadiusEndpointMapper radiusEndpointMapper;

    @Autowired
    private IRadiusEndPointService radiusEndPointService;


    @Autowired
    private RadiusQzController radiusQzController;


    @Autowired
    ISysConstantService constantService;


    @Autowired
    PoolTaskConfig poolTaskConfig;


    @Resource
    private VulHostDao vulHostDao;

    @Resource
    private VulHostVulnDao vulHostVulnDao;


    @Override
    public PageResult<AlertRecordRespVO> getAlertRecordPage(AlertRecordSearchVO reqVO) {
        Page page = PageHelper.startPage(reqVO.getPageNum(), reqVO.getPageSize());
        if (!StringUtils.isEmpty(reqVO.getMac())) {
            reqVO.setMac(StringUtils.trimAllWhitespace(reqVO.getMac()));
        }
        List<AlertRecordRespVO> alertRecordRespVOList = new ArrayList<>();
        alertRecordRespVOList = mapper.getAlertRecord(reqVO);

        return new PageResult<AlertRecordRespVO>(page.getTotal(), alertRecordRespVOList, reqVO.getPageSize(), reqVO.getPageNum());
    }


    /**
     * @return com.zans.base.vo.ApiResult
     * @Author pancm
     * @Description 界面初始化
     * @Date
     * @Param [status]
     **/
    @Override
    public ApiResult getAlertRecordInit(int status) {
        List<AlertReportDisRespVO> resultList = new ArrayList<>();
        List<AlertReportDisRespVO> typeRespVO = null;
        if (status == 0) {
            typeRespVO = mapper.getAlertRecordType();
            resultList.addAll(typeRespVO);
            Map<String, Object> result = MapBuilder.getBuilder()
                    .put("type", resultList)
                    .build();
            return ApiResult.success(result);
        }

        typeRespVO = mapper.getAlertRecordType2();
        long total = typeRespVO.get(0).getValue();
        BigDecimal rateTotal = new BigDecimal(total);
        if (typeRespVO != null) {
            for (int i = 1; i < typeRespVO.size(); i++) {
                long value = typeRespVO.get(i).getValue();
                BigDecimal rate = new BigDecimal(value);
                if (value != 0 && total != 0) {
                    rate = rate.divide(rateTotal, 4, RoundingMode.HALF_UP);
                }
                typeRespVO.get(i).setObj(rate);
            }
        }
        Map<String, Object> result = MapBuilder.getBuilder()
                .put("type", typeRespVO)
                .build();
        return ApiResult.success(result);
    }


    @Override
    public ApiResult getAlertRecordRuleInit() {
        List<AlertReportRespVO> alertReportRespVOS = mapper.getAlertRecordRule();
        long total = mapper.getAlertRecordNotDealCount();
        if (alertReportRespVOS == null || total < 1) {
            return ApiResult.success(new ArrayList<>());
        }
        for (AlertReportRespVO alertReportRespVO : alertReportRespVOS) {
            long value = alertReportRespVO.getValue();
            BigDecimal alertRate = new BigDecimal(value);
            BigDecimal totalRate = new BigDecimal(total);
            if (alertRate.intValue() != 0 && totalRate.intValue() != 0) {
                alertRate = alertRate.divide(totalRate, 4, RoundingMode.HALF_UP);
            }
            alertReportRespVO.setVal(alertRate);
        }

        return ApiResult.success(alertReportRespVOS);
    }

    @Override
    public ApiResult getAlertRecordRecent() {
        List<String> days = getDaysBetwwen(6);
        String[] dates = days.toArray(new String[7]);
        List<String[]> units = new ArrayList<>();
        for (String date : dates) {
            String[] unit = new String[3];
            CountUnit alertTotalByDate = mapper.findAlertTotalByDate(date);
            CountUnit alertDealByDate = mapper.findAlertDealByDate(date);
            unit[0] = date.substring(5, date.length());
            unit[1] = String.valueOf(alertTotalByDate.getVal());
            unit[2] = String.valueOf(alertDealByDate.getVal());
            units.add(unit);
        }
        return ApiResult.success(units);
    }

    private List<String> getDaysBetwwen(int days) {
        List<String> dayss = new ArrayList<>();
        Calendar start = Calendar.getInstance();
        start.setTime(getDateAdd(days));
        Long startTIme = start.getTimeInMillis();
        Calendar end = Calendar.getInstance();
        end.setTime(new Date());
        Long endTime = end.getTimeInMillis();
        Long oneDay = 1000 * 60 * 60 * 24L;
        Long time = startTIme;
        while (time <= endTime) {
            Date d = new Date(time);
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            dayss.add(df.format(d));
            time += oneDay;
        }
        return dayss;
    }

    private Date getDateAdd(int days) {
        Calendar c = Calendar.getInstance();
        c.add(Calendar.DAY_OF_MONTH, -days);
        return c.getTime();
    }

    @Override
    public PageResult<AlertRecordRespVO> getAlertRecordPageTop(AlertRecordSearchVO reqVO) {
        Page page = PageHelper.startPage(reqVO.getPageNum(), reqVO.getPageSize());
        mapper.getAlertRecordTop(reqVO);
        return new PageResult<AlertRecordRespVO>(page.getTotal(), page.getResult(), reqVO.getPageSize(), reqVO.getPageNum());
    }


    private String getPortId(String nasPortId) {
        String[] portIds = nasPortId.split(";");
        if (portIds.length < 3) {
            return nasPortId;
        }
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < 3; i++) {
            String sp = portIds[i];
            if (i > 0) {
                sb.append("/");
            }
            sb.append(substringAfter(sp, "="));
        }
        return sb.toString();
    }

    /**
     * @return java.lang.String
     * @Author pancm
     * @Description 获取截取字符串后面的值
     * @Date 2020/7/30
     * @Param [str, separator]
     **/
    private String substringAfter(final String str, final String separator) {
        if (StringUtils.isEmpty(str)) {
            return "";
        }
        if (separator == null) {
            return "";
        }
        final int pos = str.indexOf(separator);
        if (pos == -1) {
            return "";
        }
        return str.substring(pos + separator.length());
    }

    /**
     * @return java.lang.String
     * @Author pancm
     * @Description 获取截取字符串前面的值
     * @Date 2020/7/30
     * @Param [str, separator]
     **/
    private String substringBefore(final String str, final String separator) {
        if (StringUtils.isEmpty(str)) {
            return "";
        }
        if (separator == null) {
            return "";
        }
        final int pos = str.indexOf(separator);
        if (pos == -1) {
            return "";
        }
        return str.substring(0, pos);
    }


    @Override
    public PageResult<AlertRecordRespVO> getAlertRecordHisPage(AlertRecordSearchVO reqVO) {
        Page page = PageHelper.startPage(reqVO.getPageNum(), reqVO.getPageSize());
        List<AlertRecordRespVO> alertRecordRespVOList = mapper.getAlertRecordHis(reqVO);
        return new PageResult<AlertRecordRespVO>(page.getTotal(), alertRecordRespVOList, reqVO.getPageSize(), reqVO.getPageNum());
    }

    @Override
    public ApiResult getAlertRecordView(Long id, int typeId) {

        AlertRecordRespVO alertRecord = mapper.getAlertRecordOriginalView(id);
        if (alertRecord == null) {
            return ApiResult.error("该记录ID:" + id + " 已经消除了!");
        }

        String keywordValue = alertRecord.getKeywordValue();
        String authMark = alertRecord.getAuthMark();
        Map<String, Object> recordMap = new HashMap<>();
        Map<String, Object> resultMap = new HashMap<>();
        //这里需要对ip冲突规则做特殊处理
        if (alertRecord.getTemplateId() == 4 || alertRecord.getTemplateId() == 5) {
            dealSpecial(alertRecord, authMark, recordMap, resultMap);
            return ApiResult.success(recordMap);
        }
        AlertDetailRespVO alertDetailRespVO = mapper.getAlertRecordOriginalDetailView(alertRecord.getBusinessId());
        recordMap.put("alertRecord", alertRecord);
        if (alertDetailRespVO != null) {
            String nasPortId = alertDetailRespVO.getNasPortId();
            if (!StringUtils.isEmpty(nasPortId) && alertRecord.getRuleId() == 13) {
                nasPortId = getPortId(nasPortId);
                alertDetailRespVO.setNasPortId(nasPortId);
            }
            if(StringUtils.isEmpty(alertDetailRespVO.getIpAddr())){
                alertDetailRespVO.setIpAddr(alertRecord.getIpAddr());
            }

        }

        recordMap.put("alertDetail", alertDetailRespVO);
        getOtherData(alertRecord, recordMap);

        return ApiResult.success(recordMap);

    }

    private void getOtherData(AlertRecordRespVO alertRecord, Map<String, Object> recordMap) {
        //2021-10-13 新增第三方漏洞的告警信息查询
        Integer typeId1 = alertRecord.getTypeId();
        if (typeId1 != null && typeId1 == 4) {
            VulHost vulHost = new VulHost();
            vulHost.setIpAddr(alertRecord.getIpAddr());
            vulHost = vulHostDao.queryOne(vulHost);
            if (vulHost != null) {
                //这里根据前端的要求设置高中低危的饼图数据结构
                setCircle(vulHost);
            }
            recordMap.put("alertDetailOtherResult", vulHost);
        }
    }

    private void setCircle(VulHost vulHost) {
        //这里根据前端的要求设置高中低危的饼图数据结构
        List<CircleUnit> circleUnits = new ArrayList<>();
        CircleUnit circleUnit = new CircleUnit();
        circleUnit.setName("极危");
        circleUnit.setVal(vulHost.getCritical());
        circleUnits.add(circleUnit);
        circleUnit = new CircleUnit();
        circleUnit.setName("高危");
        circleUnit.setVal(vulHost.getHigh());
        circleUnits.add(circleUnit);
        circleUnit = new CircleUnit();
        circleUnit.setName("中危");
        circleUnit.setVal(vulHost.getMedium());
        circleUnits.add(circleUnit);
        circleUnit = new CircleUnit();
        circleUnit.setName("低危");
        circleUnit.setVal(vulHost.getLow());
        circleUnits.add(circleUnit);
        circleUnit = new CircleUnit();
        circleUnit.setName("提示");
        circleUnit.setVal(vulHost.getInfo());
        circleUnits.add(circleUnit);
        vulHost.setCircleUnits(circleUnits);
    }

    @Override
    public ApiResult getAlertRecordViewOtherDetail(VulHostVuln vulHostVuln) {
        Map<String, Object> recordMap = new HashMap<>();

        if (StringUtils.isEmpty(vulHostVuln.getIpAddr())) {
            recordMap.put("alertDetailOtherList", null);
            recordMap.put("alertDetailOtherResult", null);
            return ApiResult.success(recordMap);
        }

        AlertRecordSearchVO reqVO = new AlertRecordSearchVO();
        reqVO.setIpAddr(vulHostVuln.getIpAddr());
        reqVO.setTypeId(4);
        AlertRecordRespVO alertRecordRespVO = mapper.getAlertRecordByReq(reqVO);
        if (alertRecordRespVO == null) {
            recordMap.put("alertDetailOtherList", null);
            recordMap.put("alertDetailOtherResult", null);
            return ApiResult.success(recordMap);
        }
        VulHost vulHost = new VulHost();
        List<VulHostVuln> vulHostVulns = new ArrayList<>();
        vulHost.setIpAddr(vulHostVuln.getIpAddr());
        vulHost = vulHostDao.queryOne(vulHost);
        if (vulHost != null) {
            //这里根据前端的要求设置高中低危的饼图数据结构
            setCircle(vulHost);
            Page page = PageHelper.startPage(vulHostVuln.getPageNum(), vulHostVuln.getPageSize());
            vulHostVulns = vulHostVulnDao.queryAll(vulHostVuln);
            PageResult pageResult = new PageResult<>(page.getTotal(), vulHostVulns, vulHostVuln.getPageSize(), vulHostVuln.getPageNum());
            recordMap.put("alertDetailOtherList", pageResult);
        } else {
            recordMap.put("alertDetailOtherList", null);
        }
        recordMap.put("alertDetailOtherResult", vulHost);
        return ApiResult.success(recordMap);
    }


    private void dealSpecial(AlertRecordRespVO alertRecord, String authMark, Map<String, Object> recordMap, Map<String, Object> resultMap) {
        List<AlertIpClashRespVO> alertIpClashRespVOS = mapper.getAlertIpClash(alertRecord.getBusinessId());
        String noticeInfo = alertRecord.getNoticeInfo();
        String[] macs = getMac(noticeInfo);
        alertRecord.setMacs(macs);
        String baseMac = macs[0];
        String curMac = macs[1];
        //2020-10-13 和北傲确认 这里查询两次的原因是因为认证信息的数据用基准mac地址的数据，资产信息用最新mac地址的数据
        EndPointViewVO baseEPV = radiusEndpointMapper.findEndPointByPass(baseMac);
        EndPointViewVO curEPV = radiusEndpointMapper.findEndPointByPass(curMac);
        QzRespVO baseVO;
        QzRespVO curVO;
        List<QzRespVO> qzRespVOList = new ArrayList<>();
        if (curEPV != null && baseEPV != null) {
            BeanUtils.copyProperties(curEPV, alertRecord);
            alertRecord.setEndPointId(curEPV.getId());
            alertRecord.setAccessPolicy(Integer.valueOf(curEPV.getAccessPolicy()));
            alertRecord.setMac(curMac);
//            alertRecord.setMac(keywordValue);
            //这里因为在copy的时候会把改值覆盖为null，因此需要重新赋值一下
            alertRecord.setAuthMark(authMark);
            recordMap.put("alertRecord", alertRecord);
            curVO = radiusEndPointService.findQzById(curEPV.getId());
            baseVO = radiusEndPointService.findQzById(baseEPV.getId());
            if (curVO != null) {
//                curVO.setIpAddr(keywordValue);
                resultMap = radiusQzController.getIpAndAssetsCp(curVO);
            }
            if (baseVO == null) {
                baseVO = new QzRespVO();
            }
            qzRespVOList.add(baseVO);
            qzRespVOList.add(curVO);
            Map<String, Object> map = MapBuilder.getBuilder()
                    .put("radiusQz", alertIpClashRespVOS)
                    .build();
            map.putAll(resultMap);
            recordMap.putAll(map);
        } else {
            recordMap.put("ipView", new ArrayList<>());
            recordMap.put("arpView", new ArrayList<>());
            recordMap.put("alertRecord", alertRecord);
        }
    }


    private String[] getMac(String msg) {
        String[] macs = new String[2];
        String m1 = "原设备:";
        String m2 = ",";
        String m3 = "当前设备:";
        String m4 = "!";
        String msg1 = substringAfter(msg, m1);
        msg1 = substringBefore(msg1, m2);
        String msg2 = substringAfter(msg, m3);
        msg2 = substringBefore(msg2, m4);
        macs[0] = msg1;
        macs[1] = msg2;

        return macs;
    }


    @Override
    public ApiResult getAlertUnReadTotal() {
        return ApiResult.success(mapper.getAlertUnReadTotal());
    }


    /**
     * @return com.zans.base.vo.ApiResult
     * @Author pancm
     * @Description 处置界面初始化
     * @Date
     * @Param [status]
     **/
    @Override
    public ApiResult getAlertRecordDisposeInit() {
        List<AlertReportDisRespVO> resultList = new ArrayList<>();
        List<AlertReportDisRespVO> typeRespVO = mapper.getAlertRecordDisTypeReport();
        resultList.addAll(typeRespVO);
        Map<String, Object> result = MapBuilder.getBuilder()
                .put("type", resultList)
                .build();
        return ApiResult.success(result);
    }


    @Override
    public PageResult<AlertRecordRespVO> getAlertRecordDisposePage(AlertRecordSearchVO reqVO) {
        Page page = PageHelper.startPage(reqVO.getPageNum(), reqVO.getPageSize());
        if (!StringUtils.isEmpty(reqVO.getMac())) {
            reqVO.setMac(StringUtils.trimAllWhitespace(reqVO.getMac()));
        }
        List<AlertRecordRespVO> alertRecordRespVOList = new ArrayList<>();
        if (reqVO.getTypeId() != null && reqVO.getTypeId() == 1) {
            alertRecordRespVOList = mapper.getAlertRecordDispose(reqVO);
        } else {
            alertRecordRespVOList = mapper.getAlertRecordDispose2(reqVO);
        }
        return new PageResult<AlertRecordRespVO>(page.getTotal(), alertRecordRespVOList, reqVO.getPageSize(), reqVO.getPageNum());
    }


    @Override
    public void updateRuleRecord(AlertRecordSearchVO reqVO) {
        mapper.updateRuleRecord(reqVO);
    }

    @Override
    @Transactional
    public void delRecordById(Long id) {
        AlertRecordRespVO alertRecordRespVO = mapper.getRecordById(id);
        String businessId = alertRecordRespVO.getBusinessId();
        AlertRecordRespVO save = mapper.getRecordByBId(businessId);
        mapper.insertRecordDel(save);
        mapper.delOriginalByBId(businessId);
        mapper.delTriggerByBId(businessId);
        mapper.delRecordByBId(businessId);
    }

    @Override
    public void batchDelRecordByIds(String ids) {
        String[] idSs = ids.split(",");
        for (String ss : idSs) {
            delRecordById(Long.valueOf(ss));
        }
    }

    @Override
    public void cleanRecordByKeyword(String keywordValue) {
        List<Long> list = mapper.getAlertRecordDisposeByKeywordValue(keywordValue);
        if (list != null && list.size() > 1) {
            for (int i = 1; i < list.size(); i++) {
                delRecordById(Long.valueOf(list.get(i)));
            }
        }
    }

    @Override
    public PageResult<AlertRuleRespVO> getAlertRulePage(AlertRuleSearchVO reqVO) {
        Page page = PageHelper.startPage(reqVO.getPageNum(), reqVO.getPageSize());
        mapper.getAlertRule(reqVO);
        return new PageResult<AlertRuleRespVO>(page.getTotal(), page.getResult(), reqVO.getPageSize(), reqVO.getPageNum());
    }

    @Override
    public PageResult<AlertTypeRespVO> getAlertTypePage(AlertTypeSearchVO reqVO) {
        Page page = PageHelper.startPage(reqVO.getPageNum(), reqVO.getPageSize());
        mapper.getAlertTypePage(reqVO);
        return new PageResult<AlertTypeRespVO>(page.getTotal(), page.getResult(), reqVO.getPageSize(), reqVO.getPageNum());
    }

    @Override
    public List<SelectVO> getAlertType() {
        List<AlertTypeRespVO> alertTypeRespVOS = mapper.getAlertType(new AlertTypeSearchVO());
        List<SelectVO> selectVOList = new ArrayList<>();
        alertTypeRespVOS.forEach(alertTypeRespVO -> {
            SelectVO selectVO = new SelectVO();
            selectVO.setItemKey(alertTypeRespVO.getId());
            selectVO.setItemValue(alertTypeRespVO.getTypeName());
            selectVOList.add(selectVO);
        });
        return selectVOList;
    }

    @Override
    public List<SelectVO> getAlertIndex(Integer typeId) {
        List<AlertIndexRespVO> alertIndexRespVOS = mapper.getAlertIndex2(typeId);
        List<SelectVO> selectVOList = new ArrayList<>();
        alertIndexRespVOS.forEach(alertIndexRespVO -> {
            SelectVO selectVO = new SelectVO();
            selectVO.setItemKey(alertIndexRespVO.getIndexId());
            selectVO.setItemValue(alertIndexRespVO.getIndexName());
            selectVOList.add(selectVO);
        });
        return selectVOList;
    }

    @Override
    public List<SelectVO> getAlertRuleName(Integer typeId) {
        List<AlertRecordRespVO> respVOS = mapper.getAlertRuleName(typeId);
        List<SelectVO> selectVOList = new ArrayList<>();
        respVOS.forEach(vo -> {
            SelectVO selectVO = new SelectVO();
            selectVO.setItemKey(vo.getId());
            selectVO.setItemValue(vo.getStrategyName());
            selectVOList.add(selectVO);
        });
        return selectVOList;
    }

    @Override
    public PageResult<AlertIndexRespVO> getAlertIndexPage(AlertIndexSearchVO reqVO) {
        Page page = PageHelper.startPage(reqVO.getPageNum(), reqVO.getPageSize());
        mapper.getAlertIndex(reqVO);
        return new PageResult<AlertIndexRespVO>(page.getTotal(), page.getResult(), reqVO.getPageSize(), reqVO.getPageNum());
    }

    @Override
    public List<String> getRecordByKeywordValues(Map<String, Object> kvs) {
        return mapper.getRecordByKeywordValues(kvs);
    }

    @Override
    public List<String> getRecordByIp(Map<String, Object> kvs) {
        return mapper.getRecordByIp(kvs);
    }


    @Override
    public List<String> getHasAlertIpAddr() {
        return mapper.getAlertRecordIpAddr();
    }

    @Override
    public ApiResult remoteAlertRecordRecover(AlertRecordReqVO alertRecordReqVO) {
        String alertApiHost = constantService.getAlertApi();
        if (alertApiHost == null || !alertApiHost.startsWith("http")) {
            return ApiResult.error("系统常量alert_api为空，请联系系统管理员");
        }
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("strategyId", alertRecordReqVO.getStrategyId());
        paramMap.put("mapList", alertRecordReqVO.getMapList());
        poolTaskConfig.executeAlertApiRequest(alertApiHost, ALERT_RECOVER_URI, JSON.toJSONString(alertRecordReqVO));
        return null;

    }

    /**
     * 重点线路策略
     *
     * @param assetGuardLineId
     * @return
     */
    @Override
    public ApiResult guardline(Long assetGuardLineId) {
        List<AssetGuardLineStrategyRespVO> result = mapper.guardline(assetGuardLineId);
        return ApiResult.success(result);
    }

    @Override
    public ApiResult addStrategy(AlertRuleStrategyReqVO alertRuleStrategyReqVO) {
        //调用接口
        String radApiHost = constantService.getAlertApi();
        //String radApiHost = "http://192.168.6.42:8765";
        if (radApiHost == null || !radApiHost.startsWith("http")) {
            return ApiResult.error("系统常量rad_api为空，请联系系统管理员");
        }
        String uri = "/alert/strategy/save/";
        try {
            //alertRuleStrategyReqVO转json
            ApiResult judgeApiData = executeRadApiRequest(radApiHost, uri, alertRuleStrategyReqVO.toString());

        } catch (Exception e) {
            return ApiResult.error("调用失败");
        }
        return ApiResult.success();
    }

    @Override
    public ApiResult updateStatus(AlertRuleStrategyReqVO alertRuleStrategyReqVO) {
        //调用接口
        String radApiHost = constantService.getAlertApi();
        //String radApiHost = "http://192.168.6.42:8765";
        if (radApiHost == null || !radApiHost.startsWith("http")) {
            return ApiResult.error("系统常量rad_api为空，请联系系统管理员");
        }
        String uri = "/alert/strategy/status/";
        try {
            //alertRuleStrategyReqVO转json
            ApiResult judgeApiData = executeRadApiRequest(radApiHost, uri, alertRuleStrategyReqVO.toString());

        } catch (Exception e) {
            return ApiResult.error("调用失败");
        }
        return ApiResult.success();
    }

    public ApiResult executeRadApiRequest(String host, String uri, String jsonStr) {
        return poolTaskConfig.executeAlertApiRequest(host, uri, jsonStr);
    }

    @Override
    public Boolean checkGroupName(Long id, String groupName) {
        if (mapper.checkGroupName(id, groupName) > 0) {
            return false;
        }
        return true;
    }

    /**
     * 单条告警记录设为已读
     *
     * @param id
     */
    @Override
    public void readRecordById(Long id) {
        mapper.readRecordById(id);
    }

    /**
     * 批量告警记录设为已读
     *
     * @param ids
     */
    @Override
    public void batchReadRecordByIds(String ids) {
        String[] idSs = ids.split(",");
        for (String ss : idSs) {
            readRecordById(Long.valueOf(ss));
        }
    }

    private String millisToStringShort(long l) {
        StringBuffer sb = new StringBuffer();
        long minutes = 1;
        long hours = 60 * minutes;
        long days = 24 * hours;
        if (l / days >= 1)
            sb.append((int) (l / days) + "天");
        if (l % days / hours >= 1)
            sb.append((int) (l % days / hours) + "小时");
        if (l % days % hours / minutes >= 1)
            sb.append((int) (l % days % hours / minutes) + "分钟");

        return sb.toString();
    }

    /**
     * 历史风险列表
     *
     * @param reqVO
     * @return
     */
    @Override
    public PageResult<AlertRecordRespVO> getHisAlertRecordPage(AlertRecordSearchVO reqVO) {
        Page page = PageHelper.startPage(reqVO.getPageNum(), reqVO.getPageSize());
        if (!StringUtils.isEmpty(reqVO.getMac())) {
            reqVO.setMac(StringUtils.trimAllWhitespace(reqVO.getMac()));
        }
        List<AlertRecordRespVO> alertRecordRespVOList = new ArrayList<>();
//        if (reqVO.getTypeId() != null && reqVO.getTypeId() == 1) {
        alertRecordRespVOList = mapper.getHisAlertRecord(reqVO);
//        }
//        else {
//            alertRecordRespVOList = mapper.getHisAlertRecord2(reqVO);
//        }
//        tabAuthData(alertRecordRespVOList);
//        tabData(alertRecordRespVOList);
//        updateReadStatus(alertRecordRespVOList);

        return new PageResult<AlertRecordRespVO>(page.getTotal(), alertRecordRespVOList, reqVO.getPageSize(), reqVO.getPageNum());
    }

    @Override
    public ApiResult addFromVul(AddAlertForVulVO req) {
        AlertRecordReqVO alertRecordReqVO = new AlertRecordReqVO();
        List<Map<String,Object>> mapList = new ArrayList<>();
        Map<String,Object> map = new HashMap<>();
        map.put("ip_addr", req.getIpAddr());
        map.put("mac", req.getMac());
        map.put("scanTime", req.getScanTime());
        map.put("desc", req.getDescJson());
        map.put("alert_level", req.getAlertLevel());
        mapList.add(map);
        //扫描漏洞风险
        alertRecordReqVO.setStrategyId(5L);
        alertRecordReqVO.setMapList(mapList);
        return  this.addAlertRecordRecover(alertRecordReqVO);
    }

    @Override
    public ApiResult addAlertRecordRecover(AlertRecordReqVO alertRecordReqVO) {
        String alertApiHost = constantService.getAlertApi();
        if (alertApiHost == null || !alertApiHost.startsWith("http")) {
            return ApiResult.error("系统常量alert_api为空，请联系系统管理员");
        }
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("strategyId",alertRecordReqVO.getStrategyId());
        paramMap.put("mapList",alertRecordReqVO.getMapList());
        poolTaskConfig.executeAlertApiRequest(alertApiHost,ALERT_RECOVER_ADD_URI, JSON.toJSONString(alertRecordReqVO));
        return null;

    }

}
