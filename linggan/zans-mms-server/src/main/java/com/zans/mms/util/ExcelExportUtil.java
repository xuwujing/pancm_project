package com.zans.mms.util;

import com.zans.base.exception.BusinessException;
import com.zans.base.util.StringHelper;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.*;
import java.util.List;

/**
 * @author qitain
 */
@Component("exportExcelUtil")
@Slf4j
public class ExcelExportUtil {
	@Value("${api.export.folder}")
	String exportFolder;
	/**
	 * 写文件头
	 * @param sheetName sheet名称
	 * @param rowNameList 表头集合
	 * @return
	 */
	public String writeHeader(String sheetName,List<String> rowNameList,String fileName) throws Exception {
		String absoluteNewFilePath=this.exportFolder+fileName;
		File file = new File(absoluteNewFilePath);
		if(!file.exists()){
			file.createNewFile();
		}
		//创建HSSFWorkbook对象
		HSSFWorkbook wb = new HSSFWorkbook();
		//建立sheet对象
		HSSFSheet sheet=wb.createSheet(sheetName);
		//创建第一行
		HSSFRow firstRow=sheet.createRow(0);
		for(int i=0;i<rowNameList.size();i++){
			sheet.setColumnWidth(i,rowNameList.get(i).getBytes().length*2*256);
			firstRow.createCell(i).setCellValue(rowNameList.get(i));
		}
		try(OutputStream out=new FileOutputStream(file)){
			wb.write(out);
		}
		return absoluteNewFilePath;
	}


	public String writeMultipleHeader(List<String> sheetNameList,List<List<String>> rowNameList,String fileName) throws Exception {
		if(StringHelper.isEmpty(sheetNameList.size())||StringHelper.isEmpty(rowNameList)||sheetNameList.size()!=rowNameList.size()){
			throw new BusinessException("后台逻辑异常！");
		}
		String absoluteNewFilePath=this.exportFolder+fileName;
		File file = new File(absoluteNewFilePath);
		if(!file.exists()){
			file.createNewFile();
		}
		//创建HSSFWorkbook对象
		HSSFWorkbook wb = new HSSFWorkbook();
		for(int i =0;i<sheetNameList.size();i++){
			//建立sheet对象
			HSSFSheet sheet=wb.createSheet(sheetNameList.get(i));
			//创建第一行
			HSSFRow firstRow=sheet.createRow(0);
			for(int j=0;j<rowNameList.get(i).size();j++){
				sheet.setColumnWidth(j,rowNameList.get(i).get(j).getBytes().length*2*256);
				firstRow.createCell(j).setCellValue(rowNameList.get(i).get(j));
			}
		}

		try(OutputStream out=new FileOutputStream(file)){
			wb.write(out);
		}
		return absoluteNewFilePath;
	}

	public String writeMultipleHeaderWithWidth(List<String> sheetNameList,List<List<String>> rowNameList,String fileName,Integer width) throws Exception {
		if(StringHelper.isEmpty(sheetNameList.size())||StringHelper.isEmpty(rowNameList)||sheetNameList.size()!=rowNameList.size()){
			throw new BusinessException("后台逻辑异常！");
		}
		String absoluteNewFilePath=this.exportFolder+fileName;
		File file = new File(absoluteNewFilePath);
		if(!file.exists()){
			file.createNewFile();
		}
		//创建HSSFWorkbook对象
		HSSFWorkbook wb = new HSSFWorkbook();
		for(int i =0;i<sheetNameList.size();i++){
			//建立sheet对象
			HSSFSheet sheet=wb.createSheet(sheetNameList.get(i));
			//创建第一行
			HSSFRow firstRow=sheet.createRow(0);
			for(int j=0;j<rowNameList.get(i).size();j++){
				sheet.setColumnWidth(j,rowNameList.get(i).get(j).getBytes().length*width);
				firstRow.createCell(j).setCellValue(rowNameList.get(i).get(j));
			}
		}

		try(OutputStream out=new FileOutputStream(file)){
			wb.write(out);
		}
		return absoluteNewFilePath;
	}

}
