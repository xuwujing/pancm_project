package com.zans.portal.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.zans.base.service.impl.BaseServiceImpl;
import com.zans.base.util.StringHelper;
import com.zans.base.vo.PageResult;
import com.zans.base.vo.SelectVO;
import com.zans.portal.dao.DeviceTypeMapper;
import com.zans.portal.model.DeviceType;
import com.zans.portal.service.IDeviceTypeService;
import com.zans.portal.vo.common.TreeSelect;
import com.zans.portal.vo.device.DeviceTypeResVO;
import com.zans.portal.vo.device.DeviceTypeSearchVO;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.zans.base.config.BaseConstants.SEPARATOR_SPACE;

/**
 * @author xv
 */
@Service
public class DeviceTypeServiceImpl extends BaseServiceImpl<DeviceType> implements IDeviceTypeService {

    DeviceTypeMapper deviceTypeMapper;

    @Resource
    public void setDeviceTypeMapper(DeviceTypeMapper deviceTypeMapper) {
        super.setBaseMapper(deviceTypeMapper);
        this.deviceTypeMapper = deviceTypeMapper;
    }

    @Override
    @Cacheable(cacheNames = "DEVICE_TYPE_LIST")
    public List<SelectVO> findDeviceTypeToSelect() {
        return deviceTypeMapper.findDeviceTypeToSelect();
    }

    @Override
    public List<SelectVO> findMmsDeviceTypeToSelect() {
        return deviceTypeMapper.findMmsDeviceTypeToSelect();
    }

    @Override
    @Cacheable(cacheNames = "DEVICE_TYPE_MAP")
    public Map<Object, String> findDeviceTypeToMap() {
        List<SelectVO> selectList = this.findDeviceTypeToSelect();
        return selectList.stream().collect(Collectors.toMap(SelectVO::getItemKey, SelectVO::getItemValue));
    }

    @Override
    public String getNameByType(String type) {
        return this.findDeviceTypeToMap().get(type);
    }

    @Override
    @Cacheable(cacheNames = "DEVICE_TYPE_TEMPLATE_LIST")
    public List<SelectVO> findDeviceTypeHasTemplateToSelect() {
        return deviceTypeMapper.findDeviceTypeHasTemplateToSelect();
    }

    @Override
    public String getSupportDeviceType() {
        List<SelectVO> list = this.findDeviceTypeHasTemplateToSelect();
        List<Object> supportDevice = new ArrayList<>(list.size());
        for (SelectVO vo : list) {
            supportDevice.add(vo.getItemValue());
        }
        return StringHelper.joinCollection(supportDevice, SEPARATOR_SPACE);
    }

    @Override
    public Integer findTypeByName(String name) {
        return deviceTypeMapper.findTypeByName(name);
    }


    @Override
    public PageResult<DeviceTypeResVO> getDeviceTypePage(DeviceTypeSearchVO reqVO) {
        String orderBy = reqVO.getSortOrder();
        Page page = PageHelper.startPage(reqVO.getPageNum(), reqVO.getPageSize());

        List<DeviceTypeResVO> list = deviceTypeMapper.findDeviceTypeList(reqVO);
        return new PageResult<DeviceTypeResVO>(page.getTotal(), page.getResult(), reqVO.getPageSize(), reqVO.getPageNum());
    }

    @Override
    public List<TreeSelect> deviceTypeTreeList() {
        List<SelectVO> list = deviceTypeMapper.findMmsDeviceTypeToSelect();
        List<TreeSelect> resultList = new ArrayList<>(list.size());
        // 初始化非叶子节点
        Map<String, TreeSelect> treeMap = new HashMap<>(list.size());
        for (SelectVO selectVO : list) {
            TreeSelect node = TreeSelect.builder().id(selectVO.getItemKey()).label(selectVO.getItemValue())
                    .children(new ArrayList<>())
                    .build();
            treeMap.put((String) selectVO.getItemKey(), node);
            String key = (String) selectVO.getItemKey();
            if (key.length() ==2) {
                resultList.add(node);
            } else {
                TreeSelect parent = treeMap.get(key.substring(0,2));
                if (parent == null) {
                    resultList.add(node);
                } else {
                    parent.addChild(node);
                }
            }
        }
        return resultList;

    }

}
