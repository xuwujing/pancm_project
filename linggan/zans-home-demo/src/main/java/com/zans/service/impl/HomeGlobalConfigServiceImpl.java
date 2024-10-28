package com.zans.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.zans.dao.HomeGlobalConfigDao;
import com.zans.dao.HomeIndexConfigDao;
import com.zans.service.IHomeGlobalConfigService;
import com.zans.vo.ApiResult;
import com.zans.vo.HomeGlobalConfigVo;
import com.zans.vo.HomeIndexConfigVO;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;


/**
 * 首页全局配置表(HomeGlobalConfig)表服务实现类
 *
 * @author beixing
 * @since 2021-10-21 10:37:05
 */
@Service("homeGlobalConfigService")
public class HomeGlobalConfigServiceImpl implements IHomeGlobalConfigService {
    @Resource
    private HomeGlobalConfigDao homeGlobalConfigDao;

    @Resource
    private HomeIndexConfigDao homeIndexConfigDao;

    private static int count =1;

    @Override
    public ApiResult globalConfig() {
        Integer globalId = 1;
        List<HomeGlobalConfigVo> list = homeGlobalConfigDao.queryByGlobalId(globalId);


//        HomeGlobalConfig homeGlobalConfig = homeGlobalConfigDao.queryById(1);
//        String data =  homeGlobalConfig.getCoordData();
        return ApiResult.success(list);
    }

    @Override
    public ApiResult getPie() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("type","pie");
        String legend = "{\n" +
                "      \"bottom\": 0,\n" +
                "      \"textStyle\": {\n" +
                "        \"color\": \"#24D4D2\"\n" +
                "      }\n" +
                "    }";
//        jsonObject.put("legend",legend);

        String label = "{\n" +
                "      \"show\": \"true\",\n" +
                "      \"formatter\": \"{a|{b}} \\n {b|{c}}\",\n" +
                "      \"rich\": {\n" +
                "        \"a\": {\n" +
                "          \"color\": \"#1caba9\",\n" +
                "          \"fontSize\": 14,\n" +
                "          \"fontWeight\": 400,\n" +
                "          \"lineHeight\": 30,\n" +
                "          \"fontFamily\": \"Alibaba-PuHuiTi-Regular\"\n" +
                "        },\n" +
                "        \"b\": {\n" +
                "          \"color\": \"#b4e7e8\",\n" +
                "          \"fontSize\": 24,\n" +
                "          \"fontWeight\": \"bold\",\n" +
                "          \"lineHeight\": 30,\n" +
                "          \"fontFamily\": \"DINAlternate-Bold\"\n" +
                "        }\n" +
                "      }\n" +
                "    }";

        String label2 = "{\n" +
                "      \"show\": \"true\",\n" +
                "      \"formatter\": \"{a|{b}} \\n {b|{c}}\",\n" +
                "      \"rich\": {\n" +
                "        \"a\": {\n" +
                "          \"color\": \"#1caba9\",\n" +
                "          \"fontSize\": 16,\n" +
                "          \"fontWeight\": 500,\n" +
                "          \"lineHeight\": 60,\n" +
                "          \"fontFamily\": \"Alibaba-PuHuiTi-Regular\"\n" +
                "        },\n" +
                "        \"b\": {\n" +
                "          \"color\": \"#b4e7e8\",\n" +
                "          \"fontSize\": 54,\n" +
                "          \"fontWeight\": \"bold\",\n" +
                "          \"lineHeight\": 60,\n" +
                "          \"fontFamily\": \"DINAlternate-Bold\"\n" +
                "        }\n" +
                "      }\n" +
                "    }";


        String dataList = "[\n" +
                "      { \"name\": \"a\", \"value\": 1 },\n" +
                "      { \"name\": \"b\", \"value\": 2 },\n" +
                "      { \"name\": \"c\", \"value\": 3 }\n" +
                "   ]";
        String dataList2 = "[\n" +
                "      { \"name\": \"a\", \"value\": 11 },\n" +
                "      { \"name\": \"b\", \"value\": 22 },\n" +
                "      { \"name\": \"c\", \"value\": 33 }\n" +
                "   ]";


        if(count%2 == 0){
//            jsonObject.put("label",label);
            jsonObject.put("dataList",dataList);
        }else {
//            jsonObject.put("label",label2);
            jsonObject.put("dataList",dataList2);
        }
        count++;

        return ApiResult.success(jsonObject);
    }

    @Override
    public ApiResult getLine() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("type","line");


        String dataList = " " +
                "         {\"name\":[\"武昌\", \"汉口\",\"汉阳\"],\n" +
                "      \"value\":[11,12,13]}" +
                "   ";
        String dataList2 = " " +
                "      {\"name\":[\"硚口区\", \"黄陂区\",\"蔡甸区\"],\n" +
                "      \"value\":[14, 15,16]}\n" +
                "   ";

        if(count%2 == 0){
            jsonObject.put("dataList",dataList);
        }else {
            jsonObject.put("dataList",dataList2);
        }
        count++;
        return ApiResult.success(jsonObject);
    }

    @Override
    public ApiResult getIndex(Integer id) {
        HomeIndexConfigVO homeIndexConfig = homeIndexConfigDao.queryById(id);
        return ApiResult.success(homeIndexConfig);
    }
}
