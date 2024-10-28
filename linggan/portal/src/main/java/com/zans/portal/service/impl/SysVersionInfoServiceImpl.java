package com.zans.portal.service.impl;

import com.alibaba.fastjson.JSON;
import com.zans.base.vo.ApiResult;
import com.zans.portal.dao.SysVersionInfoDao;
import com.zans.portal.service.ISysVersionInfoService;
import com.zans.portal.util.RestTemplateHelper;
import com.zans.portal.vo.version.SysVersionInfoRepVO;
import com.zans.portal.vo.version.SysVersionInfoVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;


/**
 * @author beixing
 * @Title: (SysVersionInfo)表服务实现类
 * @Description:
 * @Version:1.0.0
 * @Since:jdk1.8
 * @date 2021-06-23 14:25:23
 */
@Service("sysVersionInfoService")
@Slf4j
public class SysVersionInfoServiceImpl implements ISysVersionInfoService {
    @Resource
    private SysVersionInfoDao sysVersionInfoDao;

    @Autowired
    RestTemplateHelper restTemplateHelper;

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    @Override
    public SysVersionInfoVO queryById(Long id) {
        return this.sysVersionInfoDao.queryById(id);
    }


    /**
     * 根据条件查询
     *
     * @return 实例对象的集合
     */
    @Override
    public ApiResult list() {
        List<SysVersionInfoVO> result = sysVersionInfoDao.queryAll(new SysVersionInfoVO());
        List<SysVersionInfoRepVO> versionInfoRepVOList = new ArrayList<>();
        for (SysVersionInfoVO sysVersionInfoVO : result) {
            SysVersionInfoRepVO sysVersionInfoRepVO = new SysVersionInfoRepVO();
            try {
                String data = restTemplateHelper.getForStr(sysVersionInfoVO.getServerUrl());
                if(data==null){
                        continue;
                }
                sysVersionInfoRepVO = JSON.parseObject(data, SysVersionInfoRepVO.class);

            } catch (Exception e) {
                log.error("调用远程接口失败！远程url:{}",sysVersionInfoVO.getServerUrl());
                continue;
            }
            sysVersionInfoRepVO.setServerIp(sysVersionInfoVO.getServerIp());
            sysVersionInfoRepVO.setServerUrl(sysVersionInfoVO.getServerUrl());
            versionInfoRepVOList.add(sysVersionInfoRepVO);
        }
        return ApiResult.success(versionInfoRepVOList);
    }


}
