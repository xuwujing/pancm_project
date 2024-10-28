package com.zans.portal.dao;

import com.zans.portal.model.SysModule;
import com.zans.base.vo.SelectVO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

@Repository
public interface SysModuleMapper extends Mapper<SysModule> {

    List<SysModule> findMenuList();

    List<SysModule> findMenuListByLikeName(@Param("moduleName") String moduleName);

    List<SysModule> findMenuByName(@Param("moduleName") String moduleName);

    List<SelectVO> findModuleToSelect();

    int findByName(@Param("name") String name, @Param("id") Integer id);

    int findByRoute(@Param("route") String route,@Param("id") Integer id);

    int findSeq(@Param("parentId") Integer parentId);

}