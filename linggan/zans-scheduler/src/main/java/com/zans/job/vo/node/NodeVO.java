package com.zans.job.vo.node;

import com.zans.job.strategy.balance.WeightRoundRobinStrategy;
import lombok.Data;

import javax.persistence.Column;
import java.util.Date;
import java.util.Objects;

/**
 * @author xv
 * @since 2020/5/7 16:04
 */
@Data
public class NodeVO implements Comparable<NodeVO>{

    private String nodeId;

    private String nodeType;

    private String ip;

    private Integer port;

    private Integer sshPort;

    private String rootPassword;

    private Integer maxTaskCount;

    private Integer weight;

    private Integer priority;

    private Integer tempQuota;

    private String remark;

    private Integer register;

    private Integer alive;

    private Date expireTime;

    private Integer enable;

    private Date createTime;

    private Date updateTime;

    private Integer runningTaskCount;

    private Integer effectiveWeight;

    private Integer currentWeight;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        NodeVO nodeVO = (NodeVO) o;
        return nodeId.equals(nodeVO.nodeId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(nodeId);
    }

    @Override
    public int compareTo(NodeVO o) {
        return currentWeight > o.currentWeight ? 1 : (currentWeight.equals(o.currentWeight) ? 0 : -1);
    }

    public void onInvokeSuccess(){
        if (effectiveWeight < this.weight) {
            effectiveWeight++;
        }
    }

    public void onInvokeFail(){
        effectiveWeight--;
    }
}
