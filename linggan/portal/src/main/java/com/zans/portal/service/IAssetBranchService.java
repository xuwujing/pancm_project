package com.zans.portal.service;

import com.zans.base.service.BaseService;
import com.zans.base.vo.PageResult;
import com.zans.base.vo.SelectVO;
import com.zans.portal.model.AssetBranch;
import com.zans.portal.vo.asset.branch.req.*;
import com.zans.portal.vo.asset.branch.resp.AssetBranchJoinRespVO;
import com.zans.portal.vo.asset.branch.resp.ChoiceDeviceRespVO;
import com.zans.portal.vo.asset.branch.resp.ExcelAssetRunStatisticsVO;
import com.zans.portal.vo.asset.branch.resp.RunningListRespVO;
import com.zans.portal.vo.common.TreeSelect;

import java.util.Date;
import java.util.List;

/**
 * @description:
 * @author: ns_wang
 * @time: 2020/10/24 11:05
 */
public interface IAssetBranchService  extends BaseService<AssetBranch> {
    Integer findByName(String name, Integer id);

    /**
     * 统计指定分组的数据 写入数据库
     * @param assetBranchId  分组id
     * @param statisticsTime  快照统计时间
     * @param saveStatisticsDetailFlag  是否保存快照详情
     * @return
     */
    List<AssetBranchStatisticsVO> statisticsAssetBranchById(Integer assetBranchId, Date statisticsTime,Boolean saveStatisticsDetailFlag);

    /**
     * 统计所有分组的数据
     * @param time
     * @return
     */
    boolean statisticsAssetBranchAll(String time);

    /**
     * 选择设备
     * @param reqVO
     * @return
     */
    PageResult<ChoiceDeviceRespVO> choiceDevicePage(ChoiceDeviceReqVO reqVO);

    /**
     * 统计运行列表 柱状图
     * @param reqVO
     * @return
     */
    PageResult<RunningListRespVO> runningList(RunningListReqVO reqVO);

    /**
     * 统计运行详情列表
     * @param reqVO
     * @return
     */
    PageResult<AssetBranchStatisticsVO> runningDetailListPage(RunningDetailListReqVO reqVO);

    /**
     * 资产子集数据列表
     * @param reqVO
     * @return
     */
    PageResult<AssetBranchStatisticsVO> assetBranchEndpointPage(AssetBranchReqVO reqVO);

    /**
     * 资产子集分组列表
     * @return
     * @param name
     */
    List<SelectVO> findToSelect(String name);

    /**
     * 资产子集分组列表
     * @return
     * @param
     */
    List<TreeSelect> findToTreeSelect();



    /**
     * 导出资产子集数据列表
     * @param reqVO
     * @return
     */
    List<ExcelAssetBranchStatisticsVO> assetBranchEndpoint(AssetBranchReqVO reqVO);

    /**
     * 导出运行统计详情列表
     * @param reqVO
     * @return
     */
    List<ExcelAssetBranchStatisticsVO> runningDetailList(RunningDetailListReqVO reqVO);

    /**
     * 已运行的快照列表
     * @return
     */
    List<String> timeList();

    /**
     * 导出运行统计列表
     * @param reqVO
     * @return
     */
    List<ExcelAssetRunStatisticsVO> runningListExport(RunningListReqVO reqVO);

    /**
     * 获取莫分组的详情数据，上一个、下一个
     * @param id
     * @return
     */
    AssetBranchJoinRespVO getDetailById(Integer id);

    /**
     * 获取到下一个的排序
     * @return
     */
    Integer getNextSeq(Integer parentId);

    /**
     * 实时统计 运行统计列表
     * @return
     */
    PageResult<RunningListRespVO> syncStatistics(RunningListReqVO  reqVO);

    List<AssetBranch> getAllList();

    List<AssetBranch> getAssetBranchByAreaId(Integer id);
}
