package com.zans.service;

import com.zans.utils.ApiResult;
import com.zans.vo.FortServerConfigVO;


/**
 * @author beixing
 * @Title: (FortServerConfig)表服务接口
 * @Description:
 * @Version:1.0.0
 * @Since:jdk1.8
 * @date 2021-06-28 14:17:37
 */
public interface IFortServerConfigService {

    /**
     * 通过ID查询单条数据
     *
     * @param serverId 主键
     * @return 实例对象
     */
    FortServerConfigVO queryById(Integer serverId);

    FortServerConfigVO queryByIp(String serverIp);
    /**
     * 通过实体作为筛选条件查询
     *
     * @param fortServerConfigVO 实例对象
     * @return 对象列表
     */
    ApiResult list(FortServerConfigVO fortServerConfigVO);


    /**
     * 新增数据
     *
     * @param fortServerConfigVO 实例对象
     * @return 实例对象
     */
    int insert(FortServerConfigVO fortServerConfigVO);

    /**
     * 修改数据
     *
     * @param fortServerConfigVO 实例对象
     * @return 实例对象
     */
    int update(FortServerConfigVO fortServerConfigVO);

    /**
     * 通过主键删除数据
     *
     * @param serverId 主键
     * @return 是否成功
     */
    boolean deleteById(Integer serverId);

}
