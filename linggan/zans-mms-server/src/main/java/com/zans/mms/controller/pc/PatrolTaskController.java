package com.zans.mms.controller.pc;

import com.zans.base.controller.BaseController;
import com.zans.base.util.DateHelper;
import com.zans.base.util.HttpHelper;
import com.zans.base.vo.ApiResult;
import com.zans.base.vo.UserSession;
import com.zans.mms.service.IPatrolTaskCheckResultService;
import com.zans.mms.service.IPatrolTaskService;
import com.zans.mms.vo.patrol.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.Date;
import java.util.List;

import static com.zans.base.util.DateHelper.yyyy_MM_dd;
import static com.zans.mms.config.PatrolConstants.PATROL_END_TIME_SUFFIX;
import static com.zans.mms.config.PatrolConstants.PATROL_START_TIME_SUFFIX;

/**
 * @author
 * @version V1.0
 **/
@RestController
@RequestMapping("patrolTask")
@Api(tags = "巡检任务管理")
@Validated
public class PatrolTaskController extends BaseController {

    @Autowired
    private IPatrolTaskService patrolTaskService;
    @Autowired
    private HttpHelper httpHelper;

    @Autowired
    private IPatrolTaskCheckResultService patrolTaskCheckResultService;

    /**
     *  获取所有数据
     *
     * @param vo  PatrolTaskReqVO
     * @return     ApiResult
     */
     @ApiOperation(value = "获取巡检任务数据列表", notes = "获取巡检任务数据列表")
     @PostMapping(value = "/list",  produces = MediaType.APPLICATION_JSON_VALUE)
     public ApiResult getList(@Valid @RequestBody PatrolTaskQueryVO vo, HttpServletRequest httpRequest) {
         UserSession userSession = httpHelper.getUser(httpRequest);
         super.checkPageParams(vo);
         if ((StringUtils.isBlank(vo.getStartDate())&& StringUtils.isBlank(vo.getEndDate()))) {
             vo.setPatrolDate(DateHelper.formatDate(new Date(),yyyy_MM_dd));
         }
         if((StringUtils.isNotBlank(vo.getStartDate())&&StringUtils.isNotBlank(vo.getEndDate()))&&(vo.getStartDate().equals(vo.getEndDate()))){
             vo.setPatrolDate(vo.getStartDate());
             vo.setStartDate("");
             vo.setEndDate("");
         }
         return patrolTaskService.getList(vo,userSession);
     }

    /**
     *  获取信息
     *
     * @param
     * @return     ApiResult
     */
     @ApiOperation(value = "查询巡检任务指定数据", notes = "查询巡检任务指定数据")
     @PostMapping(value = "/view",  produces = MediaType.APPLICATION_JSON_VALUE)
     public ApiResult view(@Valid @RequestBody PatrolTaskDetailQueryVO vo) {
         super.checkPageParams(vo);
        return patrolTaskService.getView(vo);
     }


    /**
     *  获取所有数据
     *
     * @param vo  PatrolTaskReqVO
     * @return     ApiResult
     */
    @ApiOperation(value = "获取巡检日志数据列表", notes = "获取巡检日志数据列表")
    @PostMapping(value = "/getPatrolLogList",  produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiResult getPatrolLogList(@Valid @RequestBody PatrolLogQueryVO vo, HttpServletRequest httpRequest) {
        UserSession userSession = httpHelper.getUser(httpRequest);
        super.checkPageParams(vo);
        if (StringUtils.isBlank(vo.getStartDate()) || StringUtils.isBlank(vo.getEndDate())) {
            vo.setStartDate(DateHelper.formatDate(new Date(),yyyy_MM_dd)+PATROL_START_TIME_SUFFIX);
            vo.setEndDate(DateHelper.formatDate(new Date(),yyyy_MM_dd)+ PATROL_END_TIME_SUFFIX);
        } else {
            vo.setStartDate(vo.getStartDate()+ PATROL_START_TIME_SUFFIX);
            vo.setEndDate(vo.getEndDate()+ PATROL_END_TIME_SUFFIX);
        }
        return patrolTaskService.getPatrolLogList(vo,userSession);
    }

    @ApiOperation(value = "查询全部点位", notes = "查询全部点位")
    @RequestMapping(value = "searchAll", method = RequestMethod.POST)
    public ApiResult searchAll(HttpServletRequest req, @RequestBody PatrolDevicePointReqVO patrolDevicePointReqVO) {
        UserSession userSession = httpHelper.getUser(req);
        //pc巡检单位取界面传参
///        patrolDevicePointReqVO.setOrgId(userSession.getOrgId());
        return patrolTaskCheckResultService.pcSearchAllDevicePoint(patrolDevicePointReqVO,userSession);
    }

    @ApiOperation(value = "查询巡检点位byId", notes = "查询巡检点位byId")
    @RequestMapping(value = "searchPatrolByPointId", method = RequestMethod.POST)
    public ApiResult searchPatrolByPointId(HttpServletRequest req, @RequestBody PatrolDevicePointReqVO patrolDevicePointReqVO) {
        UserSession userSession = httpHelper.getUser(req);
        //查询单个点位，可以查询已巡检 和未巡检的
        patrolDevicePointReqVO.setContainCompleted(null);
        List<PatrolDevicePointResVO> patrolDevicePointVO =
                patrolTaskCheckResultService.pcSearchDevicePointByPointId(patrolDevicePointReqVO,userSession);
        if (patrolDevicePointVO != null && patrolDevicePointVO.size()>0){
            return ApiResult.success(patrolDevicePointVO.get(0));
        }
        return ApiResult.error("巡检点位不存在或已被打卡!");
    }


    /******************************* 巡检任务删除开始 **********************/
    /**
     * 需求日期：2021/10/15
     * 需求来源：项目组内部提出
     * 需求原因：点位经纬度未校准后直接进行了巡检，大部分点位数据经纬度不准确，交管局方面明确提出需要停止本次巡检，
     * 即应删除本次的巡检任务及巡检的点位
     * 整体逻辑：删除巡检任务及其任务下的所有点位
     */
    @ApiOperation(value = "删除一个巡检任务", notes = "删除一个巡检任务")
    @PostMapping(value = "delete")
    public ApiResult delete(@RequestBody PatrolLogQueryVO vo) {
        if(null==vo.getId()){
            return ApiResult.error("传参id为空！");
        }
        return patrolTaskService.deleteTask(vo.getId());
    }






    /********************************巡检任务删除结束*******************************/
}
