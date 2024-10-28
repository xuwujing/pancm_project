package com.zans.portal.dao;

import com.zans.base.vo.SelectVO;
import com.zans.portal.model.SysConstant;
import com.zans.portal.vo.constant.ConstantReqVO;
import com.zans.portal.vo.constant.ConstantSearchVO;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface SysConstantMapper extends Mapper<SysConstant> {

    SelectVO findSelectVOByKey(@Param("key") String key);

    SysConstant findConstantByKey(@Param("key") String key);

    List<ConstantReqVO> findConstantList(@Param("reqVo") ConstantSearchVO reqVo);

}