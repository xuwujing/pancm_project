package com.zans.service;

import java.util.Map;

/**
 * @author beixing
 * @Title: zans-bak
 * @Description: 备份的接口
 * @Version:1.0.0
 * @Since:jdk1.8
 * @date 2021/12/28
 */
public interface IBackUpService {

    /**
    * @Author beixing
    * @Description  数据库备份
    * @Date  2021/12/28
    * @Param
    * @return
    **/
    void dbBackup(Map<String, String> map, int i);


    /**
    * @Author beixing
    * @Description 文件备份
    * @Date  2021/12/28
    * @Param
    * @return
    **/
    void fileBackup(Map<String, String> map, int i);



}
