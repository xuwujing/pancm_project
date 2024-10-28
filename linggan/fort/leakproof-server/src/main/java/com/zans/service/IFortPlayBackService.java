package com.zans.service;

import com.zans.model.FortPlayBack;
import com.zans.utils.ApiResult;
import com.zans.vo.FortReserveVO;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author qiyi
 * @Title: leakproof-server
 * @Description:
 * @Version:1.0.0
 * @Since:jdk1.8
 * @date 2021/7/1
 */
public interface IFortPlayBackService {

    int insertPlayBackInfo(FortPlayBack fortPlayBack);

    ApiResult getPlayBack(Integer id);

//    ApiResult replyAudit(FortPlayBackVO fortPlayBackVO);

    void download(Integer id, HttpServletRequest request, HttpServletResponse response);

    ApiResult replyAuditData(FortReserveVO fortReserveVO,HttpServletRequest request);

    ApiResult replyAuditVideo(Integer id);

    ApiResult replyAuditDetail(Integer id);

//    ApiResult test(FortReserveVO reserveVO);

}
