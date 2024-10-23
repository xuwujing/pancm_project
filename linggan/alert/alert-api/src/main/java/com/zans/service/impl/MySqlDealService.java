package com.zans.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.zans.commons.contants.Constants;
import com.zans.commons.utils.HttpClientUtil;
import com.zans.commons.utils.MyTools;
import com.zans.dao.DbDao;
import com.zans.dao.impl.DbDaoImpl;
import com.zans.pojo.AlertRuleBean;
import com.zans.pojo.AlertServerBean;
import com.zans.service.IMySqlDealService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @author pancm
 * @Title: alertmanage-server
 * @Description: mysql 实现业务代码
 * @Version:1.0.0
 * @Since:jdk1.8
 * @date 2020/9/2
 */
@Service
@Slf4j
public class MySqlDealService implements IMySqlDealService {

    private DbDao dbDao = null;


    @Override
    public JSONArray analyzeMySql(AlertServerBean alertServerBean, AlertRuleBean bean) {
        /*
         * 1.根据告警规则类型进行不同的处理
         * 2.为0表示执行sql语句，非0暂不支持
         **/
        dbDao = new DbDaoImpl(alertServerBean.getServer_name());
        JSONArray jsonArray = null;
        if (bean.getRule_type() == 0) {
            String data = bean.getAlert_dsl();
            try {
                log.debug("策略ID:{},查询的sql语句:{}", bean.getId(), data);
                List<Map<String, Object>> mapList = dbDao.query(data);
                log.debug("策略ID:{},查询的sql结果:{}", bean.getId(), mapList);
                if (MyTools.isNotEmpty(mapList)) {
                    String sqlAction = bean.getSql_action();
                    if (MyTools.isNotEmpty(sqlAction)) {
                        int i = 0;
                        for (Map<String, Object> objectMap : mapList) {
                            String sql = String.format(sqlAction, objectMap.get(bean.getAlert_keyword()));
                            dbDao.executeUpdate(sql);
                            log.debug("策略ID:{},执行的sql语句:{}!", bean.getId(), sql);
                            i++;
                        }
                        log.info("策略ID:{},已成功执行:{}条语句！", bean.getId(), i);
                    }
                    Integer alertAction = bean.getAlert_action();
                    if(alertAction!=null && alertAction == 3){
                        String sql1 = String.format(SELECT_SYS_CONSTANT,RAD_API_CHANGE);
                        String sql2 = String.format(SELECT_SYS_CONSTANT,RAD_API_HOST);
                        List<Map<String,Object>> mapList1 =  dbDao.query(sql1);
                        List<Map<String,Object>> mapList2 =  dbDao.query(sql2);
                        if(MyTools.isNotEmpty(mapList1)){
                            String uri = (String) mapList1.get(0).get(CONSTANT_VALUE);
                            if(MyTools.isNotEmpty(mapList1)){
                                String host = (String) mapList2.get(0).get(CONSTANT_VALUE);
                                Integer policy = 1;
                                for (Map<String, Object> objectMap : mapList) {
                                    String mac = (String) objectMap.get("mac");
                                    String baseIp = (String) objectMap.get("cur_ip_addr");
                                    radiusJudge(policy,host,uri,mac,0,baseIp);
                                }
                            }
                        }
                    }
                }
                String json = MyTools.toString(mapList, Constants.TIME_FORMAT2);
                jsonArray = MyTools.toJSONArray(json);
            } catch (Exception e) {
                log.error("策略ID:{},sql查询失败！sql语句:{},原因是:", bean.getId(), data, e);
            }
        } else {
            log.warn("策略ID:{},非执行语句，暂不支持！", bean.getId());
        }
        return jsonArray;
    }


    private void radiusJudge(Integer policy, String radApiHost, String radApi,String mac, Integer deleteStatus, String baseIp) {
        String module = "endpoint";
        // 调用rad_api接口
        //2021-6-25 合北辰确认 新增baseIp
        String uri = String.format("/sync/%s?mac=%s&delete_status=%s&policy=%d&baseIp=%s", module, mac, deleteStatus,
                policy,baseIp);
        // 2021-6-11 根据不同的policy选择不同的uri
        String uri2 ="";

        uri2 = radApi + "?mac=" + mac + "&policy=" + policy;

        String serverUrl = radApiHost + uri;
        String serverUrl2 = radApiHost + uri2;
        try {
            HttpClientUtil.get(serverUrl);
            HttpClientUtil.get(serverUrl2);
            log.info("调用接口成功！请求url1:{},请求url2:{}",serverUrl,serverUrl2);
        } catch (Exception e) {
           log.error("调用接口失败！请求url1:{},请求url2:{}",serverUrl,serverUrl2);
        }

    }


    private static String SELECT_SYS_CONSTANT = " select * from sys_constant s where s.constant_key = '%s' limit 1";
    public static String RAD_API_CHANGE = "rad_api:judge:change";
    public static String RAD_API_HOST = "rad_api:host";
    public static String CONSTANT_VALUE = "constant_value";
}
