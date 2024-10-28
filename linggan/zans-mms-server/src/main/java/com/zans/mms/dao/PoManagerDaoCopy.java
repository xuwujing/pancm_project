package com.zans.mms.dao;

import com.zans.mms.model.PoManager;
import com.zans.mms.model.PoManagerCopy;
import com.zans.mms.model.PoManagerLogs;
import com.zans.mms.vo.chart.CircleUnit;
import com.zans.mms.vo.po.*;
import org.springframework.stereotype.Repository;
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
@Repository
public interface PoManagerDaoCopy extends Mapper<PoManagerCopy> {

}
