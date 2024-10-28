package com.zans.mms.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.zans.base.service.impl.BaseServiceImpl;
import com.zans.base.vo.PageResult;
import com.zans.base.vo.SelectVO;
import com.zans.mms.dao.SysConstantItemMapper;
import com.zans.mms.model.BaseArea;
import com.zans.mms.model.SysConstantItem;
import com.zans.mms.service.IConstantItemService;
import com.zans.mms.vo.common.TreeSelect;
import com.zans.mms.vo.constant.ConstantItemReqVO;
import com.zans.mms.vo.constant.ConstantItemSearchVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
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

    @Override
    public List<TreeSelect> findIssueSourceTree(String dictKey) {
        List<SelectVO> list = constantItemMapper.findItemsByDict(dictKey);


        List<TreeSelect> resultList = new ArrayList<>(list.size());
        TreeSelect select1 = TreeSelect.builder()
                .id(10)
                .label("主动发现")
                .build();
        TreeSelect select2 = TreeSelect.builder()
                .id(20)
                .label("业主反馈")
                .build();
        TreeSelect select3 = TreeSelect.builder()
                .id(30)
                .label("社会投诉")
                .build();
        TreeSelect select4 = TreeSelect.builder()
                .id(40)
                .label("第三方反馈")
                .build();
        resultList.add(select1);
        resultList.add(select2);
        resultList.add(select3);
        resultList.add(select4);

        for (SelectVO selectVO : list) {
            Integer itemKey = Integer.parseInt(selectVO.getItemKey().toString()) ;
            TreeSelect node = TreeSelect.builder().id(itemKey).label(selectVO.getItemValue()).build();
            if (itemKey.intValue()>10 && itemKey.intValue()<20){
                select1.addChild(node);
            }
            if (itemKey.intValue()>20 && itemKey.intValue()<30){
                select2.addChild(node);
            }
            if (itemKey.intValue()>30 && itemKey.intValue()<40){
                select3.addChild(node);
            }
            if (itemKey.intValue()>40 && itemKey.intValue()<50){
                select4.addChild(node);
            }
        }
        return resultList;
    }

}
