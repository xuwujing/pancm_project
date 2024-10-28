package com.zans.controller;

import com.zans.service.IDeviceCipherRuleService;
import com.zans.vo.ApiResult;
import com.zans.vo.PwdRuleVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;


/**
 * (DeviceCipherRule)表控制层
 *
 * @author beixing
 * @since 2021-08-23 16:15:56
 */
@RestController
@RequestMapping("deviceCipherRule")
public class DeviceCipherRuleController {
    /**
     * 服务对象
     */
    @Autowired
    private IDeviceCipherRuleService deviceCipherRuleService;


    /**
     * 重新设置密码规则
     * @param pwdRuleVO
     * @return
     */
    @RequestMapping(value = "resetPwd",method = RequestMethod.POST)
    public ApiResult resetPwd(@RequestBody PwdRuleVO pwdRuleVO){
        return deviceCipherRuleService.resetPdw(pwdRuleVO);
    }



}
