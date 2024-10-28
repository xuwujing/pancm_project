package com.zans.dao;


import com.zans.vo.SelectVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface SysConstantItemMapper {

    @Select(" select item_key as itemKey , item_value as itemValue, class_type from sys_constant_item where dict_key=#{dictKey} order by ordinal, id")
    List<SelectVO> findItemsByDict(@Param("dictKey") String dictKey);

    @Select(" SELECT DISTINCT(dict_key) FROM sys_constant_item ORDER BY  ordinal, id")
    List<String> getDictKeys();


}
