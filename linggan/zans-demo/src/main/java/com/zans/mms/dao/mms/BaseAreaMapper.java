package com.zans.mms.dao.mms;

import com.zans.base.vo.SelectVO;
import com.zans.mms.model.BaseArea;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

@Repository
public interface BaseAreaMapper extends Mapper<BaseArea> {

    List<BaseArea> getFindAll();

    List<SelectVO> findAreaToSelect();

    String getAreaIdByName(String areaName);

    /**
    * @Author beiming
    * @Description  获取一级辖区
    * @Date  4/22/21
    * @Param
    * @return
    **/
    List<BaseArea> getAreaLevelOne();

}
