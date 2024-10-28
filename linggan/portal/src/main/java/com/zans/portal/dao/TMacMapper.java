package com.zans.portal.dao;

import com.zans.portal.model.TMac;
import com.zans.portal.vo.mac.MacRespVO;
import com.zans.portal.vo.mac.MacVO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;
import java.util.Map;

@Repository
public interface TMacMapper extends Mapper<TMac> {

    List<MacRespVO> findMacList(@Param("reqVo") MacVO reqVO);

    int getByMacAddress(@Param("macAddress") String macAddress, @Param("id") Integer id);


    int batchUpdateCompany(@Param("macMap") Map<String,Object> macMap);

    int updateCompany(@Param("key") String key,@Param("value") String value);

    int insertBatch(@Param("macMap") Map<String,Object> macMap);

    List<MacRespVO> findMacListByMacAddr(@Param("macMap") Map<String,Object> macMap);


}