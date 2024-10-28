package com.zans.mms.controller.pc;

import com.zans.base.annotion.Record;
import com.zans.base.controller.BaseController;
import com.zans.base.office.excel.ExcelHelper;
import com.zans.base.util.DateHelper;
import com.zans.base.util.FileHelper;
import com.zans.base.util.HttpHelper;
import com.zans.base.util.MapBuilder;
import com.zans.base.vo.ApiResult;
import com.zans.base.vo.SelectVO;
import com.zans.base.vo.UserSession;
import com.zans.mms.config.MMSConstants;
import com.zans.mms.model.PoManager;
import com.zans.mms.service.*;
import com.zans.mms.service.impl.PoManagerServiceImpl;
import com.zans.mms.vo.devicepoint.DevicePointQueryVO;
import com.zans.mms.vo.po.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import static com.zans.base.config.EnumErrorCode.SERVER_UPLOAD_MAX_SIZE_ERROR;
import static com.zans.base.config.EnumErrorCode.SERVER_UPLOAD_MIME_ERROR;
import static com.zans.base.config.GlobalConstants.*;
import static com.zans.base.config.GlobalConstants.SYS_DICT_KEY_PATROL_CHECK_RESULT;

/**
 * @author qitian
 * @Title: zans-mms-server
 * @Description:
 * @Version:1.0.0
 * @Since:jdk1.8
 * @date 2021/7/21
 */
@RestController
@Api(tags = "舆情投诉管理")
@RequestMapping("po")
public class PoManagerController  extends BaseController {

	@Autowired
	private IPoManagerService poManagerService;

	@Autowired
	private IPoBaseDictService poBaseDictService;

	@Autowired
	HttpHelper httpHelper;

	@Autowired
	IFileService fileService;


	@Value("${api.upload.folder}")
	String uploadFolder;


	@Autowired
	IDevicePointService devicePointService;

	@Autowired
	private ITicketService ticketService;


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
		//事件和设备类型默认值
		List<SelectVO> deviceType = poBaseDictService.findPoDeviceType();
		//事件和原因联动传值
		Map<String, List<SelectVO>> eventAndReason=poBaseDictService.getEventAndReason();
		Map<String, Object> result = MapBuilder.getBuilder()
				.put(AREA_ID,areaIdList)
				.put(PO_EVENT,poEventList)
				.put(EVENT_SOURCE,eventSourceList)
				.put(PO_TYPE,poTypeList)
				.put(REASON,reasonList)
				.put(REPAIR_STATUS,statusList)
				.put(DUTY_CONTACT,dutyContactList)
				.put(PO_DEVICE_TYPE,deviceType)
				.put("eventAndReason",eventAndReason)
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
		if("0302".equals(userSession.getRoleId())){
			vo.setDataPermissions(3);
			vo.setCreator(userSession.getOrgId());
		}
		return poManagerService.list(vo);
	}

	/**
	 * 舆情投诉数据新增
	 * @param poManager
	 * @return
	 */
	@PostMapping("create")
	public ApiResult create(@RequestBody PoManager poManager, HttpServletRequest request){
		//如果问题编号不为空 检验问题编号是否已存在
		UserSession userSession = httpHelper.getUser(request);
		poManager.setCreator(userSession.getUserName());
		return poManagerService.create(poManager,userSession);
	}


	/**
	 * 舆情投诉数据修改
	 * @param poManager
	 * @return
	 */
	@PostMapping("update")
	public ApiResult update(@RequestBody PoManager poManager,HttpServletRequest request){
		UserSession userSession = httpHelper.getUser(request);
		return poManagerService.update(poManager,userSession);
	}


	/**
	 * 舆情投诉数据删除
	 * @param id
	 * @return
	 */
	@GetMapping("delete")
	public ApiResult delete(@RequestParam("id") Long id){
		return poManagerService.delete(id);
	}

	/**
	 * 舆情投诉数据详情
	 * @param id
	 * @return
	 */
	@GetMapping("view")
	public ApiResult view(@RequestParam("id") Long id){
		return poManagerService.view(id);
	}


	/**
	 * 舆情投诉数据导出
	 * @param
	 * @return
	 */
	@PostMapping("export")
	public void export(HttpServletRequest request,
					   HttpServletResponse response,
					   @RequestBody PoManagerExportReqVO vo) throws Exception {
		String date = DateHelper.formatDate(new Date(), "yyyyMMddHHmmss") ;
		String fileName = "舆情投诉数据导出" + date+".xls";
		String filePath=poManagerService.export(vo,fileName);
		this.download(filePath, fileName, request, response);
	}


	/**
	 * 数据导入接口
	 * @param file 上传的excel文件
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@ApiOperation(value = "/import", notes = "上传文件，导入数据")
	@PostMapping("/import")
	@ResponseBody
	@Record(module = MMSConstants.MODULE_PUBLIC_OPINION,operation = MMSConstants.LOG_OPERATION_IMPORT)
	public ApiResult uploadFile(@RequestParam("file") MultipartFile file, HttpServletRequest request) throws Exception {
		if (!ExcelHelper.checkExtension(file)) {
			return ApiResult.error(SERVER_UPLOAD_MIME_ERROR).appendMessage("不是Excel类型");
		}
		long size = file.getSize();
		if (size > UPLOAD_FILE_MAX_SIZE) {
			return ApiResult.error(SERVER_UPLOAD_MAX_SIZE_ERROR)
					.appendMessage("最大" + FileHelper.getSizeInMb(UPLOAD_FILE_MAX_SIZE) + "MB");
		}
		// 上传文件，持久化到本地，写数据库
		String originName = file.getOriginalFilename();
		String newFileName = fileService.getNewFileName(originName);
		boolean saved = FileHelper.saveFile(file, uploadFolder, newFileName);
		if (saved){
			UserSession userSession =  httpHelper.getUser(request);
			return poManagerService.batchAddPo(newFileName, originName,userSession);
		}
		return ApiResult.error("file save error");
	}


	@ApiOperation(value = "/download/errorFile", notes = "下载上传失败的文件")
	@RequestMapping(value = "/download/errorFile")
	@Record(module = MMSConstants.MODULE_ASSET,operation = MMSConstants.LOG_OPERATION_DOWNLOAD)
	public void errorFile(HttpServletRequest request,
						  HttpServletResponse response, @RequestParam(value = "errorFilePath") String errorFilePath) throws Exception {
		String date = DateHelper.formatDate(new Date(), "yyyyMMddHHmmss");
		String fileCnName = "舆情投诉信息上传失败" + date+".xls";
		this.download(errorFilePath, fileCnName, request, response);
	}


	/**
	 * 资产通用导入模板下载
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@ApiOperation(value = "/download/template", notes = "资产导入模板下载")
	@GetMapping("/download/template")
	@Record(module = MMSConstants.MODULE_ASSET,operation = MMSConstants.LOG_OPERATION_DOWNLOAD)
	public void downloadTemplate ( HttpServletRequest request, HttpServletResponse response) throws Exception {
		String date = DateHelper.formatDate(new Date(), "yyyyMMddHHmmss");
		String filePath = this.uploadFolder + "template/舆情投诉数据导入-excel模板.xlsx";
		String fileCnName = "舆情投诉数据导入模板" + date + ".xlsx";
		this.download(filePath, fileCnName, request, response);
	}


	/**
	 * 获取所有数据
	 *
	 * @param vo DevicePointReqVo
	 * @return ApiResult<PageInfo < DevicePointResVo>>
	 */
	@ApiOperation(value = "获取点位列表", notes = "获取信号机点位列表")
	@PostMapping(value = "/trafficSignalList", produces = MediaType.APPLICATION_JSON_VALUE)
	public ApiResult getList(@Valid @RequestBody PoManagerResVO vo) {
		return devicePointService.getTrafficSignalList(vo);
	}


	/**
	 * 负责人推送给外场维修人员
	 *
	 */
	@ApiOperation(value = "负责人推送给外场维修人员", notes = "负责人推送给外场维修人员")
	@PostMapping(value = "/pushToRepair", produces = MediaType.APPLICATION_JSON_VALUE)
	public ApiResult pushToRepair(@RequestBody PoManagerPushReqVO vo, HttpServletRequest request) {
		UserSession userSession = httpHelper.getUser(request);
		return poManagerService.pushToRepair(vo,userSession);
	}


	/**
	 * 根据辖区确定维修人员
	 *
	 */
	@ApiOperation(value = "根据辖区确定维修人员", notes = "根据辖区确定维修人员")
	@PostMapping(value = "/getRepairPerson", produces = MediaType.APPLICATION_JSON_VALUE)
	public ApiResult getRepairPerson(@RequestBody PoManager poManager) {
		if(StringUtils.isEmpty(poManager.getAreaId())){
			return ApiResult.error("辖区传值为空");
		}
		return poManagerService.getRepairPerson(poManager.getAreaId());
	}

	/**
	 * 唯一性校验
	 *
	 */
	@ApiOperation(value = "唯一性校验", notes = "唯一性校验")
	@PostMapping(value = "/check", produces = MediaType.APPLICATION_JSON_VALUE)
	public ApiResult check(@RequestBody PoManager poManager) {
		if(null==poManager.getOriginalProblemId()){
			return ApiResult.success();
		}
		return poManagerService.check(poManager);
	}

	/**
	 * 维修前图片维护
	 */
	@PostMapping("/img")
	public ApiResult beforeImg (@RequestBody PoManagerReqVO vo){
		return poManagerService.imgUpdate(vo);
	}


	@PostMapping("repeatMark")
	public ApiResult repeatMark(@RequestBody PoManager poManager){
		poManagerService.repeatMark(poManager);
		return ApiResult.success();
	}

	@PostMapping("batchUpdate")
	public ApiResult repeatMark(@RequestBody PoManagerRepeatMarkVO vo,HttpServletRequest request){
		UserSession userSession = httpHelper.getUser(request);
		poManagerService.batchUpdate(vo,userSession);
		return ApiResult.success();
	}

	/**-----------------------------舆情转工单开始-------------------------------**/

	/**
	 * 舆情转工单
	 * @param vo
	 * @param request
	 * @return
	 */
	@PostMapping("po2Ticket")
	public ApiResult po2Ticket(@RequestBody PoManagerRepeatMarkVO vo,HttpServletRequest request){
		UserSession userSession = httpHelper.getUser(request);
		vo.setCreator(userSession.getUserName());
		return ticketService.po2Ticket(vo);
	}










	/**----------------------------舆情转工单结束---------------------------------**/


}
