package com.zans.mms.dao.guard;

import com.zans.mms.model.Asset;
import com.zans.mms.vo.asset.*;
import com.zans.mms.vo.asset.diagnosis.AssetDiagnosisHisRespVO;
import com.zans.mms.vo.asset.diagnosis.AssetDiagnosisInfoRespVO;
import com.zans.mms.vo.asset.subset.AssetSubsetDetailAddByConditionReqVO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

import java.util.HashMap;
import java.util.List;

@Repository
public interface AssetMapper extends Mapper<Asset> {
    List<AssetResVO> getList(AssetQueryVO vo);

    List<AssetMonitorResVO> getMonitorList(AssetQueryVO vo);

    AssetViewResVO getViewById(Long id);

    List<Asset> getListByCondition(HashMap<String, Object> map);

    Integer getIdByUniqueId(String assetId);

    int deleteByUniqueId(String id);

    int getExistAssetCode(String assetCode);

    void insertAsset(ExcelAssetVO vo);

    void deleteByAssetCode(String assetCode);

    void updateByAssetCode(ExcelAssetVO vo);

    void updateByAssetCodeChange(ExcelAssetVO vo);

    List<Long> getAssetByCondition(AssetSubsetDetailAddByConditionReqVO reqVO);

    List<AssetResVO> chooseAssetList(AssetQueryVO vo);

    List<String> getIdByCode(@Param("assetCode") String assetCode, @Param("deviceType") String deviceType);

    /**
    * @Author beiming
    * @Description  资产地图
    * @Date  4/22/21
    * @Param
    * @return
    **/
    List<AssetForMapResVO> getListForMap();


    AssetDiagnosisInfoRespVO getDiagnosisView(String ip);

    AssetDiagnosisInfoRespVO getDiagnosisHisView(String ip, String traceId);

    List<AssetDiagnosisHisRespVO> getThreeDaysDiagnosisHisList(String ip);


    /**
     * 根据IP地址查询数据
     *
     * @param ipAddr
     * @return
     */
    Asset findByIpAddr(@Param("ipAddr") String ipAddr);

    List<SwitcherMacInterfaceResVO> getSwitchMacInterface();


    SwitcherMacInterfaceResVO getSwitchMacInterfaceByPort(String portId);

    void deleteByMac(@Param("mac") String mac);

    SwitcherMacInterfaceResVO getSwitchMacInterfaceByPortDown(String portId);
}
