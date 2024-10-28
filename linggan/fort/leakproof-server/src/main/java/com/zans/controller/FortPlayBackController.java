package com.zans.controller;

import com.zans.service.IFortPlayBackService;
import com.zans.utils.ApiResult;
import com.zans.vo.FortReserveVO;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author qiyi
 * @Title: leakproof-server
 * @Description:
 * @Version:1.0.0
 * @Since:jdk1.8
 * @date 2021/7/2
 */
@Api(tags = "回放")
@RestController
@RequestMapping("playBack")
@Slf4j
public class FortPlayBackController {

    @Autowired
    private IFortPlayBackService fortPlayBackService;

    @RequestMapping(value = "getPlayBack", method = RequestMethod.GET)
    public ApiResult getPlayBack(@RequestParam("id") Integer id) {
        return fortPlayBackService.getPlayBack(id);
    }

//    /**
//     * 会话审计
//     * @param fortPlayBackVO
//     * @return
//     */
//    @RequestMapping(value = "replyAudit",method = RequestMethod.POST)
//    public ApiResult replyAudit(@RequestBody FortPlayBackVO fortPlayBackVO){
//        return fortPlayBackService.replyAudit(fortPlayBackVO);
//    }

    /**
     * 会话审计页面下载
     */
    @RequestMapping(value = "downPlayBack",method = RequestMethod.GET)
    public void download(@RequestParam("id") Integer id, HttpServletRequest request, HttpServletResponse response){
        fortPlayBackService.download(id,request,response);
    }

    /**
     * 会话审计新页面
     */
    @RequestMapping(value = "replyAuditData",method = RequestMethod.POST)
    public ApiResult replyAuditData(@RequestBody FortReserveVO fortReserveVO,HttpServletRequest request){
        return fortPlayBackService.replyAuditData(fortReserveVO,request);
    }

    /**
     * 会话审计播放
     */
    @RequestMapping(value = "replyAuditVideo",method = RequestMethod.GET)
    public ApiResult replyAuditVideo(@RequestParam("id") Integer id){
        return fortPlayBackService.replyAuditVideo(id);
    }

    /**
     * 会话审计详情
     */
    @RequestMapping(value = "replyAuditInfo",method = RequestMethod.GET)
    public ApiResult replyAuditInfo(@RequestParam("id") Integer id){
        return fortPlayBackService.replyAuditDetail(id);
    }
}
