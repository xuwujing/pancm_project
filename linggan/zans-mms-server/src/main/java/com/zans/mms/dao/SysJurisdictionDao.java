package com.zans.mms.dao;

import com.zans.base.vo.SelectVO;
import com.zans.mms.model.SysJurisdiction;
import com.zans.mms.vo.jurisdiction.SysJurisdictionPermVO;
import com.zans.mms.vo.jurisdiction.SysJurisdictionRepVO;
import com.zans.mms.vo.jurisdiction.SysJurisdictionReqVO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

/**
 * @author qitian
 * @Title: zans-mms-server
 * @Description:系统权限配置
 * @Version:1.0.0
 * @Since:jdk1.8
 * @date 2021/10/18
 */
@Repository
public interface SysJurisdictionDao extends Mapper<SysJurisdiction> {

	List<SysJurisdictionRepVO> list(SysJurisdictionReqVO vo);

	Integer exist(String id);

	List<SelectVO> selectList();

	String getCurrentId();

	void deleteSysjurisDiction(@Param("currentId") String currentId);

	List<SysJurisdictionPermVO> getByJurisdictionId(@Param("cloneId") String cloneId);

	void insertPerm(SysJurisdictionPermVO sysJurisdictionPermVO);
}
