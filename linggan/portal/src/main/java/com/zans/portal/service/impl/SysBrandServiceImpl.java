package com.zans.portal.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.zans.base.service.impl.BaseServiceImpl;
import com.zans.base.vo.ApiResult;
import com.zans.base.vo.PageResult;
import com.zans.portal.dao.AreaNasMapper;
import com.zans.portal.dao.ArpMapper;
import com.zans.portal.dao.SysBrandMapper;
import com.zans.portal.model.AreaNas;
import com.zans.portal.model.SysBrand;
import com.zans.portal.service.IAreaNasService;
import com.zans.portal.service.ISysBrandService;
import com.zans.portal.vo.area.AreaNasVO;
import com.zans.portal.vo.brand.BrandMergeVO;
import com.zans.portal.vo.brand.BrandResponseVO;
import com.zans.portal.vo.brand.BrandSearchVO;
import com.zans.portal.vo.device.DeviceResponseVO;
import com.zans.portal.vo.device.DeviceSearchVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * @author yanghanjin
 */
@Service
@Slf4j
public class SysBrandServiceImpl extends BaseServiceImpl<SysBrand> implements ISysBrandService {

    SysBrandMapper sysBrandMapper;

    @Resource
    public void setSysBrandMapper(SysBrandMapper brandMapper) {
        super.setBaseMapper(brandMapper);
        this.sysBrandMapper = brandMapper;
    }

    @Override
    public PageResult<BrandResponseVO> getBrandPage(BrandSearchVO reqVO) {
        String orderBy = reqVO.getSortOrder();
        Page page = PageHelper.startPage(reqVO.getPageNum(), reqVO.getPageSize());

        List<BrandResponseVO> list = sysBrandMapper.getBrandPage(reqVO);
        return new PageResult<BrandResponseVO>(page.getTotal(), page.getResult(), reqVO.getPageSize(), reqVO.getPageNum());
    }

    /***
     * 品牌名称、品牌公司、品牌关键字 唯一值验证
     * @return
     */
    @Override
    public ApiResult findBrandCountByElement(BrandMergeVO mergeVO) {
        List<Map<String, Long>> elements = sysBrandMapper.findBrandCountByElement(mergeVO);
        for (Map<String, Long> element : elements) {
            if (element.get("qty") != 0L) {
                return ApiResult.error(element.get("element") + "已存在，请确认后提交");
            }
        }
        return ApiResult.success();
    }

    @Override
    @Cacheable(cacheNames = "BRAND",key = "#id")
    public SysBrand getCacheById(Integer id) {
        return getById(id);
    }
}
