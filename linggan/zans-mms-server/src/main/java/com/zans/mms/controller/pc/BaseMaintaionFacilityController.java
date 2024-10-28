package com.zans.mms.controller.pc;

import com.zans.base.controller.BaseController;
import com.zans.base.util.HttpHelper;
import com.zans.base.vo.ApiResult;
import com.zans.mms.service.IBaseMaintaionFacilityService;
import com.zans.mms.vo.basemaintaionfacility.BaseMaintaionFacilityQueryVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * @author
 * @version V1.0
 **/
@RestController
@RequestMapping("baseMaintaionFacility")
@Api(tags = "价格维护管理")
@Validated
public class BaseMaintaionFacilityController extends BaseController {

    @Autowired
    private IBaseMaintaionFacilityService baseMaintaionFacilityService;
    @Autowired
    private HttpHelper httpHelper;

    /**
     *  获取所有数据
     *
     * @param vo  BaseMaintaionFacilityReqVO
     * @return     ApiResult
     */
     @ApiOperation(value = "获取数据列表", notes = "获取数据列表")
     @PostMapping(value = "/list",  produces = MediaType.APPLICATION_JSON_VALUE)
     public ApiResult getList(@Valid @RequestBody BaseMaintaionFacilityQueryVO vo) {
         super.checkPageParams(vo);
         return baseMaintaionFacilityService.getList(vo);
     }




//    /**
//     *  获取信息
//     *
//     * @param id    主键id
//     * @return     ApiResult<BaseMaintaionFacilityResVO>
//     */
//     @ApiOperation(value = "查询指定数据", notes = "查询指定数据")
//     @GetMapping(value = "/view",  produces = MediaType.APPLICATION_JSON_VALUE)
//     public ApiResult<BaseMaintaionFacilityResVO> view(String id) {
//        BaseMaintaionFacilityResVO viewResp = baseMaintaionFacilityService.getViewByUniqueId(id);
//        if (null == viewResp) {
//           return  ApiResult.error("不存在此记录");
//        }
//        return ApiResult.success(viewResp);
//     }
//
//     /**
//     * 新增一条数据
//     *
//     * @param reqVO 实体类
//     * @return Response对象
//     */
//     @ApiOperation(value = "新增", notes = "新增")
//     @RequestMapping(value = "save", method = RequestMethod.POST)
//     @Record
//     public ApiResult<BaseMaintaionFacility> insert(@RequestBody BaseMaintaionFacilityAddReqVO reqVO, HttpServletRequest httpRequest) {
//         UserSession userSession = httpHelper.getUser(httpRequest);
//         BaseMaintaionFacility baseMaintaionFacility = new BaseMaintaionFacility();
//         BeanUtils.copyProperties(reqVO,baseMaintaionFacility);
//         baseMaintaionFacility.setCreator(userSession.getUserName());
//         int result = baseMaintaionFacilityService.saveSelective(baseMaintaionFacility);
//         if (result > 0) {
//             return ApiResult.success();
//         }
//         return ApiResult.error("新增失败");
//     }
//
//     /**
//      * 修改一条数据
//      *
//      * @param reqVO 实体类
//      * @return Response对象
//      */
//     @ApiOperation(value = "修改", notes = "修改")
//     @RequestMapping(value = "edit", method = RequestMethod.POST)
//     @Record
//     public ApiResult<BaseMaintaionFacility> update(@RequestBody BaseMaintaionFacilityEditReqVO reqVO, HttpServletRequest httpRequest) {
//         BaseMaintaionFacility baseMaintaionFacility = new BaseMaintaionFacility();
//         BeanUtils.copyProperties(reqVO,baseMaintaionFacility);
//         baseMaintaionFacility.setUpdateTime(new Date());
//         Integer id =  baseMaintaionFacilityService.getIdByUniqueId(reqVO.getBaseMaintaionFacilityId());
//         baseMaintaionFacility.setId(id);
//         baseMaintaionFacilityService.updateSelective(baseMaintaionFacility);
//         return ApiResult.success();
//     }
//
//
//     /**
//     * 删除一条数据
//     *
//     * @param id 参数对象
//     * @return Response对象
//     */
//     @ApiOperation(value = "删除", notes = "删除")
//     @RequestMapping(value = "del", method = RequestMethod.GET)
//     @Record
//     public ApiResult delete(String id, HttpServletRequest httpRequest) {
//         Boolean b =  baseMaintaionFacilityService.existRelation(id);
//         if (b){
//             return ApiResult.error("存在业务关系，不能删除");
//         }
//         baseMaintaionFacilityService.deleteByUniqueId(id);
//         return ApiResult.success();
//     }



}
