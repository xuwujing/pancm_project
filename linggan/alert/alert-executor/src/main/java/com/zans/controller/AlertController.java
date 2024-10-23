package com.zans.controller;

import com.alibaba.fastjson.JSONObject;
import com.zans.commons.contants.Constants;
import com.zans.commons.utils.GetProperties2;
import com.zans.commons.utils.MyTools;
import com.zans.dao.AlertDao;
import com.zans.dao.impl.AlertDaoImpl;
import com.zans.service.IReceiveService;
import com.zans.vo.ApiResult;
import com.zans.vo.node.AlertRecordVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * @author pancm
 * @Title: alertmanage-server
 * @Description: 告警任务控制层
 * @Version:1.0.0
 * @Since:jdk1.8
 * @date 2020/9/14
 */
@RestController
@RequestMapping
@Slf4j
public class AlertController {

    @Autowired
    IReceiveService iReceiveService;

    private AlertDao dbDao = new AlertDaoImpl();

    /**
     * @return com.zans.vo.ApiResult
     * @Author pancm
     * @Description 告警记录添加
     * @Date 2020/9/14
     * @Param []
     **/
    @PostMapping("/alert/record/add")
    public ApiResult recordAdd(@RequestBody AlertRecordVO alertRecordVO) {
        log.info("请求的地址:{},请求的规则ruleId:{}",getIp(),alertRecordVO.getRuleId());
        return iReceiveService.record(alertRecordVO);
    }


    @GetMapping("/version")
    public JSONObject version(HttpServletRequest request){
        Map<String, String> map = GetProperties2.getAppSettings();
        JSONObject result = new JSONObject();
        result.put("git_branch", map.get("git_branch"));
        result.put("build_time", map.get("build_time"));
        result.put("git_commit", map.get("git_commit"));
        result.put("app_name", map.get("build_app"));
        result.put("profile", "local");
        result.put("version", Constants.VERSION);
        try {
          List<Map<String, Object>>  list = dbDao.query(DB_VERSION_SQL);
          if(MyTools.isNotEmpty(list)){
              result.put("db_version", list.get(0).get("version"));
              result.put("db_remark", list.get(0).get("remark"));
              result.put("db_name", "guard_scan");
          }
        } catch (Exception e) {
            log.error("查询数据库版本失败！",e);
        }
        return result;
    }


    @PostMapping("/check/dbconfig")
    public ApiResult checkDbConfig(@RequestBody JSONObject jsonObject) {
        log.info("请求的地址:{},请求的参数:{}",getIp(),jsonObject.toJSONString());
        String dbIp = jsonObject.getString("db_ip");
        String dbUser= jsonObject.getString("db_user");
        return ApiResult.success();
    }



    public String getIp() {
        return ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest().getRemoteAddr();
    }

    private static String DB_VERSION_SQL = "SELECT version,remark FROM db_version ORDER BY create_time DESC LIMIT 1";

}
