package com.zans.portal.dao;

import com.zans.portal.model.Asset;
import com.zans.portal.model.NetworkIpSegment;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author qitian
 * @Title: portal
 * @Description:ip地址池持久层
 * @Version:1.0.0
 * @Since:jdk1.8
 * @date 2021/6/23
 */

@Repository
public interface NetworkIpSegmentMapper {
	List<NetworkIpSegment> list();

	List<String> getUsedIp(@Param("ip")String ip);

	List<Asset> getAsset(@Param("ip") String ip,@Param("alive") Integer alive);
}
