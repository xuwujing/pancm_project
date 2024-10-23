package com.zans.service;

import java.util.Map;

/**
 * @author beixing
 * @Title: debug-api
 * @Description:
 * @Version:1.0.0
 * @Since:jdk1.8
 * @date 2022/3/22
 */
public interface IBaseLineService {

     void doBaseLine(String swIp, String nasPortId, String vlan, String ip, String mac);

     /**
     * @Author beixing
     * @Description 基线检查服务
      * 如果基线以及正常，需要更改回他自己的区域
      * //todo radius模式实现了更改(radius的配置)，acl模式需要实现
     * @Date  2022/3/22
     * @Param
     * @return
     **/
     void checkBaseLine(Map<String,Object> map);
}
