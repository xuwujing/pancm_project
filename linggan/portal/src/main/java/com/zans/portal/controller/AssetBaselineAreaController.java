package com.zans.portal.controller;

import com.zans.base.annotion.Record;
import com.zans.base.controller.BaseController;
import com.zans.base.vo.ApiResult;
import com.zans.base.vo.UserSession;
import com.zans.portal.dao.AssetBaselineAreaDao;
import com.zans.portal.model.AssetBaselineArea;
import com.zans.portal.model.AssetBranch;
import com.zans.portal.service.IAssetBaselineAreaService;
import com.zans.portal.service.IAssetBranchService;
import com.zans.portal.util.HttpHelper;
import com.zans.portal.vo.AssetBaselineAreaPageVO;
import com.zans.portal.vo.AssetBaselineAreaVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

import static com.zans.portal.constants.PortalConstants.*;

/**
 * create by: beiming
 * create time: 2021/11/15 17:20
 */
@Api(tags = "分组规则(AssetBaselineArea)")
@RestController
@RequestMapping("assetBaselineArea")
public class AssetBaselineAreaController extends BaseController {
    @Autowired
    IAssetBaselineAreaService assetBaselineAreaService;
    @Autowired
    IAssetBranchService assetBranchService;

    @Resource
    private AssetBaselineAreaDao assetBaselineAreaDao;

    @Autowired
    private HttpHelper httpHelper;

    /**
     * 新增一条数据
     *
     * @param vo 实体类
     * @return Response对象
     */
    @ApiOperation(value = "分组规则新增", notes = "分组规则新增")
    @RequestMapping(value = "insert", method = RequestMethod.POST)
    @Record(module = PORTAL_MODULE_BASELINE_AREA,operation = PORTAL_LOG_OPERATION_ADD)
    public ApiResult insert(@RequestBody AssetBaselineAreaVO vo, HttpServletRequest httpRequest) {
        UserSession userSession = httpHelper.getUser(httpRequest);

        String areaName = vo.getAreaName();
        AssetBaselineArea baseline = assetBaselineAreaService.getByName(areaName);
        if(baseline!=null){
            return ApiResult.error("该名称已存在!");
        }
        baseline = assetBaselineAreaService.getOnlyLevel();
        if(baseline!=null && vo.getLevel() == 1){
            return ApiResult.error("一级菜单只能有一个!");
        }

        AssetBaselineArea area = new AssetBaselineArea();
        BeanUtils.copyProperties(vo,area);
        area.setId(null);
        area.setParentId(vo.getParentId()==null?0:vo.getParentId());
        int result = assetBaselineAreaDao.insert(area);
        if (result > 0) {
            return ApiResult.success();
        }
        return ApiResult.error("新增失败");
    }

    /**
     * 修改一条数据
     *
     * @param vo 实体类
     * @return Response对象
     */
    @ApiOperation(value = "分组规则修改", notes = "分组规则修改")
    @RequestMapping(value = "edit", method = RequestMethod.POST)
    @Record(module = PORTAL_MODULE_BASELINE_AREA,operation = PORTAL_LOG_OPERATION_EDIT)
    public ApiResult update(@RequestBody AssetBaselineAreaVO vo, HttpServletRequest httpRequest) {
        UserSession userSession = httpHelper.getUser(httpRequest);
        AssetBaselineArea baselineArea = assetBaselineAreaService.getByName(vo.getAreaName());
        if (baselineArea != null && baselineArea.getId().intValue() != vo.getId().intValue()) {
            return ApiResult.error("已存在名称为:" + vo.getAreaName() + "的分组");
        }

        baselineArea = assetBaselineAreaService.getOnlyLevel();
        if(baselineArea!=null && vo.getLevel() == 1){
            return ApiResult.error("一级菜单只能有一个!");
        }


        AssetBaselineArea area = new AssetBaselineArea();
        BeanUtils.copyProperties(vo,area);
        area.setUpdateTime(null);
        area.setCreateTime(null);
        area.setId(vo.getId());
        assetBaselineAreaService.updateById(area);
        return ApiResult.success();
    }

    /**
     * 删除一条数据
     *
     * @param vo 参数对象
     * @return Response对象
     */
    @ApiOperation(value = "分组规则删除", notes = "分组规则删除")
    @RequestMapping(value = "del", method = RequestMethod.POST)
    @Record(module = PORTAL_MODULE_BASELINE_AREA,operation = PORTAL_LOG_OPERATION_ONE_DELETE)
    public ApiResult delete(@RequestBody AssetBaselineAreaVO vo, HttpServletRequest httpRequest) {
        UserSession userSession = httpHelper.getUser(httpRequest);
        List<AssetBranch> list = assetBranchService.getAssetBranchByAreaId(vo.getId());
        if (list != null && list.size()>0) {
            return ApiResult.error("该分组规则已生成分组不能删除"+list.get(0).getName());
        }
        assetBaselineAreaService.deleteById(vo.getId());
        return ApiResult.success();
    }


    /**
     * 分页查询
     */
    @ApiOperation(value = "分组规则查询", notes = "分组规则查询")
    @RequestMapping(value = "list", method = RequestMethod.POST)
    public ApiResult list(@RequestBody AssetBaselineAreaPageVO vo) {
        super.checkPageParams(vo, "");
        return assetBaselineAreaService.list(vo);
    }

    /**
     * 分组规则详情
     *  ,/assetBaselineArea/insert,/assetBaselineArea/edit,/assetBaselineArea/del,/assetBaselineArea/list,/assetBaselineArea/view
     * @return Response对象
     */
    @ApiOperation(value = "分组规则详情", notes = "分组规则详情")
    @RequestMapping(value = "view", method = RequestMethod.GET)
    public ApiResult delete(@RequestParam("id") Integer id) {
        AssetBaselineArea baselineArea = assetBaselineAreaService.getById(id);
        return ApiResult.success(baselineArea);
    }
    /**
     * 分页查询
     */
    @ApiOperation(value = "分组规则所有记录", notes = "分组规则所有记录")
    @RequestMapping(value = "listAll", method = RequestMethod.POST)
    public ApiResult listAll() {
        return assetBaselineAreaService.listAll();
    }

}
