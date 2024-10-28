package com.zans.mms.controller.pc;

import com.alibaba.fastjson.JSONObject;
import com.zans.base.annotion.Record;
import com.zans.base.controller.BaseController;
import com.zans.base.office.excel.ExcelHelper;
import com.zans.base.util.DateHelper;
import com.zans.base.util.FileHelper;
import com.zans.base.util.HttpHelper;
import com.zans.base.util.StringHelper;
import com.zans.base.vo.ApiResult;
import com.zans.base.vo.UserSession;
import com.zans.mms.config.MMSConstants;
import com.zans.mms.model.Asset;
import com.zans.mms.model.DevicePoint;
import com.zans.mms.model.Speed;
import com.zans.mms.model.SysConstantItem;
import com.zans.mms.service.IAssetService;
import com.zans.mms.service.IBaseDeviceTypeService;
import com.zans.mms.service.IFileService;
import com.zans.mms.service.ISpeedService;
import com.zans.mms.vo.asset.*;
import com.zans.mms.vo.asset.diagnosis.AssetDiagnosisInfoExReqVO;
import com.zans.mms.vo.devicepoint.DevicePointEditReqVO;
import com.zans.mms.vo.devicepoint.DevicePointQueryVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

import static com.zans.base.config.EnumErrorCode.SERVER_UPLOAD_MAX_SIZE_ERROR;
import static com.zans.base.config.EnumErrorCode.SERVER_UPLOAD_MIME_ERROR;
import static com.zans.base.config.GlobalConstants.*;

/**
 * @author
 * @version V1.0
 **/
@RestController
@RequestMapping("asset")
@Api(tags = "资产管理")
@Validated
@Slf4j
public class AssetController extends BaseController {


    @Autowired
    private IAssetService assetService;

    @Autowired
    IBaseDeviceTypeService baseDeviceTypeService;
    @Autowired
    private HttpHelper httpHelper;

    @Value("${api.upload.folder}")
    String uploadFolder;

    @Autowired
    IFileService fileService;

    @Autowired
    ISpeedService speedService;



    /**
     *  获取所有数据
     *
     * @param vo  AssetReqVo
     * @return     ApiResult<PageInfo<AssetResVo>>
     */
     @ApiOperation(value = "获取数据列表", notes = "获取数据列表")
     @PostMapping(value = "/electricPolice/list",  produces = MediaType.APPLICATION_JSON_VALUE)
     public ApiResult getList(@Valid @RequestBody AssetQueryVO vo) {
         super.checkPageParams(vo);
         String deviceType = baseDeviceTypeService.getTypeIdByName(ELECTRIC_POLICE);
         vo.setDeviceType(deviceType);
         return assetService.getList(vo);
     }

    /**
     *  获取信息
     *
     * @param assetId    assetId
     * @return     ApiResult<AssetResVo>
     */
     @ApiOperation(value = "查询指定数据", notes = "查询指定数据")
     @GetMapping(value = "/electricPolice/view",  produces = MediaType.APPLICATION_JSON_VALUE)
     public ApiResult<AssetViewResVO> electricPolice(Long assetId) {
        AssetViewResVO viewResp = assetService.getViewById(assetId);
        if (null == viewResp) {
        return   ApiResult.error("不存在此记录");
        }
        return ApiResult.success(viewResp);
     }

    /**
     *  获取所有数据
     *
     * @param vo  AssetReqVo
     * @return     ApiResult<PageInfo<AssetResVo>>
     */
     @ApiOperation(value = "获取数据列表", notes = "获取数据列表")
     @PostMapping(value = "/bayonet/list",  produces = MediaType.APPLICATION_JSON_VALUE)
     public ApiResult getBayonetList(@Valid @RequestBody AssetQueryVO vo) {
         super.checkPageParams(vo);
         String deviceType = baseDeviceTypeService.getTypeIdByName(BAYONET);
         vo.setDeviceType(deviceType);
         return assetService.getList(vo);
     }

    /**
     *  获取信息
     *
     * @param assetId    assetId
     * @return     ApiResult<AssetResVo>
     */
     @ApiOperation(value = "查询指定数据", notes = "查询指定数据")
     @GetMapping(value = "/bayonet/view",  produces = MediaType.APPLICATION_JSON_VALUE)
     public ApiResult<AssetViewResVO> bayonetView(Long assetId) {
        AssetViewResVO viewResp = assetService.getViewById(assetId);
        if (null == viewResp) {
        return   ApiResult.error("不存在此记录");
        }
        return ApiResult.success(viewResp);
     }
    /**
     *  获取所有数据
     *
     * @param vo  AssetReqVo
     * @return     ApiResult<PageInfo<AssetResVo>>
     */
     @ApiOperation(value = "获取数据列表", notes = "获取数据列表")
     @PostMapping(value = "/monitor/list",  produces = MediaType.APPLICATION_JSON_VALUE)
     public ApiResult getMonitorList(@Valid @RequestBody AssetQueryVO vo) {
         super.checkPageParams(vo);
         String deviceType = baseDeviceTypeService.getTypeIdByName(MONITOR);
         vo.setDeviceType(deviceType);
//         return assetService.getList(vo);
         return assetService.getMonitorList(vo);
     }

    /**
     *  获取信息
     *
     * @param assetId    assetId
     * @return     ApiResult<AssetResVo>
     */
     @ApiOperation(value = "查询指定数据", notes = "查询指定数据")
     @GetMapping(value = "/monitor/view",  produces = MediaType.APPLICATION_JSON_VALUE)
     public ApiResult<AssetViewResVO> monitorView(Long assetId) {
        AssetViewResVO viewResp = assetService.getViewById(assetId);
        if (null == viewResp) {
        return   ApiResult.error("不存在此记录");
        }
        return ApiResult.success(viewResp);
     }

    /**
     *  获取监控视频诊断的信息
     *
     * @param assetCode    assetId
     * @return     ApiResult<AssetResVo>
     */
    @ApiOperation(value = "查询视频诊断数据", notes = "查询视频诊指定数据")
    @GetMapping(value = "/monitor/diagnosis/view",  produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiResult<AssetViewResVO> monitorDiagnosisView(@RequestParam(value = "assetCode") String assetCode, @RequestParam(value = "traceId",defaultValue = "") String traceId) {
        return assetService.getDiagnosisView(assetCode,traceId);
    }


    @ApiOperation(value = "视频诊断标记数据", notes = "视频诊断标记数据")
    @PostMapping(value = "/monitor/diagnosis/flag",  produces = MediaType.APPLICATION_JSON_VALUE)
    @Record(module = MMSConstants.MODULE_ASSET,operation = MMSConstants.LOG_OPERATION_DIAGNOSIS)
    public ApiResult flagDiagnosis(@Valid @RequestBody AssetDiagnosisInfoExReqVO vo) {
        return assetService.flagDiagnosis(vo);
    }

    /**
     *  获取所有数据
     *
     * @param vo  AssetReqVo
     * @return     ApiResult<PageInfo<AssetResVo>>
     */
     @ApiOperation(value = "获取数据列表", notes = "获取数据列表")
     @PostMapping(value = "/trafficSignal/list",  produces = MediaType.APPLICATION_JSON_VALUE)
     public ApiResult getTrafficSignalList(@Valid @RequestBody AssetQueryVO vo) {
         super.checkPageParams(vo);
         String deviceType = baseDeviceTypeService.getTypeIdByName(TRAFFIC_SIGNAL);
         vo.setDeviceType(deviceType);
         return assetService.getList(vo);
     }

    /**
     *  获取信息
     *
     * @param assetId    assetId
     * @return     ApiResult<AssetResVo>
     */
     @ApiOperation(value = "查询指定数据", notes = "查询指定数据")
     @GetMapping(value = "/trafficSignal/view",  produces = MediaType.APPLICATION_JSON_VALUE)
     public ApiResult<AssetViewResVO> trafficSignalView(Long assetId) {
        AssetViewResVO viewResp = assetService.getViewById(assetId);
        if (null == viewResp) {
        return   ApiResult.error("不存在此记录");
        }
        return ApiResult.success(viewResp);
     }
    /**
     *  获取所有数据
     *
     * @param vo  AssetReqVo
     * @return     ApiResult<PageInfo<AssetResVo>>
     */
     @ApiOperation(value = "获取数据列表", notes = "获取数据列表")
     @PostMapping(value = "/electronicLabel/list",  produces = MediaType.APPLICATION_JSON_VALUE)
     public ApiResult getElectronicLabelList(@Valid @RequestBody AssetQueryVO vo) {
         super.checkPageParams(vo);
         String deviceType = baseDeviceTypeService.getTypeIdByName(ELECTRONIC_LABEL);
         vo.setDeviceType(deviceType);
         return assetService.getList(vo);
     }

    /**
     *  获取信息
     *
     * @param assetId    assetId
     * @return     ApiResult<AssetResVo>
     */
     @ApiOperation(value = "查询指定数据", notes = "查询指定数据")
     @GetMapping(value = "/electronicLabel/view",  produces = MediaType.APPLICATION_JSON_VALUE)
     public ApiResult<AssetViewResVO> electronicLabelView(Long assetId) {
        AssetViewResVO viewResp = assetService.getViewById(assetId);
        if (null == viewResp) {
        return   ApiResult.error("不存在此记录");
        }
        return ApiResult.success(viewResp);
     }

    /**
     *  获取所有数据
     *
     * @param vo  AssetReqVo
     * @return     ApiResult<PageInfo<AssetResVo>>
     */
    @ApiOperation(value = " 资产诱导屏查询", notes = " 资产诱导屏查询")
    @PostMapping(value = "/inductionScreen/list",  produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiResult getInductionScreenList(@Valid @RequestBody AssetQueryVO vo) {
        super.checkPageParams(vo);
//        String deviceType = baseDeviceTypeService.getTypeIdByName(ELECTRONIC_LABEL);
        vo.setDeviceType("17");
        return assetService.getList(vo);
    }

    /**
     *  获取信息
     *
     * @param assetId    assetId
     * @return     ApiResult<AssetResVo>
     */
    @ApiOperation(value = "资产诱导屏详情", notes = "资产诱导屏详情")
    @GetMapping(value = "/inductionScreen/view",  produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiResult<AssetViewResVO> inductionScreenView(Long assetId) {
        AssetViewResVO viewResp = assetService.getViewById(assetId);
        if (null == viewResp) {
            return   ApiResult.error("不存在此记录");
        }
        return ApiResult.success(viewResp);
    }




    /**
     * 新增一条数据
     *
     * @param reqVO 实体类
     * @return Response对象
     */
    @ApiOperation(value = "新增", notes = "新增")
    @RequestMapping(value = "save", method = RequestMethod.POST)
    @Record(module = MMSConstants.MODULE_ASSET,operation = MMSConstants.LOG_OPERATION_SAVE)
    public ApiResult<Asset> insert(@RequestBody AssetAddReqVO reqVO, HttpServletRequest httpRequest) {
        UserSession userSession = httpHelper.getUser(httpRequest);
        Asset asset = new Asset();
        BeanUtils.copyProperties(reqVO,asset);
        asset.setCreator(userSession.getUserName());
        int result = assetService.saveSelective(asset);
        if (result > 0) {
            return ApiResult.success();
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
    @Record(module = MMSConstants.MODULE_ASSET,operation = MMSConstants.LOG_OPERATION_EDIT)
    public ApiResult<Asset> update(@RequestBody AssetEditReqVO reqVO, HttpServletRequest httpRequest) {
        Asset asset = new Asset();
        BeanUtils.copyProperties(reqVO,asset);
        asset.setId(reqVO.getAssetId());
        assetService.synchronousProjectName(asset);
        assetService.updateSelective(asset);
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
    @Record(module = MMSConstants.MODULE_ASSET,operation = MMSConstants.LOG_OPERATION_DELETE)
    public ApiResult delete(String id, HttpServletRequest httpRequest) {
       /* Boolean b =  assetService.existRelation(id);
        if (b){
            return ApiResult.error("存在业务关系，不能删除");
        }*/
        //级联删除 删除在资产子集表中的数据
       assetService.deleteByUniqueId(id);
        return ApiResult.success();
    }




    /**
     * 资产通用导入模板下载
     * @param request
     * @param response
     * @throws Exception
     */
    @ApiOperation(value = "/download/template/{type}", notes = "资产导入模板下载")
    @GetMapping("/download/template/{type}")
    @Record(module = MMSConstants.MODULE_ASSET,operation = MMSConstants.LOG_OPERATION_DOWNLOAD)
    public void downloadTemplate (@PathVariable("type") String type, HttpServletRequest request,
                                           HttpServletResponse response) throws Exception {
        Map<String, String> map = assetService.downloadTemplate(type);
        String filePath=map.get("filePath");
        String fileCnName=map.get("fileCnName");
        this.download(filePath, fileCnName, request, response);
    }

    /**
     * 资产数据导入接口 如电警、卡口、诱导屏等
     * @param file 上传的excel文件
     * @param type 文件类型：电警/卡口/诱导屏等
     * @param request
     * @return
     * @throws Exception
     */
    @ApiOperation(value = "/upload/{type}", notes = "上传文件，导入资产数据")
    @PostMapping("/upload/{type}")
    @ResponseBody
    @Record(module = MMSConstants.MODULE_ASSET,operation = MMSConstants.LOG_OPERATION_IMPORT)
    public ApiResult uploadFile(@RequestParam("file") MultipartFile file,@PathVariable("type")String type, @RequestParam("req") String json,
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
            UserSession userSession =  httpHelper.getUser(request);
            //todo 导入数据方法改造 第一步 先拿到处理完的数据
            /**
             * 需求时间：2021/10/25
             * 需求概述：需要改造
             * 改造第一步：先把导入改成异步的 能看见进度条
             */
            JSONObject jsonObject = JSONObject.parseObject(json);
            Integer backup =  Integer.parseInt(jsonObject.get("backup").toString());
            Integer operation = Integer.parseInt(jsonObject.get("operation").toString());
            //如果确定备份数据
            if(backup==1){
                assetService.backupTable();
            }
            List<ExcelAssetVO> assetVOS = assetService.batchAddAsset(newFileName, originName, userSession, type);
            String absoluteNewFilePath = this.uploadFolder + newFileName;
            //然后进行异步的数据处理
            //先查询出当前的进度id 然后插入一条进度数据
            Speed speed = new Speed();
            speed.setTotal(assetVOS.size());
            speed.setFilePath(absoluteNewFilePath);
            speed.setCreator(userSession.getUserName());
            speed.setModule("资产");
            speed.setSuccessCount(0);
            speed.setUpdateCount(0);
            speed.setAddCount(0);
            speed.setFailCount(0);
            speedService.insertOne(speed);
            assetService.dealUploadData(operation,speed,assetVOS,absoluteNewFilePath,userSession);
            return ApiResult.success(speed);
        }
        return ApiResult.error("file save error");
    }


    @ApiOperation(value = "/download/errorFile", notes = "下载上传失败的文件")
    @RequestMapping(value = "/download/errorFile")
    @Record(module = MMSConstants.MODULE_ASSET,operation = MMSConstants.LOG_OPERATION_DOWNLOAD)
    public void errorFile(HttpServletRequest request,
                          HttpServletResponse response, @RequestParam(value = "errorFilePath") String errorFilePath) throws Exception {
        String date = DateHelper.formatDate(new Date(), "yyyyMMddHHmmss");
        String fileCnName = "资产信息上传失败" + date+".xlsx";
        this.download(errorFilePath, fileCnName, request, response);
    }


    /**
     *  获取信息
     *
     * @param assetId    assetId
     * @return     ApiResult<AssetResVo>
     */
    @ApiOperation(value = "查询指定数据", notes = "查询指定数据")
    @GetMapping(value = "/view",  produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiResult<AssetViewResVO> view(Long assetId) {
        AssetViewResVO viewResp = assetService.getViewById(assetId);
        if (null == viewResp) {
            return   ApiResult.error("不存在此记录");
        }
        return ApiResult.success(viewResp);
    }



    /**
     * 导出所有资产数据
     * @param request
     * @param response
     * @throws Exception
     */
    @ApiOperation(value = "/export/{type}", notes = "资产数据导出")
    @RequestMapping(value = "/export/{type}", method = {RequestMethod.POST})
    @Record(module = MMSConstants.MODULE_POINT,operation = MMSConstants.LOG_OPERATION_DOWNLOAD)
    public void export(HttpServletRequest request,
                       HttpServletResponse response,@PathVariable("type")String type,@RequestBody AssetExportQueryVO vo) throws Exception {
        String date = DateHelper.formatDate(new Date(), "yyyyMMddHHmmss");
        String fileName=assetService.generatedFileName(date,type);
       /* String fileName = "资产数据导出" + date+".xlsx";*/
        String filePath=assetService.export(type,vo,fileName);
        this.download(filePath, fileName, request, response);
    }



    @ApiOperation(value = "人工标记数据导出", notes = "人工标记数据导出")
    @RequestMapping(value = "/export/flag", method = {RequestMethod.GET})
    @Record(module = MMSConstants.MODULE_POINT,operation = MMSConstants.LOG_OPERATION_DOWNLOAD)
    public void exportFlag(HttpServletRequest request,
                       HttpServletResponse response) throws Exception {
        String date = DateHelper.formatDate(new Date(), "yyyyMMddHHmmss");
        String fileName = "标记图片_" + date+".zip";
        String filePath=assetService.exportFlag();
        this.download(filePath, fileName, request, response);
    }

    @ApiOperation(value = "人工标记数据导出(.txt)", notes = "人工标记数据导出(.txt)")
    @RequestMapping(value = "/export/flagByTxt", method = {RequestMethod.GET})
    @Record(module = MMSConstants.MODULE_POINT,operation = MMSConstants.LOG_OPERATION_DOWNLOAD)
    public void exportFlagByKind(HttpServletRequest request,
                           HttpServletResponse response) throws Exception {
        String date = DateHelper.formatDate(new Date(), "yyyyMMddHHmmss");
        String fileName = "标记_" + date+".zip";
        String filePath=assetService.exportFlagByKind(response);
        this.download(filePath, fileName, request, response);
    }


    /**
     *  获取导入的所有数据
     *
     * @param vo  AssetReqVo
     * @return     ApiResult<PageInfo<AssetResVo>>
     */
    @ApiOperation(value = "获取数据列表", notes = "获取数据列表")
    @PostMapping(value = "/importList",  produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiResult importList(@Valid @RequestBody AssetQueryVO vo) {
        super.checkPageParams(vo);
        return assetService.getImportList(vo);
    }

    /**
     * 导入数据修改
     *
     * @param assetEditReqVO 实体类
     * @return Response对象
     */
    @ApiOperation(value = "导入数据修改", notes = "导入数据修改")
    @RequestMapping(value = "updateImport", method = RequestMethod.POST)
    @Record(module = MMSConstants.MODULE_POINT,operation = MMSConstants.LOG_OPERATION_EDIT)
    public ApiResult updateImport(@RequestBody AssetEditReqVO assetEditReqVO) {
        Asset asset = new Asset();
        BeanUtils.copyProperties(assetEditReqVO,asset);
        asset.setId(assetEditReqVO.getAssetId());
        assetService.updateImportSelective(asset);
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
    public ApiResult importConfig(@RequestBody AssetQueryVO queryVO, HttpServletRequest httpRequest) {
        UserSession userSession = httpHelper.getUser(httpRequest);
        assetService.importConfig(queryVO,userSession);
        return ApiResult.success();
    }
}
