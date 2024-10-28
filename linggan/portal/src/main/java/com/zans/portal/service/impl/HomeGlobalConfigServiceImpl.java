package com.zans.portal.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.ImmutableMap;
import com.zans.base.util.ArithmeticUtil;
import com.zans.base.util.MapBuilder;
import com.zans.base.util.SpringContextHolder;
import com.zans.base.vo.ApiResult;
import com.zans.portal.dao.*;
import com.zans.portal.service.IHomeGlobalConfigService;
import com.zans.portal.vo.HomeGlobalConfigVo;
import com.zans.portal.vo.HomeIndexConfigVO;
import com.zans.portal.vo.alert.AlertRecordRespVO;
import com.zans.portal.vo.alert.AlertReportDisRespVO;
import com.zans.portal.vo.alert.AlertReportRespVO;
import com.zans.portal.vo.asset.resp.AssetStatisRespVO;
import com.zans.portal.vo.chart.CompareUnit;
import com.zans.portal.vo.chart.CountUnit;
import com.zans.portal.vo.common.ChartStatisVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.ReflectionUtils;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


/**
 * 首页全局配置表(HomeGlobalConfig)表服务实现类
 *
 * @author beixing
 * @since 2021-10-21 10:37:05
 */
@Service("homeGlobalConfigService")
@Slf4j
public class HomeGlobalConfigServiceImpl implements IHomeGlobalConfigService {
    @Resource
    private HomeGlobalConfigDao homeGlobalConfigDao;

    @Resource
    private HomeIndexConfigDao homeIndexConfigDao;


    @Override
    public ApiResult globalConfig(Integer globalId) {
        List<HomeGlobalConfigVo> list = homeGlobalConfigDao.queryByGlobalId(globalId);
        return ApiResult.success(list);
    }


    @Override
    public ApiResult getIndex(Integer id) {
        HomeIndexConfigVO homeIndexConfig = homeIndexConfigDao.queryById(id);
        if (homeIndexConfig == null) {
            return ApiResult.error("已被禁用或删除了!");
        }
        String className = homeIndexConfig.getClassName();
        String methodName = homeIndexConfig.getMethodName();
        String querySql = homeIndexConfig.getQuerySql();
        Object data = null;
        try {
            data = getMethodValue(className, methodName, querySql);
            log.debug("ID:#{},根据反射查询到的数据:{}", id, data);
        } catch (Exception e) {
            log.error("获取方法失败!配置:{}", homeIndexConfig, e);
        }
        homeIndexConfig.setClassName(null);
        homeIndexConfig.setMethodName(null);
        homeIndexConfig.setQuerySql(null);
        String dataList = JSONObject.toJSONString(data);
//        String pathData = homeIndexConfig.getPathData();
        homeIndexConfig.setDataList(dataList);
        return ApiResult.success(homeIndexConfig);
    }


    /**
     * @return
     * @Author beixing
     * @Description 根据反射获取方法值
     * @Date 2021/11/12
     * @Param
     **/
    private Object getMethodValue(String className, String methodName, String args) {
        Method mh = ReflectionUtils.findMethod(SpringContextHolder.getBean(className).getClass(), methodName, new Class[]{String.class});
        if (mh == null) {
            log.error("className:{},methodName:{},不存在该方法或该方法不是String类型", className, methodName);
            return null;
        }
        Object obj = ReflectionUtils.invokeMethod(mh, SpringContextHolder.getBean(className), args);
        return obj;
    }


    /**
     * @return
     * @Author beixing
     * @Description 执行sql语句
     * @Date 2021/11/12
     * @Param
     **/
    public List<JSONObject> executeSql(String sqlStr) {
        List<JSONObject> list = homeIndexConfigDao.executeSql(sqlStr);
        return list;
    }


    /**-------------------------------------------------------- start ---------------------------------------------------------------------------------- */


    /**
     * @return
     * @Author beixing
     * @Description 执行sql语句
     * @Date 2021/11/12
     * @Param
     **/
    public List<Object> deviceAccess(String sqlStr) {
        List<JSONObject> list = homeIndexConfigDao.executeSql(sqlStr);
        String sqlStr2 = " SELECT COUNT(1) AS value,\n" +
                "'放行' AS name,'/auth/radius/online/endpoint' AS path,'' AS pathData FROM radius_endpoint e \n" +
                "LEFT JOIN radius_endpoint_profile rep ON e.mac = rep.mac\n" +
                "LEFT JOIN device_type_guard dt ON rep.cur_device_type = dt.type_id \n" +
                "WHERE e.delete_status = 0 AND e.access_policy >= 2 AND dt.`enable_status` = 1\n" +
                "UNION ALL\n" +
                "SELECT COUNT(1) AS value, \n" +
                "'阻断' AS name,'/auth/radius/offline/block' AS path,'' AS pathData FROM radius_endpoint e\n" +
                "LEFT JOIN radius_endpoint_profile rep ON e.mac = rep.mac\n" +
                "LEFT JOIN device_type_guard dt ON rep.cur_device_type = dt.type_id  \n" +
                "WHERE e.delete_status = 0 AND e.access_policy = 0 AND dt.`enable_status` = 1\n" +
                "UNION ALL\n" +
                "SELECT COUNT(1) AS value,\n" +
                "'检疫' AS name,'/auth/radius/qz' AS path,'' AS pathData FROM radius_endpoint e \n" +
                "LEFT JOIN radius_endpoint_profile rep ON e.mac = rep.mac\n" +
                "LEFT JOIN device_type_guard dt ON rep.cur_device_type = dt.type_id  \n" +
                "WHERE e.delete_status = 0 AND e.access_policy = 1 AND dt.`enable_status` = 1\n";
        List<JSONObject> list2 = homeIndexConfigDao.executeSql(sqlStr2);

        List<Object> objectList = new ArrayList<>();
        objectList.add(list.get(0));
        objectList.add(list2);
        return objectList;
    }


    /**
     * @return
     * @Author beixing
     * @Description 执行sql语句
     * @Date 2021/11/12
     * @Param
     **/
    public List<Object> deviceDiscovery(String sqlStr) {
        List<JSONObject> list = homeIndexConfigDao.executeSql(sqlStr);
        String sqlStr2 ="SELECT '新设备' AS name, COUNT(1) AS value,'/asset/deviceDiscovery' AS path,\n" +
                "   '' AS pathData  FROM \n" +
                "radius_endpoint re\n" +
                " LEFT JOIN radius_endpoint_profile rp ON rp.endpoint_id=re.id\n" +
                " LEFT JOIN asset ab ON ab.ip_addr=rp.cur_ip_addr\n" +
                "WHERE   (ab.asset_source = 2 or ab.asset_source is null );";
        List<JSONObject> list2 = homeIndexConfigDao.executeSql(sqlStr2);

        List<Object> objectList = new ArrayList<>();
        objectList.add(list);
        objectList.add(list2.get(0));
        return objectList;
    }

    /**
     * @return
     * @Author beixing
     * @Description 设备分组在线率
     * @Date 2021/11/15
     * @Param
     **/
    public List<AssetStatisRespVO> getAssetBranchStatis(String test) {
        List<AssetStatisRespVO> resultList = assetBranchAssetMapper.findAssetBranchStatis();
        if (CollectionUtils.isEmpty(resultList)) {
            resultList = new ArrayList<>();
        }
        resultList.forEach(vo -> vo.setOnlineRate(ArithmeticUtil.percent(vo.getAliveNum(), vo.getTotalNum(), 1)));
        return resultList;
    }

    /**
     * @return
     * @Author beixing
     * @Description 资产类型
     * @Date 2021/11/15
     * @Param
     **/
    public JSONObject getAssetDevice(String test) {
        List<AssetStatisRespVO> resultList = getAssetDeviceStatis(null);
        JSONObject jsonObject = new JSONObject();
        List<String> nameList = new ArrayList();
        List<Long> valueList = new ArrayList();
        for (AssetStatisRespVO respVO : resultList) {
            String name = respVO.getDeviceTypeName();
            Long value = respVO.getTotalNum();
            nameList.add(name);
            valueList.add(value);
        }
        jsonObject.put("name", nameList);
        jsonObject.put("value", valueList);
        return jsonObject;
    }

    public List<AssetStatisRespVO> getAssetDeviceStatis(String test) {
        List<AssetStatisRespVO> resultList = assetMapper.findAssetDeviceStatis();
        if (StringUtils.isEmpty(resultList)) {
            resultList = new ArrayList<>();
        }
        resultList.forEach(vo -> vo.setOnlineRate(ArithmeticUtil.percent(vo.getAliveNum(), vo.getTotalNum(), 1)));
        return resultList;
    }

    /**
     * 准入概况
     *
     * @param test
     * @return
     */
    public JSONObject findRadiusMacByDate(String test) {
        List<String> days = getDaysBetween(9);
        String[] dates = days.toArray(new String[10]);
        JSONObject jsonObject = new JSONObject();
        List<String> nameList = new ArrayList<>();
        List<Long> totalList = new ArrayList<>();
        List<Long> authList = new ArrayList<>();
        for (String date : dates) {
            CountUnit auth = assetMapper.findAuthByDate(date);
            CountUnit refuse = assetMapper.findRefuseByDate(date);
            nameList.add(date.substring(5, date.length()));
            totalList.add((Long) auth.getVal());
            authList.add((Long) refuse.getVal());

        }
        jsonObject.put("name", nameList);
        jsonObject.put("total", totalList);
        jsonObject.put("auth", authList);
        return jsonObject;
    }

    /**
     * @return
     * @Author beixing
     * @Description 获取相邻的天
     * @Date 2021/11/4
     * @Param
     **/
    private static List<String> getDaysBetween(int days) {
        List<String> dayss = new ArrayList<>();
        String formatDate = "yyyy-MM-dd";
        for (int i = 0; i <= days; i++) {
            LocalDate localDate = LocalDate.now().minusDays(i);
            dayss.add(localDate.format(DateTimeFormatter.ofPattern(formatDate)));
        }
        List<String> dayss2 = new ArrayList<>();
        for (int i = dayss.size() - 1; i >= 0; i--) {
            dayss2.add(dayss.get(i));
        }
        return dayss2;
    }

    /**
     * @return
     * @Author beixing
     * @Description 品牌在线情况
     * @Date 2021/12/14
     * @Param
     **/
    public JSONObject findAssetByBrand(String test) {
        List<CompareUnit> result = new ArrayList<>();
        List<CompareUnit> assetByArea = assetMapper.findAssetByBrand();
        List<CompareUnit> assetByAreaOther = assetMapper.findAssetByBrandByOther();
        CompareUnit compareUnit2= new CompareUnit();
        int s = 0;
        int f = 0;
        for (CompareUnit compareUnit : assetByAreaOther) {
            s=s+Integer.valueOf(compareUnit.getSuccess().toString());
            f=f+Integer.valueOf(compareUnit.getFailed().toString());
        }
        compareUnit2.setSuccess(s);
        compareUnit2.setFailed(f);
        compareUnit2.setName("其他");
        assetByArea.add(compareUnit2);
        for (int i = assetByArea.size() - 1; i >= 0; i--) {
            result.add(assetByArea.get(i));
        }
        JSONObject jsonObject = new JSONObject();
        List<String> nameList = new ArrayList<>();
        List<Integer> totalList = new ArrayList<>();
        List<Integer> authList = new ArrayList<>();

        for (CompareUnit vo : result) {
            nameList.add(vo.getName());
            totalList.add(Integer.valueOf(vo.getSuccess().toString()));
            authList.add( Integer.valueOf(vo.getFailed().toString()));
        }
        jsonObject.put("name", nameList);
        jsonObject.put("total", totalList);
        jsonObject.put("auth", authList);
        return jsonObject;
    }









    /** ---------   资产全景 start ----------*/
    /**
     * @return
     * @Author beixing
     * @Description 设备分组在线率（折线图）
     * @Date 2021/11/15
     * @Param
     **/
    public JSONObject getAssetBranchAlive(String test) {
        List<AssetStatisRespVO> resultList = assetBranchAssetMapper.findAssetBranchStatis();
        if (CollectionUtils.isEmpty(resultList)) {
            resultList = new ArrayList<>();
        }
        resultList.forEach(vo -> vo.setOnlineRate(ArithmeticUtil.percent(vo.getAliveNum(), vo.getTotalNum(), 1)));

        JSONObject jsonObject = new JSONObject();
        List<String> nameList = new ArrayList<>();
        List<Long> totalList = new ArrayList<>();
        List<Double> rateList = new ArrayList<>();
        for (AssetStatisRespVO vo : resultList) {
            nameList.add(vo.getName());
            totalList.add(vo.getTotalNum());
            rateList.add(vo.getOnlineRate());
        }
        jsonObject.put("name",nameList);
        jsonObject.put("total",totalList);
        jsonObject.put("rate",rateList);
        return jsonObject;
    }



    /**
     * @return
     * @Author beixing
     * @Description 品牌在线率（折线图）
     * @Date 2021/12/14
     * @Param
     **/
    public JSONObject findAssetByBrandStatistics(String test) {
        List<CompareUnit> result = new ArrayList<>();
        List<CompareUnit> assetByArea = assetMapper.findAssetByBrand();
        List<CompareUnit> assetByAreaOther = assetMapper.findAssetByBrandByOther();
        CompareUnit compareUnit2= new CompareUnit();
        int s = 0;
        int f = 0;
        for (CompareUnit compareUnit : assetByAreaOther) {
            s=s+Integer.valueOf(compareUnit.getSuccess().toString());
            f=f+Integer.valueOf(compareUnit.getFailed().toString());
        }
        compareUnit2.setSuccess(s);
        compareUnit2.setFailed(f);
        compareUnit2.setName("其他");
        assetByArea.add(compareUnit2);
        for (int i = assetByArea.size() - 1; i >= 0; i--) {
            result.add(assetByArea.get(i));
        }
        JSONObject jsonObject = new JSONObject();
        List<String> nameList = new ArrayList<>();
        List<Double> valueList = new ArrayList<>();

        for (CompareUnit vo : result) {
            nameList.add(vo.getName());
            long ss = Long.valueOf(vo.getSuccess().toString());
            long ff = Long.valueOf(vo.getFailed().toString());
            BigDecimal success = (BigDecimal.valueOf(ss));
            BigDecimal total = success.add(BigDecimal.valueOf(ff));
            valueList.add(ArithmeticUtil.percent(success.longValue(),total.longValue(), 1));
        }
        jsonObject.put("name", nameList);
        jsonObject.put("value", valueList);
        return jsonObject;
    }

    /** ---------   资产全景 end ----------*/





    /** ---------   风险感知 start ----------*/


    /**
     * @return
     * @Author beixing
     * @Description 安全评分
     * @Date 2021/12/14
     * @Param
     **/
    public List<JSONObject> calculateScore(String test) {
        List<JSONObject> list = new ArrayList<>();
        //默认值
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("safeList", 90);
        list.add(jsonObject);
        return list;
    }

    /**
     * @return
     * @Author beixing
     * @Description 异常分类
     * @Date 2021/12/14
     * @Param
     **/
    public List<JSONObject> getAlertServerType(String test) {
        List<JSONObject> list = new ArrayList<>();
        JSONObject jsonObject = new JSONObject();
        JSONObject jsonObject2 = new JSONObject();
        List<AlertReportDisRespVO> typeRespVO =null;
//        typeRespVO = alertRuleMapper.getAlertRecordType2();
        typeRespVO = alertRuleMapper.getAlertRecordRuleType();
        long total =  typeRespVO.get(0).getValue();
        BigDecimal rateTotal = new BigDecimal(total);
        if(typeRespVO!=null){
            for (int i = 0; i < typeRespVO.size(); i++) {
                long value = typeRespVO.get(i).getValue();
                BigDecimal rate = new BigDecimal(value);
                if(value!= 0 && total!=0 ){
                    rate = rate.divide(rateTotal,4, RoundingMode.HALF_UP);
                }
                typeRespVO.get(i).setRate(rate.doubleValue());
            }
        }
        jsonObject.put("name", "异常");
        jsonObject.put("list", typeRespVO);
        list.add(jsonObject);
        List<AlertReportDisRespVO>  t = alertRuleMapper.getAlertRecordDealCountAndNotCount();
        jsonObject2.put("name", "分类");
        jsonObject2.put("list", t);
        list.add(jsonObject2);
        return list;
    }

    /**
    * @Author beixing
    * @Description  风险趋势
    * @Date  2021/12/15
    * @Param
    * @return
    **/
    public List<AlertReportRespVO> getAlertRecordTrend(String test) {
        List<AlertReportRespVO> alertReportRespVOS =  alertRuleMapper.getAlertRecordRule();
        long total = alertRuleMapper.getAlertRecordNotDealCount();
        if(alertReportRespVOS==null || total<1){
            return new ArrayList<>();
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

        return alertReportRespVOS;
    }


    /**
     * @Author beixing
     * @Description  预警列表
     * @Date  2021/12/15
     * @Param
     * @return
     **/
    public List<JSONObject> getAlertList(String sqlStr) {
        List<JSONObject> list = homeIndexConfigDao.executeSql(sqlStr);
        String sqlStr2 ="  SELECT SUM(CASE WHEN IFNULL(ao.`deal_status`, 0) > 0 THEN 1 ELSE 0 END) AS dealNum,\n" +
                " SUM(CASE WHEN IFNULL(ao.`deal_status`, 0) = 0 THEN 1 ELSE 0 END) AS noDealNum\n" +
                "   FROM alert_rule_original ao LEFT JOIN alert_rule b ON ao.`rule_id` = b.`id` where b.rule_status  =1  ";
        List<JSONObject> list2 = homeIndexConfigDao.executeSql(sqlStr2);
        JSONObject jsonObject = list2.get(0);
        jsonObject.put("opsData",list);
        List<JSONObject> jsonObjectList = new ArrayList<>();
        jsonObjectList.add(jsonObject);
        return jsonObjectList;
    }



    /**
     * @return
     * @Author beixing
     * @Description 设备掉线（折线图）
     * @Date 2021/12/14
     * @Param
     **/
    public JSONObject alertDevicesLost (String test) {
        List<AlertRecordRespVO> voList = alertRuleMapper.getAlertRecordDeviceType(3L);
        JSONObject jsonObject = new JSONObject();
        List<String> nameList = new ArrayList<>();
        List<Long> valueList = new ArrayList<>();
        long total = voList.stream().collect(Collectors.summingLong(it->  it.getId()));
        for (AlertRecordRespVO vo : voList) {
            valueList.add(vo.getId());
//            valueList.add((long) ArithmeticUtil.percent(vo.getId(), total, 4));
            nameList.add(vo.getDeviceTypeName());
        }
        jsonObject.put("name", nameList);
        jsonObject.put("value", valueList);
        return jsonObject;
    }

    /**
     * @return
     * @Author beixing
     * @Description 风险告警（折线图）
     * @Date 2021/12/14
     * @Param
     **/
    public JSONObject alertDevices (String test) {
        List<AlertRecordRespVO> voList = alertRuleMapper.getAlertRecordDeviceType(null);
        JSONObject jsonObject = new JSONObject();
        List<String> nameList = new ArrayList<>();
        List<Long> valueList = new ArrayList<>();
        long total = voList.stream().collect(Collectors.summingLong(it->  it.getId()));
        for (AlertRecordRespVO vo : voList) {
//            valueList.add(vo.getId());
            valueList.add((long) ArithmeticUtil.percent(vo.getId(), total, 4));
            nameList.add(vo.getDeviceTypeName());
        }
        jsonObject.put("name", nameList);
        jsonObject.put("value", valueList);
        return jsonObject;
    }


    /** ---------   风险感知 end ----------*/

    /** ---------   网络设备 start ----------*/


    public List<AssetStatisRespVO> getAssetDeviceStatis2(String test) {
        List<AssetStatisRespVO> resultList = assetMapper.findAssetDeviceStatis2();
        if (StringUtils.isEmpty(resultList)) {
            resultList = new ArrayList<>();
        }
        resultList.forEach(vo -> vo.setOnlineRate(ArithmeticUtil.percent(vo.getAliveNum(), vo.getTotalNum(), 1)));
        return resultList;
    }
    /**
    * @Author beixing
    * @Description  设备运行状态
    * @Date  2021/12/16
    * @Param
    * @return
    **/
    public List<JSONObject> countDeviceStatus(String test) {

        Map<String,String> map =  assetMapper.queryDeviceType();
        List<JSONObject> result = new ArrayList<>();
        JSONObject jsonObject = new JSONObject();
        List<String> nameList = new ArrayList();
        nameList.add("离线");
        nameList.add("正常");
        List<JSONObject> jsonObjectList = new ArrayList();
        JSONObject jsonObject1 = new JSONObject();
        jsonObject1.put("name","离线");
        jsonObject1.put("value",map.get("failed"));
        jsonObjectList.add(jsonObject1);
        JSONObject jsonObject2 = new JSONObject();
        jsonObject2.put("name","正常");
        jsonObject2.put("value",map.get("success"));
        jsonObjectList.add(jsonObject2);
        jsonObject.put("type",nameList);
        jsonObject.put("ecahrtList",jsonObjectList);
        result.add(jsonObject);
        return result;
    }

    /**
     * @Author beixing
     * @Description  网络质量
     * @Date  2021/12/16
     * @Param
     * @return
     **/
    public List<JSONObject> networkQuality(String test) {
        List<JSONObject> result = new ArrayList<>();
        JSONObject jsonObject = new JSONObject();

        List<JSONObject> jsonObjectList1 = new ArrayList();
        List<JSONObject> jsonObjectList2 = new ArrayList();
        List<JSONObject> jsonObjectList3 = new ArrayList();
        String[] delayName ={"ms","ms","ms"};
        Integer[] delayValue ={3,4,5};
        String[] timeName ={"10分钟","1小时","24小时"};
        Integer[] timeValue ={1,2,3};
        String[] lossName ={"丢包率","丢包率","丢包率"};
        Double[] lossValue ={0.010,0.011,0.012};

        for (int i = 0; i < delayName.length; i++) {
            JSONObject jsonObject1 = new JSONObject();
            jsonObject1.put("name",delayName[i]);
            jsonObject1.put("value",delayValue[i]);
            jsonObjectList1.add(jsonObject1);
        }
        for (int i = 0; i < timeName.length; i++) {
            JSONObject jsonObject1 = new JSONObject();
            jsonObject1.put("name",timeName[i]);
            jsonObject1.put("value",timeValue[i]);
            jsonObjectList2.add(jsonObject1);
        }
        for (int i = 0; i < lossName.length; i++) {
            JSONObject jsonObject1 = new JSONObject();
            jsonObject1.put("name",lossName[i]);
            jsonObject1.put("value",lossValue[i]);
            jsonObjectList3.add(jsonObject1);
        }

        jsonObject.put("delayList",jsonObjectList1);
        jsonObject.put("timeList",jsonObjectList2);
        jsonObject.put("lossList",jsonObjectList3);
        result.add(jsonObject);
        return result;
    }

    /**
    * @Author beixing
    * @Description  拓扑图
    * @Date  2021/12/16
    * @Param
    * @return
    **/
    public List<JSONObject> getTopology(String test){
        List<JSONObject> result = new ArrayList<>();

        JSONObject jsonObject = new JSONObject();

        List<JSONObject> coreOneList = new ArrayList();
        List<JSONObject> coreTwoList = new ArrayList();
        List<JSONObject> coreThreeList = new ArrayList();
//        String[] coreThreeName ={"应用系统接入区","云存储接入区","数据对接服务区","楼层办公接入区","纱帽中队机房","军山机房","无人驾驶试验区"};
//        String[] coreThreeIp ={"192.168.22.22","192.168.22.22","192.168.22.22","192.168.22.22","192.168.22.22","192.168.22.22","192.168.22.22"};
//        Boolean[] coreThreeValue ={false,false,false,false,false,false,true};
        String[] coreOneName ={"应用系统接入区","云存储接入区","数据对接服务区","楼层办公接入区","纱帽中队机房","军山机房","智能网联","电警卡口设备接入区2","电警卡口设备接入区","信号机接入区"};
        Boolean[] coreOneValue ={false,false,false,false,true,true,true,true,true,true};
        String[] ipOneName ={"","","","","2.2.2.120","2.2.2.90","2.2.2.70","2.2.2.44","2.2.2.41","172.101.1.1"};

//        String[] coreOneName ={"江岸核心交换机","东西湖核心交换机","江汉核心交换机","硚口核心交换机","黄陂核心交换机","新洲核心交换机","高管核心交换机"};
//        Boolean[] coreOneValue ={true,true,true,true,true,true,true,true,true};
//        String[] coreTwoName ={"武昌核心交换机","青山核心交换机","东新核心交换机","洪山核心交换机","东湖核心交换机","二桥核心交换机","车辆管理所核心交换机","化工核心交换机","江夏核心交换机"};
//        Boolean[] coreTwoValue ={true,true,true,true,true,true,true,true,true};
//        String[] coreThreeName ={"直属核心交换机","白沙洲核心交换机","沌口核心交换机","汉阳核心交换机","蔡甸核心交换机","沌口军山中队核心交换机","大桥核心交换机"};
//        Boolean[] coreThreeValue ={true,true,true,true,true,true,true};

        for (int i = 0; i < coreOneName.length; i++) {
            JSONObject jsonObject1 = new JSONObject();
            jsonObject1.put("name",coreOneName[i]);
            jsonObject1.put("isOutline",coreOneValue[i]);
            jsonObject1.put("ipAddr",ipOneName[i]);
            jsonObject1.put("id",i+1);
            coreOneList.add(jsonObject1);
        }

//        for (int i = 0; i < coreTwoName.length; i++) {
//            JSONObject jsonObject1 = new JSONObject();
//            jsonObject1.put("name",coreTwoName[i]);
//            jsonObject1.put("isOutline",coreTwoValue[i]);
//            jsonObject1.put("id",i+1);
//            coreTwoList.add(jsonObject1);
//        }
//
//        for (int i = 0; i < coreThreeName.length; i++) {
//            JSONObject jsonObject1 = new JSONObject();
//            jsonObject1.put("name",coreThreeName[i]);
//            jsonObject1.put("isOutline",coreThreeValue[i]);
//            jsonObject1.put("ip",coreThreeIp[i]);
//            jsonObject1.put("id",i+1);
//            coreThreeList.add(jsonObject1);
//        }

        jsonObject.put("coreHxName","开发区核心交换机");
        jsonObject.put("coreByName","备用核心交换机");
        jsonObject.put("coreOneList",coreOneList);
//        jsonObject.put("coreTwoList",coreTwoList);
//        JSONObject jsonObjectOne = new JSONObject();
//        jsonObjectOne.put("name","开发区核心交换机");
//        jsonObjectOne.put("ip","2.2.2.254");
//        JSONObject jsonObjectTwo = new JSONObject();
//        jsonObjectTwo.put("name","开发区汇聚交换机");
//        jsonObjectTwo.put("ip","2.2.2.44");
//        jsonObject.put("coreOne",jsonObjectOne);
//        jsonObject.put("coreTwo",jsonObjectTwo);
//        jsonObject.put("coreThree",coreThreeList);
        result.add(jsonObject);
        return result;
    }

    /**
    * @Author beixing
    * @Description  运维统计
    * @Date  2021/12/16
    * @Param
    * @return
    **/
    public  List<JSONObject>  operationalUnits(String test) {
        List<JSONObject> result = new ArrayList<>();
        JSONObject jsonObject = new JSONObject();
        List list = new ArrayList();
        list.add(MapBuilder.getBuilder().put("id",1).put("name","航天大为").put("value",1).put("rate",100.00).build());
//        list.add(MapBuilder.getBuilder().put("id",2).put("name","电信").put("value",1423).put("rate",49.13).build());
//        list.add(MapBuilder.getBuilder().put("id",3).put("name","联通").put("value",1082).put("rate",37.37).build());
        jsonObject.put("value",1);
        jsonObject.put("list",list);
        result.add(jsonObject);
        return result;
    }

    /**
    * @Author beixing
    * @Description  设备品牌
    * @Date  2021/12/16
    * @Param
    * @return
    **/
    public List<JSONObject>  countGroupByBrand(String test) {
        List<JSONObject> result = new ArrayList<>();
        JSONObject jsonObject = new JSONObject();
        List<ChartStatisVO> list = assetMapper.countGroupByBrand();
        long total = list.stream().collect(Collectors.summingLong(it->  it.getCount()));
        list.forEach(it->{
            it.setTotalNum(total);
        });
        jsonObject.put("list",list);
        result.add(jsonObject);
        return result;
    }




    /**
     * @Author beixing
     * @Description  设备类型
     * @Date  2021/12/16
     * @Param
     * @return
     **/
    public List<ChartStatisVO>   countGroupBySwType(String test) {
        List<ChartStatisVO> list = assetMapper.countGroupBySwType();
        long total = list.stream().collect(Collectors.summingLong(it->  it.getCount()));
        list.forEach(it->{
            it.setTotalNum(total);
        });
        return list;
    }

    /** ---------   网络设备 end ----------*/






    /* ---------  重点线路 start ----------*/
    /**
     * @return
     * @Author beixing
     * @Description 重点线路
     * @Date 2021/12/14
     * @Param
     **/
    public List<JSONObject> emphasisPath(String test){
        List<JSONObject> result = new ArrayList<>();
        JSONObject jsonObject = new JSONObject();
        List list = new ArrayList();
        list.add(ImmutableMap.of("pointName","天河机场","deviceTotal",105,"onlineCount",105,"onlineRate",100.0,"offStreamUnit",0));
        list.add(ImmutableMap.of("pointName","马家湖特大桥","deviceTotal",12,"onlineCount",12,"onlineRate",100.0,"offStreamUnit",0));
        list.add(ImmutableMap.of("pointName","黄花涝立交桥","deviceTotal",158,"onlineCount",155,"onlineRate",98.1,"offStreamUnit",3));
        list.add(ImmutableMap.of("pointName","丰荷山互通","deviceTotal",321,"onlineCount",320,"onlineRate",99.6,"offStreamUnit",1));
        list.add(ImmutableMap.of("pointName","机场高速汽车充电站","deviceTotal",258,"onlineCount",256,"onlineRate",99.2,"offStreamUnit",2));
        list.add(ImmutableMap.of("pointName","武汉市消防救援队","deviceTotal",276,"onlineCount",275,"onlineRate",99.6,"offStreamUnit",1));
        list.add(ImmutableMap.of("pointName","唐家墩","deviceTotal",177,"onlineCount",177,"onlineRate",100.0,"offStreamUnit",0));
        list.add(ImmutableMap.of("pointName","竹叶山立交桥","deviceTotal",31,"onlineCount",29,"onlineRate",93.5,"offStreamUnit",2));
        list.add(ImmutableMap.of("pointName","黄埔大街立交桥","deviceTotal",212,"onlineCount",210,"onlineRate",99.0,"offStreamUnit",2));
        list.add(ImmutableMap.of("pointName","四美塘公园","deviceTotal",98,"onlineCount",97,"onlineRate",98.9,"offStreamUnit",1));
        list.add(ImmutableMap.of("pointName","东湖宾馆","deviceTotal",189,"onlineCount",189,"onlineRate",100.0,"offStreamUnit",0));
        jsonObject.put("address",list);
        result.add(jsonObject);
        return result;
    }



    /* ---------  重点线路 end ----------*/


    @Resource
    private AssetMapper assetMapper;

    @Resource
    private AssetBranchAssetMapper assetBranchAssetMapper;

    @Resource
    private AlertRuleMapper alertRuleMapper;


    public static void main(String[] args) {
        System.out.println(getDaysBetween(9));
    }

    /**-------------------------------------------------------- end ---------------------------------------------------------------------------------- */
}
