package com.zans.mms.dao;

import com.zans.mms.model.PatrolScheme;
import com.zans.mms.vo.patrol.PatrolSchemeDetailResVO;
import com.zans.mms.vo.patrol.PatrolSchemeQueryVO;
import com.zans.mms.vo.patrol.PatrolSchemeResVO;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

@Repository
public interface PatrolSchemeMapper extends Mapper<PatrolScheme> {
    List<PatrolSchemeResVO> getList(PatrolSchemeQueryVO vo);

    PatrolSchemeDetailResVO getViewById(Long id);

    int deleteByUniqueId(Long id);

    int deleteById(Long id);
}