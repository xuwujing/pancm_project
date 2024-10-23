package com.zans.controller;

import com.alibaba.fastjson.JSONObject;
import com.zans.commons.contants.Constants;
import com.zans.commons.utils.GetProperties2;
import com.zans.commons.utils.MyTools;
import com.zans.contants.AlertConstants;
import com.zans.dao.AlertDao;
import com.zans.dao.impl.AlertDaoImpl;
import com.zans.service.IReceiveService;
import com.zans.vo.AlertRuleStrategyReqVO;
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
    public ApiResult addRecord(@RequestBody AlertRecordVO alertRecordVO) {
        log.info("告警记录添加！请求的地址:{},请求的规则strategyId:{},\r\n 请求的参数:{}",getIp(),alertRecordVO.getStrategyId(),alertRecordVO);
        return iReceiveService.addRecord(alertRecordVO);
    }

    @PostMapping("/alert/record/recover")
    public ApiResult recordRecover(@RequestBody AlertRecordVO alertRecordVO) {
        log.info("告警记录恢复! 请求的地址:{},请求的规则strategyId:{},\r\n请求的参数:{}",getIp(),alertRecordVO.getStrategyId(),alertRecordVO);
        return iReceiveService.recoverRecord(alertRecordVO);
    }


    /**
     * 修改策略，并且在job任务注册
     * @param
     * @return
     */
    @PostMapping("/alert/strategy/save")
    public ApiResult save(@RequestBody AlertRuleStrategyReqVO reqVO) {
        log.info("添加告警策略! 请求的地址:{},请求的规则ruleId:{}",getIp(),reqVO.getRule_id());
        return iReceiveService.strategySave(reqVO);
    }


    /**
     * 修改策略，并且在job任务注册
     * @param
     * @return
     */
    @PostMapping("/alert/strategy/status")
    public ApiResult status(@RequestBody AlertRuleStrategyReqVO reqVO) {
        log.info("添加告警策略! 请求的地址:{},请求的规则ruleId:{}",getIp(),reqVO.getRule_id());
        return iReceiveService.changeStatus(reqVO);
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


    @GetMapping("/strategyList")
    public ApiResult strategyList(){
        try {
            List<Map<String, Object>>  list = dbDao.query(AlertConstants.SELECT_STRATEGY_LIST);
            return ApiResult.success(list);
        } catch (Exception e) {
            log.error("查询接口策略列表失败！",e);
            return ApiResult.error("查询失败！");
        }

    }


    public String getIp() {
        return ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest().getRemoteAddr();
    }

    private static String DB_VERSION_SQL = "SELECT version,remark FROM db_version ORDER BY create_time DESC LIMIT 1";

}
