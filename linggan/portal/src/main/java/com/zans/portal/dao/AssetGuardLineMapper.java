package com.zans.portal.dao;

import com.alibaba.fastjson.JSONObject;
import com.zans.portal.model.AssetGuardLine;
import com.zans.portal.vo.asset.guardline.req.AssetGuardLineReqVO;
import com.zans.portal.vo.asset.guardline.resp.AssetGuardLineRespVO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

@Repository
public interface AssetGuardLineMapper extends Mapper<AssetGuardLine> {

    Integer getNextSeq();

    List<AssetGuardLineRespVO> findListByNameFuzzy(@Param("name") String name);

    /**
     * 查询名称存在条数
     * @param name  精准匹配
     * @param id    排除某ID
     * @return
     */
    Integer findCountByName(@Param("name") String name,@Param("id") Integer id);

    /**
     * 获取所有
     * @return
     */
    List<AssetGuardLine> getAll();

    /**
     * 获取已经添加的线路总数
     * @return
     */
    Integer findTotalCount();

    int deleteByIpAddr(String ipAddr);

    JSONObject getGroupStatistics(AssetGuardLineReqVO reqVO);

    JSONObject getGroupStatisticsTotal(AssetGuardLineReqVO reqVO);


}
