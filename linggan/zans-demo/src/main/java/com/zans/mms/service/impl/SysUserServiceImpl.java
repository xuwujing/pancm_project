package com.zans.mms.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.google.common.collect.Lists;
import com.zans.base.exception.RollbackException;
import com.zans.base.office.excel.ExcelEntity;
import com.zans.base.office.excel.ExcelSheetReader;
import com.zans.base.util.AESUtil;
import com.zans.base.util.LoginCacheHelper;
import com.zans.base.util.SecurityHelper;
import com.zans.base.vo.ApiResult;
import com.zans.base.vo.PageResult;
import com.zans.base.vo.SelectVO;
import com.zans.base.vo.UserSession;
import com.zans.mms.dao.guard.SysUserDao;
import com.zans.mms.model.SysUser;
import com.zans.mms.service.IConstantItemService;
import com.zans.mms.service.IFileService;
import com.zans.mms.service.ISysUserService;
import com.zans.mms.vo.user.ExcelUserInfoVO;
import com.zans.mms.vo.user.SysUserBatchEditReqVO;
import com.zans.mms.vo.user.SysUserResp;
import com.zans.mms.vo.user.UpdatePasswordReqVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.io.File;
import java.util.List;

import static com.zans.base.config.EnumErrorCode.SERVER_UPLOAD_ERROR;


/**
 * (SysUser)表服务实现类
 *
 * @author beixing
 * @since 2021-01-16 16:41:08
 */
@Service("sysUserService")
@Slf4j
public class SysUserServiceImpl implements ISysUserService {
    @Resource
    private SysUserDao sysUserDao;

    @Autowired
    LoginCacheHelper loginCacheHelper;

    @Value("${api.upload.folder}")
    String uploadFolder;

    @Autowired
    IFileService fileService;



    @Autowired
    IConstantItemService constantItemService;



    @Override
    public List<SelectVO> findUserToSelect(String areaNum,String maintainNum) {
        List<SelectVO> sysUserList = sysUserDao.querySysUser(areaNum,maintainNum);
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
        return this.sysUserDao.queryByIdOrUsername(id,userName);
    }

    @Override
    public SysUser queryByMobile(String nickName, String mobile) {
        return sysUserDao.queryByMobile(nickName,mobile);
    }


    /**
     * 根据条件查询
     *
     * @return 实例对象的集合
     */
    @Override
    public ApiResult list(SysUser sysUser) {
        int pageNum = sysUser.getPageNum();
        int pageSize = sysUser.getPageSize();
        Page page = PageHelper.startPage(pageNum, pageSize);

        List<SysUserResp> result = sysUserDao.queryAll(sysUser);
        return ApiResult.success(new PageResult<SysUserResp>(page.getTotal(), result, pageSize, pageNum));

    }

    /**
     * 新增数据
     *
     * @param sysUser 实例对象
     * @return 实例对象
     */
    @Override
    public int insert(SysUser sysUser) {
        return sysUserDao.insert(sysUser);
    }

    /**
     * 修改数据
     *
     * @param sysUser 实例对象
     * @return 实例对象
     */
    @Override
    public int update(SysUser sysUser) {
        return sysUserDao.update(sysUser);
    }

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 是否成功
     */
    @Override
    public boolean deleteById(Integer id) {
        return this.sysUserDao.deleteById(id) > 0;
    }

    @Override
    public SysUser findUserByName(String userName) {
        return sysUserDao.findUserByName(userName);
    }



    @Transactional(rollbackFor = Exception.class)
    @Override
    public ApiResult<SysUserResp> batchEditStatus(SysUserBatchEditReqVO reqVO) {
        List<Integer> ids = reqVO.getIds();
        if (null !=reqVO.getDeleteFlag() && reqVO.getDeleteFlag().intValue()==1){
            for (Integer id : ids) {
                sysUserDao.deleteById(id);
            }
            return ApiResult.success("批量删除成功");
        }

        SysUser sysUser ;
        for (Integer id : ids) {
            sysUser = new SysUser();
            sysUser.setId(id);
            sysUser.setEnable(reqVO.getEnableStatus());
            sysUserDao.update(sysUser);
            if (reqVO.getEnableStatus().intValue()==1) {
                SysUser user = sysUserDao.queryByIdOrUsername(id, null);
                //解锁
                loginCacheHelper.removeLoginAttemptError(user.getUserName());
            }


        }

        return ApiResult.success();
    }

    @Override
    public ApiResult<SysUserResp> updatePassword(UpdatePasswordReqVO reqVO) {

        SysUser sysUser = sysUserDao.queryByIdOrUsername(reqVO.getId(), null);
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
        sysUserDao.update(sysUser);
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
                return ApiResult.error(SERVER_UPLOAD_ERROR, absoluteNewFilePath);
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
            return ApiResult.error(SERVER_UPLOAD_ERROR, absoluteNewFilePath);
        }

    }


    public void dealExcelUser(List<ExcelUserInfoVO> list,
                              UserSession userSession) {
//        List<SelectVO>  maintainDevOpsSelectVOList = baseMaintainService.queryBaseMaintain();
        List<SelectVO>  maintainDevOpsSelectVOList = null;
        List<SelectVO> enableStatusList = constantItemService.findItemsByDict("enable_status");
//        List<SelectVO>  maintainRoleSelectVOList = baseMaintainRoleService.queryBaseMaintainRole();
        List<SelectVO>  maintainRoleSelectVOList = null;
        SysUser sysUser = null;
        for (ExcelUserInfoVO vo : list) {

            vo.resetMaintain(maintainDevOpsSelectVOList);
            vo.resetEnableStatus(enableStatusList);
            vo.resetRole(maintainRoleSelectVOList);

            sysUser = sysUserDao.findUserByName(vo.getUserName());
            if (sysUser != null) {
                Integer id = sysUser.getId();
                sysUser = new SysUser();
                BeanUtils.copyProperties(vo, sysUser);
                sysUser.setId(id);
                sysUser.setUserName(null);
                sysUserDao.update(sysUser);
            } else {
                sysUser = new SysUser();
                BeanUtils.copyProperties(vo, sysUser);
                sysUser.setCreator(userSession.getUserName());
                sysUser.setEnable(1);
                //123456
                sysUser.setPassword("4b9236b62fd969b5766ef1663a1fa94a");
                sysUser.setSalt("i2fb875aePCF");
                sysUser.setIsAdmin(0);

                sysUserDao.insert(sysUser);
            }

        }
    }

    @Override
    public SysUser getUserByOpenid(String openid) {
        return sysUserDao.getUserByOpenid(openid);
    }

    @Override
    public void updateProjectId(Integer userId, String projectId) {
        sysUserDao.updateProjectId(userId,projectId);

    }

    @Override
    public SysUser getById(Integer userId) {
        return sysUserDao.getById(userId);
    }
}
