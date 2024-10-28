package com.zans.portal.controller;

import com.zans.base.annotion.Record;
import com.zans.base.config.EnumErrorCode;
import com.zans.base.controller.BaseController;
import com.zans.base.office.excel.ExcelHelper;
import com.zans.base.office.excel.ExportConfig;
import com.zans.base.util.DateHelper;
import com.zans.base.util.FileHelper;
import com.zans.base.util.MapBuilder;
import com.zans.base.util.StringHelper;
import com.zans.base.vo.ApiResult;
import com.zans.base.vo.SelectVO;
import com.zans.base.vo.UserSession;
import com.zans.portal.dao.AssetBaselineDao;
import com.zans.portal.dao.DeviceTypeMapper;
import com.zans.portal.dao.SysCustomFieldDao;
import com.zans.portal.model.AssetBaseline;
import com.zans.portal.model.AssetProfile;
import com.zans.portal.model.DeviceType;
import com.zans.portal.model.SysCustomField;
import com.zans.portal.service.*;
import com.zans.portal.util.HttpHelper;
import com.zans.portal.vo.AssetBaselineVO;
import com.zans.portal.vo.AssetBaselineVersionVO;
import com.zans.portal.vo.asset.ExcelAssetBaselineVO;
import com.zans.portal.vo.projectStatusEnum;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static com.zans.base.config.EnumErrorCode.SERVER_UPLOAD_MAX_SIZE_ERROR;
import static com.zans.base.config.EnumErrorCode.SERVER_UPLOAD_MIME_ERROR;
import static com.zans.portal.config.GlobalConstants.*;
import static com.zans.portal.constants.PortalConstants.*;


/**
 * @author beixing
 * @Title: 基线表(AssetBaseline)表控制层
 * @Description:
 * @Version:1.0.0
 * @Since:jdk1.8
 * @date 2021-07-08 14:09:40
 */
@Api(tags = "基线表(AssetBaseline)")
@RestController
@RequestMapping("assetBaseline")
public class AssetBaselineController extends BaseController {
    /**
     * 服务对象
     */
    @Autowired
    private IAssetBaselineService assetBaselineService;

    @Autowired
    private AssetBaselineDao assetBaselineMapper;

    @Autowired
    private HttpHelper httpHelper;

    @Value("${api.export.folder}")
    String exportFolder;

    @Value("${api.upload.folder}")
    String uploadFolder;

    @Autowired
    IFileService fileService;

    @Autowired
    IDeviceTypeService deviceTypeService;

    @Autowired
    private DeviceTypeMapper deviceTypeMapper;

    @Autowired
    private ISysConstantService sysConstantService;

    @Autowired
    private IAssetProfileService assetProfileService;


    @Resource
    private SysCustomFieldDao sysCustomFieldDao;

    /**
     * 新增一条数据
     *
     * @param assetBaselineVO 实体类
     * @return Response对象
     */
    @ApiOperation(value = "基线表新增", notes = "基线表新增")
    @RequestMapping(value = "insert", method = RequestMethod.POST)
    @Record(module = PORTAL_MODULE_BASELINE,operation = PORTAL_LOG_OPERATION_ADD)
    public ApiResult insert(@RequestBody AssetBaselineVO assetBaselineVO, HttpServletRequest httpRequest) {
        UserSession userSession = httpHelper.getUser(httpRequest);
        assetBaselineVO.setCreator(userSession.getUserName());
        assetBaselineVO.setReviser(userSession.getUserName());

        String ip = assetBaselineVO.getIpAddr();
        AssetBaselineVO baselineVO = assetBaselineService.getByIp(ip);
        if(baselineVO!=null){
            return ApiResult.error("该IP已存在!");
        }
        String mac = assetBaselineVO.getMac();
        if(StringUtils.isEmpty(mac) || mac.length()>12){
            return ApiResult.error("mac不能为空或长度超过12位！");
        }
        int result = assetBaselineService.insertOne(assetBaselineVO);
        if (result > 0) {
            return ApiResult.success();
        }
        return ApiResult.error("新增失败");
    }

    /**
     * 修改一条数据
     *
     * @param assetBaselineVO 实体类
     * @return Response对象
     */
    @ApiOperation(value = "基线表修改", notes = "基线表修改")
    @RequestMapping(value = "edit", method = RequestMethod.POST)
    @Record(module = PORTAL_MODULE_BASELINE,operation = PORTAL_LOG_OPERATION_EDIT)
    public ApiResult update(@RequestBody AssetBaselineVO assetBaselineVO, HttpServletRequest httpRequest) {
        UserSession userSession = httpHelper.getUser(httpRequest);
        assetBaselineService.update(assetBaselineVO);
        return ApiResult.success();
    }

    /**
     * 删除一条数据
     *
     * @param assetBaselineVO 参数对象
     * @return Response对象
     */
    @ApiOperation(value = "基线表删除", notes = "基线表删除")
    @RequestMapping(value = "del", method = RequestMethod.POST)
    @Record
    public ApiResult delete(@RequestBody AssetBaselineVO assetBaselineVO, HttpServletRequest httpRequest) {
        UserSession userSession = httpHelper.getUser(httpRequest);
        AssetBaseline dv = assetBaselineService.getById(assetBaselineVO.getId());
        if (dv == null){
            ApiResult.error("不存在此数据!");
        }
        AssetProfile assetProfile = assetProfileService.findByIdAddr(dv.getIpAddr());
        if (assetProfile != null){
            ApiResult.error("存在资产无法删除!");
        }
        assetBaselineService.deleteById(assetBaselineVO.getId());
        return ApiResult.success();
    }


    /**
     * 分页查询
     */
    @ApiOperation(value = "基线表查询", notes = "基线表查询")
    @RequestMapping(value = "list", method = RequestMethod.POST)
    public ApiResult list(@RequestBody AssetBaselineVO assetBaselineVO) {
        super.checkPageParams(assetBaselineVO);
        SelectVO selectVO = sysConstantService.findSelectVOByKey(BASE_SORT);
        super.checkPageParams(assetBaselineVO, "");
        assetBaselineVO.setOrderString(selectVO.getItemValue());
        return assetBaselineService.list(assetBaselineVO);
    }

    /**
     * 基线历史分页查询
     */
    @ApiOperation(value = "基线历史分页查询", notes = "基线历史分页查询")
    @RequestMapping(value = "historyBaselineList", method = RequestMethod.POST)
    public ApiResult historyBaselineList(@RequestBody AssetBaselineVersionVO assetBaselineVO) {
        super.checkPageParams(assetBaselineVO);
        return assetBaselineService.historyBaselineList(assetBaselineVO);
    }


    /**
     * 详情查询
     */
    @ApiOperation(value = "基线表详情", notes = "基线表详情")
    @RequestMapping(value = "view", method = RequestMethod.GET)
    public ApiResult view(@RequestParam("ipAddr") String ipAddr) {
        return ApiResult.success(assetBaselineService.queryByIp(ipAddr));
    }


    /**
     * 详情查询
     */
    @ApiOperation(value = "基线变更历史记录展示", notes = "基线变更历史记录展示")
    @RequestMapping(value = "historyList", method = RequestMethod.GET)
    public ApiResult historyList(@RequestParam("ipAddr") String ipAddr) {
        return assetBaselineService.historyList(ipAddr);
    }

    /**
     * 详情查询
     */
    @ApiOperation(value = "准入放行界面的比较信息查基线表", notes = "准入放行界面的比较信息查基线表")
    @RequestMapping(value = "compareBaseLine", method = RequestMethod.GET)
    public ApiResult compareBaseLine(@RequestParam("id") Long id) {
        return assetBaselineService.compareBaseLine(id);
    }

    @ApiOperation(value = "/export", notes = "线路资产列表导出")
    @RequestMapping(value = "/export", method = {RequestMethod.POST})
    @ResponseBody
    @Record(module = PORTAL_MODULE_BASELINE,operation = PORTAL_LOG_OPERATION_EXPORT)
    public void export(@RequestBody AssetBaselineVO assetBaselineVO, HttpServletRequest request,
                       HttpServletResponse response) throws Exception {
        String filePath = this.exportFolder + "/" + "asset_baseline_" + DateHelper.getTodayShort() + "_"
                + StringHelper.getUuid() + ".xlsx";
        StringBuilder fileCnName = new StringBuilder();
        String name = "基线数据";
        fileCnName.append("基线详情数据_");
        fileCnName.append(DateHelper.formatDate(new Date(), "yyyyMMddHHmm"));
        fileCnName.append(".xlsx");
//        Page<ExcelAssetBaselineVO> page = PageHelper.startPage(assetBaselineVO.getPageNum(), assetBaselineVO.getPageSize());//7/14 取消导出分页
        List<ExcelAssetBaselineVO> excelAssetBaselineVOS = assetBaselineMapper.excelSelect(assetBaselineVO);

        if (excelAssetBaselineVOS == null || excelAssetBaselineVOS.size() == 0) {
            this.setErrorToResponse(request, response, EnumErrorCode.SERVER_DOWNLOAD_NULL_ERROR,
                    EnumErrorCode.SERVER_DOWNLOAD_NULL_ERROR.getMessage());
            return;
        }
        ExportConfig exportConfig = ExportConfig.builder().seqColumn(true).wrap(true)
                .extraContentPosition(ExportConfig.TOP).extraContent(null).freeze(true).freezeCol(10).freezeRow(1)
                .build();
        List<DeviceType> deviceTypes = deviceTypeMapper.selectAll();
        external:for (ExcelAssetBaselineVO excelAssetBaselineVO : excelAssetBaselineVOS) {
            Integer projectStatus = excelAssetBaselineVO.getProjectStatus();
            if (projectStatus != null){
                if (projectStatus == projectStatusEnum.IN_CONSTRUCTION.getCode()){
                    excelAssetBaselineVO.setProjectStatusName(projectStatusEnum.IN_CONSTRUCTION.getDesc());
                }else if (projectStatus == projectStatusEnum.QUALITY_GUARANTEE.getCode()){
                    excelAssetBaselineVO.setProjectStatusName(projectStatusEnum.QUALITY_GUARANTEE.getDesc());
                }else if (projectStatus == projectStatusEnum.OVER_GUARANTEE.getCode()){
                    excelAssetBaselineVO.setProjectStatusName(projectStatusEnum.OVER_GUARANTEE.getDesc());
                }else {
                    excelAssetBaselineVO.setProjectStatusName(projectStatusEnum.MAINTENANCE.getDesc());
                }
            }else {
                excelAssetBaselineVO.setProjectStatusName("");
            }

            Integer bindStatus = excelAssetBaselineVO.getBindStatus();
            if (bindStatus != null){
                if (bindStatus == 0){
                    excelAssetBaselineVO.setBindStatusName("未绑定");
                }else {
                    excelAssetBaselineVO.setBindStatusName("已绑定");
                }
            }else {
                excelAssetBaselineVO.setBindStatusName("未绑定");
            }

            for (DeviceType deviceType : deviceTypes) {
                if (deviceType.getTypeId().equals(excelAssetBaselineVO.getDeviceType())){
                    excelAssetBaselineVO.setDeviceTypeName(deviceType.getTypeName());
                    continue external;
                }
            }
        }
        fileService.exportExcelFile(excelAssetBaselineVOS, name, filePath, exportConfig);
        this.download(filePath, fileCnName.toString(), request, response);
        if (assetBaselineVO !=null && assetBaselineVO.getDeleteBaselineFlag().intValue()==1){
            //1-导出基线excel后删除基线数据; 0-只导出基线excel
            assetBaselineMapper.deleteBaseline();
            assetBaselineMapper.deleteBaselineVersion();
        }
    }

//    @ApiOperation(value = "/upload", notes = "基线数据导入")
//    @PostMapping("/upload")
//    @Record(module = PORTAL_MODULE_BASELINE,operation = PORTAL_LOG_OPERATION_IMPORT)
//    public ApiResult uploadFile(@RequestParam("file") MultipartFile file,
//                                HttpServletRequest request) throws Exception {
//        if (!ExcelHelper.checkExtension(file)) {
//            return ApiResult.error(SERVER_UPLOAD_MIME_ERROR).appendMessage("不是Excel类型");
//        }
//        long size = file.getSize();
//        if (size > UPLOAD_FILE_MAX_SIZE) {
//            return ApiResult.error(SERVER_UPLOAD_MAX_SIZE_ERROR)
//                    .appendMessage("最大" + FileHelper.getSizeInMb(UPLOAD_FILE_MAX_SIZE) + "MB");
//        }
//
//        // 上传文件，持久化到本地，写数据库
//        String originName = file.getOriginalFilename();
//        InputStream inputStream = file.getInputStream();
//        String newFileName = fileService.getNewFileName(originName);
//        boolean saved = FileHelper.saveFile(file, uploadFolder, newFileName);
//        if (saved) {
//            UserSession userSession = getUserSession(request);
//            assetBaselineService.readExcel(file, inputStream ,userSession);
//        }
//        return ApiResult.success();
//    }

    @ApiOperation(value = "/upload", notes = "基线数据导入")
    @PostMapping("/upload")
    @Record(module = PORTAL_MODULE_BASELINE,operation = PORTAL_LOG_OPERATION_IMPORT)
    public ApiResult uploadFile(@RequestParam("file") MultipartFile file,
                                HttpServletRequest request) throws Exception {
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
        if (saved) {
            UserSession userSession = getUserSession(request);
            assetBaselineService.excelTransform(newFileName,userSession);
        }
        return ApiResult.success();
    }


    @ApiOperation(value = "/download/template", notes = "文件下载")
    @GetMapping("/download/template")
    @Record(module = PORTAL_MODULE_BASELINE,operation = PORTAL_LOG_TEMPLATE_DOWNLOAD)
    public void downloadFile(HttpServletRequest request,
                             HttpServletResponse response) throws Exception {
        String filePath = this.uploadFolder + "/template/基线数据模板.xlsx";
        String date = DateHelper.formatDate(new Date(), "yyyyMMddHHmmss");
        String fileCnName = "基线数据模板" + date + ".xlsx";
        this.download(filePath, fileCnName, request, response);
    }

    public UserSession getUserSession(HttpServletRequest request) {
        return this.httpHelper.getUser(request);
    }

    /**
     * 详情查询
     */
    @ApiOperation(value = "初始化数据", notes = "初始化数据")
    @RequestMapping(value = "init", method = RequestMethod.GET)
    public ApiResult init() {
        List<SelectVO> deviceList = deviceTypeService.findDeviceTypeToSelect();
        SysCustomField sysCustomField = new SysCustomField();
        sysCustomField.setFiledEnable(1);
        sysCustomField.setModuleName(MODULE_NAME_BASELINE_LIST);
        sysCustomField.setFieldType(1);
        Map<String, String> data = new LinkedHashMap<>();
        data = getFiledMap(sysCustomField, data);
        Map<String, String> queryData = new LinkedHashMap<>();
        sysCustomField.setFieldType(2);
        queryData = getFiledMap(sysCustomField, queryData);
        Map<String, Object> result = MapBuilder.getBuilder()
                .put(MODULE_DEVICE, deviceList)
                .put(CUSTOM_COLUMN, data)
                .put(CUSTOM_QUERY, queryData)
                .build();
        return ApiResult.success(result);
    }

    private Map<String, String> getFiledMap(SysCustomField sysCustomField, Map<String, String> data) {
        List<SysCustomField> sysCustomFields = sysCustomFieldDao.queryAll(sysCustomField);
        if(!org.springframework.util.StringUtils.isEmpty(sysCustomFields)){
            //这个方法的排序
//            data = sysCustomFields.stream().collect(Collectors.toMap(SysCustomField::getFieldKey, SysCustomField::getFieldName));
            for (SysCustomField customField : sysCustomFields) {
                data.put(customField.getFieldKey(),customField.getFieldName());
            }

        }
        return data;
    }

}
