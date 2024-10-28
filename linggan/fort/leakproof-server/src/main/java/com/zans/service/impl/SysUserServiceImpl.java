package com.zans.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.google.common.collect.Lists;
import com.zans.constant.EnumErrorCode;
import com.zans.dao.FortUserFileExDao;
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
import org.apache.commons.net.ftp.FTPClient;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import javax.persistence.RollbackException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

import static com.zans.utils.FTPUtil.readFileByFolder;

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

    @Resource
    private FortUserFileExDao fortUserFileExDao;

    @Value("${sftp.host}")
    private String host;

    @Value("${sftp.port}")
    private Integer port;

    @Value("${sftp.username}")
    private String username;

    @Value("${sftp.password}")
    private String password;

    @Value("${sftp.download.path}")
    private String downloadPath;
    
    @Value("${sftp.video_path}")
    private String videoPath;

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
        List<SelectVO> sysUserList = sysUserMapper.querySysUser(areaNum, maintainNum);
        return sysUserList;
    }

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    @Override
    public SysUser queryByIdOrUsername(Integer id, String userName) {
        return this.sysUserMapper.queryByIdOrUsername(id, userName);
    }

    @Override
    public SysUser queryByMobile(String nickName, String mobile) {
        return sysUserMapper.queryByMobile(nickName, mobile);
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
        if (null != reqVO.getDeleteFlag() && reqVO.getDeleteFlag().intValue() == 1) {
            for (Integer id : ids) {
                sysUserMapper.deleteById(id);
            }
            return ApiResult.success("批量删除成功");
        }

        SysUser sysUser;
        for (Integer id : ids) {
            sysUser = new SysUser();
            sysUser.setId(id);
            sysUser.setEnable(reqVO.getEnableStatus());
            sysUserMapper.update(sysUser);
            if (reqVO.getEnableStatus().intValue() == 1) {
                SysUser user = sysUserMapper.queryByIdOrUsername(id, null);
                //解锁
                loginCacheHelper.removeLoginAttemptError(user.getUserName());
            }


        }

        return ApiResult.success();
    }

    @Override
    public ApiResult<SysUserResp> updatePassword(UpdatePasswordReqVO reqVO) {

        SysUser sysUser = sysUserMapper.queryByIdOrUsername(reqVO.getId(), null);
        String desNewPassword;
        try {
            desNewPassword = AESUtil.desEncrypt(reqVO.getNewPassword()).trim();
        } catch (Exception e) {
            log.error("desEncrypt error", e);
            desNewPassword = reqVO.getNewPassword();
        }

        // 密码处理
        String salt = SysUser.getRandomSalt();
        String newPassword = SecurityHelper.getMd5WithSalt(desNewPassword, salt);

        sysUser.setPassword(newPassword);
        sysUser.setSalt(salt);
        sysUserMapper.update(sysUser);
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
        List<SelectVO> maintainDevOpsSelectVOList = null;
//        List<SelectVO> enableStatusList = constantItemService.findItemsByDict("enable_status");
//        List<SelectVO>  maintainRoleSelectVOList = baseMaintainRoleService.queryBaseMaintainRole();
        List<SelectVO> maintainRoleSelectVOList = null;
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
                sysUser.setCreator(userSession.getUserName());
                sysUser.setEnable(1);
                //123456
                sysUser.setPassword("4b9236b62fd969b5766ef1663a1fa94a");
                sysUser.setSalt("i2fb875aePCF");
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
        sysUserMapper.updateProjectId(userId, projectId);

    }

    @Override
    public SysUser getById(Integer userId) {
        return sysUserMapper.getById(userId);
    }

    @Override
    public ApiResult myNetDisk(NetDiskFileVO netDiskFileVO,HttpServletRequest request) {
        List<NetDiskFile> netDiskFiles = new ArrayList<>();
        FTPUtil test = new FTPUtil();
        FTPClient ftp = test.getFTPClient(host, port, username, password);
        netDiskFiles = readFileByFolder(ftp, videoPath + getUserSession(request).getUserName());
        test.closeFTP(ftp);//关闭ftp连接
        log.info("filePath:{}", videoPath + getUserSession(request).getUserName());
        Iterator<NetDiskFile> iterator = netDiskFiles.iterator();
        int total = 0;
        while (iterator.hasNext()) {
            NetDiskFile next = iterator.next();
            if (!StringUtils.isEmpty(netDiskFileVO.getFileName()) && !next.getFileName().contains(netDiskFileVO.getFileName())) {
                iterator.remove();
                continue;
            }
            next.setDownLoadPath(downloadPath + getUserSession(request).getUserName() + "/" + next.getFileName());
        }
        total = netDiskFiles.size();
        //按照时间倒序
        netDiskFiles = netDiskFiles.stream().sorted(Comparator.comparing(NetDiskFile::getUpdateTime).reversed())
                .collect(Collectors.toList());
        if (total < (netDiskFileVO.getPageNum() - 1) * netDiskFileVO.getPageSize() && netDiskFileVO.getPageNum() > 1) {
            PageResult pageResult = new PageResult(total, netDiskFiles, netDiskFileVO.getPageSize(), 1);
            return ApiResult.success(pageResult);
        }
        netDiskFiles = netDiskFiles.subList((netDiskFileVO.getPageNum() - 1) * netDiskFileVO.getPageSize(), Math.min(netDiskFileVO.getPageNum() * netDiskFileVO.getPageSize(), netDiskFiles.size()));
        PageResult pageResult = new PageResult(total, netDiskFiles, netDiskFileVO.getPageSize(), netDiskFileVO.getPageNum());
        return ApiResult.success(pageResult);
    }

    @Override
    public void download(HttpServletRequest request, HttpServletResponse response) {
        ExecutorService pool = Executors.newCachedThreadPool();
        FTPUtil ftpUtil = new FTPUtil();
        FTPClient ftp = ftpUtil.getFTPClient(host, port, username, password);
        String userName = getUserSession(request).getUserName();
        List<NetDiskFile> netDiskFiles = readFileByFolder(ftp, videoPath + userName);
        for (NetDiskFile netDiskFile : netDiskFiles) {
            netDiskFile.setDownLoadPath(downloadPath + userName + "/" + netDiskFile.getFileName());
//            String uuid = UUID.randomUUID().toString();
//            Runnable runnable = new Runnable() {
//                @Override
//                public void run() {
//            ftpUtil.downLoadFTP(ftp, BASE_PATH + userName, netDiskFile.getFileName(), request, response);
//                }
//            };
//            pool.execute(runnable);
        }
    }

    public UserSession getUserSession(HttpServletRequest request) {
        return this.httpHelper.getUser(request);
    }
}
