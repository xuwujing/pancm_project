package com.zans.portal.service.impl;

import com.zans.base.vo.ApiResult;
import com.zans.portal.dao.NetworkIpSegmentMapper;
import com.zans.portal.model.Asset;
import com.zans.portal.model.NetworkIpSegment;
import com.zans.portal.service.INetworkIpSegmentService;
import com.zans.portal.vo.segment.NetworkIPSegmentVO;
import com.zans.portal.vo.segment.NetworkIpSegmentViewVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * @author qitian
 * @Title: portal
 * @Description:ip地址池业务层实现类
 * @Version:1.0.0
 * @Since:jdk1.8
 * @date 2021/6/23
 */
@Slf4j
@Service
public class NetworkIpSegmentServiceImpl implements INetworkIpSegmentService {

	@Resource
	NetworkIpSegmentMapper networkIpSegmentMapper;


	@Override
	public ApiResult ipSegmentList() {
		//查询network_ip_segement的所有数据
		List<NetworkIpSegment> networkIpSegmentList = networkIpSegmentMapper.list();
		if(null != networkIpSegmentList && networkIpSegmentList.size()>0){
			for(NetworkIpSegment networkIpSegment : networkIpSegmentList){
				String[] ipBegin = networkIpSegment.getIpBegin().split("\\.");
				String[] ipEnd = networkIpSegment.getIpEnd().split("\\.");
				List<String> ipList = new ArrayList<>();
				for(int i=Integer.parseInt(ipBegin[2]);i<Integer.parseInt(ipEnd[2]);i++){
					String ip=ipBegin[0]+"."+ipBegin[1]+"."+i+".0/24";
					ipList.add(ip);
				}
				networkIpSegment.setCLevelIp(ipList);
			}
		}
		return ApiResult.success(networkIpSegmentList);
	}

	/**
	 * 每个地址段对应的信息
	 * @param ip
	 * @return
	 */
	@Override
	public ApiResult ipSegmentView(String ip) {
		NetworkIpSegmentViewVO networkIpSegmentViewVO = new NetworkIpSegmentViewVO();

		String[] ipSplit=ip.split("\\.");
		String ipBegin=ipSplit[0]+"."+ipSplit[1]+".0.0";
		String ipEnd=ipSplit[0]+"."+ipSplit[1]+".254.254";
		networkIpSegmentViewVO.setIpGroup(ipBegin+"/"+ipEnd);
		networkIpSegmentViewVO.setIpSegment(ip);
		//查询有设备类型的ip地址 并记录ip 现共有ip254个 1-254
		//先从t_ip_all表中查询占用的具体ip
		String[] ipString=ip.split("/")[0].split("\\.");
		ip=ipString[0]+"."+ipString[1]+"."+ipString[2];
		int onlineNum = 0;
		int offlineNum=0;
		int usedIpNum=0;
		List<String> usedIpList=networkIpSegmentMapper.getUsedIp(ip);
		List<Asset> onlineAssetList = networkIpSegmentMapper.getAsset(ip,1);
		List<Asset> offlineAssetList = networkIpSegmentMapper.getAsset(ip,2);
		if(null!=usedIpList&&usedIpList.size()>0){
			usedIpNum=usedIpList.size();
		}
		if(null!=onlineAssetList&&onlineAssetList.size()>0){
			onlineNum=onlineAssetList.size();
		}
		if(null!=offlineAssetList&&offlineAssetList.size()>0){
			offlineNum=offlineAssetList.size();
		}
		networkIpSegmentViewVO.setOnlineIpNum(onlineNum);
		networkIpSegmentViewVO.setOfflineIpNum(offlineNum);
		networkIpSegmentViewVO.setAssignedIpNum(usedIpNum);
		networkIpSegmentViewVO.setNotUsedIpNum(254-onlineNum-offlineNum-usedIpNum);
		List<NetworkIPSegmentVO> networkIPSegmentVOList = new ArrayList<>();
		for(int i =0;i<254;i++){
			NetworkIPSegmentVO networkIPSegmentVO = new NetworkIPSegmentVO();
			for(String userIp :usedIpList){
				if(i==Integer.parseInt(userIp.split("\\.")[3])-1){
					networkIPSegmentVO.setUsed(1);
					networkIPSegmentVO.setIp(userIp);
					networkIPSegmentVO.setDeviceTypeName("未知");
					break;
				}
			}
			for(Asset asset :onlineAssetList){
				if(i==Integer.parseInt(asset.getIpAddr().split("\\.")[3])-1){
					networkIPSegmentVO.setUsed(1);
					networkIPSegmentVO.setDeviceType(Integer.valueOf(asset.getDeviceType()));
					networkIPSegmentVO.setIp(asset.getIpAddr());
					networkIPSegmentVO.setDeviceTypeName(asset.getDeviceModelBrand());
					break;
				}
			}
			for(Asset asset :offlineAssetList){
				if(i==Integer.parseInt(asset.getIpAddr().split("\\.")[3])-1){
					networkIPSegmentVO.setUsed(1);
					networkIPSegmentVO.setDeviceType(Integer.valueOf(asset.getDeviceType()));
					networkIPSegmentVO.setIp(asset.getIpAddr());
					networkIPSegmentVO.setDeviceTypeName(asset.getDeviceModelBrand());
					break;
				}
			}
			networkIPSegmentVOList.add(networkIPSegmentVO);
		}
		networkIpSegmentViewVO.setNetworkIPSegmentVOList(networkIPSegmentVOList);
		return ApiResult.success(networkIpSegmentViewVO);
	}
}
