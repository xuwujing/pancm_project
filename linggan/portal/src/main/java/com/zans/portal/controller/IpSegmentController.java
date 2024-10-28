package com.zans.portal.controller;

import com.zans.base.annotion.Record;
import com.zans.base.util.IpHelper;
import com.zans.base.util.MapBuilder;
import com.zans.base.vo.ApiResult;
import com.zans.base.vo.PageResult;
import com.zans.base.vo.SelectVO;
import com.zans.base.vo.UserSession;
import com.zans.portal.model.SysIpSegment;
import com.zans.portal.service.*;
import com.zans.portal.vo.segment.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.zans.base.config.EnumErrorCode.CLIENT_PARAMS_ERROR;
import static com.zans.portal.config.GlobalConstants.INIT_DATA_TABLE;
import static com.zans.portal.config.GlobalConstants.MODULE_ENABLE_STATUS;
import static com.zans.portal.constants.PortalConstants.*;

/**
 * @author xv
 * @since 2020/6/9 19:34
 */
@Api(value = "/segment", tags = {"/segment ~ IP地址段管理"})
@RestController
@RequestMapping("/segment")
@Validated
@Slf4j
public class IpSegmentController extends BasePortalController {

    @Autowired
    ISysIpSegmentService ipSegmentService;

    @Autowired
    IConstantItemService constantItemService;

    @Autowired
    IAreaService areaService;

    @Autowired
    ILogOperationService logOperationService;

    @Autowired
    INetworkIpSegmentService networkIpSegmentService;



    @ApiOperation(value = "/list", notes = "IP地址段查询")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "req", value = "查询条件", required = true,
                    dataType = "IpAllocSearchVO", paramType = "body")
    })
    @RequestMapping(value = "/list", method = {RequestMethod.POST})
    public ApiResult<PageResult<SegmentVO>> findAllIp(@RequestBody SegmentSearchVO req) {
        PageResult<SegmentVO> pageResult = ipSegmentService.getIpSegmentPage(req);
        return ApiResult.success(pageResult);
    }

    @ApiOperation(value = "/init", notes = "IP地址段查询初始化")
    @RequestMapping(value = "/init", method = {RequestMethod.GET})
    @ResponseBody
    public ApiResult init() {

        List<SelectVO> enableList = constantItemService.findItemsByDict(MODULE_ENABLE_STATUS);

        SegmentSearchVO req = new SegmentSearchVO();
        PageResult<SegmentVO> pageResult = ipSegmentService.getIpSegmentPage(req);
        Map<String, Object> result = MapBuilder.getBuilder()
                .put(MODULE_ENABLE_STATUS, enableList)
                .put(INIT_DATA_TABLE, pageResult)
                .build();
        return ApiResult.success(result);
    }

    @ApiOperation(value = "/view", notes = "获取IP地址段详细信息，by id")
    @ApiImplicitParam(name = "id", value = "地址id", required = true,
            dataType = "int", paramType = "query")
    @RequestMapping(value = "/view", method = {RequestMethod.GET})
    public ApiResult<SegmentViewVO> getIpById(@NotNull(message = "id必填") Integer id) {
        SegmentVO ip = ipSegmentService.getIpSegment(id);
        if (ip == null) {
            return ApiResult.error(CLIENT_PARAMS_ERROR).appendMessage("IP地址段ID不存在#" + id);
        }
        Map<String, Object> map = MapBuilder.getBuilder()
                .put("segment", ip).build();

        return ApiResult.success(map);
    }

    @ApiOperation(value = "/add", notes = "新增IP地址段")
    @ApiImplicitParam(name = "addVO", value = "IP地址段", required = true,
            dataType = "SegmentAddVO", paramType = "body")
    @RequestMapping(value = "/add", method = {RequestMethod.POST})
    @Record(module = PORTAL_MODULE_IP_SEGMENT,operation = PORTAL_LOG_OPERATION_ADD)
    public ApiResult addIpSegment(@Valid @RequestBody SegmentAddVO addVO,
                                  HttpServletRequest request) {
        try {
            String segmentShrot = "";
            List<String> ipArray = new ArrayList<>();
            for (int i = 0; i < addVO.getSegment().length; i++) {
                List<String> array = IpHelper.getIpByCategory(addVO.getSegment()[i]);
                ipArray.addAll(array);
                segmentShrot = segmentShrot + addVO.getSegment()[i] + ",";
            }
            addVO.setIpCount(ipArray.size());
            addVO.setSegmentShort(segmentShrot.substring(0, segmentShrot.length() - 1));
        } catch (Exception e) {
            return ApiResult.error("ip解析异常");
        }
        /*String[] segments = (String[]) ipArray.toArray(new String[0]);
        addVO.setSegment(segments);*/
        UserSession session = super.getUserSession(request);
        SysIpSegment segmentEntity = ipSegmentService.addSegment(addVO, session);
        return ApiResult.success(MapBuilder.getSimpleMap("id", segmentEntity.getId())).appendMessage("添加成功");
    }

    @ApiOperation(value = "/edit", notes = "修改IP地址段")
    @ApiImplicitParam(name = "editVO", value = "IP地址段", required = true,
            dataType = "SegmentEditVO", paramType = "body")
    @RequestMapping(value = "/edit", method = {RequestMethod.POST})
    @Record(module = PORTAL_MODULE_IP_SEGMENT,operation = PORTAL_LOG_OPERATION_EDIT)
    public ApiResult editIp(@Valid @RequestBody SegmentEditVO editVO,
                            HttpServletRequest request) {
        Integer id = editVO.getId();
        SysIpSegment item = ipSegmentService.getById(id);
        if (item == null) {
            return ApiResult.error(CLIENT_PARAMS_ERROR).appendMessage("ID不存在#" + id);
        }
        try {
            String segmentShrot = "";
            List<String> ipArray = new ArrayList<>();
            for (int i = 0; i < editVO.getSegment().length; i++) {
                List<String> array = IpHelper.getIpByCategory(editVO.getSegment()[i]);
                ipArray.addAll(array);
                segmentShrot = segmentShrot + editVO.getSegment()[i] + ",";
            }
            editVO.setSegmentShort(segmentShrot.substring(0, segmentShrot.length() - 1));
            editVO.setIpCount(ipArray.size());
        } catch (Exception e) {
            return ApiResult.error("ip解析异常");
        }
        ipSegmentService.updateSegment(editVO, getUserSession(request));

        return ApiResult.success(MapBuilder.getSimpleMap("id", id)).appendMessage("修改成功");
    }

    @ApiOperation(value = "/delete", notes = "删除IP地址段")
    @RequestMapping(value = "/delete", method = {RequestMethod.POST})
    @Record(module = PORTAL_MODULE_IP_SEGMENT,operation = PORTAL_LOG_OPERATION_ONE_DELETE)
    public ApiResult delete(@NotNull(message = "id") Integer id,
                            HttpServletRequest request) {
        SysIpSegment item = ipSegmentService.getById(id);
        if (item == null) {
            return ApiResult.error(CLIENT_PARAMS_ERROR).appendMessage("ID不存在#" + id);
        }

        this.ipSegmentService.deleteSegment(item, getUserSession(request));
        return ApiResult.success(MapBuilder.getSimpleMap("id", id)).appendMessage("删除成功");
    }

    @ApiOperation(value = "/ipSegment/list", notes = "IP地址段查询")
    @RequestMapping(value = "/ipSegment/list", method = {RequestMethod.GET})
    public ApiResult ipSegmentList() {
       return networkIpSegmentService.ipSegmentList();
    }

    @ApiOperation(value = "/ipSegment/view", notes = "IP地址段查询详情")
    @RequestMapping(value = "/ipSegment/view", method = {RequestMethod.GET})
    public ApiResult ipSegmentView(@RequestParam("ipAddr")String ipAddr) {
        return networkIpSegmentService.ipSegmentView(ipAddr);
    }

}
