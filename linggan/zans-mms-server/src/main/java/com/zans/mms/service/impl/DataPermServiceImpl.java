package com.zans.mms.service.impl;

import com.zans.base.util.LoginCacheHelper;
import com.zans.base.vo.UserSession;
import com.zans.mms.config.PermissionConstans;
import com.zans.mms.service.IDataPermService;
import com.zans.mms.service.IPermissionService;
import com.zans.mms.vo.perm.DataPermVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("dataPermService")
public class DataPermServiceImpl implements IDataPermService {
    @Autowired
    IPermissionService permissionService;
    @Autowired
    LoginCacheHelper loginCacheHelper;

    @Override
    public DataPermVO getPcTicketPerm(UserSession userSession) {
        return permissionService.getDataPermByUserAndPermId(userSession, PermissionConstans.PERM_ID_PC_TICKET);
    }

    @Override
    public DataPermVO getPcPatrolLogPerm(UserSession userSession) {
        return permissionService.getDataPermByUserAndPermId(userSession, PermissionConstans.PERM_ID_PC_PATROL_LOG);
    }

    @Override
    public DataPermVO getPcPatrolTaskPerm(UserSession userSession) {
        return permissionService.getDataPermByUserAndPermId(userSession, PermissionConstans.PERM_ID_PC_PATROL_TASK);
    }

    @Override
    public DataPermVO getPcDeviceReportPerm(UserSession userSession) {
        return permissionService.getDataPermByUserAndPermId(userSession, PermissionConstans.PERM_ID_DEVICE_REPORT);
    }

    @Override
    public DataPermVO getAppTicketPerm(UserSession userSession) {
        return permissionService.getDataPermByUserAndPermId(userSession, PermissionConstans.PERM_ID_APP_TICKET);
    }

    @Override
    public DataPermVO getAppPatrolPerm(UserSession userSession) {
        return permissionService.getDataPermByUserAndPermId(userSession, PermissionConstans.PERM_ID_APP_PATROL);
    }


    @Override
    public DataPermVO getAppHomeTicketPerm(UserSession userSession) {
        return permissionService.getDataPermByUserAndPermId(userSession, PermissionConstans.PERM_ID_APP_HOME_TICKET);
    }

    @Override
    public DataPermVO getAppHomePatrolPerm(UserSession userSession) {
        return permissionService.getDataPermByUserAndPermId(userSession, PermissionConstans.PERM_ID_APP_HOME_PATROL);
    }

    @Override
    public DataPermVO getAppDeviceReportPerm(UserSession userSession) {
        return permissionService.getDataPermByUserAndPermId(userSession, PermissionConstans.PERM_ID_APP_DEVICE_REPORT);
    }

    @Override
    public DataPermVO getAppPatrolTopAggPerm(UserSession userSession) {
        return permissionService.getDataPermByUserAndPermId(userSession, PermissionConstans.PERM_ID_APP_PATROL_TOP_AGG);
    }

    @Override
    public DataPermVO getTopDataPerm(UserSession userSession) {
        return permissionService.getTopDataPerm(userSession);
    }
}
