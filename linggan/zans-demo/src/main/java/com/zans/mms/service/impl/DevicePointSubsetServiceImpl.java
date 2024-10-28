package com.zans.mms.service.impl;


import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.zans.base.service.impl.BaseServiceImpl;
import com.zans.base.vo.ApiResult;
import com.zans.base.vo.PageResult;
import com.zans.base.vo.SelectVO;
import com.zans.mms.dao.mms.DevicePointSubsetDetailMapper;
import com.zans.mms.dao.mms.DevicePointSubsetMapper;
import com.zans.mms.model.DevicePointSubset;
import com.zans.mms.service.IDevicePointSubsetService;
import com.zans.mms.vo.asset.subset.AssetSubsetDetaiQueryReqVO;
import com.zans.mms.vo.devicepoint.DevicePointResVO;
import com.zans.mms.vo.devicepoint.subset.DevicePointSubsetQueryVO;
import com.zans.mms.vo.devicepoint.subset.DevicePointSubsetResVO;
import com.zans.mms.vo.devicepoint.subset.StatusCount;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

/**
 *  DevicePointSubsetServiceImpl
 *
 *  @author
 */
@Slf4j
@Service("devicePointSubsetService")
public class DevicePointSubsetServiceImpl extends BaseServiceImpl<DevicePointSubset> implements IDevicePointSubsetService {


	@Autowired
	private DevicePointSubsetMapper devicePointSubsetMapper;
	@Autowired
	private DevicePointSubsetDetailMapper devicePointSubsetDetailMapper;

	@Resource
	public void setDevicePointSubsetMapper(DevicePointSubsetMapper mapper) {
		super.setBaseMapper(mapper);
		this.devicePointSubsetMapper = mapper;
	}

	@Override
	public ApiResult getList(DevicePointSubsetQueryVO vo) {
		int pageNum = vo.getPageNum();
		int pageSize = vo.getPageSize();
		Page page = PageHelper.startPage(pageNum, pageSize);
		List<DevicePointSubsetResVO> result = devicePointSubsetMapper.getList(vo);
		//TODO  正常设备 停用设备 故障设备 拆除设备
		for (DevicePointSubsetResVO resVO : result) {

		 	List<StatusCount> counts = devicePointSubsetDetailMapper.getCountGroupByStatus(resVO.getSubsetId());
			for (StatusCount statusCount : counts) {
				if ("故障".equals(statusCount.getMaintainStatus())){
					resVO.setFaultCount(statusCount.getCountNum());
					continue;
				}
				if ("正常".equals(statusCount.getMaintainStatus())){
					resVO.setNormalCount(statusCount.getCountNum());
					continue;
				}
				if ("迁改停用".equals(statusCount.getMaintainStatus())){
					resVO.setStopCount(statusCount.getCountNum());
					continue;
				}
				if ("拆除停用".equals(statusCount.getMaintainStatus())){
					resVO.setDemolishCount(statusCount.getCountNum());
					continue;
				}

			}

		}

		return ApiResult.success(new PageResult<DevicePointSubsetResVO>(page.getTotal(), result,pageSize, pageNum));
	}

	@Override
	public Boolean existRelation(Long subsetId) {
		AssetSubsetDetaiQueryReqVO  reqVO = new AssetSubsetDetaiQueryReqVO();
		reqVO.setSubsetId(subsetId);
		List<DevicePointResVO> list = devicePointSubsetDetailMapper.groupList(reqVO);

		return !list.isEmpty() && list.size()>0;
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public int deleteById(Long subsetId) {
		int i = devicePointSubsetDetailMapper.deleteBySubsetId(subsetId);
		return devicePointSubsetMapper.deleteById(subsetId);
	}

	@Override
	public List<SelectVO> getSelectList() {
		return devicePointSubsetMapper.getSelectList();
	}
}
