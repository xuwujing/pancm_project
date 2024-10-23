package com.zans.service;

import java.util.Map;

/**
 * @author beixing
 * @Title: zans-bak
 * @Description: 恢复的任务
 * @Version:1.0.0
 * @Since:jdk1.8
 * @date 2021/12/28
 */
public interface IRestoreService {

    /**
    * @Author beixing
    * @Description
    * @Date  2021/12/28
    * @Param
    * @return
    **/
    void dbRestore(Map<String, String> map, int i);


}
