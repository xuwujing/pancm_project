package com.zans.mms.service;

import com.zans.base.service.BaseService;
import com.zans.mms.model.SysDataPermDefine;
import com.zans.mms.vo.perm.DataPermMenuVO;

import java.util.HashMap;
import java.util.List;

/**
* @Title: ISysDataPermDefineService
* @Description: 数据权限定义Service
* @Version:1.0.0
* @Since:jdk1.8
* @author beiming
* @date 4/9/21
*/
public interface ISysDataPermDefineService extends BaseService<SysDataPermDefine>{


    List<DataPermMenuVO> getListByCondition(HashMap<String, Object> permId);
}