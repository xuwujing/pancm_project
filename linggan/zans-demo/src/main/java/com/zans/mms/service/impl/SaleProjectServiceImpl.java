package com.zans.mms.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.zans.base.exception.BusinessException;
import com.zans.base.service.impl.BaseServiceImpl;
import com.zans.base.util.PermissionHelper;
import com.zans.base.vo.ApiResult;
import com.zans.base.vo.PageResult;
import com.zans.mms.dao.guard.SaleProjectMapper;
import com.zans.mms.model.SaleProject;
import com.zans.mms.model.SysUser;
import com.zans.mms.service.ISaleProjectService;
import com.zans.mms.service.ISysUserService;
import com.zans.mms.vo.saleproject.SaleProjectQueryVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.List;

/**
* @Title: SaleProjectServiceImpl
* @Description: 演示项目管理ServiceImpl
* @Version:1.0.0
* @Since:jdk1.8
* @author beiming
* @date 5/20/21
*/
@Service("saleProjectService")
public class SaleProjectServiceImpl extends BaseServiceImpl<SaleProject> implements ISaleProjectService {
    @Autowired
    PermissionHelper permissionHelper;

    @Autowired
    ISysUserService sysUserService;

    SaleProjectMapper saleProjectMapper;
    @Resource
    public void setSaleProjectMapper(SaleProjectMapper saleProjectMapper) {
        super.setBaseMapper(saleProjectMapper);
        this.saleProjectMapper = saleProjectMapper;
    }


    @Override
    public ApiResult getList(SaleProjectQueryVO vo) {
        int pageNum = vo.getPageNum();
        int pageSize = vo.getPageSize();
        Page page = PageHelper.startPage(pageNum, pageSize);
        List<SaleProject> result =  saleProjectMapper.getList(vo);
        return ApiResult.success(new PageResult<SaleProject>(page.getTotal(), result, pageSize, pageNum));
    }

    @Override
    public void deleteById(Long id) {
      saleProjectMapper.deleteById(id);
    }

    @Override
    public SaleProject getByProjectId(String projectId) {
        return saleProjectMapper.getByProjectId(projectId);
    }

    @Override
    public void changeProjectId(Integer userId, String projectId) {
        permissionHelper.removeUserIdProjectIdCache(userId);
        permissionHelper.setUserIdProjectIdCache(userId,projectId);
    }

    @Override
    public String getProjectIdByUserId(Integer userId) {
        String projectId = permissionHelper.getProjectId(userId);
        if (!StringUtils.isEmpty(projectId)){
            return projectId;
        }
        SysUser sysUser = sysUserService.getById(userId);
        if (StringUtils.isEmpty(sysUser.getProjectId())){
            throw new BusinessException("ProjectId is null");
        }
        permissionHelper.setUserIdProjectIdCache(userId,sysUser.getProjectId());
        return sysUser.getProjectId();
    }
}
