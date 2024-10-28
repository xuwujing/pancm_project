package com.zans.mms.service;

import com.zans.base.vo.ApiResult;
import com.zans.mms.vo.MacWhiteVO;

import java.util.List;

/**
 * @author qiyi
 * @Title: zans-demo
 * @Description:
 * @Version:1.0.0
 * @Since:jdk1.8
 * @date 2021/6/28
 */
public interface IMacWhiteService {

    ApiResult selectAllMac(MacWhiteVO macWhiteVO);

    ApiResult insertMac(MacWhiteVO macWhiteVO);

    ApiResult delMac(String ids);

}
