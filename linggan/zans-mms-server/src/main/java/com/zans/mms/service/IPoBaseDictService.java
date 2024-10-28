package com.zans.mms.service;

import com.zans.base.vo.SelectVO;
import com.zans.mms.model.PoBaseDict;

import java.util.List;
import java.util.Map;

/**
 * @author qitian
 * @Title: zans-mms-server
 * @Description:舆情投诉专用字典
 * @Version:1.0.0
 * @Since:jdk1.8
 * @date 2021/7/21
 */
public interface IPoBaseDictService {

	List<SelectVO> findItemsByDict(String modulePatrolAssetStatus);

	Integer areaIdValue2Key(String areaId);

	Integer eventSourceValue2Key(String eventSource);

	Integer typeValue2Key(String type);

	List<String> getDuty();

	String Key2Value(Integer eventSource, String eventSource1);

	String getAreaBelong(Integer areaId);

	PoBaseDict findByCode(String xzqh);

	Integer commonValue2Key(String tName, String poEvent);

	List<SelectVO> findPoDeviceType();

	Map<String, List<SelectVO>> getEventAndReason();

	Integer remark2ItemKey(String wftl1);

	String remark2DeviceType(String wtfl1);

	List<String> getOrgUser(List<String> currentUsername);
}
