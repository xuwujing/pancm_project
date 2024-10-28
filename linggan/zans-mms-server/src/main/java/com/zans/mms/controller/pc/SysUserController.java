package com.zans.mms.controller.pc;

import com.zans.base.annotion.Record;
import com.zans.base.controller.BaseController;
import com.zans.base.office.excel.ExcelHelper;
import com.zans.base.util.*;
import com.zans.base.vo.ApiResult;
import com.zans.base.vo.UserSession;
import com.zans.mms.config.MMSConstants;
import com.zans.mms.model.SysUser;
import com.zans.mms.service.IFileService;
import com.zans.mms.service.ISysUserService;
import com.zans.mms.service.IWechatUserWxbindService;
import com.zans.mms.vo.user.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;

import static com.zans.base.config.EnumErrorCode.*;
import static com.zans.base.config.GlobalConstants.UPLOAD_FILE_MAX_SIZE;


/**
 * (SysUser)表控制层
 *
 * @author beixing
 * @since 2021-01-20 11:07:27
 */
@Api(tags = "/用户管理")
@RestController
@RequestMapping("sysUser")
@Slf4j
public class SysUserController extends BaseController {
    /**
     * 服务对象
     */
    @Autowired
    private ISysUserService sysUserService;

    @Autowired
    private HttpHelper httpHelper;

    @Value("${api.export.folder}")
    String exportFolder;

    @Value("${api.upload.folder}")
    String uploadFolder;
    @Autowired
    IFileService fileService;

    @Autowired
    private IWechatUserWxbindService wechatUserWxbindService;
    /**
     * 新增一条数据
     *
     * @param reqVO 实体类
     * @return Response对象
     */
    @ApiOperation(value = "新增", notes = "新增")
    @RequestMapping(value = "save", method = RequestMethod.POST)
    @Record(module = MMSConstants.MODULE_SYSTEM_USER,operation = MMSConstants.LOG_OPERATION_SAVE)
    public ApiResult<SysUser> insert(@RequestBody SysUserAddReqVO reqVO, HttpServletRequest httpRequest) {
        UserSession userSession = httpHelper.getUser(httpRequest);
        reqVO.setCreator(userSession.getUserName());

        SysUser sysUser = new SysUser();
        BeanUtils.copyProperties(reqVO,sysUser);

        SysUser sameNameUser = sysUserService.findUserByName(sysUser.getUserName());
        if (sameNameUser != null) {
            return ApiResult.error(CLIENT_PARAMS_ERROR).appendMessage("用户名已存在#" + sysUser.getUserName());
        }

        SysUser user = sysUserService.queryByMobile(null,sysUser.getMobile());
        if (user != null) {
            return ApiResult.error(CLIENT_PARAMS_ERROR).appendMessage("手机号已存在#" + sysUser.getMobile());
        }
        String desPassword;
        try {
            desPassword = AESUtil.desEncrypt(reqVO.getPassword()).trim();
        } catch (Exception e) {
            log.error("desEncrypt error", e);
            desPassword = sysUser.getPassword();
        }

        // 密码处理
        String salt = SysUser.getRandomSalt();
        String newPassword = SecurityHelper.getMd5WithSalt(desPassword, salt);
        sysUser.setPassword(newPassword);
        sysUser.setSalt(salt);
//        sysUser.setEnable(1);
        int result = sysUserService.insert(sysUser);
        if (result > 0) {
            return ApiResult.success();
        }
        return ApiResult.error("新增失败");
    }

    /**
     * 修改一条数据
     *
     * @param reqVO 实体类
     * @return Response对象
     */
    @ApiOperation(value = "修改", notes = "修改")
    @RequestMapping(value = "edit", method = RequestMethod.POST)
    @Record(module = MMSConstants.MODULE_SYSTEM_USER,operation = MMSConstants.LOG_OPERATION_EDIT)
    public ApiResult<SysUser> update(@RequestBody SysUserEditReqVO reqVO, HttpServletRequest httpRequest) {
        SysUser sysUser = sysUserService.queryByIdOrUsername(reqVO.getId(),null);
        if(sysUser == null){
            return ApiResult.error("该用户不存在或已被删除！用户名称:"+reqVO.getUserName());
        }
        SysUser saveUser = new SysUser();
        BeanUtils.copyProperties(reqVO,saveUser);
        String oldMobile = sysUser.getMobile();
        String newMobile = reqVO.getMobile();
        //如果手机号发生变更，则删除小程序的绑定数据
        if(!StringHelper.equals(oldMobile,newMobile)){
            SysUser user = sysUserService.queryByMobile(null,newMobile);
            if (user != null) {
                return ApiResult.error(CLIENT_PARAMS_ERROR).appendMessage("手机号已存在#" + newMobile);
            }
            wechatUserWxbindService.deleteByUserName(reqVO.getUserName());
        }
        sysUserService.update(saveUser);
        return ApiResult.success();
    }

    /**
     * 删除一条数据
     *
     * @param id
     * @return Response对象
     */
    @ApiOperation(value = "删除", notes = "删除")
    @RequestMapping(value = "del", method = RequestMethod.GET)
    @Record(module = MMSConstants.MODULE_SYSTEM_USER,operation = MMSConstants.LOG_OPERATION_DELETE)
    public ApiResult<SysUser> delete(@RequestParam("id") Integer id, HttpServletRequest httpRequest) {
        SysUser sysUser = sysUserService.queryByIdOrUsername(id,null);
        if(sysUser == null){
            return ApiResult.error("该用户不存在或已被删除！用户ID:"+id);
        }
        sysUserService.deleteById(id);
        wechatUserWxbindService.deleteByUserName(sysUser.getUserName());
        return ApiResult.success();
    }


    /**
     * 分页查询
     */
    @ApiOperation(value = "查询", notes = "查询")
    @RequestMapping(value = "list", method = RequestMethod.POST)
    public ApiResult<SysUserResp> list(@RequestBody SysUserSearchReqVO sysUserSearchReqVO) {
        super.checkPageParams(sysUserSearchReqVO);
        SysUser sysUser = new SysUser();
        BeanUtils.copyProperties(sysUserSearchReqVO,sysUser);
        return sysUserService.list(sysUser);
    }

    /**
     * 批量更新状态
     */

    @ApiOperation(value = "更新密码", notes = "更新密码")
    @RequestMapping(value = "updatePassword", method = RequestMethod.POST)
    @Record(module = MMSConstants.MODULE_SYSTEM_USER,operation = MMSConstants.LOG_OPERATION_EDIT)
    public ApiResult<SysUserResp> updatePassword(@RequestBody UpdatePasswordReqVO reqVO, HttpServletRequest httpRequest) {
        return sysUserService.updatePassword(reqVO.getId(),reqVO.getNewPassword());
    }
    /**
     * 批量更新状态
     */

    @ApiOperation(value = "更新密码个人修改", notes = "更新密码个人修改")
    @RequestMapping(value = "updatePasswordBySession", method = RequestMethod.POST)
    @Record(module = MMSConstants.MODULE_SYSTEM_USER,operation = MMSConstants.LOG_OPERATION_EDIT)
    public ApiResult<SysUserResp> updatePasswordBySession(@RequestBody UpdatePasswordSessionReqVO reqVO, HttpServletRequest httpRequest) {
        UserSession userSession = httpHelper.getUser(httpRequest);
        return sysUserService.updatePassword(userSession.getUserId(),reqVO.getNewPassword());
    }

    /**
     * 批量更新状态
     */

    @ApiOperation(value = "批量更新状态", notes = "批量更新状态")
    @RequestMapping(value = "batchEditStatus", method = RequestMethod.POST)
    @Record(module = MMSConstants.MODULE_SYSTEM_USER,operation = MMSConstants.LOG_OPERATION_BATCH_EDIT)
    public ApiResult<SysUserResp> batchEditStatus(@RequestBody SysUserBatchEditReqVO reqVO, HttpServletRequest httpRequest) {
        return sysUserService.batchEditStatus(reqVO);
    }



    @ApiOperation(value = "/download/template", notes = "文件下载")
    @GetMapping("/download/template")
    @Record(module = MMSConstants.MODULE_SYSTEM_USER,operation = MMSConstants.LOG_OPERATION_DOWNLOAD)
    public void downloadFile(HttpServletRequest request,
                             HttpServletResponse response) throws Exception {
        String filePath = this.uploadFolder+"template/用户信息导入模板.xlsx";

        String date = DateHelper.formatDate(new Date(), "yyyyMMddHHmmss");
        String fileCnName = "用户信息导入模板" + date+".xlsx";
        this.download(filePath, fileCnName, request, response);
    }

    @ApiOperation(value = "/upload", notes = "上传文件，post参数id")
    @PostMapping("/upload")
    @ResponseBody
    @Record(module = MMSConstants.MODULE_SYSTEM_USER,operation = MMSConstants.LOG_OPERATION_IMPORT)
    public ApiResult uploadFile(@RequestParam("file") MultipartFile file,
                                HttpServletRequest request) throws Exception {
        if (!ExcelHelper.checkExtension(file)) {
            return ApiResult.error(SERVER_UPLOAD_MIME_ERROR).appendMessage("不是Excel类型");
        }
        long size = file.getSize();
        if (size > UPLOAD_FILE_MAX_SIZE) {
            return ApiResult.error(SERVER_UPLOAD_MAX_SIZE_ERROR)
                    .appendMessage("最大" + FileHelper.getSizeInMb(UPLOAD_FILE_MAX_SIZE) + "MB");
        }

        // 上传文件，持久化到本地，写数据库
        String originName = file.getOriginalFilename();
        String newFileName = fileService.getNewFileName(originName);
        boolean saved = FileHelper.saveFile(file, uploadFolder, newFileName);
        if (saved){
            UserSession userSession =  httpHelper.getUser(request);

            return sysUserService.batchAddUser(newFileName, originName,userSession);
        }
        return ApiResult.error("file save error");
    }

    @ApiOperation(value = "/download/errorFile", notes = "下载上传失败的文件")
    @RequestMapping(value = "/download/errorFile", method = {RequestMethod.GET})
    @Record(module = MMSConstants.MODULE_SYSTEM_USER,operation = MMSConstants.LOG_OPERATION_DOWNLOAD)
    public void errorFile(HttpServletRequest request,
                          HttpServletResponse response, @RequestParam(value = "errorFilePath") String errorFilePath) throws Exception {
        String date = DateHelper.formatDate(new Date(), "yyyyMMddHHmmss");
        String fileCnName = "用户信息上传失败" + date+".xlsx";
        this.download(errorFilePath, fileCnName, request, response);
    }

}
