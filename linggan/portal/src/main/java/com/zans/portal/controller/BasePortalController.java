package com.zans.portal.controller;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.zans.base.controller.BaseController;
import com.zans.base.util.FileHelper;
import com.zans.base.vo.UserSession;
import com.zans.portal.util.HttpHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * @author xv
 * @since 2020/5/7 12:28
 */
@Slf4j
@Controller
public class BasePortalController extends BaseController {

    @Autowired
    protected HttpHelper httpHelper;

    @Value("${mock-api.mock-dir}")
    protected String mockDir;

    public UserSession getUserSession(HttpServletRequest request) {
        return this.httpHelper.getUser(request);
    }

    protected List<Map<String, Object>> loadMockDataList(String fileName) {

        String folder = FileHelper.convertFolder(mockDir);
        String filePath = folder + "/" + fileName;
        String content = FileHelper.readFileToString(filePath);
        List<Map<String, Object>> dataList = JSONObject.parseObject(content, new TypeReference<List<Map<String, Object>>>() {
        });
        return dataList;
    }

    protected Map<String, Object> loadMockData(String json) {
        Map<String, Object> data = JSONObject.parseObject(json, new TypeReference<Map<String, Object>>() {
        });
        return data;
    }

}
