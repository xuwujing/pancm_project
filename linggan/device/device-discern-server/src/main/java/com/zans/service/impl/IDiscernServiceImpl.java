package com.zans.service.impl;


import com.alibaba.fastjson.JSONObject;
import com.zans.dao.*;
import com.zans.model.DeviceFeatureInfo;
import com.zans.model.DeviceFeatureModelOfficial;
import com.zans.model.DeviceFeatureSample;
import com.zans.service.IDiscernService;
import com.zans.task.AnalyzeDeviceTask;
import com.zans.utils.MyTools;
import com.zans.vo.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.beanutils.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.zans.config.GlobalConstants.TASK_SUC_COUNT;
import static com.zans.config.GlobalConstants.tpx;


/**
 * @author beixing
 * @Title: 表服务实现类
 * @Description:
 * @Version:1.0.0
 * @Since:jdk1.8
 * @date 2021-06-23 17:54:33
 */
@Service("discernServiceImpl")
@Slf4j
public class IDiscernServiceImpl implements IDiscernService {

    @Resource
    private DeviceFeatureDao deviceFeatureDao;

    @Resource
    private DeviceFeatureSampleDao deviceFeatureSampleDao;

    @Resource
    private DeviceFeatureModelOfficialDao deviceFeatureModelOfficialDao;

    @Resource
    private DeviceFeatureModelRelevanceDao deviceFeatureModelRelevanceDao ;

    @Resource
    private DeviceFeatureTypeRelevanceDao deviceFeatureTypeRelevanceDao ;

    @Resource
    private DeviceFeatureInfoDao featureInfoDao;

    @Override
    public ApiResult discern(DiscernRequestVO discernRequestVO) {
        String ip = discernRequestVO.getIp();
        String businessId = discernRequestVO.getBusinessId();
        List<ResultRespVO> versionRespVOS = deviceFeatureDao.groupByBusinessIdAndIp(businessId,ip);
        if(MyTools.isEmpty(versionRespVOS)){
            log.warn("device_feature表无数据，不进行分析!");
            return ApiResult.error("该IP不存在！");
        }
        DeviceFeatureSampleVO sampleVO = new DeviceFeatureSampleVO();
        sampleVO.setBusinessId(discernRequestVO.getBusinessId());
        DeviceFeatureSampleVO deviceFeatureSampleVO = deviceFeatureSampleDao.findOne(sampleVO);
        if(deviceFeatureSampleVO == null){
            return ApiResult.error("暂未分析出该IP对应的型号和类型!");
        }
        DiscernResponseVO discernResponseVO = new DiscernResponseVO();
        discernResponseVO.setIp(ip);
        discernResponseVO.setModel(deviceFeatureSampleVO.getModel());
        discernResponseVO.setType(deviceFeatureSampleVO.getDeviceType());
        return ApiResult.success(discernResponseVO);
    }

    @Override
    public ApiResult analyzeReport(String businessId) {
        log.info("开始进行分析！businessId:{}",businessId);
        if (MyTools.isEmpty(businessId)){
            businessId = "webVersion";
        }
        List<DeviceFeatureInfoVO> infoVOS =  featureInfoDao.queryAll(new DeviceFeatureInfoVO());
        if(MyTools.isEmpty(infoVOS)){
            return ApiResult.error("无数据");
        }
        int modelCount = 0;
        int typeCount = 0;
        for (int i = 0; i < infoVOS.size(); i++) {
            DeviceFeatureInfoVO infoVO = infoVOS.get(i);
            DeviceFeatureSampleVO sampleVO = new DeviceFeatureSampleVO();
            sampleVO.setBusinessId(infoVO.getBusinessId());
            DeviceFeatureSampleVO deviceFeatureSampleVO = deviceFeatureSampleDao.findOne(sampleVO);
            if(deviceFeatureSampleVO == null){
                continue;
            }
            DeviceFeatureVO featureVO = new DeviceFeatureVO();
            try {
                BeanUtils.setProperty(featureVO,businessId,infoVO.getBusinessId());
            } catch (IllegalAccessException | InvocationTargetException e) {
                log.error("设置:#{}值失败！",businessId,e);
            }
            featureVO.setIp(infoVO.getIp());
            DeviceFeatureVO deviceFeatureVO = deviceFeatureDao.findOne(featureVO);
            if(deviceFeatureVO == null){
                continue;
            }
            boolean model =  deviceFeatureSampleVO.getModel().equals(deviceFeatureVO.getModel());
            boolean type =  deviceFeatureSampleVO.getDeviceType().equals(deviceFeatureVO.getDeviceType().toString());
            DeviceFeatureInfo deviceFeatureInfo = new DeviceFeatureInfo();
            deviceFeatureInfo.setBusinessId(infoVO.getBusinessId());
            deviceFeatureInfo.setModel(deviceFeatureSampleVO.getModel());
            deviceFeatureInfo.setDeviceType(Integer.valueOf(deviceFeatureSampleVO.getDeviceType()));
            deviceFeatureInfo.setRealityModel(deviceFeatureVO.getModel());
            deviceFeatureInfo.setRealityDeviceType(deviceFeatureVO.getDeviceType());
            featureInfoDao.update(deviceFeatureInfo);
            if(model){
                modelCount++;
            }
            if(type){
                typeCount++;
            }
        }
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("total",infoVOS.size());
        jsonObject.put("modelCount",modelCount);
        jsonObject.put("typeCount",typeCount);
        BigDecimal total = new BigDecimal(infoVOS.size());
        BigDecimal type = new BigDecimal(typeCount);
        BigDecimal model = new BigDecimal(modelCount);
        BigDecimal typeRate = type.divide(total, 2, RoundingMode.HALF_UP);
        BigDecimal modelRate = model.divide(total, 2, RoundingMode.HALF_UP);
        jsonObject.put("type",typeRate);
        jsonObject.put("model",modelRate);
        log.info("统计完毕!数据:{}",jsonObject);
        return ApiResult.success(jsonObject);
    }



    @Override
    public ApiResult analyze(String businessId) {
        /**
         * 1.分析device_feature的版本，进行group by操作；
         * 2.得到每个group by的值，开启多线程执行特征码分析任务；
         * 3.特征码分析任务，根据目前现有匹配的特征得到设备类别和设备类型;
         * 4.匹配的设备类别和设备类型，不确定唯一值的取最大值，并将此值存储起来到样本表中;
         * 5.后续在维护最新样本表；
         */
        try {
            if (MyTools.isEmpty(businessId)){
                businessId = "web_version";
            }
            List<ResultRespVO> versionRespVOS = deviceFeatureDao.groupByBusinessId(businessId);
            if(MyTools.isEmpty(versionRespVOS)){
                log.warn("device_feature表无数据，不进行分析!");
                return ApiResult.error("处理失败！device_feature表无数据，不进行分析");
            }
            long count = versionRespVOS.size();
            log.info("开始分析!条数:{}",count);
            String finalBusinessId = businessId;
            versionRespVOS.forEach(versionRespVO -> {
                versionRespVO.setFieldName(finalBusinessId);
                tpx.execute(new AnalyzeDeviceTask(versionRespVO));
            });

            while (true){
                if(count == TASK_SUC_COUNT.longValue()){
                    saveSampleModel(finalBusinessId);
                    saveSampleType(finalBusinessId);
                    deviceFeatureDao.insertDeviceFeatureInfo(businessId);
                    log.info("分析完毕！条数:{}",TASK_SUC_COUNT.longValue());
                    break;
                }
            }
        } catch (Exception e) {
            log.error("处理失败！",e);
            return ApiResult.error("处理失败！");
        }

        return ApiResult.success();
    }

    @Override
    public ApiResult reset() {
        TASK_SUC_COUNT.getAndSet(0);
        deviceFeatureDao.reset();
        log.info("表已清除");
        return ApiResult.success();
    }

    @Override
    public ApiResult matchType() {
        List<DeviceFeatureModelOfficialVO> officialVOS= deviceFeatureModelOfficialDao.queryByCamera();
        if(MyTools.isEmpty(officialVOS)){
            return ApiResult.error("无数据");
        }
        for (int i = 0; i < officialVOS.size(); i++) {
            DeviceFeatureModelOfficialVO officialVO = officialVOS.get(i);
            String modelCodeNew = officialVO.getModelCodeNew();
            String modelCode = officialVO.getModelCode();
            String deviceTypeNew = matchCameraType(modelCode);
            if(MyTools.isEmpty(deviceTypeNew)){
                continue;
            }
            DeviceFeatureModelOfficial deviceFeatureModelOfficial = new DeviceFeatureModelOfficial();
            deviceFeatureModelOfficial.setDeviceTypeNew(deviceTypeNew);
            deviceFeatureModelOfficial.setModelCodeNew(modelCodeNew);
            deviceFeatureModelOfficialDao.update(deviceFeatureModelOfficial);
        }
        return ApiResult.success();
    }

    /**
    * @Author beixing
    * @Description  网络摄像机的匹配规则
    * @Date  2021/7/29
    * @Param
    * @return
    **/
    private String matchCameraType(String model){
        Map<String,String> structureTypeMap = new HashMap<>();
        structureTypeMap.put("C","模拟摄像机");
        structureTypeMap.put("H","快球");
        structureTypeMap.put("M","一体机");
        structureTypeMap.put("D","数字摄像机");
        Map<String,String> cameraTypeMap = new HashMap<>();
        cameraTypeMap.put("1","普通型摄像");
        cameraTypeMap.put("2","一体化摄像机");
        cameraTypeMap.put("5","球型摄像");
        cameraTypeMap.put("7","IP球型摄像机");
        cameraTypeMap.put("8","IP枪型摄像机");
        cameraTypeMap.put("0","其他类型摄像");
        cameraTypeMap.put("3","其他类型摄像");
        cameraTypeMap.put("4","其他类型摄像");
        cameraTypeMap.put("6","其他类型摄像");
        cameraTypeMap.put("9","其他类型摄像");
        if(model.length()<9){
            return "";
        }

        //结构类型(6) C:模拟摄像机，H：快球，Z：一体机，M：一体机芯片，D：数字摄像机
        String structureType = model.substring(5,6);
        //摄像机类型(7) 1:普通型摄像 2:一体化摄像机 5:球型摄像 7:IP型摄像机 8:IP枪型摄像机 0,3,4,6,9其他类型摄像机
        String cameraType = model.substring(6,7);
        String name = structureTypeMap.get(structureType);
        String name2 = cameraTypeMap.get(cameraType);
        return getModelTypeName(name,name2);
    }


    private String getModelTypeName(String name,String name2){
        if(MyTools.isEmpty(name)){
            name= "摄像机";
        }
        if(MyTools.isEmpty(name2)){
            name2= "其他摄像机";
        }
        return name.concat("_")+name2;
    }



    private void saveSampleModel(String finalBusinessId) {
        DeviceFeatureModelRelevanceVO deviceFeatureModelRelevanceVO = new DeviceFeatureModelRelevanceVO();
//        deviceFeatureModelRelevanceVO.setStatus(2);
        List<DeviceFeatureModelRelevanceVO> voList = deviceFeatureModelRelevanceDao.queryAll(deviceFeatureModelRelevanceVO);
        if(MyTools.isEmpty(voList)){
            return ;
        }
        for (int i = 0; i < voList.size(); i++) {
            DeviceFeatureModelRelevanceVO vo = voList.get(i);
            DeviceFeatureSample deviceFeatureSample = new DeviceFeatureSample();
            String businessId = vo.getBusinessId();
            String model = vo.getModel();
            List<ResultRespVO> resultRespVOS = deviceFeatureDao.groupByDeviceType(finalBusinessId,businessId,model);
            if(MyTools.isNotEmpty(resultRespVOS)){
                deviceFeatureSample.setDeviceType(resultRespVOS.get(0).getName());
                if(resultRespVOS.size() == 1){
                    deviceFeatureSample.setDeviceTypeStatus(2);
                }else {
                    deviceFeatureSample.setDeviceTypeStatus(1);
                }
            }
            deviceFeatureSample.setModel(model);
            deviceFeatureSample.setModelStatus(vo.getStatus());
            DeviceFeatureSampleVO sampleVO = new DeviceFeatureSampleVO();
            sampleVO.setBusinessId(businessId);
            sampleVO.setBusinessIdKey(finalBusinessId);
            DeviceFeatureSampleVO sampleVO1 = deviceFeatureSampleDao.findOne(sampleVO);
            if(sampleVO1 == null){
                deviceFeatureSample.setBusinessId(businessId);
                deviceFeatureSample.setBusinessIdKey(finalBusinessId);
                deviceFeatureSampleDao.insert(deviceFeatureSample);
            }else {
                deviceFeatureSampleDao.update(deviceFeatureSample);
            }
        }
        return ;
    }

    private void saveSampleType(String finalBusinessId) {
        DeviceFeatureTypeRelevanceVO typeRelevanceVO = new DeviceFeatureTypeRelevanceVO();
//        typeRelevanceVO.setStatus(2);
        List<DeviceFeatureTypeRelevanceVO> voList1 = deviceFeatureTypeRelevanceDao.queryAll(typeRelevanceVO);
        if(MyTools.isEmpty(voList1)){
            return ;
        }
        for (int i = 0; i < voList1.size(); i++) {
            DeviceFeatureTypeRelevanceVO vo = voList1.get(i);
            DeviceFeatureSample deviceFeatureSample = new DeviceFeatureSample();
            String businessId = vo.getBusinessId();
            String deviceType = vo.getDeviceType();
            List<ResultRespVO> resultRespVOS = deviceFeatureDao.groupByModel(finalBusinessId,businessId,deviceType);
            if(MyTools.isNotEmpty(resultRespVOS)){
                deviceFeatureSample.setModel(resultRespVOS.get(0).getName());
                if(resultRespVOS.size() == 1){
                    deviceFeatureSample.setModelStatus(2);
                }else {
                    deviceFeatureSample.setModelStatus(1);
                }
            }
            deviceFeatureSample.setDeviceTypeStatus(vo.getStatus());
            deviceFeatureSample.setDeviceType(vo.getDeviceType());
            DeviceFeatureSampleVO sampleVO = new DeviceFeatureSampleVO();
            sampleVO.setBusinessId(businessId);
            DeviceFeatureSampleVO sampleVO1 = deviceFeatureSampleDao.findOne(sampleVO);
            if(sampleVO1 == null){
                deviceFeatureSample.setBusinessId(businessId);
                deviceFeatureSampleDao.insert(deviceFeatureSample);
            }else {
                deviceFeatureSampleDao.update(deviceFeatureSample);
            }
        }
        return ;
    }

}
