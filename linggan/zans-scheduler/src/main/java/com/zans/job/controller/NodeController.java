package com.zans.job.controller;

import com.zans.base.controller.BaseController;
import com.zans.base.util.DateHelper;
import com.zans.base.util.MapBuilder;
import com.zans.base.util.StringHelper;
import com.zans.base.vo.ApiResult;
import com.zans.base.vo.PageResult;
import com.zans.base.vo.UserSession;
import com.zans.job.model.OpsJob;
import com.zans.job.model.OpsJobTask;
import com.zans.job.model.OpsNode;
import com.zans.job.service.IAsyncService;
import com.zans.job.service.INodeService;
import com.zans.job.service.IRemoteService;
import com.zans.job.service.ITaskService;
import com.zans.job.vo.common.NodeDataSource;
import com.zans.job.vo.execution.ExecuReqVO;
import com.zans.job.vo.execution.ExecuRespVO;
import com.zans.job.vo.job.JobReqVO;
import com.zans.job.vo.job.JobRespVO;
import com.zans.job.vo.node.NodeRegVO;
import com.zans.job.vo.node.NodeReqVO;
import com.zans.job.vo.node.NodeRespVO;
import com.zans.job.vo.node.NodeVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import static com.zans.job.config.JobConstants.*;

/**
 * @author xv
 * @since 2020/5/7 16:12
 */
@Api(value = "/node", tags = {"/node ~ 节点配置控制器"})
@Slf4j
@RestController
@RequestMapping(value = "/node")
public class NodeController extends BaseController {

    @Autowired
    INodeService nodeService;

    @ApiOperation(value = "/list", notes = "所有节点信息")
    @RequestMapping(value = "/list", method = {RequestMethod.GET})
    public ApiResult sayHello(HttpServletRequest request) {
        String now = DateHelper.getNow();
        List<OpsNode> nodeList = nodeService.getAll();
        Map<String, Object> result = MapBuilder.getBuilder().put("hello", "world")
                .put("now", DateHelper.getNow())
                .put("nodes", nodeList)
                .build();
        return ApiResult.success(result);
    }

    @ApiOperation(value = "/rad_api", notes = "获得rad_client配置信息")
    @RequestMapping(value = "/rad_api", method = {RequestMethod.GET})
    public ApiResult getRadClient(HttpServletRequest request) {
        String now = DateHelper.getNow();
        NodeVO node = nodeService.findRadApi();
        Map<String, Object> result = MapBuilder.getSimpleMap("rad_api", node);
        return ApiResult.success(result);
    }


    @ApiOperation(value = "/list", notes = "节点列表查询")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "req", value = "查询条件", required = true, dataType = "NodeReqVO", paramType = "body")
    })
    @RequestMapping(value = "/list", method = {RequestMethod.POST})
    @ResponseBody
    public ApiResult list(@RequestBody NodeReqVO req) {
        super.checkPageParams(req, " update_time desc");

        PageResult<NodeRespVO> page = nodeService.getNodePage(req);

        Map<String, Object> result = MapBuilder.getBuilder()
                .put("table", page)
                .build();
        return ApiResult.success(result);
    }

    @ApiOperation(value = "/update", notes = "修改Node服务")
    @ApiImplicitParam(name = "mergeVO", value = "修改Node服务", required = true, dataType = "NodeRespVO", paramType = "body")
    @RequestMapping(value = "/update", method = {RequestMethod.POST})
    public ApiResult update(@Valid @RequestBody NodeRespVO mergeVO, HttpServletRequest request) {
        String nodeId = mergeVO.getNodeId();
        OpsNode node = nodeService.findNodeById(nodeId);
        if (node == null) {
            return ApiResult.error("当前节点不存在#" + nodeId);
        }
        node = NodeRespVO.initNode(node, mergeVO);
        nodeService.update(node);
        return ApiResult.success(MapBuilder.getSimpleMap("id", nodeId)).appendMessage("请求成功");
    }


    @ApiOperation(value = "/deleteNode", notes = "删除Job")
    @ApiImplicitParam(name = "id", value = "id", required = true, dataType = "int", paramType = "query")
    @RequestMapping(value = "/deleteNode", method = {RequestMethod.POST})
    public ApiResult deleteNode(@NotNull(message = "id必填") String id, HttpServletRequest request) {
        OpsNode node = nodeService.findNodeById(id);
        if (node == null) {
            return ApiResult.error("当前节点不存在#" + id);
        }
        if (node.getAlive() == 0) {
            return ApiResult.error("节点活跃中，无法删除#" + id);
        }
        node.setEnable(1);
        nodeService.update(node);
        return ApiResult.success().appendMessage("删除成功");
    }


    @ApiOperation(value = "/view", notes = "节点详情")
    @RequestMapping(value = "/view", method = {RequestMethod.POST})
    public ApiResult<NodeRespVO> view(@NotNull(message = "id必填") String id, HttpServletRequest request) {
        OpsNode node = nodeService.findNodeById(id);
        if (node == null) {
            return ApiResult.error("当前节点不存在#" + id);
        }
        NodeRespVO respVO = new NodeRespVO();
        BeanUtils.copyProperties(node,respVO);

        Map<String, Object> map = MapBuilder.getBuilder()
                .put("executeTable", respVO)
                .build();
        return ApiResult.success(map);
    }

}
