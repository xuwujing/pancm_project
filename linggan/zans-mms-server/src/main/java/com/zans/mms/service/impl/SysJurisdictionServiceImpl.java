package com.zans.mms.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.zans.base.exception.BusinessException;
import com.zans.base.util.StringHelper;
import com.zans.base.vo.ApiResult;
import com.zans.base.vo.PageResult;
import com.zans.base.vo.SelectVO;
import com.zans.mms.dao.SysJurisdictionDao;
import com.zans.mms.model.SysJurisdiction;
import com.zans.mms.model.SysRolePerm;
import com.zans.mms.service.IPermissionService;
import com.zans.mms.service.ISysJurisdictionService;
import com.zans.mms.vo.asset.AssetResVO;
import com.zans.mms.vo.jurisdiction.SysJurisdictionPermVO;
import com.zans.mms.vo.jurisdiction.SysJurisdictionRepVO;
import com.zans.mms.vo.jurisdiction.SysJurisdictionReqVO;
import com.zans.mms.vo.perm.PermissionRespVO;
import com.zans.mms.vo.role.RoleRespVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.LinkedList;
import java.util.List;

/**
 * @author qitian
 * @Title: zans-mms-server
 * @Description:系统权限配置逻辑层
 * @Version:1.0.0
 * @Since:jdk1.8
 * @date 2021/10/18
 */
@Service("sysJurisdictionService")
@Slf4j
public class SysJurisdictionServiceImpl implements ISysJurisdictionService {

	@Resource
	SysJurisdictionDao sysJurisdictionDao;

	@Autowired
	IPermissionService permissionService;


	/**
	 * 权限列表
	 * @param vo
	 * @return
	 */
	@Override
	public ApiResult list(SysJurisdictionReqVO vo) {
		int pageNum = vo.getPageNum();
		int pageSize = vo.getPageSize();
		Page page = PageHelper.startPage(pageNum, pageSize);
		List<SysJurisdictionRepVO> result = sysJurisdictionDao.list(vo);
		return ApiResult.success(new PageResult<SysJurisdictionRepVO>(page.getTotal(), result, pageSize, pageNum));
	}


	/**
	 * 添加一条权限组
	 * @param sysJurisdiction
	 * @return
	 */
	@Override
	public ApiResult add(SysJurisdiction sysJurisdiction) {
		//查询此权限id是否存在
		if(StringHelper.isEmpty(sysJurisdiction.getId())){
			sysJurisdiction.setId(sysJurisdictionDao.getCurrentId());
		}
		try {
			//查询一个当前
			sysJurisdictionDao.insert(sysJurisdiction);
		}catch (Exception e){
			log.error("新增失败，错误原因#{}",e);
			return ApiResult.error("新增失败，请联系管理员");
		}
		return ApiResult.success();
	}

	/**
	 * 判断权限id是否存在
	 * @param id
	 * @return
	 */
	private boolean exist(String id) {
		return sysJurisdictionDao.exist(id) ==0;
	}

	@Override
	public ApiResult update(SysJurisdiction sysJurisdiction) {
		if(StringHelper.isEmpty(sysJurisdiction.getId())){
			throw  new BusinessException("主键id未传！");
		}
		try {
			//查询一个当前
			sysJurisdictionDao.updateByPrimaryKeySelective(sysJurisdiction);
		}catch (Exception e){
			log.error("修改失败，错误原因#{}",e);
			return ApiResult.error("修改失败，请联系管理员");
		}
		return ApiResult.success();
	}


	@Override
	public List<SelectVO> selectList() {
		return sysJurisdictionDao.selectList();
	}


	/**
	 * 权限克隆
	 * @param vo
	 * @return
	 */
	@Transactional(rollbackFor = Exception.class)
	@Override
	public ApiResult cloneSysJurisdiction(SysJurisdictionReqVO vo) {
		SysJurisdiction sysJurisdiction = new SysJurisdiction();
		BeanUtils.copyProperties(vo,sysJurisdiction);
		sysJurisdiction.setId(sysJurisdictionDao.getCurrentId());
		//先做权限组插入
		sysJurisdictionDao.insert(sysJurisdiction);
		//插入完后，删除原本的所有权限
		sysJurisdictionDao.deleteSysjurisDiction(sysJurisdictionDao.getCurrentId());
		//查询被克隆的权限组权限列表
		List<SysJurisdictionPermVO> sysJurisdictionPermVOList = sysJurisdictionDao.getByJurisdictionId(vo.getCloneId());
		if(!StringHelper.isEmpty(sysJurisdictionPermVOList)){
			for(SysJurisdictionPermVO sysJurisdictionPermVO : sysJurisdictionPermVOList){
				sysJurisdictionPermVO.setJurisdictionId(sysJurisdiction.getId());
				sysJurisdictionDao.insertPerm(sysJurisdictionPermVO);
			}
		}
		return ApiResult.success();
	}
}
