package com.zans.portal.service;

import com.zans.base.vo.ApiResult;
import com.zans.base.vo.PageResult;
import com.zans.portal.model.AssessRecord;
import com.zans.portal.vo.asset.req.AssetAssessSearchVO;
import com.zans.portal.vo.asset.resp.AssetAssessResVO;

/**
* @Title: IAssessService
* @Description: 考核接口
* @Version:1.0.0
* @Since:jdk1.8
* @author beixing
* @Date  2021/11/3
**/
public interface IAssessService {

    /**
    * @Author beixing
    * @Description  查询条件
    * @Date  2021/11/2
    * @Param
    * @return
    **/
    ApiResult list(AssetAssessSearchVO req);


    ApiResult view(AssetAssessSearchVO req);


    ApiResult chartView(AssetAssessSearchVO req);

    ApiResult stateApply(AssessRecord req);

    ApiResult stateView(AssessRecord req);

    ApiResult approve(AssessRecord req);

    /**
     * 考核计算代码
     * @return
     */
    ApiResult calculateByDay();

    PageResult<AssetAssessResVO> getAssessExport(AssetAssessSearchVO req);
}
