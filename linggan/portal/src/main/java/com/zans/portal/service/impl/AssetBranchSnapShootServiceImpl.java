package com.zans.portal.service.impl;

import com.zans.base.vo.ApiResult;
import com.zans.portal.dao.AssetBranchSnapShootMapper;
import com.zans.portal.service.IAssetBranchSnapShootService;
import com.zans.portal.vo.asset.req.AssetBranchSnapShootReqVO;
import com.zans.portal.vo.asset.resp.AssetBranchSnapShootRespVO;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author pancm
 * @Title: portal
 * @Description: 快照业务操作实现类
 * @Version:1.0.0
 * @Since:jdk1.8
 * @date 2020/10/20
 */
@Service
public class AssetBranchSnapShootServiceImpl implements IAssetBranchSnapShootService {

    @Autowired
    private AssetBranchSnapShootMapper mapper;

    @Override
    public ApiResult getAssetBranchSnapShoot(AssetBranchSnapShootReqVO req) {
        List<AssetBranchSnapShootRespVO> respVOList = mapper.getAssetBranchSnapShoot(req);
        return ApiResult.success(respVOList);
    }

    @Override
    public ApiResult save(AssetBranchSnapShootReqVO req) {
        // 不能重复添加同一个时间点
        if (StringUtils.isNotBlank(req.getSnapShootTime())) {
            req.setEnable(1);
            mapper.disableAll();
            List<AssetBranchSnapShootRespVO> voList = mapper.findJobByTime(req.getSnapShootTime());
            if (CollectionUtils.isNotEmpty(voList)) {
                req.setId(voList.get(0).getId());
                mapper.update(req);
            } else {
                mapper.save(req);
            }
        }
        return ApiResult.success();
    }
}
