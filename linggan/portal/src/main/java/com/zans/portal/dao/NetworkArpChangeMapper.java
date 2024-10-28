package com.zans.portal.dao;

import com.zans.portal.vo.network.NetworkArpChangeRespVO;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author pancm
 * @Title: portal
 * @Description:
 * @Version:1.0.0
 * @Since:jdk1.8
 * @date 2020/10/26
 */
@Repository
public interface NetworkArpChangeMapper {

    NetworkArpChangeRespVO findNetworkArpChangeOldByIp(String ip);

    NetworkArpChangeRespVO findNetworkArpChangeCurByIp(String ip);


    List<NetworkArpChangeRespVO> findNetworkArpChangeByIp(String ip);
}
