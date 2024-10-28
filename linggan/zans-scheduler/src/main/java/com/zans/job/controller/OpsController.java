package com.zans.job.controller;

import com.jcraft.jsch.JSchException;
import com.zans.base.util.DateHelper;
import com.zans.base.util.FileHelper;
import com.zans.base.vo.ApiResult;
import com.zans.job.config.JobConstants;
import com.zans.job.model.OpsNode;
import com.zans.job.service.IAsyncService;
import com.zans.job.service.INodeService;
import com.zans.job.service.IRemoteService;
import com.zans.job.service.IUpgradeService;
import com.zans.job.service.bean.BaseApp;
import com.zans.job.vo.node.NodeVO;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.NotEmpty;
import java.io.IOException;
import java.util.Date;
import java.util.List;

/**
 * 运维端口
 *
 * @author xv
 * @since 2020/5/15 11:03
 */
@Slf4j
@RestController
@RequestMapping(value = "ops")
public class OpsController {

    @Autowired
    IUpgradeService upgradeService;

    @Autowired
    INodeService nodeService;

    @Autowired
    IAsyncService asyncService;

    @Autowired
    IRemoteService remoteService;

    @Value("${upgrade.src.folder}")
    String uploadFolder;


    @ApiOperation(value = "/upgrade", notes = "应用升级")
    @RequestMapping(value = "/upgrade", method = {RequestMethod.POST, RequestMethod.GET})
    public ApiResult upgrade(@NotEmpty(message = "应用名称必填") @RequestParam("app") String appName,
                             HttpServletRequest request) {
        log.info("upgrade  job#{}", appName);
        BaseApp app = upgradeService.getAppByName(appName);
        if (app == null) {
            String error = String.format("未知的应用名称#%s", appName);
            log.error(error);
            return ApiResult.error(error);
        }
        log.info("upgrade begin#{}", app);
        // TODO 离线服务器，开机后自动升级
        List<NodeVO> nodeList = nodeService.getEnableNodes(app.getNodeType(), null);
        asyncService.upgradeApp(nodeList, app);

        return ApiResult.success();
    }

    @ApiOperation(value = "/test", notes = "应用升级")
    @RequestMapping(value = "/test", method = {RequestMethod.GET})
    public ApiResult apiTest(@NotEmpty(message = "超时时间") @RequestParam("timeout") Integer timeout,
                             HttpServletRequest request) {
        log.info("api test#{}", timeout);
        // TODO 离线服务器，开机后自动升级
        List<NodeVO> nodes = nodeService.getDispatchNodes(JobConstants.PRIORITY_ZERO,null);
        if (nodes == null || nodes.size() == 0) {
            return ApiResult.error("empty");
        }
        NodeVO node = nodes.get(0);
        boolean result = remoteService.test(node.getIp(), node.getPort(), timeout);
        return ApiResult.success(result);
    }

    @ApiOperation(value = "/upload", notes = "上传升级包")
    @RequestMapping(value = "/upload", method = {RequestMethod.POST})
    public ApiResult upload(MultipartFile file, HttpServletRequest request) {
        if (file == null || file.isEmpty()) {
            return ApiResult.error("请上传文件");
        }
        String dateTime = DateHelper.formatDate(new Date(), "yyyyMMddHHmmss");
        String fileName = file.getOriginalFilename();
        boolean saved = FileHelper.saveFile(file, uploadFolder, fileName);
        if (!saved) {
            return ApiResult.error("上传失败");
        }
        return ApiResult.success(fileName);
    }

    @ApiOperation(value = "/enable", notes = "1:启动  0:关闭 服务")
    @RequestMapping(value = "/enable", method = {RequestMethod.POST})
    public ApiResult enable(@RequestParam(value = "id", required = true) String id,
                            @RequestParam(value = "enable", required = true) Integer enable) throws IOException, JSchException {
        OpsNode node = nodeService.findNodeById(id);
        if (node == null) {
            return ApiResult.error("当前节点不存在#" + id);
        }
        Boolean b = upgradeService.execEnableCommand(node.getIp(), "root", node.getRootPassword(), node.getSshPort(), node.getNodeType(), enable);
        if (b) {
            node.setAlive(enable);
            nodeService.update(node);
        }
        return ApiResult.success();
    }

}
