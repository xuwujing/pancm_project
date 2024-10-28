package com.zans.mms.service;

import com.zans.base.service.BaseService;
import com.zans.base.vo.PageResult;
import com.zans.base.vo.SelectVO;
import com.zans.mms.model.SysConstantItem;
import com.zans.mms.vo.constant.ConstantItemReqVO;
import com.zans.mms.vo.constant.ConstantItemSearchVO;

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

    /**
    * @Author beiming
    * @Description  获取常量配置值
    * @Date  4/20/21
    * @Param
    * @return
    **/
    String getConstantValueByKey(String constantKey);

}
