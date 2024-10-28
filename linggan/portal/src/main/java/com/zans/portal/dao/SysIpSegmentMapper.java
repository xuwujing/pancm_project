package com.zans.portal.dao;

import com.zans.portal.model.SysIpSegment;
import com.zans.portal.vo.segment.SegmentSearchVO;
import com.zans.portal.vo.segment.SegmentVO;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface SysIpSegmentMapper extends Mapper<SysIpSegment> {

    List<SegmentVO> findAllIpSegment(@Param("ip") SegmentSearchVO ip);

    SegmentVO findIpSegment(@Param("id") Integer id);
}
