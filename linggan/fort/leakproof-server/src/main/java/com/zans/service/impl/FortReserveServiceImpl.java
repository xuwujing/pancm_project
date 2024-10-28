package com.zans.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.zans.config.MyTools;
import com.zans.config.RedisUtil;
import com.zans.config.WebSocketTunnel;
import com.zans.dao.*;
import com.zans.model.*;
import com.zans.service.IFortReserveService;
import com.zans.utils.PageResult;
import com.zans.utils.*;
import com.zans.vo.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpEntity;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.HttpHostConnectException;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;

import static com.zans.config.GlobalConstants.AGENT_SERVER;
import static com.zans.config.GlobalConstants.FILE_URL_LOGIN;
import static com.zans.constant.SystemConstant.*;
import static com.zans.utils.FTPUtil.testFtp;


/**
 * @author qiyi
 * @Title: leakproof-server
 * @Description:
 * @Version:1.0.0
 * @Since:jdk1.8
 * @date 2021/6/23
 */
@Service("fortReserveService")
@Slf4j
public class FortReserveServiceImpl implements IFortReserveService {

    @Resource
    private FortReserveMapper fortReserveMapper;

    @Resource
    private FortServerConfigDao fortServerConfigDao;

    @Resource
    private FortAgentServerMapper fortAgentServerMapper;

    @Autowired
    protected HttpHelper httpHelper;

    @Resource
    private SysUserMapper sysUserMapper;

    @Autowired
    RedisUtil redisUtil;

    @Resource
    FortUserFileDao fortUserFileDao;

    @Value("${sftp.host}")
    private String host;

    @Value("${sftp.port}")
    private Integer port;

    @Value("${sftp.username}")
    private String username;

    @Value("${sftp.password}")
    private String password;

    @Value("${video.size}")
    private Integer videoSize;

    private static final SimpleDateFormat simpleDateFormatFor_yyyy_MM_dd = new SimpleDateFormat(APPOINTMENT_DATE_FORMAT);

    private static final SimpleDateFormat simpleDateFormatFor_yyyyMMddHHmmssSSS = new SimpleDateFormat(RESERVE_DATE_FORMAT);

    private static final SimpleDateFormat simpleDateFormatFor_yyyy_MM_dd_HH_mm_ss = new SimpleDateFormat(DATE_FORMAT);

    @Override
    public ApiResult currentAppointment(FortReserveVO reserveVO, HttpServletRequest request) {
        UserSession userSession = getUserSession(request);
//        String proposer = userSession.getUserName();
//        FortReserveVO fortReserveVO = new FortReserveVO();
//        fortReserveVO.setProposer(proposer);
        reserveVO.setProposer(userSession.getUserName());
        Page<FortReserveVO> page = PageHelper.startPage(reserveVO.getPageNum(), reserveVO.getPageSize());
        List<FortReserveVO> selectCurrentAppointment = fortReserveMapper.sessionAudit(reserveVO);
        for (FortReserveVO vo : selectCurrentAppointment) {
            reserveDataInfo(vo);
            vo.setAppointmentDate(getAppointmentDate(vo));
            getTimeRemaining(vo);
            if (new Date().toString().compareTo(vo.getStartTime()) >= 0 && new Date().toString().compareTo(vo.getEndTime()) <= 0 && vo.getStatus() != 0 && vo.getStatus() != 2) {
                vo.setStatus(UNDERWAY);
            }
            fortReserveMapper.updateStatus(vo);
            String nowDate = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
            String startTime = vo.getStartTime();
            String endTime = vo.getEndTime();
            if (nowDate.equals(startTime.substring(0, startTime.indexOf(" ")))) {
                vo.setStartTime(startTime.substring(startTime.indexOf(" ") + 1, startTime.length()));
            }
            if (nowDate.equals(endTime.substring(0, endTime.indexOf(" ")))) {
                vo.setEndTime(endTime.substring(endTime.indexOf(" ") + 1, endTime.length()));
            }
        }
        return ApiResult.success(new PageResult<>(page.getTotal(), selectCurrentAppointment, reserveVO.getPageSize(), reserveVO.getPageNum()));
    }

    @Override
    public ApiResult appointmentNum() {
        List<FortReserveVO> selectCurrentAppointment = fortReserveMapper.sessionAudit(new FortReserveVO(null, simpleDateFormatFor_yyyy_MM_dd_HH_mm_ss.format(new Date()), null));
        List<FortReserveVO> selectAppointmentHistory = fortReserveMapper.sessionAudit(new FortReserveVO(null, null, simpleDateFormatFor_yyyy_MM_dd_HH_mm_ss.format(new Date())));
        return ApiResult.success(new AppointmentNumVO(selectCurrentAppointment.size(), selectAppointmentHistory.size()));
    }

    @Override
    public ApiResult applyForMachineHour(FortReserveVO reserveVO, HttpServletRequest request) {
        List<String> userChoose = new ArrayList<>();
        for (String s : reserveVO.getTime()) {
            userChoose.add(reserveVO.getDay() + " " + s);
        }
        boolean ifSelect = false;
        for (String startTime : userChoose) {
            FortReserveVO fortReserveVO = new FortReserveVO();
            fortReserveVO.setStartTime(startTime);
            fortReserveVO.setServerIp(reserveVO.getServerIp());
            FortReserve fortReserve = fortReserveMapper.checkIfSelect(fortReserveVO);
            if (fortReserve != null) {
                ifSelect = true;
            }
        }
        if (ifSelect) {
            return ApiResult.error("您预约的时间段已被其他用户预约,请刷新页面重新预约！");
        }
        UserSession userSession = getUserSession(request);
        SysUser user = sysUserMapper.findUserByName(userSession.getUserName());
        String reserveId = getRandomNumByDate();//生成随机数 作为每一次申请唯一标识
        String reserveReason = reserveVO.getReserveReason();//预约理由
        List<String> time = reserveVO.getTime();
        List<String> result = new ArrayList<>();
        if (videoSize < time.size()) {
            return ApiResult.error("单次不能预约超过" + videoSize + "个小时");
        }
        //此时段预约次数已达上限
        FortReserveVO fortReserveVO = new FortReserveVO();
        fortReserveVO.setStartTime(reserveVO.getDay());
        fortReserveVO.setEndTime(MyTools.addPlusDay(reserveVO.getDay(), 1));
        fortReserveVO.setServerIp(reserveVO.getServerIp());
        List<FortReserve> allReserve = fortReserveMapper.selectAllReserve(fortReserveVO);
        for (String s : reserveVO.getTime()) {
            String startTime = s;//小时段开始时间
            String endTime = plusOneHour(startTime);//小时段结束时间
            Integer isReserve = 0;
            for (FortReserve reserve : allReserve) {
                String reserveVOEndTime = reserve.getEndTime();
                if (reserve.getEndTime().compareTo(startTime) <= 0) {
                    continue;
                }
                if (reserveVOEndTime.compareTo(startTime) > 0 && reserveVOEndTime.compareTo(endTime) < 0) {
                    isReserve++;
                }
            }
            if (2 <= isReserve) {
                return ApiResult.error("该时段已不可预约，请刷新页面重新选择！");
            }
        }
        try {
            for (int i = 0; i < time.size(); i++) {
                String start = "";
                String end = "";
                start = time.get(i).split(":")[ARRAY_INDEX_FIRST];
                while (i < (getListSizeCut1(time)) && isNumAdd(Integer.parseInt(time.get(i).split(":")[ARRAY_INDEX_FIRST]), Integer.parseInt(time.get(i + 1).split(":")[ARRAY_INDEX_FIRST]))) {
                    end = time.get(i + 1).split(":")[ARRAY_INDEX_FIRST];
                    i++;
                }
                if (!StringUtils.hasText(end)) {
                    result.add(start);
                } else {
                    result.add(start + "-" + end);//将开始时间与结束时间拼接成08-12的形式
                }
            }
            //将前端传输的时间格式 08:00:00 09:00:00 10:00:00 12:00:00改为 08:00:00-10:00:00的形式减少数据库的插入词数 优化展示时数据条数
            for (String s : result) {
                String startTime = "";
                String endTime = "";
                startTime = reserveVO.getDay() + " " + s.split("-")[ARRAY_INDEX_FIRST] + TIME_HOUR_MIN;
                if (0 >= startTime.compareTo(simpleDateFormatFor_yyyy_MM_dd_HH_mm_ss.format(new Date()))) {
                    startTime = simpleDateFormatFor_yyyy_MM_dd_HH_mm_ss.format(new Date());//如果预约的时间段已开始 将开始时间改为当前
                }
                if (s.split("-").length == ARRAY_LENGTH) {
                    endTime = reserveVO.getDay() + " " + s.split("-")[ARRAY_INDEX_SECOND] + TIME_HOUR_MIN;//将08-12的结束时间拼接为2021-07-03 12:00:00
                } else {
                    endTime = reserveVO.getDay() + " " + s + TIME_HOUR_MIN;
                }
                Date dateStart = simpleDateFormatFor_yyyy_MM_dd_HH_mm_ss.parse(endTime);
                String originalTime = simpleDateFormatFor_yyyy_MM_dd_HH_mm_ss.format(dateStart);
                simpleDateFormatFor_yyyy_MM_dd_HH_mm_ss.setTimeZone(TimeZone.getTimeZone("GMT+0"));
                Date date = simpleDateFormatFor_yyyy_MM_dd_HH_mm_ss.parse(originalTime);
                simpleDateFormatFor_yyyy_MM_dd_HH_mm_ss.setTimeZone(TimeZone.getTimeZone("GMT+1"));//获取结束时间的下一个小时
                endTime = simpleDateFormatFor_yyyy_MM_dd_HH_mm_ss.format(date);
                //查询是否有其他预约时间冲突 begin
                FortReserve fortReserve = new FortReserve();
                fortReserve.setProposer(user.getUserName());
                fortReserve.setStartTime(startTime);
                fortReserve.setEndTime(endTime);
                List<FortReserveVO> fortReserves = fortReserveMapper.queryTimeConflict(fortReserve);
                if (fortReserves.size() > 0) {
                    for (FortReserveVO reserve : fortReserves) {
                        FortReserve delFortReserve = new FortReserve();
                        BeanUtils.copyProperties(reserve, delFortReserve);
                        fortReserveMapper.deleteByPrimaryKey(delFortReserve);
                    }
                }
                //查询是否有其他预约时间冲突 end
                simpleDateFormatFor_yyyy_MM_dd_HH_mm_ss.setTimeZone(TimeZone.getTimeZone("GMT+8"));
                if (user.getIsAdmin().equals(0)) {
                    fortReserveMapper.insertSelective(new FortReserve(null, reserveVO.getServerIp(), reserveId, startTime, endTime, APPLYING, reserveReason, user.getUserName(), "", "", null, ZansDateUtils.now(), ZansDateUtils.now()));
                } else {
                    String approveReason = "";
                    if (StringUtils.isEmpty(reserveReason)) {
                        if (user.getIsAdmin().equals(1)) {
                            reserveReason = "管理员预约自动通过";
                        } else {
                            reserveReason = "办案民警预约自动通过";
                        }
                    }
                    if (user.getIsAdmin().equals(1)) {
                        approveReason = "管理员审批自动通过";
                    } else {
                        approveReason = "办案民警审批自动通过";
                    }
                    fortReserveMapper.insertSelective(new FortReserve(null, reserveVO.getServerIp(), reserveId, startTime, endTime, PASS_APPLY, reserveReason, user.getUserName(), user.getUserName(), approveReason, simpleDateFormatFor_yyyy_MM_dd_HH_mm_ss.format(new Date()), ZansDateUtils.now(), ZansDateUtils.now()));
                }
                simpleDateFormatFor_yyyy_MM_dd_HH_mm_ss.setTimeZone(TimeZone.getTimeZone("GMT+8"));//恢复时区
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return ApiResult.success();
    }

    @Override
    public ApiResult timeConflict(FortReserveVO reserveVO, HttpServletRequest request) {
        UserSession userSession = getUserSession(request);
        SysUser user = sysUserMapper.findUserByName(userSession.getUserName());
        List<String> time = reserveVO.getTime();
        List<String> result = new ArrayList<>();
        try {
            for (int i = 0; i < time.size(); i++) {
                String start = "";
                String end = "";
                start = time.get(i).split(":")[ARRAY_INDEX_FIRST];
                while (i < (getListSizeCut1(time)) && isNumAdd(Integer.parseInt(time.get(i).split(":")[ARRAY_INDEX_FIRST]), Integer.parseInt(time.get(i + 1).split(":")[ARRAY_INDEX_FIRST]))) {
                    end = time.get(i + 1).split(":")[ARRAY_INDEX_FIRST];
                    i++;
                }
                if (!StringUtils.hasText(end)) {
                    result.add(start);
                } else {
                    result.add(start + "-" + end);//将开始时间与结束时间拼接成08-12的形式
                }
            }
            //将前端传输的时间格式 08:00:00 09:00:00 10:00:00 12:00:00改为 08:00:00-10:00:00的形式减少数据库的插入词数 优化展示时数据条数
            for (String s : result) {
                String startTime = "";
                String endTime = "";
                startTime = reserveVO.getDay() + " " + s.split("-")[ARRAY_INDEX_FIRST] + TIME_HOUR_MIN;
                if (s.split("-").length == ARRAY_LENGTH) {
                    endTime = reserveVO.getDay() + " " + s.split("-")[ARRAY_INDEX_SECOND] + TIME_HOUR_MIN;//将08-12的结束时间拼接为2021-07-03 12:00:00
                } else {
                    endTime = reserveVO.getDay() + " " + s + TIME_HOUR_MIN;
                }
                Date dateStart = simpleDateFormatFor_yyyy_MM_dd_HH_mm_ss.parse(endTime);
                String originalTime = simpleDateFormatFor_yyyy_MM_dd_HH_mm_ss.format(dateStart);
                simpleDateFormatFor_yyyy_MM_dd_HH_mm_ss.setTimeZone(TimeZone.getTimeZone("GMT+0"));
                Date date = simpleDateFormatFor_yyyy_MM_dd_HH_mm_ss.parse(originalTime);
                simpleDateFormatFor_yyyy_MM_dd_HH_mm_ss.setTimeZone(TimeZone.getTimeZone("GMT+1"));//获取结束时间的下一个小时
                endTime = simpleDateFormatFor_yyyy_MM_dd_HH_mm_ss.format(date);
                //查询是否有其他预约时间冲突 begin
                FortReserve fortReserve = new FortReserve();
                fortReserve.setProposer(user.getUserName());
                fortReserve.setStartTime(startTime);
                fortReserve.setEndTime(endTime);
                List<FortReserveVO> fortReserves = fortReserveMapper.queryTimeConflict(fortReserve);
                simpleDateFormatFor_yyyy_MM_dd_HH_mm_ss.setTimeZone(TimeZone.getTimeZone("GMT+8"));
                if (fortReserves.size() > 0) {
                    for (FortReserveVO reserve : fortReserves) {
                        if (reserve.getStartTime().compareTo(simpleDateFormatFor_yyyy_MM_dd_HH_mm_ss.format(new Date())) <= 0 && reserve.getStatus() != 0 && reserve.getStatus() != 2) {
                            return ApiResult.conflict("您有已开始的预约申请,本次预约失败");
                        }
                    }
                    for (FortReserveVO reserve : fortReserves) {
                        if (reserve.getStatus() == 0) {
                            return ApiResult.success("您提交的" + reserve.getServerName() + "主机" + reserve.getStartTime() + "-" + reserve.getEndTime() + "正在审批中，是否要覆盖之前的预约?");
                        }
                        return ApiResult.success("您的" + reserve.getServerName() + "主机" + reserve.getStartTime() + "-" + reserve.getEndTime() + "的审批已通过,是否要覆盖之前的预约?");
                    }
                }
                //查询是否有其他预约时间冲突 end
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
//        String successMessage = "1";
//        if (!"admin".equals(userSession.getUserName())){
//            successMessage = "预约成功";
//        }
        return ApiResult.success("1");
    }

    @Override
    public ApiResult chooseServer() {
        List<FortServerConfigVO> fortServerConfigVOS = fortServerConfigDao.selectAllServer();
        List<ServerNameVO> resultData = new ArrayList<>();
        for (FortServerConfigVO fortServerConfigVO : fortServerConfigVOS) {
            FortReserve fortReserve = new FortReserve();
            fortReserve.setServerIp(fortServerConfigVO.getServerIp());
            fortReserve.setStatus(0);
            List<FortReserve> select = fortReserveMapper.select(fortReserve);
            resultData.add(new ServerNameVO(fortServerConfigVO.getServerName(), fortServerConfigVO.getServerIp(), select.size()));
        }
        return ApiResult.success(resultData);
    }

    @Override
    public ApiResult queryReserve(String serverIp, int mouths) {
        FortReserve fortReserve = new FortReserve();
        fortReserve.setServerIp(serverIp);
        List<ReserveTimeVO> reserveTime = getReserveTime(serverIp, mouths);
        return ApiResult.success(reserveTime);
    }

    @Override
    public ApiResult queryReserveDay(String serverIp, String startTime) {
        String endTime = MyTools.addPlusDay(startTime, 1);
        FortReserveVO fortReserveVO = new FortReserveVO();
        fortReserveVO.setServerIp(serverIp);
        fortReserveVO.setStartTime(startTime);
        fortReserveVO.setEndTime(endTime);
        List<FortReserve> fortReserveVOS = fortReserveMapper.queryReserveDay(fortReserveVO);
        List<DayReserveHourVO> template = getTemplate();
        if (simpleDateFormatFor_yyyy_MM_dd.format(new Date()).compareTo(startTime) <= 0) {
            for (int i = 0; i < template.size(); i++) {
                for (FortReserve reserveVO : fortReserveVOS) {
                    if (template.get(i).getName().substring(0, 2).equalsIgnoreCase(reserveVO.getStartTime().substring(0, 2))) {
                        Integer index = Integer.parseInt(reserveVO.getEndTime().substring(0, 2)) - Integer.parseInt(reserveVO.getStartTime().substring(0, 2));
                        if (index > 0) {
                            for (Integer integer = 0; integer < index; integer++) {
                                template.get(i).setDisabled(false);
                                if (index >= 1 && integer != (index - 1)) {
                                    i++;
                                }
                            }
                        } else {//当index为负数时 为23:00:00-00:00:00
                            template.get(i).setDisabled(false);
                        }
                    }
                }
            }
            SimpleDateFormat hh_mm_ss_format = new SimpleDateFormat("HH");
            String hh_mm_ss_format_time = hh_mm_ss_format.format(new Date());
            //当天过去的时间直接置为不可选
            String format = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
            if (startTime.equals(format)) {//当天
                for (DayReserveHourVO dayReserveHourVO : template) {
                    if (hh_mm_ss_format_time.compareTo(dayReserveHourVO.getName()) > 0) {
                        dayReserveHourVO.setDisabled(false);
                    }
                }
            }
        } else {
            for (DayReserveHourVO dayReserveHourVO : template) {
                dayReserveHourVO.setDisabled(false);
            }
        }

        /**
         * 一小时最多预约两次
         */
        List<FortReserve> fortReserves = fortReserveMapper.selectAllReserve(fortReserveVO);
        for (DayReserveHourVO dayReserveHourVO : template) {
            Integer isReserve = 0;
            for (FortReserve reserveVO : fortReserves) {
                String reserveVOEndTime = reserveVO.getEndTime();
                String[] showTime = dayReserveHourVO.getDisplayTime().split("-");
                if (reserveVOEndTime.compareTo(showTime[0]) > 0 && reserveVOEndTime.compareTo(showTime[1]) < 0) {
                    isReserve++;
                }
            }
            if (2 <= isReserve) {
                dayReserveHourVO.setDisabled(false);
            }
        }
        return ApiResult.success(template);
    }

    @Override
    public ApiResult queryReserveWeek(String serverIp, String startTime) {
        List<FortReserve> finalData = new ArrayList<>();
        List<WeekReserveVO> weekReserveVOS = new ArrayList<>();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date parse = null;
        try {
            parse = sdf.parse(startTime);
        } catch (ParseException parseException) {
            parseException.printStackTrace();
        }
        Calendar c = Calendar.getInstance();
        c.setTime(parse);
        int weekday = c.get(Calendar.DAY_OF_WEEK);//返回2为周一
        List<String> getData = new ArrayList<>();
        if (weekday >= 2) {
            for (int i = 1; i < weekday - 1; i++) {
                String s = LocalDate.parse(sdf.format(parse)).minusDays(i).toString();
                getData.add(s);
            }
        }
        if (weekday <= 7) {
            for (int i = 1; i <= 8 - weekday; i++) {
                String s = LocalDate.parse(sdf.format(parse)).plusDays(i).toString();
                getData.add(s);
            }
        }
        getData.add(sdf.format(parse));
        List<String> newList = getData.stream().sorted(Comparator.comparing(String::toString))
                .collect(Collectors.toList());
        for (String s : newList) {
            try {
                String nextDay = LocalDate.parse(sdf.format(sdf.parse(s))).plusDays(1).toString();
                List<FortReserve> fortReserves = fortReserveMapper.queryIfReserve(new FortReserveVO(null, s, nextDay, serverIp));
                finalData.addAll(fortReserves);
            } catch (ParseException parseException) {
                log.error("error:{}", parseException);
                parseException.printStackTrace();
            }
        }
        for (int i = 0; i < finalData.size(); i++) {
            FortReserve fortReserve = finalData.get(i);
            WeekReserveVO weekReserveVO = new WeekReserveVO();
            weekReserveVO.setId(fortReserve.getId());
            weekReserveVO.setStartTime(fortReserve.getStartTime());
            weekReserveVO.setEndTime(fortReserve.getEndTime());
            weekReserveVO.setStatus(fortReserve.getStatus() == 0 ? 0 : 1);
            weekReserveVO.setTitle(fortReserve.getStatus() == 0 ? "待审批" : "已预约");
            weekReserveVO.setProposer(fortReserve.getProposer());
            weekReserveVOS.add(weekReserveVO);
        }
        List<WeekReserveVO> w = weekReserveVOS.stream().sorted(Comparator.comparing(WeekReserveVO::getStartTime)).collect(Collectors.toList());
        //添加字段start&end为前端展示时间 startTime&endTime为真实预约时间
        for (WeekReserveVO weekReserveVO : w) {
            String[] start = weekReserveVO.getStartTime().split(":");
            String[] end = weekReserveVO.getEndTime().split(":");
            String startYmd = weekReserveVO.getStartTime().split(" ")[0];//年月日
            if (Integer.parseInt(start[1]) == 0 && Integer.parseInt(start[2]) == 0) {
                weekReserveVO.setStart(weekReserveVO.getStartTime());//设置展示开始时间
            } else {
                weekReserveVO.setStart(startYmd + " " + getShowStartTime(weekReserveVO.getStartTime(), serverIp));
            }

            if (Integer.parseInt(end[1]) == 0 && Integer.parseInt(end[2]) == 0) {
                weekReserveVO.setEnd(weekReserveVO.getEndTime());//设置展示结束时间
            } else {
                String showEndTime = getShowEndTime(weekReserveVO.getEndTime(), serverIp);
                String newBathYmd = startYmd;
                if ("00:00:00".equals(showEndTime)) {
                    newBathYmd = MyTools.addPlusDay(startYmd, 1);
                }
                weekReserveVO.setEnd(newBathYmd + " " + showEndTime);
            }
        }
        return ApiResult.success(w);
    }

    @Override
    public ApiResult approveList(FortReserveVO fortReserveVO) {
        String format = simpleDateFormatFor_yyyy_MM_dd.format(new Date());
        Page<FortReserve> page = PageHelper.startPage(fortReserveVO.getPageNum(), fortReserveVO.getPageSize());
        List<FortReserveVO> select = fortReserveMapper.sessionAudit(fortReserveVO);
        for (FortReserveVO reserve : select) {
            if (format.equals(reserve.getStartTime().substring(0, reserve.getStartTime().indexOf(" ")))) {
                reserve.setStartTime(reserve.getStartTime().substring(reserve.getStartTime().indexOf(" ") + 1));
            }
            if (format.equals(reserve.getEndTime().substring(0, reserve.getEndTime().indexOf(" ")))) {
                reserve.setEndTime(reserve.getEndTime().substring(reserve.getEndTime().indexOf(" ") + 1));
            }
            if (format.equals(reserve.getCreateTime().substring(0, reserve.getCreateTime().indexOf(" ")))) {
                reserve.setCreateTime(reserve.getCreateTime().substring(reserve.getCreateTime().indexOf(" ") + 1));
            }
        }
        return ApiResult.success(new PageResult<>(page.getTotal(), select, fortReserveVO.getPageSize(), fortReserveVO.getPageNum()));
    }

    @Override
    public ApiResult approveListNum(FortReserveVO fortReserveVO) {
        List<FortReserveVO> select = fortReserveMapper.sessionAudit(fortReserveVO);
        Map<String, Integer> result = new HashMap<>();
        Integer approvalPending = 0;//待审批
        Integer passApprove = 0;//审批通过
        Integer approveFailed = 0;//审批驳回
        Integer approveAll = 0;//所有审批
        for (FortReserveVO reserveVO : select) {
            if (0 == reserveVO.getStatus()) {
                approvalPending++;
            } else if (2 == reserveVO.getStatus()) {
                approveFailed++;
            } else {
                passApprove++;
            }
            approveAll++;
        }
        result.put("approvalPending", approvalPending);
        result.put("passApprove", passApprove);
        result.put("approveFailed", approveFailed);
        result.put("approveAll", approveAll);
        return ApiResult.success(result);
    }

    @Override
    public ApiResult check(Integer id, HttpServletRequest request) {
        FortReserve fortReserve = new FortReserve();
        fortReserve.setId(id);
        fortReserve = fortReserveMapper.selectByPrimaryKey(fortReserve);
        String result = "";
        if (fortReserve != null) {
            switch (fortReserve.getStatus()) {
                case 0:
                    result = "该预约尚未审批通过,请稍后重试";
                    break;
                case 2:
                    result = "该预约已被否决,请重新预约";
                    break;
                case 1:
                case 3:
                    result = "该预约尚未开始,请稍后重试";
                    break;
                case 5:
                    result = "该预约已结束,请重新预约";
                    break;
            }
            if (!StringUtils.isEmpty(result)) {
                return ApiResult.error(result);
            }
            if (ipInUserMap.get(fortReserve.getServerIp()) != null && ipInUserMap.get(fortReserve.getServerIp())) {
                return ApiResult.error("该ip正在被使用！");
            }
        } else {
            return ApiResult.error("未找到该预约信息");
        }
        String format = simpleDateFormatFor_yyyy_MM_dd_HH_mm_ss.format(new Date());
        if (fortReserve.getEndTime().compareTo(format) <= 0) {
            return ApiResult.error("该预约已到期!请重新申请");
        }
        SysUser sysUser = new SysUser();
        sysUser.setUserName(fortReserve.getProposer());
        sysUser = sysUserMapper.selectOne(sysUser);
        if (sysUser == null) {
            return ApiResult.error("无该用户信息");
        }
        //判断主机是否在线
        if (isConnect(fortReserve.getServerIp(), CONNECT_PORT)) {
            try {
                if (testFtp(host,port,username,password)){
                    //判断水印服务是否正常
//                    if (startAgent(fortReserve.getServerIp())) {
                        //判断该用户该主机的文件服务是否正常
                        if (loginFileClient(fortReserve.getServerIp(), sysUser)) {
                            return ApiResult.success();
                        }
                        return ApiResult.error("登录文件客户端失败!");
//                    }
//                    return ApiResult.error("该主机agent服务异常,请联系管理员");
                }
                return ApiResult.error("ftp服务器未开启！请联系管理员");
            }catch (IOException e){
                log.info("IO-Error:{}",e);
                return ApiResult.error("服务器异常");
            }
        }
        return ApiResult.error("该主机不在线");
    }

    @Override
    public ApiResult approvalDetail(Integer id) {
        FortReserve fortReserve = new FortReserve();
        fortReserve.setId(id);
        fortReserve = fortReserveMapper.selectByPrimaryKey(fortReserve);
        if (fortReserve == null) {
            return ApiResult.error("无该预约信息");
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String startTime = fortReserve.getStartTime();
        String endTime = fortReserve.getEndTime();
        if (startTime.startsWith(sdf.format(new Date()))) {
            fortReserve.setStartTime(startTime.substring(startTime.indexOf(" ")));
        }
        if (endTime.startsWith(sdf.format(new Date()))) {
            fortReserve.setEndTime(endTime.substring(endTime.indexOf(" ")));
        }
        return ApiResult.success(fortReserve);
    }

    @Override
    public ApiResult approve(FortReserveVO fortReserveVO, HttpServletRequest request) {
        UserSession userSession = getUserSession(request);
        if (fortReserveVO.getId() == null) {
            return ApiResult.error("数据有误");
        }
        fortReserveVO.setApprover(userSession.getUserName());
        log.info("审批人:{}", userSession.getUserName());
        fortReserveVO.setApproveTime(simpleDateFormatFor_yyyy_MM_dd_HH_mm_ss.format(new Date()));
        fortReserveMapper.approve(fortReserveVO);
        return ApiResult.success();
    }

    @Override
    public FortReserve findFortReserveByIp(String ip) {
        FortReserveVO fortReserveVO = new FortReserveVO();
        fortReserveVO.setStartTime(simpleDateFormatFor_yyyy_MM_dd_HH_mm_ss.format(new Date()));
        fortReserveVO.setServerIp(ip);
        FortReserve fortReserveByIp = fortReserveMapper.findFortReserveByIp(fortReserveVO);
        return fortReserveByIp;
    }

    @Override
    public ApiResult terminationNow(Integer id) {
        FortReserve fortReserve = new FortReserve();
        fortReserve.setId(id);
        fortReserve = fortReserveMapper.selectByPrimaryKey(fortReserve);
        if (fortReserve == null) {
            return ApiResult.error("预约不存在");
        }
        fortReserve.setEndTime(simpleDateFormatFor_yyyy_MM_dd_HH_mm_ss.format(new Date()));
        fortReserve.setStatus(FINISHED);
        fortReserveMapper.updateByPrimaryKeySelective(fortReserve);
        return ApiResult.success();
    }

    public boolean startAgent(String ip) {
        HttpPost httpPost = new HttpPost(String.format(START_AGENT, ip));
        MultipartEntityBuilder builder = MultipartEntityBuilder.create();
        com.zans.vo.Pair pair = AGENT_SERVER.get(ip);
        if (pair == null) {
            log.info("agent-server ->" + ip + "->null ->add");
            FortAgentServer fortAgentServer = fortAgentServerMapper.queryByIp(ip);
            if (fortAgentServer == null){
                return false;
            }
            AGENT_SERVER.put(fortAgentServer.getServerIp(), new com.zans.vo.Pair(fortAgentServer.getPath(), fortAgentServer.getName()));
            log.info("add end->{}",AGENT_SERVER.get(ip));
        }
        String path = String.valueOf(AGENT_SERVER.get(ip).getKey());
        String name = String.valueOf(AGENT_SERVER.get(ip).getValue());
        builder.addTextBody("path", path);
        builder.addTextBody("name", name);
        log.info("ip:{},path:{},name:{}", ip, path, name);
        HttpEntity multipart = builder.build();
        RequestConfig requestConfig = RequestConfig.custom().setConnectTimeout(5000)
                .setConnectionRequestTimeout(5000).setSocketTimeout(5000).build();
        httpPost.setConfig(requestConfig);
        try {
            String responseJson = WebSocketTunnel.postSet(multipart, httpPost);
            ApiResult apiResult = JSONObject.parseObject(new String(responseJson.getBytes(), DEFAULT_CHARSET), ApiResult.class);
            log.warn("接口返回结果:{}", apiResult);
            if (apiResult.getCode() == 0) {
                return true;
            }
        } catch (IOException e) {
            log.error("连接失败!error:{}", e);
        } catch (JSONException e) {
            log.error("返回类型错误!error:{}", e);
        }
        return false;
    }

    public String getShowStartTime(String start, String serverIp) {
        String year_md = start.split(" ")[0];//年月日
        String hour_ms = start.split(" ")[1].split(":")[0] + ":00:00";//时分秒
        List<FortReserve> fortReserves = fortReserveMapper.queryIfExist(
                new FortReserveVO(null, year_md + " " + hour_ms, year_md + " " + plusOneHour(hour_ms), serverIp));//查询结果最多两条
        if (fortReserves.size() == 0) {
            //没有记录在此区间内结束 且该条记录起始时间不为整点
            return start.split(" ")[1].split(":")[0] + ":30:00";
        }
        if (fortReserves.size() == 1) {//两种情况 本身在此区间内 另外一条记录在
            if (fortReserves.get(0).getStartTime().equals(start)) {//为同一条记录
                return start.split(" ")[1].split(":")[0] + ":00:00";
            }
            //第二种 存在一条记录在此区间内结束
            return start.split(" ")[1].split(":")[0] + ":30:00";
        }
        if (fortReserves.size() == 2) {//存在两条记录在此区间内结束
            if (fortReserves.get(0).getStartTime().equals(start)) {//如果第一条是该记录 则返回整点
                return start.split(" ")[1].split(":")[0] + ":00:00";
            }
            return start.split(" ")[1].split(":")[0] + ":30:00";
        }
        return start;
    }

    public String getShowEndTime(String end, String serverIp) {
        String year_md = end.split(" ")[0];//年月日
        String hour_ms = end.split(" ")[1].split(":")[0] + ":00:00";//时分秒
        String showEndTime = end.split(" ")[1].split(":")[0] + ":30:00";//**:30:00
        String showEndTime_1 = plusOneHour(hour_ms);
        List<FortReserve> fortReserves = fortReserveMapper.queryIfExist(
                new FortReserveVO(null, year_md + " " + hour_ms, year_md + " " + plusOneHour(hour_ms), serverIp));//查询结果最多两条
        if (fortReserves.size() == 0) {
            return showEndTime;
        }
        if (fortReserves.size() == 1) {
//            if (fortReserves.get(0).getEndTime().equals(end)){
//                return showEndTime;
//            }
            return showEndTime;//两种情况均返回**:30:00
        }
        if (fortReserves.size() == 2) {
            if (fortReserves.get(0).getEndTime().equals(end)) {
                return showEndTime;
            }
            return showEndTime_1;
        }
        return end;
    }

    public UserSession getUserSession(HttpServletRequest request) {
        return this.httpHelper.getUser(request);
    }

    private static boolean isNumAdd(Integer i, Integer j) {
        if (j == i + 1) {
            return true;
        }
        return false;
    }

    public static Integer getListSizeCut1(List list) {
        if (list.size() == 0) {
            return 0;
        }
        return list.size() - 1;
    }

    private List<ReserveTimeVO> getReserveTime(String serverIp, int mouths) {
        String format = simpleDateFormatFor_yyyy_MM_dd.format(new Date());
        List<ReserveTimeVO> result = new ArrayList<>();
        List<String> timeQuantum = getTimeQuantum(mouths);
        for (int i = 0; i < timeQuantum.size() - 1; i++) {
            FortReserveVO fortReserveVO = new FortReserveVO();
            fortReserveVO.setServerIp(serverIp);
            fortReserveVO.setStartTime(timeQuantum.get(i));
            fortReserveVO.setEndTime(timeQuantum.get(i + 1));
            Integer count = fortReserveMapper.queryTimeStampCount(fortReserveVO);
            ReserveTimeVO reserveTimeVO = new ReserveTimeVO();
            reserveTimeVO.setDate(timeQuantum.get(i));
            if (timeQuantum.get(i).compareTo(format) >= 0) {
                if (count == DAY_RESERVE_NUM_MAX) {
                    reserveTimeVO.setTitle(RESERVE_STATUS_FULL);
                } else {
                    reserveTimeVO.setTitle(RESERVE_STATUS);
                }
            } else {
                reserveTimeVO.setTitle(RESERVE_STATUS_TIMEOUT);
            }

            result.add(reserveTimeVO);
        }
        return result;
    }

    private static String getAppointmentDate(FortReserveVO vo) {
        StringBuilder appointmentDate = new StringBuilder();
        appointmentDate.append(vo.getStartTime());
        appointmentDate.append("-");
        appointmentDate.append(vo.getEndTime());
        return appointmentDate.toString();
    }

    public static String getDatePoor(Date endDate, Date nowDate) {
        long nd = 1000 * 24 * 60 * 60;
        long nh = 1000 * 60 * 60;
        long nm = 1000 * 60;
        long sss = 1000;
        long diff = endDate.getTime() - nowDate.getTime();
        long day = diff / nd;
        long hour = diff % nd / nh;
        long min = diff % nd % nh / nm;
        long seconds = (int) (diff % nd % nh % nm / sss);
        if (hour != 0) {
            return "0" + hour + ":" + (min >= 10 ? min : ("0" + min)) + ":" + (seconds >= 10 ? seconds : ("0" + seconds));
        } else if (min != 0) {
            return "00:" + (min >= 10 ? min : ("0" + min)) + ":" + (seconds >= 10 ? seconds : ("0" + seconds));
        }
        return "00:00:" + (seconds >= 10 ? seconds : ("0" + seconds));
    }

    public static void getTimeRemaining(FortReserveVO vo) {
        try {
            if (new Date().getTime() >= simpleDateFormatFor_yyyy_MM_dd_HH_mm_ss.parse(vo.getStartTime()).getTime() && new Date().getTime() <= simpleDateFormatFor_yyyy_MM_dd_HH_mm_ss.parse(vo.getEndTime()).getTime()) {
                Date startTime = simpleDateFormatFor_yyyy_MM_dd_HH_mm_ss.parse(vo.getEndTime());
                long nd = 1000 * 24 * 60 * 60;
                long nh = 1000 * 60 * 60;
                long nm = 1000 * 60;
                long sss = 1000;
                long diff = startTime.getTime() - new Date().getTime();
                int day = (int) (diff / nd);
                int hour = (int) (diff % nd / nh);
                int min = (int) (diff % nd % nh / nm);
                int seconds = (int) (diff % nd % nh % nm / sss);
                vo.setLastTimeVO(new LastTimeVO(day, hour, min, seconds));
                if (!vo.getStatus().equals(0) && !vo.getStatus().equals(2)) {
                    vo.setStatus(UNDERWAY);
                }
            } else if (simpleDateFormatFor_yyyy_MM_dd_HH_mm_ss.parse(vo.getStartTime()).getTime() > new Date().getTime()) {
                if (!vo.getStatus().equals(0) && !vo.getStatus().equals(2)) {
                    vo.setStatus(TO_START);
                }
            } else {
                if (!vo.getStatus().equals(0) && !vo.getStatus().equals(2)) {
                    vo.setStatus(FINISHED);
                }
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public void reserveDataInfo(FortReserveVO vo) {
        if (!vo.getStatus().equals(0) && !vo.getStatus().equals(2)) {
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
                    vo.setTakingTime(getDatePoor(simpleDateFormatFor_yyyy_MM_dd_HH_mm_ss.parse(vo.getEndTime()), new Date()));
                } catch (ParseException e) {
                    log.info("时间转换异常:{}", e);
                } catch (NullPointerException nullPointerException) {
                    log.error("error:{}", nullPointerException);
                }
            }
            FortReserve fortReserve = new FortReserve();
            BeanUtils.copyProperties(vo, fortReserve);
            fortReserveMapper.updateByPrimaryKeySelective(fortReserve);
        }
    }

    /**
     * 获取一天的起始日期
     *
     * @return
     */
    private static Date getStartTime() {
        Calendar todayStart = Calendar.getInstance();
        todayStart.set(Calendar.HOUR, 0);
        todayStart.set(Calendar.MINUTE, 0);
        todayStart.set(Calendar.SECOND, 0);
        todayStart.set(Calendar.MILLISECOND, 0);
        return todayStart.getTime();
    }

    /**
     * 获取一天的结束日期
     *
     * @return
     */
    private static Date getEndTime() {
        Calendar todayEnd = Calendar.getInstance();
        todayEnd.set(Calendar.HOUR, 23);
        todayEnd.set(Calendar.MINUTE, 59);
        todayEnd.set(Calendar.SECOND, 59);
        todayEnd.set(Calendar.MILLISECOND, 999);
        return todayEnd.getTime();
    }

    /**
     * 获取指定月的每日的起始日期
     *
     * @param mouths
     * @return
     */
    private static List<String> getTimeQuantum(int mouths) {
        List<String> results = new ArrayList<>();
        try {
            String s1 = getFirstDayOfMonth(mouths);
            String s2 = getFirstDayOfMonth(mouths + 1);

            long start = simpleDateFormatFor_yyyy_MM_dd.parse(s1).getTime();
            long end = simpleDateFormatFor_yyyy_MM_dd.parse(s2).getTime();

            for (long i = start; i < end + (1000 * 24 * 3600); i = i + (1000 * 24 * 3600)) {
                Date date = new Date();
                date.setTime(i);
                String da = simpleDateFormatFor_yyyy_MM_dd.format(date);
                String ss1 = da + " 00:00:00";
                Date parse = simpleDateFormatFor_yyyy_MM_dd_HH_mm_ss.parse(ss1);
                Date date2 = new Date(parse.getTime());
                String format = simpleDateFormatFor_yyyy_MM_dd.format(date2);
                results.add(format);
            }

        } catch (ParseException e) {
            e.printStackTrace();
        }
        return results;
    }

    /**
     * 获取指定月份第一天
     *
     * @param month
     * @return
     */
    public static String getFirstDayOfMonth(int month) {
        Calendar calendar = Calendar.getInstance();
        // 设置月份
        calendar.set(Calendar.MONTH, month - 1);
        // 获取某月最小天数
        int firstDay = calendar.getActualMinimum(Calendar.DAY_OF_MONTH);
        // 设置日历中月份的最小天数
        calendar.set(Calendar.DAY_OF_MONTH, firstDay);
        // 格式化日期
        String firstDays = simpleDateFormatFor_yyyy_MM_dd.format(calendar.getTime()) + " 00:00:00";
        return firstDays;
    }

    public static String getRandomNumByDate() {
        StringBuilder reserveId = new StringBuilder();
        reserveId.append(simpleDateFormatFor_yyyyMMddHHmmssSSS.format(new Date()));
        reserveId.append(randomNum());
        return reserveId.toString();
    }

    /**
     * 生成四位随机数
     */
    public static String randomNum() {
        SecureRandom random = null;
        try {
            random = SecureRandom.getInstance(SECURE_RANDOM);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < 4; i++) {
            // 生成 0-9 随机整数
            int number = random.nextInt(10);
            stringBuilder.append(number + "");
        }
        return stringBuilder.toString();
    }

    public static List<DayReserveHourVO> getTemplate() {
        List<DayReserveHourVO> result = new ArrayList<>();
        for (Integer i = 1; i <= DAY_RESERVE_NUM_MAX; i++) {
            StringBuilder stringBuilder = new StringBuilder();
            DayReserveHourVO dayReserveHourVO = new DayReserveHourVO();
            dayReserveHourVO.setDisabled(true);
            dayReserveHourVO.setValue(i);
            if (i <= 2) {
                stringBuilder.append("0");
            }
            stringBuilder.append(i + 7);
            stringBuilder.append(TIME_HOUR_MIN);
            dayReserveHourVO.setName(stringBuilder.toString());
            result.add(dayReserveHourVO);
        }
        for (int i = 0; i < result.size(); i++) {
            String nowTime = result.get(i).getName();
            if (i != result.size() - 1) {
                String nextTime = result.get(i + 1).getName();
                result.get(i).setDisplayTime(nowTime + "-" + nextTime);
                result.get(i).setShowName(nowTime.substring(0, 5) + "-" + nextTime.substring(0, 5));
            } else {
                result.get(i).setDisplayTime(nowTime + "-00:00:00");
                result.get(i).setShowName(nowTime.substring(0, 5) + "-00:00");
            }
        }
        return result;
    }

    public static boolean getIfLogin(String ip, SysUser sysUser) {
        HttpPost httpPost = new HttpPost("http://" + ip + ":8081/edr/client/login");
        MultipartEntityBuilder builder = MultipartEntityBuilder.create();
        if (!StringUtils.isEmpty(sysUser.getUserName()) && !StringUtils.isEmpty(sysUser.getPassword())) {
            builder.addTextBody("phone", sysUser.getUserName());
            builder.addTextBody("password", sysUser.getPassword());
        } else {
            builder.addTextBody("phone", "admin");
            builder.addTextBody("password", "123456");
        }
        log.info("username:{}", StringUtils.isEmpty(sysUser.getUserName()) ? "lgwy" : sysUser.getUserName());
        HttpEntity multipart = builder.build();
        // 增加http请求超时设置
        RequestConfig requestConfig = RequestConfig.custom().setConnectTimeout(5000)
                .setConnectionRequestTimeout(5000).setSocketTimeout(5000).build();
        httpPost.setConfig(requestConfig);
        try {
            log.info("begin to checkIfLogin");
            log.info("request params mutpart:{},httpPost:{}", multipart, httpPost);
            String responseJson = WebSocketTunnel.postSet(multipart, httpPost);
            ApiResult apiResult = JSONObject.parseObject(new String(responseJson.getBytes(), "UTF-8"), ApiResult.class);
            log.warn("接口返回结果:{}", apiResult);
            if (apiResult.getCode() == 0) {
                return true;
            }
            if (apiResult.getCode() == 5000 && "".equals(apiResult.getData())) {//用户已登陆
                return true;
            }
        } catch (HttpHostConnectException httpHostConnectException) {
            log.error("主机agent服务未启动,error:{}", httpHostConnectException);
            return false;
        } catch (IOException e) {
            log.error("连接失败!error:{}", e);
            return false;
        } catch (JSONException e) {
            log.error("返回错误!error:{}", e);
            return false;
        }
        return false;
    }

    public static LastTimeVO formatTime(long time) {
        long day = time / 60 / 60 / 24 / 1000;
        time = time - day * 60 * 60 * 24 * 1000;
        long hour = time / 60 / 60 / 1000;
        time = time - hour * 60 * 60 * 1000;
        long min = time / 60 / 1000;
        time = time - min * 60 * 1000;
        long seconds = time / 1000;
        time = time - seconds * 1000;
        long milliseconds = time;
        LastTimeVO lastTimeVO = new LastTimeVO();
        lastTimeVO.setDay((int) day);
        lastTimeVO.setHour((int) hour);
        lastTimeVO.setMinutes((int) min);
        lastTimeVO.setSeconds((int) seconds);
        lastTimeVO.setMilliseconds((int) milliseconds);
        return lastTimeVO;
    }

    public static boolean isConnect(String host, int port) {
        Socket socket = new Socket();
        try {
            socket.connect(new InetSocketAddress(host, port));
        } catch (IOException e) {
            return false;
        } finally {
            try {
                socket.close();
            } catch (IOException e) {
                log.error("关闭失败！error:{}", e);
                e.printStackTrace();
            }
        }
        return true;
    }

    /**
     * @param ip
     * @param sysUser
     * @return
     * @Author beixing
     * @Description 登录文件客户端
     * @Date 2021/8/17
     */
    public boolean loginFileClient(String ip, SysUser sysUser) {
        FortUserFile fortUserFile = fortUserFileDao.queryByUser(sysUser.getUserName());
        if (fortUserFile == null) {
            log.warn("未配置该用户的映射目录！请联系管理员!用户:{}", sysUser.getUserName());
            return false;
        }
        LoginVO loginVO = new LoginVO();
        BeanUtils.copyProperties(fortUserFile, loginVO);
        loginVO.setUser(fortUserFile.getUserName());
        String nfsUrl = getNfsUrl(fortUserFile.getUrl(), fortUserFile.getIp());
        loginVO.setUrl(nfsUrl);
        String url = String.format(FILE_URL_LOGIN, ip);
        try {
            log.info("请求url:{}，请求参数：{}", url, loginVO);
            String result = HttpClientUtil.post(url, loginVO.toString());
            ApiResult apiResult = JSON.parseObject(result, ApiResult.class);
            if (apiResult.getCode() == 0 || apiResult.getCode() == 5000) {
                log.info("用户:{} 登录文件客户端成功!", sysUser.getUserName());
                return true;
            }
            log.warn("请求接口失败！接口返回参数:{}", apiResult);
            return false;
        } catch (Exception e) {
            log.error("连接失败!请求url:{}，请求参数：{},error", url, loginVO, e);
            return false;
        }
    }

    private String getNfsUrl(String url, String ip) {
        url = url.replace("/", "\\");
        return "\\\\".concat(ip).concat("\\").concat(url);
    }

    public String plusOneHour(String time) {
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        Date parse = new Date();
        try {
            parse = sdf.parse(time);
        } catch (ParseException e) {
            log.error("时间转换失败!");
            e.printStackTrace();
        }
        LocalDateTime localDateTime = parse.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
        LocalDateTime localDateTime1 = localDateTime.plusMinutes(60);
        Date newDate = Date.from(localDateTime1.atZone(ZoneId.systemDefault()).toInstant());
        return sdf.format(newDate);
    }

}
