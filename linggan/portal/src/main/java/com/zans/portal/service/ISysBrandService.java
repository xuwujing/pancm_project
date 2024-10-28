package com.zans.portal.service;

import com.zans.base.service.BaseService;
import com.zans.base.vo.ApiResult;
import com.zans.base.vo.PageResult;
import com.zans.portal.model.SysBrand;
import com.zans.portal.vo.brand.BrandMergeVO;
import com.zans.portal.vo.brand.BrandResponseVO;
import com.zans.portal.vo.brand.BrandSearchVO;

public interface ISysBrandService extends BaseService<SysBrand> {

    PageResult<BrandResponseVO> getBrandPage(BrandSearchVO reqVO);

    ApiResult findBrandCountByElement(BrandMergeVO mergeVO);

    SysBrand getCacheById(Integer id);

}
