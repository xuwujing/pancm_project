package com.zans.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.zans.dao.DeviceCipherApproveDao;
import com.zans.model.DeviceCipherApprove;
import com.zans.service.IDeviceCipherApproveService;
import com.zans.vo.ApiResult;
import com.zans.vo.DeviceCipherApproveVO;
import com.zans.vo.PageResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


/**
 * (DeviceCipherApprove)表服务实现类
 *
 * @author beixing
 * @since 2021-08-23 16:23:48
 */
@Service("deviceCipherApproveService")
@Slf4j
public class DeviceCipherApproveServiceImpl implements IDeviceCipherApproveService {
    @Resource
    private DeviceCipherApproveDao deviceCipherApproveDao;

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    @Override
    public DeviceCipherApprove queryById(Integer id) {
        return this.deviceCipherApproveDao.queryById(id);
    }

    /**
     * 新增数据
     *
     * @param deviceCipherApprove 实例对象
     * @return 实例对象
     */
    @Override
    public int insert(DeviceCipherApprove deviceCipherApprove) {
        return deviceCipherApproveDao.insert(deviceCipherApprove);
    }

    /**
     * 修改数据
     *
     * @param deviceCipherApprove 实例对象
     * @return 实例对象
     */
    @Override
    public int update(DeviceCipherApprove deviceCipherApprove) {
        return deviceCipherApproveDao.update(deviceCipherApprove);
    }

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 是否成功
     */
    @Override
    public boolean deleteById(Integer id) {
        return this.deviceCipherApproveDao.deleteById(id) > 0;
    }


    @Override
    public ApiResult approvalList(DeviceCipherApproveVO deviceCipherApproveVO) {
        Page<DeviceCipherApproveVO> page = PageHelper.startPage(deviceCipherApproveVO.getPageNum(), deviceCipherApproveVO.getPageSize());
        List<DeviceCipherApproveVO> deviceCipherApproves = new ArrayList<>();
        if (1 == deviceCipherApproveVO.getApproveStatus()){
            deviceCipherApproves = deviceCipherApproveDao.queryAll(deviceCipherApproveVO);
        }else {
            deviceCipherApproves = deviceCipherApproveDao.queryApproveList(deviceCipherApproveVO);
        }
        for (DeviceCipherApproveVO deviceCipherApprove : deviceCipherApproves) {
            deviceCipherApprove.setTimeExplain(timeExplain(deviceCipherApprove.getApproveStatus(), deviceCipherApprove.getApproveStatus() == 1 ? deviceCipherApprove.getCreateTime() : deviceCipherApprove.getUpdateTime()));
        }
        return ApiResult.success(new PageResult<>(page.getTotal(), deviceCipherApproves, deviceCipherApproveVO.getPageSize(), deviceCipherApproveVO.getPageNum()));
    }

    public static String timeExplain(Integer status, String timeFormat) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        long time = 0;
        try {
            time = sdf.parse(timeFormat).getTime();
        } catch (ParseException e) {
            log.error("时间转换异常：{}",e);
        }
        long timeGap = new Date().getTime() - time;
        long timeSecond = timeGap / 1000;
        int mouth = (int) (timeSecond / 60 / 60 / 24 / 30);
        int day = (int) ((timeSecond - mouth * 60 * 60 * 24 * 30) / 60 / 60 / 24);
        int hour = (int) ((timeSecond - mouth * 60 * 60 * 24 * 30 - day * 60 * 60 * 24) / 60 / 60);
        int min = (int) ((timeSecond - mouth * 60 * 60 * 24 * 30 - day * 60 * 60 * 24 - hour * 60 * 60) / 60);
        int second = (int) (timeSecond - mouth * 60 * 60 * 24 * 30 - day * 60 * 60 * 24 - hour * 60 * 60 - min * 60);
        String data = mouth == 0 ? (day == 0 ? (hour == 0 ? (min == 0 ? (second + "秒前") : min + "分钟前") : hour + "小时前") : day + "天前") : mouth + "月前";
        return data + (status == 1 ? "发起申请" : (status == 2 ? ("审批通过") : "审批驳回"));
    }
}
