package com.zans.mms.dao;

import com.zans.mms.model.CommonLabel;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

/**
 * @author qitian
 * @Title: zans-mms-server
 * @Description:
 * @Version:1.0.0
 * @Since:jdk1.8
 * @date 2021/8/31
 */

@Repository
public interface CommonLabelDao extends Mapper<CommonLabel> {

}

