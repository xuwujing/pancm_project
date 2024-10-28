package com.zans.mms.controller.demokit;

import com.zans.base.controller.BaseController;
import com.zans.base.util.MapBuilder;
import com.zans.base.vo.ApiResult;
import com.zans.base.vo.SelectVO;
import com.zans.base.vo.UserSession;
import com.zans.mms.model.Gear;
import com.zans.mms.service.IConstantItemService;
import com.zans.mms.service.IGearService;
import com.zans.mms.vo.gear.GearReqVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

import static com.zans.mms.config.PortalGlobalConstants.*;
import static com.zans.mms.config.PortalGlobalConstants.SYS_DICT_KEY_MAINTAIN_STATUS;

/**
 * @author qitian
 * @Title: zans-demo
 * @Description:一机一档控制层
 * @Version:1.0.0
 * @Since:jdk1.8
 * @date 2021/6/29
 */
@Api(value = "demoKit/gear", tags = {"/gear ~ 一机一档"})
@RestController
@RequestMapping("demoKit/gear")
@Validated
@Slf4j
public class GearController extends BaseController {

	@Autowired
	private IGearService gearService;

	@Autowired
	IConstantItemService constantItemService;

	/**
	 * 一机一档列表初始化
	 * @param
	 * @return
	 */
	@ApiOperation(value = "/init", notes = "一机一档列表初始化")
	@RequestMapping(value = "/init", method = {RequestMethod.GET})
	public ApiResult init() {
		List<SelectVO> arpAlive = constantItemService.findItemsByDict(MODULE_ARP_ALIVE);
		List<SelectVO> brand = constantItemService.findItemsByDict("brand");
		List<SelectVO> monitorPointType = constantItemService.findItemsByDict("monitorPointType");
		List<SelectVO> deviceLocalType = constantItemService.findItemsByDict("deviceLocalType");
		List<SelectVO> networkType = constantItemService.findItemsByDict("networkType");
		List<SelectVO> deviceStatus = constantItemService.findItemsByDict("deviceStatus");
		List<SelectVO> monitorPoint= constantItemService.findItemsByDict("monitorPoint");
		List<SelectVO> deviceType= constantItemService.findItemsByDict("deviceType");
		List<SelectVO> deviceFunctionType= constantItemService.findItemsByDict("deviceFunctionType");
		List<SelectVO> lightProperty =constantItemService.findItemsByDict("lightProperty");
		List<SelectVO> cameraCodingFormat =constantItemService.findItemsByDict("cameraCodingFormat");
		List<SelectVO> department =constantItemService.findItemsByDict("departmentDemo");
		List<SelectVO> industryCode =constantItemService.findItemsByDict("industryCode");
		List<SelectVO> deviceCode =constantItemService.findItemsByDict("deviceCode");
		List<SelectVO> netId =constantItemService.findItemsByDict("netId");

		Map<String, Object> result = MapBuilder.getBuilder()
				.put("brand",brand)
				.put("monitorPointType",monitorPointType)
				.put("deviceLocalType",deviceLocalType)
				.put("networkType",networkType)
				.put("deviceStatus",deviceStatus)
				.put("monitorPoint",monitorPoint)
				.put("deviceType",deviceType)
				.put("deviceFunctionType",deviceFunctionType)
				.put("lightProperty",lightProperty)
				.put("cameraCodingFormat",cameraCodingFormat)
				.put("department",department)
				.put("industryCode",industryCode)
				.put("deviceCode",deviceCode)
				.put("netId",netId)
				.build();
		return ApiResult.success(result);
	}


	/**
	 * 一机一档列表
	 * @param req
	 * @return
	 */
	@ApiOperation(value = "/list", notes = "一机一档列表")
	@RequestMapping(value = "/list", method = {RequestMethod.POST})
	public ApiResult getAlertRecordList(@RequestBody GearReqVO req) {
		super.checkPageParams(req);
		return gearService.list(req);
	}

	/**
	 * 一机一档列表
	 * @param gear 一机一档实体
	 * @return
	 */
	@ApiOperation(value = "/saveOrUpdate", notes = "一机一档列表")
	@RequestMapping(value = "/saveOrUpdate", method = {RequestMethod.POST})
	public ApiResult saveOrUpdate(@RequestBody Gear gear, HttpServletRequest request) {
		if(null==gear.getId()){
			String username = httpHelper.getUser(request).getUserName();
			gear.setCreator(username);
		}
		gearService.saveOrUpdate(gear);
		return ApiResult.success();
	}

	/**
	 * 一机一档详情
	 * @param id 一机一档实体主键
	 * @return
	 */
	@ApiOperation(value = "/view", notes = "一机一档详情")
	@RequestMapping(value = "/view", method = {RequestMethod.GET})
	public ApiResult view(@RequestParam("id") Long id) {
		Gear gear = gearService.getById(id);
		return ApiResult.success(gear);
	}

	/**
	 * 一机一档删除
	 * @param id 一机一档实体主键
	 * @return
	 */
	@ApiOperation(value = "/delete", notes = "一机一档删除")
	@RequestMapping(value = "/delete", method = {RequestMethod.GET})
	public ApiResult delete(@RequestParam("id") Long id) {
		gearService.delete(id);
		return ApiResult.success();
	}


}
