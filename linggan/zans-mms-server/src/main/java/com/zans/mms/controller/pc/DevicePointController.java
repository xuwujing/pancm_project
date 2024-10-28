package com.zans.mms.controller.pc;

import com.alibaba.fastjson.JSONObject;
import com.zans.base.annotion.Record;
import com.zans.base.controller.BaseController;
import com.zans.base.office.excel.ExcelHelper;
import com.zans.base.util.DateHelper;
import com.zans.base.util.FileHelper;
import com.zans.base.util.HttpHelper;
import com.zans.base.vo.ApiResult;
import com.zans.base.vo.UserSession;
import com.zans.mms.config.MMSConstants;
import com.zans.mms.model.DevicePoint;
import com.zans.mms.model.Speed;
import com.zans.mms.service.IDevicePointCheckLogService;
import com.zans.mms.service.IDevicePointService;
import com.zans.mms.service.IFileService;
import com.zans.mms.service.ISpeedService;
import com.zans.mms.vo.devicepoint.*;
import com.zans.mms.vo.devicepoint.check.DevicePointCheckReqVO;
import com.zans.mms.vo.devicepoint.map.DevicePointMapQueryVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import static com.zans.base.config.EnumErrorCode.SERVER_UPLOAD_MAX_SIZE_ERROR;
import static com.zans.base.config.EnumErrorCode.SERVER_UPLOAD_MIME_ERROR;
import static com.zans.base.config.GlobalConstants.UPLOAD_FILE_MAX_SIZE;
import static com.zans.mms.config.MMSConstants.*;

/**
 * @author
 * @version V1.0
 **/
@RestController
@RequestMapping("devicePoint")
@Api(tags = "点位管理")
@Validated
@Slf4j
public class DevicePointController extends BaseController {

    @Autowired
    IDevicePointService devicePointService;

    @Autowired
    private HttpHelper httpHelper;

    @Value("${api.upload.folder}")
    String uploadFolder;

    @Autowired
    IFileService fileService;

    @Autowired
    IDevicePointCheckLogService devicePointCheckLogService;

    @Autowired
    private ISpeedService speedService;


    /**
     * 获取所有数据
     *
     * @param vo DevicePointReqVo
     * @return ApiResult<PageInfo < DevicePointResVo>>
     */
    @ApiOperation(value = "获取数据列表", notes = "获取数据列表")
    @PostMapping(value = "/list", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiResult getList(@Valid @RequestBody DevicePointQueryVO vo) {
        super.checkPageParams(vo);
        return devicePointService.getList(vo);
    }

    /**
     * 获取信息
     *
     * @param pointId
     * @return ApiResult<DevicePointResVo>
     */
    @ApiOperation(value = "查询指定数据", notes = "查询指定数据")
    @GetMapping(value = "/view", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiResult<DevicePointDetailResVO> view(Long pointId) {
        DevicePointDetailResVO viewResp = devicePointService.getViewById(pointId);
        if (null == viewResp) {
            return ApiResult.error("不存在此记录");
        }
        return ApiResult.success(viewResp);
    }


    /**
     * 新增一条数据
     *
     * @param reqVO 实体类
     * @return Response对象
     */
    @ApiOperation(value = "正式新增点位用，临时点位不要用", notes = "正式新增点位用，临时点位不要用")
    @RequestMapping(value = "save", method = RequestMethod.POST)
    @Record(module = MMSConstants.MODULE_POINT,operation = MMSConstants.LOG_OPERATION_SAVE)
    public ApiResult<DevicePoint> insert(@RequestBody DevicePointAddReqVO reqVO, HttpServletRequest httpRequest) {
        UserSession userSession = httpHelper.getUser(httpRequest);
        DevicePoint devicePoint = new DevicePoint();
        BeanUtils.copyProperties(reqVO,devicePoint);
        devicePoint.setCreator(userSession.getUserName());
        if (StringUtils.isEmpty(reqVO.getMapSource())){
            devicePoint.setMapSource("1");

        }
        devicePoint.setPointType(1);
        devicePoint.setLongitude(new BigDecimal(reqVO.getLongitude()));
        devicePoint.setLatitude(new BigDecimal(reqVO.getLatitude()));
        int result = devicePointService.saveSelective(devicePoint);
//        int result = devicePointService.insertOne(devicePoint);
        if (result > 0) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("id",devicePoint.getId());
            return ApiResult.success(jsonObject);
        }
        return ApiResult.error("新增失败");
    }

    /**
     * 修改一条数据
     *
     * @param reqVO 实体类
     * @return Response对象
     */
    @ApiOperation(value = "修改", notes = "修改")
    @RequestMapping(value = "edit", method = RequestMethod.POST)
    @Record(module = MMSConstants.MODULE_POINT,operation = MMSConstants.LOG_OPERATION_EDIT)
    public ApiResult<DevicePoint> update(@RequestBody DevicePointEditReqVO reqVO, HttpServletRequest httpRequest) {
        UserSession userSession = httpHelper.getUser(httpRequest);
        DevicePoint devicePoint = new DevicePoint();
        BeanUtils.copyProperties(reqVO,devicePoint);
        devicePoint.setUpdateTime(new Date());
        devicePoint.setId(reqVO.getPointId());
        devicePoint.setLongitude(new BigDecimal(reqVO.getLongitude()));
        devicePoint.setLatitude(new BigDecimal(reqVO.getLatitude()));
        devicePoint.setCreator(userSession.getUserName());
        devicePointService.updateDevicePoint(devicePoint);
        return ApiResult.success();
    }

    /**
     * 删除一条数据
     *
     * @param id 参数对象
     * @return Response对象
     */
    @ApiOperation(value = "删除", notes = "删除")
    @RequestMapping(value = "del", method = RequestMethod.GET)
    @Record(module = MMSConstants.MODULE_POINT,operation = MMSConstants.LOG_OPERATION_DELETE)
    public ApiResult delete(Long id, HttpServletRequest httpRequest) {
       /* Boolean b =  devicePointService.existRelation(id);
        if (b){
            return ApiResult.error("存在业务关系，不能删除");
        }*/
        devicePointService.deleteByUniqueId(id);
        return ApiResult.success();
    }

    @ApiOperation(value = "/download/template", notes = "文件下载")
    @GetMapping("/download/template")
    @Record(module = MMSConstants.MODULE_POINT,operation = MMSConstants.LOG_OPERATION_DOWNLOAD)
    public void downloadFile(HttpServletRequest request,
                             HttpServletResponse response) throws Exception {
        String filePath = this.uploadFolder+"template/资产导入-点位-excel模板.xlsx";

        String date = DateHelper.formatDate(new Date(), "yyyyMMddHHmmss");
        String fileCnName = "点位导入模板" + date+".xlsx";
        this.download(filePath, fileCnName, request, response);
    }

    /**
     * 点位数据上传及导入
     * @param file 点位导入文件
     * @param request
     * @return
     * @throws Exception
     */
    @ApiOperation(value = "/upload", notes = "上传文件，导入点位数据")
    @PostMapping("/upload")
    @ResponseBody
    @Record(module = MMSConstants.MODULE_POINT,operation = MMSConstants.LOG_OPERATION_IMPORT)
    public ApiResult uploadFile(@RequestParam("file") MultipartFile file,
                                @RequestParam("req") String json,
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
        if (saved){
            JSONObject jsonObject = JSONObject.parseObject(json);
            Integer backup =  Integer.parseInt(jsonObject.get("backup").toString());
            Integer operation = Integer.parseInt(jsonObject.get("operation").toString());
            //如果确定备份数据
            if(backup==1){
                devicePointService.backupTable();
            }
            UserSession userSession =  httpHelper.getUser(request);
            List<ExcelDevicePointVO> excelDevicePointVOList = devicePointService.batchAddDevicePoint(newFileName, originName, userSession);
            String absoluteNewFilePath = this.uploadFolder + newFileName;
            //然后进行异步的数据处理
            //先查询出当前的进度id 然后插入一条进度数据
            Speed speed = new Speed();
            speed.setTotal(excelDevicePointVOList.size());
            speed.setFilePath(absoluteNewFilePath);
            speed.setCreator(userSession.getUserName());
            speed.setModule("点位");
            speed.setSuccessCount(0);
            speed.setUpdateCount(0);
            speed.setAddCount(0);
            speed.setFailCount(0);
            speedService.insertOne(speed);
            devicePointService.dealUploadData(operation,speed,excelDevicePointVOList,absoluteNewFilePath,userSession);
            return ApiResult.success(speed);
        }
        return ApiResult.error("file save error");
    }


    @ApiOperation(value = "/download/errorFile", notes = "下载上传失败的文件")
    @RequestMapping(value = "/download/errorFile", method = {RequestMethod.GET})
    @Record(module = MMSConstants.MODULE_POINT,operation = MMSConstants.LOG_OPERATION_DOWNLOAD)
    public void errorFile(HttpServletRequest request,
                          HttpServletResponse response, @RequestParam(value = "errorFilePath") String errorFilePath) throws Exception {
        String date = DateHelper.formatDate(new Date(), "yyyyMMddHHmmss");
        String fileCnName = "点位信息上传失败" + date+".xlsx";
        this.download(errorFilePath, fileCnName, request, response);
    }

    /**
     * 校正点位
     *
     * @param reqVO 实体类
     * @return Response对象
     */
    @ApiOperation(value = "校正点位")
    @RequestMapping(value = "checkPoint", method = RequestMethod.POST)
    @Record(module = MODULE_POINT,operation = LOG_OPERATION_SAVE,opPlatform = LOG_APP)
    public ApiResult<DevicePoint> checkPoint(@RequestBody DevicePointCheckReqVO reqVO, HttpServletRequest httpRequest) {
        UserSession userSession = httpHelper.getUser(httpRequest);
        reqVO.setCreator(userSession.getUserName());
        boolean b = devicePointCheckLogService.checkPoint(reqVO);
        if (b) {
            return ApiResult.success().message("校正点位成功");
        }
        return ApiResult.error("校正点位失败");
    }


    /**
     * 获取所有数据
     *
     * @param vo DevicePointReqVo
     * @return ApiResult<PageInfo < DevicePointResVo>>
     */
    @ApiOperation(value = "点位地图", notes = "点位地图")
    @PostMapping(value = "/map/list", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiResult getMapList(@Valid @RequestBody DevicePointMapQueryVO vo) {
        return devicePointService.getMapList(vo);
    }


    /**
     * 导出所有点位数据
     * @param request
     * @param response
     * @throws Exception
     */
    @ApiOperation(value = "/export", notes = "点位数据导出")
    @RequestMapping(value = "/export", method = {RequestMethod.POST})
    @Record(module = MMSConstants.MODULE_POINT,operation = MMSConstants.LOG_OPERATION_DOWNLOAD)
    public void export(@Valid @RequestBody DevicePointExportQueryVO vo,HttpServletRequest request,
                          HttpServletResponse response) throws Exception {
        String date = DateHelper.formatDate(new Date(), "yyyyMMddHHmmss");
        String fileName = "点位数据导出" + date+".xls";
        String filePath=devicePointService.export(vo,fileName);
        this.download(filePath, fileName, request, response);
    }


    /**
     * 获取所有导入数据
     *
     * @param vo DevicePointReqVo
     * @return ApiResult<PageInfo < DevicePointResVo>>
     */
    @ApiOperation(value = "获取导入数据列表", notes = "获取导入数据列表")
    @PostMapping(value = "/importList", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiResult importList(@Valid @RequestBody DevicePointQueryVO vo) {
        super.checkPageParams(vo);
        return devicePointService.getImportList(vo);
    }


    /**
     * 导入数据修改
     *
     * @param reqVO 实体类
     * @return Response对象
     */
    @ApiOperation(value = "导入数据修改", notes = "导入数据修改")
    @RequestMapping(value = "updateImport", method = RequestMethod.POST)
    @Record(module = MMSConstants.MODULE_POINT,operation = MMSConstants.LOG_OPERATION_EDIT)
    public ApiResult<DevicePoint> updateImport(@RequestBody DevicePointEditReqVO reqVO, HttpServletRequest httpRequest) {
        UserSession userSession = httpHelper.getUser(httpRequest);
        DevicePoint devicePoint = new DevicePoint();
        BeanUtils.copyProperties(reqVO,devicePoint);
        devicePoint.setUpdateTime(new Date());
        devicePoint.setId(reqVO.getPointId());
        devicePoint.setLongitude(new BigDecimal(reqVO.getLongitude()));
        devicePoint.setLatitude(new BigDecimal(reqVO.getLatitude()));
        devicePoint.setCreator(userSession.getUserName());
        devicePointService.updateImportDevicePoint(devicePoint);
        return ApiResult.success();
    }


    /**
     * 导入数据确认
     *
     * @param queryVO 实体类
     * @return Response对象
     */
    @ApiOperation(value = "导入数据确认", notes = "导入数据确认")
    @PostMapping(value = "importConfig")
    @Record(module = MMSConstants.MODULE_POINT,operation = MMSConstants.LOG_OPERATION_EDIT)
    public ApiResult importConfig(@RequestBody DevicePointQueryVO queryVO, HttpServletRequest httpRequest) {
        UserSession userSession = httpHelper.getUser(httpRequest);
        devicePointService.importConfig(queryVO,userSession);
        return ApiResult.success();
    }




}
