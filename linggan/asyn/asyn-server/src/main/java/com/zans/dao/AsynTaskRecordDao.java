package com.zans.dao;

import com.zans.model.AsynTaskRecord;
import com.zans.vo.AsynTaskRecordVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * (AsynTaskRecord)表数据库访问层
 *
 * @author beixing
 * @since 2021-11-22 11:31:54
 */
@Mapper
public interface AsynTaskRecordDao {

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    AsynTaskRecord queryById(Long id);


    AsynTaskRecordVo queryByTaskId(String id);


    /**
     * 通过实体作为筛选条件查询
     *
     * @param asynTaskRecord 实例对象
     * @return 对象列表
     */
    List<AsynTaskRecord> queryAll(AsynTaskRecord asynTaskRecord);


    /**
     * @Author beixing
     * @Description  执行的状态统计
     * @Date  2021/12/17
     * @Param
     * @return
     **/
    Map<String,Integer> statistics();

    /**
     * @Author beixing
     * @Description  所有执行花费的时间，单位秒
     * @Date  2021/12/17
     * @Param
     * @return
     **/
    List<Map<String,Integer>> executorTime();

    /**
    * @Author beixing
    * @Description  当天执行的状态统计
    * @Date  2021/12/17
    * @Param
    * @return
    **/
    Map<String,Integer> statisticsNowDay();

    /**
    * @Author beixing
    * @Description  当天执行花费的时间，单位秒
    * @Date  2021/12/17
    * @Param
    * @return
    **/
    List<Map<String,Integer>> executorTimeByNowDay();

    /**
     * 新增数据
     *
     * @param asynTaskRecord 实例对象
     * @return 影响行数
     */
    int insert(AsynTaskRecord asynTaskRecord);

    /**
     * 批量新增数据（MyBatis原生foreach方法）
     *
     * @param entities List<AsynTaskRecord> 实例对象列表
     * @return 影响行数
     */
    int insertBatch(@Param("entities") List<AsynTaskRecord> entities);

    /**
     * 批量新增或按主键更新数据（MyBatis原生foreach方法）
     *
     * @param entities List<AsynTaskRecord> 实例对象列表
     * @return 影响行数
     */
    int insertOrUpdateBatch(@Param("entities") List<AsynTaskRecord> entities);

    /**
     * 修改数据
     *
     * @param asynTaskRecord 实例对象
     * @return 影响行数
     */
    int update(AsynTaskRecord asynTaskRecord);

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 影响行数
     */
    int deleteById(Long id);

}

