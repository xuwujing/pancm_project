package com.zans.job.vo.node;

import com.alibaba.fastjson.annotation.JSONField;
import com.zans.job.model.OpsJob;
import com.zans.job.model.OpsNode;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

@ApiModel
@Data
public class NodeRespVO {

    @ApiModelProperty(name = "node_id", value = "应用名称")
    @JSONField(name = "node_id")
    private String nodeId;

    @ApiModelProperty(name = "ip", value = "ip")
    @JSONField(name = "ip")
    private String ip;

    @ApiModelProperty(name = "node_type", value = "服务器类型")
    @JSONField(name = "node_type")
    private String nodeType;

    @ApiModelProperty(name = "max_task_count", value = "最大并发数")
    @JSONField(name = "max_task_count")
    @NotNull
    private Integer maxTaskCount;

    @ApiModelProperty(name = "current_task_count", value = "当前任务数")
    @JSONField(name = "current_task_count")
    private Integer currentTaskCount;

    @ApiModelProperty(name = "weight", value = "权重")
    @JSONField(name = "weight")
    @NotNull
    private Integer weight;

    @ApiModelProperty(name = "priority", value = "优先级")
    @JSONField(name = "priority")
    @NotNull
    private Integer priority;

    @ApiModelProperty(name = "alive", value = "运行状态")
    @JSONField(name = "alive")
    private Integer alive;

    @ApiModelProperty(name = "enable", value = "节点状态")
    @JSONField(name = "enable")
    @NotNull
    private Integer enable;

    @ApiModelProperty(name = "port", value = "端口")
    @JSONField(name = "port")
    private Integer port;

    @ApiModelProperty(name = "ssh_port", value = "SSH端口")
    @JSONField(name = "ssh_port")
    @NotNull
    private Integer sshPort;

    @ApiModelProperty(name = "root_password", value = "密码")
    @JSONField(name = "root_password")
    @NotNull
    private String rootPassword;

    @ApiModelProperty(name = "temp_quota", value = "给低优先级的额度")
    @JSONField(name = "temp_quota")
    private Integer tempQuota;

    @ApiModelProperty(name = "remark", value = "节点注释")
    @JSONField(name = "remark")
    private String remark;

    @ApiModelProperty(name = "register", value = "注册")
    @JSONField(name = "register")
    private Integer register;

    @ApiModelProperty(name = "expire_time", value = "租约到期时间")
    @JSONField(name = "expire_time")
    private Integer expireTime;

    @ApiModelProperty(name = "ip_port", value = "节点注释")
    @JSONField(name = "ip_port")
    private String ipPort;

    public static OpsNode initNode(OpsNode node, NodeRespVO respVO) {
        node.setSshPort(respVO.getSshPort());
        node.setRootPassword(respVO.getRootPassword());
        node.setMaxTaskCount(respVO.getMaxTaskCount());
        node.setWeight(respVO.getWeight());
        node.setPriority(respVO.getPriority());
        node.setRemark(respVO.getRemark());
        return node;
    }

}
