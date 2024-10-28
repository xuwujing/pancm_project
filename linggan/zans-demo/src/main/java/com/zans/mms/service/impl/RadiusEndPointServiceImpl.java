package com.zans.mms.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.zans.base.service.impl.BaseServiceImpl;
import com.zans.base.util.HttpHelper;
import com.zans.base.util.RedisUtil;
import com.zans.base.util.StringHelper;
import com.zans.base.vo.ApiResult;
import com.zans.base.vo.PageResult;
import com.zans.base.vo.UserSession;
import com.zans.mms.config.PoolTaskConfig;
import com.zans.mms.dao.guard.RadiusEndpointMapper;
import com.zans.mms.model.LogOperation;
import com.zans.mms.model.RadiusEndpoint;
import com.zans.mms.model.RadiusEndpointLog;
import com.zans.mms.model.SysConstant;
import com.zans.mms.service.*;
import com.zans.mms.util.LogBuilder;
import com.zans.mms.vo.arp.AssetRespVO;
import com.zans.mms.vo.arp.AssetSearchVO;
import com.zans.mms.vo.radius.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.zans.base.config.GlobalConstants.RAD_API_BLOCK;
import static com.zans.base.config.GlobalConstants.RAD_API_CHANGE;

/**
 * @author yhj
 */
@Service
@Slf4j
public class RadiusEndPointServiceImpl extends BaseServiceImpl<RadiusEndpoint> implements IRadiusEndPointService {

	@Autowired
	RedisUtil redisUtil;

	public static int RADIUS_ACCESS_POLICY_BLOCK = 0;
	public static int RADIUS_ACCESS_POLICY_QZ = 1;
	public static int RADIUS_ACCESS_POLICY_PASS = 2;
	public static Integer LOG_MODULE_RADIUS = 14;
	public static String LOG_OPERATION_EDIT = "修改";

	public static String LOG_RESULT_SUCCESS = "完成";

	public static String MODULE_ARP_ALIVE = "alive";



	@Autowired
	RadiusEndpointMapper endpointMapper;

	@Autowired
	IConstantItemService constantItemService;

	@Autowired
	IRadiusAcctService radiusAcctService;

	@Autowired
	IAlertRuleService iAlertRuleService;


	@Autowired
	ISysConstantService constantService;

	@Autowired
	protected HttpHelper httpHelper;

	@Autowired
	PoolTaskConfig poolTaskConfig;

	@Autowired
	ILogOperationService logOperationService;

	@Resource
	public void setEndpointMapper(RadiusEndpointMapper endpointMapper) {
		super.setBaseMapper(endpointMapper);
		this.endpointMapper = endpointMapper;
	}

	@Override
	public PageResult<EndPointViewVO> getEndPointPage(EndPointReqVO reqVO) {
		Integer policy = reqVO.getAccessPolicy();
		if (policy != null) {
			if (!isPolicyValid(policy)) {
				return null;
			}
		}
		if (reqVO.getUsername() != null) {
			reqVO.setUsername(reqVO.getUsername().replaceAll(" ", ""));
		}
		Page page = PageHelper.startPage(reqVO.getPageNum(), reqVO.getPageSize());
		List<EndPointViewVO> list = null;
		if (policy == null || policy == RADIUS_ACCESS_POLICY_PASS) {
			list = endpointMapper.findEndPointList(reqVO);
		} else if (policy == RADIUS_ACCESS_POLICY_BLOCK) {
			list = endpointMapper.findBlockEndPointList(reqVO);
		} else {
			list = endpointMapper.findEndPointList(reqVO);
		}

		Map<Object, String> aliveMap = constantItemService.findItemsMapByDict(MODULE_ARP_ALIVE);
		Map<String, Object> map = new HashMap<>();
		Map<String, Object> map2= new HashMap<>();
		String[] kvs = new String[reqVO.getPageSize()];
		String[] kvs2 = new String[reqVO.getPageSize()];
		for (int i = 0; i < list.size(); i++) {
			EndPointViewVO vo = list.get(i);
			vo.setAliveNameByMap(aliveMap);
			if (vo.getAlive() != null) {
				vo.setAliveName(vo.getAlive() == 1 ? "已入网" : "未入网");
			}
			vo.setAlertRecord(0);
			kvs[i] = vo.getPass();
			kvs2[i] = vo.getIpAddr();
		}
		map.put("kvs", kvs);
		map2.put("kvs", kvs2);
		// 2020-10-7 和北傲确认，在检疫区/在线设备/阻断设备列表项增加是否显示告警记录按钮,如果触发了就显示
		List<String> kvsList = iAlertRuleService.getRecordByKeywordValues(map);
		List<String> kvsList2 = iAlertRuleService.getRecordByIp(map2);
		for (EndPointViewVO vo : list) {
			for (String s : kvsList) {
				if (vo.getPass().equals(s)) {
					vo.setAlertRecord(1);
				}
			}
			for (String s : kvsList2) {
				if(StringUtils.isEmpty(vo.getIpAddr())){
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


	/*@Transactional(rollbackFor = Exception.class)*/
	@Override
	public ApiResult judge(Integer id, Integer policy, String authMark, UserSession userSession) {

		String radApiHost = constantService.getRadApi();
		if (radApiHost == null || !radApiHost.startsWith("http")) {
			return ApiResult.error("系统常量rad_api为空，请联系系统管理员");
		}

		RadiusEndpoint endpoint = this.getById(id);
		String mac = endpoint.getUsername();
		Integer deleteStatus = 0;

		if (!redisUtil.hasKey("acct#"+mac)){
			return ApiResult.error("不存在" + "acct#"+mac+", 请稍后操作!");
		}

		// 强行DM
		// COA 很多交换机不支持
		if (!isPolicyValid(policy)) {
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			return ApiResult.error("参数异常#" + policy);
		}

		String edgeMode = constantService.getEdgeAccessMode();
		if (!"acl".equals(edgeMode)) {
			radiusJudge(policy, radApiHost, mac, deleteStatus);
		}

		/// 2020-9-15 和北辰确认，襄阳项目远程调用都禁用

		endpoint.setAuthMark(authMark);
		endpoint.setAuthTime(new Date());
		endpoint.setAuthPerson(userSession.getNickName());
		endpoint.setAccessPolicy(policy);
		this.update(endpoint);

		//2020-12-23 新增逻辑,将审核的操作进行写入
		RadiusEndpointLog radiusEndpointLog = new RadiusEndpointLog();
		radiusEndpointLog.setDataSource(1);
		radiusEndpointLog.setAccessPolicy(policy);
		radiusEndpointLog.setDeleteStatus(deleteStatus);
		radiusEndpointLog.setUsername(StringUtils.trimAllWhitespace(mac));
		endpointMapper.saveRadiusEndpointLog(radiusEndpointLog);

		// 记录日志
		LogOperation logOperation = new LogBuilder().session(userSession).module(LOG_MODULE_RADIUS.toString())
				.operation(LOG_OPERATION_EDIT).content(id).result(LOG_RESULT_SUCCESS)
				.build();
		logOperationService.insert(logOperation);
		return ApiResult.success();
	}

	private void radiusJudge(Integer policy, String radApiHost, String mac, Integer deleteStatus) {
		String module = "endpoint";
		// 调用rad_api接口
		String uri = String.format("/sync/%s?mac=%s&delete_status=%s&policy=%d", module, mac, deleteStatus,
				policy);

		// 2021-6-11 根据不同的policy选择不同的uri

		String uri2 ="";
		String radApi ="";
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
		SysConstant blockSysConstant = constantService.findConstantByKey(radApi);
		String radApiBlockUri = "/rad/dm";
		if (blockSysConstant != null) {
			radApiBlockUri = blockSysConstant.getConstantValue();
		}
		return radApiBlockUri;
	}

	private boolean isPolicyValid(Integer policy) {
		if (policy == null || policy < RADIUS_ACCESS_POLICY_BLOCK || policy > RADIUS_ACCESS_POLICY_PASS) {
			return false;
		} else {
			return true;
		}
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

	public ApiResult executeRadApiRequest(String host, String uri) {
		log.info("{}|远程操作,请求用户:{},请求ip:{},内容:{}|", getHeadId(), getUser(), getIp(), host + uri);
		return poolTaskConfig.executeRadApiRequest2(host, uri);

		// ApiResult result = null;
		// try {
		// String serverUrl = host + uri;
		// result = restTemplateHelper.getForObject(serverUrl, 60 * 1000);
		// return result;
		// } catch (Exception e) {
		// e.printStackTrace();
		// return ApiResult.error("解析远程数据异常#" + JSON.toJSONString(result));
		// }
	}

	private String getHeadId() {
		try {
			return ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest()
					.getHeader("reqId");
		} catch (Exception e) {
			return "xxxx";
		}
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

	public UserSession getUserSession(HttpServletRequest request) {
		return this.httpHelper.getUser(request);
	}

	@Override
	public List<AssetRespVO> findAssetsForList(AssetSearchVO asset) {
		return endpointMapper.findAssetsForList(asset);
	}

	@Override
	public QzRespVO findQzById(Integer id) {

		QzRespVO vo = endpointMapper.findQzById(id);
		if (vo != null) {
			Map<Object, String> aliveMap = constantItemService.findItemsMapByDict(MODULE_ARP_ALIVE);
			vo.setAliveNameByMap(aliveMap);
			AcctRespVO acct = radiusAcctService.findLatestAcctByMac(vo.getUsername());
			if (acct != null) {
				// BeanUtils.copyProperties(acct, vo)
				// 2020-9-27 详情页新增返回字段
				vo.setNasName(acct.getNasName());
				vo.setCalledStationId(acct.getCalledStationId());
				vo.setAcctStartTime(acct.getAcctStartTime());
				vo.setNasPortType(acct.getNasPortType());
				String second = StringHelper.secondToTime(String.valueOf(acct.getAcctSessionTime()));
				vo.setAcctSessionTime(second);
			}
			if (StringUtils.isEmpty(vo.getVlan())) {
				vo.setVlan("-");
			}

		}
		return vo;
	}


	@Override
	public QzViewRespVO findBaseByCurMac(String username) {

		return endpointMapper.findBaseByCurMac(username);
	}

	@Override
	public ApiResult getPolicy(String username) {
		if (username != null) {
			username = username.replaceAll(" ", "");
		}
		RadiusPolicyRespVO radiusPolicyRespVO = endpointMapper.getPolicy(username);
		return ApiResult.success(radiusPolicyRespVO);
	}

	@Override
	public ApiResult deleteAcctList(Integer id) {
		RadiusEndpoint endpoint =this.getById(id);
		Integer policy=endpoint.getAccessPolicy();
		//通过endPoint查ip
		String currentIp=endpointMapper.getByEndPointId(id);
		endpointMapper.deleteByEndpoint(endpoint.getUsername(),currentIp);
//		endpointMapper.deleteById(id);

//		if(endpoint.getAccessPolicy()==0){
//			//通过endPoint查ip
//			String currentIp=endpointMapper.getByEndPointId(id);
//			endpointMapper.deleteById(id);
//			String module = "endpoint";
//			String uri = String.format("/sync/%s?mac=%s&delete_status=%s&policy=%d&base_ip=%s", module, endpoint.getPass(), 1,
//					policy, currentIp);
//
//			String radApi = getRadApi(RAD_API_BLOCK);
//			String uri2 = radApi + "?mac=" + endpoint.getPass() + "&policy=" + policy;
//			String radApiHost = constantService.getRadApi();
//			if (radApiHost == null || !radApiHost.startsWith("http")) {
//				return ApiResult.error("系统常量rad_api为空，请联系系统管理员");
//			}
//			log.info("{}|远程操作,请求用户:{},请求ip:{},内容1:{},内容2:{}|", getHeadId(), getUser(), getIp(), radApiHost + uri, radApiHost + uri2);
//			poolTaskConfig.executeRadApiRequest2(radApiHost, uri, radApiHost, uri2);
//			return ApiResult.success("删除成功！");
//		}
		return ApiResult.error("删除失败，该终端不在阻断区！");
	}

	@Override
	public QzViewRespVO findCurByCurMac(String username) {
		return endpointMapper.findCurByCurMac(username);
	}


	@Override
	public void initData() {
		redisUtil.delAll();
		endpointMapper.initData();
	}
}
