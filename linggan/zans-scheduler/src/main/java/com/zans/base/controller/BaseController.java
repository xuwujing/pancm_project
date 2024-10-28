package com.zans.base.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.zans.base.config.EnumErrorCode;
import com.zans.base.util.DateHelper;
import com.zans.base.util.FileHelper;
import com.zans.base.util.MapBuilder;
import com.zans.base.vo.ApiResult;
import com.zans.base.vo.BasePage;
import com.zans.base.vo.MacPage;
import lombok.extern.slf4j.Slf4j;
import org.javatuples.Pair;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.URLEncoder;
import java.net.UnknownHostException;
import java.util.List;
import java.util.Map;

import static com.zans.base.config.BaseConstants.DATE_RANGE_ARRAY_SIZE;
import static com.zans.base.config.BaseConstants.VALID_IP_MAX_LENGTH;
import static com.zans.base.config.EnumErrorCode.SERVER_DOWNLOAD_ERROR;

@Slf4j
@Controller
public class BaseController {

    @Value("${api.page-size:20}")
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
            InputStream is = new FileInputStream(new File(filePath));
            response.reset();

            this.setCors(request, response);

            response.setContentType("application/octet-stream");
            response.setCharacterEncoding("utf-8");
            response.setHeader("Content-Disposition", "attachment;filename=" + encodingName);
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
            this.setErrorToResponse(request, response, SERVER_DOWNLOAD_ERROR, ex.getMessage());
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

    protected String getIpAddr(HttpServletRequest request) {
        String ipAddress = null;
        try {
            ipAddress = request.getHeader("x-forwarded-for");
            if (ipAddress == null || ipAddress.length() == 0 ||
                    "unknown".equalsIgnoreCase(ipAddress)) {
                ipAddress = request.getHeader("Proxy-Client-IP");
            }
            if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
                ipAddress = request.getHeader("WL-Proxy-Client-IP");
            }
            if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
                ipAddress = request.getRemoteAddr();
                log.info("request#{}", ipAddress);

                if ("0:0:0:0:0:0:0:1".equals(ipAddress) || "127.0.0.1".equals(ipAddress)) {
                    // 根据网卡取本机配置的IP
                    try {
                        InetAddress inet = Inet4Address.getLocalHost();
                        ipAddress = inet.getHostAddress();
                    } catch (UnknownHostException e) {
                        e.printStackTrace();
                    }
                }
            }
            // 对于通过多个代理的情况，第一个IP为客户端真实IP,多个IP按照','分割
            if (ipAddress != null && ipAddress.length() > VALID_IP_MAX_LENGTH) {
                // "***.***.***.***".length()= 15
                if (ipAddress.indexOf(",") > 0) {
                    ipAddress = ipAddress.substring(0, ipAddress.indexOf(","));
                }
            }
        } catch (Exception e) {
            ipAddress="";
        }
//        ipAddress = request.getRemoteAddr();
        log.info("ip-header#{}, {}", ipAddress, request.getRemoteAddr());

        return ipAddress;
    }


}
