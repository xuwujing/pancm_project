package com.zans.config;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.zans.dao.FortAgentServerMapper;
import com.zans.dao.FortPlayBackMapper;
import com.zans.dao.FortUserFileDao;
import com.zans.dao.SysUserMapper;
import com.zans.model.FortAgentServer;
import com.zans.model.FortPlayBack;
import com.zans.model.FortUserFile;
import com.zans.model.SysUser;
import com.zans.utils.ApiResult;
import com.zans.utils.HttpClientUtil;
import com.zans.vo.LoginVO;
import com.zans.vo.WebSocketSessionVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.websocket.CloseReason;
import javax.websocket.Session;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.zans.config.GlobalConstants.AGENT_SERVER;
import static com.zans.config.GlobalConstants.FILE_URL_LOGOUT;
import static com.zans.config.WebSocketTunnel.future;
import static com.zans.constant.SystemConstant.*;


/**
 * @author beixing
 * @Title: TaskRunner
 * @Description:
 * @Version:1.0.0
 * @Since:jdk1.8
 * @Date 2021/4/20
 **/
@Component
@Order(value = 3)
@Slf4j
public class TaskRunner implements CommandLineRunner {

    private final ExecutorService executor = Executors.newCachedThreadPool();

    @Autowired
    private ApplicationConfig applicationConfig;

    @Resource
    private FortPlayBackMapper fortPlayBackMapper;

    @Resource
    private FortUserFileDao fortUserFileDao;

    @Resource
    private FortAgentServerMapper fortAgentServerMapper;

    @Resource
    private SysUserMapper sysUserMapper;


    @Autowired
    RedisUtil redisUtil;

    public static SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @Override
    public void run(String... args) {
        playExpiredTask(1);
    }


    private void playExpiredTask(int period) {
        Runnable runnable = () -> {
            try {
                ConcurrentHashMap<String, WebSocketSessionVO> map = webSocketSession;
                if (!map.isEmpty()) {
                    for (String ip : map.keySet()) {
                        String ipAddr = ip;
                        long nowTime = System.currentTimeMillis();
                        long expireTime = map.get(ip).getExpireTime();

                        if (nowTime > expireTime) {
//                            if (stopAgent(ip)){
//                                log.info("关闭水印服务正常");
//                            }else {
//                                log.info("关闭水印服务异常");
//                            }
                            ipInUserMap.put(ip, false);
                            /**
                             * 取消 监听文件改动 定时任务
                             */
                            stopTask();
                            FortPlayBack fortPlayBack = new FortPlayBack();
                            fortPlayBack.setServerIp(ipAddr);
                            fortPlayBack.setStatus(TO_BE_DECODE);
                            FortPlayBack queryFortPlayBack = fortPlayBackMapper.selectNewByIp(fortPlayBack);
                            if (queryFortPlayBack == null) {
                                log.info("guac未生成");
                            }
                            SysUser user = sysUserMapper.findUserByName(map.get(ip).getUserName());
                            if (loginOutFileClient(ipAddr, new SysUser(map.get(ip).getUserName()))) {
                                log.info(ipAddr + "登出文件客户端成功");
                                optClose(map.get(ip).getSession());
                                webSocketSession.remove(ip);
                                //压缩命令  如果decode为1 则要将guac解码-m4v-mp4
                                if(applicationConfig == null){
                                    applicationConfig = (ApplicationConfig) SpringBeanFactory.getBean("applicationConfig");
                                }
                                if (applicationConfig.getDecode() == 1) {
                                    log.warn("设备ip:{} 播放已过期,进行停止 准备生成文件:{}", ip, queryFortPlayBack.getFileName());
                                } else {
                                    log.warn("设备ip:{} 播放已过期,本次操作未解码-m4v-mp4", ip);
                                    return;
                                }
                                queryFortPlayBack.setStatus(DECODING);//准备解码文件  更改状态为解码中
                                queryFortPlayBack.setUpdateTime(simpleDateFormat.format(new Date()));
                                fortPlayBackMapper.updateByPrimaryKeySelective(queryFortPlayBack);
                                //测试  改为调用91接口
                                Map<String, Object> params = new HashMap<>();
                                params.put("id", queryFortPlayBack.getId());
                                String s = HttpClientUtil.get(applicationConfig.getDecodeServerUrl(), params);
//                                getDecode(queryFortPlayBack.getPlayBackUrlGuac(), queryFortPlayBack.getFileName(), user);
                                log.info("开始解码");
                            }
                        }
                    }
                }
            } catch (Exception ex) {
                log.error("执行失败！", ex);
            }

        };
        ScheduledExecutorService service = Executors.newSingleThreadScheduledExecutor();
        service.scheduleAtFixedRate(runnable, 1, period, TimeUnit.SECONDS);
    }

    private void optClose(Session session) {
        try {
            // 判断当前连接是否还在线
            if (session.isOpen()) {
                try {
                    // 关闭连接
                    CloseReason closeReason = new CloseReason(CloseReason.CloseCodes.NORMAL_CLOSURE, "进行关闭！");
                    session.close(closeReason);
                } catch (IOException e) {
                    log.error("ip:{},关闭失败！", session.getRequestParameterMap(), e);
                }
            }
        } catch (NullPointerException nullPointerException) {
            log.info("session-Null,error", nullPointerException);
        }

    }

    public boolean stopAgent(String ip) {
        HttpPost httpPost = new HttpPost(String.format(STOP_AGENT, ip));
        MultipartEntityBuilder builder = MultipartEntityBuilder.create();
        com.zans.vo.Pair pair = AGENT_SERVER.get(ip);
        if (pair == null) {
            FortAgentServer fortAgentServer = fortAgentServerMapper.queryByIp(ip);
            AGENT_SERVER.put(fortAgentServer.getServerIp(), new com.zans.vo.Pair(fortAgentServer.getPath(), fortAgentServer.getName()));
        }
        String path = String.valueOf(AGENT_SERVER.get(ip).getKey());
        String name = String.valueOf(AGENT_SERVER.get(ip).getValue());
        builder.addTextBody("path", path);
        builder.addTextBody("name", name);
        log.info("ip:{},path:{},name:{}", ip, path, name);
        HttpEntity multipart = builder.build();
        try {
            String responseJson = WebSocketTunnel.postSet(multipart, httpPost);
            ApiResult apiResult = JSONObject.parseObject(new String(responseJson.getBytes(), DEFAULT_CHARSET), ApiResult.class);
            log.warn("接口返回结果:{}", apiResult);
            if (apiResult.getCode() == 0) {
                return true;
            }
        } catch (IOException e) {
            log.error("连接失败!error:{}", e);
            return false;
        } catch (JSONException e) {
            log.error("返回类型错误!error:{}", e);
            return false;
        }
        return false;
    }

    /**
     * @param ip
     * @param sysUser
     * @return
     * @Author beixing
     * @Description 登出文件客户端
     * @Date 2021/8/17
     */
    public boolean loginOutFileClient(String ip, SysUser sysUser) {
        FortUserFile fortUserFile = fortUserFileDao.queryByUser(sysUser.getUserName());
        if (fortUserFile == null) {
            log.warn("未配置该用户的映射目录！请联系管理员!用户:{}", sysUser.getUserName());
            return false;
        }
        LoginVO loginVO = new LoginVO();
        BeanUtils.copyProperties(fortUserFile, loginVO);
        loginVO.setUser(fortUserFile.getUserName());
        String url = String.format(FILE_URL_LOGOUT, ip);
        try {
            String result = HttpClientUtil.post(url, loginVO.toString());
            ApiResult apiResult = JSON.parseObject(result, ApiResult.class);
            if (apiResult.getCode() != 0) {
                log.warn("请求接口失败！接口返回参数:{}", apiResult);
                return false;
            }
        } catch (Exception e) {
            log.error("连接失败!请求url:{}，请求参数：{}error:{}", url, loginVO, e);
            return false;
        }
        return true;
    }

    public void getDecode(String outputFilePathGuac, String fileName, SysUser user) {
        executor.submit(new Runnable() {
            @Override
            public void run() {
                InputStream is3 = null;
                InputStreamReader isr3 = null;
                BufferedReader br3 = null;

                InputStream is4 = null;
                InputStreamReader isr4 = null;
                BufferedReader br4 = null;
                try {
                    log.info("清晰度:{}", applicationConfig.getResolution().get(0));
                    ProcessBuilder builder1 = new ProcessBuilder("/bin/bash", "-c", "guacenc -s " + applicationConfig.getResolution().get(0) + " -r 20000000 -f " + outputFilePathGuac);
                    log.info("guacenc -s " + applicationConfig.getResolution().get(0) + " -r 20000000 -f " + outputFilePathGuac);
                    //merge the error output with the standard output
                    builder1.redirectErrorStream(true);
                    long start1 = System.currentTimeMillis();
                    Process p1 = builder1.start();
                    log.info("task1 run");
                    clearInputStream(p1);
                    int i1 = p1.waitFor();
                    p1.destroy();
                    if (i1 != 0) {
                        log.info("guac->m4v out:{}", i1);
                    }
                    log.info("task1 destroy " + (i1 == 0 ? "success" : "false"));
                    long end1 = System.currentTimeMillis();
                    log.warn(outputFilePathGuac + "解码->m4v耗时:{}ms", end1 - start1);
                    // 后续加到配置文件
                    if (i1 == 0) {
                        String waterMark = "操作回放 " + user.getUserName() + " " + user.getMobile() + " " + " %{localtime\\:%T}";
                        log.info("watermark:{}", waterMark);
                        String info = "-vf \"drawtext=fontfile=simhei.ttf: text='%s':x=900:y=500:fontsize=24:fontcolor=white@0.2:shadowy=2\"";
                        log.info("info:{}", info);
                        String cmd = "ffmpeg -i " + outputFilePathGuac + ".m4v " + String.format(info, waterMark) + " -vcodec h264 -r 29.97 " + outputFilePathGuac + ".mp4 -y";
                        //ffmpeg -i 2.m4v -vf "drawtext=fontfile=simhei.ttf: text='天下武功唯快不破':x=20:y=10:fontsize=24:fontcolor=white@0.2:shadowy=2" -vcodec h264 22.mp4  -y
                        ProcessBuilder builder2 = new ProcessBuilder("/bin/bash", "-c", cmd);
//                    ProcessBuilder builder2 = new ProcessBuilder("/bin/bash", "-c", "ffmpeg -i " + outputFilePathGuac + ".m4v -vcodec h264 -r 29.97 " + outputFilePathGuac + ".mp4");
                        log.info("cmd:{}", cmd);
                        //merge the error output with the standard output
                        builder2.redirectErrorStream(true);
                        long start2 = System.currentTimeMillis();
                        Process p2 = builder2.start();
                        log.info("task2 run");
                        //处理InputStream的线程
                        clearInputStream(p2);
                        int i2 = p2.waitFor();
                        p2.destroy();
                        if (i2 != 0) {
                            log.info("m4v->mp4 out:{}", i2);
                        }
                        long end2 = System.currentTimeMillis();
                        log.info("task2 destroy " + (i2 == 0 ? "success" : "false"));
                        log.warn(outputFilePathGuac + ".mp4解码->mp4耗时:{}ms", end2 - start2);
                        if (i2 == 0) {
                            ProcessBuilder builder3 = new ProcessBuilder("/bin/bash", "-c", "ls -sh " + outputFilePathGuac + ".mp4");
                            builder3.redirectErrorStream(true);
                            long start3 = System.currentTimeMillis();
                            Process p3 = builder3.start();
                            p3.waitFor();
                            long end3 = System.currentTimeMillis();
                            log.warn(outputFilePathGuac + ".mp4查询文件大小耗时:{}ms", end3 - start3);
                            is3 = p3.getInputStream();
                            isr3 = new InputStreamReader(is3);
                            br3 = new BufferedReader(isr3);
                            StringBuilder sb = new StringBuilder();
                            while (true) {
                                String line = br3.readLine();
                                if (line == null) {
                                    break;
                                }
                                if (line.trim().length() > 0) {
                                    sb.append(line);
                                    sb.append("\n");
                                }
                            }
                            log.warn(sb.toString());
                            p3.destroy();

                            //begin
                            ProcessBuilder builder4 = new ProcessBuilder("/bin/bash", "-c", "ffmpeg -i " + outputFilePathGuac + ".mp4");
                            builder4.redirectErrorStream(true);
                            long start4 = System.currentTimeMillis();
                            Process p4 = builder4.start();
                            p4.waitFor();
                            long end4 = System.currentTimeMillis();
                            log.warn(outputFilePathGuac + ".mp4查询文件时长耗时:{}ms", end4 - start4);
                            is4 = p4.getInputStream();
                            isr4 = new InputStreamReader(is4);
                            br4 = new BufferedReader(isr4);
                            StringBuilder sb4 = new StringBuilder();
                            while (true) {
                                String line = br4.readLine();
                                if (line == null) {
                                    break;
                                }
                                if (line.trim().length() > 0 && line.trim().startsWith("Duration")) {
                                    String trim = line.trim();
                                    sb4.append(trim.substring(trim.indexOf(":") + 2, trim.indexOf(".")));
                                    break;
                                }
                            }
                            log.warn(sb4.toString());
                            p4.destroy();
                            //end
                            updateStatus(fileName, sb, sb4);
                        }
                    }
                } catch (Exception e) {
                    log.error("解码失败!文件:{},错误:{}", fileName, e.toString());
                    e.printStackTrace();
                } finally {
                    try {
                        if (br4 != null) {
                            br4.close();
                        }
                        if (isr4 != null) {
                            isr4.close();
                        }
                        if (is4 != null) {
                            is4.close();
                        }
                        if (br3 != null) {
                            br3.close();
                        }
                        if (isr3 != null) {
                            isr3.close();
                        }
                        if (is3 != null) {
                            is3.close();
                        }
                    } catch (Exception e) {
                        log.error("关闭流错误!error:{}", e);
                    }
                }
            }
        });
    }

    public void updateStatus(String fileName, StringBuilder sb, StringBuilder sb4) {

        FortPlayBack fortPlayBack = new FortPlayBack();
        fortPlayBack.setFileName(fileName);
        FortPlayBack queryFortPlayBack = fortPlayBackMapper.selectNewByIp(fortPlayBack);
        if ("ls: cannot access".equals(sb.toString().substring(0, sb.toString().indexOf("/")).trim())) {
            queryFortPlayBack.setFileSize("文件不存在");
            queryFortPlayBack.setVideoTime("00:00:00");
            queryFortPlayBack.setStatus(TO_BE_DECODE);//文件生成失败! 返回初始状态
        } else {
            queryFortPlayBack.setFileSize(sb.toString().substring(0, sb.toString().indexOf("/")).trim());
            queryFortPlayBack.setVideoTime(sb4.toString());//7-15 取消显示毫秒
            queryFortPlayBack.setStatus(DECODING_SUCCESS);
        }
        try {
            //新增视频结束时间
            SimpleDateFormat simpleDateFormat_HH_mm_ss = new SimpleDateFormat("HH:mm:ss");
            Date startTimeSize = simpleDateFormat.parse(queryFortPlayBack.getCreateTime());
            Date videoTime = simpleDateFormat_HH_mm_ss.parse(queryFortPlayBack.getVideoTime());
            long endTimeSize = startTimeSize.getTime() + videoTime.getTime() + 8 * 60 * 60 * 1000;//1970-01-01 00:00:00 推进8h
            String endTime = simpleDateFormat.format(new Date(endTimeSize));
            queryFortPlayBack.setEndTime(endTime);
            queryFortPlayBack.setUpdateTime(simpleDateFormat.format(new Date()));
            fortPlayBackMapper.updateByPrimaryKeySelective(queryFortPlayBack);
            log.warn("文件:{} 已生成mp4文件", queryFortPlayBack.getFileName());
        } catch (ParseException parseException) {
            log.error("时间转换异常,error:{}", parseException);
        }
    }

    public void clearInputStream(Process p2) {
        new Thread(() -> {
            BufferedReader in = new BufferedReader(new InputStreamReader(p2.getInputStream()));
            String line = null;
            try {
                while ((line = in.readLine()) != null) {
//                    log.info("clearInputStream -- output: " + line);
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    in.close();
                } catch (IOException e) {
                    log.error("clearInputStream error:{}", e);
                }
            }
        }).start();
    }

    public static Pair<Integer, Integer> getCharacterPosition(String string) {
        Matcher slashMatcher = Pattern.compile("/").matcher(string);
        Pair<Integer, Integer> pair = null;
        int mIdx = 0;
        int left = 0;
        int right = 0;
        while (slashMatcher.find()) {
            mIdx++;
            if (mIdx == 4) {
                left = slashMatcher.start() + 1;
            }
            if (mIdx == 5) {
                right = slashMatcher.start();
                pair = Pair.of(left, right);
                break;
            }
        }
        return pair;
    }

    private void stopTask() {
        if (future != null) {
            future.cancel(true);
        }
        log.info("user-file DynamicTask.stopCron()");
    }
}




