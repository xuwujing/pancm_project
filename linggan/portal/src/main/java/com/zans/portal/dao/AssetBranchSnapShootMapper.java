package com.zans.portal.dao;

import com.zans.portal.vo.asset.req.AssetBranchSnapShootReqVO;
import com.zans.portal.vo.asset.resp.AssetBranchSnapShootRespVO;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author pancm
 * @Title: portal
 * @Description:
 * @Version:1.0.0
 * @Since:jdk1.8
 * @date 2020/10/20
 */
@Repository
public interface AssetBranchSnapShootMapper  {

    List<AssetBranchSnapShootRespVO> getAssetBranchSnapShoot(AssetBranchSnapShootReqVO req);

    void save(AssetBranchSnapShootReqVO req);

    void update(AssetBranchSnapShootReqVO req);

    void disableAll();

    List<AssetBranchSnapShootRespVO> findJobByTime(@Param("snapShootTime") String snapShootTime);
}
