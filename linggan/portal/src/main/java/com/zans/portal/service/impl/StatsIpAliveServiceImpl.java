package com.zans.portal.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.zans.base.service.impl.BaseServiceImpl;
import com.zans.base.vo.PageResult;
import com.zans.portal.dao.StatsIpAliveMapper;
import com.zans.portal.model.StatsIpAlive;
import com.zans.portal.service.IStatsIpAliveService;
import com.zans.portal.vo.stats.IpAliveResponseVO;
import com.zans.portal.vo.stats.IpAliveSearchVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * @author xv
 * @since 2020/4/17 12:02
 */
@Service
@Slf4j
public class StatsIpAliveServiceImpl extends BaseServiceImpl<StatsIpAlive> implements IStatsIpAliveService {

    StatsIpAliveMapper aliveMapper;

    @Resource
    public void setAliveMapper(StatsIpAliveMapper aliveMapper) {
        super.setBaseMapper(aliveMapper);
        this.aliveMapper = aliveMapper;
    }

    @Override
    public PageResult<IpAliveResponseVO> getIpAlivePage(IpAliveSearchVO reqVO, Date[] timeInterval) {
        String orderBy = reqVO.getSortOrder();
        Page page = PageHelper.startPage(reqVO.getPageNum(), reqVO.getPageSize());

        List<IpAliveResponseVO> list = aliveMapper.findIpAliveList(reqVO, timeInterval[0], timeInterval[1]);
        return new PageResult<IpAliveResponseVO>(page.getTotal(), page.getResult(), reqVO.getPageSize(), reqVO.getPageNum());
    }

    @Override
    public Double getPacket(String ip, Date start, Date end, Integer type) {
        if (type == 0) {
            return aliveMapper.findPacketLoss(ip, start, end);
        } else {
            return aliveMapper.findPacketTime(ip, start, end);
        }
    }

}
