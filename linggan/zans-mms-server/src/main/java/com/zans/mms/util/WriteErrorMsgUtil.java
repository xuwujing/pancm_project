package com.zans.mms.util;


import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.*;
import org.springframework.stereotype.Component;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * @author QiTian
 */
@Component("writeErrorMsgUtil")
@Slf4j
public class WriteErrorMsgUtil {


	/**
	 * 写错误信息
	 * @param rowIndex excel行索引
	 * @param msg 错误消息
	 */
	public void writeErrorMsg(Integer rowIndex, String msg, String absoluteNewFilePath) {
		writeErrorMsg(rowIndex,0,msg,absoluteNewFilePath);
	}



	/**
	 * 写错误信息
	 * @param rowIndex excel行索引
	 * @param msg 错误消息
	 * @param sheetNo sheet页索引
	 */
	public void writeErrorMsg(Integer rowIndex,Integer sheetNo, String msg, String absoluteNewFilePath) {
		FileInputStream fs=null;
		FileOutputStream out =null;

		try{
			//获取absoluteNewFilePath
			fs=new FileInputStream(absoluteNewFilePath);
			//做2003和2007兼容
			Workbook wb = WorkbookFactory.create(fs);
			//默认取第一个sheet页
			Sheet sheetAt = wb.getSheetAt(sheetNo);
			Row row = sheetAt.getRow(rowIndex);
			Cell cell = row.createCell(row.getLastCellNum());
			cell.setCellValue(msg);
			out = new FileOutputStream(absoluteNewFilePath);
			wb.write(out);
			out.flush();
		}catch (Exception e){
			e.printStackTrace();
			log.error("模板错误信息添加出现错误！");
		}finally {
			if(out!=null){
				try {
					out.close();
				} catch (IOException e) {
					log.error("模板错误信息写入出现异常{}",e);
				}
			}
			if(fs!=null){
				try {
					fs.close();
				} catch (IOException e) {
					log.error("模板错误信息写入出现异常{}",e);
				}
			}
		}
	}
}
