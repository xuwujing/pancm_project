package com.zans.config;

import com.zans.service.IBackUpService;
import com.zans.util.AESUtil;
import com.zans.util.SmartSshUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Map;

import static com.zans.config.Constants.*;

/**
 * @author pancm
 * @Title:
 * @Description: 启动之后初始化
 * @Version:1.0.0
 * @Since:jdk1.8
 * @date 2020/9/1
 */
@Component
@Slf4j
public class Runner implements CommandLineRunner {

    @Autowired
    private MapDbProperties mapDbProperties;

    @Autowired
    private MapFileProperties mapFileProperties;

    @Autowired
    private IBackUpService backUpService;

    @Override
    public void run(String... args) {
        initProperties();

    }

    /**
     * @return void
     * @Author pancm
     * @Description 配置初始化
     * @Date 2020/9/1
     * @Param []
     **/
    private void initProperties() {
        SqlLiteConnection.creatTable();
        Map<String, String> mapDb = mapDbProperties.getInfo();
        Map<String, String> mapFile = mapFileProperties.getInfo();
        check(mapDb, mapFile);
        int num = Integer.parseInt(mapDb.get(DB_NUM));
        for (int i = 1; i <= num; i++) {
            //任务
            backUpService.dbBackup(mapDb, i);
        }

        int fileNum = Integer.parseInt(mapFile.get(DB_NUM));
        for (int i = 1; i <= fileNum; i++) {
            backUpService.fileBackup(mapFile,i);
        }

    }




    /**
     * @return
     * @Author beixing
     * @Description 配置文件校验
     * @Date 2021/12/27
     * @Param
     **/
    private void check(Map<String, String> mapDb, Map<String, String> mapFile) {
        int num = Integer.parseInt(mapDb.get(DB_NUM));
        for (int i = 1; i <= num; i++) {
            //数据库连接校验，失败会直接退出程序！
            new ConnectionManager(mapDb, i);
        }
        int fileNum = Integer.parseInt(mapFile.get(DB_NUM));
        for (int i = 1; i <= fileNum; i++) {
            String hostName = mapFile.get(HOST_NAME + i);
            int port = Integer.parseInt(mapFile.getOrDefault(PORT + i, "22"));
            String username = mapFile.get(USERNAME + i);
            String password = mapFile.get(PWD_NAME + i);
            String password2 = AESUtil.desEncrypt(password).trim();
            // 连接成功并且是明文的话
            if (password2 == null) {
                log.warn("第{}编号的ssh的密码是明文!", i);
            }
            password = password2 == null ? password : password2;
            boolean isFlag = SmartSshUtils.testSFTP(hostName, port, username, password);
            if (!isFlag) {
                log.error("第{}编号的ssh连接失败！程序退出!", i);
                System.exit(1);
            }
            mapFile.put(PWD_NAME + i,password);
            log.info("第{}编号的ssh连接成功!", i);
        }

    }


}
