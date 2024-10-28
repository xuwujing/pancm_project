package com.zans.job.strategy;

import com.zans.job.model.OpsJobTask;
import com.zans.job.model.OpsNode;
import com.zans.job.vo.node.NodeVO;

import java.util.List;
import java.util.Map;

/**
 * 负载均衡的策略
 * @author xv
 * @since 2020/5/8 18:49
 */
public interface IBalanceStrategy {

    Map<NodeVO, List<Long>> doBalance(List<Long> taskList, List<NodeVO> nodeList);
}
