package com.zans.portal.service;

import com.zans.base.service.BaseService;
import com.zans.base.vo.ApiResult;
import com.zans.base.vo.PageResult;
import com.zans.base.vo.UserSession;
import com.zans.portal.model.AssetGuardLine;
import com.zans.portal.vo.asset.branch.resp.ChoiceDeviceRespVO;
import com.zans.portal.vo.asset.guardline.req.AssetGuardLineReqVO;
import com.zans.portal.vo.asset.guardline.req.GuardLineChoiceDeviceReqVO;
import com.zans.portal.vo.asset.guardline.resp.AssetGuardLineJoinRespVO;
import com.zans.portal.vo.asset.guardline.resp.AssetGuardLineListVO;
import com.zans.portal.vo.asset.guardline.resp.AssetGuardLineRespVO;
import com.zans.portal.vo.asset.guardline.resp.ExcelAssetGuardLineStatisticsVO;

import java.util.List;
/**
 * 警卫线路
 */
public interface IAssetGuardLineService extends BaseService<AssetGuardLine> {

    /**
     * 获取到下一个的排序
     * @return
     */
    Integer getNextSeq();

    /**
     * 警卫线路集合
     * @return
     * @param name  模糊查询
     */
    List<AssetGuardLineRespVO> getListByName(String name);

    /**
     *
     * @param name 精准匹配
     * @param id   排除某ID, 可以为空
     * @return
     */
    boolean isNameExist(String name,Integer id);

    /**
     * 获取线路资产列表
     * @param reqVO
     * @return
     */
    List<AssetGuardLineListVO> getGuardLineAsset(AssetGuardLineReqVO reqVO);

    /**
     * 导出数据获取
     * @param reqVO
     * @return
     */
    List<ExcelAssetGuardLineStatisticsVO> getGuardListExcelList(AssetGuardLineReqVO reqVO);

    /**
     * 获取线路可以增加的资产列表
     * @param reqVO
     * @return
     */
    public PageResult<ChoiceDeviceRespVO> choiceDevice(GuardLineChoiceDeviceReqVO reqVO);


    /**
     * 获取某线路的详情数据，上一个、下一个
     * @param id
     * @return
     */
    AssetGuardLineJoinRespVO getDetailById(Integer id);


    /**
     * 执行强制下线
     *  @param gulineLineAssetIdList
     * @param ipAddr
     * @param userSession
     */
    void execForceOfflineCommand(List<Integer> gulineLineAssetIdList, List<String> ipAddr, Integer policy, UserSession userSession);

    /**
     * 执行恢复入网操作
     * @param gulineLineAssetIdList
     * @param ipAddr
     * @param userSession
     */
    void execForceOnlineCommand(List<Integer> gulineLineAssetIdList, List<String> ipAddr, Integer policy, UserSession userSession);

    /**
     * 判断是否可以添加更多线路
     * @return
     */
    boolean canAddNewLine();

    int deleteByIpAddr(String ipAddr);

    ApiResult getPointList(AssetGuardLineReqVO reqVO);
}
