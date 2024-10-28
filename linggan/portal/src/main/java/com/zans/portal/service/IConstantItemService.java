package com.zans.portal.service;

import com.zans.base.service.BaseService;
import com.zans.base.vo.PageResult;
import com.zans.portal.model.SysConstantItem;
import com.zans.base.vo.SelectVO;
import com.zans.portal.vo.constant.ConstantItemReqVO;
import com.zans.portal.vo.constant.ConstantItemSearchVO;

import java.util.List;
import java.util.Map;

public interface IConstantItemService extends BaseService<SysConstantItem> {

    List<SelectVO> findItemsByDict(String dictKey);

    Map<Object, String> findItemsMapByDict(String dictKey);

    List<String> getDictKeys();

    PageResult<ConstantItemReqVO> getConstantItemPage(ConstantItemSearchVO reqVO);

    Integer getOrdinalByDictKeyAndItemKey(String dictKey, String itemKey);

    SysConstantItem findItemsByDictAndOridinal(String dictKey, Integer ordinal);

    SysConstantItem findByDictKeyAndItemKey(String dictKey, String itemKey, Integer id);

}
