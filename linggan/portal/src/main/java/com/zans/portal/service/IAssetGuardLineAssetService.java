package com.zans.portal.service;

import com.zans.base.service.BaseService;
import com.zans.base.vo.BasePage;
import com.zans.base.vo.PageResult;
import com.zans.base.vo.UserSession;
import com.zans.portal.model.AssetGuardLineAsset;
import com.zans.portal.vo.asset.guardline.req.AssetGuardLineAssetAddReqVO;
import com.zans.portal.vo.asset.guardline.req.AssetGuardLineReqVO;
import com.zans.portal.vo.asset.guardline.resp.*;

import java.util.List;
/**
 * 警卫线路下资产
 */
public interface IAssetGuardLineAssetService extends BaseService<AssetGuardLineAsset>{

    int addAssetToLine(AssetGuardLineAssetAddReqVO req, UserSession userSession);

    /**
     * 获取线路下资产列表
     * @param reqVO
     * @return
     */
    List<AssetGuardLineListVO> getGuardLineAssetList(AssetGuardLineReqVO reqVO);

    /**
     * 获取导出列表
     * @param reqVO
     * @return
     */
    List<ExcelAssetGuardLineStatisticsVO> getGuardListAssetExcelList(AssetGuardLineReqVO reqVO);

    /**
     * 获取线路已经添加的资产ID
     * @param guardLineId
     * @return
     */
    List<Integer> getExistAssetIds(Integer guardLineId);


    List<String> getExistAssetIps(Integer guardLineId);

    /**
     * 线路运行统计
     * @return
     */
    PageResult<GuardLineRunningListRespVO> runningList(BasePage reqVO);

    /**
     * 线路运行统计导出数据
     * @return
     */
    List<ExcelGuardLineRunningListRespVO> runningListExport();


    /**
     * 查找强制下线设备ID
     */
    List<GuardLineForceCommandObjectVO> findForceEndpointIds(List<Integer> idList);

    List<GuardLineForceCommandObjectVO> findForceEndpointIps(List<String> ipList);

   List<GuardLineForceCommandObjectVO> findForceOnlineEndpointIds(List<Integer> idList);

   List<GuardLineForceCommandObjectVO> findForceEndpointIps2(List<String> ipList);

    /**
     * 获取线路所有资产
     * @param lineId
     * @return
     */
    List<AssetGuardLineAsset> getAllByLineId(int lineId);


    /**
     * 获取强制执行结果
     * @param gulineLineAssetIdList
     * @param userSession
     * @return
     */
    List<GuardLineForceCommandObjectVO> getCommandExecResult(List<Integer> gulineLineAssetIdList);
}
