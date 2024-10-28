package com.zans.mms.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.zans.base.service.impl.BaseServiceImpl;
import com.zans.base.vo.PageResult;
import com.zans.base.vo.SelectVO;
import com.zans.mms.dao.guard.SysConstantItemMapper;
import com.zans.mms.model.SysConstantItem;
import com.zans.mms.service.IConstantItemService;
import com.zans.mms.vo.constant.ConstantItemReqVO;
import com.zans.mms.vo.constant.ConstantItemSearchVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service("constantItemService")
@Slf4j
public class ConstantItemServiceImpl extends BaseServiceImpl<SysConstantItem> implements IConstantItemService {

    SysConstantItemMapper constantItemMapper;

    @Resource
    public void setConstantItemMapper(SysConstantItemMapper baseMapper) {
        super.setBaseMapper(baseMapper);
        this.constantItemMapper = baseMapper;
    }

    @Override
    @Cacheable(cacheNames = "CONSTANT_LIST", key = "#dictKey")
    public List<SelectVO> findItemsByDict(String dictKey) {
        List<SelectVO> list = constantItemMapper.findItemsByDict(dictKey);
        for (SelectVO vo : list) {
            vo.resetKey();
        }
        return list;
    }

    @Override
    @Cacheable(cacheNames = "CONSTANT_MAP", key = "#dictKey")
    public Map<Object, String> findItemsMapByDict(String dictKey) {
        log.info("reload db.constant#{}", dictKey);
        List<SelectVO> selectList = this.constantItemMapper.findItemsByDict(dictKey);
        for (SelectVO vo : selectList) {
            vo.resetKey();
        }
        Map<Object, String> map = selectList.stream().collect(Collectors.toMap(SelectVO::getItemKey, SelectVO::getItemValue));
        return map;
    }

    @Override
    public PageResult<ConstantItemReqVO> getConstantItemPage(ConstantItemSearchVO reqVO) {
        String orderBy = reqVO.getSortOrder();
        Page page = PageHelper.startPage(reqVO.getPageNum(), reqVO.getPageSize());

        List<ConstantItemReqVO> list = constantItemMapper.findConstantItemList(reqVO);
        return new PageResult<ConstantItemReqVO>(page.getTotal(), page.getResult(), reqVO.getPageSize(), reqVO.getPageNum());
    }

    @Override
    public Integer getOrdinalByDictKeyAndItemKey(String dictKey, String itemKey) {
        return constantItemMapper.getOrdinalByDictKeyAndItemKey(dictKey, itemKey);
    }

    @Override
    public List<String> getDictKeys() {
        return constantItemMapper.getDictKeys();
    }

    @Override
    public SysConstantItem findItemsByDictAndOridinal(String dictKey, Integer ordinal) {
        return constantItemMapper.findItemsByDictAndOridinal(dictKey, ordinal);
    }

    @Override
    public SysConstantItem findByDictKeyAndItemKey(String dictKey, String itemKey, Integer id) {
        return constantItemMapper.findByDictKeyAndItemKey(dictKey, itemKey, id);
    }

    @Override
    public String getConstantValueByKey(String constantKey) {
        return constantItemMapper.getConstantValueByKey(constantKey);
    }
}
