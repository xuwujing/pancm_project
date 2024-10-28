package com.zans.portal.dao;

import com.zans.portal.model.AssetScan;
import com.zans.portal.vo.asset.req.AssetAssessSearchVO;
import com.zans.portal.vo.asset.resp.AssetAssessResVO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

@Repository
public interface AssetScanMapper extends Mapper<AssetScan> {

    /**
     * @Author beixing
     * @Description  考核列表查询
     * @Date  2021/11/2
     * @Param
     * @return
     **/
    List<AssetAssessResVO> findAssetAndAssessList(@Param("reqVo") AssetAssessSearchVO reqVo);

    /**
     * 查找IP对应的交换机最后一条扫描记录
     */
    AssetScan findLastScanRecordByIp(@Param("ip") String ip);

    /** 查询重复生成的数据 */
    List<AssetScan> findRepetitionIp();

    void deleteRepetitionIp(String ip,String time);

    List<AssetScan> findLastScanRecordByIpAndTime(@Param("ip") String ip, @Param("startTime") String startTime);

    AssetAssessResVO getScanDayByIp(@Param("ipAddr") String ipAddr, @Param("approveStartTime") String approveStartTime, @Param("approveEndTime") String approveEndTime);

}
