package com.zans.service.impl;

import com.zans.config.SqlLiteConnection;
import com.zans.service.IBackUpService;
import com.zans.util.FileHelper;
import com.zans.util.SmartSshUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipArchiveOutputStream;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.io.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

import static com.zans.config.Constants.*;

/**
 * @author beixing
 * @Title: zans-bak
 * @Description: 文件备份
 * @Version:1.0.0
 * @Since:jdk1.8
 * @date 2021/12/28
 */
@Service
@Slf4j
public class BackUpServiceImpl implements IBackUpService {
    /**
     * @param map
     * @param i
     * @return
     * @Author beixing
     * @Description 数据库备份
     * @Date 2021/12/28
     * @Param
     */
    @Override
    public void dbBackup(Map<String, String> map, int i) {
        String executeTime = map.getOrDefault(EXECUTE_TIME + i, "23:30:00");
        String dbName = map.get(DB_NAME + i);
        Calendar calendar = Calendar.getInstance();
        int hour = Integer.parseInt(executeTime.substring(0, 2));
        int m = Integer.parseInt(executeTime.substring(3, 5));
        int s = Integer.parseInt(executeTime.substring(6, 8));
        calendar.set(Calendar.HOUR_OF_DAY, hour); // 控制时
        calendar.set(Calendar.MINUTE, m);    // 控制分
        calendar.set(Calendar.SECOND, s);    // 控制秒
        Date time = calendar.getTime();
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            public void run() {
                log.info("编号:{}，开始进行的数据库:{}备份!", i, dbName);
                try {
                    executeDb(map, i);
                    log.info("编号:{},数据库:{} 备份成功!", i, dbName);
                    save(map, i, "mysql");
                } catch (IOException e) {
                    log.error("编号:{},数据库:{} 备份失败!", i, dbName, e);
                }
            }
        }, time, 1000 * 60 * 60 * 24);// 这里设定将延时每天固定执行
        log.info("编号:{},数据库备份任务启动成功!", i);
        int isStartExecute = Integer.parseInt(map.getOrDefault(IS_START_EXECUTE + i, "0"));
        if (isStartExecute == 1) {
            log.info("编号:{},数据库:{},启动时进行备份!", i, dbName);
            try {
                executeDb(map, i);
                save(map, i, "mysql");
                log.info("编号:{},数据库:{} 备份成功!", i, dbName);
            } catch (IOException e) {
                log.error("编号:{},数据库:{} 备份失败!", i, dbName, e);
            }
        }
    }


    /**
     * @Author beixing
     * @Description  记录存储
     * @Date  2021/12/27
     * @Param
     * @return
     **/
    private void save(Map<String, String> map, int i, String typeName) {
        Map<String, String> valueMap = new HashMap<String, String>();
        valueMap.put("type_name", typeName);
        valueMap.put("num", "" + i);
        valueMap.put("file_path", map.get("sourcePath"));
        valueMap.put("backup_path", map.get("backUpPath"));
        valueMap.put("update_time", LocalDateTime.now().format(DateTimeFormatter.ofPattern(DEFAULT_FORMAT)));
        log.info("类型:{},编号:{}的map数据!map:{}", typeName, i, valueMap);
        SqlLiteConnection.insertTable(valueMap);
    }


    /**
     * @return
     * @Author beixing
     * @Description 执行数据存储
     * @Date 2021/12/27
     * @Param
     **/
    private void executeDb(Map<String, String> map, int i) throws IOException {
        String command = getCommand(map, i);
        String os = System.getProperty("os.name");
        Process process = null;
        if (os.toLowerCase().startsWith("win")) {
            process = Runtime.getRuntime().exec(command);
        } else {
            process = Runtime.getRuntime().exec(new String[]{"/bin/sh", "-c", command});
        }

        BufferedReader br = null;
        StringBuffer sbf = new StringBuffer();
        String line = null;
        br = new BufferedReader(new InputStreamReader(process.getInputStream()));
        long now = System.currentTimeMillis();
        while ((line = br.readLine()) != null) {
            sbf.append(line);
            sbf.append(" ");
            if ((System.currentTimeMillis() - now) > 10 * 1000) {
                process.destroy();
//                    log.info("已结束:{}", LocalDateTime.now());
            }
        }
        br.close();
        String resultInfo = sbf.toString();
        log.info("result:{}", resultInfo);
        process.destroy();

        //文件压缩
        String filePath = map.get("backUpPath");
        String filePath2 = map.get("backUpPath").concat(".zip");
        String name = "";
        compress(filePath,filePath2,name);
        log.info("编号:{},文件压缩成功!压缩文件名称:{}", i,filePath2);
        FileHelper.deleteFile(filePath);

        String targetFilePath = map.get(TARGET_PATH + i);
        int backUpDay = Integer.parseInt(map.get(BACKUP_DAY));
        deleteFiles(targetFilePath,backUpDay);
    }

    /**
     * 文件压缩
     *
     * @param path,path2
     * @return
     */
    public static void compress(String path, String path2, String fileName) throws IOException {
        // 创建压缩对象
        ZipArchiveEntry entry = new ZipArchiveEntry(fileName);
        // 要压缩的文件
        File f = new File(path);
        FileInputStream fis = new FileInputStream(f);
        // 输出的对象 压缩的文件
        ZipArchiveOutputStream zipOutput = new ZipArchiveOutputStream(new File(path2));
        zipOutput.putArchiveEntry(entry);
        int j;
        while ((j = fis.read()) != -1) {
            zipOutput.write(j);
        }
        zipOutput.closeArchiveEntry();
        zipOutput.close();
        fis.close();
    }

    /**
     * 备份命令
     *
     * @param map
     * @param i
     * @return
     */
    private String getCommand(Map<String, String> map, int i) {
        String ip = map.get(URL_NAME + i);
        int port = Integer.parseInt(map.getOrDefault(PORT + i, "3306"));
        String targetFilePath = map.get(TARGET_PATH + i);
        String username = map.get(USERNAME + i);
        String password = map.get(PWD_NAME + i);
        String dbName = map.get(DB_NAME + i);
        String tableName = map.get(TABLE_NAME + i);
        String mysqlDumpPath = map.get(MYSQL_PATH) + DUMP_NAME;
        String fileName = dbName + LINE_SIGN2 + getNow() + ".sql";

        File saveFile = new File(targetFilePath);
        if (!saveFile.exists()) {
            saveFile.mkdirs();
        }

        StringBuilder arg = new StringBuilder();
        arg.append(mysqlDumpPath);
        arg.append(" -h" + ip);
        arg.append(" -u" + username);
        arg.append(" -p" + password);
        arg.append(" --port " + port);
        arg.append(" -r ");
        arg.append(targetFilePath);
        arg.append(fileName);
        arg.append(" --databases " + dbName);
        if (!StringUtils.isEmpty(tableName)) {
            arg.append(" --tables " + tableName);
        }
        log.info("mysql备份执行命令:" + arg.toString());
        map.put("backUpPath", targetFilePath + fileName);
        map.put("sourcePath", dbName);
        return arg.toString();
    }

    private String getNow() {
        return LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
    }

    private  void deleteFiles(String path,int day) {
        File file = new File(path);
        if(file.isDirectory()){
            File[] listFiles = file.listFiles();
            for (int i = 0; i < listFiles.length; i++) {
                File f = listFiles[i];
                String name =  f.getName();
                String t = name.split(LINE_SIGN2)[1];
                String now = LocalDate.now().minusDays(day).format(DateTimeFormatter.ofPattern("yyyyMMdd"));
                if(now.compareTo(t)>-1){
                    f.delete();
                    log.info("文件:{} 删除成功！",name);
                }
            }
        }
    }

    /**
     * @param map
     * @param i
     * @return
     * @Author beixing
     * @Description 文件备份
     * @Date 2021/12/28
     * @Param
     */
    @Override
    public void fileBackup(Map<String, String> map, int i) {
        String executeTime = map.getOrDefault(EXECUTE_TIME + i, "23:30:00");
        String hostName = map.get(HOST_NAME + i);
        int port = Integer.parseInt(map.getOrDefault(PORT + i, "22"));
        int os = Integer.parseInt(map.getOrDefault(OS + i, "1"));
        int isIncrement = Integer.parseInt(map.getOrDefault(IS_INCREMENT + i, "0"));
        String sourcePathPrefix = map.get(SOURCE_PATH_PREFIX + i);
        String srcFilePath = map.get(SOURCE_PATH + i);
        String targetFilePath = map.get(TARGET_PATH + i);
        String username = map.get(USERNAME + i);
        String password = map.get(PWD_NAME + i);
        Calendar calendar = Calendar.getInstance();
        int hour = Integer.parseInt(executeTime.substring(0, 2));
        int m = Integer.parseInt(executeTime.substring(3, 5));
        int s = Integer.parseInt(executeTime.substring(6, 8));
        calendar.set(Calendar.HOUR_OF_DAY, hour); // 控制时
        calendar.set(Calendar.MINUTE, m);    // 控制分
        calendar.set(Calendar.SECOND, s);    // 控制秒
        Date time = calendar.getTime();
        Timer timer = new Timer();
        String finalPassword = password;
        timer.scheduleAtFixedRate(new TimerTask() {
            public void run() {
                log.info("开始进行文件下载!下载路径:{},保存路径:{}", srcFilePath, targetFilePath);
                fileBak(hostName, port, srcFilePath, targetFilePath, username, finalPassword);
                log.info("文件下载成功!下载路径:{},保存路径:{}", srcFilePath, targetFilePath);
                map.put("backUpPath", targetFilePath);
                map.put("sourcePath", srcFilePath);
                save(map, i, "file");
            }
        }, time, 1000 * 60 * 60 * 24);// 这里设定将延时每天固定执行
        log.info("编号:{},文件备份任务启动成功!", i);
        int isStartExecute = Integer.parseInt(map.getOrDefault(IS_START_EXECUTE + i, "0"));
        if (isStartExecute == 1) {
            log.info("编号:{},下载路径:{},启动时进行备份!", i, srcFilePath);
            //增量备份
            if(isIncrement == 1){
                incrementBackUp(hostName, port, username, password, srcFilePath, targetFilePath,sourcePathPrefix,os);
            }else{
                fileBak(hostName, port, srcFilePath, targetFilePath, username, password);
                log.info("编号:{},下载路径:{} 备份成功!", i, srcFilePath);
            }
            map.put("backUpPath", targetFilePath);
            map.put("sourcePath", srcFilePath);
            save(map, i, "file");
        }
    }

    private void fileBak(String hostName, int port, String srcFilePath, String targetFilePath, String username, String password) {
        String[] srcFilePaths = srcFilePath.split(COMMA_SIGN);
        for (String filePath : srcFilePaths) {
            SmartSshUtils.downLoadFileBySsh(hostName, port, username, password, filePath, targetFilePath);
        }
    }


    /**
     * @Author beixing
     * @Description  增量备份
     * @Date  2021/12/7
     * @Param
     * @return
     **/
    private void incrementBackUp(String hostName, int port, String username, String password,
                        String srcFilePath, String targetFilePath,String prefix,int os){
        String srcFilePath2 = srcFilePath +getLinuxOrWindPathName(prefix,os);
        String targetFilePath2 = targetFilePath +getPathName(prefix);
        log.info("编号:{} 进行增量备份,下载路径:{},保存路径:{}", srcFilePath2, targetFilePath2);
        fileBak(hostName, port, srcFilePath, targetFilePath2, username, password);
        log.info("编号:{} 增量备份下载成功!下载路径:{},保存路径:{}", srcFilePath, targetFilePath2);
    }

    /**
    * @Author beixing
    * @Description  获取增量文件的路径
    * @Date  2021/12/28
    * @Param
    * @return
    **/
    private String getLinuxOrWindPathName(String prefix,int os){
        LocalDateTime localDateTime= LocalDateTime.now().minusDays(1);
        int yyyy = localDateTime.getYear();
        int mm = localDateTime.getMonthValue();
        int dd = localDateTime.getDayOfMonth();
        String symbol ="/";
        if(os != 1){
            symbol = "\\";
        }
        if(StringUtils.isEmpty(prefix)){
            return symbol+ yyyy+symbol+mm+symbol+dd;
        }
        return prefix + symbol+ yyyy+symbol+mm+symbol+dd;
    }

    private String getPathName(String prefix){
        LocalDateTime localDateTime= LocalDateTime.now().minusDays(1);
        int yyyy = localDateTime.getYear();
        int mm = localDateTime.getMonthValue();
        int dd = localDateTime.getDayOfMonth();
        if(StringUtils.isEmpty(prefix)){
            return File.separator+ yyyy+File.separator+mm+File.separator+dd;
        }
        return prefix + File.separator+ yyyy+File.separator+mm+File.separator+dd;
    }


    public static void main(String[] args) throws IOException {
        String filePath = "D:\\\\bak\\\\d1\\\\guard_scan_20211227.sql";
        String filePath2 = "D:\\\\bak\\\\d1\\\\guard_scan_20211227.sql.zip";
        String name = "";
        compress(filePath,filePath2,name);
        FileHelper.deleteFile(filePath);
    }

}
