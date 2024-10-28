package com.zans.mms.service;

import com.zans.base.service.BaseService;
import com.zans.base.vo.SelectVO;
import com.zans.mms.model.BaseArea;
import com.zans.mms.vo.common.TreeSelect;

import java.util.List;
import java.util.Map;

/**
 * (BaseArea)表服务接口
 *
 * @author makejava
 * @since 2021-01-12 15:20:07
 */
public interface IBaseAreaService extends BaseService<BaseArea> {


    List<TreeSelect> areaTreeList();

    List<SelectVO> areaList();

    List<SelectVO> findAreaToSelect();
    Map<Object, String> findAreaToMap();

    /**
    * @Author beiming
    * @Description  辖区树-大辖区
    * @Date  4/22/21
    * @Param
    * @return
    **/
    List<TreeSelect> allAreaTreeList();
}
