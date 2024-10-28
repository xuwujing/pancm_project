package com.zans.portal.service;

import com.zans.base.service.BaseService;
import com.zans.base.vo.ApiResult;
import com.zans.base.vo.PageResult;
import com.zans.base.vo.UserSession;
import com.zans.portal.model.SysSwitcherBranch;
import com.zans.portal.vo.common.TreeSelect;
import com.zans.portal.vo.switcher.*;

import java.util.List;


public interface ISwitcherBrunchService extends BaseService<SysSwitcherBranch> {

    PageResult<SwitchBranchResVO> getSwitchPage(SwitchBranchSearchVO reqVO);

    SysSwitcherBranch findBySwHost( String ipAddr,  Integer id);

    ApiResult batchAddSwitcher(String newFileName, String originName, UserSession userSession);

    List<TreeSelect> getPointListByArea(Integer areaId,List<Integer> projectIds,String pointName);


    ApiResult deleteSwitcherBranch(Integer id);

    void editSwitcherBranch(SysSwitcherBranch switcherBranch, SwitchBranchMergeVO mergeVO,UserSession userSession);

    int insertSwitcherBranch( SwitchBranchMergeVO mergeVO,UserSession userSession);

    ApiResult dispose(SwitchBranchDisposeVO disposeVO,UserSession userSession);

    /**
     * 验收
     * @return
     */
    ApiResult batchAcceptance(SwitchBranchAcceptVO acceptVO);

    List<TreeSelect> getMapTreeAndData(Integer parentId);
}
