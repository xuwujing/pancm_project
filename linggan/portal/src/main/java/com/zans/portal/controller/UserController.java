package com.zans.portal.controller;

import com.zans.base.annotion.Record;
import com.zans.base.util.CheckPassHelper;
import com.zans.base.util.MapBuilder;
import com.zans.base.util.SecurityHelper;
import com.zans.base.util.StringHelper;
import com.zans.base.vo.ApiResult;
import com.zans.base.vo.PageResult;
import com.zans.base.vo.SelectVO;
import com.zans.base.vo.UserSession;
import com.zans.portal.config.GlobalConstants;
import com.zans.portal.model.TUser;
import com.zans.portal.service.*;
import com.zans.portal.util.AESUtil;
import com.zans.portal.util.HttpHelper;
import com.zans.portal.util.LoginCacheHelper;
import com.zans.portal.vo.role.RoleAndUserRespVO;
import com.zans.portal.vo.user.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Map;

import static com.zans.base.config.EnumErrorCode.*;
import static com.zans.portal.config.GlobalConstants.*;
import static com.zans.portal.constants.PortalConstants.*;


@Api(value = "/user", tags = "/user, 用户管理")
@RestController
@RequestMapping("/user")
@Validated
@Slf4j
public class UserController extends BasePortalController {
    @Value("${security.user-verify}")
    String userVerify;

    @Autowired
    IUserService userService;

    @Autowired
    HttpHelper httpHelper;

    @Autowired
    ILogOperationService logOperationService;

    @Autowired
    IConstantItemService constantItemService;

    @Autowired
    IPermissionService permissionService;

    @Autowired
    IRoleService roleService;


    @Autowired
    LoginCacheHelper loginCacheHelper;

    @ApiOperation(value = "/list", notes = "获取用户列表，屏蔽密码")
    @RequestMapping(value = "/list", method = {RequestMethod.POST})
    public ApiResult<List<UserRespVO>> findAllUser(@RequestBody UserReqVO reqVo) {
        return getList(reqVo);
    }


    private ApiResult<List<UserRespVO>> getList(@RequestBody UserReqVO reqVo) {
        List<SelectVO> departList = constantItemService.findItemsByDict(MODULE_USER_DEPARTMENT);
        Map<Object, String> enableMap = constantItemService.findItemsMapByDict(GlobalConstants.MODULE_ENABLE_STATUS);
        Map<Object, String> lockMap = constantItemService.findItemsMapByDict(GlobalConstants.MODULE_USER_LOCK);
        Map<Object, String> departMap = constantItemService.findItemsMapByDict(GlobalConstants.MODULE_USER_DEPARTMENT);

        super.checkPageParams(reqVo, null);
        PageResult<UserRespVO> userList = userService.getUserPageWHJG(reqVo);
        List<UserRespVO> list = userList.getList();
        for (UserRespVO vo : list) {
            vo.resetEnableName(enableMap);
            vo.resetLockStatusName(lockMap);
            vo.resetDepartName(departMap);
        }
        userList.setList(list);
        List<SelectVO> roleList = roleService.findRoleToSelect();
        Map<String, Object> obj = MapBuilder.getBuilder().put("user", userList)
                .put("role", roleList).build();
        obj.put("department", departList);
        return ApiResult.success(obj);
    }

    @ApiOperation(value = "/current", notes = "当前登录用户")
    @RequestMapping(value = "/current", method = {RequestMethod.GET})
    @ResponseBody
    public ApiResult<UserSession> getCurrentUser(HttpServletRequest request) {
        UserSession userSession = httpHelper.getUser(request);
        return ApiResult.success(userSession);
    }

    @ApiOperation(value = "/view", notes = "获取用户详细信息")
    @ApiImplicitParam(name = "id", value = "用户id", required = true, dataType = "int", paramType = "query")
    @RequestMapping(value = "/view", method = {RequestMethod.GET})
    public ApiResult<UserRespVO> getUserById(@NotNull(message = "用户id必填") Integer id) {
        UserRespVO user = userService.findUserByIdWHJG(id);
        if (user == null) {
            return ApiResult.error(CLIENT_PARAMS_ERROR).appendMessage("user.id isn't existed#" + id);
        }
        return ApiResult.success(user);
    }

    @ApiOperation(value = "/me", notes = "获取自己用户详细信息")
    @RequestMapping(value = "/me", method = {RequestMethod.GET})
    public ApiResult<UserRespVO> getMeUser(HttpServletRequest request) {
        UserRespVO user = null;
        UserSession userSession = getUserSession(request);
        if (userSession == null) {
            return ApiResult.error(NEED_LOGIN_AGAIN).appendMessage("user.id isn't existed#");
        }
        user = userService.findUserByIdWHJG(userSession.getUserId());
        if (user == null) {
            return ApiResult.error(CLIENT_PARAMS_ERROR).appendMessage("user.id isn't existed#" + userSession.getUserId());
        }
        return ApiResult.success(user);
    }

    @ApiOperation(value = "/add", notes = "新增用户")
    @RequestMapping(value = "/add", method = {RequestMethod.POST})
    @Record(module = PORTAL_MODULE_USER, operation = PORTAL_LOG_OPERATION_ADD)
    public ApiResult addUser(@Valid @RequestBody UserAddVO userAddReq,
                             HttpServletRequest request) {

        TUser sameNameUser = userService.findUserByName(userAddReq.getUserName());
        if (sameNameUser != null) {
            return ApiResult.error(CLIENT_PARAMS_ERROR).appendMessage("用户名已存在#" + userAddReq.getUserName());
        }

        TUser userInput = userAddReq.convertToUser();

        String desPassword;
        try {
            desPassword = AESUtil.desEncrypt(userInput.getPassword()).trim();
            userInput.setPassword(desPassword);
        } catch (Exception e) {
            log.error("desEncrypt error", e);
            desPassword = userInput.getPassword();
        }


        // 密码处理
        String salt = TUser.getRandomSalt();
        String newPassword = SecurityHelper.getMd5WithSalt(userInput.getPassword(), salt);
        userInput.setPassword(newPassword);
        userInput.setSalt(salt);

        // 设置默认状态
        userInput.keepNormal();
        userInput.setDeleteStatus(DELETE_NOT);
        userService.save(userInput);

        userService.addUserRole(userInput.getId(), userAddReq.getRole());


        return ApiResult.success(MapBuilder.getSimpleMap("id", userInput.getId())).appendMessage("添加用户成功");
    }

    @ApiOperation(value = "/edit", notes = "修改用户")
    @RequestMapping(value = "/edit", method = {RequestMethod.POST})
    @Record(module = PORTAL_MODULE_USER, operation = PORTAL_LOG_OPERATION_EDIT)
    public ApiResult editUser(@Valid @RequestBody UserEditVO userEditReq,
                              HttpServletRequest request) {

        TUser userInput = new TUser();
        BeanUtils.copyProperties(userEditReq, userInput);

        TUser sameNameUser = userService.findUserByNameExceptId(userInput.getUserName(), userInput.getId());
        if (sameNameUser != null) {
            return ApiResult.error(CLIENT_PARAMS_ERROR).appendMessage(
                    String.format("用户名已存在#%s", userInput.getUserName()));
        }

        TUser userDb = userService.getById(userInput.getId());
        if (userDb == null) {
            return ApiResult.error(CLIENT_PARAMS_ERROR).appendMessage(
                    String.format("用户ID不存在"));
        }
        UserSession userSession = getUserSession(request);
        //如果是超管，只有自己能够修改
        if (userDb.getIsAdmin() != null && userDb.getIsAdmin() == 1) {
            if (userSession.getUserId().intValue() != userInput.getId().intValue()) {
                return ApiResult.error(CLIENT_PARAMS_ERROR).appendMessage(
                        String.format("不允许操作管理员"));
            }
        }

        // TODO 判断权限


        if (StringUtils.isNotEmpty(userInput.getPassword())) {
            // 密码更改处理
            this.resetPassword(userDb, userInput.getPassword());
            userInput.setPassword(userDb.getPassword());
        } else {
            //前端未修改密码 置空null
            userInput.setPassword(null);
        }


        if (userSession.getUserId().intValue() == userInput.getId().intValue()) {
            //本人无法禁用\启用自己
            if (userInput.getEnable() != null && !userDb.getEnable().equals(userInput.getEnable())) {
                return ApiResult.error(CLIENT_PARAMS_ERROR).appendMessage(
                        String.format("不能禁用自己#%s", userInput.getNickName()));
            }
        }


        userService.updateSelective(userInput);
        if (null != userEditReq.getRole()) {
            RoleAndUserRespVO roleAndUserRespVO = userService.findUserRoleById(userInput.getId());
            if (roleAndUserRespVO == null) {

                log.info("该用户:{}，在角色表不存在，进行新增!", userEditReq.getUserName());
                userService.addUserRole(userInput.getId(), userEditReq.getRole());
            } else {
                userService.updateUserRole(userInput.getId(), userEditReq.getRole());
            }

        }

        //重载菜单缓存数据
        permissionService.getNoCacheMenuTreeByUserId(userInput.getId());


        return ApiResult.success(MapBuilder.getSimpleMap("id", userInput.getId())).message("用户信息修改成功");
    }

    @ApiOperation(value = "/change_password", notes = "修改密码")
    @RequestMapping(value = "/change_password", method = {RequestMethod.POST})
    @Record(module = PORTAL_MODULE_USER, operation = PORTAL_LOG_OPERATION_USER_CHANGE_PASSWORD)
    public ApiResult changeUserPassword(@Valid @RequestBody ChangePassVO userInput,
                                        HttpServletRequest request) {
        if (userInput == null || userInput.getId() == null) {
            return ApiResult.error(CLIENT_PARAMS_NULL).appendMessage("user id is null");
        }
        if (CheckPassHelper.checkNumSeq(userInput.getPassword())) {
            return ApiResult.error(CLIENT_PARAMS_ERROR).appendMessage("不能包含有连续四位及以上顺序(或逆序)数字");
        }
        if (CheckPassHelper.checkSeqChar(userInput.getPassword())) {
            return ApiResult.error(CLIENT_PARAMS_ERROR).appendMessage("不能包含有连续四位及以上顺序(或逆序)字母，不区分大小写");
        }
        if (CheckPassHelper.isRepeat4Times(userInput.getPassword())) {
            return ApiResult.error(CLIENT_PARAMS_ERROR).appendMessage("不能包含有连续四位及以上重复字符，字母不区分大小写");
        }
        TUser userDb = userService.getById(userInput.getId());
        if (userDb == null) {
            return ApiResult.error(CLIENT_PARAMS_ERROR).appendMessage(
                    String.format("user is not existed#%d", userInput.getId()));
        }
        UserSession session = super.getUserSession(request);
        if (userDb.getId().intValue() != session.getUserId().intValue()) {
            return ApiResult.error(PERMISSION_NONE).appendMessage("只能修改自己密码");
        }

        // TODO 判断权限
        this.resetPassword(userDb, userInput.getPassword());

        userService.update(userDb);


        //退出登录
        loginOut(request);
        return ApiResult.success(MapBuilder.getSimpleMap("id", userInput.getId())).message("用户密码修改成功");
    }

    private void resetPassword(TUser userDb, String passwordInput) {

        // TODO 校验前次密码
        // 密码更改处理
        String salt = userDb.getSalt();
        if (StringHelper.isBlank(salt)) {
            salt = TUser.getRandomSalt();
            userDb.setSalt(salt);
        }

        String desPassword;
        try {
            desPassword = AESUtil.desEncrypt(passwordInput).trim();

        } catch (Exception e) {
            log.error("desEncrypt error", e);
            desPassword = passwordInput;
        }


        String newPassword = SecurityHelper.getMd5WithSalt(desPassword, userDb.getSalt());
        userDb.setPassword(newPassword);
    }

    private void loginOut(HttpServletRequest request) {
        if (GlobalConstants.USER_VERIFY_JWT.equalsIgnoreCase(userVerify)) {
            String token = request.getHeader(SESSION_KEY_TOKEN);
            if (token != null) {
                loginCacheHelper.logout(token);
            }
        } else {
            HttpSession s = request.getSession();
            if (s != null) {
                s.removeAttribute(SESSION_KEY_USER_ID);
                s.removeAttribute(SESSION_KEY_PASSPORT);
                s.removeAttribute(SESSION_KEY_NICK_NAME);
            }
        }
    }

    @ApiOperation(value = "/change_enable", notes = "启用/禁用用户")
    @RequestMapping(value = "/change_enable", method = {RequestMethod.POST})
    @Record(module = PORTAL_MODULE_USER, operation = PORTAL_LOG_OPERATION_ENABLE)
    public ApiResult changeUserEnable(@NotNull(message = "用户id必填") Integer id,
                                      @NotNull(message = "用户状态必填") @Min(value = 0) @Max(value = 1) Integer status,
                                      HttpServletRequest request) {
        UserRespVO user = userService.findUserByIdWHJG(id);
        if (user == null) {
            return ApiResult.error(CLIENT_PARAMS_ERROR).appendMessage(
                    String.format("用户不存在"));
        }

        if (user.getIsAdmin() != null && user.getIsAdmin() == 1) {
            return ApiResult.error(CLIENT_PARAMS_ERROR).appendMessage(
                    String.format("不能禁用超管"));
        }
        // TODO 判断权限

        // 不能禁用自己
        UserSession session = super.getUserSession(request);
        if (session.getUserId().intValue() == id) {
            return ApiResult.error(CLIENT_PARAMS_ERROR).appendMessage(
                    String.format("不能禁用自己#%d", user.getNickName()));
        }

        Map<Object, String> enableMap = constantItemService.findItemsMapByDict(GlobalConstants.MODULE_ENABLE_STATUS);
        String statusName = enableMap.get(status);


        userService.changeUserEnableStatus(id, status);
        return ApiResult.success(MapBuilder.getSimpleMap("id", id));
    }


    @ApiOperation(value = "/delete", notes = "删除用户")
    @RequestMapping(value = "/delete", method = {RequestMethod.POST})
    @Record(module = PORTAL_MODULE_USER, operation = PORTAL_LOG_OPERATION_ONE_DELETE)
    public ApiResult delete(@NotNull(message = "用户id必填") Integer id,
                            HttpServletRequest request) {
        UserRespVO user = userService.findUserByIdWHJG(id);
        if (user == null) {
            return ApiResult.error(String.format("用户不存在"));
        }

        // 不能删除自己
        UserSession session = super.getUserSession(request);
        if (session.getUserId().intValue() == id) {
            return ApiResult.error(String.format("不能删除自己%s", session.getNickName()));
        }
        if (user.getEnable() == STATUS_ENABLE) {
            return ApiResult.error(String.format("请先禁用%s", user.getNickName()));
        }

        //
        if (user.getIsAdmin() != null && user.getIsAdmin() == 1) {
            return ApiResult.error("不能删除超级管理员!");
        }

        Map<Object, String> enableMap = constantItemService.findItemsMapByDict(GlobalConstants.MODULE_DELETE);
        String statusName = enableMap.get(1);


        TUser deleteVO = new TUser();
        deleteVO.setId(id);
//        userService.updateSelective(deleteVO);
        //这里就直接删除用户
//        userService.delete(deleteVO);
        userService.deleteUserById(id);
        log.warn("删除用户:{}成功！操作者:{}", user.getUserName(), session.getUserName());
        return ApiResult.success(MapBuilder.getSimpleMap("id", id));
    }
}
