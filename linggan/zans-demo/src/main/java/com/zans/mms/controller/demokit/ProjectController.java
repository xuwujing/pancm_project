package com.zans.mms.controller.demokit;

import com.alibaba.fastjson.JSONObject;
import com.zans.base.annotion.Record;
import com.zans.base.controller.BaseController;
import com.zans.base.vo.ApiResult;
import com.zans.base.vo.UserSession;
import com.zans.mms.config.MMSConstants;
import com.zans.mms.model.DevicePoint;
import com.zans.mms.model.SaleProject;
import com.zans.mms.model.SysUser;
import com.zans.mms.service.ISaleProjectService;
import com.zans.mms.service.ISysUserService;
import com.zans.mms.vo.saleproject.SaleProjectEditReqVO;
import com.zans.mms.vo.saleproject.SaleProjectQueryVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;


import static com.zans.mms.config.DemoKitConstants.DEFAULT_PROJECT_ID;

/**
* @Title: ProjectController
* @Description: 项目管理
* @Version:1.0.0
* @Since:jdk1.8
* @author beiming
* @date 5/20/21
*/
@Api(value = "/项目管理", tags = {"/项目管理"})
@RestController
@Validated
@Slf4j
@RequestMapping("demoKit")
public class ProjectController extends BaseController {
    @Autowired
    ISaleProjectService saleProjectService;

    @Autowired
    ISysUserService sysUserService;
    /**
     * 获取所有数据
     *
     * @param vo DevicePointReqVo
     * @return ApiResult<PageInfo < DevicePointResVo>>
     */
    @ApiOperation(value = "获取数据列表", notes = "获取数据列表")
    @PostMapping(value = "/project/list", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiResult getList(@Valid @RequestBody SaleProjectQueryVO vo) {
        super.checkPageParams(vo);
        return saleProjectService.getList(vo);
    }
    /**
     * 新增一条数据
     *
     * @param reqVO 实体类
     * @return Response对象
     */
    @ApiOperation(value = "新增一条数据", notes = "新增一条数据")
    @RequestMapping(value = "/project/save", method = RequestMethod.POST)
    @Record(module = MMSConstants.MODULE_PROJECT,operation = MMSConstants.LOG_OPERATION_SAVE)
    public ApiResult<SaleProject> insert(@RequestBody SaleProjectEditReqVO reqVO, HttpServletRequest httpRequest) {
        SaleProject sale = saleProjectService.getByProjectId(reqVO.getProjectId());
        if (null != sale){
            return ApiResult.error("已存在的项目编号");
        }

        UserSession userSession = httpHelper.getUser(httpRequest);
        SaleProject saleProject = new SaleProject();
        BeanUtils.copyProperties(reqVO,saleProject);
        saleProject.setCreator(userSession.getUserName());

        int result = saleProjectService.saveSelective(saleProject);
        if (result > 0) {
            return ApiResult.success("success");
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
    @RequestMapping(value = "/project/edit", method = RequestMethod.POST)
    @Record(module = MMSConstants.MODULE_PROJECT,operation = MMSConstants.LOG_OPERATION_EDIT)
    public ApiResult<DevicePoint> update(@RequestBody SaleProjectEditReqVO reqVO, HttpServletRequest httpRequest) {
        SaleProject sale = saleProjectService.getById(reqVO.getId());
        if (null == sale){
            return ApiResult.error("不存在此记录");
        }
        if (DEFAULT_PROJECT_ID.equals(sale.getProjectId())){
            return ApiResult.error("内置项目不允许修改");
        }

        SaleProject saleProject = new SaleProject();
        BeanUtils.copyProperties(reqVO,saleProject);
        saleProjectService.updateSelective(saleProject);
        return ApiResult.success();
    }

    /**
     * 删除一条数据
     *
     * @param id 参数对象
     * @return Response对象
     */
    @ApiOperation(value = "删除", notes = "删除")
    @RequestMapping(value = "/project/del", method = RequestMethod.GET)
    @Record(module = MMSConstants.MODULE_PROJECT,operation = MMSConstants.LOG_OPERATION_DELETE)
    public ApiResult delete(Long id, HttpServletRequest httpRequest) {
        SaleProject saleProject = saleProjectService.getById(id);
        if (null == saleProject){
            return ApiResult.error("不存在此记录");
        }
        if (DEFAULT_PROJECT_ID.equals(saleProject.getProjectId())){
            return ApiResult.error("内置项目不允许删除");
        }
        saleProjectService.deleteById(id);
        return ApiResult.success();
    }


    /**
     *
     * 切换项目编号      * @param projectId 参数对象
     * @return Response对象
     */
    @ApiOperation(value = "切换项目编号", notes = "切换项目编号")
    @RequestMapping(value = "/project/changeProjectId", method = RequestMethod.GET)
    @Record(module = MMSConstants.MODULE_PROJECT,operation = MMSConstants.LOG_OPERATION_CHANGE_PROJECT_ID)
    public ApiResult changeProjectId(@RequestParam("projectId") String projectId, HttpServletRequest httpRequest) {
        SaleProject saleProject = saleProjectService.getByProjectId(projectId);
        if (null == saleProject) {
            return ApiResult.error("不存在此记录");
        }
        UserSession userSession = httpHelper.getUser(httpRequest);
        sysUserService.updateProjectId(userSession.getUserId(),projectId);
        saleProjectService.changeProjectId(userSession.getUserId(),projectId);
        return ApiResult.success();
    }

}
