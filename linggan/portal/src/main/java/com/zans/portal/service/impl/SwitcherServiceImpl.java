package com.zans.portal.service.impl;

import java.io.BufferedReader;
import java.io.StringReader;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.Resource;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.zans.base.service.impl.BaseServiceImpl;
import com.zans.base.util.ArithmeticUtil;
import com.zans.base.vo.PageResult;
import com.zans.portal.dao.SysSwitcherMapper;
import com.zans.portal.model.SysSwitcher;
import com.zans.portal.model.SysSwitcherVlanConfig;
import com.zans.portal.service.IAreaNasService;
import com.zans.portal.service.ISwitcherService;
import com.zans.portal.service.ISysSwitcherVlanConfigService;
import com.zans.portal.util.IpUtil;
import com.zans.portal.vo.switcher.SwitchAreaStatisRespVO;
import com.zans.portal.vo.switcher.SwitchResVO;
import com.zans.portal.vo.switcher.SwitchSearchVO;
import com.zans.portal.vo.switcher.SwitchVlanSplitVO;
import com.zans.portal.vo.switcher.SwitcherProcessorVO;
import com.zans.portal.vo.switcher.SwitcherScanListRespVO;
import com.zans.portal.vo.switcher.SwitcherScanReqVO;
import com.zans.portal.vo.switcher.SwitcherScanRespVO;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.javatuples.Pair;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class SwitcherServiceImpl extends BaseServiceImpl<SysSwitcher> implements ISwitcherService {

    @Autowired
    IAreaNasService nasService;

    @Autowired
    ISysSwitcherVlanConfigService sysSwitcherVlanConfigService;

    SysSwitcherMapper switcherMapper;

    @Resource
    public void setSwitcherMapper(SysSwitcherMapper switcherMapper) {
        super.setBaseMapper(switcherMapper);
        this.switcherMapper = switcherMapper;
    }

    @Override
    public PageResult<SwitchResVO> getSwitchPage(SwitchSearchVO reqVO) {
        String orderBy = reqVO.getSortOrder();
        Page page = PageHelper.startPage(reqVO.getPageNum(), reqVO.getPageSize());

        List<SwitchResVO> list = switcherMapper.findSwitchList(reqVO);
        return new PageResult<SwitchResVO>(page.getTotal(), page.getResult(), reqVO.getPageSize(), reqVO.getPageNum());
    }

    @Override
    public SysSwitcher findBySwHost(String swHost, Integer id) {
        return switcherMapper.findBySwHost(swHost, id);
    }




    @Override
    public SwitcherScanRespVO getScanBySwId(SwitcherScanReqVO reqVO) {
        return switcherMapper.getScanBySwId(reqVO);
    }

    @Override
    public SwitcherScanRespVO getLastScanBySwId(String ipAddr) {
        return switcherMapper.getLastScanBySwId(ipAddr);
    }

    @Override
    public PageResult<SwitcherScanListRespVO> getSwitchScanPage(SwitcherScanReqVO reqVO) {
        String orderBy = reqVO.getSortOrder();
        Page page = PageHelper.startPage(reqVO.getPageNum(), reqVO.getPageSize());

        List<SwitcherScanListRespVO> list = switcherMapper.findSwitchScanList(reqVO);
        return new PageResult<SwitcherScanListRespVO>(page.getTotal(), page.getResult(), reqVO.getPageSize(),
                reqVO.getPageNum());
    }



    @Override
    public List<SwitcherScanListRespVO> findSwitchScanList(SwitcherScanReqVO reqVO) {
        return switcherMapper.findSwitchScan(reqVO);
    }

    @Override
    public List<SwitcherScanListRespVO> findSwitchScanDateHourList(SwitcherScanReqVO reqVO) {
        return switcherMapper.findSwitchDateHourScan(reqVO);
    }

    @Override
    public void splitVlanInfoFromConfig() {
        List<SwitchVlanSplitVO> switchConfigList = switcherMapper.findAllArpEnabledSwitchWithConfig();
        if (CollectionUtils.isNotEmpty(switchConfigList)) {
            sysSwitcherVlanConfigService.deleteAll();
            Pattern pattern = Pattern.compile("\\d+");
            Pattern ipPattern = Pattern.compile("\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}");
            for (SwitchVlanSplitVO vo : switchConfigList) {
                System.out.println(vo.getConfig());
                try (BufferedReader reader = new BufferedReader(new StringReader(vo.getConfig()))) {
                    String line = reader.readLine();
                    String vlanif = "";
                    while (line != null) {
                        line = line.trim();
                        if (line.contains("Vlanif")) {
                            vlanif = line.trim();
                        }
                        if (StringUtils.isNotBlank(vlanif)) {
                            if (line.contains("ip") && line.contains("address")) {
                                Matcher matcher = pattern.matcher(vlanif);
                                Matcher ipMatcher = ipPattern.matcher(line);
                                if (matcher.find() && ipMatcher.find()) {
                                    String vlan = matcher.group();
                                    String ip = ipMatcher.group();
                                    ipMatcher.find();
                                    String mask = ipMatcher.group();
                                    SysSwitcherVlanConfig vlanConfig = new SysSwitcherVlanConfig();
                                    BeanUtils.copyProperties(vo, vlanConfig);
                                    vlanConfig.setVlan(Integer.valueOf(vlan));
                                    vlanConfig.setVlanIpAddr(ip);
                                    vlanConfig.setVlanMask(mask);
                                    Pair<String, String> pair = IpUtil.calcIpSegment(ip, mask);
                                    vlanConfig.setVlanStartIpAddr(pair.getValue0());
                                    vlanConfig.setVlanLastIpAddr(pair.getValue1());
                                    // 判断是否存在
                                    if (!sysSwitcherVlanConfigService.existRecord(vlanConfig.getSwId(),
                                            vlanConfig.getVlan(), ip, mask)) {
                                        sysSwitcherVlanConfigService.saveSelective(vlanConfig);
                                    }
                                }
                            }
                        }
                        if (line.contains("#")) {
                            vlanif = "";
                        }
                        if (line.contains("return")) {
                            break;
                        }
                        line = reader.readLine();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

    }

    @Override
    public List<SysSwitcher> getListCondition(HashMap<String, Object> map) {
        return switcherMapper.getListCondition(map);
    }


}
