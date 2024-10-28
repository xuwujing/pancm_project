package com.zans.mms.controller.applet;

import com.zans.base.annotion.Record;
import com.zans.base.controller.BaseController;
import com.zans.base.util.HttpHelper;
import com.zans.base.vo.ApiResult;
import com.zans.mms.service.IFileDownService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import static com.zans.mms.config.MMSConstants.LOG_OPERATION_DELETE;
import static com.zans.mms.config.MMSConstants.LOG_OPERATION_UPLOAD;
import static com.zans.mms.config.MMSConstants.MODULE_APP_BASE_FILE;

/**
 * 附件管理控制层
 *
 * @author beixing
 * @since 2021-03-19 11:20:08
 */
@Api(value = "app/file", tags = {"/APP附件管理"})
@RestController
@RequestMapping("app/file")
@Slf4j
public class AppBaseFileController extends BaseController {

    @Autowired
    private IFileDownService fileDownService;


    @Autowired
    HttpHelper httpHelper;



    @ApiOperation(value = "/上传图片", notes = "上传图片")
    @RequestMapping(value = "/img/upload", method = {RequestMethod.POST})
    @Record(module = MODULE_APP_BASE_FILE,operation = LOG_OPERATION_UPLOAD)
    public ApiResult uploadImg(@RequestParam("adjunctId") String adjunctId, @RequestParam("files") MultipartFile[] files,
                                @RequestParam(value = "type",defaultValue = "1") Integer type,
                                @RequestParam("module") String module,
                                @RequestParam("number")  String number ){
        return fileDownService.upLoadImg(adjunctId,files,type,module,number);
    }


    @ApiOperation(value = "/删除图片", notes = "删除图片")
    @RequestMapping(value = "/img/del", method = {RequestMethod.POST})
    @Record(module = MODULE_APP_BASE_FILE,operation = LOG_OPERATION_DELETE)
    public ApiResult delImg(@RequestParam("adjunctId") String adjunctId,
                            @RequestParam("id") Long id){
        return fileDownService.delImg(adjunctId,id);
    }


}
