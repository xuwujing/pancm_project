package com.zans.portal.controller;

import com.zans.base.annotion.Record;
import com.zans.base.office.excel.ExcelHelper;
import com.zans.base.office.excel.ExportConfig;
import com.zans.base.util.DateHelper;
import com.zans.base.util.FileHelper;
import com.zans.base.util.MapBuilder;
import com.zans.base.util.StringHelper;
import com.zans.base.vo.ApiResult;
import com.zans.base.vo.PageResult;
import com.zans.base.vo.SelectVO;
import com.zans.base.vo.UserSession;
import com.zans.portal.model.DeviceType;
import com.zans.portal.model.SysBrand;
import com.zans.portal.model.TDeviceModelOfficial;
import com.zans.portal.service.*;
import com.zans.portal.vo.device.DeviceMergeVO;
import com.zans.portal.vo.device.DeviceResponseVO;
import com.zans.portal.vo.device.DeviceSearchVO;
import com.zans.portal.vo.device.ExcelUnknownDeviceVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.*;
import java.util.stream.Collectors;

import static com.zans.base.config.EnumErrorCode.*;
import static com.zans.portal.config.GlobalConstants.*;
import static com.zans.portal.constants.PortalConstants.*;

@Api(value = "/model/official", tags = {"/model/official ~ 设备型号管理"})
@RestController
@RequestMapping("/model/official")
@Validated
@Slf4j
public class DeviceModelOfficialController extends BasePortalController {



    @Autowired
    ITDeviceModelOfficialService service;
    @Autowired
    IDeviceTypeService deviceTypeService;
    @Autowired
    IConstantItemService constantItemService;
    @Autowired
    ILogOperationService logOperationService;
    @Autowired
    private ISysBrandService brandService;
    @Value("${api.export.folder}")
    String exportFolder;
    @Value("${api.upload.folder}")
    String uploadFolder;
    @Autowired
    IFileService fileService;



    @ApiOperation(value = "/list", notes = "设备型号查询")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "req", value = "查询条件", required = true,
                    dataType = "DeviceSearchVO", paramType = "body")
    })
    @RequestMapping(value = "/list", method = {RequestMethod.POST})
    @ResponseBody
    public ApiResult getList(@RequestBody DeviceSearchVO req) {
        List<SelectVO> deviceList = deviceTypeService.findDeviceTypeToSelect();
        List<SelectVO> muteList = constantItemService.findItemsByDict(MODULE_ARP_MUTE);
        List<SelectVO> brandList = new ArrayList<>();
        brandService.getAll().forEach(e -> {
            SelectVO vo = new SelectVO();
            vo.setItemKey(e.getBrandId());
            vo.setItemValue(e.getBrandName());
            brandList.add(vo);
        });
        PageResult<DeviceResponseVO> pageResult = service.getOfficialPage(req);

        Map<String, Object> result = MapBuilder.getBuilder()
                .put(MODULE_DEVICE, deviceList)
                .put(MODULE_ARP_MUTE, muteList)
                .put(INIT_DATA_TABLE, pageResult)
                .put("brands", brandList)
                .build();
        return ApiResult.success(result);
    }

    @ApiOperation(value = "/insertOrUpdate", notes = "新增或修改设备型号")
    @ApiImplicitParam(name = "mergeVO", value = "新增或修改设备型号", required = true,
            dataType = "DeviceMergeVO", paramType = "body")
    @RequestMapping(value = "/insertOrUpdate", method = {RequestMethod.POST})
    @Record(module = PORTAL_MODULE_DEVICE_MODEL_OFFICIAL,operation = PORTAL_LOG_OPERATION_ADD)
    public ApiResult insertOrUpdate(@Valid @RequestBody DeviceMergeVO mergeVO,
                                    HttpServletRequest request) {
        TDeviceModelOfficial model = new TDeviceModelOfficial();
        BeanUtils.copyProperties(mergeVO, model);
        //校验型号不能重复
        int offcialCount = service.findOffcialCountByCode(model.getModelCode(), model.getId());
        if (offcialCount != 0) {
            return ApiResult.error("设备型号重复，请确认后提交");
        }
        if (!StringUtils.isEmpty(mergeVO.getBrandId())) {
            SysBrand brand = brandService.getById(mergeVO.getBrandId());
            model.setCompany(brand == null ? model.getCompany() : brand.getCompany());
        }
        if (model.getId() == null) {
            service.save(model);
        } else {
            service.update(model);
        }

        return ApiResult.success(MapBuilder.getSimpleMap("id", model.getId())).appendMessage("请求成功");
    }

    @ApiOperation(value = "/delete", notes = "删除设备型号")
    @ApiImplicitParam(name = "id", value = "id", required = true, dataType = "int", paramType = "query")
    @RequestMapping(value = "/delete", method = {RequestMethod.POST})
    @Record(module = PORTAL_MODULE_DEVICE_MODEL_OFFICIAL,operation = PORTAL_LOG_OPERATION_ONE_DELETE)
    public ApiResult delete(@NotNull(message = "id必填") Integer id, HttpServletRequest request) {
        TDeviceModelOfficial official = service.getById(id);
        if (official == null) {
            return ApiResult.error(CLIENT_PARAMS_ERROR).appendMessage("设备型号不存在#" + id);
        }
        service.delete(official);

        // 记录日志
        return ApiResult.success().appendMessage("删除成功");
    }

    @ApiOperation(value = "/export", notes = "导出")
    @RequestMapping(value = "/export", method = {RequestMethod.POST})
    @Record(module = PORTAL_MODULE_DEVICE_MODEL_OFFICIAL,operation = PORTAL_LOG_OPERATION_EXPORT)
    public void downloadFile( HttpServletRequest request, HttpServletResponse response) throws Exception {
        log.info("downloadFile#===========");
        String filePath = this.exportFolder +
                "device_model_" + DateHelper.getTodayShort() + "_" + StringHelper.getUuid() + ".xlsx";
        String name = "设备模型";
        String fileCnName = name + "_" + DateHelper.getTodayShort() + ".xlsx";

        List<ExcelUnknownDeviceVO> list = service.getUnknownDevicePage();

        Map<Integer, String[]> selectContent = new HashMap<>();
        //公司下拉
        List<String> brandNames = brandService.getAll().stream().map(SysBrand::getBrandName).collect(Collectors.toList());
        selectContent.put(1,brandNames.toArray(new String[brandNames.size()]));
        //设备类型下拉
        List<String> typeNames = deviceTypeService.getAll().stream().map(DeviceType::getTypeName).collect(Collectors.toList());
        selectContent.put(4,typeNames.toArray(new String[typeNames.size()]));
        selectContent.put(6,new String[]{"哑终端(前端设备)","活跃终端(服务器)"});
        ExportConfig exportConfig = ExportConfig.builder()
                .seqColumn(true)
                .wrap(true)
                .selectContent(selectContent)
                .build();
        fileService.exportExcelFile(list, name, filePath, exportConfig);
        this.download(filePath, fileCnName, request, response);
    }

    @ApiOperation(value = "/upload", notes = "上传文件，post参数id")
    @PostMapping("/upload")
    @ResponseBody
    @Record(module = PORTAL_MODULE_DEVICE_MODEL_OFFICIAL,operation = PORTAL_LOG_OPERATION_IMPORT)
    public ApiResult uploadFile(@RequestParam("file") MultipartFile file,  HttpServletRequest request) throws Exception {
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
            UserSession userSession = getUserSession(request);
            return  service.batchAddDevice(newFileName, originName,userSession);
        }
        return ApiResult.success();
    }

    @ApiOperation(value = "/download/errorFile", notes = "下载上传失败的文件")
    @RequestMapping(value = "/download/errorFile", method = {RequestMethod.GET})
    @Record(module = PORTAL_MODULE_DEVICE_MODEL_OFFICIAL,operation = LOG_OPERATION_DOWNLOAD_ERROR_FILE)
    public void errorFile(HttpServletRequest request,
                          HttpServletResponse response,@RequestParam(value = "errorFilePath") String errorFilePath) throws Exception {
        String date = DateHelper.formatDate(new Date(), "yyyyMMddHHmmss");
        String fileCnName = "设备模型上传失败" + date+".xlsx";
        this.download(errorFilePath, fileCnName, request, response);
    }

}
