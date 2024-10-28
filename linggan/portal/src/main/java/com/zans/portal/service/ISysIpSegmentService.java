package com.zans.portal.service;

import com.zans.base.service.BaseService;
import com.zans.base.vo.PageResult;
import com.zans.base.vo.UserSession;
import com.zans.portal.model.SysIpSegment;
import com.zans.portal.vo.segment.SegmentAddVO;
import com.zans.portal.vo.segment.SegmentEditVO;
import com.zans.portal.vo.segment.SegmentSearchVO;
import com.zans.portal.vo.segment.SegmentVO;

/**
 * @author xv
 * @since 2020/6/9 19:27
 */
public interface ISysIpSegmentService extends BaseService<SysIpSegment> {

    /**
     * IP分配，列表查询
     * @param reqVO 查询条件
     * @return 列表
     */
    PageResult<SegmentVO> getIpSegmentPage(SegmentSearchVO reqVO);

    /**
     * IP分配，查询单个记录
     * @param id 记录主键
     * @return 记录
     */
    SegmentVO getIpSegment(Integer id);

    SysIpSegment addSegment(SegmentAddVO addVO, UserSession session);

    void updateSegment(SegmentEditVO editVO, UserSession session);

    void deleteSegment(SysIpSegment segment, UserSession session);
}
