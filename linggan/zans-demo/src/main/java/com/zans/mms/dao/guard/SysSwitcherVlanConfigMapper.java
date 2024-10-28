package com.zans.mms.dao.guard;


import com.zans.mms.model.SysSwitcherVlanConfig;
import com.zans.mms.vo.switcher.SwitcherVlanConfigRespVO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

@Repository
public interface SysSwitcherVlanConfigMapper extends Mapper<SysSwitcherVlanConfig> {

	List<SwitcherVlanConfigRespVO> findIpMatchVlanList(@Param("ip") String ip);
}