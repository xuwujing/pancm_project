package com.zans.mms.controller.pc;

import com.zans.base.annotion.Record;
import com.zans.base.controller.BaseController;
import com.zans.base.util.HttpHelper;
import com.zans.base.vo.ApiResult;
import com.zans.base.vo.UserSession;
import com.zans.mms.config.MMSConstants;
import com.zans.mms.model.PatrolScheme;
import com.zans.mms.service.IPatrolSchemeService;
import com.zans.mms.vo.patrol.PatrolSchemeAddReqVO;
import com.zans.mms.vo.patrol.PatrolSchemeEditReqVO;
import com.zans.mms.vo.patrol.PatrolSchemeQueryVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.Date;

/**
 * @author
 * @version V1.0
 **/
@RestController
@RequestMapping("patrolScheme")
@Api(tags = "巡检计划管理")
@Validated
public class PatrolSchemeController extends BaseController {

    @Autowired
    private IPatrolSchemeService patrolSchemeService;
    @Autowired
    private HttpHelper httpHelper;

    /**
     *  获取所有数据
     *
     * @param vo  PatrolSchemeReqVO
     * @return     ApiResult
     */
     @ApiOperation(value = "获取巡检计划数据列表", notes = "获取巡检计划数据列表")
     @PostMapping(value = "/list",  produces = MediaType.APPLICATION_JSON_VALUE)
     public ApiResult getList(@Valid @RequestBody PatrolSchemeQueryVO vo) {
         super.checkPageParams(vo);
         return patrolSchemeService.getList(vo);
     }


     /**
     * 新增一条数据
     *
     * @param reqVO 实体类
     * @return Response对象
     */
     @ApiOperation(value = "新增", notes = "新增")
     @RequestMapping(value = "save", method = RequestMethod.POST)
     @Record(module = MMSConstants.MODULE_PATROL_SCHEME,operation = MMSConstants.LOG_OPERATION_SAVE)
     public ApiResult insert(@RequestBody PatrolSchemeAddReqVO reqVO, HttpServletRequest httpRequest) {
         UserSession userSession = httpHelper.getUser(httpRequest);
         PatrolScheme patrolScheme = new PatrolScheme();
         BeanUtils.copyProperties(reqVO,patrolScheme);
         patrolScheme.setCreator(userSession.getUserName());

         int result = patrolSchemeService.addSchemeAndJob(patrolScheme);
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
     @Record(module = MMSConstants.MODULE_PATROL_SCHEME,operation = MMSConstants.LOG_OPERATION_EDIT)
     public ApiResult  update(@RequestBody PatrolSchemeEditReqVO reqVO, HttpServletRequest httpRequest) {
         PatrolScheme patrolScheme = new PatrolScheme();
         BeanUtils.copyProperties(reqVO,patrolScheme);
         patrolScheme.setUpdateTime(new Date());
         patrolScheme.setId(reqVO.getId());

         patrolSchemeService.updateSchemeAndJob(patrolScheme);
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
     @Record(module = MMSConstants.MODULE_PATROL_SCHEME,operation = MMSConstants.LOG_OPERATION_DELETE)
     public ApiResult delete(Long id, HttpServletRequest httpRequest) {
//         Boolean b =  patrolSchemeService.existRelation(id);
//         if (b){
//             return ApiResult.error("存在业务关系，不能删除");
//         }

         patrolSchemeService.deleteSchemeAndJob(id);
         return ApiResult.success();
     }

    /**
     * 立即执行任务
     *
     * @param id 参数对象
     * @return Response对象
     */
    @ApiOperation(value = "立即执行任务", notes = "立即执行任务")
    @RequestMapping(value = "generateTask", method = RequestMethod.GET)
    @Record(module = MMSConstants.MODULE_PATROL_SCHEME,operation = MMSConstants.LOG_OPERATION_SUBMIT)
    public ApiResult generateTask(Long id, HttpServletRequest httpRequest) {
        patrolSchemeService.generateTaskBySchemeId(id);
        return ApiResult.success();
    }


}
