package com.zans.job.strategy.balance;

import com.zans.base.util.Counter;
import com.zans.base.util.RandomHelper;
import com.zans.job.strategy.IBalanceStrategy;
import com.zans.job.vo.node.NodeVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.*;

import static com.zans.job.config.JobConstants.NODE_DEFAULT_WEIGHT;

/**
 * @author xv
 * @since 2020/5/13 13:13
 */
@Slf4j
@Component("weightRoundRobin")
public class WeightRoundRobinStrategy implements IBalanceStrategy {


    @Override
    public Map<NodeVO, List<Long>> doBalance(List<Long> taskList, List<NodeVO> nodeList) {
        Map<NodeVO, List<Long>> result = new HashMap<>(nodeList.size());
        int size = nodeList.size();
        log.info("task#{}, node#{}", taskList.size(), size);
        if (size == 0) {
            log.error("没有可用节点");
            return null;
        }
        int total = 0;
        Counter counter = new Counter();
        for (NodeVO vo : nodeList) {
            String nodeId = vo.getNodeId();
            Integer weight = vo.getWeight();
            if (weight == null || weight < 0) {
                weight = NODE_DEFAULT_WEIGHT;
                vo.setWeight(weight);
            }
            log.info("weight, {}#{}", nodeId, weight);
        }

        Selector selector = new Selector(nodeList);

        for(Long taskId : taskList) {
            NodeVO node = selector.select();
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



    static class Selector {

        private List<NodeVO> serverList;


        public Selector(List<NodeVO> nodeList) {
            this.serverList = nodeList;
        }

        public NodeVO select() {
            if (this.serverList == null || this.serverList.size() == 0) {
                return null;
            }
            if (this.serverList.size() == 1) {
                return this.serverList.get(0);
            }

            Integer total = 0;
            NodeVO nodeOfMaxWeight = null;
            for (NodeVO node : serverList) {
                total += node.getEffectiveWeight();
                node.setCurrentWeight(node.getCurrentWeight() + node.getEffectiveWeight());

                if (nodeOfMaxWeight == null) {
                    nodeOfMaxWeight = node;
                }else{
                    nodeOfMaxWeight = nodeOfMaxWeight.compareTo(node) > 0 ? nodeOfMaxWeight : node;
                }
            }
            nodeOfMaxWeight.setCurrentWeight(nodeOfMaxWeight.getCurrentWeight() - total);
            return nodeOfMaxWeight;
        }
    }



    public static void main(String[] args) {
        List<NodeVO> nodeList = new ArrayList<>(3);
//        nodeList.add(NodeVO.builder().nodeId("1").weight(4).currentWeight(0).effectiveWeight(4).build());
//        nodeList.add(NodeVO.builder().nodeId("2").weight(2).currentWeight(0).effectiveWeight(2).build());
//        nodeList.add(NodeVO.builder().nodeId("3").weight(1).currentWeight(0).effectiveWeight(1).build());
        Selector selector = new Selector(nodeList);

        for(int i=0; i<7; i++) {
            NodeVO next = selector.select();
            System.out.println(i + "#" + next.getNodeId());
        }
    }
}
