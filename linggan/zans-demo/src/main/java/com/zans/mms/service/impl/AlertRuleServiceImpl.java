package com.zans.mms.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.zans.base.util.MapBuilder;
import com.zans.base.vo.ApiResult;
import com.zans.base.vo.PageResult;
import com.zans.base.vo.SelectVO;
import com.zans.mms.controller.demokit.RadiusQzController;
import com.zans.mms.dao.guard.AlertRuleMapper;
import com.zans.mms.dao.guard.RadiusEndpointMapper;
import com.zans.mms.dao.guard.SysSwitcherBranchMapper;
import com.zans.mms.service.IAlertRuleService;
import com.zans.mms.service.IRadiusEndPointService;
import com.zans.mms.vo.alert.*;
import com.zans.mms.vo.radius.EndPointViewVO;
import com.zans.mms.vo.radius.QzRespVO;
import com.zans.mms.vo.switcher.SwitchBranchResVO;
import com.zans.mms.vo.switcher.SwitchBranchSearchVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

/**
 * @author pancm
 * @Title: portal
 * @Description:
 * @Version:1.0.0
 * @Since:jdk1.8
 * @date 2020/8/27
 */
@Service("alertRuleService")
public class AlertRuleServiceImpl implements IAlertRuleService {

    @Autowired
    private AlertRuleMapper mapper;

    @Autowired
    private RadiusEndpointMapper radiusEndpointMapper;

    @Autowired
    private IRadiusEndPointService radiusEndPointService;

    @Autowired
    private RadiusQzController radiusQzController;


    @Autowired
    private SysSwitcherBranchMapper switcherBranchMapper;


    @Override
    public List<String> getRecordByKeywordValues(Map<String, Object> kvs) {
        return mapper.getRecordByKeywordValues(kvs);
    }

    @Override
    public List<String> getRecordByIp(Map<String, Object> kvs) {
        return mapper.getRecordByIp(kvs);
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
//        Long dealCount = mapper.getAlertRecordDealCount2();
//        AlertReportDisRespVO alertReportDisRespVO = new AlertReportDisRespVO();
//        alertReportDisRespVO.setName("全部");
//        alertReportDisRespVO.setValue(dealCount);
//        resultList.add(alertReportDisRespVO);
        List<AlertReportDisRespVO> typeRespVO =null;
        if(status ==0){
            typeRespVO = mapper.getAlertRecordType();
            resultList.addAll(typeRespVO);
            Map<String, Object> result = MapBuilder.getBuilder()
                    .put("type", resultList)
                    .build();
            return ApiResult.success(result);
        }

        typeRespVO = mapper.getAlertRecordType2();
        long total =  typeRespVO.get(0).getValue();
        BigDecimal rateTotal = new BigDecimal(total);
        if(typeRespVO!=null){
            for (int i = 1; i < typeRespVO.size(); i++) {
                long value = typeRespVO.get(i).getValue();
                BigDecimal rate = new BigDecimal(value);
                if(value!= 0 && total!=0 ){
                    rate = rate.divide(rateTotal,4, RoundingMode.HALF_UP);
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
    public PageResult<AlertRecordRespVO> getAlertRecordPage(AlertRecordSearchVO reqVO) {
        Page page = PageHelper.startPage(reqVO.getPageNum(), reqVO.getPageSize());
        if (!StringUtils.isEmpty(reqVO.getUsername())) {
            reqVO.setUsername(StringUtils.trimAllWhitespace(reqVO.getUsername()));
        }
        List<AlertRecordRespVO> alertRecordRespVOList = new ArrayList<>();
        if(reqVO.getNoticeTime()!=null){
            if(reqVO.getNoticeTime()==1){
                reqVO.setNoticeTime(24);
            }
            if(reqVO.getNoticeTime()==2){
                reqVO.setNoticeTime(24*7);
            }
            if(reqVO.getNoticeTime()==3){
                reqVO.setNoticeTime(24*7*2);
            }
        }
        if (reqVO.getTypeId() != null && reqVO.getTypeId() == 1) {
            alertRecordRespVOList = mapper.getAlertRecord(reqVO);
        } else {
            alertRecordRespVOList = mapper.getAlertRecord2(reqVO);
        }
        tabAuthData(alertRecordRespVOList);
//        tabData(alertRecordRespVOList);
        //todo 此方法报错 因为不存在isRead字段
        //updateReadStatus(alertRecordRespVOList);

        return new PageResult<AlertRecordRespVO>(page.getTotal(), alertRecordRespVOList, reqVO.getPageSize(), reqVO.getPageNum());
    }


    /**
     * @return void
     * @Author pancm
     * @Description 补全频繁认证的数据
     * @Date 2020/12/1
     * @Param [alertRecordRespVOList]
     **/
    private void tabAuthData(List<AlertRecordRespVO> alertRecordRespVOList) {
        final String defaultName = "(交换机)";
        final String defaultName2 = "N/A";
        final String defaultIp = "framed_ip_address=null";
        for (int i = 0; i < alertRecordRespVOList.size(); i++) {
            AlertRecordRespVO alertRecordRespVO = alertRecordRespVOList.get(i);
            if (alertRecordRespVO.getRuleId() == 13) {
                if (StringUtils.isEmpty(alertRecordRespVO.getUsername())) {
                    AlertRecordRespVO alertRecordRespVO2 = mapper.getRecordByMac(alertRecordRespVO.getKeywordValue());
                    if (alertRecordRespVO2 != null) {
                       /* if (!StringUtils.isEmpty(alertRecordRespVO2.getAreaName())) {
                            alertRecordRespVO.setAreaName(alertRecordRespVO2.getAreaName() + defaultName);
                        } else {
                            alertRecordRespVO.setAreaName(defaultName2);
                        }*/
                        if (!StringUtils.isEmpty(alertRecordRespVO2.getPointName())) {
                            alertRecordRespVO.setPointName(alertRecordRespVO2.getPointName() + defaultName);
                        } else {
                            alertRecordRespVO.setPointName(defaultName2);
                        }
                        alertRecordRespVO.setDeviceTypeName(defaultName2);
                        alertRecordRespVO.setIpAddr(defaultIp);
                        alertRecordRespVO.setUsername(alertRecordRespVO2.getUsername());
                    }else {
                        //alertRecordRespVO.setAreaName(defaultName2);
                        alertRecordRespVO.setPointName(defaultName2);
                        alertRecordRespVO.setDeviceTypeName(defaultName2);
                        alertRecordRespVO.setIpAddr(defaultIp);
                        alertRecordRespVO.setUsername(alertRecordRespVO.getKeywordValue());
                    }
                }else {
                    /*if (StringUtils.isEmpty(alertRecordRespVO.getAreaName())) {
                        alertRecordRespVO.setAreaName(defaultName2);
                    }*/
                    if (StringUtils.isEmpty(alertRecordRespVO.getPointName())) {
                        alertRecordRespVO.setPointName(defaultName2);
                    }

                }
            } else if (alertRecordRespVO.getRuleId() == 12) {
                String noticeInfo = alertRecordRespVO.getNoticeInfo();
                if (StringUtils.isEmpty(alertRecordRespVO.getIpAddr())) {
                    String ip = getIP(noticeInfo);
                    alertRecordRespVO.setIpAddr(ip);
                }

                if (StringUtils.isEmpty(alertRecordRespVO.getUsername())) {
                    String[] macs = getMac(noticeInfo);
                    alertRecordRespVO.setUsername(macs[1]);
                }

                if (StringUtils.isEmpty(alertRecordRespVO.getPointName())) {
                    alertRecordRespVO.setPointName(defaultName2);
                }

                if (StringUtils.isEmpty(alertRecordRespVO.getDeviceTypeName())) {
                    alertRecordRespVO.setDeviceTypeName(defaultName2);
                }

               /* if (StringUtils.isEmpty(alertRecordRespVO.getAreaName())) {
                    alertRecordRespVO.setAreaName(defaultName2);
                }*/
            }
        }
    }


    private void updateReadStatus(List<AlertRecordRespVO> alertRecordRespVOList) {
        Map<String, Object> map = new HashMap<>();
        Set<String> ids = new HashSet<>();
        for (AlertRecordRespVO alertRecordRespVO : alertRecordRespVOList) {
            ids.add(alertRecordRespVO.getKeywordValue());
        }
        if(ids.size()>0){
            map.put("ids",ids);
            //todo 此sql报错
            //mapper.batchUpdateByIds(map);
        }
    }

    @Override
    public ApiResult getAlertRecordView(Long id, int status, int typeId) {
        AlertRecordRespVO alertRecord = null;
        if (typeId == 1) {
            alertRecord = mapper.getAlertRecordOriginalView(id);
        } else {
            alertRecord = mapper.getAlertRecordView2(id);
        }
        if (alertRecord == null) {
            return ApiResult.error("该记录ID:" + id + " 已经消除了!");
        }

        String keywordValue = alertRecord.getKeywordValue();
        String authMark = alertRecord.getAuthMark();
        Map<String, Object> recordMap = new HashMap<>();
        Map<String, Object> resultMap = new HashMap<>();
        //这里需要对ip冲突规则做特殊处理
        if (alertRecord.getTemplateId() == 2) {
            dealSpecial(alertRecord, authMark, recordMap, resultMap);
            return ApiResult.success(recordMap);
        }
        //这里对交换机成环检测的做特殊处置
        if (alertRecord.getTemplateId() == 3) {
            getAlertLoop(alertRecord, recordMap);
            return ApiResult.success(recordMap);
        }


        AlertDetailRespVO alertDetailRespVO = mapper.getAlertRecordOriginalDetailView(alertRecord.getBusinessId());
        recordMap.put("alertRecord", alertRecord);
        String nasPortId = alertDetailRespVO.getNasPortId();
        if(!StringUtils.isEmpty(nasPortId) && alertRecord.getRuleId() == 13){
            nasPortId=getPortId(nasPortId);
            alertDetailRespVO.setNasPortId(nasPortId);
        }

        recordMap.put("alertDetail",alertDetailRespVO);

        return ApiResult.success(recordMap);

//        EndPointViewVO endPointViewVO = radiusEndpointMapper.findEndPointByPass(keywordValue);
//        QzRespVO respVO;
//        if (endPointViewVO != null) {
//            BeanUtils.copyProperties(endPointViewVO, alertRecord);
//            alertRecord.setEndPointId(endPointViewVO.getId());
//            alertRecord.setAccessPolicy(Integer.valueOf(endPointViewVO.getAccessPolicy()));
//            alertRecord.setMac(keywordValue);
//            //这里因为在copy的时候会把改值覆盖为null，因此需要重新赋值一下
//            alertRecord.setAuthMark(authMark);
//            recordMap.put("alertRecord", alertRecord);
//            // 2020-9-25 新增逻辑，增加检疫区详情显示
//            respVO = radiusEndPointService.findQzById(endPointViewVO.getId());
//            if (respVO != null) {
//                resultMap = radiusQzController.getIpAndAssetsCp(respVO);
//            } else {
//                respVO = new QzRespVO();
//            }
//            Map<String, Object> map = MapBuilder.getBuilder()
//                    .put("radiusQz", respVO)
//                    .build();
//            map.putAll(resultMap);
//            recordMap.putAll(map);
//        } else {
//            List<QzViewRespVO> ipView = new ArrayList<>();
//            List<QzViewRespVO> arpView = new ArrayList<>();
//            QzViewRespVO qv1 = new QzViewRespVO();
//            qv1.setIpStatus(0);
//            ipView.add(qv1);
//            QzViewRespVO qv2 = new QzViewRespVO();
//            qv2.setArpStatus(0);
//            arpView.add(qv2);
//
//            recordMap.put("radiusQz", new QzRespVO());
//            recordMap.put("ipView", ipView);
//            recordMap.put("arpView", arpView);
//            recordMap.put("alertRecord", alertRecord);
//        }
//        return ApiResult.success(recordMap);
    }

    @Override
    public List<AlertRecordRespVO> getAlertRecordData() {
        return  mapper.getAlertRecordData();
    }

    @Override
    public void saveAlertData(int ruleId, String businessId, String ipAddr, String mac, String msg) {
        mapper.saveAlertData(ruleId,businessId,ipAddr,mac,msg);
    }

    private void dealSpecial(AlertRecordRespVO alertRecord,  String authMark, Map<String, Object> recordMap, Map<String, Object> resultMap) {

        List<AlertIpClashRespVO> alertIpClashRespVOS =mapper.getAlertIpClash(alertRecord.getBusinessId());


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
        if (curEPV != null&&baseEPV!=null) {
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


    private void getAlertLoop(AlertRecordRespVO alertRecord, Map<String, Object> recordMap) {
        String ipAddr = alertRecord.getKeywordValue();
        SwitchBranchSearchVO switchBranchSearchVO = new SwitchBranchSearchVO();
        switchBranchSearchVO.setIpAddr(ipAddr);
        List<SwitchBranchResVO> list = switcherBranchMapper.findSwitchList(switchBranchSearchVO);
        List<AlertLoopRespVO> alertLoopRespVO = mapper.getAlertLoop(ipAddr);

        recordMap.put("alertRecord", alertRecord);
        recordMap.put("alertLoop", alertLoopRespVO);
        List<AlertReportRespVO> list1 = new ArrayList<>();
        if (alertLoopRespVO != null && alertLoopRespVO.size() > 0) {
            alertLoopRespVO.forEach(alertLoopRespVO1 -> {
                AlertReportRespVO alertReportRespVO = new AlertReportRespVO();
                String port = getPort(alertLoopRespVO1.getPort());
                String port2 = getPort2(alertLoopRespVO1.getPort2());
                alertReportRespVO.setName(port2);
                alertReportRespVO.setValue((long) alertLoopRespVO1.getStatus());
                list1.add(alertReportRespVO);
                alertLoopRespVO1.setPort(port);
            });
        }
        recordMap.put("alertLoopStatus", list1);
        if (list!=null) {
            recordMap.put("switch", list.get(0));
        } else {
            recordMap.put("switch", new SwitchBranchResVO());
        }
    }

    private String getPort(String msg) {
        final String eth = "eth";
        final String commaSign = ",";
        String[] msgS = msg.split(commaSign);
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < msgS.length; i++) {
            String s = msgS[i];
            if (!(s.substring(0, 3).toLowerCase().indexOf(eth) > -1)) {
                continue;
            }
            if (i > 0) {
                sb.append(commaSign);
            }
            sb.append(s);
        }
//        return msg.replaceAll("[A-Za-z]","");
        return sb.toString();
    }

    private String getPort2(String msg) {
        return msg.replaceAll("[A-Za-z]", "");
    }


    private String getIP(String msg) {
        String m1 = "警告:";
        String m2 = "地址";
        String msg1 = substringAfter(msg, m1);
        msg1 = substringBefore(msg1, m2);
        return msg1;
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
//        Matcher matcher = macPattern.matcher(msg);
//        int i=0;
//        while (matcher.find()) {
//            String key = matcher.group();
//            macs[i]=key;
//            i++;
//        }
        return macs;
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

    private String getPortId(String nasPortId) {
        String []portIds  = nasPortId.split(";");
        if(portIds.length<3){
            return nasPortId;
        }
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < 3; i++) {
            String sp = portIds[i];
            if(i>0){
                sb.append("/");
            }
            sb.append(substringAfter(sp,"="));
        }
        return sb.toString();
    }

}
