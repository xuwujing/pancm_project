package com.zans.mms.dao.guard;


import com.zans.mms.model.SysSwitcherBranch;
import com.zans.mms.vo.switcher.SwitchBranchResVO;
import com.zans.mms.vo.switcher.SwitchBranchSearchVO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;


@Repository
public interface SysSwitcherBranchMapper extends Mapper<SysSwitcherBranch> {


	List<SwitchBranchResVO> findSwitchList(@Param("reqVo") SwitchBranchSearchVO searchVO);

}
