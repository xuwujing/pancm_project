package com.zans.portal.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.zans.base.config.EnumErrorCode;
import com.zans.base.service.impl.BaseServiceImpl;
import com.zans.base.vo.ApiResult;
import com.zans.base.vo.SelectVO;
import com.zans.portal.config.GlobalConstants;
import com.zans.portal.constants.PortalConstants;
import com.zans.portal.dao.LogOperationMapper;
import com.zans.portal.dao.SysModuleMapper;
import com.zans.portal.dao.SysRoleMapper;
import com.zans.portal.dao.TUserMapper;
import com.zans.portal.model.LogOperation;
import com.zans.portal.model.SysModule;
import com.zans.portal.model.SysRole;
import com.zans.portal.model.TUser;
import com.zans.portal.service.IConstantItemService;
import com.zans.portal.service.ILogOperationService;
import com.zans.base.util.StringHelper;
import com.zans.portal.util.RedisUtil;
import com.zans.portal.vo.chart.CircleUnit;
import com.zans.base.vo.PageResult;
import com.zans.portal.vo.log.*;
import com.zans.portal.vo.user.TUserVO;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.Resource;
import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.*;

import static com.zans.portal.config.GlobalConstants.MODULE_OP_LOG_MODULE;
import static com.zans.portal.constants.PortalConstants.*;
import static java.util.stream.Collectors.toList;

@Service
@Slf4j
public class LogOperationServiceImpl extends BaseServiceImpl<LogOperation> implements ILogOperationService {

    @Autowired
    IConstantItemService constantItemService;

    @Autowired
    LogOperationMapper logOperationMapper;

    @Autowired
    private SysModuleMapper sysModuleMapper;

    @Autowired
    private RedisUtil redisUtil;

    @Autowired
    private TUserMapper userMapper;

    @Autowired
    SysRoleMapper sysRoleMapper;

    private final static String TRACE_ID = "traceId";

    @Resource
    public void setLogOperationMapper(LogOperationMapper logOperationMapper) {
        super.setBaseMapper(logOperationMapper);
        this.logOperationMapper = logOperationMapper;
    }

    @Override
    public PageResult<OpLogRespVO> getPage(OpLogSearchVO req) {
        int pageNum = req.getPageNum();
        int pageSize = req.getPageSize();
        String orderBy = req.getSortOrder();

        Map<Object, String> logModuleMap = constantItemService.findItemsMapByDict(MODULE_OP_LOG_MODULE);

        Page page = PageHelper.startPage(pageNum, pageSize);
        List<OpLogRespVO> list = logOperationMapper.findLogList(req);
        for (OpLogRespVO vo : list) {
            vo.resetModuleNameByMap(logModuleMap);
        }

        return new PageResult<OpLogRespVO>(page.getTotal(), page.getResult(), pageSize, pageNum);
    }

    @Override
    public OpLogRespVO getOpLog(Integer id) {
        OpLogRespVO vo = logOperationMapper.getOpLog(id);

        Map<Object, String> logModuleMap = constantItemService.findItemsMapByDict(MODULE_OP_LOG_MODULE);

        String content = vo.getContent();
        Map<String, Object> contentMap = new LinkedHashMap<>();
        if (StringHelper.isNotBlank(content) && content.startsWith("{") && content.endsWith("}")) {
            Map<String, Object> map = JSONObject.parseObject(vo.getContent(), new TypeReference<Map<String, Object>>() {
            });
            Map<String, String> logKeyMap = GlobalConstants.OP_LOG_KEY_MAP;
            for (String key : map.keySet()) {
                Object value = map.get(key);
                String translateKey = logKeyMap.get(key);
                if (translateKey != null) {
                    contentMap.put(translateKey, value);
                } else {
                    contentMap.put(key, value);
                }
            }
            vo.setContentMap(contentMap);
            vo.resetModuleNameByMap(logModuleMap);
        }
        return vo;
    }


    @Override
    public List<CircleUnit> findGroupOfModule(Integer module) {
        return logOperationMapper.findGroupOfModule(module);
    }

    @Override
    public ApiResult loginLog(EsLoginLogRespVO esLoginLogRespVO) {
        Page<LogOperation52VO> page = PageHelper.startPage(esLoginLogRespVO.getPageNum(), esLoginLogRespVO.getPageSize());
        List<LogOperation52VO> logOperations = logOperationMapper.loginLog(esLoginLogRespVO);
        for (LogOperation52VO logOperation : logOperations) {
            try {
                ApiResult apiResult = JSONObject.parseObject(logOperation.getRepData(), ApiResult.class);
                logOperation.setResult(apiResult.getCode() == EnumErrorCode.SUCCESS.getCode() ? STATUS_SUCCESS : STATUS_FALSE);
                logOperation.setRepData(apiResult.getCode() == EnumErrorCode.SUCCESS.getCode() ? STATUS_LOGIN_SUCCESS : apiResult.getMessage());
            } catch (Exception e) {
                logOperation.setResult(STATUS_FALSE);
                logOperation.setRepData(STATUS_SYSTEM_FALSE);
            }
        }
        return ApiResult.success(new PageResult<>(page.getTotal(), logOperations, esLoginLogRespVO.getPageSize(), esLoginLogRespVO.getPageNum()));
    }

    @Override
    public ApiResult operationLog(EsOperationLogRespVO esOperationLogRespVO) {
        Page<LogOperation52VO> page = PageHelper.startPage(esOperationLogRespVO.getPageNum(), esOperationLogRespVO.getPageSize());
        List<LogOperation52VO> logOperations = logOperationMapper.operationLog(esOperationLogRespVO);
        return ApiResult.success(new PageResult<>(page.getTotal(), logOperations, esOperationLogRespVO.getPageSize(), esOperationLogRespVO.getPageNum()));
    }

    @Override
    public ApiResult init() {
        Map<String, List<SelectVO>> data = new HashMap<>();
        //1.operationType
        List<SelectVO> resultOpType = new ArrayList<>();
        Field[] declaredFields = PortalConstants.class.getDeclaredFields();
        Integer index = 1;
        for (Field declaredField : declaredFields) {
            if (declaredField.getName().startsWith("PORTAL_LOG_OPERATION")) {
                try {
                    resultOpType.add(new SelectVO(index++, declaredField.get("").toString()));
                } catch (IllegalAccessException illegalAccessException) {
                    log.error(illegalAccessException.toString());
                }
            }
        }
        data.put("operationType", resultOpType);

        //2.module
        List<SelectVO> resultModule = new ArrayList<>();
        index = 1;
        for (int i = 0; i < declaredFields.length; i++) {
            if (declaredFields[i].getName().startsWith("PORTAL_MODULE")){
                try {
                    resultModule.add(new SelectVO(index++, declaredFields[i].get("").toString()));
                } catch (IllegalAccessException illegalAccessException) {
                    log.error(illegalAccessException.toString());
                }
            }
        }
        data.put("module", resultModule);

        //3.roleName
        List<SysRole> sysRoles = sysRoleMapper.selectAll();
        List<SelectVO> resultRoleName = new ArrayList<>();
        for (SysRole sysRole : sysRoles) {
            resultRoleName.add(new SelectVO(sysRole.getRoleId(), sysRole.getRoleName()));
        }
        data.put("roleName", resultRoleName);
        return ApiResult.success(data);
    }


    @Override
    public ApiResult onLineList(TUserVO tUserVO) {
        List<TUserVO> result = new ArrayList<>();
        String prefix = ON_LINE_USER + "*";
        List<Object> allValue = redisUtil.getAllValue(prefix);
        for (Object o : allValue) {
            boolean putIn = true;
            try {
                TUserVO tUser = JSONObject.parseObject(o.toString(), TUserVO.class);
                if (StringUtils.isEmpty(tUserVO.getUserName()) && !StringUtils.isEmpty(tUserVO.getRoleName())) {
                    if (!tUser.getRoleName().contains(tUserVO.getRoleName())) {
                        putIn = false;
                    }
                } else if (!StringUtils.isEmpty(tUserVO.getUserName()) && StringUtils.isEmpty(tUserVO.getRoleName())) {
                    if (!tUser.getUserName().contains(tUserVO.getUserName())) {
                        putIn = false;
                    }
                } else if (!StringUtils.isEmpty(tUserVO.getUserName()) && !StringUtils.isEmpty(tUserVO.getRoleName())) {
                    if (!tUser.getUserName().contains(tUserVO.getUserName())) {
                        putIn = false;
                    }
                    if (!tUser.getRoleName().contains(tUserVO.getRoleName())) {
                        putIn = false;
                    }
                }
                if (putIn) {
                    tUser.setNowIp(((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest().getRemoteAddr());
                    result.add(tUser);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        List<TUserVO> finalResult = getData(tUserVO.getPageNum(), tUserVO.getPageSize(), result);
        return ApiResult.success(new PageResult<>(result.size(), finalResult, tUserVO.getPageSize(), tUserVO.getPageNum()));
    }

    public List<TUserVO> getData(Integer pageNum, Integer pageSize, List<TUserVO> list) {
        Integer newPageNum = (pageNum - 1) * pageSize;
        Integer newPageSize = pageNum * pageSize >= list.size() ? list.size() : pageNum * pageSize;
        return list.subList(newPageNum, newPageSize);
    }

    @Override
    public ApiResult heartbeat(Integer userId) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        TUser tUser = new TUser();
        tUser.setId(userId);
        log.info("id:{}",userId);
        tUser = userMapper.selectByPrimaryKey(tUser);
        TUserVO tUserVO = new TUserVO();
        BeanUtils.copyProperties(tUser, tUserVO);
        tUserVO.setLoginTime(simpleDateFormat.format(new Date()));
        SysRole sysRole = sysRoleMapper.selectRoleName(tUserVO);
        log.warn(JSONObject.toJSONString(sysRole));
        tUserVO.setRoleName(sysRole.getRoleName());
        if (redisUtil.hasKey(ON_LINE_USER + userId)) {
            //如果存在  只更新时间
            redisUtil.expire(ON_LINE_USER + userId, WEBSOCKET_SESSION_EXPIRE_TIME);
        } else {
            if (tUser != null) {
                redisUtil.set(ON_LINE_USER + userId, JSONArray.toJSON(tUserVO).toString(), WEBSOCKET_SESSION_EXPIRE_TIME);
            }
        }
        return ApiResult.success();
    }


    @Override
    public void statisticalOffline() {
        //1.找出所有在线用户
        String prefix = ON_LINE_USER + "*";
        List<Object> allValue = redisUtil.getAllValue(prefix);
        List<Integer> newLineUserId = new ArrayList<>();
        //2记录下当前在线用户
        for (Object o : allValue) {
            TUserVO tUser = JSONObject.parseObject(o.toString(), TUserVO.class);
            newLineUserId.add(tUser.getId());
        }
        Object onLineUser = redisUtil.get("onLineUser");
        List<Integer> oldOnLineUser = onLineUser == null ? new ArrayList<>() : JSONObject.parseObject(onLineUser.toString(), List.class);
        //3.找出离线用户 记录非正常登出方式 退出登录状态
        //日志为空 ->当前环境为最新 清空所有数据 K=onLineUser也为空  此时不记录登出
//        if (!CollectionUtils.isEmpty(logOperationMapper.selectAll())){
        List<Integer> offLineUser = oldOnLineUser.stream().filter(item -> !newLineUserId.contains(item)).collect(toList());
        for (Integer integer : offLineUser) {
            TUser tUser = new TUser();
            tUser.setId(integer);
            tUser = userMapper.selectByPrimaryKey(tUser);
            LogOperation newLogOperation = new LogOperation();
            if (!ObjectUtils.isEmpty(tUser)) {
                newLogOperation.setUserName(tUser.getUserName());
                LogOperation logByUserNameRecently = logOperationMapper.findLogByUserNameRecently(newLogOperation);
                LogOperation logOperation = new LogOperation();
                logOperation.setTraceId(logByUserNameRecently == null ? "" : logByUserNameRecently.getTraceId());
                logOperation.setUserName(tUser.getUserName());
                logOperation.setOpPlatform(OP_PLATFORM);
                logOperation.setModule(PORTAL_MODULE_LOGIN);
                logOperation.setOperation(LOG_OPERATION_USER_LOGOUT);
                // TODO
                logOperation.setClassName("com.zans.portal.service.impl.LogOperationServiceImpl");
                logOperation.setMethodName(PORTAL_MODULE_LOGIN);
                logOperation.setReqData(tUser.getUserName());
                logOperation.setRepData(JSONObject.toJSONString(ApiResult.success(STATUS_LOGOUT_SUCCESS)));
                logOperation.setFromIp(logByUserNameRecently == null ? "" : logByUserNameRecently.getFromIp());
                logOperation.setResult(STATUS_SUCCESS);
                logOperation.setCreateTime(new Date());
                logOperation.setUpdateTime(new Date());
                logOperationMapper.insert(logOperation);
            }
        }
//        }
        redisUtil.set("onLineUser", JSONArray.toJSON(newLineUserId).toString(), SCHDULED_TASK_TIME);
    }

    @Override
    public int updateByTraceId(LogOperation logOperation) {
        return logOperationMapper.updateByTraceId(logOperation);
    }
}
