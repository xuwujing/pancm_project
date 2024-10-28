package com.zans.dao;

import com.zans.model.FortServerConfig;
import com.zans.vo.FortServerConfigVO;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Mapper;
import tk.mybatis.mapper.common.BaseMapper;

import java.util.List;

/**
 * @author beixing
 * @Title: (FortServerConfig)表数据库访问层
 * @Description:
 * @Version:1.0.0
 * @Since:jdk1.8
 * @date 2021-06-28 14:17:37
 */
@Mapper
public interface FortServerConfigDao extends BaseMapper<FortServerConfig> {

    /**
     * 通过ID查询单条数据
     *
     * @param serverId 主键
     * @return 实例对象
     */
    FortServerConfigVO queryById(Integer serverId);

    FortServerConfigVO queryByIp(String serverIp);


    /**
     * 通过实体查询一条数据
     *
     * @param fortServerConfigVO 实例对象
     * @return 对象列表
     */
    FortServerConfigVO findOne(FortServerConfigVO fortServerConfigVO);

    /**
     * 通过实体作为筛选条件查询
     *
     * @param fortServerConfigVO 实例对象
     * @return 对象列表
     */
    List<FortServerConfigVO> queryAll(FortServerConfigVO fortServerConfigVO);

    /**
     * 新增数据
     *
     * @param fortServerConfig 实例对象
     * @return 影响行数
     */
    int insert(FortServerConfig fortServerConfig);

    /**
     * 批量新增数据（MyBatis原生foreach方法）
     *
     * @param entities List<FortServerConfig> 实例对象列表
     * @return 影响行数
     */
    int insertBatch(@Param("entities") List<FortServerConfig> entities);

    /**
     * 批量新增或按主键更新数据（MyBatis原生foreach方法）
     *
     * @param entities List<FortServerConfig> 实例对象列表
     * @return 影响行数
     */
    int insertOrUpdateBatch(@Param("entities") List<FortServerConfig> entities);

    /**
     * 修改数据
     *
     * @param fortServerConfig 实例对象
     * @return 影响行数
     */
    int update(FortServerConfig fortServerConfig);

    /**
     * 通过主键删除数据
     *
     * @param serverId 主键
     * @return 影响行数
     */
    int deleteById(Integer serverId);

    FortServerConfig selectIpByServerName(FortServerConfigVO serverConfigVO);

    List<FortServerConfigVO> selectAllServer();

}

