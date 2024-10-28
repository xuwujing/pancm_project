package com.zans.mms.service;

import java.util.List;

import com.zans.base.vo.ApiResult;
import com.zans.mms.model.SysVersionInfo;
import com.zans.mms.vo.SysServerVO;

public interface ISysVersionInfoService {

    List<SysVersionInfo> getVerifyResult();
    
    void reVerify();

    ///2021-08-09 暂不用 停止单个或多个服务 后续确认再删除
//    void stopAndStartService(List<SysServerVO> serverVOList);

    void stopAndStartAllService();

    ApiResult stopAndStartGuacamole(String status);

    ApiResult trigToGuacamole();
}
