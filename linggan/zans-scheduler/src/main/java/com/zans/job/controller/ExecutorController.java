package com.zans.job.controller;

import com.zans.base.controller.BaseController;
import com.zans.base.util.DateHelper;
import com.zans.base.util.MapBuilder;
import com.zans.base.util.StringHelper;
import com.zans.base.vo.ApiResult;
import com.zans.job.model.OpsNode;
import com.zans.job.service.*;
import com.zans.job.vo.common.NodeDataSource;
import com.zans.job.vo.node.NodeRegVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import static com.zans.job.config.JobConstants.*;

/**
 * @author xv
 * @since 2020/5/15 14:44
 */
@Api(value = "/executor", tags = {"/executor ~ 执行控制器"})
@Slf4j
@RestController
@RequestMapping(value = "/executor")
public class ExecutorController extends BaseController {

    @Autowired
    INodeService nodeService;

    @Autowired
    IRemoteService remoteService;

    @Autowired
    IAsyncService asyncService;

    @Autowired
    IExecutorService executorService;

    @Autowired
    IExecutionService executionService;

    @ApiOperation(value = "/register", notes = "新执行节点信息")
    @RequestMapping(value = "/register", method = {RequestMethod.POST})
    public ApiResult register(@RequestBody NodeRegVO nodeReq,
                              HttpServletRequest request) {
        log.info("node reg#{}", nodeReq);
        String ip = nodeReq.getIp();
        Integer port = nodeReq.getPort();
        String nodeId = nodeReq.getNodeId();
        String nodeType = nodeReq.getNodeType();
        Integer priority = nodeReq.getPriority();
        if(priority == null){
            priority =1;
        }
        List<Object> errors = new ArrayList<>(6);
        if (nodeId == null) {
            errors.add("节点ID为空");
        }
        if (ip == null) {
            errors.add(String.format("IP错误#%s", ip));
        }
        if (port == null || port <= 1000) {
            errors.add(String.format("端口错误#%s", port));
        }
        //2020-10-8 和北辰确认，增加一个alert类型，用于处理告警相关
        if (nodeType == null ||
                (!NODE_TYPE_SCAN.equals(nodeType) && !NODE_TYPE_RAD_API.equals(nodeType) && !NODE_TYPE_ALERT.equals(nodeType))) {
            errors.add(String.format("类型错误#%s", nodeType));
        }
        boolean alive = remoteService.checkNodeAlive(ip, port, REMOTE_ACCESS_REPEAT);
        if (!alive) {
            String error = String.format("服务无法访问# http://%s:%d", ip, port);
            log.error("节点注册失败#{}, {}", nodeReq, error);
            errors.add(error);
        }
        if (errors.size() > 0) {
            return ApiResult.error(StringHelper.joinList(errors));
        }

        OpsNode nodeDb = null;
        nodeDb = nodeService.findNodeById(nodeId);
        if (nodeDb == null) {

            // 首次注册
            nodeDb = new OpsNode();
            nodeDb.setNodeId(nodeId);
            nodeDb.setNodeType(nodeType);
            nodeDb.setIp(ip);
            nodeDb.setPort(port);
            nodeDb.setEnable(1);
            nodeDb.setRegister(alive ? 1 : 0);
            nodeDb.setPriority(priority);
            nodeService.save(nodeDb);
        } else {
            // ip、端口变动
            if (!port.equals(nodeDb.getPort()) || !ip.equals(nodeDb.getIp())) {
                nodeDb.setPort(port);
                nodeDb.setIp(ip);
                nodeDb.setPriority(priority);
                nodeService.update(nodeDb);
            }
        }

        // 启动所有未完成的任务
        asyncService.sendUnFinishedTaskToNode(nodeDb);

        if (NODE_TYPE_RAD_API.equals(nodeType)) {
            NodeDataSource jobDataSource = executorService.getJobDataSource();
            NodeDataSource scanDataSource = executorService.getBusinessDataSource();
            Map<String, Object> map = MapBuilder.getBuilder().put("job", jobDataSource).put("scan", scanDataSource).build();
            return ApiResult.success(map);
        } else {
            return ApiResult.success();
        }

    }

    @ApiOperation(value = "/renew", notes = "节点续租")
    @RequestMapping(value = "/renew", method = {RequestMethod.POST})
    public ApiResult renew(String id,
                           HttpServletRequest request) {
        OpsNode nodeDb = nodeService.findNodeById(id);
        if (nodeDb == null) {
            return ApiResult.error("节点ID不存在#" + id);
        }
        Date newLease = DateHelper.addMinutes(new Date(), 2);
        log.info("node#{} renew#{}", id, DateHelper.getDateTime(newLease));
        if (newLease != null) {
            nodeDb.setExpireTime(newLease);
            nodeService.update(nodeDb);
        }
        return ApiResult.success();
    }

}
