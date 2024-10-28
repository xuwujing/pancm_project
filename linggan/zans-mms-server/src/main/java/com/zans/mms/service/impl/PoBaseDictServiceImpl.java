package com.zans.mms.service.impl;

import com.zans.base.vo.SelectVO;
import com.zans.mms.dao.PoBaseDictDao;
import com.zans.mms.model.PoBaseDict;
import com.zans.mms.service.IPoBaseDictService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.zans.base.config.GlobalConstants.*;

/**
 * @author qitian
 * @Title: zans-mms-server
 * @Description:
 * @Version:1.0.0
 * @Since:jdk1.8
 * @date 2021/7/21
 */
@Service("poBaseDictService")
@Slf4j
public class PoBaseDictServiceImpl implements IPoBaseDictService {

	@Resource
	private PoBaseDictDao poBaseDictDao;


	@Override
	public List<SelectVO> findItemsByDict(String dictKey) {
		List<SelectVO> list = poBaseDictDao.findItemsByDict(dictKey);
		for (SelectVO vo : list) {
			vo.resetKey();
		}
		return list;
	}

	@Override
	public Integer areaIdValue2Key(String areaId) {
		return poBaseDictDao.findValueByKeyAndDict(areaId,AREA_ID);
	}

	@Override
	public Integer eventSourceValue2Key(String eventSource) {
		return poBaseDictDao.findValueByKeyAndDict(eventSource,EVENT_SOURCE);
	}

	@Override
	public Integer typeValue2Key(String type) {
		return poBaseDictDao.findValueByKeyAndDict(type,PO_TYPE);
	}


	@Override
	public List<String> getDuty() {
		return poBaseDictDao.getDuty();
	}

	@Override
	public String Key2Value(Integer itemKey, String dictKey) {
		return poBaseDictDao.Key2Value(itemKey,dictKey);
	}

	@Override
	public String getAreaBelong(Integer areaId) {
		return poBaseDictDao.getAreaBelong(areaId);
	}

	@Override
	public PoBaseDict findByCode(String xzqh) {
		return poBaseDictDao.findByCode(xzqh);
	}

	@Override
	public Integer commonValue2Key(String tName, String poEvent) {
		return poBaseDictDao.findValueByKeyAndDict(tName,PO_TYPE);
	}

	@Override
	public List<SelectVO> findPoDeviceType() {
		return poBaseDictDao.findPoDeviceType();
	}

	@Override
	public Map<String, List<SelectVO>> getEventAndReason() {
		Map<String, List<SelectVO>> map = new HashMap<>();
		//先查处所有的事件 再根据事件 查出所有的原因
		List<String> eventKey= poBaseDictDao.getEventKey();
		for(int i = 0;i<eventKey.size();i++){
			List<SelectVO> reasonList = poBaseDictDao.getReasonList(eventKey.get(i));
			map.put(eventKey.get(i),reasonList);
		}
		return map;
	}

	@Override
	public Integer remark2ItemKey(String wftl1) {
		return poBaseDictDao.remark2ItemKey(wftl1);
	}

	@Override
	public String remark2DeviceType(String wtfl1) {
		return poBaseDictDao.remark2DeviceType(wtfl1);
	}

	@Override
	public List<String> getOrgUser(List<String> currentUsername) {
		return poBaseDictDao.getOrgUser(currentUsername);
	}
}
