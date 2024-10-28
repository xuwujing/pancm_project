package com.zans.service;

import com.zans.model.DeviceCipherRule;
import com.zans.vo.ApiResult;
import com.zans.vo.PwdRuleVO;

/**
 * (DeviceCipherRule)表服务接口
 *
 * @author beixing
 * @since 2021-08-23 16:15:56
 */
public interface IDeviceCipherRuleService {

    /**
     * 通过ID查询单条数据
     *
     * @param 
     * @return 实例对象
     */
    DeviceCipherRule queryById();

    ApiResult resetPdw(PwdRuleVO pwdRuleVO);

}
