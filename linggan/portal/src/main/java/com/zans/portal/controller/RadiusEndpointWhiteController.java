package com.zans.portal.controller;

import com.zans.base.annotion.Record;
import com.zans.base.vo.ApiResult;
import com.zans.portal.service.IRadiusEndpointWhiteService;
import com.zans.portal.util.HttpHelper;
import com.zans.portal.vo.RadiusEndpointWhiteVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;


/**
 * @author beixing
 * @Title: 设备白名单表(RadiusEndpointWhite)表控制层
 * @Description:
 * @Version:1.0.0
 * @Since:jdk1.8
 * @date 2022-02-16 18:22:03
 */
@Api(tags = "设备白名单表(RadiusEndpointWhite)")
@RestController
@RequestMapping("radiusEndpointWhite")
public class RadiusEndpointWhiteController {
    /**
     * 服务对象
     */
    @Autowired
    private IRadiusEndpointWhiteService radiusEndpointWhiteService;

    @Autowired
    private HttpHelper httpHelper;



    /**
     * 删除一条数据
     *
     * @param radiusEndpointWhiteVO 参数对象
     * @return Response对象
     */
    @ApiOperation(value = "设备白名单表移除", notes = "设备白名单表移除")
    @RequestMapping(value = "remove", method = RequestMethod.POST)
    @Record
    public ApiResult remove(@RequestBody RadiusEndpointWhiteVO radiusEndpointWhiteVO, HttpServletRequest httpRequest) {
        return  radiusEndpointWhiteService.remove(radiusEndpointWhiteVO);
    }


    /**
     * 刷新
     *
     * @param radiusEndpointWhiteVO 参数对象
     * @return Response对象
     */
    @ApiOperation(value = "设备白名单表刷新", notes = "设备白名单表刷新")
    @RequestMapping(value = "refresh", method = RequestMethod.POST)
    @Record
    public ApiResult refresh(@RequestBody RadiusEndpointWhiteVO radiusEndpointWhiteVO, HttpServletRequest httpRequest) {
        return  radiusEndpointWhiteService.refresh(radiusEndpointWhiteVO);
    }


    /**
     * 分页查询
     */
    @ApiOperation(value = "设备白名单表查询", notes = "设备白名单表查询")
    @RequestMapping(value = "list", method = RequestMethod.POST)
    public ApiResult list(@RequestBody RadiusEndpointWhiteVO radiusEndpointWhiteVO) {
        return radiusEndpointWhiteService.list(radiusEndpointWhiteVO);
    }

    /**
     * 详情查询
     */
    @ApiOperation(value = "设备白名单表详情", notes = "设备白名单表详情")
    @RequestMapping(value = "view", method = RequestMethod.GET)
    public ApiResult view(@RequestParam("id") Long id) {
        return ApiResult.success(radiusEndpointWhiteService.queryById(id));
    }
}
