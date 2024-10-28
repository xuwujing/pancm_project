package com.zans.mms.service;

import com.zans.base.service.BaseService;
import com.zans.base.vo.PageResult;
import com.zans.base.vo.UserSession;
import com.zans.mms.model.Arp;
import com.zans.mms.vo.arp.AssetRespVO;
import com.zans.mms.vo.arp.AssetSearchVO;
import org.javatuples.Pair;

import java.util.List;
import java.util.Map;

public interface IArpService extends BaseService<Arp> {

	PageResult<AssetRespVO> getAssetPage(AssetSearchVO reqVO);

}
