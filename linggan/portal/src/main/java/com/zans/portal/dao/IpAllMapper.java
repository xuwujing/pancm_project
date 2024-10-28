package com.zans.portal.dao;

import com.zans.portal.model.IpAll;
import com.zans.portal.vo.arp.ArpSwitchVO;
import com.zans.portal.vo.chart.CircleUnit;
import com.zans.portal.vo.chart.CountUnit;
import com.zans.portal.vo.ip.IpSearchVO;
import com.zans.portal.vo.ip.IpVO;
import com.zans.portal.vo.radius.QzViewRespVO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;
import java.util.Map;

@Repository
public interface IpAllMapper extends Mapper<IpAll> {

    List<IpVO> findAllIp(@Param("ip") IpSearchVO ip);

    IpVO getIp(@Param("id") Integer id);




    List<CountUnit> getGroupByBrand();

    List<CountUnit> getGroupByDeviceType();

    Integer getTotolCount();

    /**
     * 查询上次分配的最大IP
     * 用 inet_aton(ip_addr)，按 10 进制排序
     *
     * @param area       区域
     * @param deviceType 设备类型
     * @return
     */
    IpVO findLastAssignIp(@Param("area") Integer area, @Param("deviceType") Integer deviceType);

    /**
     * 默认的最大IP
     * @param area
     * @param deviceType
     * @return
     */
    IpVO findDefaultAssignIp(@Param("area") Integer area, @Param("deviceType") Integer deviceType);

    int deleteIpByAlloc(@Param("allocId") Integer allocId);


    QzViewRespVO findByIp(@Param("ip") String ip);

    List<CircleUnit> networkin();

    List<CountUnit> modules();

    List<IpAll> getListForConvert();


    //2020-9-18 更改查询
    ArpSwitchVO getByMacAdress(@Param("macAdress") String macAdress);

    List<IpAll> selectByCondition(Map<String, Object> paramMap);
}
