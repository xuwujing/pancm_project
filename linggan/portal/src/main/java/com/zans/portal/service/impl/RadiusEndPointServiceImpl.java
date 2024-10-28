package com.zans.portal.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.zans.base.service.impl.BaseServiceImpl;
import com.zans.base.util.DateHelper;
import com.zans.base.util.MapBuilder;
import com.zans.base.util.StringHelper;
import com.zans.base.vo.ApiResult;
import com.zans.base.vo.PageResult;
import com.zans.base.vo.SelectVO;
import com.zans.base.vo.UserSession;
import com.zans.portal.config.GlobalConstants;
import com.zans.portal.config.PoolTaskConfig;
import com.zans.portal.dao.*;
import com.zans.portal.model.*;
import com.zans.portal.service.*;
import com.zans.portal.util.DBUtil;
import com.zans.portal.util.HttpHelper;
import com.zans.portal.util.RestTemplateHelper;
import com.zans.portal.util.TelnetUtil;
import com.zans.portal.vo.AliveHeartbeatVO;
import com.zans.portal.vo.AssetBaselineVO;
import com.zans.portal.vo.alert.AlertRecordReqVO;
import com.zans.portal.vo.arp.AssetRespVO;
import com.zans.portal.vo.arp.AssetSearchVO;
import com.zans.portal.vo.asset.AssetVO;
import com.zans.portal.vo.asset.req.AssetMapUpdateReqVO;
import com.zans.portal.vo.asset.req.AssetReqVO;
import com.zans.portal.vo.asset.resp.AssetMapRespVO;
import com.zans.portal.vo.chart.CircleUnit;
import com.zans.portal.vo.chart.CountUnit;
import com.zans.portal.vo.radius.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.InetAddress;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

import static com.zans.base.config.BaseConstants.VALID_MAC_LENGTH;
import static com.zans.portal.config.GlobalConstants.*;
import static com.zans.portal.constants.PortalConstants.QZ_PROBE_IP;

/**
 * @author yhj
 */
@Service
@Slf4j
public class RadiusEndPointServiceImpl extends BaseServiceImpl<RadiusEndpoint> implements IRadiusEndPointService {


    RadiusEndpointMapper endpointMapper;

    @Autowired
    ILogOperationService logOperationService;

    @Autowired
    ISysConstantService sysConstantService;

    @Autowired
    RestTemplateHelper restTemplateHelper;


    @Autowired
    IConstantItemService constantItemService;

    @Autowired
    ISysConstantService constantService;

    @Autowired
    IAreaService areaService;

    @Autowired
    IIpService ipService;

    @Autowired
    private IDeviceTypeService deviceTypeService;

    @Autowired
    PoolTaskConfig poolTaskConfig;

    @Autowired
    IAlertRuleService iAlertRuleService;

    @Autowired
    IRadiusEndPointProfileService iRadiusEndPointProfileService;

    @Autowired
    protected HttpHelper httpHelper;

    @Autowired
    private IAssetService assetService;

    @Autowired
    private SysSwitcherMapper sysSwitcherMapper;

    @Autowired
    IAssetBaselineVersionService assetBaselineVersionService;
    @Autowired
    IAssetBaselineService assetBaselineService;

    @Autowired
    IAlertRuleService alertRuleService;

    @Autowired
    private AssetMapper assetMapper;

    @Resource
    private SysConstantMapper sysConstantMapper;

    @Resource
    private AliveHeartbeatMapper aliveHeartbeatMapper;


    @Resource
    private TraceDataDao traceDataDao;


    public static String baseUrl = "http://%s:22171/scan/fping/stats?str_ip_list=";


    @Resource
    public void setEndpointMapper(RadiusEndpointMapper endpointMapper) {
        super.setBaseMapper(endpointMapper);
        this.endpointMapper = endpointMapper;
    }

    @Override
    public PageResult<EndPointViewVO> getEndPointPage(EndPointReqVO reqVO) {
        Page page = PageHelper.startPage(reqVO.getPageNum(), reqVO.getPageSize());
        List<EndPointViewVO> list = null;
        Integer policy = reqVO.getAccessPolicy();
        if (policy != null) {
            if (!isPolicyValid(policy)) {
                return null;
            }
        }
        if (policy == null || policy == GlobalConstants.RADIUS_ACCESS_POLICY_PASS) {
            list = endpointMapper.findEndPointList(reqVO);
        } else if (policy == RADIUS_ACCESS_POLICY_QZ) {
            list = endpointMapper.findEndPointList(reqVO);
        }  else if (policy == RADIUS_ACCESS_POLICY_BLOCK) {
            list = endpointMapper.findBlockEndPointList(reqVO);
        } else {
            list = endpointMapper.findEndPointList(reqVO);
        }

        Map<Object, String> aliveMap = constantItemService.findItemsMapByDict(MODULE_ARP_ALIVE);
        Map<String, Object> map = new HashMap<>();
        Map<String, Object> map2 = new HashMap<>();
        String[] kvs = new String[reqVO.getPageSize()];
        String[] kvs2 = new String[reqVO.getPageSize()];
        for (int i = 0; i < list.size(); i++) {
            EndPointViewVO vo = list.get(i);
            vo.setAliveNameByMap(aliveMap);
            if (vo.getAlive() != null) {
                vo.setAliveName(vo.getAlive() == 1 ? "已入网" : "未入网");
            }
            if (policy == null || policy == GlobalConstants.RADIUS_ACCESS_POLICY_PASS) {
                vo.setAliveStatusName(vo.getAlive() == null ? "" : (vo.getAlive() == 1 ? "在线" : "离线"));
            }
            vo.setAlertRecord(0);
            kvs[i] = vo.getMac();
            kvs2[i] = vo.getIpAddr();
        }
        map.put("kvs", kvs);
        map2.put("kvs", kvs2);
        // 2020-10-7 和北傲确认，在检疫区/在线设备/阻断设备列表项增加是否显示告警记录按钮,如果触发了就显示
        List<String> kvsList = iAlertRuleService.getRecordByKeywordValues(map);
        List<String> kvsList2 = iAlertRuleService.getRecordByIp(map2);
        for (EndPointViewVO vo : list) {
            for (String s : kvsList) {
                if (vo.getMac().equals(s)) {
                    vo.setAlertRecord(1);
                }
            }
            for (String s : kvsList2) {
                if (StringUtils.isEmpty(vo.getIpAddr())) {
                    continue;
                }
                if (vo.getIpAddr().equals(s)) {
                    vo.setAlertRecord(1);
                }
            }
        }


        return new PageResult<EndPointViewVO>(page.getTotal(), page.getResult(), reqVO.getPageSize(),
                reqVO.getPageNum());
    }

    private boolean isPolicyValid(Integer policy) {
        //2021-6-26 和北辰确认，存在policy大于2的数据，因此去掉一个判断
        if (policy == null || policy < RADIUS_ACCESS_POLICY_BLOCK) {
            return false;
        } else {
            return true;
        }
    }

    @Override
    public PageResult<EndPointViewVO> getQzEndPointPage(QzReqVO reqVO) {
        String orderBy = reqVO.getSortOrder();
        Page page = PageHelper.startPage(reqVO.getPageNum(), reqVO.getPageSize());

        List<EndPointViewVO> list = endpointMapper.findQzEndPointList(reqVO);
        Map<Object, String> aliveMap = constantItemService.findItemsMapByDict(MODULE_ARP_ALIVE);
        Map<String, Object> map = new HashMap<>();
        Map<String, Object> map2 = new HashMap<>();
        String[] kvs = new String[reqVO.getPageSize()];
        String[] kvs2 = new String[reqVO.getPageSize()];
        for (int i = 0; i < list.size(); i++) {
            EndPointViewVO vo = list.get(i);
            vo.setAliveNameByMap(aliveMap);
            if (vo.getAlive() != null) {
                vo.setAliveName(vo.getAlive() == 1 ? "已入网" : "未入网");
            }
            vo.setAlertRecord(0);
            //设置漏扫放行状态默认值0
            vo.setHitStatus(0);
            //2020-12-16 列表页增加vlan配置错误的提示
            String vlan = vo.getVlan();
            String curVlan = vo.getCurVlan();
            if (!StringUtils.isEmpty(vlan) && !StringUtils.isEmpty(curVlan)) {
                if (!vlan.equals(curVlan)) {
                    vo.setIsVlanErr(1);
                }
            }

            kvs[i] = vo.getMac();
            kvs2[i] = vo.getIpAddr();
        }
        map.put("kvs", kvs);
        map2.put("kvs", kvs2);
        // 2020-10-7 和北傲确认，在检疫区/在线设备/阻断设备列表项增加是否显示告警记录按钮,如果触发了就显示
        List<String> kvsList = iAlertRuleService.getRecordByKeywordValues(map);
        List<String> kvsList2 = iAlertRuleService.getRecordByIp(map2);
        for (EndPointViewVO vo : list) {
            for (String s : kvsList) {
                if (vo.getMac().equals(s)) {
                    vo.setAlertRecord(1);
                }
            }
            for (String s : kvsList2) {
                if (StringUtils.isEmpty(vo.getIpAddr())) {
                    continue;
                }
                if (vo.getIpAddr().equals(s)) {
                    vo.setAlertRecord(1);
                }
            }
        }

        return new PageResult<EndPointViewVO>(page.getTotal(), page.getResult(), reqVO.getPageSize(),
                reqVO.getPageNum());
    }


    @Override
    public RadiusEndpoint findByMacMin(String mac) {
        String macMin = StringHelper.convertMacToMin(mac);
        return endpointMapper.findEndpointByMacMin(macMin);
    }

    @Override
    public QzRespVO findQzById(Integer id) {

        QzRespVO vo = endpointMapper.findQzById(id);
        if (vo != null) {
            Map<Object, String> aliveMap = constantItemService.findItemsMapByDict(MODULE_ARP_ALIVE);
            vo.setAliveNameByMap(aliveMap);
            if (StringUtils.isEmpty(vo.getVlan())) {
                vo.setVlan("-");
            }
        }
        return vo;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public ApiResult judge(Integer id, Integer policy, String authMark, UserSession userSession) {
        String radApiHost = constantService.getRadApi();
        if (radApiHost == null || !radApiHost.startsWith("http")) {
            return ApiResult.error("系统常量rad_api为空，请联系系统管理员");
        }
        RadiusEndpointProfile radiusEndpointProfile = iRadiusEndPointProfileService.getOne(id);
        if (radiusEndpointProfile == null) {
            return ApiResult.error("改值不存在！");
        }
        String baseIp = radiusEndpointProfile.getCurIpAddr();
        RadiusEndpoint endpoint = this.getById(id);
        String mac = endpoint.getMac();
        Integer deleteStatus = 0;
        //检疫的时候 baseIp不变，放行的时候去curIpAddr，阻断的时候清空该值
        if (policy == 1) {
            baseIp = endpoint.getBaseIp();
        }
        //2021-6-28 和北辰确认，阻断不清除baseIp阻断数据
//        if (policy == 0) {
//            baseIp="";
//        }
        // 强行DM
        // COA 很多交换机不支持
        if (!isPolicyValid(policy)) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return ApiResult.error("参数异常#" + policy);
        }

        endpoint.setAuthPerson(userSession.getNickName());

        this.changeBaselineAndDispelAlert(policy, authMark, radiusEndpointProfile, endpoint);

        String edgeMode = constantService.getEdgeAccessMode();
        if (!"acl".equals(edgeMode)) {
            radiusJudge(policy, radApiHost, mac, deleteStatus, baseIp);
            // if (radApiData.getCode() != CODE_SUCCESS) {
            // TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            // log.info("dm error#{}, {}, {}", uri, radApiData.getCode(),
            // radApiData.getMessage());
            // }
        } else {
            String uri = String.format("/acl?mac=%s&policy=%s", mac, policy);
            ApiResult radApiData = executeRadApiRequest(radApiHost, uri);
            // if (radApiData.getCode() != CODE_SUCCESS) {
            // TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            // log.info("acl error#{}, {}, {}", uri, radApiData.getCode(),
            // radApiData.getMessage());
            // }
        }


        //2020-12-23 新增逻辑,将审核的操作进行写入
        RadiusEndpointLog radiusEndpointLog = new RadiusEndpointLog();
        radiusEndpointLog.setDataSource(1);
        radiusEndpointLog.setAccessPolicy(policy);
        radiusEndpointLog.setDeleteStatus(deleteStatus);
        radiusEndpointLog.setMac(StringUtils.trimAllWhitespace(mac));
        endpointMapper.saveRadiusEndpointLog(radiusEndpointLog);

        // 记录日志

        return ApiResult.success();
    }

    /**
     * @return
     * @Author beiming
     * @Description 基线更新，告警消除  //https://fgr44sks34.feishu.cn/docs/doccnVpcudUnBGuFgQBbURDtxob#WhISWn
     * @Date 7/10/21
     * @Param
     **/
    public void changeBaselineAndDispelAlert(Integer policy, String authMark, RadiusEndpointProfile profile, RadiusEndpoint endpoint) {
        String oldBaseIp = endpoint.getBaseIp();
        endpoint.setAuthMark(authMark);
        endpoint.setAuthTime(new Date());
        endpoint.setAccessPolicy(policy);
        if (policy.intValue() >= 2) {

            endpoint.setBaseIp(profile.getCurIpAddr());
            AssetBaselineVO ipBaselineVO = assetBaselineService.getByIp(profile.getCurIpAddr());
            String pointName = "";
            if (StringHelper.isBlank(oldBaseIp)) {
                if (ipBaselineVO == null) {
                    log.info("新设备入网,建立基线ip#{},mac#{}", profile.getCurIpAddr(), profile.getMac());
                    //新设备入网 建立基线
                    AssetBaseline addBaseline = new AssetBaseline();
                    addBaseline.setIpAddr(profile.getCurIpAddr());
                    addBaseline.setMac(profile.getMac());
                    addBaseline.setModelDes(profile.getCurModelDes());
                    addBaseline.setBrandName(profile.getBrandName());
                    addBaseline.setDeviceType(profile.getCurDeviceType());
                    addBaseline.setServerOs(profile.getCurServerOs());
                    addBaseline.setOpenPort(profile.getCurOpenPort());
                    addBaseline.setNasIp(profile.getCurNasIpAddress());
                    addBaseline.setNasPortId(profile.getCurNasPortId());
                    addBaseline.setVlan(profile.getCurVlan());
                    addBaseline.setBindStatus(ASSET_BASELINE_BIND);
                    addBaseline.setRemark(authMark);
                    addBaseline.setCreator(endpoint.getAuthPerson());
                    assetBaselineService.saveSelective(addBaseline);
                } else {
                    //导入的基线数据或者基线绑定解除后的
                    log.info("导入的基线数据或者基线绑定解除后的ip#{},mac#{}", profile.getCurIpAddr(), profile.getMac());
                    pointName = ipBaselineVO.getPointName();
                    updateBaseline(authMark, profile, endpoint, ipBaselineVO);
                }
                //消除告警
                AlertRecordReqVO alertRecordReqVO = new AlertRecordReqVO();
                List<Map<String, Object>> mapList = new ArrayList<>();
                Map<String, Object> map = new HashMap<>();
                // 警告：检疫区有新设备入网，IP：{ip_addr}, MAC：{username}，入网路径：{sw_point_name}-{nas_ip_address}，注意检查！
                map.put("ip_addr", profile.getCurIpAddr());
                map.put("mac", profile.getMac());
                map.put("sw_point_name", pointName);
                map.put("nas_ip_address", profile.getCurNasIpAddress());
                mapList.add(map);
                //全局检疫区有新设备入网警告
                alertRecordReqVO.setStrategyId(7L);
                alertRecordReqVO.setMapList(mapList);
                alertRuleService.remoteAlertRecordRecover(alertRecordReqVO);
            } else {
                if (!oldBaseIp.equals(profile.getCurIpAddr())) {//比较cur_ip与base_ip是否相等  否相等
                    log.info("基线更新 ip#{},mac#{}", profile.getCurIpAddr(), profile.getMac());
                    if (ipBaselineVO == null) {
                        //增加原base_ip历史基线  删除原base_ip基线，新增cur_ip新基线，消除告警,更新base_ip为cur_ip
                        AssetBaselineVO oldBaselineVO = assetBaselineService.getByIp(oldBaseIp);
                        addVersionDeleteOldAddNewBaseline(authMark, profile, endpoint, oldBaselineVO);
                        //  消除告警
                        recoverIpClashAlert(oldBaseIp, oldBaselineVO.getMac(), profile.getMac());
                    } else {
                        String curIpBaselineUsername = ipBaselineVO.getMac();
                        AssetBaselineVO oldBaselineVO = assetBaselineService.getByIp(oldBaseIp);
                        //新增base_ip历史基线
                        addVersionByBaseline(endpoint.getAuthPerson(), oldBaselineVO);
                        //解绑base_ip原基线
                        assetBaselineService.unbindByIpAddr(oldBaseIp);
                        if (curIpBaselineUsername.equals(profile.getMac())) {
                            //新增base_ip历史基线 解绑base_ip原基线，
                            // 更新cur_ip基线记录 消除告警 更新base_ip未cur_ip
                            updateBaseline(authMark, profile, endpoint, ipBaselineVO);
                        } else {
                            //新增base_ip历史基线,解绑base_ip原基线，
                            // 新增cur_ip历史基线,删除cur_ip原基线,新增cur_ip基线，消除告警,更新base_ip为cur_ip
                            AssetBaselineVO oldCurIpBaselineVO = assetBaselineService.getByIp(profile.getCurIpAddr());
                            addVersionDeleteOldAddNewBaseline(authMark, profile, endpoint, oldCurIpBaselineVO);
                        }
                        // 消除告警
                        recoverIpClashAlert(oldBaseIp, oldBaselineVO.getMac(), profile.getMac());
                    }
                }
            }
        }
        this.update(endpoint);
    }

    /**
     * 异步批量审批
     *
     * @param id
     * @param policy
     * @param authMark
     * @param userSession
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public ApiResult asynchronousJudge(Integer id, Integer policy, String authMark, UserSession userSession) {
        //调用接口
        String radApiHost = constantService.getJudgeApi();
        if (radApiHost == null || !radApiHost.startsWith("http")) {
            return ApiResult.error("系统常量rad_api为空，请联系系统管理员");
        }
        String username = userSession.getUserName();
        if (null == id) {
            return ApiResult.error("id为空，请联系系统管理员！");
        }
        if (null == policy) {
            return ApiResult.error("policy为空，请联系系统管理员！");
        }
        if (StringUtils.isEmpty(username)) {
            return ApiResult.error("用户信息为空，请联系系统管理员！");
        }

        String uri = "/api/radius/endpoint/judge?id=" + id + "&policy=" + policy + "&authMark=" + authMark + "&username=" + username;
        try {
            ApiResult judgeApiData = executeRadApiRequest(radApiHost, uri);
        } catch (Exception e) {
            return ApiResult.error("调用失败！");
        }

        return ApiResult.success();
    }

    /**
     * @return
     * @Author beiming
     * @Description 全局IP冲突告警  消除
     * @Date 7/12/21
     * @Param
     **/
    private void recoverIpClashAlert(String oldBaseIp, String baseMac, String curMac) {
        AlertRecordReqVO alertRecordReqVO = new AlertRecordReqVO();
        List<Map<String, Object>> mapList = new ArrayList<>();
        Map<String, Object> map = new HashMap<>();
        // 警告: {ip_addr} 地址接入了不同的设备,原设备:{base_mac},当前设备:{cur_mac}!
        map.put("ip_addr", oldBaseIp);
        map.put("base_mac", baseMac);
        map.put("cur_mac", curMac);
        mapList.add(map);
        //全局IP冲突告警
        alertRecordReqVO.setStrategyId(9L);
        alertRecordReqVO.setMapList(mapList);
        alertRuleService.remoteAlertRecordRecover(alertRecordReqVO);
    }

    private void updateBaseline(String authMark, RadiusEndpointProfile profile, RadiusEndpoint endpoint, AssetBaselineVO ipBaselineVO) {
        AssetBaseline updateBaseline = new AssetBaseline();
        BeanUtils.copyProperties(ipBaselineVO, updateBaseline);

        updateBaseline.setMac(profile.getMac());
        updateBaseline.setModelDes(profile.getCurModelDes());
        updateBaseline.setBrandName(profile.getBrandName());
        updateBaseline.setDeviceType(profile.getCurDeviceType());
        updateBaseline.setServerOs(profile.getCurServerOs());
        updateBaseline.setOpenPort(profile.getCurOpenPort());
        updateBaseline.setNasIp(profile.getCurNasIpAddress());
        updateBaseline.setNasPortId(profile.getCurNasPortId());
//                updateBaseline.setVlan();
        updateBaseline.setBindStatus(ASSET_BASELINE_BIND);
        updateBaseline.setRemark(authMark);
        updateBaseline.setCreator(endpoint.getAuthPerson());
        assetBaselineService.updateSelective(updateBaseline);
    }

    private void addVersionDeleteOldAddNewBaseline(String authMark, RadiusEndpointProfile profile, RadiusEndpoint endpoint, AssetBaselineVO deleteBaseline) {
        addVersionByBaseline(endpoint.getAuthPerson(), deleteBaseline);

        assetBaselineService.deleteById(deleteBaseline.getId());

        AssetBaseline addBaseline = new AssetBaseline();
        BeanUtils.copyProperties(deleteBaseline, addBaseline);
        addBaseline.setId(null);
        addBaseline.setIpAddr(profile.getCurIpAddr());
        addBaseline.setMac(profile.getMac());
        addBaseline.setModelDes(profile.getCurModelDes());
        addBaseline.setBrandName(profile.getBrandName());
        addBaseline.setDeviceType(profile.getCurDeviceType());
        addBaseline.setServerOs(profile.getCurServerOs());
        addBaseline.setOpenPort(profile.getCurOpenPort());
        addBaseline.setNasIp(profile.getCurNasIpAddress());
        addBaseline.setNasPortId(profile.getCurNasPortId());
        addBaseline.setBindStatus(ASSET_BASELINE_BIND);
        addBaseline.setRemark(authMark);
        addBaseline.setCreator(endpoint.getAuthPerson());
        assetBaselineService.saveSelective(addBaseline);
    }

    private void addVersionByBaseline(String authPerson, AssetBaselineVO deleteBaseline) {
        if (deleteBaseline == null) {
            throw new RuntimeException("历史基线记录不存在");
        }
        AssetBaselineVersion addBaselineVersion = new AssetBaselineVersion();
        BeanUtils.copyProperties(deleteBaseline, addBaselineVersion);
        addBaselineVersion.setAssetBaselineId(deleteBaseline.getId());
        addBaselineVersion.setId(null);
        //本次创建人
        addBaselineVersion.setCreator(authPerson);
        addBaselineVersion.setVersionCreator(deleteBaseline.getCreator());
//        addBaselineVersion.setVersionCreateTime(deleteBaseline.getCreateTime());
        assetBaselineVersionService.saveSelective(addBaselineVersion);
    }

    @Override
    public void radiusJudge(Integer policy, String radApiHost, String mac, Integer deleteStatus, String baseIp) {
        String module = "endpoint";
        // 调用rad_api接口
        //2021-6-25 合北辰确认 新增baseIp
        String uri = String.format("/sync/%s?mac=%s&delete_status=%s&policy=%d&base_ip=%s", module, mac, deleteStatus,
                policy, baseIp);

        // 2021-6-11 根据不同的policy选择不同的uri

        String uri2 = "";
        String radApi = "";
        //如果是阻断，则选择radApiBlock
        if (policy == 0) {
            radApi = getRadApi(RAD_API_BLOCK);
        } else {
            radApi = getRadApi(RAD_API_CHANGE);
        }
        uri2 = radApi + "?mac=" + mac + "&policy=" + policy;

        log.info("{}|远程操作,请求用户:{},请求ip:{},内容1:{},内容2:{}|", getHeadId(), getUser(), getIp(), radApiHost + uri, radApiHost + uri2);
        poolTaskConfig.executeRadApiRequest2(radApiHost, uri, radApiHost, uri2);
    }

    private String getRadApi(String radApi) {
        SysConstant blockSysConstant = sysConstantService.findConstantByKey(radApi);
        String radApiBlockUri = "/rad/dm";
        if (blockSysConstant != null) {
            radApiBlockUri = blockSysConstant.getConstantValue();
        }
        return radApiBlockUri;
    }

    @Override
    public ApiResult doDm(String mac, Integer policy) {
        //调用接口
        String radApiHost = constantService.getJudgeApi();
        if (radApiHost == null || !radApiHost.startsWith("http")) {
            return ApiResult.error("系统常量rad_api为空，请联系系统管理员");
        }
        if (null == mac) {
            return ApiResult.error("mac为空，请联系系统管理员！");
        }
        if (null == policy) {
            return ApiResult.error("policy为空，请联系系统管理员！");
        }
        String uri = "/api/radius/endpoint/dm?policy=" + policy + "&mac=" + mac;
        try {
            ApiResult judgeApiData = executeRadApiRequest(radApiHost, uri);
        } catch (Exception e) {
            return ApiResult.error("调用失败");
        }

        return ApiResult.success();

       /* String radApiHost = constantService.getRadApi();
        if (radApiHost == null || !radApiHost.startsWith("http")) {
            return ApiResult.error("系统常量rad_api为空，请联系系统管理员");
        }

        String edgeMode = constantService.getEdgeAccessMode();
        if (!"acl".equals(edgeMode)) {
            String uri = "/rad/dm?mac=" + mac + "&policy=" + policy;
            ApiResult radApiData = executeRadApiRequest(radApiHost, uri);
            return radApiData;
        } else {
            return ApiResult.error("ACL模式不支持DM操作，请联系系统管理员");
        }*/

    }

    public ApiResult executeRadApiRequest(String host, String uri) {
        log.info("{}|远程操作,请求用户:{},请求ip:{},内容:{}|", getHeadId(), getUser(), getIp(), host + uri);
        return poolTaskConfig.executeRadApiRequest(host, uri);
    }


    private void remoteSyncEndpoint(String host, String mac, Integer deleteStatus, Integer accessPolicy) {
        if (host == null) {
            host = constantService.getRadApi();
            if (host == null) {
                log.error("系统常量rad_api为空，请联系系统管理员");
                return;
            }
        }
        String module = "endpoint";
        // 调用rad_api接口
        String uri = String.format("/sync/%s?mac=%s&delete_status=%s&policy=%d", module, mac, deleteStatus,
                accessPolicy);
        executeRadApiRequest(host, uri);
    }


    @Override
    public List<CircleUnit> getAssetTotal() {
        List<CircleUnit> report = endpointMapper.getAssetTotal();
        BigDecimal online = BigDecimal.ZERO;
        BigDecimal total = BigDecimal.ZERO;
        BigDecimal stop = BigDecimal.ZERO;
        for (CircleUnit circleUnit : report) {
            if ("online".equals(circleUnit.getName())) {
                online = new BigDecimal(circleUnit.getVal().toString());
            }
            if ("total".equals(circleUnit.getName())) {
                total = new BigDecimal(circleUnit.getVal().toString());
            }
            if ("stop".equals(circleUnit.getName())) {
                stop = new BigDecimal(circleUnit.getVal().toString());
            }
        }

        /**
         * @description: 北傲与肖总确认 离线 = 总数-在线 edit by ns_wang 2020/9/16 15:02 2020-10-23
         *               修改逻辑: 离线=总数-在线-停用
         */
        BigDecimal offlineNew = total.subtract(online).subtract(stop);
        for (CircleUnit circleUnit : report) {
            if ("offline".equals(circleUnit.getName())) {
                circleUnit.setVal(offlineNew);
            }
        }

        // 2020-9-23 增加逻辑，统计的数据为0，那么不用计算直接设置为0
        BigDecimal onlineRate = new BigDecimal(0);

        if (online.intValue() != 0) {
            // 计算资产概率 在线率
            onlineRate = online.divide(total, 4, RoundingMode.HALF_UP);
        }

        CircleUnit circleUnit1 = new CircleUnit();
        circleUnit1.setName("onlineRate");
        circleUnit1.setChineName("在线率");
        circleUnit1.setVal(onlineRate);
        report.add(circleUnit1);
        return report;
    }

    /**
     * @return java.util.List<com.zans.portal.vo.chart.CircleUnit>
     * @Author pancm
     * @Description 准入统计
     * @Date 2020/10/24
     * @Param []
     **/
    @Override
    public List<CircleUnit> getAccessTotal() {
        List<CircleUnit> report = endpointMapper.getAccessTotal();
        BigDecimal auth = BigDecimal.ZERO;
        BigDecimal total = BigDecimal.ZERO;
        for (CircleUnit circleUnit : report) {
            if ("auth".equals(circleUnit.getName())) {
                auth = new BigDecimal(circleUnit.getVal().toString());
            }
            if ("total".equals(circleUnit.getName())) {
                total = new BigDecimal(circleUnit.getVal().toString());
            }

        }

        BigDecimal authRate = new BigDecimal(0);
        if (total.intValue() != 0) {
            authRate = auth.divide(total, 4, RoundingMode.HALF_UP);
        }
        CircleUnit circleUnit2 = new CircleUnit();
        circleUnit2.setName("authRate");
        circleUnit2.setChineName("准入率");
        circleUnit2.setVal(authRate);
        report.add(circleUnit2);
        return report;
    }


    @Override
    public List<CountUnit> summary() {
        return endpointMapper.summary();
    }

    @Override
    public List<CountUnit> device() {
        List<CountUnit> result = new ArrayList<>();
        List<CountUnit> device = endpointMapper.findByDevice();
        Long val = 0L;
        for (int i = 0; i < device.size(); i++) {
            if (i < 20) {
                result.add(device.get(i));
            } else {
                val += (Long) device.get(i).getVal();
            }
        }
        CountUnit unit = new CountUnit();
        unit.setCountName("其他");
        unit.setVal(val);
        result.add(unit);

        return result;
    }


    @Override
    public List<CircleUnit> getCountOfRiskType(String dayStart) {
        return endpointMapper.getCountOfRiskType(dayStart);
    }


    @Override
    public List<AssetMapRespVO> assetMapList(AssetReqVO reqVO) {
        List<String> buildTypes = reqVO.getBuildTypes();
        if (buildTypes != null && buildTypes.size() > 0) {
            // 海康=1期 广电1 其他二期 电信2
            if (buildTypes.contains("1") && !buildTypes.contains("2")) {
                reqVO.setBrandName("海康");
            }
            if (!buildTypes.contains("1") && buildTypes.contains("2")) {
                reqVO.setBrandNameIsNoHaiKang("海康");
            }
        }

        return endpointMapper.assetMapList(reqVO);
    }


    @Override
    public ApiResult updateAssetMap(AssetMapUpdateReqVO searchVO) {
        // IpAll ipAll = ipService.getById(searchVO.getId());
        Asset asset = assetService.getById(searchVO.getId());
        if (asset == null) {
            return ApiResult.error("数据不存在!");
        }
        Asset updateVo = new Asset();
        if (searchVO.getDepartmentId() == null || searchVO.getDepartmentId().intValue() == 0) {
            // 如果未设置部门 默认卧龙派出所
            searchVO.setDepartmentId(6);
        }
        BeanUtils.copyProperties(searchVO, updateVo);
        updateVo.setLongitude(searchVO.getLongitude());
        updateVo.setLatitude(searchVO.getLatitude());
        updateVo.setMapCatalogueId(searchVO.getDepartmentId());

        /**
         * @description: 改为编辑asset表 edit by ns_wang 2020/11/2 15:24
         */
        // ipService.updateSelective(updateVo);
        assetService.updateSelective(updateVo);
        return ApiResult.success();
    }


    @Override
    public QzViewRespVO findBaseByCurMac(String username) {

        return endpointMapper.findBaseByCurMac(username);
    }

    @Override
    public QzViewRespVO findCurByCurMac(String username) {
        return endpointMapper.findCurByCurMac(username);
    }

    @Override
    public List<AssetRespVO> findAssetsForList(AssetSearchVO asset) {
        return endpointMapper.findAssetsForList(asset);
    }

    @Override
    public List<AssetRespVO> getAssetNewListPage(AssetSearchVO asset) {
         return endpointMapper.getAssetNewListPage(asset);
        }

    @Override
    public void checkAlive(List<String> list, PageResult<EndPointViewVO> pageResult) throws Exception {
        if (CollectionUtils.isEmpty(list)) {
            return;
        }
        SysConstant sysConstant = new SysConstant();
        sysConstant.setConstantKey(QZ_PROBE_IP);
        sysConstant = sysConstantMapper.selectOne(sysConstant);
        if (sysConstant == null) {
            throw new Exception("基础数据缺失!");
        }
        String[] probeIpArr = sysConstant.getConstantValue().split(",");
        String finalUrl = String.format(baseUrl, probeIpArr[0]);
        String finalParam = org.apache.commons.lang3.StringUtils.join(list.toArray(), " ");
        System.out.println(finalParam);
        ApiResult forObject = restTemplateHelper.getForObject(finalUrl + finalParam);
        List<Object> data = (List<Object>) forObject.getData();
        Map<String, Integer> result = new HashMap<>(data.size());
        for (Object datum : data) {
            String substring = datum.toString().substring(1, datum.toString().length() - 1);
            String ipAddr = substring.split(",")[0].substring(1, substring.split(",")[0].length() - 1);
            Integer packageRate = Integer.valueOf(substring.split(",")[3]);
            result.put(ipAddr, packageRate);
        }
        List<EndPointViewVO> endPointViewVOS = pageResult.getList();
        for (Map.Entry<String, Integer> stringIntegerEntry : result.entrySet()) {
            String ipAddr = stringIntegerEntry.getKey();
            QzViewRespVO qzViewRespVO = endpointMapper.findCurByIp(ipAddr);
            if (qzViewRespVO == null) {
                log.warn("ip:{},并未查询到mac!", ipAddr);
                return;
            }
//            AssetVO assetVO = assetMapper.findByIpAddr(ipAddr);
//            if (assetVO == null) {
//                return;
//            }
//            AssetProfile assetProfile = assetProfileMapper.findByIdAddr(assetVO.getIpAddr());
//            if (assetProfile == null) {
//                return;
//            }
            // 丢包率 等于100即为离线
            int aliveStatus = stringIntegerEntry.getValue() == 100 ? 2 : 1;
            Date aliveLastTime = qzViewRespVO.getAliveLastTime();
            AssetVO assetVO = new AssetVO();
            assetVO.setIpAddr(ipAddr);
            assetVO.setMac(qzViewRespVO.getMac());
            assetVO.setAlive(aliveStatus);
            assetVO.setAliveLastTime(stringIntegerEntry.getValue() == 100 ? aliveLastTime : new Date());//如果在线则更新时间 不在线则不动
            AliveHeartbeatVO aliveHeartBeat = assetMapper.findAliveHeartBeat(assetVO);
            if (aliveHeartBeat == null) {//如果不在线 是否向alive——heartbeat加数据
                aliveHeartBeat = new AliveHeartbeatVO();
                aliveHeartBeat.setIpAddr(assetVO.getIpAddr());
                aliveHeartBeat.setMac(assetVO.getMac());
                aliveHeartBeat.setAliveLastTime(new Date());
                aliveHeartBeat.setCreateTime(new Date());
                aliveHeartBeat.setUpdateTime(new Date());
                AliveHeartbeat aliveHeartbeat = new AliveHeartbeat();
                BeanUtils.copyProperties(aliveHeartBeat, aliveHeartbeat);
                aliveHeartbeatMapper.insertSelective(aliveHeartbeat);
            } else {
                assetMapper.updateAliveByIpAddr(assetVO);
            }
            // 将ping的在线值回填
            for (EndPointViewVO pointViewVO : endPointViewVOS) {
                if (ipAddr.equals(pointViewVO.getIpAddr())) {
                    pointViewVO.setAlive(aliveStatus);
                    pointViewVO.setAliveLastTime(assetVO.getAliveLastTime());
                    break;
                }
            }
        }
    }

    @Override
    public QzRespVO verifyLegal(Map<String, Object> map) {
        List<SelectVO> deviceList = deviceTypeService.findDeviceTypeToSelect();
        QzRespVO current = JSONObject.parseObject(JSONObject.toJSONString(map.get("current")), QzRespVO.class);
        List<AssetBaselineVO> baseLine = JSONObject.parseArray(JSONObject.toJSONString(map.get("baseLine")), AssetBaselineVO.class);
        for (SelectVO selectVO : deviceList) {
            if (current.getDeviceType() == selectVO.getItemKey()) {
                current.setDeviceTypeName(selectVO.getItemValue());
            }
        }

        AssetBaselineVO copyAssetBaseLineVo = new AssetBaselineVO();
        BeanUtils.copyProperties(current, copyAssetBaseLineVo);//只需要copy ip mac nasIp vlan 四个属性
        baseLine.add(copyAssetBaseLineVo);
        //根据ip mac nasIp vlan分组
        int size = baseLine.stream().collect(Collectors.groupingBy(
                assetBaselineVO -> new AssetBaselineVO(assetBaselineVO.getIpAddr(), assetBaselineVO.getMac(), assetBaselineVO.getVlan(), assetBaselineVO.getNasIp())
        )).size();
        if (1 != size) {
            return current;
        }
        return null;
    }

    public String getUser() {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes())
                .getRequest();
        if (request == null) {
            return "admin9";
        }
        String account = request.getParameter("passport");
        if (StringUtils.isEmpty(account)) {
            UserSession userSession = getUserSession(request);
            if (userSession != null) {
                return userSession.getUserName();
            }
            return "";
        }
        return request.getParameter("passport");
    }

    public String getIp() {
        try {
            return ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest()
                    .getRemoteAddr();
        } catch (Exception e) {
            return "xxxx";
        }
    }

    private String getHeadId() {
        try {
            return ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest()
                    .getHeader("reqId");
        } catch (Exception e) {
            return "xxxx";
        }
    }

    public UserSession getUserSession(HttpServletRequest request) {
        return this.httpHelper.getUser(request);
    }


    @Override
    public ApiResult verify(QzRespVO respVO) {
        List<RadiusVerifyRespVO> pingList = new ArrayList<>();
        List<RadiusVerifyRespVO> telnetList = new ArrayList<>();
        List<RadiusVerifyRespVO> radiusList = new ArrayList<>();
        List<RadiusVerifyRespVO> radiusLogList = new ArrayList<>();
        ping(respVO, pingList);
        telnet(respVO, telnetList);
        queryRadius(respVO, radiusList);
        queryRadiusLog(respVO, radiusLogList);

        Map<String, Object> map = MapBuilder.getBuilder()
                .put("pingList", pingList)
                .put("telnetList", telnetList)
                .put("radiusList", radiusList)
                .put("radiusLogList", radiusLogList)
                .build();
        return ApiResult.success(map);
    }

    private void queryRadiusLog(QzRespVO respVO, List<RadiusVerifyRespVO> radiusLogList) {
        List<RadiusEndpointLog> logList = endpointMapper.findRadiusEndpointLog(StringUtils.trimAllWhitespace(respVO.getMac()));
        RadiusVerifyRespVO radiusVerifyRespVO = new RadiusVerifyRespVO();
        radiusVerifyRespVO.setKey("radiusLog");
        radiusVerifyRespVO.setTitle("portal审核日志信息和rad_api审核日志信息:");
        if (logList == null || logList.size() == 0) {
            return;
        }
        radiusVerifyRespVO.setValue(logList);
        radiusLogList.add(radiusVerifyRespVO);
//        logList.forEach(radiusEndpointLog -> {
//            radiusLogList.add(radiusVerifyRespVO);
//        });
    }

    private boolean telnet(QzRespVO respVO, List<RadiusVerifyRespVO> stringList) {
        RadiusVerifyRespVO radiusVerifyRespVO = new RadiusVerifyRespVO();
        radiusVerifyRespVO.setKey("telnet");
        radiusVerifyRespVO.setTitle("telnet连接测试:");

        String swIp = respVO.getNasIp();
        if (StringUtils.isEmpty(swIp)) {
            String str2 = "naIp is null！ ";
            radiusVerifyRespVO.setValue(String.format(str2, swIp));
            stringList.add(radiusVerifyRespVO);
            return true;
        }
        SysSwitcher switcher = sysSwitcherMapper.findSysSwitcherByIp(swIp);
        if (switcher == null) {
            String str2 = "naIp %s does not exist sys_switcher！ ";
            radiusVerifyRespVO.setValue(String.format(str2, swIp));
            stringList.add(radiusVerifyRespVO);
            return true;
        }
        String mac = convertMacToLineStyle(respVO.getMac());
        String user = switcher.getSwAccount();
        String passWord = switcher.getSwPassword();
        /// 开发环境测试使用
//        String tt= "4c ed fb c6 08 93";
//         mac = convertMacToLineStyle(tt);
//        swIp = "192.168.8.12";
//        user = "admin1";
//        passWord = "admin111";
        TelnetUtil telnetUtil = new TelnetUtil(swIp, user, passWord);
        boolean flag = telnetUtil.login();
        if (!flag) {
            String telnetLogin = "nasIp  %s login fail!";
            radiusVerifyRespVO.setValue(String.format(telnetLogin, swIp));
            stringList.add(radiusVerifyRespVO);
            log.warn("设备:{},nasIp:{}登录失败!", respVO.getMac(), swIp);
            return true;
        }
        log.info("nas ip {} 登录成功!", swIp);
        telnet(mac, telnetUtil, radiusVerifyRespVO, stringList);

        return false;
    }

    private void ping(QzRespVO respVO, List<RadiusVerifyRespVO> stringList) {
        long startTime = System.currentTimeMillis();
        final boolean ping = ping(respVO.getIpAddr());
        if (!ping) {
            log.warn("设备:{},ip:{}未ping通！", respVO.getMac(), respVO.getIpAddr());
        }
        long endTime = System.currentTimeMillis();
        RadiusVerifyRespVO radiusVerifyRespVO = new RadiusVerifyRespVO();
        radiusVerifyRespVO.setKey("ping");
        radiusVerifyRespVO.setTitle(String.format("ping %s ", respVO.getIpAddr()));
        radiusVerifyRespVO.setValue(ping ? "连接成功" : "连接失败");
        radiusVerifyRespVO.setCostTime(endTime - startTime);
        stringList.add(radiusVerifyRespVO);
    }

    private void queryRadius(QzRespVO respVO, List<RadiusVerifyRespVO> radiusList) {
        if (dataSourceConcurrentMap != null && dataSourceConcurrentMap.size() > 0) {
            RadiusVerifyRespVO radiusVerifyRespVO = new RadiusVerifyRespVO();
            radiusVerifyRespVO.setKey("radius");
            radiusVerifyRespVO.setTitle("进行查询radius的access_policy状态:");
            List<RadiusRespVO> list = new ArrayList<>();
            for (String key : dataSourceConcurrentMap.keySet()) {
                DBUtil dbUtil = new DBUtil(key);
                String sql = "SELECT mac,access_policy AS accessPolicy,update_time AS updateTime FROM radius_endpoint r WHERE r.`mac` = '%s' LIMIT 1";
                sql = String.format(sql, respVO.getMac());
                RadiusRespVO radiusRespVO = new RadiusRespVO();
                radiusRespVO.setDbName(key);
                try {
                    List<Map<String, Object>> mapList = dbUtil.executeQuery(sql);
                    if (mapList != null && mapList.size() > 0) {
//                        stringList.add("数据库:"+key+",查询值:"+mapList.get(0).toString());
//                        radiusRespVO = JSON.parseObject(JSONObject.toJSONString((mapList.get(0))), RadiusRespVO.class);
                        radiusRespVO.setAccessPolicy(Integer.valueOf(mapList.get(0).get("access_policy").toString()));
                        radiusRespVO.setUsername(mapList.get(0).get("mac").toString());
                        radiusRespVO.setUpdateTime(mapList.get(0).get("update_time").toString());
//                        radiusVerifyRespVO.setValue(mapList.get(0).toString());
                    } else {
//                        stringList.add("数据库:"+key+",未查询到!");
//                        radiusVerifyRespVO.setValue(new ArrayList<>());
                    }
                    list.add(radiusRespVO);
                } catch (SQLException e) {
                    log.error("数据源:{},查询失败！", key, e);
                }
            }
            radiusVerifyRespVO.setValue(list);
            radiusList.add(radiusVerifyRespVO);
        } else {
            RadiusVerifyRespVO radiusVerifyRespVO = new RadiusVerifyRespVO();
            radiusVerifyRespVO.setKey("");
//            String str2 = "未查询到radius的access_policy状态 ";
//            stringList.add(str2);
            radiusVerifyRespVO.setValue(new ArrayList<>());
            radiusList.add(radiusVerifyRespVO);
        }

    }


    private void telnet(String mac, TelnetUtil telnetUtil, RadiusVerifyRespVO radiusVerifyRespVO, List<RadiusVerifyRespVO> stringList) {
        final String sv = "system-view";
        final String cmd = "dis mac-address " + mac;
        final String cmd2 = "display access-user mac-address " + mac;
        telnetUtil.sendSysCommand(sv);
//        String msg = getMsg(cmd, telnetUtil);
        String msg = getMsg(cmd2, telnetUtil);
        radiusVerifyRespVO.setValue(msg);
        telnetUtil.sendCommand("quit");
        telnetUtil.disconnect();
        stringList.add(radiusVerifyRespVO);
    }

    private String getMsg(String cmd, TelnetUtil telnetUtil) {
//        stringList.add("查看交换机的设备信息:");
        String msg = telnetUtil.sendSysCommand(cmd);
        StringBuffer sb = new StringBuffer();
        List<String> stringList = new ArrayList<>();
        if (!StringUtils.isEmpty(msg)) {
            String[] msgs = msg.split("\r\n");
            if (msgs != null && msgs.length > 0) {
                for (String s : msgs) {
                    sb.append(s);
                    sb.append("{???}");
                    stringList.add(s);
                }
            }
            msg = Arrays.asList(msgs).toString();
        }
//        return stringList.toString();
        return sb.toString();
    }


    private boolean ping(String ipAddress) {
        int timeOut = 3000;
        boolean status = false;
        try {
            status = InetAddress.getByName(ipAddress).isReachable(timeOut);
        } catch (IOException e) {
            log.error("尝试连接ip:{},失败！", ipAddress, e);
        }
        return status;
    }


    private String convertMacToLineStyle(String mac) {
        if (mac != null && mac.length() == VALID_MAC_LENGTH) {
            mac = StringUtils.trimAllWhitespace(mac);
            StringBuffer sb = new StringBuffer(mac);
            sb.insert(4, "-").insert(9, "-");
            return sb.toString();
        }
        return mac;
    }

    @Override
    public ApiResult syncJudge(Integer id, Integer policy, String authMark, UserSession userSession) {
        //调用接口
        String radApiHost = constantService.getJudgeApi();
        if (radApiHost == null || !radApiHost.startsWith("http")) {
            return ApiResult.error("系统常量rad_api为空，请联系系统管理员");
        }
        String username = userSession.getUserName();
        if (null == id) {
            return ApiResult.error("id为空，请联系系统管理员！");
        }
        if (null == policy) {
            return ApiResult.error("policy为空，请联系系统管理员！");
        }
        if (StringUtils.isEmpty(username)) {
            return ApiResult.error("用户信息为空，请联系系统管理员！");
        }
        String traceId = getTraceId();
        String uri = "/api/radius/endpoint/judge?id=" + id + "&policy=" + policy
                + "&authMark=" + authMark + "&username=" + username
                + "&debug=" + DEBUG_SCHEMA + "&traceId=" + traceId;
        ApiResult result = null;
        try {
            String serverUrl = radApiHost + uri;
            log.info("reqUrl:{}", serverUrl);
            saveTrace(DEBUG_SCHEMA, 0, 1, serverUrl, traceId, DateHelper.getNow(), PROJECT_PORTAL, PROJECT_JUDGE_API, "开始请求judge_api审核!");
            result = restTemplateHelper.getForObject(serverUrl, 60 * 1000);
            log.info("reqResult:{}", result);
            if (result == null) {
                saveTrace(DEBUG_SCHEMA, 1, 2, "null", traceId, DateHelper.getNow(), PROJECT_PORTAL, PROJECT_JUDGE_API, "请求judge_api审核失败!");
                return ApiResult.error("审批失败，请联系管理员");
            }
            saveTrace(DEBUG_SCHEMA, 0, 2, serverUrl, traceId, DateHelper.getNow(), PROJECT_PORTAL, PROJECT_JUDGE_API, "请求judge_api审核成功!");
            return result;
        } catch (Exception e) {
            log.error("解析远程数据异常#", e);
            saveTrace(DEBUG_SCHEMA, 1, 2, e.toString(), traceId, DateHelper.getNow(), PROJECT_PORTAL, PROJECT_JUDGE_API, "请求judge_api审核失败!");
            return ApiResult.error("解析远程数据异常#" + JSON.toJSONString(result));
        }
    }

    @Override
    public ApiResult batchJudge(EndPointBatchReqVO reqVO) {
        //调用接口
        String radApiHost = constantService.getJudgeApi();
        if (radApiHost == null || !radApiHost.startsWith("http")) {
            return ApiResult.error("系统常量rad_api为空，请联系系统管理员");
        }
        String traceId = getTraceId();
        reqVO.setTraceId(traceId);
        reqVO.setDebug(DEBUG_SCHEMA);
        //批量审核
        String uri = "/api/radius/endpoint/batchJudge";
        ApiResult result = null;
        try {
            String serverUrl = radApiHost + uri;
            String data = JSONObject.toJSONString(reqVO);
            log.info("reqUrl:{},data:{}", serverUrl, data);
            saveTrace(DEBUG_SCHEMA, 0, 1, data, traceId, DateHelper.getNow(), PROJECT_PORTAL, PROJECT_JUDGE_API, "开始请求judge_api批量审核!");
            result = restTemplateHelper.postForJsonString(serverUrl, data);
            log.info("reqResult:{}", result);
            if (result == null) {
                saveTrace(DEBUG_SCHEMA, 1, 2, "null", traceId, DateHelper.getNow(), PROJECT_PORTAL, PROJECT_JUDGE_API, "请求judge_api批量审核失败!");
                return ApiResult.error("批量审批失败，请联系管理员");
            }
            saveTrace(DEBUG_SCHEMA, 0, 2, data, traceId, DateHelper.getNow(), PROJECT_PORTAL, PROJECT_JUDGE_API, "请求judge_api批量审核成功!");
            return result;
        } catch (Exception e) {
            log.error("解析远程数据异常#", e);
            saveTrace(DEBUG_SCHEMA, 1, 2, e.toString(), traceId, DateHelper.getNow(), PROJECT_PORTAL, PROJECT_JUDGE_API, "请求judge_api批量审核失败!");
            return ApiResult.error("解析远程数据异常#" + JSON.toJSONString(result));
        }

    }

    @Override
    public ApiResult delete(Integer id) {
        //调用接口
        String radApiHost = constantService.getJudgeApi();
        if (radApiHost == null || !radApiHost.startsWith("http")) {
            return ApiResult.error("系统常量rad_api为空，请联系系统管理员");
        }

        String uri = "/api/radius/endpoint/delete?id=" + id;
        ApiResult result = null;
        try {
            String serverUrl = radApiHost + uri;
            log.info("reqUrl:{}", serverUrl);
            result = restTemplateHelper.postForObject(serverUrl, null);
            log.info("reqResult:{}", result);
            if (result == null) {
                return ApiResult.error("删除失败，请联系管理员");
            }
            return result;
        } catch (Exception e) {
            log.error("解析远程数据异常#", e);
            return ApiResult.error("解析远程数据异常#" + JSON.toJSONString(result));
        }
    }


    private void saveTrace(Integer debug, int isFail, int order,
                           String reqData, String traceId, String time,
                           String source, String target, String remark) {
        if (debug == null || debug == 0) {
            return;
        }
        TraceData traceData = new TraceData();
        traceData.setReqData(reqData);
        traceData.setTraceSource(source);
        traceData.setTraceTarget(target);
        traceData.setLogTime(time);
        traceData.setTraceType(1);
        traceData.setTraceId(traceId);
        traceData.setIsFail(isFail);
        traceData.setTraceOrder(order);
        traceData.setRemark(remark);
        traceDataDao.insert(traceData);
    }

    private String getTraceId() {
        return ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest().getHeader(TRACE_ID);
    }

}
