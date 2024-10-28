package com.zans.mms.service;

import com.zans.base.vo.UserSession;
import com.zans.mms.vo.perm.DataPermVO;
/**
* @Title: IDataPermService
* @Description: 数据权限Service
* @Version:1.0.0
* @Since:jdk1.8
* @author beiming
* @date 4/9/21
*/
public interface IDataPermService {

    DataPermVO getPcTicketPerm(UserSession userSession);

    DataPermVO getPcPatrolLogPerm(UserSession userSession);

    DataPermVO getPcPatrolTaskPerm(UserSession userSession);

    DataPermVO getPcDeviceReportPerm(UserSession userSession);

    DataPermVO getAppTicketPerm(UserSession userSession);

    DataPermVO getAppPatrolPerm(UserSession userSession);

    DataPermVO getAppHomeTicketPerm(UserSession userSession);

    DataPermVO getAppHomePatrolPerm(UserSession userSession);

    DataPermVO getAppDeviceReportPerm(UserSession userSession);


    DataPermVO getAppPatrolTopAggPerm(UserSession userSession);

    /**
    * @Author beixing
    * @Description  获取PC、APP首页的展示数据权限
     * 和北授确认，以角色的单位为准，
     * 运维单位只能看到自己的，
     * 其他的单位可以看到所有的
    * @Date  2021/4/1
    * @Param
    * @return
    **/
    DataPermVO getTopDataPerm(UserSession userSession);

}
