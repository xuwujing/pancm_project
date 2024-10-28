package com.zans.config;

import com.zans.util.SmartSshUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.io.File;
import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;


/**
 * @author beixing
 * @Title: FailScheduleTask
 * @Description: 查询失败的数据，丢进失败队列运行
 * @Version:1.0.0
 * @Since:jdk1.8
 * @Date 2021/12/1
 **/
@Configuration
@EnableScheduling
@Slf4j
public class ScheduleTask {


    @Value("${sftp.hostName}")
    private String hostName;
    @Value("${sftp.username}")
    private String username;
    @Value("${sftp.password}")
    private String password;
    @Value("${sftp.srcFilePath}")
    private String srcFilePath;
    @Value("${sftp.targetFilePath}")
    private String targetFilePath;

    @Value("${sftp.execute-time:230000}")
    private String executeTime;

    @Value("${mmsImgDay:0}")
    private Integer mmsImgDay;

    /**
     * @return
     * @Author beixing
     * @Description 每天一点钟执行一次
     * @Date 2021/12/7
     * @Param
     **/
//    @Scheduled(cron = "0 10 23 1/1 * ? ")
    private void configureTasks() {
        log.info("开始进行文件下载!下载路径:{},保存路径:{}", srcFilePath, targetFilePath);
        SmartSshUtils.downLoadFileBySsh(hostName, username, password, srcFilePath, targetFilePath);
        log.info("文件下载成功!下载路径:{},保存路径:{}", srcFilePath, targetFilePath);
    }

    @Bean
    public void start() {
        Calendar calendar = Calendar.getInstance();
        int hour = Integer.parseInt(executeTime.substring(0,2));
        int m = Integer.parseInt(executeTime.substring(2,4));
        int s = Integer.parseInt(executeTime.substring(4,6));
        calendar.set(Calendar.HOUR_OF_DAY, hour); // 控制时
        calendar.set(Calendar.MINUTE, m);    // 控制分
        calendar.set(Calendar.SECOND, s);    // 控制秒

        Date time = calendar.getTime();

        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            public void run() {
                if(mmsImgDay == 1){
                    mmsImg();
                }else {
                    log.info("开始进行文件下载!下载路径:{},保存路径:{}", srcFilePath, targetFilePath);
                    SmartSshUtils.downLoadFileBySsh(hostName, username, password, srcFilePath, targetFilePath);
                    log.info("文件下载成功!下载路径:{},保存路径:{}", srcFilePath, targetFilePath);

                }
            }
        }, time, 1000 * 60 * 60 * 24);// 这里设定将延时每天固定执行

    }


    /**
    * @Author beixing
    * @Description  运维系统，按天导出
    * @Date  2021/12/7
    * @Param
    * @return
    **/
    private void mmsImg(){
        String rawSrcFilePath = srcFilePath +getLinuxPathName(raw);
        String rawTargetFilePath = targetFilePath +getPathName(raw);
        String thumbnailSrcFilePath = srcFilePath +getLinuxPathName(thumbnail);
        String thumbnailTargetFilePath = targetFilePath +getPathName(thumbnail);
        log.info("运维系统原始图片开始进行文件下载!下载路径:{},保存路径:{}", rawSrcFilePath, rawTargetFilePath);
        SmartSshUtils.downLoadFileBySsh(hostName, username, password, rawSrcFilePath, rawTargetFilePath);
        log.info("运维系统原始图片文件下载成功!下载路径:{},保存路径:{}", rawSrcFilePath, rawTargetFilePath);
        log.info("运维系统缩略图片开始进行文件下载!下载路径:{},保存路径:{}", thumbnailSrcFilePath, thumbnailTargetFilePath);
        SmartSshUtils.downLoadFileBySsh(hostName, username, password, thumbnailSrcFilePath, thumbnailTargetFilePath);
        log.info("运维系统缩图片文件下载成功!下载路径:{},保存路径:{}", thumbnailSrcFilePath, thumbnailTargetFilePath);
    }


    private String getLinuxPathName(String prefix){
        LocalDateTime localDateTime= LocalDateTime.now().minusDays(1);
        int yyyy = localDateTime.getYear();
        int mm = localDateTime.getMonthValue();
        int dd = localDateTime.getDayOfMonth();
        return prefix + "/"+ yyyy+"/"+mm+"/"+dd;
    }

    private String getPathName(String prefix){
        LocalDateTime localDateTime= LocalDateTime.now().minusDays(1);
        int yyyy = localDateTime.getYear();
        int mm = localDateTime.getMonthValue();
        int dd = localDateTime.getDayOfMonth();
        return prefix + File.separator+ yyyy+File.separator+mm+File.separator+dd;
    }


    private final String raw = "raw";

    private final String thumbnail = "thumbnail";



}
