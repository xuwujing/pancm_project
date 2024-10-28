package com.zans.mms.dao.mms;

import com.zans.mms.model.BaseVfs;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

/**
 * 附件表(BaseVfs)表数据库访问层
 *
 * @author beixing
 * @since 2021-03-02 16:22:01
 */
@Repository
public interface BaseVfsDao extends Mapper<BaseVfs> {

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    BaseVfs queryById(Long id);


    List<BaseVfs> queryByAdjunctId(@Param("adjunctId")  String adjunctId);


    /**
     * 通过实体作为筛选条件查询
     *
     * @param baseVfs 实例对象
     * @return 对象列表
     */
    List<BaseVfs> queryAll(BaseVfs baseVfs);

    /**
     * 新增数据
     *
     * @param baseVfs 实例对象
     * @return 影响行数
     */
    @Override
    int insert(BaseVfs baseVfs);

    /**
     * 批量新增数据（MyBatis原生foreach方法）
     *
     * @param entities List<BaseVfs> 实例对象列表
     * @return 影响行数
     */
    int insertBatch(@Param("entities") List<BaseVfs> entities);

    /**
     * 批量新增或按主键更新数据（MyBatis原生foreach方法）
     *
     * @param entities List<BaseVfs> 实例对象列表
     * @return 影响行数
     */
    int insertOrUpdateBatch(@Param("entities") List<BaseVfs> entities);

    /**
     * 修改数据
     *
     * @param baseVfs 实例对象
     * @return 影响行数
     */
    int update(BaseVfs baseVfs);

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 影响行数
     */
    int deleteById(Long id);

    List<BaseVfs> getAcceptAdjunctList(Long id);
}

