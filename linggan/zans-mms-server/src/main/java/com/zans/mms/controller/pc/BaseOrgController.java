package com.zans.mms.controller.pc;

import com.zans.base.annotion.Record;
import com.zans.base.util.HttpHelper;
import com.zans.base.vo.ApiResult;
import com.zans.base.vo.UserSession;
import com.zans.mms.config.MMSConstants;
import com.zans.mms.model.BaseOrg;
import com.zans.mms.service.IBaseOrgService;
import com.zans.mms.vo.baseorg.BaseOrgRepVO;
import com.zans.mms.vo.baseorg.BaseOrgReqVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;


/**
 * 单位维护 base_org表(BaseOrg)表控制层
 *
 * @author beixing
 * @since 2021-03-24 15:50:02
 */
@Api(tags = "单位维护")
@RestController
@RequestMapping("baseOrg")
public class BaseOrgController {
    /**
     * 服务对象
     */
    @Autowired
    private IBaseOrgService baseOrgService;

    @Autowired
    private HttpHelper httpHelper;

    /**
     * 新增一条数据
     *
     * @param baseOrgReqVO 实体类
     * @return Response对象
     */
    @ApiOperation(value = "单位维护新增", notes = "单位维护新增")
    @RequestMapping(value = "save", method = RequestMethod.POST)
    @Record(module = MMSConstants.MODULE_SYSTEM_ORG,operation = MMSConstants.LOG_OPERATION_SAVE)
    public ApiResult<BaseOrg> insert(@RequestBody BaseOrgReqVO baseOrgReqVO, HttpServletRequest httpRequest) {
        UserSession userSession = httpHelper.getUser(httpRequest);
        baseOrgReqVO.setCreator(userSession.getUserName());
        BaseOrgRepVO baseOrgRepVO = baseOrgService.queryByOrgId(baseOrgReqVO.getOrgId());
        if(baseOrgRepVO!=null){
            return ApiResult.error("该组织编码已存在！");
        }
        BaseOrg baseOrg = new BaseOrg();
        BeanUtils.copyProperties(baseOrgReqVO,baseOrg);
         baseOrgService.saveSelective(baseOrg);
        return ApiResult.success(baseOrg);
    }

    /**
     * 修改一条数据
     *
     * @param
     * @return Response对象
     */
    @ApiOperation(value = "单位维护修改", notes = "单位维护修改")
    @RequestMapping(value = "edit", method = RequestMethod.POST)
    @Record(module = MMSConstants.MODULE_SYSTEM_ORG,operation = MMSConstants.LOG_OPERATION_EDIT)
    public ApiResult<BaseOrg> update(@RequestBody BaseOrgReqVO baseOrgReqVO, HttpServletRequest httpRequest) {
        BaseOrg baseOrg = new BaseOrg();
        BeanUtils.copyProperties(baseOrgReqVO,baseOrg);
        baseOrgService.updateSelective(baseOrg);
        return ApiResult.success();
    }

    /**
     * 删除一条数据
     *
     * @param
     * @return Response对象
     */
    @ApiOperation(value = "单位维护删除", notes = "单位维护删除")
    @RequestMapping(value = "del", method = RequestMethod.POST)
    @Record(module = MMSConstants.MODULE_SYSTEM_ORG,operation = MMSConstants.LOG_OPERATION_DELETE)
    public ApiResult<BaseOrg> delete(@RequestBody BaseOrgReqVO baseOrgReqVO, HttpServletRequest httpRequest) {
        BaseOrg baseOrg = new BaseOrg();
        BeanUtils.copyProperties(baseOrgReqVO,baseOrg);
        baseOrgService.delete(baseOrg);
        return ApiResult.success();
    }


    /**
     * 分页查询
     */
    @ApiOperation(value = "单位维护查询", notes = "单位维护查询")
    @RequestMapping(value = "list", method = RequestMethod.POST)
    public ApiResult<BaseOrg> list(@RequestBody BaseOrgReqVO baseOrg) {
        return baseOrgService.queryList(baseOrg);
    }

}
