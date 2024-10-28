package com.zans.mms.dao.guard;

import com.zans.mms.model.Gear;
import com.zans.mms.vo.gear.GearReqVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author qitian
 * @Title: zans-demo
 * @Description:一机一档逻辑控制层
 * @Version:1.0.0
 * @Since:jdk1.8
 * @date 2021/6/29
 */
@Mapper
public interface GearDao {

	List<Gear> getList(GearReqVO req);

	void insert(Gear gear);

	void update(Gear gear);

	void delete(@Param("id") Long id);
}
