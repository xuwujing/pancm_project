package com.zans.portal.dao;

import com.zans.base.vo.SelectVO;
import com.zans.portal.model.SysBrand;
import com.zans.portal.vo.brand.BrandMergeVO;
import com.zans.portal.vo.brand.BrandResponseVO;
import com.zans.portal.vo.brand.BrandSearchVO;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;
import java.util.Map;

public interface SysBrandMapper extends Mapper<SysBrand> {

    List<BrandResponseVO> getBrandPage(@Param("reqVo") BrandSearchVO reqVo);

    List<Map<String, Long>> findBrandCountByElement(@Param("mergeVO") BrandMergeVO mergeVO);

    List<SelectVO> findBrand();
}
