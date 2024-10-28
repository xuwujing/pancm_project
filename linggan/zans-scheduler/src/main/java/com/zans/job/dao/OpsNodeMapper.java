package com.zans.job.dao;

import com.zans.job.model.OpsNode;
import com.zans.job.vo.node.NodeReqVO;
import com.zans.job.vo.node.NodeRespVO;
import com.zans.job.vo.node.NodeVO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

@Repository
public interface OpsNodeMapper extends Mapper<OpsNode> {

    List<NodeVO> getEnableNodes(@Param("nodeType") String nodeType, @Param("priority") Integer priority);

    OpsNode findNodeById(@Param("nodeId") String nodeId);

    List<NodeRespVO> findNodeList(@Param("reqVo") NodeReqVO reqVO);

}
