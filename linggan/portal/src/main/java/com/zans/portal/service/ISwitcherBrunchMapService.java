package com.zans.portal.service;

import com.zans.base.service.BaseService;
import com.zans.base.vo.PageResult;
import com.zans.portal.model.SysSwitcherBranch;
import com.zans.portal.vo.asset.resp.AssetMapRespVO;
import com.zans.portal.vo.switcher.SwitchBranchConvergeMapResVO;
import com.zans.portal.vo.switcher.SwitchBranchMapInitVO;
import com.zans.portal.vo.switcher.SwitchBranchSearchVO;


public interface ISwitcherBrunchMapService extends BaseService<SysSwitcherBranch> {

    SwitchBranchConvergeMapResVO mapList(SwitchBranchMapInitVO reqVO);


    PageResult<AssetMapRespVO> assetSwitchMapListPage(SwitchBranchSearchVO reqVO);
}
