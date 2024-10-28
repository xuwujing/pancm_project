package com.zans.job.strategy;

import com.zans.job.model.OpsJobTask;

import java.util.List;
import java.util.Map;

/**
 * 分片策略
 * @author xv
 * @since 2020/5/8 18:56
 */
public interface IShardingStrategy {

    Map<OpsJobTask, List<Object>> doSharding();
}
