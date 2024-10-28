package com.zans.mms.controller.pc;

import com.zans.base.controller.BaseController;
import com.zans.base.util.MapBuilder;
import com.zans.base.vo.ApiResult;
import com.zans.base.vo.UserSession;
import com.zans.mms.dao.SysJurisdictionDao;
import com.zans.mms.model.SysJurisdiction;
import com.zans.mms.service.IPermissionService;
import com.zans.mms.service.ISysJurisdictionService;
import com.zans.mms.vo.jurisdiction.SysJurisdictionReqVO;
import com.zans.mms.vo.role.RoleRespVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.NotNull;

import static com.zans.base.config.EnumErrorCode.CLIENT_PARAMS_ERROR;

/**
 * @author qitian
 * @Title: zans-mms-server
 * @Description:
 * @Version:1.0.0
 * @Since:jdk1.8
 * @date 2021/10/18
 */
@Api(tags = "/权限管理管理")
@RestController
@RequestMapping("sysJur")
@Slf4j
public class SysJurisdictionController extends BaseController {

	@Autowired
	private ISysJurisdictionService sysJurisdictionService;

	@Autowired
	IPermissionService permissionService;


	/**
	 * 列表查询
	 * @param vo
	 * @return
	 */
	@PostMapping("list")
	public ApiResult list(@RequestBody SysJurisdictionReqVO vo){
		super.checkPageParams(vo);
		return sysJurisdictionService.list(vo);
	}

	/**
	 * 新增权限组
	 * @param sysJurisdiction 权限组实体
	 * @return
	 */
	@PostMapping("add")
	public ApiResult add(@RequestBody SysJurisdiction sysJurisdiction, HttpServletRequest request){
		UserSession user = httpHelper.getUser(request);
		sysJurisdiction.setCreator(user.getUserName());
		return sysJurisdictionService.add(sysJurisdiction);
	}




	/**
	 * 修改权限组
	 * @param sysJurisdiction 权限组实体
	 * @return
	 */
	@PostMapping("update")
	public ApiResult update(@RequestBody SysJurisdiction sysJurisdiction){
		return sysJurisdictionService.update(sysJurisdiction);
	}

	/**
	 * 权限组克隆
	 * @param vo 权限组请求实体
	 * @return
	 */
	@PostMapping("clone")
	public ApiResult clone(@RequestBody SysJurisdictionReqVO vo, HttpServletRequest request){
		UserSession user = httpHelper.getUser(request);
		vo.setCreator(user.getUserName());
		return sysJurisdictionService.cloneSysJurisdiction(vo);
	}
}
