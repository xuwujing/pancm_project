package com.zans.mms.dao;

import com.zans.base.vo.SelectVO;
import com.zans.mms.model.PoBaseDict;
import com.zans.mms.model.PoManager;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

/**
 * @author qitian
 * @Title: zans-mms-server
 * @Description:
 * @Version:1.0.0
 * @Since:jdk1.8
 * @date 2021/7/21
 */
public interface PoBaseDictDao extends Mapper<PoBaseDict> {
	List<SelectVO> findItemsByDict(String dictKey);

	Integer findValueByKeyAndDict(@Param("val") String val,@Param("dict") String dict);

	List<String> getDuty();

	String Key2Value(@Param("itemKey") Integer itemKey,@Param("dictKey") String dictKey);

	String getAreaBelong(Integer areaId);

	PoBaseDict findByCode(@Param("remark") String xzqh);

	List<SelectVO> findPoDeviceType();

	List<String> getEventKey();

	List<SelectVO> getReasonList(@Param("param") String param);

	Integer remark2ItemKey(@Param("remark") String wftl1);

	String remark2DeviceType(@Param("remark") String wtfl1);

	List<String> getOrgUser(@Param("currentUsername") List<String> currentUsername);
}
