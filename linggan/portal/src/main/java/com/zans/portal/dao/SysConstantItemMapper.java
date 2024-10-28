package com.zans.portal.dao;

import com.zans.portal.model.SysConstantItem;
import com.zans.base.vo.SelectVO;
import com.zans.portal.vo.constant.ConstantItemReqVO;
import com.zans.portal.vo.constant.ConstantItemSearchVO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

@Repository
public interface SysConstantItemMapper extends Mapper<SysConstantItem> {

    List<SelectVO> findItemsByDict(@Param("dictKey") String dictKey);

    List<String> getDictKeys();

    List<ConstantItemReqVO> findConstantItemList(@Param("reqVO") ConstantItemSearchVO reqVO);

    Integer getOrdinalByDictKeyAndItemKey(@Param("dictKey") String dictKey, @Param("itemKey") String itemKey);

    SysConstantItem findItemsByDictAndOridinal(@Param("dictKey") String dictKey, @Param("ordinal") Integer ordinal);

    SysConstantItem findByDictKeyAndItemKey(@Param("dictKey") String dictKey, @Param("itemKey") String itemKey, @Param("id") Integer id);

}