package com.zans.service.impl;

import com.zans.service.IRestoreService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Map;

import static com.zans.config.Constants.*;

/**
 * @author beixing
 * @Title: zans-bak
 * @Description:
 * @Version:1.0.0
 * @Since:jdk1.8
 * @date 2021/12/28
 */
@Service
@Slf4j
public class RestoreServiceImpl implements IRestoreService {

    /**
     * @param map
     * @param i
     * @return
     * @Author beixing
     * @Description
     * @Date 2021/12/28
     * @Param
     */
    @Override
    public void dbRestore(Map<String, String> map, int i) {

    }

    /**
     * 还原命令
     * @param map
     * @param i
     * @return
     */
    private String getRestoreCommand(Map<String, String> map, int i) {
        String ip = map.get(URL_NAME + i);
        int port = Integer.parseInt(map.getOrDefault(PORT + i, "3306"));
        String username = map.get(USERNAME + i);
        String password = map.get(PWD_NAME + i);
        String dbName = map.get(DB_NAME + i);
        String tableName = map.get(TABLE_NAME + i);
        String mysqlDumpPath = map.get(MYSQL_PATH) + MYSQL_NAME;
        String fileName = dbName + "_" + getNow() + ".sql";
        StringBuilder arg = new StringBuilder();
        arg.append(mysqlDumpPath);
        arg.append(" -h" + ip);
        arg.append(" -u" + username);
        arg.append(" -p" + password);
        arg.append(fileName);
        arg.append(" --databases " + dbName);
        if (!StringUtils.isEmpty(tableName)) {
            arg.append(" " + tableName);
        }
        log.info("mysql备份执行命令:" + arg.toString());
        return arg.toString();
    }

    private String getNow() {
        return LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
    }

}
