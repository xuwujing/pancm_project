package com.zans.mms.dao.guard;


import com.zans.mms.model.RadiusAcct;
import com.zans.mms.vo.radius.AcctRespVO;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.Mapper;

public interface RadiusAcctMapper extends Mapper<RadiusAcct> {

	AcctRespVO findLatestAcctByMac(@Param("mac") String mac);

}
