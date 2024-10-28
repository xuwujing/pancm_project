package com.zans.mms.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.zans.base.exception.BusinessException;
import com.zans.base.vo.ApiResult;
import com.zans.base.vo.PageResult;
import com.zans.mms.dao.guard.GearDao;
import com.zans.mms.model.Gear;
import com.zans.mms.service.IGearService;
import com.zans.mms.vo.gear.GearReqVO;
import com.zans.mms.vo.asset.AssetResVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author qitian
 * @Title: zans-demo
 * @Description:一机一档业务层
 * @Version:1.0.0
 * @Since:jdk1.8
 * @date 2021/6/29
 */
@Service
@Slf4j
public class GearServiceImpl implements IGearService {

	@Resource
	private GearDao gearDao;


	/**
	 * 一机一档列表
	 * @param req
	 * @return
	 */
	@Override
	public ApiResult list(GearReqVO req) {
		int pageNum = req.getPageNum();
		int pageSize = req.getPageSize();
		Page page = PageHelper.startPage(pageNum, pageSize);
		List<Gear> result = gearDao.getList(req);
		return ApiResult.success(new PageResult<Gear>(page.getTotal(), result, pageSize, pageNum));
	}

	/**
	 * 新增或修改一机一档数据s
	 * @param gear 一机一档实体
	 */
	@Override
	public void saveOrUpdate(Gear gear) {
		//通过id判断此次需要新增 还是修改
		//如果为空 新增 否则 修改
		if(null== gear.getId()){
			try {
				gearDao.insert(gear);
			}catch (Exception e){
				log.error("错误信息{}",e);
				throw  new BusinessException("一机一档数据新增失败!");
			}

		}else{
			try {
				gearDao.update(gear);
			}catch (Exception e){
				log.error("错误信息{}",e);
				throw  new BusinessException("一机一档数据修改失败!");
			}

		}
	}

	@Override
	public Gear getById(Long id) {
		GearReqVO gearReqVO = new GearReqVO();
		gearReqVO.setId(id);
		List<Gear> list = gearDao.getList(gearReqVO);
		if(list!=null && list.size()>0){
			return list.get(0);
		}
		return null;
	}

	@Override
	public void delete(Long id) {
		try {
			gearDao.delete(id);
		}catch (Exception e){
			throw new BusinessException("数据删除失败");

		}

	}
}
