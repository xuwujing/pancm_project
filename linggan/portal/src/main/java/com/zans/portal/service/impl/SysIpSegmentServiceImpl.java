package com.zans.portal.service.impl;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.zans.base.exception.RollbackException;
import com.zans.base.service.impl.BaseServiceImpl;
import com.zans.base.util.IpHelper;
import com.zans.base.vo.ApiResult;
import com.zans.base.vo.PageResult;
import com.zans.base.vo.UserSession;
import com.zans.portal.dao.SysIpSegmentMapper;
import com.zans.portal.model.LogOperation;
import com.zans.portal.model.SysIpSegment;
import com.zans.portal.service.IAreaService;
import com.zans.portal.service.IConstantItemService;
import com.zans.portal.service.ILogOperationService;
import com.zans.portal.service.ISysIpSegmentService;
import com.zans.portal.util.LogBuilder;
import com.zans.portal.vo.segment.SegmentAddVO;
import com.zans.portal.vo.segment.SegmentEditVO;
import com.zans.portal.vo.segment.SegmentSearchVO;
import com.zans.portal.vo.segment.SegmentVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

import static com.zans.portal.config.GlobalConstants.*;
import static com.zans.portal.config.GlobalConstants.LOG_RESULT_SUCCESS;

/**
 * @author xv
 * @since 2020/6/9 19:28
 */
@Slf4j
@Service
public class SysIpSegmentServiceImpl extends BaseServiceImpl<SysIpSegment> implements ISysIpSegmentService {

    @Autowired
    IConstantItemService constantItemService;

    @Autowired
    IAreaService areaService;

    @Autowired
    ILogOperationService logOperationService;

    SysIpSegmentMapper ipSegmentMapper;

    @Resource
    public void setIpSegmentMapper(SysIpSegmentMapper ipSegmentMapper) {
        super.setBaseMapper(ipSegmentMapper);
        this.ipSegmentMapper = ipSegmentMapper;
    }

    @Override
    public PageResult<SegmentVO> getIpSegmentPage(SegmentSearchVO req) {
        int pageNum = req.getPageNum();
        int pageSize = req.getPageSize();
        String orderBy = req.getSortOrder();

        Page page = PageHelper.startPage(pageNum, pageSize, orderBy);
        List<SegmentVO> list = ipSegmentMapper.findAllIpSegment(req);
        Map<Object, String> enableMap = constantItemService.findItemsMapByDict(MODULE_ENABLE_STATUS);
        for (SegmentVO ip : list) {
            ip.setEnableNameByMap(enableMap);
        }
        return new PageResult<SegmentVO>(page.getTotal(), page.getResult(), pageSize, pageNum);
    }

    @Override
    public SegmentVO getIpSegment(Integer id) {
        SegmentVO vo = ipSegmentMapper.findIpSegment(id);
        Map<Object, String> enableMap = constantItemService.findItemsMapByDict(MODULE_ENABLE_STATUS);
        if (vo != null) {
            vo.setEnableNameByMap(enableMap);
        }
        return vo;
    }

    @Override
    @Transactional(rollbackFor = RollbackException.class)
    public SysIpSegment addSegment(SegmentAddVO addVO, UserSession session) {
        SysIpSegment segmentEntity = new SysIpSegment();
        BeanUtils.copyProperties(addVO, segmentEntity);
        String seg = "";
        String[] segments = addVO.getSegment();
        for (String segment : segments) {
            seg = seg + segment + ",";
        }
        seg = seg.substring(0, seg.length() - 1);
        segmentEntity.setSegment(seg);
        this.save(segmentEntity);



        return segmentEntity;
    }

    @Override
    @Transactional(rollbackFor = RollbackException.class)
    public void updateSegment(SegmentEditVO editVO, UserSession session) {
        SysIpSegment item = this.getById(editVO.getId());
        BeanUtils.copyProperties(editVO, item);
        String seg = "";
        String[] segments = editVO.getSegment();
        for (String segment : segments) {
            seg = seg + segment + ",";
        }
        seg = seg.substring(0, seg.length() - 1);
        item.setSegment(seg);
        item.setIpCount(segments.length);
        this.update(item);


    }

    @Override
    @Transactional(rollbackFor = RollbackException.class)
    public void deleteSegment(SysIpSegment segment, UserSession session) {
        String json = JSON.toJSONString(segment);
        this.delete(segment);

    }
}
