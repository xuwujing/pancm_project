package com.zans.mms.controller.pc;

import com.zans.base.controller.BaseController;
import com.zans.base.vo.ApiResult;
import com.zans.base.vo.UserSession;
import com.zans.mms.service.IRankingService;
import com.zans.mms.vo.asset.AssetQueryVO;
import com.zans.mms.vo.ranking.MaintenanceReqVO;
import com.zans.mms.vo.ranking.QualityReqVO;
import com.zans.mms.vo.ranking.RankingQueryVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import java.util.List;

import static com.zans.base.config.GlobalConstants.ELECTRIC_POLICE;

/**
 * @author qitian
 * @Title: zans-mms-server
 * @Description:绩效考核数据录入
 * @Version:1.0.0
 * @Since:jdk1.8
 * @date 2021/10/8
 */
@Api(value = "/ranking", tags = {"/ranking ~ 绩效考核"})
@RestController
@RequestMapping("/ranking")
@Validated
@Slf4j
public class RankingController extends BaseController {

	@Autowired
	IRankingService rankingService;


	/**------------------------------维保单位相关接口开始----------------------------------------------------**/

	/**
	 * 维保单位数据
	 * @param vo
	 * @return
	 */
	@ApiOperation(value = "维保单位数据列表", notes = "维保单位数据列表")
	@PostMapping(value = "/maintenanceList",  produces = MediaType.APPLICATION_JSON_VALUE)
	public ApiResult getList(@Valid @RequestBody RankingQueryVO vo) {
		super.checkPageParams(vo);
		return rankingService.maintenanceList(vo);
	}

	/**
	 * 维保单位数据
	 * @param vo
	 * @return
	 */
	@ApiOperation(value = "维保单位数据录入", notes = "维保单位数据录入")
	@PostMapping(value = "/addMaintenanceList",  produces = MediaType.APPLICATION_JSON_VALUE)
	public ApiResult addMaintenanceList(@Valid @RequestBody List<MaintenanceReqVO> vo, HttpServletRequest request) {
		UserSession user = httpHelper.getUser(request);
		return rankingService.addMaintenance(vo,user);
	}
	/**
	 * 维保单位数据
	 * @param
	 * @return
	 */
	@ApiOperation(value = "维保单位数据列表", notes = "维保单位数据列表")
	@PostMapping(value = "/maxMaintenanceList",  produces = MediaType.APPLICATION_JSON_VALUE)
	public ApiResult maxMaintenanceList() {
		return rankingService.maxMaintenanceList();
	}


	/**
	 * 维保单位数据
	 * @param vo
	 * @return
	 */
	@ApiOperation(value = "维保单位数据录入", notes = "维保单位数据录入")
	@PostMapping(value = "/addMaintenance",  produces = MediaType.APPLICATION_JSON_VALUE)
	public ApiResult addMaintenance(@Valid @RequestBody MaintenanceReqVO vo, HttpServletRequest request) {
		UserSession user = httpHelper.getUser(request);
		return rankingService.addOneMaintenance(vo,user);
	}

	/**
	 * 维保单位数据编辑
	 * @param vo
	 * @return
	 */
	@ApiOperation(value = "维保单位数据编辑", notes = "维保单位数据编辑")
	@PostMapping(value = "/editMaintenance",  produces = MediaType.APPLICATION_JSON_VALUE)
	public ApiResult editMaintenance(@Valid @RequestBody MaintenanceReqVO vo) {
		return rankingService.editMaintenance(vo);
	}

	/**
	 * 维保单位数据删除
	 */
	@ApiOperation(value = "维保单位数据删除", notes = "维保单位数据删除")
	@PostMapping(value = "/delMaintenance",  produces = MediaType.APPLICATION_JSON_VALUE)
	public ApiResult delMaintenance(@Valid @RequestBody MaintenanceReqVO vo) {
		return rankingService.delMaintenance(vo);
	}



	/**------------------------------维保单位相关接口结束----------------------------------------------------**/



	/**------------------------------质保单位相关接口开始----------------------------------------------------**/
	/**
	 * 质保单位数据列表
	 * @param vo
	 * @return
	 */
	@ApiOperation(value = "质保单位数据列表", notes = "质保单位数据列表")
	@PostMapping(value = "/qualityList",  produces = MediaType.APPLICATION_JSON_VALUE)
	public ApiResult qualityList(@Valid @RequestBody RankingQueryVO vo) {
		super.checkPageParams(vo);
		vo.setType(1);
		return rankingService.qualityList(vo);
	}

	/**
	 * 质保单位数据编辑
	 * @param vo
	 * @return
	 */
	@ApiOperation(value = "质保单位数据编辑", notes = "质保单位数据编辑")
	@PostMapping(value = "/editQuality",  produces = MediaType.APPLICATION_JSON_VALUE)
	public ApiResult editQuality(@Valid @RequestBody QualityReqVO vo) {
		vo.setType(1);
		return rankingService.editQuality(vo);
	}

	/**
	 * 质保单位数据删除
	 * @param vo
	 * @return
	 */
	@ApiOperation(value = "质保单位数据删除", notes = "质保单位数据删除")
	@PostMapping(value = "/delQuality",  produces = MediaType.APPLICATION_JSON_VALUE)
	public ApiResult delQuality(@Valid @RequestBody QualityReqVO vo) {
		vo.setType(1);
		return rankingService.delQuality(vo);
	}
	/**
	 * 质保单位数据录入
	 * @param vo
	 * @return
	 */
	@ApiOperation(value = "质保单位数据录入", notes = "质保单位数据录入")
	@PostMapping(value = "/addQuality",  produces = MediaType.APPLICATION_JSON_VALUE)
	public ApiResult addQuality(@Valid @RequestBody QualityReqVO vo, HttpServletRequest request) {
		UserSession user = httpHelper.getUser(request);
		vo.setType(1);
		return rankingService.addQuality(vo,user);
	}


	/**------------------------------质保单位相关接口结束----------------------------------------------------**/



	/**------------------------------拓建单位相关接口开始----------------------------------------------------**/
	/**
	 * 拓建单位数据列表
	 * @param vo
	 * @return
	 */
	@ApiOperation(value = "拓建单位数据列表", notes = "拓建单位数据列表")
	@PostMapping(value = "/extensionList",  produces = MediaType.APPLICATION_JSON_VALUE)
	public ApiResult extensionList(@Valid @RequestBody RankingQueryVO vo) {
		super.checkPageParams(vo);
		vo.setType(2);
		return rankingService.qualityList(vo);
	}

	/**
	 * 拓建单位数据编辑
	 * @param vo
	 * @return
	 */
	@ApiOperation(value = "拓建单位数据编辑", notes = "拓建单位数据编辑")
	@PostMapping(value = "/editExtension",  produces = MediaType.APPLICATION_JSON_VALUE)
	public ApiResult editExtension(@Valid @RequestBody QualityReqVO vo) {
		vo.setType(2);
		return rankingService.editQuality(vo);
	}

	@ApiOperation(value = "拓建单位数据删除", notes = "拓建单位数据删除")
	@PostMapping(value = "/delExtension",  produces = MediaType.APPLICATION_JSON_VALUE)
	public ApiResult delExtension(@Valid @RequestBody QualityReqVO vo) {
		vo.setType(2);
		return rankingService.delQuality(vo);
	}

	/**
	 * 拓建单位数据录入
	 * @param vo
	 * @return
	 */
	@ApiOperation(value = "拓建单位数据录入", notes = "拓建单位数据录入")
	@PostMapping(value = "/addExtension",  produces = MediaType.APPLICATION_JSON_VALUE)
	public ApiResult addExtension(@Valid @RequestBody QualityReqVO vo, HttpServletRequest request) {
		UserSession user = httpHelper.getUser(request);
		vo.setType(2);
		return rankingService.addQuality(vo,user);
	}



	/**------------------------------拓建单位相关接口结束----------------------------------------------------**/


}
