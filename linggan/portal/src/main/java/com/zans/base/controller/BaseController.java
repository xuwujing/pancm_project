package com.zans.base.controller;

import static com.zans.base.config.BaseConstants.DATE_RANGE_ARRAY_SIZE;
import static com.zans.base.config.EnumErrorCode.SERVER_DOWNLOAD_ERROR;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson.JSON;
import com.zans.base.config.EnumErrorCode;
import com.zans.base.office.excel.ExcelHelper;
import com.zans.base.util.DateHelper;
import com.zans.base.util.MapBuilder;
import com.zans.base.vo.ApiResult;
import com.zans.base.vo.BasePage;
import com.zans.base.vo.MacPage;
import com.zans.portal.config.GlobalConstants;

import org.javatuples.Pair;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
public class BaseController {

    @Value("${api.page-size:10}")
    private int defaultPageSize;

    public void checkPageParams(BasePage page) {
        this.checkPageParams(page, null);
    }

    public void checkPageParams(BasePage page, String orderBy) {
        Integer pageSize = page.getPageSize();
        Integer pageNum = page.getPageNum();

        if (pageSize == null || pageSize < 0) {
            page.setPageSize(defaultPageSize);
        }
        if (pageNum == null || pageNum <= 0) {
            page.setPageNum(1);
        }

        List<String> dateRange = page.getDateRange();
        if (dateRange != null && dateRange.size() == DATE_RANGE_ARRAY_SIZE) {
            page.setStartDate(dateRange.get(0));
            page.setEndDate(DateHelper.correctEndTime(dateRange.get(1)));
        }

        if (page instanceof MacPage) {
            MacPage macPage = (MacPage) page;
            if (macPage.getMac() != null) {
                macPage.setMac(macPage.getMac().toLowerCase());
            }
        }

        if (orderBy != null) {
            page.setDefaultOrder(orderBy);
        }

    }

    public ApiResult convertToApiResult(Pair<Boolean, String> result, Integer id) {
        if (result.getValue0() != null) {
            if (result.getValue0()) {
                return ApiResult.success(MapBuilder.getSimpleMap("id", id));
            } else {
                return ApiResult.error(result.getValue1());
            }
        }
        return ApiResult.error("unknown error");
    }

    protected void responseApiResult(HttpServletRequest request,
                                    HttpServletResponse response,
                                    ApiResult apiResult) throws Exception{
        response.reset();
        this.setCors(request, response);
        response.setContentType("application/json");
        response.setCharacterEncoding("utf-8");
        String content = JSON.toJSONString(apiResult);
        response.getWriter().println(content);
    }

    protected void setErrorToResponse(HttpServletRequest request,
                                   HttpServletResponse response,
                                   EnumErrorCode errorCode,
                                   String errorMessage) throws Exception{
        response.reset();
        this.setCors(request, response);
        response.setContentType("application/json-error");
        response.setCharacterEncoding("utf-8");


        Map<String, Object> map = MapBuilder.getBuilder().put(errorCode)
                .append("message", errorMessage).build();
        String content = JSON.toJSONString(map);
        String encodingCode = URLEncoder.encode(errorCode.getMessage(), "UTF-8");
        String encodingMessage = URLEncoder.encode(errorMessage, "UTF-8");
        response.setHeader("Error-Code", encodingCode);
        response.setHeader("Error-Message", encodingMessage);
        response.getWriter().println(content);
    }

    protected void setCors(HttpServletRequest request,
                           HttpServletResponse response) {
        String origin = request.getHeader("Origin");
        if (StringUtils.isEmpty(origin)) {
            origin = "*";
        }
        String rh = request.getHeader("Access-Control-Request-Headers");
        if (StringUtils.isEmpty(origin)) {
            rh = "DNT,X-Mx-ReqToken,Keep-Alive,User-Agent,X-Requested-With,If-Modified-Since,Cache-Control," +
                    "Content-Type,Authorization,SessionToken,Content-Disposition";
        }
        // 解决跨域后，axios 无法获得其它 Response Header，默认只有 Content-Language，Content-Type，Expires，Last-Modified，Pragma
        response.setHeader("Access-Control-Expose-Headers", "Content-Disposition,Error-Code,Error-Message");
        response.setHeader("Access-Control-Allow-Origin", origin);
        response.setHeader("Access-Control-Allow-Credentials", "true");
        response.setHeader("Access-Control-Allow-Headers", rh);
    }

    protected void download(String filePath,
                          String fileCnName,
                          HttpServletRequest request,
                          HttpServletResponse response) throws Exception {

        try {
            String encodingName = URLEncoder.encode(fileCnName, "UTF-8");
            File file = new File(filePath);
            InputStream is = new FileInputStream(file);
            response.reset();
            this.setCors(request, response);
            response.setContentType("application/octet-stream");
            response.setCharacterEncoding("utf-8");
            response.setHeader("Content-Disposition", "attachment;filename=" + encodingName);
            response.addHeader("Content-Length", String.valueOf(file.length()));
            byte[] buff = new byte[1024];
            BufferedInputStream bis = null;
            OutputStream os = null;
            try {
                os = response.getOutputStream();
                bis = new BufferedInputStream(is);
                int i = 0;
                while ((i = bis.read(buff)) != -1) {
                    os.write(buff, 0, i);
                    os.flush();
                }
            } finally {
                if (bis != null) {
                    bis.close();
                }
            }

        } catch (Exception ex) {
            String message = "download error#" + filePath;
            log.error(message, ex);
            this.setErrorToResponse(request, response, EnumErrorCode.SERVER_DOWNLOAD_NULL_ERROR, EnumErrorCode.SERVER_DOWNLOAD_NULL_ERROR.getMessage());
        }
    }

    protected Integer getIntegerParameter(HttpServletRequest request, String key) {
        if (request == null || key == null) {
            return null;
        }
        String value = request.getParameter(key);
        if (value == null) {
            return null;
        }
        Integer intValue = null;
        try {
            intValue = Integer.parseInt(value);
        } catch (Exception ex) {

        }
        return intValue;
    }


}