package com.zans.mms.dao;

import com.zans.base.vo.SelectVO;
import com.zans.mms.model.SysConstantItem;
import com.zans.mms.vo.constant.ConstantItemReqVO;
import com.zans.mms.vo.constant.ConstantItemSearchVO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

@Repository
public interface SysConstantItemMapper extends Mapper<SysConstantItem> {

	List<SelectVO> findItemsByDict(@Param("dictKey") String dictKey);

	List<String> getDictKeys();

	List<ConstantItemReqVO> findConstantItemList(@Param("reqVO") ConstantItemSearchVO reqVO);

	Integer getOrdinalByDictKeyAndItemKey(@Param("dictKey") String dictKey, @Param("itemKey") String itemKey);

	SysConstantItem findItemsByDictAndOridinal(@Param("dictKey") String dictKey, @Param("ordinal") Integer ordinal);

	SysConstantItem findByDictKeyAndItemKey(@Param("dictKey") String dictKey, @Param("itemKey") String itemKey, @Param("id") Integer id);

	String getConstantValueByKey(@Param("constantKey") String constantKey);


	void updateByConstantKey(@Param("constantKey") String constantKey,@Param("constantValue") Long constantValue);

	void updateStringByConstantKey(@Param("constantKey")String sqsj, @Param("constantValue")String maxSqsj);

	String getTimeByKey(@Param("constantKey")String sqsj);

	Integer getItemKey(@Param("itemValue") String issueSource,@Param("dictKey") String dictKey);

	String getItemValue(@Param("itemKey") Integer dealWay,@Param("dictKey") String dictKey);

	String getCurrentItemKeyByDictKey(@Param("dictKey") String project_name);

	Integer existProjectName(@Param("itemValue") String projectName);

	SysConstantItem getSysConstantItem(@Param("dictKey") String project_name,@Param("itemValue") String projectName);
}