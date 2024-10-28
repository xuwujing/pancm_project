package com.zans.mms.service;

import com.zans.base.service.BaseService;
import com.zans.base.vo.ApiResult;
import com.zans.mms.model.SaleProject;
import com.zans.mms.vo.saleproject.SaleProjectQueryVO;

/**
* @Title: ISaleProjectService
* @Description: 演示项目管理Service
* @Version:1.0.0
* @Since:jdk1.8
* @author beiming
* @date 5/20/21
*/
public interface ISaleProjectService extends BaseService<SaleProject> {
    ApiResult getList(SaleProjectQueryVO vo);

    void deleteById(Long id);

    SaleProject getByProjectId(String projectId);

    void changeProjectId(Integer userId, String projectId);

    String getProjectIdByUserId(Integer userId);

}
