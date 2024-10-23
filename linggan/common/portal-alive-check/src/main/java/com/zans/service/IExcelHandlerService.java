package com.zans.service;

import com.zans.vo.ApiResult;

/**
 * @author beixing
 * @Title: collect-server
 * @Description: excel的处理类
 * @Version:1.0.0
 * @Since:jdk1.8
 * @date 2022/2/28
 */
public interface IExcelHandlerService {

     ApiResult excelTransform(String filePath);
}
