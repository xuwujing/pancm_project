package com.zans.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.zans.dao.FortPlayBackMapper;
import com.zans.dao.FortReserveMapper;
import com.zans.model.FortPlayBack;
import com.zans.model.FortReserve;
import com.zans.model.FortServerConfig;
import com.zans.model.UserSession;
import com.zans.service.IFortPlayBackService;
import com.zans.utils.ApiResult;
import com.zans.utils.HttpHelper;
import com.zans.vo.FortPlayBackVO;
import com.zans.vo.FortReserveVO;
import com.zans.vo.PageResult;
import com.zans.vo.SessionAuditVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.zans.constant.SystemConstant.*;

/**
 * @author qiyi
 * @Title: leakproof-server
 * @Description:
 * @Version:1.0.0
 * @Since:jdk1.8
 * @date 2021/7/1
 */
@Service("fortPlayBackService")
@Slf4j
public class FortPlayBackServiceImpl implements IFortPlayBackService {

    @Autowired
    private FortPlayBackMapper fortPlayBackMapper;

    @Autowired
    private FortReserveMapper fortReserveMapper;

    @Autowired
    protected HttpHelper httpHelper;

    @Value("${video.playPath}")
    private String playPath;

    @Value("${guacamole.videoUrl}")
    private String bathVideoPath;

    private static final SimpleDateFormat simpleDateFormatFor_yyyy_MM_dd_HH_mm_ss = new SimpleDateFormat(DATE_FORMAT);

    @Override
    public int insertPlayBackInfo(FortPlayBack fortPlayBack) {

        return fortPlayBackMapper.insertPlayBackInfo(fortPlayBack);
    }

    @Override
    public ApiResult getPlayBack(Integer id) {
        FortPlayBack fortPlayBack = new FortPlayBack();
        fortPlayBack.setId(id);
        try {
            fortPlayBack = fortPlayBackMapper.selectByPrimaryKey(fortPlayBack);
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(playPath);
            stringBuilder.append(fortPlayBack.getPlayBackUrlGuac().substring(21, fortPlayBack.getPlayBackUrlGuac().length()) + ".mp4");
            return ApiResult.success(stringBuilder.toString());
        } catch (Exception e) {
            return ApiResult.error("id有误");
        }
    }

    @Override
    public void download(Integer id, HttpServletRequest request, HttpServletResponse response) {
        List<FortPlayBack> fortPlayBacks = fortPlayBackMapper.select(new FortPlayBack(id));
        FileInputStream fis = null;
        OutputStream os = null;
        if (!CollectionUtils.isEmpty(fortPlayBacks)) {
            FortPlayBack fortPlayBack = fortPlayBacks.get(0);
            try {
                download(fortPlayBack.getPlayBackUrlGuac() + ".mp4", fortPlayBack.getFileName().substring(0, fortPlayBack.getFileName().lastIndexOf(".")) + ".mp4", request, response);
            } catch (Exception e) {
                log.info(e.toString());
            }
        } else {
            log.info("数据缺失");
        }
    }

    @Override
    public ApiResult replyAuditData(FortReserveVO reserveVO, HttpServletRequest request) {
        Page<SessionAuditVO> page = PageHelper.startPage(reserveVO.getPageNum(), reserveVO.getPageSize());
        List<SessionAuditVO> queryNoEmptyAudit = fortReserveMapper.queryNoEmptyAudit(new FortReserveVO(reserveVO.getServerName(), reserveVO.getStartTime(), reserveVO.getEndTime(), reserveVO.getProposer(), simpleDateFormatFor_yyyy_MM_dd_HH_mm_ss.format(new Date())));
        List<FortReserveVO> result = new ArrayList<>();
        for (SessionAuditVO sessionAuditVO : queryNoEmptyAudit) {
            FortReserve fortReserve = new FortReserve();
            FortReserveVO fortReserveVO = new FortReserveVO();
            fortReserve.setId(sessionAuditVO.getFortReserveId());
            fortReserve = fortReserveMapper.selectByPrimaryKey(fortReserve);
            if (fortReserve == null) {
                continue;
            }
            BeanUtils.copyProperties(fortReserve, fortReserveVO);
            fortReserveVO.setServerName(sessionAuditVO.getServerName());
            fortReserveVO.setServerApplication(sessionAuditVO.getServerApplication());
            result.add(fortReserveVO);
        }
        for (FortReserveVO vo : result) {
            vo.setVideoStatus(fortReserveMapper.videoStatus(vo) > 0 ? HAS_DECODING : NO_DECODE);
            reserveDataInfo(vo);
            vo.setAppointmentDate(getAppointmentDate(vo));
        }
        return ApiResult.success(new PageResult<>(page.getTotal(), result, reserveVO.getPageSize(), reserveVO.getPageNum()));
    }

    @Override
    public ApiResult replyAuditVideo(Integer id) {
        if (id <= 0) {
            return ApiResult.error("id error");
        }
        FortReserve fortReserve = new FortReserve();
        fortReserve.setId(id);
        fortReserve = fortReserveMapper.selectByPrimaryKey(fortReserve);
        List<FortPlayBack> fortPlayBacks = new ArrayList<>();
        if (!ObjectUtils.isEmpty(fortReserve)) {
            FortPlayBackVO fortPlayBackVO = new FortPlayBackVO();
            fortPlayBackVO.setServerIp(fortReserve.getServerIp());
            fortPlayBackVO.setStartTime(fortReserve.getStartTime());
            fortPlayBackVO.setEndTime(fortReserve.getEndTime());
            fortPlayBacks = fortReserveMapper.replyAuditVideoSql(fortPlayBackVO);
        } else {
            return ApiResult.error("数据异常");
        }
        List<String> result = new ArrayList<>();
        for (FortPlayBack fortPlayBack : fortPlayBacks) {
            StringBuilder videoUrl = new StringBuilder();
            videoUrl.append(playPath);
            videoUrl.append(fortPlayBack.getPlayBackUrlGuac().substring(bathVideoPath.length()) + ".mp4");//播放基路径
            result.add(videoUrl.toString());
        }

        return ApiResult.success(result);
    }

    @Override
    public ApiResult replyAuditDetail(Integer id) {
        FortReserveVO queryData = new FortReserveVO();
        queryData.setId(id);
        List<FortReserveVO> fortReserveVOS = fortReserveMapper.sessionAudit(queryData);
        List<FortPlayBack> fortPlayBacks = new ArrayList<>();
        if (!CollectionUtils.isEmpty(fortReserveVOS)) {
            fortPlayBacks = fortPlayBackMapper.selectTimeQuantum(fortReserveVOS.get(0));
        }
        List<FortPlayBackVO> result = new ArrayList<>();
        for (FortPlayBack fortPlayBack : fortPlayBacks) {
            FortPlayBackVO fortPlayBackVO = new FortPlayBackVO();
            BeanUtils.copyProperties(fortPlayBack, fortPlayBackVO);
            fortPlayBackVO.setStartTime(fortPlayBack.getCreateTime());
            fortPlayBackVO.setEndTime(fortPlayBack.getEndTime());

            StringBuilder videoUrl = new StringBuilder();
            videoUrl.append(playPath);
            if (!StringUtils.isEmpty(fortPlayBack.getPlayBackUrlGuac())) {
                videoUrl.append(fortPlayBack.getPlayBackUrlGuac().substring(bathVideoPath.length()) + ".mp4");
            } else {
                videoUrl.delete(0, videoUrl.length());
            }
            fortPlayBackVO.setPlayBackUrl(videoUrl.toString());

            result.add(fortPlayBackVO);
        }
        return ApiResult.success(result);
    }

    private void assign(List<FortServerConfig> fortServerConfigs, FortReserveVO fortReserveVO) {
        for (FortServerConfig serverConfig : fortServerConfigs) {
            if (serverConfig.getServerIp().equals(fortReserveVO.getServerIp())) {
                fortReserveVO.setServerName(serverConfig.getServerName());
                fortReserveVO.setServerApplication(serverConfig.getServerApplication());
                return;
            }
        }
    }

    private static String getAppointmentDate(FortReserveVO vo) {
        StringBuilder appointmentDate = new StringBuilder();
        appointmentDate.append(vo.getStartTime());
        appointmentDate.append("-");
        appointmentDate.append(vo.getEndTime());
        return appointmentDate.toString();
    }

    private void reserveDataInfo(FortReserveVO vo) {
        if (simpleDateFormatFor_yyyy_MM_dd_HH_mm_ss.format(new Date()).compareTo(vo.getEndTime()) > 0) {
            vo.setTakingTime(RESERVE_FINISH);
            vo.setStatus(FINISHED);
        } else if (vo.getStartTime().compareTo(simpleDateFormatFor_yyyy_MM_dd_HH_mm_ss.format(new Date())) > 0) {
            vo.setTakingTime(RESERVE_NO_BEGIN);
            if (vo.getStatus() == PASS_APPLY) {
                vo.setStatus(TO_START);
            }
        } else {
            if (vo.getStatus() == PASS_APPLY) {
                vo.setStatus(UNDERWAY);
            }
            try {
                vo.setTakingTime(RESERVE_UNDERWAY + getDatePoor(simpleDateFormatFor_yyyy_MM_dd_HH_mm_ss.parse(vo.getEndTime()), new Date()));
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        FortReserve fortReserve = new FortReserve();
        BeanUtils.copyProperties(vo, fortReserve);
        fortReserveMapper.updateByPrimaryKeySelective(fortReserve);
    }

    protected void download(String filePath,
                            String fileCnName,
                            HttpServletRequest request,
                            HttpServletResponse response) throws Exception {

        try {
            String encodingName = URLEncoder.encode(fileCnName, "UTF-8");
            File file = new File(filePath);
            log.info("文件路径:{}", filePath);
            InputStream is = new FileInputStream(file);
            response.reset();
            this.setCors(request, response);
            response.setContentType("application/force-download");
            response.setHeader("Content-Disposition", "attachment;filename=" + encodingName);
            byte[] buff = new byte[1024];
            BufferedInputStream bis = null;
            OutputStream os = null;
            try {
                os = response.getOutputStream();
                bis = new BufferedInputStream(is);
                int i = 0;
                while ((i = bis.read(buff)) != -1) {
                    os.write(buff, 0, i);
                    os.flush();
                }
            } finally {
                if (bis != null) {
                    bis.close();
                }
            }

        } catch (Exception ex) {
            String message = "download error#" + filePath;
            log.error(message, ex);
        }
    }

    protected void setCors(HttpServletRequest request,
                           HttpServletResponse response) {
        String origin = request.getHeader("Origin");
        if (StringUtils.isEmpty(origin)) {
            origin = "*";
        }
        String rh = request.getHeader("Access-Control-Request-Headers");
        if (StringUtils.isEmpty(origin)) {
            rh = "DNT,X-Mx-ReqToken,Keep-Alive,User-Agent,X-Requested-With,If-Modified-Since,Cache-Control," +
                    "Content-Type,Authorization,SessionToken,Content-Disposition";
        }
        // 解决跨域后，axios 无法获得其它 Response Header，默认只有 Content-Language，Content-Type，Expires，Last-Modified，Pragma
        response.setHeader("Access-Control-Expose-Headers", "Content-Disposition,Error-Code,Error-Message");
        response.setHeader("Access-Control-Allow-Origin", origin);
        response.setHeader("Access-Control-Allow-Credentials", "true");
        response.setHeader("Access-Control-Allow-Headers", rh);
    }

    protected static String getDatePoor(Date endDate, Date nowDate) {
        long nd = 1000 * 24 * 60 * 60;
        long nh = 1000 * 60 * 60;
        long nm = 1000 * 60;
        long sss = 1000;
        long diff = endDate.getTime() - nowDate.getTime();
        long day = diff / nd;
        long hour = diff % nd / nh;
        long min = diff % nd % nh / nm;
        int seconds = (int) (diff % nd % nh % nm / sss);
        if (day != 0) {
            return day + "天" + hour + "小时" + min + "分钟" + seconds + "秒";
        } else if (hour != 0) {
            return hour + "小时" + min + "分钟" + seconds + "秒";
        } else if (min != 0) {
            return min + "分钟" + seconds + "秒";
        }
        return seconds + "秒";
    }

    public UserSession getUserSession(HttpServletRequest request) {
        return this.httpHelper.getUser(request);
    }
}
