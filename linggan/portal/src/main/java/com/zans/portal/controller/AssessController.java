package com.zans.portal.controller;

import com.zans.base.annotion.Record;
import com.zans.base.config.EnumErrorCode;
import com.zans.base.office.excel.ExportConfig;
import com.zans.base.util.DateHelper;
import com.zans.base.util.StringHelper;
import com.zans.base.vo.ApiResult;
import com.zans.base.vo.PageResult;
import com.zans.base.vo.UserSession;
import com.zans.portal.model.AssessRecord;
import com.zans.portal.service.*;
import com.zans.portal.vo.asset.req.AssetAssessSearchVO;
import com.zans.portal.vo.asset.resp.AssetAssessResExportVO;
import com.zans.portal.vo.asset.resp.AssetAssessResVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

import static com.zans.portal.constants.PortalConstants.PORTAL_LOG_OPERATION_EXPORT;
import static com.zans.portal.constants.PortalConstants.PORTAL_MODULE_ALERT;

@Api(value = "/assess", tags = {"/~考核"})
@RestController
@RequestMapping("/assess")
@Slf4j
@Validated
public class AssessController extends BasePortalController {

    @Autowired
    ISwitcherService switchService;
    @Autowired
    ISwitcherBrunchService switcherBrunchService;
    @Autowired
    IAssetService assetService;
    @Autowired
    IAreaService areaService;
    @Autowired
    IRegionService regionService;
    @Autowired
    IConstantItemService constantItemService;

    @Autowired
    ILogOperationService logOperationService;

    @Autowired
    private IAssessService assessService;

    @Autowired
    IFileService fileService;

    @Value("${api.export.folder}")
    String exportFolder;
    @Value("${api.upload.folder}")
    String uploadFolder;


    @ApiOperation(value = "/list", notes = "考核查询")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "req", value = "查询条件", required = true,
                    dataType = "SwitchAssessSearchVO", paramType = "body")
    })
    @RequestMapping(value = "/list", method = {RequestMethod.POST})
    @ResponseBody
    public ApiResult getList(@RequestBody AssetAssessSearchVO req) {
        String sortName = req.getSortName();
        if (StringUtils.isEmpty(sortName)) {
            sortName = " id desc ";
        } else {
            sortName = sortName + " ,id desc ";
        }
        super.checkPageParams(req, sortName);
        return assessService.list(req);
    }


    @ApiOperation(value = "/view", notes = "详情")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "req", value = "查询条件", required = true,
                    dataType = "AssetAssessSearchVO", paramType = "body")
    })
    @RequestMapping(value = "/view", method = {RequestMethod.POST})
    public ApiResult view(@RequestBody AssetAssessSearchVO req) {
        return assessService.view(req);
    }


    @ApiOperation(value = "/chart/view", notes = "图表详情")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "req", value = "查询条件", required = true,
                    dataType = "AssetAssessSearchVO", paramType = "body")
    })
    @RequestMapping(value = "/chart/view", method = {RequestMethod.POST})
    public ApiResult chartView(@RequestBody AssetAssessSearchVO req) {
        return assessService.chartView(req);
    }


    @ApiOperation(value = "/state/apply", notes = "申诉提交")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "req", value = "申诉提交", required = true,
                    dataType = "AssessRecord", paramType = "body")
    })
    @RequestMapping(value = "/state/apply", method = {RequestMethod.POST})
    public ApiResult stateApply(@RequestBody AssessRecord req, HttpServletRequest request) {
        UserSession session = super.getUserSession(request);
        req.setStateUser(session.getNickName());
        return assessService.stateApply(req);
    }



    @ApiOperation(value = "/state/view", notes = "申诉详情")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "req", value = "查询条件", required = true,
                    dataType = "AssessRecord", paramType = "body")
    })
    @RequestMapping(value = "/state/view", method = {RequestMethod.POST})
    public ApiResult stateView(@RequestBody AssessRecord req) {
        return assessService.stateView(req);
    }

    @ApiOperation(value = "/approve", notes = "审批")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "req", value = "查询条件", required = true,
                    dataType = "AssessRecord", paramType = "body")
    })
    @RequestMapping(value = "/approve", method = {RequestMethod.POST})
    public ApiResult approve(@RequestBody AssessRecord req, HttpServletRequest request) {
        UserSession session = super.getUserSession(request);
        req.setApproveUser(session.getNickName());
        return assessService.approve(req);
    }

    @ApiOperation(value = "/export", notes = "考核导出")
    @RequestMapping(value = "/export", method = {RequestMethod.POST}, headers = "Accept=application/octet-stream")
    @Record(module = PORTAL_MODULE_ALERT,operation = PORTAL_LOG_OPERATION_EXPORT)
    public void export(@RequestBody AssetAssessSearchVO req , HttpServletRequest request, HttpServletResponse response) throws Exception {
        log.info("alert downloadFile #===========");
        String filePath = this.exportFolder + "考核导出_" + DateHelper.getTodayShort() + "_" + StringHelper.getUuid() + ".xlsx";
        String name = "考核导出";
        String fileCnName = name + "_" + DateHelper.getTodayShort() + ".xlsx";
        req.setPageSize(10000);
        PageResult<AssetAssessResVO> pageResult = assessService.getAssessExport(req);
        if (pageResult.getList().size()==0){
            this.setErrorToResponse(request, response, EnumErrorCode.SERVER_DOWNLOAD_NULL_ERROR, EnumErrorCode.SERVER_DOWNLOAD_NULL_ERROR.getMessage());
            return;
        }
        List<AssetAssessResExportVO> list = new ArrayList<>();
        for (AssetAssessResVO respVO : pageResult.getList()) {
            AssetAssessResExportVO exportVO = new AssetAssessResExportVO();
            BeanUtils.copyProperties(respVO,exportVO);
            list.add(exportVO);
        }
        ExportConfig exportConfig = ExportConfig.builder()
                .seqColumn(true)
                .wrap(true)
                .build();
        fileService.exportExcelFile(list, name, filePath, exportConfig);
        this.download(filePath, fileCnName, request, response);
    }

}
