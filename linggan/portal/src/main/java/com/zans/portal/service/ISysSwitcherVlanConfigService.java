package com.zans.portal.service;

import java.util.List;

import com.zans.base.service.BaseService;
import com.zans.portal.model.SysSwitcherVlanConfig;
import com.zans.portal.vo.switcher.SwitcherVlanConfigRespVO;

public interface ISysSwitcherVlanConfigService extends BaseService<SysSwitcherVlanConfig> {

    void deleteAll();
    
    /**
     * 判断是否存在记录
     * @param swId
     * @param vlan
     * @param ip
     * @param mask
     * @return
     */
    boolean existRecord(int swId,int vlan,String ip,String mask);

    /**
     * 根据ip查找所对应的交换机配置的VLAN信息
     * @param ip
     * @return
     */
    List<SwitcherVlanConfigRespVO> getIpMatchVlanList(String ip);
}
