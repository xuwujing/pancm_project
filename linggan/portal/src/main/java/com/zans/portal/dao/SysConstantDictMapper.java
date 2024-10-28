package com.zans.portal.dao;

import com.zans.base.vo.SelectVO;
import com.zans.portal.model.SysConstantDict;
import com.zans.portal.vo.constant.ConstantDictResVO;
import com.zans.portal.vo.constant.ConstantDictSearchVO;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface SysConstantDictMapper extends Mapper<SysConstantDict> {

    List<ConstantDictResVO> findConstantDictList(@Param("reqVo") ConstantDictSearchVO reqVO);

    SelectVO findByKeyOrName(@Param("dictKey") String dictKey, @Param("dictName") String dictName);

    SysConstantDict findByKey(@Param("dictKey") String dictKey);

}