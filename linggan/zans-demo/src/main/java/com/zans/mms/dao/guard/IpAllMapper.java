package com.zans.mms.dao.guard;


import com.zans.mms.model.IpAll;
import com.zans.mms.vo.radius.QzViewRespVO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;
import java.util.Map;

@Repository
public interface IpAllMapper extends Mapper<IpAll> {

    QzViewRespVO findByIp(@Param("ip") String ip);
}