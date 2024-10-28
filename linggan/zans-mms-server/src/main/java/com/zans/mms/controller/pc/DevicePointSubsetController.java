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
import com.zans.mms.model.DevicePointSubset;
import com.zans.mms.service.IDevicePointSubsetDetailService;
import com.zans.mms.service.IDevicePointSubsetService;
import com.zans.mms.service.IFileService;
import com.zans.mms.vo.asset.subset.AssetSubsetDetaiQueryReqVO;
import com.zans.mms.vo.devicepoint.subset.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
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
import java.util.List;
import java.util.Map;

import static com.zans.base.config.EnumErrorCode.SERVER_UPLOAD_MAX_SIZE_ERROR;
import static com.zans.base.config.EnumErrorCode.SERVER_UPLOAD_MIME_ERROR;
import static com.zans.base.config.GlobalConstants.UPLOAD_FILE_MAX_SIZE;

/**
 * @author
 * @version V1.0
 **/
@RestController
@RequestMapping("devicePointSubset")
@Api(tags = "devicePointSubset管理")
@Validated
public class DevicePointSubsetController extends BaseController {

    @Autowired
    private IDevicePointSubsetService devicePointSubsetService;
    @Autowired
    private IDevicePointSubsetDetailService devicePointSubsetDetailService;

    @Autowired
    private HttpHelper httpHelper;


    @Value("${api.upload.folder}")
    String uploadFolder;

    @Autowired
    IFileService fileService;

    /**
     *  获取所有数据
     *
     * @param vo  DevicePointSubsetReqVO
     * @return     ApiResult
     */
     @ApiOperation(value = "获取数据列表", notes = "获取数据列表")
     @PostMapping(value = "/list",  produces = MediaType.APPLICATION_JSON_VALUE)
     public ApiResult getList(@Valid @RequestBody DevicePointSubsetQueryVO vo) {
         super.checkPageParams(vo);
         return devicePointSubsetService.getList(vo);
     }

     /**
     * 新增一条数据
     *
     * @param reqVO 实体类
     * @return Response对象
     */
     @ApiOperation(value = "新增", notes = "新增")
     @RequestMapping(value = "save", method = RequestMethod.POST)
     @Record(module = MMSConstants.MODULE_POINT_SUBSET,operation = MMSConstants.LOG_OPERATION_SAVE)
     public ApiResult<DevicePointSubset> insert(@RequestBody DevicePointSubsetAddReqVO reqVO, HttpServletRequest httpRequest) {
         UserSession userSession = httpHelper.getUser(httpRequest);
         DevicePointSubset devicePointSubset = new DevicePointSubset();
         BeanUtils.copyProperties(reqVO,devicePointSubset);
         devicePointSubset.setCreator(userSession.getUserName());
         int result = devicePointSubsetService.save(devicePointSubset);
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
     @Record(module = MMSConstants.MODULE_POINT_SUBSET,operation = MMSConstants.LOG_OPERATION_EDIT)
     public ApiResult<DevicePointSubset> update(@RequestBody DevicePointSubsetEditReqVO reqVO, HttpServletRequest httpRequest) {
         DevicePointSubset devicePointSubset = new DevicePointSubset();
         BeanUtils.copyProperties(reqVO,devicePointSubset);
         devicePointSubset.setUpdateTime(new Date());
         devicePointSubset.setId(reqVO.getSubsetId());
         devicePointSubsetService.updateSelective(devicePointSubset);
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
     @Record(module = MMSConstants.MODULE_POINT_SUBSET,operation = MMSConstants.LOG_OPERATION_DELETE)
     public ApiResult delete(Long subsetId, HttpServletRequest httpRequest) {
//         Boolean b =  devicePointSubsetService.existRelation(subsetId);
//         if (b){
//             return ApiResult.error("存在业务关系，不能删除");
//         }
         devicePointSubsetService.deleteById(subsetId);
         return ApiResult.success();
     }


    @Record(module = MMSConstants.MODULE_POINT_SUBSET,operation = MMSConstants.LOG_OPERATION_BATCH_EDIT)
    @ApiOperation(value = "/groups/addPoint", notes = "分组中添加资产")
    @RequestMapping(value = "/groups/addPoint", method = {RequestMethod.POST})
    @ResponseBody
    public ApiResult groupsAddPoint(@RequestBody PointSubsetDetailAddReqVO reqVO, HttpServletRequest request) {
        UserSession userSession = httpHelper.getUser(request);
//        reqVO.setAddType(PatrolConstants.POINT_SUBSET_ADD_IGNORE);
        devicePointSubsetDetailService.groupsAddPoint(reqVO,userSession);

        return ApiResult.success();
    }

    @Record(module = MMSConstants.MODULE_POINT_SUBSET,operation = MMSConstants.LOG_OPERATION_BATCH_EDIT)
    @ApiOperation(value = "/groups/addPointByCondition", notes = "分组中根据条件添加点位")
    @RequestMapping(value = "/groups/addPointByCondition", method = {RequestMethod.POST})
    @ResponseBody
    public ApiResult groupsAddPointByCondition(@RequestBody PointSubsetDetailAddByConditionReqVO reqVO, HttpServletRequest request) {
        UserSession userSession = httpHelper.getUser(request);
//        reqVO.setAddType(PatrolConstants.POINT_SUBSET_ADD_IGNORE);
        devicePointSubsetDetailService.groupsAddPointByCondition(reqVO,userSession);
        return ApiResult.success();
    }

    @Record(module = MMSConstants.MODULE_POINT_SUBSET,operation = MMSConstants.LOG_OPERATION_BATCH_DELETE)
    @ApiOperation(value = "/groups/removePoint", notes = "分组移除资产")
    @RequestMapping(value = "/groups/removePoint", method = {RequestMethod.POST})
    @ResponseBody
    public ApiResult groupsRemovePoint(@RequestBody PointSubsetDetailAddReqVO reqVO, HttpServletRequest request) {
        devicePointSubsetDetailService.removePoint(reqVO);
        return ApiResult.success();
    }

    /**
     * 资产子集表查询
     */
    @ApiOperation(value = "资产子集表查询", notes = "资产子集表查询")
    @RequestMapping(value = "/groups/list", method = RequestMethod.POST)
    public ApiResult list(@RequestBody AssetSubsetDetaiQueryReqVO reqVO) {
        super.checkPageParams(reqVO);
        return devicePointSubsetDetailService.groupList(reqVO);
    }

    /**
     * 获取所有数据
     *
     * @param
     * @return
     */
    @ApiOperation(value = "获取数据列表", notes = "获取数据列表")
    @GetMapping(value = "/select/list", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiResult getSelectList() {
        List<SelectVO> pointSubsetList = devicePointSubsetService.getSelectList();
        Map<String, Object> result = MapBuilder.getBuilder()
                .put("pointSubsetList",pointSubsetList)
                .build();
        return ApiResult.success(result);
    }


    /**
     * 巡检子集导入模板下载
     * @param request
     * @param response
     * @throws Exception
     */
    @ApiOperation(value = "/download/template", notes = "巡检子集导入模板下载")
    @GetMapping("/download/template")
    @Record(module = MMSConstants.MODULE_POINT_SUBSET,operation = MMSConstants.LOG_OPERATION_DOWNLOAD)
    public void downloadElectricPoliceFile(HttpServletRequest request,
                                           HttpServletResponse response) throws Exception {
        String filePath = this.uploadFolder+"template/巡检子集导入-excel模板.xlsx";
        String date = DateHelper.formatDate(new Date(), "yyyyMMddHHmmss");
        String fileCnName = "巡检子集导入模板" + date+".xlsx";
        this.download(filePath, fileCnName, request, response);
    }


    /**
     * 下载失败的巡检子集信息
     * @param request
     * @param response
     * @param errorFilePath
     * @throws Exception
     */
    @ApiOperation(value = "/download/errorFile", notes = "下载上传失败的文件")
    @RequestMapping(value = "/download/errorFile")
    @Record(module = MMSConstants.MODULE_POINT_SUBSET,operation = MMSConstants.LOG_OPERATION_DOWNLOAD)
    public void errorFile(HttpServletRequest request,
                          HttpServletResponse response, @RequestParam(value = "errorFilePath") String errorFilePath) throws Exception {
        String date = DateHelper.formatDate(new Date(), "yyyyMMddHHmmss");
        String fileCnName = "巡检子集信息上传失败" + date+".xlsx";
        this.download(errorFilePath, fileCnName, request, response);
    }

    /**
     * 文件上传 导入巡检子集数据
     * @param file 文件
     * @param json 前端参数
     * @param request
     * @return
     * @throws Exception
     */
    @ApiOperation(value = "/upload", notes = "上传文件，导入点位子集数据")
    @PostMapping("/upload")
    @ResponseBody
    @Record(module = MMSConstants.MODULE_POINT_SUBSET,operation = MMSConstants.LOG_OPERATION_IMPORT)
    public ApiResult uploadFile(@RequestParam("file") MultipartFile file,@RequestParam("req") String json,
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
            return devicePointSubsetDetailService.batchAddDevicePointSubset(newFileName, originName,userSession,json);
        }
        return ApiResult.error("file save error");
    }


    /**
     * 导出巡检子集
     * @return
     */
    @ApiOperation(value = "/exportFile",notes = "巡检子集excel导出")
    @GetMapping("/exportFile")
    @ResponseBody
    @Record(module = MMSConstants.MODULE_POINT_SUBSET,operation = MMSConstants.LOG_OPERATION_DOWNLOAD)
    public void exportFile(@RequestParam("subsetId") Long subsetId, HttpServletRequest request, HttpServletResponse response) throws Exception {
        String date = DateHelper.formatDate(new Date(), "yyyyMMddHHmmss");
        String fileName = "巡检子集数据导出" + date+".xls";
        String filePath=devicePointSubsetDetailService.exportFile(subsetId,fileName);
        this.download(filePath, fileName, request, response);
    }

    /**
     * 巡检子集中的点位数据导出
     * @param subsetId 点位子集id
     * @param request
     * @param response
     * @throws Exception
     */
    @ApiOperation(value = "/export",notes = "巡检子集点位excel导出")
    @RequestMapping("/export")
    @ResponseBody
    @Record(module = MMSConstants.MODULE_POINT_SUBSET,operation = MMSConstants.LOG_OPERATION_DOWNLOAD)
    public void export(@RequestParam("subsetId") Long subsetId, HttpServletRequest request,HttpServletResponse response) throws  Exception{
        String date = DateHelper.formatDate(new Date(), "yyyyMMddHHmmss");
        String fileName = "巡检子集数据导出" + date+".xls";
        String filePath=devicePointSubsetDetailService.exportFile(subsetId,fileName);
        this.download(filePath, fileName, request, response);
    }



    /**
     * 删除一条数据
     *
     * @param subsetId 参数对象
     * @return Response对象
     */
    @ApiOperation(value = "清空一个子集数据", notes = "清空一个子集数据")
    @RequestMapping(value = "clear", method = RequestMethod.GET)
    @Record(module = MMSConstants.MODULE_POINT_SUBSET,operation = MMSConstants.LOG_OPERATION_DELETE)
    public ApiResult clear(Long subsetId) {
        devicePointSubsetService.clearById(subsetId);
        return ApiResult.success();
    }

}
