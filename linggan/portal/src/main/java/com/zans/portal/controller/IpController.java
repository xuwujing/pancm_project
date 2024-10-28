package com.zans.portal.controller;

import com.zans.base.annotion.Record;
import com.zans.base.util.DateHelper;
import com.zans.base.util.MapBuilder;
import com.zans.base.valid.MacAddress;
import com.zans.base.vo.ApiResult;
import com.zans.base.vo.PageResult;
import com.zans.base.vo.SelectVO;
import com.zans.base.vo.UserSession;
import com.zans.portal.config.GlobalConstants;
import com.zans.portal.dao.AssetMapper;
import com.zans.portal.model.Asset;
import com.zans.portal.model.IpAll;
import com.zans.portal.service.*;
import com.zans.portal.vo.asset.req.AssetEditReqVO;
import com.zans.portal.vo.ip.IpEditVO;
import com.zans.portal.vo.ip.IpSearchVO;
import com.zans.portal.vo.ip.IpVO;
import com.zans.portal.vo.ip.req.IpAllAddReqVO;
import com.zans.portal.vo.ip.req.IpAllEditReqVO;
import com.zans.portal.vo.ip.req.IpAllSerachReqVO;
import com.zans.portal.vo.radius.QzViewRespVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;
import java.util.Map;

import static com.zans.base.config.EnumErrorCode.CLIENT_PARAMS_ERROR;
import static com.zans.portal.config.GlobalConstants.*;
import static com.zans.portal.constants.PortalConstants.*;

@Api(value = "/ip", tags = {"/ip ~ IP地址管理"})
@RestController
@RequestMapping("/ip")
@Validated
@Slf4j
public class IpController extends BasePortalController {

    @Autowired
    IIpService ipService;

    @Autowired
    IConstantItemService constantItemService;

    @Autowired
    IDeviceTypeService deviceTypeService;

    @Autowired
    IAreaService areaService;



    @Autowired
    ILogOperationService logOperationService;



    @Value("${api.upload.folder}")
    String uploadFolder;

    @Autowired
    IFileService fileService;



    @Autowired
    private IAssetService iAssetService;

    @Autowired
    private AssetMapper assetMapper;

    @ApiOperation(value = "/list", notes = "地址分配查询")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "req", value = "查询条件", required = true,
                    dataType = "IpSearchVO", paramType = "body")
    })
    @RequestMapping(value = "/list", method = {RequestMethod.POST})
    public ApiResult<PageResult<IpVO>> findAllIp(@RequestBody IpAllSerachReqVO req) {
        PageResult<IpVO> pageResult = ipService.getIpPage(req);
        return ApiResult.success(pageResult);
    }

    @ApiOperation(value = "/list_init", notes = "地址分配查询初始化")
    @RequestMapping(value = "/list_init", method = {RequestMethod.GET})
    @ResponseBody
    public ApiResult init() {
        List<SelectVO> deviceTypeList = deviceTypeService.findDeviceTypeToSelect();
        List<SelectVO> authList = constantItemService.findItemsByDict(MODULE_IP_ALL_AUTH_STATUS);
        List<SelectVO> projectStatusList = constantItemService.findItemsByDict(MODULE_IP_ALL_PROJECT_STATUS);
        List<SelectVO> maintainStatusList = constantItemService.findItemsByDict(GlobalConstants.SYS_DICT_KEY_MAINTAIN_STATUS);
        IpSearchVO req = new IpAllSerachReqVO();
        req.setPageNum(1);
        req.setPageSize(10);
        PageResult<IpVO> pageResult = ipService.getIpPage(req);
        Map<String, Object> result = MapBuilder.getBuilder()
                .put("authStatus", authList)
                .put(MODULE_DEVICE, deviceTypeList)
                .put("projectStatus", projectStatusList)
                .put("maintainStatus", maintainStatusList)
                .put(INIT_DATA_TABLE, pageResult)
                .build();
        return ApiResult.success(result);
    }

    @ApiOperation(value = "/view", notes = "获取地址分配详细信息，by id")
    @ApiImplicitParam(name = "id", value = "地址id", required = true,
            dataType = "int", paramType = "query")
    @RequestMapping(value = "/view", method = {RequestMethod.GET})
    public ApiResult<IpVO> getIpById(@NotNull(message = "id必填") Integer id) {
        IpVO ip = ipService.getIp(id);
        if (ip == null) {
            return ApiResult.error(CLIENT_PARAMS_ERROR).appendMessage("IP地址.ID不存在#" + id);
        }
        return ApiResult.success(ip);
    }

    @ApiOperation(value = "/add", notes = "新增地址信息")
    @ApiImplicitParam(name = "editVO", value = "地址信息", required = true,
            dataType = "IpEditVO", paramType = "body")
    @RequestMapping(value = "/add", method = {RequestMethod.POST})
    @Record(module = PORTAL_MODULE_IP,operation = PORTAL_LOG_OPERATION_ADD)
    public ApiResult addIp(@Valid @RequestBody IpEditVO editVO,
                           HttpServletRequest request) {
        IpAll ip = new IpAll();
        BeanUtils.copyProperties(editVO, ip);
        Date theDate = DateHelper.parseDay(editVO.getTheDate());
        ip.setTheDate(theDate);
        ip.setAuthStatus(IP_AUTH_INIT);
        ipService.save(ip);

        return ApiResult.success(MapBuilder.getSimpleMap("id", ip.getId()));
    }

    @ApiOperation(value = "/edit", notes = "修改地址信息")
    @RequestMapping(value = "/edit", method = {RequestMethod.POST})
    @Record(module = PORTAL_MODULE_IP,operation = PORTAL_LOG_OPERATION_EDIT)
    @Transactional
    public ApiResult editIp(@Valid @RequestBody IpAllEditReqVO editVO,
                            HttpServletRequest request) {
        Integer id = editVO.getId();
        IpAll ip = ipService.getById(id);
        if (ip == null) {
            return ApiResult.error(CLIENT_PARAMS_ERROR).appendMessage("t_ip_all.id isn't existed#" + id);
        }
        BeanUtils.copyProperties(editVO, ip);
        ip.setLongitudeB(ip.getLongitude());
        ip.setLatitudeB(ip.getLatitude());

        if (PROJECT_ENV_XYGA.equals(PROJECT_ENV)) {
            ip.setLongitudeB(ip.getLongitude());
            ip.setLatitudeB(ip.getLatitude());
            ipService.updateSelective(ip);
        } else {

            if (StringUtils.isNotBlank(editVO.getTheDate())) {
                Date theDate = DateHelper.parseDay(editVO.getTheDate());
                ip.setTheDate(theDate);
            }
            ipService.update(ip);
            AssetEditReqVO editReqVOqVO = new AssetEditReqVO();
            String ipAddr = ip.getIpAddr();
            editReqVOqVO.setIpAddr(ipAddr);
            editReqVOqVO.setDeviceModelBrand(ip.getDeviceModelBrand());
            editReqVOqVO.setDeviceModelDes(ip.getDeviceModelDes());
            editReqVOqVO.setDeviceType(ip.getDeviceTypeId());
            editReqVOqVO.setContractor(ip.getContractor());
            editReqVOqVO.setContractorPerson(ip.getContractorPerson());
            editReqVOqVO.setContractorPhone(ip.getContractorPhone());
            editReqVOqVO.setPointName(ip.getPointName());
            editReqVOqVO.setProjectName(ip.getProjectName());
            editReqVOqVO.setMaintainPhone(ip.getMaintainPhone());
            Asset asset = assetMapper.findByIpAddr(ipAddr);
            //如果资产表不存在就进行新增操作
            if (asset == null) {
                log.info("地址信息修改数！ip:{} 在资产表中不存在！进行新增！", ipAddr);
                asset = new Asset();
                BeanUtils.copyProperties(editReqVOqVO, asset);
                Integer userId = getUserSession(request).getUserId();
                asset.setCreatorId(userId);
                asset.setUpdateId(userId);
                asset.setDeleteStatus(0);
                asset.setEnableStatus(1);
//                asset.setAlive(2);
//                asset.setAliveLastTime(DateHelper.parseDatetime("2020-01-01 00:00:00"));
                iAssetService.save(asset);
            } else {
                editReqVOqVO.setId(asset.getId());
                iAssetService.editAsset(editReqVOqVO, getUserSession(request));
            }
        }


        return ApiResult.success(MapBuilder.getSimpleMap("id", id));
    }

    @ApiOperation(value = "/delete", notes = "删除地址信息")
    @RequestMapping(value = "/delete", method = {RequestMethod.POST})
    @Record(module = PORTAL_MODULE_IP,operation = PORTAL_LOG_OPERATION_ONE_DELETE)
    public ApiResult delete(@NotNull(message = "IP.id") Integer id,
                            HttpServletRequest request) {
        IpAll ipItem = this.ipService.getById(id);
        if (ipItem == null) {
            return ApiResult.error(CLIENT_PARAMS_ERROR).appendMessage("t_ip_all.id isn't existed");
        }
        String mac = ipItem.getAuthMac();
        ipService.delete(ipItem);


        //2020-11-4 和北傲确认，删除t_ip_all的同时删除资产表的数据
        Asset asset = assetMapper.findByIpAddr(ipItem.getIpAddr());
        if (asset != null) {
            Integer aId = asset.getId();
//            iAssetService.deleteAsset(aId);
            assetMapper.deleteByPrimaryKey(aId);
        }

        // 记录日志

        return ApiResult.success(MapBuilder.getSimpleMap("id", id));
    }

    @ApiOperation(value = "/submit", notes = "提交入网申请")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "地址id", required = true,
                    dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "mac", value = "入网设备的MAC地址", required = true,
                    dataType = "string", paramType = "query")
    })
    @RequestMapping(value = "/submit", method = {RequestMethod.POST})
    @Record(module = PORTAL_MODULE_IP,operation = PORTAL_LOG_OPERATION_AUTH_SUBMIT)
    public ApiResult submit(@NotNull(message = "IP.id") Integer id,
                            @MacAddress String mac,
                            HttpServletRequest request) {
        IpAll ipItem = this.ipService.getById(id);
        if (ipItem == null) {
            return ApiResult.error(CLIENT_PARAMS_ERROR).appendMessage("入网认证ID不存在#" + id);
        }
        Integer status = ipItem.getAuthStatus();
        if (status != IP_AUTH_INIT && status != IP_AUTH_REJECT && status != IP_AUTH_REJECT_AGAIN) {
            return ApiResult.error(CLIENT_PARAMS_ERROR).appendMessage("入网认证审批状态不正确#" + status);
        }

        UserSession session = this.getUserSession(request);
        if (session != null) {
            ipItem.setAuthPerson(session.getNickName());
        }
        ipItem.setAuthMac(mac.toLowerCase());
        // 清空审批意见
        ipItem.setAuthMark(null);
        ipItem.setAuthStatus(IP_AUTH_SUBMIT);
        ipItem.setAuthTime(DateHelper.getNow());
        ipService.update(ipItem);

        // 记录日志


        return ApiResult.success(MapBuilder.getSimpleMap("id", id));
    }

    @ApiOperation(value = "/approve", notes = "提交入网审批")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "地址id", required = true,
                    dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "status", value = "审批，2，通过；3，否决", required = true,
                    dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "mark", value = "审批意见", required = true,
                    dataType = "string", paramType = "query")
    })
    @RequestMapping(value = "/approve", method = {RequestMethod.POST})
    @Record(module = PORTAL_MODULE_IP,operation = PORTAL_LOG_OPERATION_APPROVE)
    public ApiResult approve(@NotNull(message = "IP.id") Integer id,
                             @NotNull(message = "审批状态必填") @Min(value = 2) @Max(value = 3) Integer status,
                             @NotBlank(message = "审批意见必填") String mark,
                             HttpServletRequest request) {
        IpAll ipItem = this.ipService.getById(id);
        if (ipItem == null) {
            return ApiResult.error(CLIENT_PARAMS_ERROR).appendMessage("入网认证ID不存在");
        }
        if (ipItem.getAuthStatus() != IP_AUTH_SUBMIT) {
            return ApiResult.error(CLIENT_PARAMS_ERROR).appendMessage("入网认证审批状态不正确#" + ipItem.getAuthStatus());
        }

        UserSession session = this.getUserSession(request);
        ipService.doApprove(ipItem, session, status, mark);
        return ApiResult.success(MapBuilder.getSimpleMap("id", id));
    }




    @ApiOperation(value = "/download/template", notes = "文件下载")
    @GetMapping("/download/template")
    @Record(module = PORTAL_MODULE_IP,operation = PORTAL_LOG_TEMPLATE_DOWNLOAD)
    public void downloadFile(HttpServletRequest request,
                             HttpServletResponse response) throws Exception {
        String filePath = this.uploadFolder + "/template/资产上传模板(襄阳).xlsx";
        String date = DateHelper.formatDate(new Date(), "yyyyMMddHHmmss");
        String fileCnName = "资产上传模板" + date + ".xlsx";
        this.download(filePath, fileCnName, request, response);
    }

    @ApiOperation(value = "/download/errorFile", notes = "下载上传失败的文件")
    @GetMapping("/download/errorFile")
    @Record(module = PORTAL_MODULE_IP,operation = LOG_OPERATION_DOWNLOAD_ERROR_FILE)
    public void errorFile(HttpServletRequest request,
                          HttpServletResponse response, @RequestParam(value = "errorFilePath") String errorFilePath) throws Exception {
        String date = DateHelper.formatDate(new Date(), "yyyyMMddHHmmss");
        String fileCnName = "资产上传失败" + date + ".xlsx";
        ////这里其实导入到asset表中  襄阳公安专用
        this.download(errorFilePath, fileCnName, request, response);
    }


    @ApiOperation(value = "/addIpAll", notes = "新增终端地址信息")
    @RequestMapping(value = "/addIpAll", method = {RequestMethod.POST})
    @Record(module = PORTAL_MODULE_IP,operation = PORTAL_LOG_OPERATION_ADD)
    public ApiResult addIpAll(@Valid @RequestBody IpAllAddReqVO addReqVO,
                              HttpServletRequest request) {

        QzViewRespVO qzViewRespVO = ipService.findByIp(addReqVO.getIpAddr());
        if (qzViewRespVO != null) {
            return ApiResult.error("已存在此ip");
        }
        UserSession userSession = getUserSession(request);
        IpAll ip = new IpAll();
        BeanUtils.copyProperties(addReqVO, ip);
        ip.setLatitudeB(addReqVO.getLatitude());
        ip.setLongitudeB(addReqVO.getLongitude());
        /**
         * @description: 点位名称中存放 设备编号+点位名称
         * edit by ns_wang 2020/10/9 10:08
         */
        ip.setPointName(addReqVO.getDeviceModelDes() + " " + addReqVO.getPointName());
        ip.setContractorPerson(userSession.getNickName());
        ipService.saveSelective(ip);


        return ApiResult.success(MapBuilder.getSimpleMap("id", ip.getId()));
    }
}
