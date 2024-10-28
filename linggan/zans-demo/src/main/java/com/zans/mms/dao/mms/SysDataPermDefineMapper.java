package com.zans.mms.dao.mms;

import com.zans.mms.model.SysDataPermDefine;
import com.zans.mms.vo.perm.DataPermMenuVO;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

import java.util.HashMap;
import java.util.List;

/**
* @Title: SysDataPermDefineMapper
* @Description: 数据权限定义mapper
* @Version:1.0.0
* @Since:jdk1.8
* @author beiming
* @date 4/9/21
*/
@Repository
public interface SysDataPermDefineMapper extends Mapper<SysDataPermDefine> {
    List<DataPermMenuVO> getListByCondition(HashMap<String, Object> map);
}
