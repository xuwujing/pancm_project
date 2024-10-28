package com.zans.mms.service.impl;

import com.zans.mms.dao.SerialNumMapper;
import com.zans.mms.service.ISerialNumService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service("serialNumService")
public class SerialNumServiceImpl implements ISerialNumService {
    //工单固定前缀
    public static final String TICKET_NUM_PRE = "GD";
    //舆情固定前缀
    public static final String PO_MANAGER_NUM_PRE = "YQ";
    //日期前缀
    public static final DateTimeFormatter FMT_MMS_NUM = DateTimeFormatter.ofPattern("yyyyMMdd");

    //前缀加上当前日期方法
    public static final String splicingDate(String prefix) {
       return prefix+ LocalDateTime.now().format(FMT_MMS_NUM);
    }

    /**
     * 填充当前id 如果不足六位前面填充0，如果足六位 取后六位
     * @param currentId 当前id
     * @return
     */
    @Override
    public  String fillCurrentId(String currentId){
        //根据current的长度判断 如果大于6 直接取后六位 小于6做填充
        if(currentId.length()>6){
            return currentId.substring(currentId.length()-6);
        }else {
            return  String.format("%06d",Integer.valueOf(currentId));
        }
    }


    @Resource
    SerialNumMapper serialNumMapper;

    @Override
    public String generateTicketSerialNum() {
        //根据当前表自增数字判断
        return splicingDate(TICKET_NUM_PRE)+ fillCurrentId(serialNumMapper.getTicketIncrement());
    }

    @Override
    public String generatePoManagerSerialNum() {
        return splicingDate(PO_MANAGER_NUM_PRE)+ fillCurrentId(serialNumMapper.getPoManagerIncrement());
    }
}
