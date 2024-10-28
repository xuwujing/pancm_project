package com.zans.mms.service;

import com.zans.base.vo.ApiResult;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author pancm
 * @Title: zans-mms-server
 * @Description:
 * @Version:1.0.0
 * @Since:jdk1.8
 * @date 2021/1/21
 */
public interface IFileDownService {

    void image(String adjunctId , Integer type, HttpServletRequest request, HttpServletResponse response);


    ApiResult upLoadImg(String adjunctId,MultipartFile[] files,Integer type,String module,String number);

    ApiResult delImg(String adjunctId,Long id);


}
