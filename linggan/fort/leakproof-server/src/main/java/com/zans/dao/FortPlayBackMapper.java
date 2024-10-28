package com.zans.dao;

import com.zans.model.FortPlayBack;
import com.zans.model.FortReserve;
import com.zans.vo.FortPlayBackVO;
import com.zans.vo.FortReserveVO;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

/**
 * @author qiyi
 * @Title: leakproof-server
 * @Description:
 * @Version:1.0.0
 * @Since:jdk1.8
 * @date 2021/7/1
 */
@org.apache.ibatis.annotations.Mapper
public interface FortPlayBackMapper extends Mapper<FortPlayBack> {

    int insertPlayBackInfo(FortPlayBack fortPlayBack);

    List<FortPlayBack> selectPlayBackInfo();

    FortPlayBack selectNewByIp(FortPlayBack fortPlayBack);

    List<FortPlayBack> selectPlayBack(FortPlayBackVO fortPlayBackVO);

    List<FortPlayBack> selectTimeQuantum(FortReserveVO fortReserveVO);

    List<FortPlayBack> check(FortReserve fortReserve);

}
