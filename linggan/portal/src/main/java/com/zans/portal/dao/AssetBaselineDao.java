package com.zans.portal.dao;

import com.zans.portal.model.Asset;
import com.zans.portal.model.AssetBaseline;
import com.zans.portal.vo.AssetBaselineVO;
import com.zans.portal.vo.AssetBaselineVersionVO;
import com.zans.portal.vo.asset.ExcelAssetBaselineVO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

/**
 * @author beixing
 * @Title: 基线表(AssetBaseline)表数据库访问层
 * @Description:
 * @Version:1.0.0
 * @Since:jdk1.8
 * @date 2021-07-08 14:09:39
 */
@Repository
public interface AssetBaselineDao extends Mapper<AssetBaseline> {

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    AssetBaselineVO queryById(Long id);

    AssetBaselineVO queryByIp(String ip);



    /**
     * 通过实体查询一条数据
     *
     * @param assetBaselineVO 实例对象
     * @return 对象列表
     */
    AssetBaselineVO findOne(AssetBaselineVO assetBaselineVO);

    /**
     * 通过实体作为筛选条件查询
     *
     * @param assetBaselineVO 实例对象
     * @return 对象列表
     */
    List<AssetBaselineVO> queryAll(AssetBaselineVO assetBaselineVO);


    /**
     * 批量新增数据（MyBatis原生foreach方法）
     *
     * @param entities List<AssetBaseline> 实例对象列表
     * @return 影响行数
     */
    int insertBatch(@Param("entities") List<AssetBaseline> entities);

    /**
     * 批量新增或按主键更新数据（MyBatis原生foreach方法）
     *
     * @param entities List<AssetBaseline> 实例对象列表
     * @return 影响行数
     */
    int insertOrUpdateBatch(@Param("entities") List<AssetBaseline> entities);

    /**
     * 修改数据
     *
     * @param assetBaseline 实例对象
     * @return 影响行数
     */
    int update(AssetBaseline assetBaseline);

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 影响行数
     */
    int deleteById(Long id);

	List<AssetBaselineVersionVO> historyList(String ip);

	AssetBaselineVO getByIp(String ipAddr);

    List<AssetBaselineVO> getByMac(String mac);

    List<AssetBaselineVersionVO> historyBaselineList(AssetBaselineVersionVO assetBaseline);

    List<AssetBaseline> findByIpMac(@Param("username") String username, @Param("ipAddr") String curIpAddr);

    void unbindByIpAddr(String ip);

    List<ExcelAssetBaselineVO> excelSelect(AssetBaselineVO assetBaselineVO);

    int updateByAddr(Asset asset);

    int updateAssetBaselineByAddr(AssetBaseline assetBaseline);

	List<AssetBaselineVO> getByIpOrMac(@Param("ipAddr") String ipAddr,@Param("mac") String mac);



    List<AssetBaselineVO> assetMapForTreeList(AssetBaselineVO assetBaselineVO);

    void deleteBaseline();
    void deleteBaselineVersion();

    int deleteIpByAlloc(@Param("allocId") Integer allocId);

}

