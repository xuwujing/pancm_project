package com.zans.portal.service.impl;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.google.common.collect.ImmutableMap;
import com.zans.base.exception.RollbackException;
import com.zans.base.service.impl.BaseServiceImpl;
import com.zans.base.util.MapBuilder;
import com.zans.base.util.StringHelper;
import com.zans.base.vo.ApiResult;
import com.zans.base.vo.PageResult;
import com.zans.base.vo.SelectVO;
import com.zans.base.vo.UserSession;
import com.zans.portal.config.GlobalConstants;
import com.zans.portal.dao.AreaMapper;
import com.zans.portal.dao.ArpMapper;
import com.zans.portal.dao.IpAllMapper;
import com.zans.portal.model.*;
import com.zans.portal.service.*;
import com.zans.portal.util.LogBuilder;
import com.zans.portal.vo.arp.*;
import com.zans.portal.vo.chart.*;
import com.zans.portal.vo.home.AssetStatsVO;
import com.zans.portal.vo.home.RegionRiskVO;
import com.zans.portal.vo.home.SecurityStatsVO;
import com.zans.portal.vo.ip.IpVO;
import com.zans.portal.vo.stats.ArpHourStatVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.javatuples.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import static com.zans.base.config.BaseConstants.CODE_SUCCESS;
import static com.zans.portal.config.GlobalConstants.*;

@Service
@Slf4j
public class ArpServiceImpl extends BaseServiceImpl<Arp> implements IArpService {

    ArpMapper arpMapper;



    @Autowired
    IConstantItemService constantItemService;

    @Autowired
    ILogOperationService logOperationService;

    @Autowired
    ISwitcherService switchService;

    @Autowired
    IAreaService areaService;

    @Autowired
    AreaMapper areaMapper;

    @Autowired
    IDeviceTypeService deviceTypeService;


    @Autowired
    IpAllMapper ipAllMapper;

    @Autowired
    IAssetService assetService;

    @Autowired
    private IRadiusEndPointService endPointService;

    @Autowired
    private IRadiusEndPointProfileService endPointProfileService;

    @Autowired
    ISwitcherService switcherService;


    @Resource
    public void setArpMapper(ArpMapper arpMapper) {
        super.setBaseMapper(arpMapper);
        this.arpMapper = arpMapper;
    }




    private static int RISK_MAX_LEVEL = 5000;

    private static int[][] RISK_INDEX_RANGE = {
            {0, 1000, 0},
            {1000, 2000, 1},
            {2000, 3000, 2},
            {3000, RISK_MAX_LEVEL, 3}
    };

    private static Map<Integer, Double> riskWeight = ImmutableMap.of(
            RISK_PRIVATE, 1.0d,
            RISK_FORGE, 1.0d,
            RISK_REPLACE, 1.0d,
            RISK_EXCEPTION, 0.1d
    );

    /**
     * 计算安全指数
     * 0-1000： 4.0 ~ 5.0
     * 1000 ~ 2000， 3.0 - 4.0
     * 2000 ~ 3000， 2.0 ~ 3.0
     * 3000 ~ 5000， 1.0 ~ 2.0
     * 5000+, 0 ~ 1.0
     *
     * @param riskCount
     * @return
     */
    private double computeSafeIndex(Integer riskCount) {
        double max = 5.0;
        if (riskCount == null || riskCount < 0) {
            return max;
        } else if (riskCount == RISK_MAX_LEVEL) {
            return max - RISK_INDEX_RANGE.length;
        }
        if (riskCount > RISK_MAX_LEVEL) {
            double relative = Math.log10(new Double(riskCount - RISK_MAX_LEVEL));
            int length = String.valueOf(Integer.MAX_VALUE).length() + 1;
            return doubleFormat(max - RISK_INDEX_RANGE.length - relative / length);
        } else {
            for (int[] array : RISK_INDEX_RANGE) {
                if (riskCount >= array[0] && riskCount < array[1]) {
                    int relative = riskCount - array[0];
                    int denominator = array[1] - array[0];
                    return doubleFormat(max - array[2] - (new Double(relative) / denominator));
                }
            }
        }
        max = max * 20;
        return max;
    }

    private static double doubleFormat(double input) {
        BigDecimal decimal = new BigDecimal(input).setScale(2, RoundingMode.HALF_UP);
        return decimal.doubleValue();
    }


    private void dealSwitcherTopo(List<NetworkSwitchVO> netWork, String swIp,List<String> rootIps,AtomicInteger depth) {
        int i = depth.incrementAndGet();
        if (rootIps.contains(swIp) || i >9){
            return;
        }
        NetworkSwitchVO vo =  arpMapper.findSwitcherTopo(swIp);
        netWork.add(vo);
        dealSwitcherTopo(netWork,vo.getSwIp(),rootIps,depth);
    }


    public static Date getDateAdd(int days) {
        Calendar c = Calendar.getInstance();
        c.add(Calendar.DAY_OF_MONTH, -days);
        return c.getTime();
    }

    private static List<String> getDaysBetwwen(int days) {
        List<String> dayss = new ArrayList<>();
        Calendar start = Calendar.getInstance();
        start.setTime(getDateAdd(days));
        Long startTIme = start.getTimeInMillis();
        Calendar end = Calendar.getInstance();
        end.setTime(new Date());
        Long endTime = end.getTimeInMillis();
        Long oneDay = 1000 * 60 * 60 * 24L;
        Long time = startTIme;
        while (time <= endTime) {
            Date d = new Date(time);
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            dayss.add(df.format(d));
            time += oneDay;
        }
        return dayss;
    }





}
