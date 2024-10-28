package com.zans.mms.service.impl;


import com.zans.base.service.impl.BaseServiceImpl;
import com.zans.mms.dao.SysDataPermDefineMapper;
import com.zans.mms.model.SysDataPermDefine;
import com.zans.mms.service.ISysDataPermDefineService;
import com.zans.mms.vo.perm.DataPermMenuVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;

/**
* @Title: SysDataPermDefineServiceImpl
* @Description: 数据权限定义ServiceImpl
* @Version:1.0.0
* @Since:jdk1.8
* @author beiming
* @date 4/9/21
*/
@Slf4j
@Service("sysDataPermDefineService")
public class SysDataPermDefineServiceImpl extends BaseServiceImpl<SysDataPermDefine> implements ISysDataPermDefineService {
		
		
	@Autowired
	private SysDataPermDefineMapper sysDataPermDefineMapper;

    @Resource
    public void setSysDataPermDefineMapper(SysDataPermDefineMapper baseMapper) {
        super.setBaseMapper(baseMapper);
        this.sysDataPermDefineMapper = baseMapper;
    }


    @Override
    public List<DataPermMenuVO> getListByCondition(HashMap<String, Object> map) {
        return sysDataPermDefineMapper.getListByCondition(map);
    }
}