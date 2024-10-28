package com.zans.mms.service.impl;

import com.zans.base.exception.BusinessException;
import com.zans.base.vo.ApiResult;
import com.zans.mms.dao.CommonLabelDao;
import com.zans.mms.model.CommonLabel;
import com.zans.mms.service.ICommonLabelService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author qitian
 * @Title: zans-mms-server
 * @Description:
 * @Version:1.0.0
 * @Since:jdk1.8
 * @date 2021/8/31
 */
@Service("commonLabelService")
@Slf4j
public class CommonLabelServiceImpl implements ICommonLabelService {

	@Resource
	private CommonLabelDao commonLabelDao;

	@Override
	public ApiResult create(CommonLabel commonLabel) {
		if(StringUtils.isEmpty(commonLabel.getModule())){
			throw new BusinessException("模块传参为空！");
		}
		//查询是否存在此标签
		Example example = new Example(CommonLabel.class);
		example.createCriteria().andEqualTo("creator",commonLabel.getCreator())
				.andEqualTo("module",commonLabel.getModule())
				.andEqualTo("value",commonLabel.getValue());
		int i = commonLabelDao.selectCountByExample(example);
		Example example2 = new Example(CommonLabel.class);
		example2.createCriteria().andEqualTo("creator","lgwy")
				.andEqualTo("module",commonLabel.getModule())
				.andEqualTo("value",commonLabel.getValue());
		int j = commonLabelDao.selectCountByExample(example2);
		int k=i+j;
		if(k==0){
			commonLabelDao.insertSelective(commonLabel);
			CommonLabel result = commonLabelDao.selectByPrimaryKey(commonLabel.getId());
			return ApiResult.success(result);
		}else {
			return ApiResult.success("标签已存在，请勿重复添加！");
		}
	}

	@Override
	public ApiResult update(CommonLabel commonLabel) {
		commonLabelDao.updateByPrimaryKeySelective(commonLabel);
		return ApiResult.success();
	}

	@Override
	public ApiResult delete(CommonLabel commonLabel) {
		commonLabelDao.deleteByPrimaryKey(commonLabel.getId());
		return ApiResult.success();
	}

	@Override
	public ApiResult list(CommonLabel commonLabel) {
		if(StringUtils.isEmpty(commonLabel.getModule())){
			throw new BusinessException("模块传参为空！");
		}
		//先查询配置部分
		Example systemDataExample = new Example(CommonLabel.class);
		systemDataExample.createCriteria().andEqualTo("creator","lgwy")
				.andEqualTo("module",commonLabel.getModule());
		List<CommonLabel> systemData = commonLabelDao.selectByExample(systemDataExample);
		Example ownerDataExample = new Example(CommonLabel.class);
		ownerDataExample.createCriteria().andEqualTo("creator",commonLabel.getCreator())
				.andEqualTo("module",commonLabel.getModule());
		//自己配置的部分
		List<CommonLabel> ownerData = commonLabelDao.selectByExample(ownerDataExample);
		Map<String, Object> map = new HashMap<>();
		map.put("system",systemData);
		map.put("owner",ownerData);
		return ApiResult.success(map);
	}
}
