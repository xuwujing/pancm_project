package com.zans.portal.controller;

import com.zans.base.annotion.Record;
import com.zans.base.office.excel.ExcelHelper;
import com.zans.base.util.DateHelper;
import com.zans.base.util.FileHelper;
import com.zans.base.util.MapBuilder;
import com.zans.base.util.StringHelper;
import com.zans.base.vo.ApiResult;
import com.zans.base.vo.PageResult;
import com.zans.base.vo.SelectVO;
import com.zans.base.vo.UserSession;
import com.zans.portal.model.IpAlloc;
import com.zans.portal.service.*;
import com.zans.portal.vo.ip.IpAllocAddVO;
import com.zans.portal.vo.ip.IpAllocRespVO;
import com.zans.portal.vo.ip.IpAllocSearchVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.javatuples.Triplet;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import static com.zans.base.config.EnumErrorCode.*;
import static com.zans.portal.config.GlobalConstants.*;

/**
 * @author xv
 * @since 2020/3/24 10:51
 */
@Api(value = "/ip/alloc", tags = {"/ip/alloc ~ IP分配管理"})
@RestController
@RequestMapping("/ip/alloc")
@Validated
@Slf4j
public class IpAllocController extends BasePortalController {
    @Autowired
    IIpAllocService allocService;

    @Autowired
    IConstantItemService constantItemService;

    @Autowired
    IDeviceTypeService deviceTypeService;

    @Autowired
    IAreaService areaService;

    @Autowired
    ILogOperationService logOperationService;

    @Autowired
    IFileService fileService;

    @Value("${api.upload.folder}")
    String uploadFolder;

    @Value("${api.ip-alloc.default-order:a.id desc}")
    String defaultOrder;


    @ApiOperation(value = "/list", notes = "地址分配查询")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "req", value = "查询条件", required = true,
                    dataType = "IpAllocSearchVO", paramType = "body")
    })
    @RequestMapping(value = "/list", method = {RequestMethod.POST})
    public ApiResult<PageResult<IpAllocRespVO>> findAllIp(@RequestBody IpAllocSearchVO req) {
        super.checkPageParams(req, defaultOrder);
        PageResult<IpAllocRespVO> pageResult = allocService.getIpAllocPage(req);
        return ApiResult.success(pageResult);
    }

    @ApiOperation(value = "/init", notes = "地址分配查询初始化")
    @RequestMapping(value = "/init", method = {RequestMethod.GET})
    @ResponseBody
    public ApiResult init() {
        List<SelectVO> deviceTypeList = deviceTypeService.findDeviceTypeToSelect();
        List<SelectVO> deviceTypeTemplateList = deviceTypeService.findDeviceTypeHasTemplateToSelect();
        List<SelectVO> insertList = constantItemService.findItemsByDict(MODULE_IP_ALLOC_INSERT_STATUS);
        List<SelectVO> validList = constantItemService.findItemsByDict(MODULE_IP_ALLOC_VALID_STATUS);
        List<SelectVO> areaList = areaService.findAreaToSelect();

        IpAllocSearchVO req = new IpAllocSearchVO();
        super.checkPageParams(req, defaultOrder);
        PageResult<IpAllocRespVO> pageResult = allocService.getIpAllocPage(req);
        Map<String, Object> result = MapBuilder.getBuilder()
                .put(MODULE_IP_ALLOC_INSERT_STATUS, insertList)
                .put(MODULE_IP_ALLOC_VALID_STATUS, validList)
                .put(MODULE_DEVICE, deviceTypeList)
                .put(MODULE_DEVICE_TEMPLATE, deviceTypeTemplateList)
                .put(MODULE_AREA, areaList)
                .put("support_device", deviceTypeService.getSupportDeviceType())
                .put(INIT_DATA_TABLE, pageResult)
                .build();
        return ApiResult.success(result);
    }

    @ApiOperation(value = "/view", notes = "获取地址分配详细信息，by id")
    @ApiImplicitParam(name = "id", value = "地址id", required = true,
            dataType = "int", paramType = "query")
    @RequestMapping(value = "/view", method = {RequestMethod.GET})
    public ApiResult<IpAllocRespVO> getIpById(@NotNull(message = "id必填") Integer id) {
        IpAllocRespVO ip = allocService.getIpAlloc(id);
        if (ip == null) {
            return ApiResult.error(CLIENT_PARAMS_ERROR).appendMessage("IP分配ID不存在#" + id);
        }
        List<Object> table = allocService.readIpAllocFile(ip.getFilePath(), ip.getFileName());

        Map<String, Object> map = MapBuilder.getBuilder()
                .put("alloc", ip).put("table", table).build();

        return ApiResult.success(map);
    }

    @ApiOperation(value = "/check", notes = "校验文件，by id")
    @ApiImplicitParam(name = "id", value = "地址id", required = true,
            dataType = "int", paramType = "query")
    @RequestMapping(value = "/check", method = {RequestMethod.POST})
    @Record
    public ApiResult<IpAllocRespVO> checkExcelById(@NotNull(message = "id必填") Integer id) {
        IpAlloc ip = allocService.getById(id);
        if (ip == null) {
            return ApiResult.error(CLIENT_PARAMS_ERROR).appendMessage("IP分配ID不存在#" + id);
        }

        String oldFilePath = ip.getFilePath();
        String newFileName = fileService.getNewFileName(ip.getFileName());
        Triplet<Boolean, List<Object>, String> tuple = allocService.checkIpAllocFile(oldFilePath, ip.getFileName(), newFileName);

        // 不通过，更新文件
        boolean valid = tuple.getValue0();
        if (!valid) {
            ip.setFilePath(newFileName);
            ip.setValidStatus(STATUS_DISABLE);
            allocService.update(ip);
            // 保证事务安全
            FileHelper.deleteFile(this.uploadFolder, oldFilePath);
        } else {
            ip.setValidStatus(STATUS_ENABLE);
            allocService.update(ip);
        }

        IpAllocRespVO ipAllocRespVO = allocService.getIpAlloc(id);
        Map<String, Object> map = MapBuilder.getBuilder()
                .put("alloc", ipAllocRespVO)
                .put("valid", tuple.getValue0())
                .put("table", tuple.getValue1())
                .put("error", tuple.getValue2()).build();
        return ApiResult.success(map);
    }


    @PostMapping("/assign")
    @ResponseBody
    @Record
    public ApiResult doAssignIp(@NotNull(message = "id必填") Integer id) throws Exception {
        IpAlloc ip = allocService.getById(id);
        if (ip == null) {
            return ApiResult.error(CLIENT_PARAMS_ERROR).appendMessage("IP分配ID不存在#" + id);
        }
        String oldFilePath = ip.getFilePath();
        String newFileName = fileService.getNewFileName(ip.getFileName());
        Triplet<Boolean, List<Object>, String> tuple = allocService.assignIpAlloc(ip, oldFilePath, ip.getFileName(), newFileName);
        // 通过，更新文件，写入数据库
        boolean valid = tuple.getValue0();
        if (valid) {
            ip.setFilePath(newFileName);
            ip.setValidStatus(STATUS_ENABLE);
            allocService.update(ip);
            FileHelper.deleteFile(this.uploadFolder, oldFilePath);
        } else {
            ip.setValidStatus(STATUS_DISABLE);
        }

        IpAllocRespVO ipAllocRespVO = allocService.getIpAlloc(id);
        Map<String, Object> map = MapBuilder.getBuilder()
                .put("alloc", ipAllocRespVO)
                .put("valid", valid)
                .put("table", tuple.getValue1())
                .put("error", tuple.getValue2())
                .build();
        return ApiResult.success(map);

    }


    @ApiOperation(value = "/handAssign", notes = "手动分配")
    @ApiImplicitParam(name = "id", value = "手动分配,下载文件", required = true)
    @GetMapping("/handAssign")
    @Record
    public void doHandAssignIp(@NotNull(message = "id必填") Integer id,
                               HttpServletRequest request,
                               HttpServletResponse response) throws Exception {

        log.info("downloadFile#{}", id);
        IpAlloc ipAlloc = this.allocService.getById(id);
        if (ipAlloc == null) {
            String errorMessage = "下载失败，分配ID不存在#" + id;
            log.error(errorMessage);
            this.setErrorToResponse(request, response, SERVER_DOWNLOAD_ERROR, errorMessage);
            return;
        }
        String oldFilePath = ipAlloc.getFilePath();
//        String newFileName = fileService.getNewFileName(ipAlloc.getFileName());
        String newFileName = ipAlloc.getFileName();
        String filePath = this.uploadFolder + "/" + oldFilePath;
        this.download(filePath, newFileName, request, response);

    }


    @ApiOperation(value = "/confirmAssign", notes = "确认分配")
    @ApiImplicitParam(name = "id", value = "确认分配", required = true)
    @PostMapping("/confirmAssign")
    @Record
    public ApiResult confirmAssign(@NotNull(message = "id必填") Integer id,
                                   HttpServletRequest request) throws Exception {

        IpAlloc ip = allocService.getById(id);
        if (ip == null) {
            return ApiResult.error(CLIENT_PARAMS_ERROR).appendMessage("IP分配ID不存在#" + id);
        }

        boolean allocBefore = allocService.isIpAlloc(id);
        if (allocBefore) {
            ip.setInsertStatus(STATUS_ENABLE);
            allocService.update(ip);
            return ApiResult.error(CLIENT_PARAMS_ERROR).appendMessage("IP已分配过#" + id);
        }

        String oldFilePath = ip.getFilePath();
        String newFileName = ip.getFilePath();
        Triplet<Boolean, List<Object>, String> tuple = allocService.checkIpAllocFileIp(oldFilePath, ip.getFileName(), newFileName);
        String errorMsg = tuple.getValue2();
        // 通过，更新文件
        boolean valid = tuple.getValue0();
        if (valid) {
            ip.setFilePath(newFileName);
            ip.setValidStatus(STATUS_ENABLE);
            valid = allocService.importAllocIp(ip, tuple.getValue1(), getUserSession(request));
        } else {
            ip.setValidStatus(STATUS_DISABLE);
            ip.setInsertStatus(STATUS_DISABLE);
            allocService.update(ip);
            errorMsg = "文件校验失败！请检查文件重新上传!";
        }


        IpAllocRespVO ipAllocRespVO = allocService.getIpAlloc(id);
        String operationResult = valid ? LOG_RESULT_SUCCESS : LOG_RESULT_FAIL;
        Map<String, Object> contentMap = MapBuilder.getBuilder()
                .put("t_ip_alloc.id", id)
                .put("file_path", ip.getFilePath())
                .put("valid", tuple.getValue0())
                .put("error", errorMsg).build();
//        LogOperation logOperation = new LogBuilder().session(getUserSession(request))
//                .module(LOG_MODULE_IP_ALLOC)
//                .operation(LOG_OPERATION_IMPORT)
//                .content(JSON.toJSONString(contentMap))
//                .result(operationResult).build();
//        logOperationService.save(logOperation);
        contentMap.put("table", tuple.getValue1());
        contentMap.put("alloc", ipAllocRespVO);
        return ApiResult.success(contentMap);
    }

    @ApiOperation(value = "/cancelAssign", notes = "取消分配")
    @ApiImplicitParam(name = "id", value = "取消分配", required = true)
    @PostMapping("/cancelAssign")
    @Record
    public ApiResult cancelAssign(@NotNull(message = "id必填") Integer id, HttpServletRequest request) throws Exception {
        IpAlloc ip = allocService.getById(id);
        if (ip == null) {
            return ApiResult.error(CLIENT_PARAMS_ERROR).appendMessage("IP分配ID不存在#" + id);
        }
        UserSession userSession = getUserSession(request);
        log.warn("用户:{},取消ip分配!",userSession.getUserName());
        ip.setValidStatus(STATUS_DISABLE);
        ip.setInsertStatus(STATUS_DISABLE);
        allocService.update(ip);
        this.allocService.deleteIpAlloc2(ip, userSession);
        Map<String, Object> contentMap = MapBuilder.getBuilder()
                .put("t_ip_alloc.id", id)
                .put("file_path", ip.getFilePath())
                .put("valid", true)
                .put("error", null).build();
        contentMap.put("table", new ArrayList<>());
        contentMap.put("alloc", new IpAllocRespVO());
        return ApiResult.success(contentMap);
    }


    @ApiOperation(value = "/ipCheck", notes = "校验ip，by id")
    @ApiImplicitParam(name = "id", value = "地址id", required = true,
            dataType = "int", paramType = "query")
    @RequestMapping(value = "/ipCheck", method = {RequestMethod.POST})
    @Record
    public ApiResult<IpAllocRespVO> ipCheckExcelById(@NotNull(message = "id必填") Integer id) {
        IpAlloc ip = allocService.getById(id);
        if (ip == null) {
            return ApiResult.error(CLIENT_PARAMS_ERROR).appendMessage("IP分配ID不存在#" + id);
        }
        String oldFilePath = ip.getFilePath();
        String newFileName = ip.getFilePath();
        Triplet<Boolean, List<Object>, String> tuple = allocService.checkIpAllocFileIp(oldFilePath, ip.getFileName(), newFileName);
        ip.setValidStatus(STATUS_ENABLE);
        allocService.update(ip);
        String errorMsg = tuple.getValue2();
        if(!tuple.getValue0()){
            errorMsg = "文件校验失败！请检查文件重新上传!";
        }

        IpAllocRespVO ipAllocRespVO = allocService.getIpAlloc(id);
        Map<String, Object> map = MapBuilder.getBuilder()
                .put("alloc", ipAllocRespVO)
                .put("valid", tuple.getValue0())
                .put("table", tuple.getValue1())
                .put("error", errorMsg).build();
        return ApiResult.success(map);
    }


    @ApiOperation(value = "/download/errorFile", notes = "下载上传失败的文件")
    @RequestMapping(value = "/download/errorFile", method = {RequestMethod.GET})
    @Record
    public void errorFile(HttpServletRequest request,
                          HttpServletResponse response,@RequestParam(value = "errorFilePath") String errorFilePath) throws Exception {
        String date = DateHelper.formatDate(new Date(), "yyyyMMddHHmmss");
        String fileCnName = "IP校验失败" + date+".xlsx";
        errorFilePath = this.uploadFolder + "/" + errorFilePath;
        this.download(errorFilePath, fileCnName, request, response);
    }


    @ApiOperation(value = "/handSave", notes = "IP手工入库，by id")
    @PostMapping("/handSave")
    @ResponseBody
    @Record
    public ApiResult handSave(@NotNull(message = "id必填") Integer id, HttpServletRequest request) throws Exception {
        IpAlloc ip = allocService.getById(id);
        if (ip == null) {
            return ApiResult.error(CLIENT_PARAMS_ERROR).appendMessage("IP分配ID不存在#" + id);
        }

        boolean allocBefore = allocService.isIpAlloc(id);
        if (allocBefore) {
            ip.setInsertStatus(STATUS_ENABLE);
            allocService.update(ip);
            return ApiResult.error(CLIENT_PARAMS_ERROR).appendMessage("IP已分配过#" + id);
        }

        String oldFilePath = ip.getFilePath();
        String newFileName = ip.getFilePath();
        Triplet<Boolean, List<Object>, String> tuple = allocService.checkIpAllocFileIp(oldFilePath, ip.getFileName(), newFileName);

        // 通过，更新文件
        boolean valid = tuple.getValue0();
        if (valid) {
            ip.setFilePath(newFileName);
            ip.setValidStatus(STATUS_ENABLE);
            valid = allocService.importAllocIp(ip, tuple.getValue1(), getUserSession(request));
        } else {
            ip.setValidStatus(STATUS_DISABLE);
            ip.setInsertStatus(STATUS_DISABLE);
            allocService.update(ip);
        }


        IpAllocRespVO ipAllocRespVO = allocService.getIpAlloc(id);
        String operationResult = valid ? LOG_RESULT_SUCCESS : LOG_RESULT_FAIL;
        Map<String, Object> contentMap = MapBuilder.getBuilder()
                .put("t_ip_alloc.id", id)
                .put("file_path", ip.getFilePath())
                .put("valid", tuple.getValue0())
                .put("error", tuple.getValue2()).build();
//        LogOperation logOperation = new LogBuilder().session(getUserSession(request))
//                .module(LOG_MODULE_IP_ALLOC)
//                .operation(LOG_OPERATION_IMPORT)
//                .content(JSON.toJSONString(contentMap))
//                .result(operationResult).build();
//        logOperationService.save(logOperation);

        contentMap.put("table", tuple.getValue1());
        contentMap.put("alloc", ipAllocRespVO);
        return ApiResult.success(contentMap);
    }



    @PostMapping("/import")
    @ResponseBody
    @Record
    public ApiResult doImport(@NotNull(message = "id必填") Integer id, HttpServletRequest request) throws Exception {
        IpAlloc ip = allocService.getById(id);
        if (ip == null) {
            return ApiResult.error(CLIENT_PARAMS_ERROR).appendMessage("IP分配ID不存在#" + id);
        }

        boolean allocBefore = allocService.isIpAlloc(id);
        if (allocBefore) {
            ip.setInsertStatus(STATUS_ENABLE);
            allocService.update(ip);
            return ApiResult.error(CLIENT_PARAMS_ERROR).appendMessage("IP已分配过#" + id);
        }

        String oldFilePath = ip.getFilePath();
        String newFileName = fileService.getNewFileName(ip.getFileName());
        Triplet<Boolean, List<Object>, String> tuple = allocService.assignIpAlloc(ip, oldFilePath, ip.getFileName(), newFileName);

        // 通过，更新文件
        boolean valid = tuple.getValue0();
        if (valid) {
            ip.setFilePath(newFileName);
            ip.setValidStatus(STATUS_ENABLE);
            valid = allocService.importAllocIp(ip, tuple.getValue1(), getUserSession(request));
            FileHelper.deleteFile(this.uploadFolder, oldFilePath);
        } else {
            ip.setValidStatus(STATUS_DISABLE);
            ip.setInsertStatus(STATUS_DISABLE);
            allocService.update(ip);
        }


        IpAllocRespVO ipAllocRespVO = allocService.getIpAlloc(id);
        String operationResult = valid ? LOG_RESULT_SUCCESS : LOG_RESULT_FAIL;
        Map<String, Object> contentMap = MapBuilder.getBuilder()
                .put("t_ip_alloc.id", id)
                .put("file_path", ip.getFilePath())
                .put("valid", tuple.getValue0())
                .put("error", tuple.getValue2()).build();
//        LogOperation logOperation = new LogBuilder().session(getUserSession(request))
//                .module(LOG_MODULE_IP_ALLOC)
//                .operation(LOG_OPERATION_IMPORT)
//                .content(JSON.toJSONString(contentMap))
//                .result(operationResult).build();
//        logOperationService.save(logOperation);

        contentMap.put("table", tuple.getValue1());
        contentMap.put("alloc", ipAllocRespVO);
        return ApiResult.success(contentMap);

    }

    @PostMapping("/upload")
    @ResponseBody
    @Record
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

        IpAllocRespVO ipAllocRespVO = null;

        // 更新 ip_alloc.file_path，读取文件内容
        Integer id = super.getIntegerParameter(request, "alloc_id");
        List<Object> table = new ArrayList<>();
        if (saved) {
            table = allocService.readIpAllocFile(newFileName, originName);
            if (id != null) {
                IpAlloc ipAlloc = allocService.getById(id);
                if (ipAlloc != null) {
                    String oldFilePath = ipAlloc.getFilePath();
                    ipAlloc.setFilePath(newFileName);
                    // 重新上传文件，需要重新校验
                    ipAlloc.setValidStatus(STATUS_DISABLE);
                    ipAlloc.setFileName(allocService.generateFileName(ipAlloc));
                    allocService.update(ipAlloc);
                    FileHelper.deleteFile(this.uploadFolder, oldFilePath);

                    ipAllocRespVO = allocService.getIpAlloc(id);
                }
            }
        }

        // 写入本地文件，提供下载
        Map<String, Object> map = MapBuilder.getBuilder()
                .put("alloc", ipAllocRespVO)
                .put("file_path", newFileName)
                .put("file_name", file.getOriginalFilename())
                .put("table", table).build();
        return ApiResult.success(map);
    }

    @ApiOperation(value = "/add", notes = "新增IP分配")
    @ApiImplicitParam(name = "addVO", value = "IP分配", required = true,
            dataType = "IpAllocAddVO", paramType = "body")
    @RequestMapping(value = "/add", method = {RequestMethod.POST})
    @Record
    public ApiResult addIpAlloc(@Valid @RequestBody IpAllocAddVO addVO,
                                HttpServletRequest request) {

        IpAlloc allocEntity = new IpAlloc();
        BeanUtils.copyProperties(addVO, allocEntity);
        UserSession session = super.getUserSession(request);
        allocEntity.setCreatePerson(session.getUserId());
        allocEntity.setAllocDay(new Date());
        allocEntity.setInsertStatus(STATUS_DISABLE);
        allocEntity.setValidStatus(STATUS_DISABLE);

        allocService.save(allocEntity);

//        LogOperation logOperation = new LogBuilder().session(getUserSession(request))
//                .module(LOG_MODULE_IP_ALLOC)
//                .operation(LOG_OPERATION_ADD)
//                .content(JSON.toJSONString(addVO))
//                .result(LOG_RESULT_SUCCESS).build();
//        logOperationService.save(logOperation);
        return ApiResult.success(MapBuilder.getSimpleMap("id", allocEntity.getId()));
    }

    @ApiOperation(value = "/delete", notes = "IP配置删除")
    @RequestMapping(value = "/delete", method = {RequestMethod.POST})
    @Record
    public ApiResult delete(@NotNull(message = "id必填") Integer id,
                            HttpServletRequest request) {
        IpAlloc ipAlloc = allocService.getById(id);
        if (ipAlloc == null) {
            return ApiResult.error(CLIENT_PARAMS_ERROR).appendMessage("IP分配不存在#" + id);
        }

        UserSession userSession = getUserSession(request);
        this.allocService.deleteIpAlloc(ipAlloc, userSession);

        return ApiResult.success();
    }

    @GetMapping("/import/read")
    @ResponseBody
    public ApiResult readFile(HttpServletRequest request) throws Exception {
        String filename = "监控20170907_武昌_示范工程_光谷智能.xlsx";
        filename = "电警20190908_黄陂_黄海大道（塔帽线-海工大）工程_景网孙愉.xlsx";
//        String filePath = this.uploadFolder + filename;
//        ClassPathResource resource = new ClassPathResource(filePath);
//        InputStream is = resource.getInputStream();
        String filePath = this.uploadFolder + "/" + filename;
        InputStream is = new FileInputStream(new File(filePath));
//
        // header 缺陷，多行表头定位
        // 需要精确修改 excel 的 Cell，因此不能用高级API
//        IpAllocFile file = fileService.readIpAllocFile(filename, is);
        String outFile = this.uploadFolder + "/" + "1.xlsx";
        fileService.assignIp(null, filename, is, outFile);
        return ApiResult.success();
    }

    @GetMapping("/download/file")
    @Record
    public void downloadAllocFile(@NotNull(message = "id必填") Integer id,
                                  HttpServletRequest request,
                                  HttpServletResponse response) throws Exception {
        log.info("downloadFile#{}", id);
        IpAlloc ipAlloc = this.allocService.getById(id);
        if (ipAlloc == null) {
            String errorMessage = "下载失败，分配ID不存在#" + id;
            log.error(errorMessage);
            this.setErrorToResponse(request, response, SERVER_DOWNLOAD_ERROR, errorMessage);
            return;
        }

        String filePath = this.uploadFolder + "/" + ipAlloc.getFilePath();
        String fileCnName = ipAlloc.getFileName();
        this.download(filePath, fileCnName, request, response);
    }

    @GetMapping("/download/template")
    @Record
    public void downloadTemplate(@NotNull(message = "id必填") Integer id,
                                 HttpServletRequest request,
                                 HttpServletResponse response) throws Exception {
        log.info("downloadTemplate#{}", id);
        IpAllocRespVO ipAlloc = this.allocService.getIpAlloc(id);

        if (ipAlloc == null || StringHelper.isBlank(ipAlloc.getTemplate())) {
            String errorMessage = "下载失败，模板不存在#" + id;
            log.error(errorMessage);
            this.setErrorToResponse(request, response, SERVER_DOWNLOAD_ERROR, errorMessage);
            return;
        }
        log.info("downloadTemplate#{}", ipAlloc.getTemplate());

        String filePath = allocService.generateFileByTemplate(ipAlloc);
        if (filePath == null) {
            log.error("generateFileByTemplate error#" + ipAlloc.getTemplate());
            filePath = this.uploadFolder + "/" + ipAlloc.getTemplate();
        }

        String fileCnName = allocService.generateFileName(ipAlloc);
        super.download(filePath, fileCnName, request, response);
    }

}
