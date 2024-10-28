package com.zans.job.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.zans.base.service.impl.BaseServiceImpl;
import com.zans.base.vo.PageResult;
import com.zans.job.dao.OpsNodeMapper;
import com.zans.job.model.OpsNode;
import com.zans.job.service.INodeService;
import com.zans.job.service.ITaskService;
import com.zans.job.vo.node.NodeReqVO;
import com.zans.job.vo.node.NodeRespVO;
import com.zans.job.vo.node.NodeVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

import static com.zans.job.config.JobConstants.NODE_TYPE_RAD_API;

/**
 * @author xv
 * @since 2020/5/7 16:11
 */
@Service("nodeService")
@Slf4j
public class NodeServiceImpl extends BaseServiceImpl<OpsNode> implements INodeService {

    OpsNodeMapper nodeMapper;

    @Autowired
    ITaskService taskService;

    @Resource
    public void setNodeMapper(OpsNodeMapper nodeMapper) {
        super.setBaseMapper(nodeMapper);
        this.nodeMapper = nodeMapper;
    }

    @Override
    public List<NodeVO> getEnableNodes(String nodeType, Integer priority) {
        return nodeMapper.getEnableNodes(nodeType, priority);
    }

    @Override
    public List<NodeVO> getDispatchNodes(Integer priority,String jobType) {
        Integer curPrioriry = priority;
        if (curPrioriry == null) {
            curPrioriry = 1;
        }

        //2020-10-9 和北辰确认，这里的jobType改成调用的type来进行判断
        List<NodeVO> nodeList = getEnableNodes(jobType, priority);
        while (curPrioriry >= 0 && (nodeList == null || nodeList.size() == 0)) {
            curPrioriry--;
            nodeList = getEnableNodes(jobType, curPrioriry);
        }
        for(NodeVO node : nodeList) {
            Integer count = taskService.getRunningTaskOfNode(node.getNodeId());
            node.setRunningTaskCount(count);
        }
        return nodeList;
    }

    @Override
    public OpsNode findNodeById(String nodeId) {
        return nodeMapper.findNodeById(nodeId);
    }

    @Override
    public NodeVO findRadApi() {
        List<NodeVO> nodeList = getEnableNodes(NODE_TYPE_RAD_API, null);
        if (nodeList.size() == 0) {
            return null;
        }
        return nodeList.get(0);
    }


    @Override
    public PageResult<NodeRespVO> getNodePage(NodeReqVO reqVO) {
        String orderBy = reqVO.getSortOrder();
        Page page = PageHelper.startPage(reqVO.getPageNum(), reqVO.getPageSize());

        List<NodeRespVO> list = nodeMapper.findNodeList(reqVO);
        return new PageResult<NodeRespVO>(page.getTotal(), page.getResult(), reqVO.getPageSize(), reqVO.getPageNum());
    }

}
