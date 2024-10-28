package com.zans.portal.service;

import com.zans.base.service.BaseService;
import com.zans.portal.model.SysModule;
import com.zans.base.vo.SelectVO;
import com.zans.portal.vo.common.TreeSelect;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author xv
 * @since 2020/3/7 0:04
 */
public interface IModuleService extends BaseService<SysModule> {

    List<SysModule> findMenuList();

    List<SysModule> findMenuListByLikeName(String moduleName);

    List<SelectVO> findModuleToSelect();

    List<TreeSelect> getMenuTree(String menuName);

    int findByName(String name, Integer id);

    int findByRoute(String route, Integer id);

    int findSeq(Integer parentId);

}
