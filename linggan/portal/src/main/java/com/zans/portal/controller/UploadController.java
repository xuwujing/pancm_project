package com.zans.portal.controller;

import com.zans.base.annotion.Record;
import com.zans.base.util.DateHelper;
import com.zans.base.util.FileHelper;
import com.zans.base.vo.ApiResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;


@Api(value = "/upload", tags = {"/upload ~ 上传控制器"})
@RestController
@RequestMapping("/upload")
@Slf4j
public class UploadController {

    @Autowired
    private Environment env;

    @ApiOperation(value = "/upgradePackage", notes = "上传升级包")
    @RequestMapping(value = "/upgradePackage", method = {RequestMethod.POST})
    @Record
    public ApiResult upgradePackage(MultipartFile file) {
        String folder = env.getProperty("api.upload.folder") + "packge/";
        if (file == null || file.isEmpty()) {
            return ApiResult.error("请上传文件");
        }
        String fileName = file.getOriginalFilename();
        if (!fileName.matches(".*\\.zip") && !fileName.matches(".*\\.rar")) {
            return ApiResult.error("请上传压缩包文件");
        }
        String dateTime = DateHelper.formatDate(new Date(), "yyyyMMddHHmmss");
        fileName = dateTime + "_" + file.getOriginalFilename();
        boolean saved = FileHelper.saveFile(file, folder, fileName);
        if (!saved) {
            return ApiResult.error("上传失败");
        }
        return ApiResult.success((folder + fileName).replaceAll("\\\\", "/"));
    }

}
