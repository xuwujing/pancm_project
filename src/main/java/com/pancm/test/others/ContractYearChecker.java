package com.pancm.test.others;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class ContractYearChecker {
    public static void main(String[] args) {
        // 合同周期开始日期和结束日期
        Date contractStartDate = createDate("2012-05-01");
        Date contractEndDate = createDate("2013-04-30");

        // 账单周期开始日期和结束日期
        Date billStartDate = createDate("2013-04-30");
        Date billEndDate = createDate("2013-07-30");
        // 判断账单周期是否跨年
        boolean isYearCrossing = isBillYearCrossing(contractStartDate, contractEndDate, billStartDate, billEndDate);
        Date billStartDate2 = createDate("2013-05-01");
        Date billEndDate2 = createDate("2013-07-30");
        boolean isYearCrossing2 = isBillYearCrossing(contractStartDate, contractEndDate, billStartDate2, billEndDate2);
        Date billStartDate3 = createDate("2012-05-01");
        Date billEndDate3 = createDate("2012-07-30");
        boolean isYearCrossing3 = isBillYearCrossing(contractStartDate, contractEndDate, billStartDate3, billEndDate3);
        System.out.println("===============");
        // 输出结果
        System.out.println(isYearCrossing);
        System.out.println(isYearCrossing2);
        System.out.println(isYearCrossing3);
        List<Integer> billDateArray = getBillDateArray(billStartDate, billEndDate);
        // 输出结果
        for (int date : billDateArray) {
            System.out.println(date);
        }
        if(billDateArray.size()>3){
            billDateArray = billDateArray.subList(0,3);
        }
        System.out.println(billDateArray);

        // 获取账单周期的月份列表
        List<Date> billDateList = getBillDateList(billStartDate, billEndDate);
        System.out.println(billDateList);

        // 需要检查的日期
        Date dateToCheck = createDate("2013-04-30");
        Date dateToCheck2 = createDate("2013-05-01");

        // 某一段日期的开始日期和结束日期
        Date rangeStartDate = createDate("2012-05-01");
        Date rangeEndDate = createDate("2013-04-30");
        System.out.println("-----");
        billDateList = billDateList.subList(0,3);
        for (int i = 0; i <4 ; i++) {
            Date rangeStartDate1 = addYears(createDate("2012-05-01"),i);
            Date rangeEndDate1 =  addYears(createDate("2013-04-30"),i);
            for (Date date : billDateList) {
                System.out.println(date);
                System.out.println(isDateInRange(date, rangeStartDate1, rangeEndDate1));
            }
            System.out.println("-");
        }
        System.out.println("-----");
        // 判断日期是否在某一段区间内
        boolean isInRange = isDateInRange(dateToCheck, rangeStartDate, rangeEndDate);
        boolean isInRange2 = isDateInRange(dateToCheck2, rangeStartDate, rangeEndDate);
        System.out.println(isInRange);
        System.out.println(isInRange2);
    }

    public static Date addYears(Date date, int years) {
        // 将Date对象转换为LocalDate对象
        LocalDate localDate = date.toInstant().atZone(java.time.ZoneId.systemDefault()).toLocalDate();
        // 增加年份
        LocalDate newLocalDate = localDate.plusYears(years);
        // 将LocalDate对象转换为Date对象
        Date newDate = java.sql.Date.valueOf(newLocalDate);
        return newDate;
    }

    public static boolean isDateInRange(Date dateToCheck, Date rangeStartDate, Date rangeEndDate) {
        return dateToCheck.compareTo(rangeStartDate) >= 0 && dateToCheck.compareTo(rangeEndDate) <= 0;
    }

    public static List<Date> getBillDateList(Date billStartDate, Date billEndDate) {
        List<Date> billDateList = new ArrayList<>();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(billStartDate);
        while (calendar.getTime().compareTo(billEndDate) <= 0) {
            billDateList.add(calendar.getTime());
            calendar.add(Calendar.MONTH, 1);
        }
        return billDateList;
    }

    public static List<Integer> getBillDateArray(Date billStartDate, Date billEndDate) {
        // 创建账单周期的整型数组
        List<Integer> monthList = new ArrayList<>();

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(billStartDate);

        // 获取账单周期的每个月份
        while (calendar.getTime().compareTo(billEndDate) <= 0) {
            monthList.add(calendar.get(Calendar.MONTH) + 1);
            calendar.add(Calendar.MONTH, 1);
        }
        // 返回账单周期的整型数组
        return monthList;
    }


    public static boolean isBillYearCrossing(Date contractStartDate, Date contractEndDate, Date billStartDate, Date billEndDate) {
        boolean f = billStartDate.compareTo(contractEndDate) <= 0 && billStartDate.compareTo(contractStartDate) >= 0;
        boolean d = billEndDate.compareTo(contractEndDate) <= 0 && billEndDate.compareTo(contractStartDate) >= 0;
        // 判断账单周期是否跨年
        return f && !d;
    }

    public static Date createDate(String dateString) {
        try {
            return new SimpleDateFormat("yyyy-MM-dd").parse(dateString);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }
}
