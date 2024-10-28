package com.zans.mms.controller.demokit;

import com.zans.base.annotion.Record;
import com.zans.base.config.GlobalConstants;
import com.zans.base.controller.BaseController;
import com.zans.base.util.MapBuilder;
import com.zans.base.util.StringHelper;
import com.zans.base.vo.ApiResult;
import com.zans.base.vo.PageResult;
import com.zans.base.vo.SelectVO;
import com.zans.mms.config.PortalGlobalConstants;
import com.zans.mms.model.LogOperation;
import com.zans.mms.model.RadiusEndpoint;
import com.zans.mms.service.*;
import com.zans.mms.util.LogBuilder;
import com.zans.mms.vo.radius.EndPointBatchReqVO;
import com.zans.mms.vo.radius.EndPointReqVO;
import com.zans.mms.vo.radius.EndPointViewVO;
import com.zans.mms.vo.radius.QzRespVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.core.ColumnMapRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.sql.DataSource;
import java.util.List;
import java.util.Map;

import static com.zans.base.config.EnumErrorCode.CLIENT_PARAMS_ERROR;
import static com.zans.mms.config.MMSConstants.*;

/**
 * @author qitian
 * @Title: zans-demo
 * @Description:radius核心（在线、离线、检疫）
 * @Version:1.0.0
 * @Since:jdk1.8
 * @date 2021/5/25
 */
@Api(value = "demoKit/radius/endpoint", tags = {"demoKit/radius/endpoint ~ radius核心（在线、离线、检疫）"})
@RestController
@RequestMapping("demoKit/radius/endpoint")
@Validated
@Slf4j
public class RadiusEndPointController extends BaseController {

	/**
	 * 常量
	 */
	public static String MODULE_AREA = "area";

	public static String MODULE_DEVICE = "device";

	public static String INIT_DATA_TABLE = "table";

	//检疫区自定义列
	public static Map<String, String> CUSTOM_COLUMN_MAP;

	/**
	 * 检疫区自定义列
	 */
	public static String CUSTOM_COLUMN = "custom_column";

	@Autowired
	IRadiusEndPointService radiusEndPointService;

	@Autowired
	IDeviceTypeService deviceTypeService;

	@Autowired
	IAreaService areaService;

	@Autowired
	IRadiusAcctService radiusAcctService;

	@Autowired
	ILogOperationService logOperationService;

	@Autowired
	RadiusQzController radiusQzController;

	@ApiOperation(value = "/list", notes = "radius 在线、离线、检疫列表")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "req", value = "查询条件", required = true, dataType = "EndPointReqVO", paramType = "body")
	})
	@RequestMapping(value = "/list", method = {RequestMethod.POST})
	@ResponseBody
	public ApiResult list(@RequestBody EndPointReqVO req) {
		super.checkPageParams(req, "e.update_time desc");

		List<SelectVO> deviceList = deviceTypeService.findDeviceTypeToSelect();
		List<SelectVO> area = areaService.findAreaToSelect();
		if (req.getUsername() != null) {
			req.setUsername(req.getUsername().replaceAll(" ", ""));
		}
		PageResult<EndPointViewVO> pageResult = radiusEndPointService.getEndPointPage(req);

		List<EndPointViewVO> list = pageResult.getList();
		list.forEach(e -> {
			if (StringUtils.isNotEmpty(e.getAcctSessionTime())) {
				String second = StringHelper.secondToTime(String.valueOf(e.getAcctSessionTime()));
				e.setAcctSessionTime(second);
			}
		});

		Map<String, Object> result = MapBuilder.getBuilder()
				.put(INIT_DATA_TABLE, pageResult)
				.put(MODULE_AREA, area)
				.put(MODULE_DEVICE, deviceList)
				.put(CUSTOM_COLUMN,CUSTOM_COLUMN_MAP)
				.build();
		return ApiResult.success(result);
	}


	/***
	 * 获取policy
	 * @return
	 */
	@RequestMapping(value = "/getPolicy", method = { RequestMethod.GET})
	public ApiResult getPolicy(@RequestParam("username") String username) {

		ApiResult result = radiusEndPointService.getPolicy(username);
		return result;
	}

	/***
	 * 审核处置
	 * @param id
	 * @param policy  0:阻断; 1:检疫区; 2:放行
	 * @param request
	 * @return
	 */
	@ApiOperation(value = "/judge", notes = "审核")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "id", value = "id", required = true, dataType = "Integer", paramType = "query"),
			@ApiImplicitParam(name = "policy", value = "policy", required = true, dataType = "Integer", paramType = "query"),
			@ApiImplicitParam(name = "authMark", value = "authMark", required = true, dataType = "String", paramType = "query")
	})
	@RequestMapping(value = "/judge", method = {RequestMethod.POST, RequestMethod.GET})
	@Record(module = MODULE_RADIUS_END_POINT,operation = LOG_OPERATION_JUDGE)
	public ApiResult judge(@RequestParam("id") Integer id,
						   @RequestParam("policy") Integer policy,
						   @RequestParam("authMark") String authMark,
						   HttpServletRequest request) {
		ApiResult result = radiusEndPointService.judge(id, policy, authMark, getUserSession(request));
		return result;
	}


	/***
	 * 批量审核处置
	 * @param request
	 * @return
	 */
	@ApiOperation(value = "/batchJudge", notes = "批量审核")
	@RequestMapping(value = "/batchJudge", method = {RequestMethod.POST})
	@Record(module = MODULE_RADIUS_END_POINT,operation = LOG_OPERATION_BATCH_JUDGE)
	public ApiResult batchJudge(@RequestBody EndPointBatchReqVO reqVO,
								HttpServletRequest request) {
		ApiResult result =null;
		Integer policy = reqVO.getPolicy();
		String authMark = reqVO.getAuthMark();
		String []ids = reqVO.getIds().split(",");
		for (String id : ids) {
			result = radiusEndPointService.judge(Integer.parseInt(id), policy, authMark, getUserSession(request));
		}
		return result;
	}

	@ApiOperation(value = "/view", notes = "查看设备详情")
	@ApiImplicitParam(name = "id", value = "地址id", required = true, dataType = "int", paramType = "query")
	@RequestMapping(value = "/view", method = {RequestMethod.GET})
	public ApiResult getEndPointById(@RequestParam("id") Integer id,
									 @RequestParam(value = "arpId", required = false) Integer arpId) {
		//2020-9-28 在线设备和阻断设备详情显示改为和检疫区一致
		QzRespVO respVO = radiusEndPointService.findQzById(id);
		if (respVO == null) {
			return ApiResult.error(CLIENT_PARAMS_ERROR).appendMessage("检疫区不存在#" + id);
		}
		String nasPortId = respVO.getNasPortId();
		if (!org.springframework.util.StringUtils.isEmpty(nasPortId)) {
			nasPortId = nasPortId.replace("3D", "");
			nasPortId = nasPortId.replace("=3B", ";");
			nasPortId=StringHelper.getPortId(nasPortId);
			respVO.setNasPortId(nasPortId);
		}

		Map<String,Object> resultMap = radiusQzController.getIpAndAssetsCp(respVO);
		Map<String, Object> map = MapBuilder.getBuilder()
				.put("radiusQz", respVO)
				.build();
		map.putAll(resultMap);
		return ApiResult.success(map);
	}


	@ApiOperation(value = "/delete", notes = "删除")
	@ApiImplicitParam(name = "id", value = "id", required = true, dataType = "Integer", paramType = "query")
	@RequestMapping(value = "/delete", method = {RequestMethod.POST, RequestMethod.GET})
	@Record
	public ApiResult deleteAcctList(@RequestParam("id") Integer id, HttpServletRequest request) {
		/*RadiusEndpoint endpoint = radiusEndPointService.getById(id);
		if(zuduan){
			String uri = String.format("/sync/%s?mac=%s&delete_status=%s&policy=%d&base_ip=%s", module, mac, deleteStatus,
					policy, baseIp);
			endpoint.setDeleteStatus(1);
			radiusEndPointService.update(endpoint);*/
		return radiusEndPointService.deleteAcctList(id);

	}


}
