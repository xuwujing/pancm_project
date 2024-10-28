package com.zans.mms.dao.mms;

import com.zans.mms.model.MacWhite;
import com.zans.mms.vo.MacWhiteVO;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface MacWhiteMapper extends Mapper<MacWhite> {

    List<MacWhiteVO> selectAllMac(MacWhiteVO macWhiteVO);

    int insertMac(MacWhiteVO macWhiteVO);

    int delMac(List<String> ids);

}