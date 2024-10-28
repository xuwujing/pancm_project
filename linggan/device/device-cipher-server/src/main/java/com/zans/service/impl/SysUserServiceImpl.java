package com.zans.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.google.common.collect.Lists;
import com.zans.constant.EnumErrorCode;
import com.zans.dao.SysUserMapper;
import com.zans.model.ExcelUserInfoVO;
import com.zans.model.SysUser;
import com.zans.model.UserSession;
import com.zans.service.IFileService;
import com.zans.service.ISysUserService;
import com.zans.utils.*;
import com.zans.vo.PageResult;
import com.zans.vo.*;
import com.zans.vo.excel.ExcelEntity;
import com.zans.vo.excel.ExcelSheetReader;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import javax.persistence.RollbackException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author qiyi
 * @Title: leakproof-server
 * @Description:
 * @Version:1.0.0
 * @Since:jdk1.8
 * @date 2021/7/20
 */
@Service("sysUserService")
@Slf4j
public class SysUserServiceImpl implements ISysUserService {

    @Autowired
    protected HttpHelper httpHelper;

    @Resource
    private SysUserMapper sysUserMapper;


    @Override
    public SysUser selectById(Integer id) {
        SysUser sysUser = new SysUser();
        sysUser.setId(id);
        sysUser = sysUserMapper.selectByPrimaryKey(sysUser);
        return sysUser == null ? new SysUser() : sysUser;
    }

    @Autowired
    LoginCacheHelper loginCacheHelper;

//    @Value("${api.upload.folder}")
    String uploadFolder = "/home/release/file/upload/";

    @Autowired
    IFileService fileService;

    @Override
    public List<SelectVO> findUserToSelect(String areaNum, String maintainNum) {
        List<SelectVO> sysUserList = sysUserMapper.querySysUser(areaNum,maintainNum);
        return sysUserList;
    }

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    @Override
    public SysUser queryByIdOrNameOrPhone(Integer id, String userName,String phone) {
        return this.sysUserMapper.queryByIdOrNameOrPhone(id,userName,phone);
    }

    @Override
    public SysUser queryByMobile(String nickName, String mobile) {
        return sysUserMapper.queryByMobile(nickName,mobile);
    }


    /**
     * 根据条件查询
     *
     * @return 实例对象的集合
     */
    @Override
    public ApiResult list(SysUser sysUser) {
//        int pageNum = sysUser.getPageNum();
//        int pageSize = sysUser.getPageSize();
        Page page = PageHelper.startPage(1, 15);

        List<SysUserResp> result = sysUserMapper.queryAll(sysUser);
        return ApiResult.success(new PageResult<SysUserResp>(page.getTotal(), result, 15, 1));

    }

    /**
     * 新增数据
     *
     * @param sysUser 实例对象
     * @return 实例对象
     */
    @Override
    public int insert(SysUser sysUser) {
        return sysUserMapper.insert(sysUser);
    }

    /**
     * 修改数据
     *
     * @param sysUser 实例对象
     * @return 实例对象
     */
    @Override
    public int update(SysUser sysUser) {
        return sysUserMapper.update(sysUser);
    }

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 是否成功
     */
    @Override
    public boolean deleteById(Integer id) {
        return this.sysUserMapper.deleteById(id) > 0;
    }

    @Override
    public SysUser findUserByName(String userName) {
        return sysUserMapper.findUserByName(userName);
    }



    @Transactional(rollbackFor = Exception.class)
    @Override
    public ApiResult<SysUserResp> batchEditStatus(SysUserBatchEditReqVO reqVO) {
        List<Integer> ids = reqVO.getIds();
        if (null !=reqVO.getDeleteFlag() && reqVO.getDeleteFlag().intValue()==1){
            for (Integer id : ids) {
                sysUserMapper.deleteById(id);
            }
            return ApiResult.success("批量删除成功");
        }

        SysUser sysUser ;
        for (Integer id : ids) {
            sysUser = new SysUser();
            sysUser.setId(id);
            sysUser.setEnable(reqVO.getEnableStatus());
            sysUserMapper.update(sysUser);
            if (reqVO.getEnableStatus().intValue()==1) {
                SysUser user = sysUserMapper.queryByIdOrNameOrPhone(id, null,null);
                //解锁
                loginCacheHelper.removeLoginAttemptError(user.getUserName());
            }


        }

        return ApiResult.success();
    }

    @Override
    public ApiResult<SysUserResp> updatePassword(UpdatePasswordReqVO reqVO) {

//        SysUser sysUser = sysUserMapper.queryByIdOrUsername(reqVO.getId(), null);
//        String desNewPassword;
//        try {
//            desNewPassword = AESUtil.desEncrypt(reqVO.getNewPassword()).trim();
//        } catch (Exception e) {
//            log.error("desEncrypt error", e);
//            desNewPassword = reqVO.getNewPassword();
//        }
//
//        // 密码处理
//        String salt = SysUser.getRandomSalt();
//        String newPassword = SecurityHelper.getMd5WithSalt(desNewPassword, salt);
//
//        sysUser.setPassword(newPassword);
//        sysUser.setSalt(salt);
//        sysUserMapper.update(sysUser);
        return ApiResult.success();
    }
    private File getRemoteFile(String filePath) {
        if (filePath == null) {
            return null;
        }
        File file = new File(this.uploadFolder + "/" + filePath);
        if (!file.exists()) {
            log.error("file error#" + this.uploadFolder + "/" + filePath);
            return null;
        }
        return file;
    }
    private ExcelSheetReader getUserReader() {
        return ExcelSheetReader.builder().headRowNumber(Lists.newArrayList(2)).sheetNo(0)
                .notNullFields(Lists.newArrayList("ip_addr,point_name,area_name,device_type_name"))
                .headerClass(ExcelUserInfoVO.class).build();
    }
    @Override
    @Transactional(rollbackFor = RollbackException.class)
    public ApiResult batchAddUser(String filePath, String fileName,
                                  UserSession userSession) {
        File file = this.getRemoteFile(filePath);
        if (file == null) {
            return null;
        }
        String absoluteNewFilePath = this.uploadFolder + filePath;
        log.info("file#{}", file.getAbsolutePath());
        try {
            ExcelEntity linkResult = fileService.checkFile(fileName, file, absoluteNewFilePath, getUserReader());
            if (!linkResult.getValid()) {
                log.error("文件校验失败#" + absoluteNewFilePath);
                return ApiResult.error(EnumErrorCode.SERVER_UPLOAD_ERROR.getCode(), absoluteNewFilePath);
            }

            if (linkResult == null) {
                return ApiResult.error("未填写任何用户!");
            }
            List<ExcelUserInfoVO> list = linkResult.getEntity().convertToRawTable(ExcelUserInfoVO.class);
            if (list == null || list.size() == 0) {
                return ApiResult.error("未填写任何用户!!");
            }
            log.info("link.size#{}", list.size());
            this.dealExcelUser(list, userSession);

            return ApiResult.success();
        } catch (Exception ex) {
            log.error("读取用户上传文件失败#" + absoluteNewFilePath, ex);
            return ApiResult.error(EnumErrorCode.SERVER_UPLOAD_ERROR.getCode(), absoluteNewFilePath);
        }

    }


    public void dealExcelUser(List<ExcelUserInfoVO> list,
                              UserSession userSession) {
//        List<SelectVO>  maintainDevOpsSelectVOList = baseMaintainService.queryBaseMaintain();
        List<SelectVO>  maintainDevOpsSelectVOList = null;
//        List<SelectVO> enableStatusList = constantItemService.findItemsByDict("enable_status");
//        List<SelectVO>  maintainRoleSelectVOList = baseMaintainRoleService.queryBaseMaintainRole();
        List<SelectVO>  maintainRoleSelectVOList = null;
        SysUser sysUser = null;
        for (ExcelUserInfoVO vo : list) {

            vo.resetMaintain(maintainDevOpsSelectVOList);
//            vo.resetEnableStatus(enableStatusList);
            vo.resetRole(maintainRoleSelectVOList);

            sysUser = sysUserMapper.findUserByName(vo.getUserName());
            if (sysUser != null) {
                Integer id = sysUser.getId();
                sysUser = new SysUser();
                BeanUtils.copyProperties(vo, sysUser);
                sysUser.setId(id);
                sysUser.setUserName(null);
                sysUserMapper.update(sysUser);
            } else {
                sysUser = new SysUser();
                BeanUtils.copyProperties(vo, sysUser);
                sysUser.setEnable(1);
                //123456
                sysUser.setPassword("4b9236b62fd969b5766ef1663a1fa94a");
                sysUser.setIsAdmin(0);

                sysUserMapper.insert(sysUser);
            }

        }
    }

    @Override
    public SysUser getUserByOpenid(String openid) {
        return sysUserMapper.getUserByOpenid(openid);
    }

    @Override
    public void updateProjectId(Integer userId, String projectId) {
        sysUserMapper.updateProjectId(userId,projectId);

    }

    @Override
    public SysUser getById(Integer userId) {
        return sysUserMapper.getById(userId);
    }

    @Override
    public ApiResult myNetDisk(HttpServletRequest request) {
        List<NetDiskFile> netDiskFiles = new ArrayList<>();

        return ApiResult.success(netDiskFiles);
    }

    @Override
    public void download(HttpServletRequest request, HttpServletResponse response) {
        ExecutorService pool = Executors.newCachedThreadPool();


    }

    public UserSession getUserSession(HttpServletRequest request) {
        return this.httpHelper.getUser(request);
    }


    private void download(String requestUrl,
                          String fileCnName,
                          HttpServletResponse response) {
        try {
            String encodingName = URLEncoder.encode(fileCnName, "UTF-8");
            URL url = new URL(requestUrl);
            InputStream is =url.openStream();
            response.reset();
            response.setContentType("application/octet-stream");
            response.setCharacterEncoding("utf-8");
            response.setHeader("Content-Disposition", "attachment;filename=" + encodingName);
//            response.addHeader("Content-Length", "1111");
            byte[] buff = new byte[1024];
            BufferedInputStream bis = null;
            OutputStream os = null;
            try {
                os = response.getOutputStream();
                bis = new BufferedInputStream(is);
                int i = 0;
                while ((i = bis.read(buff)) != -1) {
                    os.write(buff, 0, i);
                    os.flush();
                }
            } finally {
                if (bis != null) {
                    bis.close();
                }
            }

        } catch (Exception ex) {
            String message = "download error#";
            log.error(message, ex);
        }
    }


    /**
     * GET请求
     *
     * @param requestUrl 请求地址
     * @return
     */
    public void get(String requestUrl,String uuid,String username,HttpServletRequest request, HttpServletResponse response,NetDiskFile netDiskFile) {
        log.info("requestUrl:{}",requestUrl);
        HttpURLConnection connection = null;
        BufferedReader br = null;
        BufferedInputStream bis = null;
        try {
            URL url = new URL(requestUrl);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setConnectTimeout(15000);
            connection.setReadTimeout(60000);
            connection.setRequestProperty("accept", "*/*");
            connection.setRequestProperty("connection", "Keep-Alive");
            connection.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            connection.connect();
//            Map<String, List<String>> map = connection.getHeaderFields();
//            for (String key : map.keySet()) {
//                System.out.println(key + "---------->" + map.get(key));
//            }
            if (connection.getResponseCode() == 200) {
//                log.info("生成数据库记录");
//                //插入记录
//                com.zans.model.FortUserFileEx fortUserFileEx = new FortUserFileEx();
//                fortUserFileEx.setFileUuid(uuid.toString().replace("-", ""));
//                fortUserFileEx.setIp(SFTP_HOST);
//                fortUserFileEx.setUserName(username);
//                fortUserFileEx.setUrl(username + "/" + netDiskFile.getFileName());
//                fortUserFileEx.setFileName(netDiskFile.getFileName());
//                fortUserFileEx.setFileType(netDiskFile.getFileName().substring(netDiskFile.getFileName().indexOf(".") + 1));
//                fortUserFileEx.setFileSize(netDiskFile.getFileSize());
//                fortUserFileEx.setFileStatus(1);
//                log.info("插入记录");
//                fortUserFileExDao.insert(fortUserFileEx);

                log.info("请求成功");

                bis = new BufferedInputStream(connection.getInputStream());
//                bis = new BufferedInputStream(url.openStream());
                int line;
                byte[] buff = new byte[1024];
                response.reset();
                ServletOutputStream outputStream = response.getOutputStream();
                this.setCors(request, response);
                response.setContentType("application/octet-stream,charset=UTF-8");
                response.addHeader("Content-type","application/octet-stream,charset=UTF-8");
                response.setCharacterEncoding("utf-8");
                response.setHeader("Content-Disposition", "attachment;filename=" + username);
                response.addHeader("Content-Length", connection.getHeaderFields().get("Content-Length").get(0));
                while ((line = bis.read(buff)) != -1) {
                    outputStream.write(buff, 0, line);
                    outputStream.flush();
                }
            }
        } catch (Exception e) {
            String message = "download error#" + username;
            log.error(message, e);
            e.printStackTrace();
        } finally {
            try {
                if (null != bis) {
                    bis.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            connection.disconnect();
            log.info("--------->>> GET request end <<<----------");
        }
    }

    protected void setCors(HttpServletRequest request,
                           HttpServletResponse response) {
        String origin = request.getHeader("Origin");
        if (StringUtils.isEmpty(origin)) {
            origin = "*";
        }
        String rh = request.getHeader("Access-Control-Request-Headers");
        if (StringUtils.isEmpty(origin)) {
            rh = "DNT,X-Mx-ReqToken,Keep-Alive,User-Agent,X-Requested-With,If-Modified-Since,Cache-Control," +
                    "Content-Type,Authorization,SessionToken,Content-Disposition";
        }
        // 解决跨域后，axios 无法获得其它 Response Header，默认只有 Content-Language，Content-Type，Expires，Last-Modified，Pragma
        response.setHeader("Access-Control-Expose-Headers", "Content-Disposition,Error-Code,Error-Message");
        response.setHeader("Access-Control-Allow-Origin", origin);
        response.setHeader("Access-Control-Allow-Credentials", "true");
        response.setHeader("Access-Control-Allow-Headers", rh);
    }
}
