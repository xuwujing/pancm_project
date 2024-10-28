package com.zans.mms.controller.applet;

import com.zans.base.controller.BaseController;
import com.zans.base.exception.BusinessException;
import com.zans.base.util.MapBuilder;
import com.zans.base.vo.ApiResult;
import com.zans.base.vo.SelectVO;
import com.zans.base.vo.UserSession;
import com.zans.mms.model.PoManager;
import com.zans.mms.model.PoManagerLogs;
import com.zans.mms.service.IPoBaseDictService;
import com.zans.mms.service.IPoManagerService;
import com.zans.mms.vo.SeriesVO;
import com.zans.mms.vo.po.PoManagerCharRepVO;
import com.zans.mms.vo.po.PoManagerDataVO;
import com.zans.mms.vo.po.PoManagerLineChartVO;
import com.zans.mms.vo.po.PoManagerReqVO;
import com.zans.mms.vo.ticket.AppTicketCharReqVO;
import com.zans.mms.vo.ticket.AppTicketChartVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.*;

import static com.zans.base.config.GlobalConstants.*;
import static com.zans.base.config.GlobalConstants.REASON;

/**
 * @author qitian
 * @Title: zans-mms-server
 * @Description:
 * @Version:1.0.0
 * @Since:jdk1.8
 * @date 2021/8/4
 */
@Api(value = "/小程序舆情", tags = {"/小程序舆情"})
@RestController
@Validated
@Slf4j
@RequestMapping("app/po")
public class AppPoManagerController extends BaseController {

	@Autowired
	private IPoManagerService poManagerService;

	@Autowired
	private IPoBaseDictService poBaseDictService;

	/**
	 * 初始化字典方法
	 * @return
	 */
	@RequestMapping("init")
	public ApiResult init(){

		List<SelectVO> areaIdList = poBaseDictService.findItemsByDict(AREA_ID);
		List<SelectVO> poEventList = poBaseDictService.findItemsByDict(PO_EVENT);
		List<SelectVO> eventSourceList = poBaseDictService.findItemsByDict(EVENT_SOURCE);
		List<SelectVO> poTypeList = poBaseDictService.findItemsByDict(PO_TYPE);
		List<SelectVO> reasonList = poBaseDictService.findItemsByDict(REASON);
		List<SelectVO> statusList = poBaseDictService.findItemsByDict(REPAIR_STATUS);
		List<SelectVO> dutyContactList = poBaseDictService.findItemsByDict(DUTY_CONTACT);
		List<SelectVO> faultPhenomenonList = poBaseDictService.findItemsByDict(FAULT_PHENOMENON);
		Map<String, Object> result = MapBuilder.getBuilder()
				.put(AREA_ID,areaIdList)
				.put(PO_EVENT,poEventList)
				.put(EVENT_SOURCE,eventSourceList)
				.put(PO_TYPE,poTypeList)
				.put(REASON,reasonList)
				.put(REPAIR_STATUS,statusList)
				.put(DUTY_CONTACT,dutyContactList)
				.put(FAULT_PHENOMENON,faultPhenomenonList)
				.build();
		return ApiResult.success(result);
	}


	/**
	 * 列表查询
	 * @param vo
	 * @return
	 */
	@PostMapping("list")
	public ApiResult list(@RequestBody PoManagerReqVO vo,HttpServletRequest request){
		super.checkPageParams(vo);
		//数据权限
		/**
		 * 业主、监理、王惠 查看所有舆情
		 * 三大片区运维人员只能查看自己负责的舆情，分配给三大片区负责人的舆情（按责任人）
		 * 信号机维护人员2名（广电），仅能查看自己负责的舆情
		 */
		UserSession userSession = httpHelper.getUser(request);
		/*if("0501".equals(userSession.getRoleId())||"0101".equals(userSession.getRoleId())||"wangh".equals(userSession.getUserName())||"lgwy".equals(userSession.getUserName())){
			// 1全部 2自己负责的 3 分配给自己的
			vo.setDataPermissions(1);
		}else if("0302".equals(userSession.getRoleId())){
			vo.setDataPermissions(3);
			vo.setCreator(userSession.getOrgId());
		} else{
			vo.setDataPermissions(2);
			vo.setCreator(userSession.getUserName());
		}*/
		if("0302".equals(userSession.getRoleId())||"0301".equals(userSession.getRoleId())){
			vo.setDataPermissions(3);
			vo.setCreator(userSession.getOrgId());
		}
		return poManagerService.appList(vo);
	}

	/**
	 * 详情
	 * @param id
	 * @return
	 */
	@GetMapping("view")
	public ApiResult view(@RequestParam("id") Long id){
		return poManagerService.appView(id);
	}

	/**
	 * 维修前图片维护
	 */
	@PostMapping("before/img")
	public ApiResult beforeImg (@RequestBody PoManagerReqVO vo){
		return poManagerService.imgUpdate(vo);
	}


	@GetMapping("logs")
	public ApiResult logs(@RequestParam("id") Long id){
		return poManagerService.getLogs(id);
	}

	@PostMapping("repair")
	public ApiResult repair (@RequestBody PoManagerLogs poManagerLogs, HttpServletRequest request){
		UserSession userSession = httpHelper.getUser(request);
		poManagerLogs.setCreator(userSession.getUserName());
		poManagerLogs.setOpPlatform(2);
		return poManagerService.saveLog(poManagerLogs,userSession);
	}

	/**
	 * 舆情投诉数据修改
	 * @param poManager
	 * @return
	 */
	@PostMapping("update")
	public ApiResult update(@RequestBody PoManager poManager, HttpServletRequest request){
		UserSession userSession = httpHelper.getUser(request);
		return poManagerService.update(poManager,userSession);
	}

	@PostMapping("repeatMark")
	public ApiResult repeatMark(@RequestBody PoManager poManager){
		poManagerService.repeatMark(poManager);
		return ApiResult.success();
	}

	/**---------------------------------报表开始--------------------------------------------**/
	@ApiOperation(value = "主城区投诉原因占比", notes = "主城区投诉原因占比")
	@RequestMapping(value = "/getReason", method = RequestMethod.POST)
	public ApiResult getReason(@RequestBody AppTicketCharReqVO appTicketCharReqVO) {
		if(null==appTicketCharReqVO.getType()&& StringUtils.isEmpty(appTicketCharReqVO.getStartTime())
				&&StringUtils.isEmpty(appTicketCharReqVO.getEndTime())){
			appTicketCharReqVO.setType(1);
		}else{
			if(StringUtils.isEmpty(appTicketCharReqVO.getStartTime()) ||StringUtils.isEmpty(appTicketCharReqVO.getEndTime())){
				throw new BusinessException("开始时间或结束时间未传值！");
			}
			appTicketCharReqVO.setType(null);
		}
		AppTicketChartVO appTicketChartVO= poManagerService.getReason(appTicketCharReqVO);
		return ApiResult.success(appTicketChartVO);
	}

	@ApiOperation(value = "舆情数量统计", notes = "舆情数量统计")
	@RequestMapping(value = "/getPoTable", method = RequestMethod.POST)
	public ApiResult getPoTable(@RequestBody AppTicketCharReqVO appTicketCharReqVO) {
		PoManagerCharRepVO poManagerCharRepVO = new PoManagerCharRepVO();
		if(null==appTicketCharReqVO.getType()&& StringUtils.isEmpty(appTicketCharReqVO.getStartTime())
				&&StringUtils.isEmpty(appTicketCharReqVO.getEndTime())){
			appTicketCharReqVO.setType(1);
			//根据当前日期设置日期和星期几
			poManagerCharRepVO.setCurrentDate(new SimpleDateFormat("yyyy/MM/dd").format(new Date()));
			poManagerCharRepVO.setCurrentWeekDay(this.getWeekOfDate(new Date()));
		}else{
			if(StringUtils.isEmpty(appTicketCharReqVO.getStartTime()) ||StringUtils.isEmpty(appTicketCharReqVO.getEndTime())){
				throw new BusinessException("开始时间或结束时间未传值！");
			}
			appTicketCharReqVO.setType(null);
		}
		List<String> remoteCity = new ArrayList<>();
		Collections.addAll(remoteCity,"东新","东西湖","汉南","黄陂","蔡甸","江夏","新洲");
		List<PoManagerDataVO> poManagerDataVOList= poManagerService.getPoTable(appTicketCharReqVO);
		List<PoManagerDataVO> centerCityData = new ArrayList<>();
		List<PoManagerDataVO> remoteCityData = new ArrayList<>();
		for(PoManagerDataVO poManagerDataVO : poManagerDataVOList){
			if(remoteCity.contains(poManagerDataVO.getAreaName())){
				remoteCityData.add(poManagerDataVO);
			}else{
				centerCityData.add(poManagerDataVO);
			}
		}
		poManagerCharRepVO.setPoManagerDataVOList(poManagerDataVOList);
		Integer total=poManagerService.getCount(appTicketCharReqVO);
		poManagerCharRepVO.setTotal(total);
		Integer mainCityNum = poManagerService.getMainCityCount(appTicketCharReqVO);
		poManagerCharRepVO.setMainCityCount(mainCityNum);
		Integer remoteCityNum = poManagerService.getRemoteCityCount(appTicketCharReqVO);
		poManagerCharRepVO.setRemoteCityCount(remoteCityNum);
		poManagerCharRepVO.setCenterCityData(centerCityData);
		poManagerCharRepVO.setRemoteCityData(remoteCityData);
		return ApiResult.success(poManagerCharRepVO);
	}


	@ApiOperation(value = "舆情折线图", notes = "舆情折线图")
	@RequestMapping(value = "/getLineChart", method = RequestMethod.POST)
	public ApiResult getLineChart(@RequestBody AppTicketCharReqVO appTicketCharReqVO) {
		PoManagerLineChartVO poManagerCharRepVO = new PoManagerLineChartVO();
		if(null==appTicketCharReqVO.getType()&& StringUtils.isEmpty(appTicketCharReqVO.getStartTime())
				&&StringUtils.isEmpty(appTicketCharReqVO.getEndTime())){
			appTicketCharReqVO.setType(1);
			//根据当前日期设置日期和星期几
		}else{
			if(StringUtils.isEmpty(appTicketCharReqVO.getStartTime()) ||StringUtils.isEmpty(appTicketCharReqVO.getEndTime())){
				throw new BusinessException("开始时间或结束时间未传值！");
			}
			appTicketCharReqVO.setType(null);
		}
		Integer max=0;
		List<PoManagerDataVO> poManagerDataVOList= poManagerService.getPoTable(appTicketCharReqVO);
		SeriesVO totalSeriesVO = new SeriesVO();
		totalSeriesVO.setName("投诉量");
		List<String> totalSeriesVOData = new ArrayList<>();
		SeriesVO completeSeriesVO = new SeriesVO();
		completeSeriesVO.setName("完工数");
		List<String> completeSeriesVOData = new ArrayList<>();
		SeriesVO timeoutSeriesVO = new SeriesVO();
		timeoutSeriesVO.setName("超时");
		List<String> timeoutSeriesVOData = new ArrayList<>();
		List<String> categories = new ArrayList<>();
		List<SeriesVO> seriesVOList = new ArrayList<>();
		for(PoManagerDataVO poManagerDataVO :poManagerDataVOList){
			categories.add(poManagerDataVO.getAreaName());
			if(poManagerDataVO.getTotalCount()>max){
				max=poManagerDataVO.getTotalCount();
			}
			totalSeriesVOData.add(poManagerDataVO.getTotalCount().toString());
			completeSeriesVOData.add(poManagerDataVO.getCompleteCount().toString());
			timeoutSeriesVOData.add(poManagerDataVO.getTimeOutCount().toString());
		}
		totalSeriesVO.setData(totalSeriesVOData);
		completeSeriesVO.setData(completeSeriesVOData);
		timeoutSeriesVO.setData(timeoutSeriesVOData);
		seriesVOList.add(totalSeriesVO);
		seriesVOList.add(completeSeriesVO);
		seriesVOList.add(timeoutSeriesVO);
		poManagerCharRepVO.setCategories(categories);
		poManagerCharRepVO.setSeriesVOList(seriesVOList);
		poManagerCharRepVO.setMax(max);
		return ApiResult.success(poManagerCharRepVO);
	}


	/**
	 * 获取当前日期是星期几
	 *
	 * @param date
	 * @return 当前日期是星期几
	 */
	public String getWeekOfDate(Date date) {
		String[] weekDays = { "星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六" };
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		int w = cal.get(Calendar.DAY_OF_WEEK) - 1;
		if (w < 0){
			w = 0;
		}
		return weekDays[w];
	}











	/**----------------------------------报表结束--------------------------------------------**/

}
