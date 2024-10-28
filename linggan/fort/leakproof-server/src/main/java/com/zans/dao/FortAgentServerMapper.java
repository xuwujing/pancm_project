package com.zans.dao;

import com.zans.model.FortAgentServer;
import tk.mybatis.mapper.common.Mapper;

@org.apache.ibatis.annotations.Mapper
public interface FortAgentServerMapper extends Mapper<FortAgentServer> {

    FortAgentServer queryByIp(String serverIp);

}