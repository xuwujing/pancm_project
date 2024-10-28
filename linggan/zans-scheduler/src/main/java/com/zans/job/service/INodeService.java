package com.zans.job.service;

import com.zans.base.service.BaseService;
import com.zans.base.vo.PageResult;
import com.zans.job.model.OpsNode;
import com.zans.job.vo.node.NodeReqVO;
import com.zans.job.vo.node.NodeRespVO;
import com.zans.job.vo.node.NodeVO;

import java.util.List;

/**
 * @author xv
 * @since 2020/5/7 12:02
 */
public interface INodeService extends BaseService<OpsNode> {

    /**
     * 是否严格按优先级获取机器
     * @param nodeType
     * @param priority
     * @return
     */
    List<NodeVO> getEnableNodes(String nodeType, Integer priority);

    //2020-10-9 这里改成通过jobtype来进行查询
    List<NodeVO> getDispatchNodes(Integer priority,String jobType);

//    List<NodeVO> getDispatchNodes(Integer priority);

    OpsNode findNodeById(String nodeId);

    NodeVO findRadApi();

    PageResult<NodeRespVO> getNodePage(NodeReqVO reqVO);

}
