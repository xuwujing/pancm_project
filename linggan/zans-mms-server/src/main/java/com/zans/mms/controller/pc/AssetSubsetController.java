package com.zans.mms.controller.pc;

import com.zans.base.annotion.Record;
import com.zans.base.controller.BaseController;
import com.zans.base.office.excel.ExcelHelper;
import com.zans.base.util.DateHelper;
import com.zans.base.util.FileHelper;
import com.zans.base.util.HttpHelper;
import com.zans.base.vo.ApiResult;
import com.zans.base.vo.UserSession;
import com.zans.mms.config.MMSConstants;
import com.zans.mms.model.AssetSubset;
import com.zans.mms.service.IAssetService;
import com.zans.mms.service.IAssetSubsetDetailService;
import com.zans.mms.service.IAssetSubsetService;
import com.zans.mms.service.IFileService;
import com.zans.mms.vo.asset.subset.*;
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
import java.util.Date;

import static com.zans.base.config.EnumErrorCode.SERVER_UPLOAD_MAX_SIZE_ERROR;
import static com.zans.base.config.EnumErrorCode.SERVER_UPLOAD_MIME_ERROR;
import static com.zans.base.config.GlobalConstants.UPLOAD_FILE_MAX_SIZE;

/**
 * @author
 * @version V1.0
 **/
@RestController
@RequestMapping("assetSubset")
@Api(tags = "资产子集管理")
@Validated
@Slf4j
public class AssetSubsetController extends BaseController {

    @Autowired
    private IAssetSubsetService assetSubsetService;
    @Autowired
    private HttpHelper httpHelper;
    @Autowired
    private IAssetSubsetDetailService assetSubsetDetailService;

    @Autowired
    private IAssetService assetService;

    @Value("${api.upload.folder}")
    String uploadFolder;

    @Autowired
    IFileService fileService;


    /**
     *  获取所有数据
     *
     * @param vo  AssetSubsetReqVO
     * @return     ApiResult
     */
     @ApiOperation(value = "获取数据列表", notes = "获取数据列表")
     @PostMapping(value = "/list",  produces = MediaType.APPLICATION_JSON_VALUE)
     public ApiResult getList(@Valid @RequestBody AssetSubsetQueryVO vo) {
         super.checkPageParams(vo);
         return assetSubsetService.getList(vo);
     }


     /**
     * 新增一条数据
     *
     * @param reqVO 实体类
     * @return Response对象
     */
     @ApiOperation(value = "新增", notes = "新增")
     @RequestMapping(value = "save", method = RequestMethod.POST)
     @Record(module = MMSConstants.MODULE_ASSET_SUBSET,operation = MMSConstants.LOG_OPERATION_SAVE)
     public ApiResult<AssetSubset> insert(@RequestBody AssetSubsetAddReqVO reqVO, HttpServletRequest httpRequest) {
         UserSession userSession = httpHelper.getUser(httpRequest);
         int count = assetSubsetService.findByName(reqVO.getSubsetName(), null);
         if (count>0) {
             return ApiResult.error("存在同名分组");
         }
         AssetSubset assetSubset = new AssetSubset();
         BeanUtils.copyProperties(reqVO,assetSubset);
         assetSubset.setCreator(userSession.getUserName());
         int result = assetSubsetService.saveSelective(assetSubset);
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
     @Record(module = MMSConstants.MODULE_ASSET_SUBSET,operation = MMSConstants.LOG_OPERATION_EDIT)
     public ApiResult<AssetSubset> update(@RequestBody AssetSubsetEditReqVO reqVO, HttpServletRequest httpRequest) {
         AssetSubset assetSubset = new AssetSubset();
         BeanUtils.copyProperties(reqVO,assetSubset);
         assetSubset.setUpdateTime(new Date());
         assetSubset.setId(reqVO.getSubsetId());
         assetSubsetService.updateSelective(assetSubset);
         return ApiResult.success();
     }


     /**
     * 删除一条数据
     *
     * @param subsetId 参数对象
     * @return Response对象
     */
     @ApiOperation(value = "删除", notes = "删除")
     @RequestMapping(value = "del", method = RequestMethod.GET)
     @Record(module = MMSConstants.MODULE_ASSET_SUBSET,operation = MMSConstants.LOG_OPERATION_DELETE)
     public ApiResult delete(Long subsetId, HttpServletRequest httpRequest) {
         assetSubsetService.deleteByUniqueId(subsetId);
         return ApiResult.success();
     }

    @Record(module = MMSConstants.MODULE_ASSET_SUBSET,operation = MMSConstants.LOG_OPERATION_SAVE)
    @ApiOperation(value = "分组中添加资产", notes = "分组中添加资产")
    @RequestMapping(value = "/groups/addAsset", method = {RequestMethod.POST})
    @ResponseBody
    public ApiResult groupsAddAsset(@RequestBody AssetSubsetDetailAddReqVO reqVO, HttpServletRequest request) {
        UserSession userSession = httpHelper.getUser(request);
        assetSubsetDetailService.groupsAddAsset(reqVO,userSession);
        return ApiResult.success();
    }

    @Record(module = MMSConstants.MODULE_ASSET_SUBSET,operation = MMSConstants.LOG_OPERATION_SAVE)
    @ApiOperation(value = "分组中根据条件添加资产", notes = "分组中根据条件添加资产")
    @RequestMapping(value = "/groups/addAssetByCondition", method = {RequestMethod.POST})
    @ResponseBody
    public ApiResult groupsAddAssetByCondition(@RequestBody(required=false) AssetSubsetDetailAddByConditionReqVO reqVO, HttpServletRequest request) {
        UserSession userSession = httpHelper.getUser(request);
        assetSubsetDetailService.groupsAddAssetByCondition(reqVO,userSession);
        return ApiResult.success();
    }





    @Record(module = MMSConstants.MODULE_ASSET_SUBSET,operation = MMSConstants.LOG_OPERATION_BATCH_DELETE)
    @ApiOperation(value = "分组移除资产", notes = "分组移除资产")
    @RequestMapping(value = "/groups/removeAsset", method = {RequestMethod.POST})
    @ResponseBody
    public ApiResult groupsRemoveAsset(@RequestBody AssetSubsetDetailAddReqVO reqVO, HttpServletRequest request) {

        assetSubsetDetailService.removeAsset(reqVO);
        return ApiResult.success();
    }

    /**
     * 资产子集表查询
     */
    @ApiOperation(value = "资产子集表查询", notes = "资产子集表查询")
    @RequestMapping(value = "/groups/list", method = RequestMethod.POST)
    public ApiResult list(@RequestBody AssetSubsetDetaiQueryReqVO reqVO) {
        super.checkPageParams(reqVO);
        return assetSubsetDetailService.groupList(reqVO);
    }


    /**
     * 资产子集导入模板下载
     * @param request
     * @param response
     * @throws Exception
     */
    @ApiOperation(value = "/download/template", notes = "文件下载")
    @GetMapping("/download/template")
    @Record(module = MMSConstants.MODULE_ASSET_SUBSET,operation = MMSConstants.LOG_OPERATION_DOWNLOAD)
    public void downloadFile(HttpServletRequest request,
                             HttpServletResponse response) throws Exception {
        String filePath = this.uploadFolder+"template/资产子集导入-excel模板.xlsx";

        String date = DateHelper.formatDate(new Date(), "yyyyMMddHHmmss");
        String fileCnName = "资产子集导入模板" + date+".xlsx";
        this.download(filePath, fileCnName, request, response);
    }

    /**
     * 资产子集文件上传及数据导入
     * @param file 上传的excel文件，用于数据读取
     * @param json 接收前端传过来的数据 如isClean isDelete
     * @param request
     * @return
     * @throws Exception
     */
    @ApiOperation(value = "/upload", notes = "上传文件，post参数id")
    @PostMapping("/upload")
    @ResponseBody
    @Record(module = MMSConstants.MODULE_ASSET_SUBSET,operation = MMSConstants.LOG_OPERATION_IMPORT)
    public ApiResult uploadFile(@RequestParam("file") MultipartFile file,@RequestParam("req") String json,
                                HttpServletRequest request) throws Exception {
        log.info(json);
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

            return assetSubsetService.batchAddAssetSubset(newFileName, originName,userSession,json);
        }
        return ApiResult.error("file save error");
    }

    /**
     * 失败文件下载
     * @param request
     * @param response
     * @param errorFilePath
     * @throws Exception
     */
    @ApiOperation(value = "/download/errorFile", notes = "下载上传失败的文件")
    @RequestMapping(value = "/download/errorFile", method = {RequestMethod.GET})
    @Record(module = MMSConstants.MODULE_ASSET_SUBSET,operation = MMSConstants.LOG_OPERATION_DOWNLOAD)
    public void errorFile(HttpServletRequest request,
                          HttpServletResponse response, @RequestParam(value = "errorFilePath") String errorFilePath) throws Exception {
        String date = DateHelper.formatDate(new Date(), "yyyyMMddHHmmss");
        String fileCnName = "资产子集信息上传失败" + date+".xlsx";
        this.download(errorFilePath, fileCnName, request, response);
    }

    /**
     *  获取所有数据
     *
     * @param vo  AssetReqVo
     * @return     ApiResult<PageInfo<AssetResVo>>
     */
    @ApiOperation(value = "资产子集新增设备选择设备列表", notes = "资产子集新增设备选择设备列表")
    @PostMapping(value = "/chooseAsset/list",  produces = MediaType.APPLICATION_JSON_VALUE)
    @Record(module = MMSConstants.MODULE_ASSET_SUBSET,operation = MMSConstants.LOG_OPERATION_SAVE)
    public ApiResult getList(@Valid @RequestBody AssetChooseReqVO vo) {
        super.checkPageParams(vo);
        return assetService.chooseAssetList(vo);
    }

    /**
     * 导出巡检子集中资产
     * @return
     */
    @ApiOperation(value = "/export",notes = "资产子集excel导出")
    @PostMapping("/export")
    @ResponseBody
    @Record(module = MMSConstants.MODULE_ASSET_SUBSET,operation = MMSConstants.LOG_OPERATION_EXPORT)
    public void export(@RequestBody AssetSubsetDetaiQueryReqVO assetSubsetDetaiQueryReqVO, HttpServletRequest request, HttpServletResponse response) throws Exception {
        String date = DateHelper.formatDate(new Date(), "yyyyMMddHHmmss");
        String fileName = "资产子集数据导出" + date+".xls";
        String filePath=assetSubsetDetailService.export(assetSubsetDetaiQueryReqVO,fileName);
        this.download(filePath, fileName, request, response);
    }


    /**
     * 导出巡检子集
     * @return
     */
    @ApiOperation(value = "/exportFile",notes = "资产子集excel导出")
    @RequestMapping("/exportFile")
    @ResponseBody
    @Record(module = MMSConstants.MODULE_ASSET_SUBSET,operation = MMSConstants.LOG_OPERATION_EXPORT)
    public void exportFile(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String date = DateHelper.formatDate(new Date(), "yyyyMMddHHmmss");
        String fileName = "资产子集数据导出" + date+".xls";
        String filePath=assetSubsetDetailService.exportFile(fileName);
        this.download(filePath, fileName, request, response);
    }

//------------------------------------------------------一键ping start----------------------------------------------------------------

    /**
     * 一键ping统计
     * @return
     */
    @ApiOperation(value = "/一键ping统计",notes = "一键ping统计")
    @PostMapping(value = "/pingAndStats",  produces = MediaType.APPLICATION_JSON_VALUE)
    @Record(module = MMSConstants.MODULE_ASSET_SUBSET,operation = MMSConstants.LOG_OPERATION_BATCH_ADD)
    public ApiResult pingAndStats(HttpServletRequest request,@Valid @RequestBody AssetSubsetPingReqVO reqVO) {
        UserSession userSession = httpHelper.getUser(request);
        return assetSubsetDetailService.pingAndStats(reqVO,userSession);
    }

//------------------------------------------------------一键ping end------------------------------------------------------------------

    /**
     * 获取所有数据
     *
     * @param vo DevicePointReqVo
     * @return ApiResult<PageInfo < DevicePointResVo>>
     */
    @ApiOperation(value = "资产地图", notes = "资产地图")
    @PostMapping(value = "/map/list", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiResult getMapList(@Valid @RequestBody DevicePointQueryVO vo) {
        super.checkPageParams(vo);
        return assetSubsetService.getMapList(vo);
    }

    /**
     *  获取所有子集数据
     *
     * @param
     * @return     ApiResult
     */
    @ApiOperation(value = "获取所有子集数据", notes = "获取所有子集数据")
    @PostMapping(value = "/listAll",  produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiResult getListAll() {
        return assetSubsetService.getSelectList();
    }


    @ApiOperation(value = "清空一个子集数据", notes = "清空一个子集数据")
    @RequestMapping(value = "clear", method = RequestMethod.GET)
    @Record(module = MMSConstants.MODULE_ASSET_SUBSET,operation = MMSConstants.LOG_OPERATION_DELETE)
    public ApiResult clear(Long subsetId) {
        assetSubsetDetailService.clearById(subsetId);
        return ApiResult.success();
    }


}
