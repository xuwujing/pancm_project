package com.zans.mms.config;

import com.alibaba.fastjson.JSONObject;
import com.zans.base.util.RedisUtil;
import com.zans.mms.dao.*;
import com.zans.mms.model.*;
import com.zans.mms.service.IConstantItemService;
import com.zans.mms.service.ICronJobService;
import com.zans.mms.service.IPatrolTaskService;
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
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.zans.mms.config.AssetConstants.ASSET_DIAGNOSIS_QUEUE;

@Slf4j
@Configuration
@EnableScheduling
public class CronScheduleTask {
    @Autowired
    ICronJobService cronJobService;

    @Value("${patrol.task:0}")
    private String patrolTaskFlag;

    @Value("${patrol.cleanDay:120}")
    private Integer cleanDay;


    @Value("${api.imgUrl.folder}")
    private String imgUrl;

    @Value("${file.img.retentionDays:9999}")
    private Integer retentionDays;

    @Value("${api.diagnosis.folder}")
    private String diagnosisFolder;

    @Value("${request.diagnosis.url}")
    private String diagnosisUrl;


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

    @Resource
    private AssetDiagnosisFlagInfoHisDao assetDiagnosisFlagInfoHisDao;

    @Resource
    private AssetDiagnosisFlagInfoExDao assetDiagnosisFlagInfoExDao;


    @Autowired
    private IConstantItemService constantItemService;


    //3.添加定时任务 0 0 1 * * ?
    @Scheduled(cron = "0 10 0 * * ?")
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
    @Scheduled(cron = "0/2 * * * * ?")
    private void pullDiagnosisMessage() {
        if ("1".equals(patrolTaskFlag)){
            String data = redisUtil.leftPop(ASSET_DIAGNOSIS_QUEUE);
            if (StringUtils.isEmpty(data)){
                return;
            }
            dealDiagnosisData(data);
        }
    }

    /**
    * @Author beixing
    * @Description  处理诊断的数据
    * @Date  2021/5/14
    * @Param
    * @return
    **/
    private void dealDiagnosisData(String data){
        JSONObject jsonObject = JSONObject.parseObject(data).getJSONObject("data");
        JSONObject resultData = jsonObject.getJSONObject("result");
        JSONObject faultTypeJson = resultData.getJSONObject("faultTypeResult");
        String deviceId = jsonObject.getString("deviceId");
        if(StringUtils.isEmpty(deviceId)){
            return;
        }
        log.info("拉取视频诊断结果:{} ",jsonObject);
        AssetDiagnosisInfo assetDiagnosisInfo = new AssetDiagnosisInfo();
        assetDiagnosisInfo.setAssetCode(deviceId);
        assetDiagnosisInfo.setTraceId(jsonObject.getString("traceId"));
        assetDiagnosisInfo.setDiagnosisResult(resultData.getInteger("diagnosisResult"));
        assetDiagnosisInfo.setIpAddress(jsonObject.getString("ipAddress"));
        String fileUrl = resultData.getString("fileUrl");
        fileUrl = fileUrl.replace("\\","/");
//        String url = diagnosisUrl + fileUrl;
//        String desUrl = diagnosisFolder+fileUrl;
//        String fileName = FileHelper.getFilename(url);
//        String filePath = FileHelper.getFilePath(desUrl,fileName);
//        FileHelper.createMultilayerFile(filePath);
//        UrlUtils.readImg(url, desUrl);

        assetDiagnosisInfo.setImgUrl(fileUrl);
        assetDiagnosisInfo.setDiagnosisTime(resultData.getString("diagnosisTime"));
        List<AssetDiagnosisInfoEx> assetDiagnosisInfoExes = new ArrayList<>();
//        List<AssetDiagnosisFlagInfoEx> assetDiagnosisFlagInfoExes = new ArrayList<>();
        StringBuffer sb = new StringBuffer();
        StringBuffer sbName = new StringBuffer();
        Map<Object, String> stringMap =  constantItemService.findItemsMapByDict("faultType");
        for (Map.Entry<String, Object> e : faultTypeJson.entrySet()) {
            AssetDiagnosisInfoEx assetDiagnosisInfoEx = new AssetDiagnosisInfoEx();
            if ("2".equals(e.getValue().toString())) {
                sb.append(e.getKey()).append(",");
                sbName.append(stringMap.get(Integer.valueOf(e.getKey()))).append(",");
            }
            assetDiagnosisInfoEx.setFaultType(Integer.valueOf(e.getKey()));
            assetDiagnosisInfoEx.setFaultTypeResult(Integer.valueOf(e.getValue().toString()));
            assetDiagnosisInfoEx.setAssetCode(deviceId);
            assetDiagnosisInfoEx.setTraceId(jsonObject.getString("traceId"));
            assetDiagnosisInfoEx.setDiagnosisResult(resultData.getInteger("diagnosisResult"));
            assetDiagnosisInfoEx.setImgUrl(fileUrl);
            assetDiagnosisInfoEx.setDiagnosisTime(resultData.getString("diagnosisTime"));
            assetDiagnosisInfoExes.add(assetDiagnosisInfoEx);

//            AssetDiagnosisFlagInfoEx assetDiagnosisFlagInfoEx = new AssetDiagnosisFlagInfoEx();
//            BeanUtils.copyProperties(assetDiagnosisInfoEx,assetDiagnosisFlagInfoEx);
//            assetDiagnosisFlagInfoExes.add(assetDiagnosisFlagInfoEx);
        }
        if(sb.length()>1){
            sb.delete(sb.length()-1,sb.length());
        }
        if(sbName.length()>1){
            sbName.delete(sbName.length()-1,sbName.length());
        }
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

//        AssetDiagnosisFlagInfoHis assetDiagnosisFlagInfoHis = new AssetDiagnosisFlagInfoHis();
//        BeanUtils.copyProperties(assetDiagnosisInfo,assetDiagnosisFlagInfoHis);
//        assetDiagnosisFlagInfoHisDao.insert(assetDiagnosisFlagInfoHis);
//        assetDiagnosisFlagInfoExDao.insertBatch(assetDiagnosisFlagInfoExes);
    }

    //3.添加定时任务 0 0 1 * * ?
    @Scheduled(cron = "0 0 21 * * ?")
    //每天20点清理数据
    private void cleanDB() {
        if ("1".equals(patrolTaskFlag)){
            log.info("开始清理数据: " + LocalDateTime.now());
            //清理120 天前的数据   2021-12-16 北授要求修改
            //武汉交管环境，目前巡检数据仅保留2个月，超过两个月都被删除掉了  修改成： 武汉交管环境，目前巡检数据仅保留4个月，超过4个月备份到另外一个表
            patrolTaskService.cleanPatrolResult(cleanDay);
            log.info("清理数据结束: " + LocalDateTime.now());
        }
    }


}
