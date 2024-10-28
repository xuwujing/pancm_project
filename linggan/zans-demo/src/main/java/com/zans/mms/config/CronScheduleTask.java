package com.zans.mms.config;

import com.alibaba.fastjson.JSONObject;
import com.zans.base.util.RedisUtil;
import com.zans.mms.dao.guard.*;
import com.zans.mms.model.AssetDiagnosisInfo;
import com.zans.mms.model.AssetDiagnosisInfoEx;
import com.zans.mms.model.AssetDiagnosisInfoHis;
import com.zans.mms.model.AssetDiagnosticThreshold;
import com.zans.mms.service.IConstantItemService;
import com.zans.mms.service.ICronJobService;
import com.zans.mms.service.IPatrolTaskService;
import com.zans.mms.vo.asset.diagnosis.AssetDiagnosticThresholdVO;
import com.zans.mms.vo.asset.diagnosis.AssetThresholdVO;
import com.zans.mms.vo.asset.diagnosis.DiagnosticThresholdOverallVO;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.io.File;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

import static com.zans.mms.config.AssetConstants.ASSET_DIAGNOSIS_QUEUE;

@Slf4j
@Configuration
@EnableScheduling
public class CronScheduleTask {
    @Autowired
    ICronJobService cronJobService;

    @Value("${patrol.task:0}")
    private String patrolTaskFlag;

    @Value("${api.imgUrl.folder}")
    private String imgUrl;

    @Value("${file.img.retentionDays:60}")
    private Integer retentionDays;

    @Autowired
    IPatrolTaskService patrolTaskService;


    private final String raw = "raw";

    @Resource
    private RedisUtil redisUtil;


    @Resource
    private AssetDiagnosisInfoDao assetDiagnosisInfoDao;

    @Resource
    private AssetDiagnosisInfoHisDao assetDiagnosisInfoHisDao;

    @Resource
    private AssetDiagnosisInfoExDao assetDiagnosisInfoExDao;

    @Autowired
    private IConstantItemService constantItemService;

    @Resource
    private DiagnosticThresholdOverallDao diagnosticThresholdOverallDao;

    @Resource
    private AssetDiagnosticThresholdDao assetDiagnosticThresholdDao;

    //3.添加定时任务 0 0 1 * * ?
    @Scheduled(cron = "0 0 2 * * ?")
    //或直接指定时间间隔，例如：5秒
    //@Scheduled(fixedRate=5000)
    private void configureTasks() {
        if ("1".equals(patrolTaskFlag)){
            log.info("巡检计划自动新建任务开始: " + LocalDateTime.now());
            cronJobService.generatePatrolTask();
            log.info("巡检计划自动新建任务结束: " + LocalDateTime.now());
        }
    }


    //3.添加定时任务 0 0 1 * * ?
    @Scheduled(cron = "0 0 2 * * ?")
    //或直接指定时间间隔，例如：5秒
    //@Scheduled(fixedRate=5000)
    private void delFileTask() {
        log.info("删除图片文件开始: " + LocalDateTime.now());
        delFile();
        log.info("删除图片文件结束: " + LocalDateTime.now());
    }


    private void delFile(){
        LocalDateTime localDateTime = LocalDateTime.now().plusDays((retentionDays * -1));
        int yyyy = localDateTime.getYear();
        int mm = localDateTime.getMonthValue();
        int dd = localDateTime.getDayOfMonth();
        String path = imgUrl + File.separator+ raw + File.separator+ yyyy+File.separator+mm+File.separator+dd;
        File file = new File(path);
        deleteFile(file);
    }

    /**
     * 删除当前文件夹下所有文件包括当前文件夹
     * @param monFile
     */
    private void deleteFile(File monFile) {
        try{
            File[] monFiles = monFile.listFiles();
            for(File file : monFiles){
                if(file.isDirectory()){
                    deleteFile(file);
                }else{
                    file.delete();
                }
            }
            monFile.delete();
        }catch(Exception e){
            log.error("删除文件出错，原因是：" + e.getMessage());
        }
    }

    @Scheduled(cron = "0 0 16 * * ?")
    //或直接指定时间间隔，例如：5秒
    //@Scheduled(fixedRate=5000)
    private void pushPatrolMessage() {
        if ("1".equals(patrolTaskFlag)){
            log.info("巡检任务消息推送开始: " + LocalDateTime.now());
            patrolTaskService.pushPatrolMessage();
            log.info("巡检任务消息推送结束: " + LocalDateTime.now());
        }
    }

    /**
    * @Author beiming
    * @Description   每5秒拉取一次视频诊断结果
    * @Date  4/27/21
    * @Param
    * @return
    **/
    @Scheduled(cron = "0/5 * * * * ?")
    private void pullDiagnosisMessage() {
        String data = redisUtil.leftPop(ASSET_DIAGNOSIS_QUEUE);
        if (StringUtils.isEmpty(data)){
            return;
        }
        dealDiagnosisData(data);

    }

    /**
    * @Author beixing
    * @Description  处理诊断的数据
    * @Date  2021/5/14
    * @Param
    * @return
    **/
    private void dealDiagnosisData(String data){
        log.info("拉取到诊断的数据:{}",data);
        JSONObject jsonObject = JSONObject.parseObject(data).getJSONObject("data");
        JSONObject resultData = jsonObject.getJSONObject("result");
        JSONObject faultTypeJson = resultData.getJSONObject("faultTypeResult");
        String deviceId = jsonObject.getString("deviceId");
        if(StringUtils.isEmpty(deviceId)){
            return;
        }
        String ipAddress = jsonObject.getString("ipAddress");
        AssetDiagnosisInfo assetDiagnosisInfo = new AssetDiagnosisInfo();
        assetDiagnosisInfo.setAssetCode(deviceId);
        assetDiagnosisInfo.setTraceId(jsonObject.getString("traceId"));
        assetDiagnosisInfo.setImgUrl(resultData.getString("fileUrl"));
        assetDiagnosisInfo.setDiagnosisTime(resultData.getString("diagnosisTime"));
        assetDiagnosisInfo.setIpAddress(ipAddress);
        List<AssetDiagnosisInfoEx> assetDiagnosisInfoExes = new ArrayList<>();
        StringBuffer sb = new StringBuffer();
        StringBuffer sbName = new StringBuffer();
        Map<Object, String> stringMap =  constantItemService.findItemsMapByDict("faultType");
        // 这里的判断优先使用设备的阈值，如果设置没有配置，那么使用全局的阈值并且将全局的阈值写入到设置阈值中
        AssetDiagnosticThresholdVO assetDiagnosticThresholdVO = new AssetDiagnosticThresholdVO();
        assetDiagnosticThresholdVO.setDeviceId(deviceId);
        assetDiagnosticThresholdVO.setIpaddress(ipAddress);
        List<AssetThresholdVO> assetThresholdVOS = new ArrayList<>();
        // 查询该设备的阈值
        List<AssetDiagnosticThresholdVO> assetDiagnosticThresholdVOS = assetDiagnosticThresholdDao.queryAll(assetDiagnosticThresholdVO);
        if(assetDiagnosticThresholdVOS == null ||assetDiagnosticThresholdVOS.size() == 0){
            //查询全局的阈值
            List<DiagnosticThresholdOverallVO> diagnosticThresholdOverallVOS = diagnosticThresholdOverallDao.queryAll(new DiagnosticThresholdOverallVO());
            List<AssetDiagnosticThreshold> assetDiagnosticThresholds = new ArrayList<>();
            for (DiagnosticThresholdOverallVO diagnosticThresholdOverallVO : diagnosticThresholdOverallVOS) {
                AssetThresholdVO assetThresholdVO = new AssetThresholdVO();
                BeanUtils.copyProperties(diagnosticThresholdOverallVO,assetThresholdVO);
                assetThresholdVOS.add(assetThresholdVO);
                AssetDiagnosticThreshold assetDiagnosticThreshold = new AssetDiagnosticThreshold();
                BeanUtils.copyProperties(diagnosticThresholdOverallVO,assetDiagnosticThreshold);
                assetDiagnosticThreshold.setDeviceId(deviceId);
                assetDiagnosticThreshold.setIpaddress(ipAddress);
                assetDiagnosticThresholds.add(assetDiagnosticThreshold);
            }
            assetDiagnosticThresholdDao.insertBatch(assetDiagnosticThresholds);
        }else {
            for (AssetDiagnosticThresholdVO diagnosticThresholdOverallVO : assetDiagnosticThresholdVOS) {
                AssetThresholdVO assetThresholdVO = new AssetThresholdVO();
                BeanUtils.copyProperties(diagnosticThresholdOverallVO,assetThresholdVO);
                assetThresholdVOS.add(assetThresholdVO);
            }
        }

        int  diagnosisResult = 1;
        for (Map.Entry<String, Object> e : faultTypeJson.entrySet()) {
            AssetDiagnosisInfoEx assetDiagnosisInfoEx = new AssetDiagnosisInfoEx();

            // 这里用100减去该值的原因是，诊断算法是值越大问题概率越大
            // 而演示项目这边需要值越大越好，反正则否
            int curFaultTypeThreshold = 0;
            Object obj = e.getValue();
            //诊断算法改成分数之后出现了超过100分且会存在小数的情况，这里要处理
            if(obj instanceof Integer){
                int k = (int) obj;
                if(k>100){
                    k= 100;
                }
                curFaultTypeThreshold = 100-k;
            }else if(obj instanceof BigDecimal){
                BigDecimal bigDecimal = (BigDecimal) obj;
                int k = bigDecimal.intValue();
                if(k>100){
                    k= 100;
                }
                curFaultTypeThreshold = 100-k;
            }else {
                log.warn("未知的分数类型！obj：{},class:{}",obj,obj.getClass());
                continue;
            }


            FaultTypeThresholdBean faultTypeThresholdBean = isCompare(Integer.parseInt(e.getKey()),curFaultTypeThreshold,assetThresholdVOS);
            boolean faultTypeFlag = faultTypeThresholdBean.getFlag();
            if (faultTypeFlag) {
                sb.append(e.getKey()).append(",");
                sbName.append(stringMap.get(Integer.valueOf(e.getKey()))).append(",");
                diagnosisResult = 2;
            }

            assetDiagnosisInfoEx.setFaultType(Integer.valueOf(e.getKey()));
            assetDiagnosisInfoEx.setFaultTypeResult(faultTypeThresholdBean.getCurFaultTypeThreshold());
            assetDiagnosisInfoEx.setFaultTypeResultName(faultTypeFlag?"异常":"正常");
            assetDiagnosisInfoEx.setAssetCode(deviceId);
            assetDiagnosisInfoEx.setTraceId(jsonObject.getString("traceId"));
            assetDiagnosisInfoEx.setDiagnosisResult(diagnosisResult);
            assetDiagnosisInfoEx.setImgUrl(resultData.getString("fileUrl"));
            assetDiagnosisInfoEx.setDiagnosisTime(resultData.getString("diagnosisTime"));
            assetDiagnosisInfoExes.add(assetDiagnosisInfoEx);
        }
        if(sb.length()>1){
            sb.delete(sb.length()-1,sb.length());
        }
        if(sbName.length()>1){
            sbName.delete(sbName.length()-1,sbName.length());
        }
        //这个设备异常判断需要根据阈值设置来判断
        assetDiagnosisInfo.setDiagnosisResult(diagnosisResult);
        assetDiagnosisInfo.setFaultTypes(sb.toString());
        assetDiagnosisInfo.setFaultTypesName(sbName.toString());
        int exist = assetDiagnosisInfoDao.isExist(deviceId);
        if(exist>0){
            assetDiagnosisInfoDao.update(assetDiagnosisInfo);
        }else {
            assetDiagnosisInfoDao.insert(assetDiagnosisInfo);
        }
        AssetDiagnosisInfoHis assetDiagnosisInfoHis = new AssetDiagnosisInfoHis();
        BeanUtils.copyProperties(assetDiagnosisInfo,assetDiagnosisInfoHis);
        assetDiagnosisInfoHisDao.insert(assetDiagnosisInfoHis);
        assetDiagnosisInfoExDao.insertBatch(assetDiagnosisInfoExes);
    }


    private FaultTypeThresholdBean isCompare(int faultType,int curFaultTypeThreshold,List<AssetThresholdVO> diagnosticThresholdOverallVOS){
        FaultTypeThresholdBean faultTypeThresholdBean = new FaultTypeThresholdBean();
        for (AssetThresholdVO vo : diagnosticThresholdOverallVOS) {
            int faultType2 = vo.getFaultType();
            Integer faultTypeThreshold = vo.getFaultTypeThreshold();
            if( faultType2 == faultType){
//                curFaultTypeThreshold = dealCoverOrBlurry(faultType,curFaultTypeThreshold);
                //这里改成小于
                faultTypeThresholdBean.setFlag(curFaultTypeThreshold<=faultTypeThreshold);
                faultTypeThresholdBean.setCurFaultTypeThreshold(curFaultTypeThreshold);
                faultTypeThresholdBean.setFaultType(faultType);
                return faultTypeThresholdBean;
            }
         }
        faultTypeThresholdBean.setFlag(false);
        faultTypeThresholdBean.setCurFaultTypeThreshold(curFaultTypeThreshold);
        faultTypeThresholdBean.setFaultType(faultType);
        return faultTypeThresholdBean;
    }


    /**
     *  处理遮挡和模糊的数据
     * @param faultType
     * @param curFaultTypeThreshold
     * @return
     */
    private int dealCoverOrBlurry(int faultType,int curFaultTypeThreshold){
        // 遮挡是61-100 模糊是0-60
        int blurry = 60;
        int cover = 61;
        int faultTypeCover = 110001;
        int faultTypeBlurry = 160001;
        //如果是0分,那么就改成100,目前是分越高越好
//        if(curFaultTypeThreshold == 0){
//            return 100;
//        }
        if(faultType == faultTypeCover || faultType == faultTypeBlurry){
            //这里要换算100分，因此需要处理一下
            //如果是遮挡，则减去60然后除以0.4进行计算
            //否则是模糊，除以0.6进行计算
            if(faultType == faultTypeCover && curFaultTypeThreshold>= cover){
                double result = (curFaultTypeThreshold-40) / 0.6;
                int roundNum = (int) Math.round(result);
                log.info("视频诊断遮挡(110001)分值进行转换成功!转换前值:{},转换后值:{}",curFaultTypeThreshold,roundNum);
                curFaultTypeThreshold = roundNum;
                return curFaultTypeThreshold;
            }
            if (faultType == faultTypeBlurry && curFaultTypeThreshold <= blurry){
//                if(curFaultTypeThreshold == 0){
//                    return curFaultTypeThreshold;
//                }
                double result = curFaultTypeThreshold / 0.6;
                int roundNum = (int) Math.round(result);
                log.info("视频诊断模糊(160001)分值进行转换成功!转换前值:{},转换后值:{}",curFaultTypeThreshold,roundNum);
                curFaultTypeThreshold = roundNum;
                return curFaultTypeThreshold;
            }
            log.info("视频诊断类型:{},值:{},不满足转换!直接赋值",faultType,curFaultTypeThreshold);
            int ran = new Random().nextInt(10);
            return 90 + ran;
        }
        return  curFaultTypeThreshold;
    }




}

@Data
class FaultTypeThresholdBean{
    private Integer curFaultTypeThreshold;
    private Boolean flag;
    private Integer faultType;

}
