package com.zans.portal.dao;

import com.zans.portal.model.SysSwitcherBranch;
import com.zans.portal.vo.asset.resp.AssetMapRespVO;
import com.zans.portal.vo.common.ChartStatisVO;
import com.zans.portal.vo.switcher.SwitchBranchAcceptVO;
import com.zans.portal.vo.switcher.SwitchBranchMapInitVO;
import com.zans.portal.vo.switcher.SwitchBranchResVO;
import com.zans.portal.vo.switcher.SwitchBranchSearchVO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

@Repository
public interface SysSwitcherBranchMapper extends Mapper<SysSwitcherBranch> {

    List<SwitchBranchResVO> findSwitchList(@Param("reqVo") SwitchBranchSearchVO searchVO);

    SysSwitcherBranch findBySwHost(@Param("swHost") String ipAddr, @Param("id") Integer id);





    List<com.zans.portal.vo.switcher.SwitchBranchResVO> mapList(SwitchBranchMapInitVO reqVO);

    int batchAcceptance(SwitchBranchAcceptVO acceptVO);

    //每个品牌设备
    List<ChartStatisVO> countGroupByBrand();
    //每种交换机设备数
    List<ChartStatisVO> countGroupBySwType();
    //acceptance=1 查已验收
    int countProject(@Param("acceptance") Integer acceptance);

    //在线设备数
    int  countOnlineDevice();
    //，离线断光 掉线
    List<ChartStatisVO> countOfflineDevice();

    int count();


    List<SwitchBranchResVO> getPointListByArea(Integer areaId, List<Integer> projectIds, String pointName);

    List<AssetMapRespVO> assetSwitchMapListPage(SwitchBranchSearchVO reqVO);
}
