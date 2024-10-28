package com.zans.mms.dao.mms;

import com.zans.mms.model.DevicePoint;
import com.zans.mms.model.DevicePointSubsetDetail;
import com.zans.mms.vo.asset.subset.AssetSubsetDetaiQueryReqVO;
import com.zans.mms.vo.devicepoint.DevicePointQueryVO;
import com.zans.mms.vo.devicepoint.DevicePointResVO;
import com.zans.mms.vo.devicepoint.subset.DevicePointSubsetExportVO;
import com.zans.mms.vo.devicepoint.subset.StatusCount;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;
import java.util.Set;

@Repository
public interface DevicePointSubsetDetailMapper extends Mapper<DevicePointSubsetDetail> {
    int removeAsset(@Param("subsetId") Long subsetId, @Param("pointId")Long pointId);

    List<DevicePointResVO> groupList(AssetSubsetDetaiQueryReqVO reqVO);

    List<Long> getPointIdsBySubsetId(Long subsetId);

    int getCountBySubsetId(Long subsetId);

    List<StatusCount> getCountGroupByStatus(Long subsetId);

    List<DevicePointResVO> getPointList(DevicePointQueryVO vo);

    List<Long> getPointIdBySubsetId(Long subsetId);

    int deleteBySubsetId(Long subsetId);

    void deleteByPointId(Long id);

    void deleteDetailBySubsetId(long subsetId);

    String selectBySubsetIdAndPointId(DevicePointSubsetDetail devicePointSubsetDetail);

	List<DevicePointSubsetExportVO> selectExportData();

    int getCountByPointId(Long pointId);

    void deleteByPointIds(@Param("pointIds") Set<Long> retainPointSet);

	List<DevicePoint> getExportDataBySubsetId(String subsetId);
}
