package com.zans.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.zans.commons.utils.MyTools;
import com.zans.dao.EsDao;
import com.zans.pojo.AlertRuleBean;
import com.zans.pojo.AlertServerBean;
import com.zans.pojo.RuleBean;
import com.zans.service.IEsDealService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;

import static com.zans.commons.contants.Constants.TIME_SEPARATOR;
import static com.zans.commons.utils.HttpClientUtil.post;
import static com.zans.contants.AlertConstants.*;

/**
 * @author pancm
 * @Title: alertmanage-server
 * @Description: es 实现业务代码
 * @Version:1.0.0
 * @Since:jdk1.8
 * @date 2020/9/2
 */
@Service
@Slf4j
public class EsDealService implements IEsDealService {

    @Autowired
    private EsDao esDao;

    @Override
    public JSONArray analyzeEs(AlertServerBean alertServerBean, AlertRuleBean bean) {
        /*
         * 1.根据告警规则类型进行不同的处理
         * 2.为0表示通过接口进行请求，为1表示使用规则进行查询
         **/
        JSONArray jsonArray = null;
        String data = bean.getAlert_dsl();
        if (bean.getRule_type() == 0) {
            String url = getUrl(alertServerBean.getServer_ip(), alertServerBean.getServer_port());
            String postTest = null;
            try {
                log.debug("策略ID:{},请求ES地址:{},请求数据:{}！",bean.getId(),url,data.trim());
                postTest = post(url, data);
                if(bean.getAlert_dsl_deal()!=null && bean.getAlert_dsl_deal() == 1){
                    jsonArray = getScriptData(postTest,bean.getAlert_keyword());
                }else {
                    jsonArray = getAggData(postTest);
                }
            } catch (Exception e) {
                log.error("策略ID:{},ES请求DSL失败！",bean.getId(),e);
            }
            return jsonArray;
        }
        RuleBean ruleBean = MyTools.toBean(bean.getAlert_rule(), RuleBean.class);
        try {
            jsonArray = esDao.query(ruleBean);
        } catch (IOException e) {
            log.error("策略ID:{},ES查询失败！查询",bean.getId(),e);
        }
        return jsonArray;
    }

    /**
     * @Author pancm
     * @Description 得到ES的请求地址
     * @Date  2020/9/2
     * @Param [ip, port]
     * @return java.lang.String
     **/
    private String getUrl(String ip, int port) {
        StringBuffer sb = new StringBuffer();
        sb.append(HTTP).append(ip).append(TIME_SEPARATOR).append(port).append(ES_URI);
        return sb.toString();
    }

    /**
     * @Author pancm
     * @Description 得到ES聚合查询的返回结果1
     * @Date  2020/9/2
     * @Param [data]
     * @return com.alibaba.fastjson.JSONArray
     **/
    private JSONArray getAggData(String data) {
        JSONObject jsonObject = MyTools.toJson(data).getJSONObject(ES_AGG);
        JSONArray jsonArray = JSON.parseArray(jsonObject.getJSONObject(ES_GROUP).getString(ES_BUCKETS));
        return jsonArray;
    }

    /**
     * @Author pancm
     * @Description 得到ES的script查询的返回结果
     * @Date  2020/9/8
     * @Param [data]
     * @return com.alibaba.fastjson.JSONArray
     **/
    private JSONArray getScriptData(String data,String keyword) {
        JSONArray jsonArray = MyTools.toJson(data).getJSONObject(ES_HIT).getJSONArray(ES_HIT);
        JSONArray resultArray = new JSONArray();
        for (Object o : jsonArray) {
            JSONObject resultJson = new JSONObject();
            JSONObject jsonObject =(JSONObject)o;
            JSONArray jsonArray1 =jsonObject.getJSONObject(ES_FIELDS).getJSONArray(keyword);
            Object o1 =jsonArray1.get(0);
            Object value = jsonObject.getJSONArray(ES_SORT).get(0);
            resultJson.put(keyword,o1);
            resultJson.put(ES_AGG_COUNT,value);
            resultArray.add(resultJson);
        }
        return resultArray;
    }

    public static void main(String[] args) {
        EsDealService esDealService = new EsDealService();
        AlertServerBean alertServerBean = new AlertServerBean();
        alertServerBean.setServer_ip("192.168.8.78");
        alertServerBean.setServer_port(9200);
        AlertRuleBean bean = new AlertRuleBean();
        bean.setAlert_dsl("{\"size\":10,\"query\":{\"bool\":{\"must\":[{\"script\":{\"script\":{\"source\":\"InternalSqlScriptUtils.nullSafeFilter(InternalSqlScriptUtils.gt(InternalSqlScriptUtils.div(InternalSqlScriptUtils.docValue(doc,params.v0),InternalSqlScriptUtils.docValue(doc,params.v1)),params.v2))\",\"lang\":\"painless\",\"params\":{\"v0\":\"acct_input_octets\",\"v1\":\"acct_output_octets\",\"v2\":0.1}},\"boost\":1}},{\"range\":{\"acct_update_time\":{\"from\":\"now-60d/d\",\"to\":null,\"include_lower\":false,\"include_upper\":true,\"boost\":1}}},{\"range\":{\"acct_output_octets\":{\"from\":0,\"to\":null,\"include_lower\":false,\"include_upper\":false,\"boost\":1}}}],\"adjust_pure_negative\":true,\"boost\":1}},\"_source\":false,\"stored_fields\":\"_none_\",\"docvalue_fields\":[{\"field\":\"username\"}],\"sort\":[{\"_script\":{\"script\":{\"source\":\"InternalSqlScriptUtils.nullSafeSortNumeric(InternalSqlScriptUtils.div(InternalSqlScriptUtils.docValue(doc,params.v0),InternalSqlScriptUtils.docValue(doc,params.v1)))\",\"lang\":\"painless\",\"params\":{\"v0\":\"acct_input_octets\",\"v1\":\"acct_output_octets\"}},\"type\":\"number\",\"order\":\"desc\"}}]}");
        bean.setAlert_dsl_deal(1);
        bean.setAlert_keyword("username");
        bean.setRule_type(0);
        JSONArray jsonArray = esDealService.analyzeEs(alertServerBean,bean);
        bean.setAlert_dsl("{\"size\":10,\"query\":{\"bool\":{\"must\":[{\"script\":{\"script\":{\"source\":\"InternalSqlScriptUtils.nullSafeFilter(InternalSqlScriptUtils.gt(InternalSqlScriptUtils.div(InternalSqlScriptUtils.docValue(doc,params.v0),InternalSqlScriptUtils.docValue(doc,params.v1)),params.v2))\",\"lang\":\"painless\",\"params\":{\"v0\":\"acct_input_octets\",\"v1\":\"acct_output_octets\",\"v2\":0.1}},\"boost\":1}},{\"range\":{\"acct_update_time\":{\"from\":\"now-1d/d\",\"to\":null,\"include_lower\":false,\"include_upper\":true,\"boost\":1}}},{\"range\":{\"acct_output_octets\":{\"from\":0,\"to\":null,\"include_lower\":false,\"include_upper\":false,\"boost\":1}}}],\"adjust_pure_negative\":true,\"boost\":1}},\"_source\":false,\"stored_fields\":\"_none_\",\"docvalue_fields\":[{\"field\":\"username\"}],\"sort\":[{\"_script\":{\"script\":{\"source\":\"InternalSqlScriptUtils.nullSafeSortNumeric(InternalSqlScriptUtils.div(InternalSqlScriptUtils.docValue(doc,params.v0),InternalSqlScriptUtils.docValue(doc,params.v1)))\",\"lang\":\"painless\",\"params\":{\"v0\":\"acct_input_octets\",\"v1\":\"acct_output_octets\"}},\"type\":\"number\",\"order\":\"desc\"}}]}");
        JSONArray jsonArray2 = esDealService.analyzeEs(alertServerBean,bean);
        System.out.println(jsonArray.toJSONString());
        System.out.println(jsonArray2.toJSONString());
    }

}
