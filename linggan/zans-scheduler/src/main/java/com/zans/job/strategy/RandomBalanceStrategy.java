package com.zans.job.strategy;

import com.zans.base.util.RandomHelper;
import com.zans.job.vo.node.NodeVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.security.SecureRandom;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * @author xv
 * @since 2020/5/8 19:06
 */
@Slf4j
@Component("randomBalance")
public class RandomBalanceStrategy implements IBalanceStrategy {

    @Override
    public Map<NodeVO, List<Long>> doBalance(List<Long> taskList, List<NodeVO> nodeList) {

        int size = nodeList.size();
        log.info("task#{}, node#{}", taskList.size(), size);
        if (size == 0) {
            log.error("没有可用节点");
            return null;
        }

        Map<NodeVO, List<Long>> result = new HashMap<>(nodeList.size());
        SecureRandom random = RandomHelper.getSecureRandom();
        for(Long taskId : taskList) {
            int next = 0;
            if (size > 1) {
                next = random.nextInt(size);
            }
            log.info("task#{}, node.index#{}", taskId, next);
            NodeVO node = nodeList.get(next);
            List<Long> value = result.get(node);
            if (value == null) {
                value = new LinkedList<>();
                value.add(taskId);
                result.put(node, value);
            } else {
                value.add(taskId);
            }
        }
        return result;
    }

}
