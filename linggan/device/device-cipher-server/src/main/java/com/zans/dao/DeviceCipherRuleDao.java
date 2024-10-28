package com.zans.dao;

import com.zans.model.DeviceCipherRule;
import com.zans.vo.DeviceCipherRuleVO;
import org.apache.ibatis.annotations.Mapper;

/**
 * (DeviceCipherRule)表数据库访问层
 *
 * @author beixing
 * @since 2021-08-23 16:15:56
 */
@Mapper
public interface DeviceCipherRuleDao {

    DeviceCipherRule query();

    int updateRule(DeviceCipherRuleVO deviceCipherRuleVO);


}

