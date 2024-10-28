package com.zans.config;

import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.zans.dao.FortUserFileExDao;
import com.zans.model.FortPlayBack;
import com.zans.model.FortReserve;
import com.zans.model.FortUserFileEx;
import com.zans.model.SysUser;
import com.zans.schedule.MyRunnable;
import com.zans.service.IFortPlayBackService;
import com.zans.service.IFortReserveService;
import com.zans.service.IFortServerConfigService;
import com.zans.service.ISysUserService;
import com.zans.utils.ApiResult;
import com.zans.utils.FTPUtil;
import com.zans.utils.FileHelper;
import com.zans.utils.SshClient;
import com.zans.vo.FortServerConfigVO;
import com.zans.vo.NetDiskFile;
import com.zans.vo.WebSocketSessionVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.guacamole.GuacamoleException;
import org.apache.guacamole.net.GuacamoleSocket;
import org.apache.guacamole.net.GuacamoleTunnel;
import org.apache.guacamole.net.InetGuacamoleSocket;
import org.apache.guacamole.net.SimpleGuacamoleTunnel;
import org.apache.guacamole.protocol.ConfiguredGuacamoleSocket;
import org.apache.guacamole.protocol.GuacamoleClientInformation;
import org.apache.guacamole.protocol.GuacamoleConfiguration;
import org.apache.guacamole.websocket.GuacamoleWebSocketTunnelEndpoint;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.scheduling.Trigger;
import org.springframework.scheduling.TriggerContext;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.websocket.EndpointConfig;
import javax.websocket.RemoteEndpoint;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.net.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.ScheduledFuture;
import java.util.stream.Collectors;

import static com.zans.constant.SystemConstant.*;
import static com.zans.utils.FTPUtil.readFileByFolder;

/**
 * @author beixing
 * @Title: WebSocketTunnel
 * @Description:
 * @Version:1.0.0
 * @Since:jdk1.8
 * @Date 2021/6/24
 **/
@ServerEndpoint(value = "/webSocket", subprotocols = "guacamole")
@EnableScheduling
@Component
@Slf4j
public class WebSocketTunnel extends GuacamoleWebSocketTunnelEndpoint {

    private ApplicationConfig applicationConfig =  (ApplicationConfig) SpringBeanFactory.getBean("applicationConfig");

    private IFortServerConfigService fortServerConfigService = (IFortServerConfigService) SpringBeanFactory.getBean("fortServerConfigService");

    private IFortPlayBackService fortPlayBackService = (IFortPlayBackService) SpringBeanFactory.getBean("fortPlayBackService");

    private ISysUserService sysUserService = (ISysUserService) SpringBeanFactory.getBean("sysUserService");

    private IFortReserveService fortReserveService = (IFortReserveService) SpringBeanFactory.getBean("fortReserveService");

    private RedisUtil redisUtil = (RedisUtil) SpringBeanFactory.getBean("redisUtil");

    private ThreadPoolTaskScheduler threadPoolTaskScheduler = (ThreadPoolTaskScheduler) SpringBeanFactory.getBean("taskScheduler");

    private FortUserFileExDao fortUserFileExDao = (FortUserFileExDao) SpringBeanFactory.getBean("fortUserFileExDao");

    private static SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public static ScheduledFuture<?> future;//用户文件监听器

    public static Map<String, List<NetDiskFile>> fileMap = new HashMap<>();

    /**
     * Returns a new tunnel for the given session. How this tunnel is created
     * or retrieved is implementation-dependent.
     *
     * @param session        The session associated with the active WebSocket
     *                       connection.
     * @param endpointConfig information associated with the instance of
     *                       the endpoint created for handling this single connection.
     * @return A connected tunnel, or null if no such tunnel exists.
     * @throws GuacamoleException If an error occurs while retrieving the
     *                            tunnel, or if access to the tunnel is denied.
     */
    @Override
    protected GuacamoleTunnel createTunnel(Session session, EndpointConfig endpointConfig) throws GuacamoleException {
        try {
            log.info("webSocket req:{}", session.getRequestParameterMap());
            String sourceIp = getRemoteAddress(session).toString();
            String hostname = applicationConfig.getHostname(); //guacamole server地址
            int port = applicationConfig.getPort(); //guacamole server端口
            String ip = session.getRequestParameterMap().get("ip").get(0);
            Integer height = Integer.valueOf(session.getRequestParameterMap().get("height").get(0));
            Integer width = Integer.valueOf(session.getRequestParameterMap().get("width").get(0));
            Integer id = Integer.valueOf(session.getRequestParameterMap().get("id").get(0));
            log.info("height：{},width：{}", height, width);


            FortServerConfigVO fortServerConfigVO = fortServerConfigService.queryByIp(ip);
            if (fortServerConfigVO == null) {
                log.error("该ip:{}未配置！", ip);
                throw new GuacamoleException("not found ip:" + ip);
            }
            GuacamoleConfiguration configuration = new GuacamoleConfiguration();
            configuration.setProtocol("rdp"); // 远程连接协议
            configuration.setParameter("hostname", fortServerConfigVO.getServerIp());
            configuration.setParameter("port", fortServerConfigVO.getServerPort());
            configuration.setParameter("username", fortServerConfigVO.getServerAccount());
            configuration.setParameter("password", fortServerConfigVO.getServerPwd());
            configuration.setParameter("ignore-cert", "true");
            SysUser sysUser = sysUserService.selectById(id);
            String videoUrl = applicationConfig.getVideoUrl();
            String outputFilePath = videoUrl + File.separator + sysUser.getUserName() + getPathName(ip);//相对路径
            FileHelper.createMultilayerFile(outputFilePath);
            String fileName = ip + "_" + getNowTime() + ".guac";//文件名
            log.info("文件名:{}", fileName);
            //添加会话录制--录屏
//        configuration.setParameter("typescript-path", "/");
//        configuration.setParameter("create-typescript-path", "true");
//        configuration.setParameter("typescript-name", "" );


            //0不录屏 1录屏
            if (applicationConfig.getTranscribe() == 1) {
                configuration.setParameter("recording-path", outputFilePath);
                configuration.setParameter("create-recording-path", "true");
                configuration.setParameter("recording-name", fileName);
                //插入回放记录 后续做数据调整  如果不生成录屏 则不生成回放记录仪
                String fileSize = "";
                String videoTime = "00:00:00";
                if (applicationConfig.getDecode() == 0) {
                    fileSize = "未进行解码操作";
                }
                fortPlayBackService.insertPlayBackInfo(new FortPlayBack(null, sysUser.getUserName(), ip, sourceIp.substring(1, sourceIp.indexOf(":")), fileName, outputFilePath + File.separator + fileName, outputFilePath + File.separator + fileName + ".m4v", TO_BE_DECODE, fileSize, videoTime, simpleDateFormat.format(new Date()), simpleDateFormat.format(new Date()), simpleDateFormat.format(new Date())));
            }
            GuacamoleClientInformation information = new GuacamoleClientInformation();
            information.setOptimalScreenHeight(height);
            information.setOptimalScreenWidth(width);
            GuacamoleSocket socket = new ConfiguredGuacamoleSocket(
                    new InetGuacamoleSocket(hostname, port),
                    configuration,
                    information
            );
            WebSocketSessionVO socketSessionVO = new WebSocketSessionVO();
            socketSessionVO.setUserName(sysUser.getUserName());
            socketSessionVO.setSession(session);
            long expireTime = System.currentTimeMillis() + applicationConfig.getExpireTime() * 1000;
            FortReserve fortReserveByIp = fortReserveService.findFortReserveByIp(ip);
            long remainingTime = 0;
            if (fortReserveByIp != null) {
                remainingTime = calRemainingTime(fortReserveByIp.getEndTime());
                expireTime = System.currentTimeMillis() + remainingTime;
            }
            log.info("远程时长为:{}ms", fortReserveByIp == null ? applicationConfig.getExpireTime() : remainingTime);
            socketSessionVO.setExpireTime(expireTime);
            webSocketSession.put(ip, socketSessionVO);
            log.warn("开始操作ip:{}的远程桌面！", ip);
            long finalRemainingTime = remainingTime;
//            boolean set = redisUtil.set(ip + "-" + sysUser.getUserName(), "", fortReserveByIp == null ? applicationConfig.getExpireTime() : finalRemainingTime);
            boolean set = redisUtil.set(ip, "", fortReserveByIp == null ? applicationConfig.getExpireTime() : finalRemainingTime);
            if (set) {
                GuacamoleTunnel tunnel = new SimpleGuacamoleTunnel(socket);
                log.info("开始远程");
                ipInUserMap.put(ip, true);
                /**
                 * 启动监听 文件定时器 用户退出时关闭定时器
                 */
                threadPoolTaskScheduler.submit(() -> {
                    beginTask(sysUser.getUserName());
                });
                return tunnel;
            }
        } catch (Exception e) {
            log.error("Allerror:{}", e);
        }
        return null;
    }

    private String resolveRemoteIp(SocketAddress socketAddress) {
        if (socketAddress instanceof InetSocketAddress) {
            InetAddress inetAddress = ((InetSocketAddress) socketAddress).getAddress();
            if (inetAddress instanceof Inet4Address) {
                log.info("IPv4:{}", inetAddress);
                return inetAddress.getHostAddress();
            } else if (inetAddress instanceof Inet6Address) {
                log.info("IPv6:{}", inetAddress);
            } else {
                log.error("Not an IP address.");
                return null;
            }
        } else {
            log.error("Not an internet protocol socket.");
        }
        return null;
    }

    private static long calRemainingTime(String endTime) {
        try {
            Date parse = simpleDateFormat.parse(endTime);
            long remaining = parse.getTime() - new Date().getTime();
            return remaining;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public static InetSocketAddress getRemoteAddress(Session session) {
        if (session == null) {
            return null;
        }
        RemoteEndpoint.Async async = session.getAsyncRemote();

        //在Tomcat 8.0.x版本有效
//		InetSocketAddress addr = (InetSocketAddress) getFieldInstance(async,"base#sos#socketWrapper#socket#sc#remoteAddress");
        //在Tomcat 8.5以上版本有效
        InetSocketAddress addr = (InetSocketAddress) getFieldInstance(async, "base#socketWrapper#socket#sc#remoteAddress");
        return addr;
    }

    private static Object getFieldInstance(Object obj, String fieldPath) {
        String fields[] = fieldPath.split("#");
        for (String field : fields) {
            obj = getField(obj, obj.getClass(), field);
            if (obj == null) {
                return null;
            }
        }

        return obj;
    }

    private static Object getField(Object obj, Class<?> clazz, String fieldName) {
        for (; clazz != Object.class; clazz = clazz.getSuperclass()) {
            try {
                Field field;
                field = clazz.getDeclaredField(fieldName);
                field.setAccessible(true);
                return field.get(obj);
            } catch (Exception e) {
            }
        }

        return null;
    }


    private String getPathName(String ip) {
        LocalDateTime localDateTime = LocalDateTime.now();
        int yyyy = localDateTime.getYear();
        int mm = localDateTime.getMonthValue();
        int dd = localDateTime.getDayOfMonth();
        int hh = localDateTime.getHour();
        String path = File.separator + yyyy + File.separator + mm + File.separator + dd + File.separator + hh + File.separator + ip;
        return path;
    }


    //判断水印服务是否能启动 再
    public static boolean getIfLogin(String ip, SysUser sysUser) {
        HttpPost httpPost = new HttpPost("http://" + ip + ":8081/edr/client/login");
        MultipartEntityBuilder builder = MultipartEntityBuilder.create();
        if (!StringUtils.isEmpty(sysUser.getUserName()) && !StringUtils.isEmpty(sysUser.getPassword())) {
            builder.addTextBody("phone", sysUser.getUserName());
            builder.addTextBody("password", sysUser.getPassword());
        } else {
            builder.addTextBody("phone", "admin");
            builder.addTextBody("password", "123456");
        }
        log.info("username:{}", StringUtils.isEmpty(sysUser.getUserName()) ? LGWY : sysUser.getUserName());
        HttpEntity multipart = builder.build();
        try {
            String responseJson = WebSocketTunnel.postSet(multipart, httpPost);
            ApiResult apiResult = JSONObject.parseObject(new String(responseJson.getBytes(), DEFAULT_CHARSET), ApiResult.class);
            log.warn("接口返回结果:{}", apiResult);
            if (apiResult.getCode() == 0 || (apiResult.getCode() == 5000 && "".equals(apiResult.getData()))) {
                return true;
            }
        } catch (IOException e) {
            log.error("连接失败!error:{}", e);
            return false;
        } catch (JSONException e) {
            log.error("返回错误!error:{}", e);
            return false;
        }
        return false;
    }

    public static String postSet(HttpEntity params, HttpPost httpPost) throws IOException {
        httpPost.setEntity(params);
        CloseableHttpClient client = HttpClients.createDefault();
        CloseableHttpResponse response = client.execute(httpPost);
        HttpEntity entity = response.getEntity();
        if (entity != null) {
            String result = EntityUtils.toString(entity, DEFAULT_CHARSET);
            return result;
        }
        return "";
    }

    private String getNowTime() {
        return LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
    }

    private void beginTask(String username) {
        FTPUtil test = new FTPUtil();
        FTPClient ftp = test.getFTPClient(SFTP_HOST, SFTP_PORT, SFTP_USERNAME, SFTP_PASSWORD);
        List<NetDiskFile> baseNetDiskFiles = readFileByFolder(ftp, applicationConfig.getVideoPath() + username);//查询的用户下的最新文件
        log.info(username + "->初始文件列表为:{}", baseNetDiskFiles);
        fileMap.put(username, baseNetDiskFiles);//定时任务开启前 初始化用户对应的文件列表
        future = threadPoolTaskScheduler.schedule(new MyRunnable(), new Trigger() {
            @Override
            public Date nextExecutionTime(TriggerContext triggerContext) {//此处可以改为配置文件获取定时任务执行频率
                //TODO 监听文件变动
                List<NetDiskFile> netDiskFiles = readFileByFolder(ftp, applicationConfig.getVideoPath() + username);//查询的用户下的最新文件
                List<String> newFileNames = netDiskFiles.stream().map(NetDiskFile::getFileName).collect(Collectors.toList());
//                log.info("newFile:{}",newFileNames);
                List<NetDiskFile> oldFile = fileMap.get(username);
                List<String> oldFileNames = oldFile.stream().map(NetDiskFile::getFileName).collect(Collectors.toList());
//                log.info("oldFile:{}",oldFileNames);
                //移除的文件
                List<String> removeFile = oldFileNames.stream().filter(t -> !newFileNames.contains(t)).collect(Collectors.toList());
                delFile(removeFile, username);
//                log.info("移除文件:{}",removeFile);
                //新增的文件
                List<String> newlyFile = newFileNames.stream().filter(t -> !oldFileNames.contains(t)).collect(Collectors.toList());
                if (checkIfUpdate(fileMap.get(username), netDiskFiles) || newlyFile.size() != 0) {
                    backup(username);
                }
//                log.info("新增文件:{}",newlyFile);
                List<FortUserFileEx> files = new ArrayList<>();
                for (NetDiskFile netDiskFile : oldFile) {
                    for (String s : removeFile) {
                        if (s.equals(netDiskFile.getFileName())) {
                            files.add(new FortUserFileEx(null, UUID.randomUUID().toString(), SFTP_HOST, username, applicationConfig.getVideoPath() + username, netDiskFile.getFileName(), netDiskFile.getFileName().substring(netDiskFile.getFileName().lastIndexOf(".")), netDiskFile.getFileSize(), 1, null, null));
                        }
                    }
                }
                for (NetDiskFile netDiskFile : netDiskFiles) {
                    for (String s : newlyFile) {
                        if (s.equals(netDiskFile.getFileName())) {
                            files.add(new FortUserFileEx(null, UUID.randomUUID().toString(), SFTP_HOST, username, applicationConfig.getVideoPath() + username, netDiskFile.getFileName(), netDiskFile.getFileName().substring(netDiskFile.getFileName().lastIndexOf(".")), netDiskFile.getFileSize(), 0, simpleDateFormat.format(new Date()), simpleDateFormat.format(new Date())));
                        }
                    }
                }
                if (files.size() != 0) {
                    log.info("要处理的files为:{}", files);
                }
                if (0 != files.size()) {
                    fortUserFileExDao.insertBatch(files);
                }
                fileMap.put(username, netDiskFiles);//更新用户对应文件列表
                return new CronTrigger("0/1 * * * * ?").nextExecutionTime(triggerContext);
            }
        });
        log.info("DynamicTask.startCron1()");
    }

    public boolean checkIfUpdate(List<NetDiskFile> baseNetDiskFiles, List<NetDiskFile> newList) {
        for (NetDiskFile baseNetDiskFile : baseNetDiskFiles) {
            for (NetDiskFile netDiskFile : newList) {
                //如果有修改时间不一样的 则文件被修改过
                if (baseNetDiskFile.getFileName().equals(netDiskFile.getFileName())
                        && !baseNetDiskFile.getUpdateTime().equals(netDiskFile.getUpdateTime())) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 备份文件
     *
     * @param username
     */
    public void backup(String username) {
        String[] list = new String[3];
        list[0] = "\\cp -rf /home/userfile/" + username + " /home/userfile/beifen/";
        list[1] = "convmv -f gbk -t utf-8 -r --notest /home/userfile/beifen/" + username;
        list[2] = "exit";
        String root = SshClient.execShell("192.168.10.90", "root", "lgwy@2020", 22, list);
        System.out.println(root);
    }

    public void delFile(List<String> fileName, String username) {
        log.info("移除文件:" + fileName);
        String basePath = applicationConfig.getVideoPath() + "beifen/";
        String[] list = new String[2];
        StringBuilder sb = new StringBuilder();
        sb.append("rm -f");
        for (String s : fileName) {
            sb.append(" " + basePath + username + "/" + s);
        }
        System.out.println(sb);
        list[0] = sb.toString();
        list[1] = "exit";
        SshClient.execShell("192.168.10.90", "root", "lgwy@2020", 22, list);
    }

}
