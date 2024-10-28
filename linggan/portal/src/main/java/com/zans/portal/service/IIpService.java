package com.zans.portal.service;

import com.zans.base.service.BaseService;
import com.zans.base.vo.ApiResult;
import com.zans.base.vo.PageResult;
import com.zans.base.vo.UserSession;
import com.zans.portal.model.IpAll;
import com.zans.portal.vo.ip.ExcelIpAllVO;
import com.zans.portal.vo.ip.IpSearchVO;
import com.zans.portal.vo.ip.IpVO;
import com.zans.portal.vo.radius.QzViewRespVO;

import java.util.List;

public interface IIpService extends BaseService<IpAll> {

    PageResult<IpVO> getIpPage(IpSearchVO reqVO);

    IpVO getIp(Integer id);

    /**
     * 查询上次分配的最大IP
     * 用 inet_aton(ip_addr)，按 10 进制排序
     * @param area
     * @param deviceType
     * @return
     */
    IpVO findLastAssignIp(Integer area, Integer deviceType);

    /**
     * 默认的IP
     * @param area
     * @param deviceType
     * @return
     */
    IpVO findDefaultAssignIp(Integer area, Integer deviceType);

    /**
     * 删除自动分配的IP
     * @param allocId
     * @return
     */
    int deleteIpByAlloc(Integer allocId);

    /**
     * 通过 or 否决
     * @param ipItem
     * @param session
     * @param status
     * @param mark
     */
    void doApprove(IpAll ipItem, UserSession session, Integer status, String mark);


    QzViewRespVO findByIp(String ip);




    ApiResult readIpAllFile(String newFileName, String originName,UserSession userSession,Integer assetBranchId,Integer assetGuardLineId);


     void dealExcelIpAll(List<ExcelIpAllVO> list,Integer assetBranchId,Integer assetGuardLineId,UserSession userSession);


    /**
     * 根据IP查找
     * @param ip
     * @return
     */
    IpAll findByIpAddr(String ip);
}
