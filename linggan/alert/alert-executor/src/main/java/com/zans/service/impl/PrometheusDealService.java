package com.zans.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.zans.commons.utils.MyTools;
import com.zans.pojo.AlertRuleBean;
import com.zans.pojo.AlertServerBean;
import com.zans.service.IPrometheusDealService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import static com.zans.commons.contants.Constants.CODING_UTF8;
import static com.zans.commons.contants.Constants.TIME_SEPARATOR;
import static com.zans.commons.utils.HttpClientUtil.get;
import static com.zans.contants.AlertConstants.*;

/**
 * @author pancm
 * @Title: alertmanage-server
 * @Description: prometheus实现业务代码
 * @Version:1.0.0
 * @Since:jdk1.8
 * @date 2020/9/2
 */
@Service
@Slf4j
public class PrometheusDealService implements IPrometheusDealService {


    @Override
    public JSONArray analyzePrometheus(AlertServerBean alertServerBean, AlertRuleBean bean) {
        JSONArray jsonArray = null;
        String data = null;
        String url = getUrl(alertServerBean.getServer_ip(), alertServerBean.getServer_port());
        String postTest = null;
        try {
            //这里需要对内容进行编码，不然存在特殊字符会请求失败
            data = URLEncoder.encode(bean.getAlert_dsl(), CODING_UTF8);
            url = url.concat(data);
            postTest = get(url);
            jsonArray = getPrometheusData(postTest, bean.getAlert_threshold());
        } catch (Exception e) {
            log.error("请求prometheus失败！请求参数:{},原因是:", url, e);
        }
        return jsonArray;
    }

    /**
     * @return java.lang.String
     * @Author pancm
     * @Description
     * @Date 2020/9/2
     * @Param [ip, port]
     **/
    private String getUrl(String ip, int port) {
        StringBuffer sb = new StringBuffer();
        sb.append(HTTP).append(ip).append(TIME_SEPARATOR).append(port).append(PTS_URI);
        return sb.toString();
    }

    /**
     * @return com.alibaba.fastjson.JSONArray
     * @Author pancm
     * @Description 解析prometheus的报文体
     * @Date 2020/9/7
     * @Param [data]
     **/
    private JSONArray getPrometheusData(String data, Integer alertThreshold) {
        JSONArray jsonArray = MyTools.toJson(data).getJSONObject(PTS_DATA).getJSONArray(PTS_RESULT);
        JSONObject resultJson = new JSONObject();
        JSONArray resultArray = new JSONArray();
        for (Object o : jsonArray) {
            JSONObject jsonObject = (JSONObject) o;
            String device = jsonObject.getJSONObject(PTS_METRIC).getString(PTS_DEVICE);
            Object value = jsonObject.getJSONArray(PTS_VALUE).get(1);
            String v = String.format("%.0f", Double.valueOf(value.toString()));
            if (MyTools.isNotEmpty(device)) {
                if (PTS_DEFAULT.equals(device)) {
                    if (Integer.parseInt(v) > alertThreshold) {
                        resultJson.put(PTS_INSTANCE, jsonObject.getJSONObject(PTS_METRIC).getString(PTS_INSTANCE));
                        resultJson.put(PTS_VALUE, v);
                        resultArray.add(resultJson);
                        break;
                    }
                }
                continue;
            }
            if (Integer.parseInt(v) > alertThreshold) {
                resultJson.put(PTS_INSTANCE, jsonObject.getJSONObject(PTS_METRIC).getString(PTS_INSTANCE));
                resultJson.put(PTS_VALUE, v);
                resultArray.add(resultJson);
            }
        }
        return resultArray;
    }


    public static void main(String[] args) throws UnsupportedEncodingException {
        PrometheusDealService prometheusDealService = new PrometheusDealService();
        AlertServerBean alertServerBean = new AlertServerBean();
        String dsl = "(node_filesystem_size_bytes {mountpoint =\"/\"} - node_filesystem_free_bytes {mountpoint =\"/\"}) / node_filesystem_size_bytes {mountpoint =\"/\"} * 100";
        String dsl2 = "100 - (avg(irate(node_cpu_seconds_total{mode=\"idle\"}[5m])) by (instance) * 100)";
        String dsl3 = "100 - (node_memory_MemFree_bytes+node_memory_Cached_bytes+node_memory_Buffers_bytes) / node_memory_MemTotal_bytes * 100";
        AlertRuleBean bean = new AlertRuleBean();
        alertServerBean.setServer_ip("10.0.6.212");
        alertServerBean.setServer_port(9090);
        bean.setAlert_dsl(dsl2);
        bean.setAlert_threshold(30);
        JSONArray jsonArray = prometheusDealService.analyzePrometheus(alertServerBean, bean);
        System.out.println(jsonArray);
    }


}
