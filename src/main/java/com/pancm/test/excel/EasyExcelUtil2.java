package com.pancm.test.excel;

import cn.hutool.core.io.FileUtil;
import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.EasyExcelFactory;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.alibaba.excel.write.style.column.LongestMatchColumnWidthStyleStrategy;
import com.github.pagehelper.StringUtil;
import com.pancm.test.pojoTest.TenementPlaceCompanyCodeVO;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


@Slf4j
public class EasyExcelUtil2 {

    private static final String NOTFUND_EXCELFILE_ERRORMESSAGE = "请选择excel文件";
    private static final String EXCEL_READER_LOGINFO = "Excel文件解析：文件名 = ";
    private static final String EXCEL_EXT_NAME = ".xlsx";
    private static final String EXCEL_TYPE_ERROR_MESSAGE = "文件格式错误";
    private static final String EXCEL_INCORRECT_DATA = "导入失败, 请检查导入数据的准确性";

    private EasyExcelUtil2(){

    }

    /**
     * 根据自定义对象读取本地excel并指定读取起始行
     *
     * @param <T>       实体类泛型
     * @param localPath 本地文件路径
     * @param rowNum    从第几行开始读
     * @param clazz     实体类
     * @return 导入的数据集合
     */
    public static <T> List<T> readExcelWithRowNum(String localPath, Integer sheetNum, Integer rowNum, Class<T> clazz) throws Exception {
        if (StringUtil.isEmpty(localPath)) {
            throw new Exception(NOTFUND_EXCELFILE_ERRORMESSAGE);
        }
        File file = new File(localPath);
        log.info(EXCEL_READER_LOGINFO + localPath);
        if ((!localPath.toLowerCase().endsWith(".xls") && !localPath.toLowerCase().endsWith(EXCEL_EXT_NAME))) {
            throw new Exception(EXCEL_TYPE_ERROR_MESSAGE);
        }
        try (InputStream fileStream = FileUtil.getInputStream(file)) {
            GeneralExcelListener<T> excelListener = new GeneralExcelListener<>();
            EasyExcelFactory.read(fileStream, clazz, excelListener)
                    .autoTrim(true)
                    .sheet(sheetNum)
                    .headRowNumber(rowNum)
                    .doRead();
            return excelListener.getDatas();
        } catch (Exception e) {
            log.error(EXCEL_INCORRECT_DATA, e);
            throw new Exception(EXCEL_TYPE_ERROR_MESSAGE);
        }
    }

    /**
     * 导出excel
     * @param fileName 导出的文件名
     * @param head 头部
     * @param response 响应
     * @param request  请求
     * @param lists    导出的数据
     * @param <T>      导出的实体类泛型
     */
    public static <T> void writerExcel(String fileName, List<List<String>> head, HttpServletResponse response, HttpServletRequest request, List<T> lists, Class<T> clazz) {
        String sheetName = fileName;
        try {
            String userAgent = request.getHeader("User-Agent");

            if (userAgent.contains("MSIE") || userAgent.contains("Trident")) {
                // 针对IE或者以IE为内核的浏览器：
                fileName = URLEncoder.encode(fileName, "UTF-8");
            } else {
                // 非IE浏览器的处理:
                fileName = new String(fileName.getBytes(StandardCharsets.UTF_8), StandardCharsets.ISO_8859_1);
            }
            response.setHeader("Content-disposition", String.format("attachment; filename=\"%s\"", fileName + EXCEL_EXT_NAME));
            response.setHeader("Cache-Control", "no-cache");
            response.setHeader("Pragma", "no-cache");
            response.setDateHeader("Expires", -1);
        } catch (UnsupportedEncodingException e1) {
            log.error("导出excel未知编码异常", e1);
        }
        try {
            EasyExcelFactory.write(response.getOutputStream(), clazz)
                    .head(head)
                    .registerWriteHandler(new LongestMatchColumnWidthStyleStrategy())
                    .sheet(sheetName)
                    .doWrite(lists);
        } catch (IOException e) {
            log.error("导出excel文件异常", e);
        }
    }


    /**
     * 导出excel
     * @param fileName 导出的文件名
     * @param response 响应
     * @param request  请求
     * @param lists    导出的数据
     * @param <T>      导出的实体类泛型
     */
    public static <T> void writerExcel(String fileName, HttpServletResponse response, HttpServletRequest request, List<T> lists, Class<T> clazz) {
        String sheetName = fileName;
        try {
            String userAgent = request.getHeader("User-Agent");
            fileName = URLEncoder.encode(fileName, "UTF-8");
//            if (userAgent.contains("MSIE") || userAgent.contains("Trident")) {
//                // 针对IE或者以IE为内核的浏览器：
//                fileName = URLEncoder.encode(fileName, "UTF-8");
//            } else {
//                // 非IE浏览器的处理:
//                fileName = new String(fileName.getBytes(StandardCharsets.UTF_8), StandardCharsets.ISO_8859_1);
//            }
            response.setCharacterEncoding("utf-8");
            response.setHeader("Content-disposition", String.format("attachment; filename=\"%s\"", fileName + EXCEL_EXT_NAME));
            response.setHeader("Cache-Control", "no-cache");
            response.setHeader("Pragma", "no-cache");
            response.setDateHeader("Expires", -1);
        } catch (UnsupportedEncodingException e1) {
            log.error("导出excel未知编码异常", e1);
        }
        try {
            EasyExcelFactory.write(response.getOutputStream(), clazz)
                    .registerWriteHandler(new LongestMatchColumnWidthStyleStrategy())
                    .sheet(sheetName)
                    .doWrite(lists);
        } catch (IOException e) {
            log.error("导出excel文件异常", e);
        }
    }


    /**
     * 根据自定义对象读取excel
     * @param <T>   实体类泛型
     * @param excel 文件流
     * @param clazz 实体类
     * @return 导入的数据集合
     */
    public static <T> ArrayList<T> readerExcel(MultipartFile excel, Class<T> clazz) throws Exception {
        if (excel.isEmpty()) {
            throw new Exception("请选择excel文件");
        }
        String fileName = excel.getOriginalFilename();
        log.info("Excel文件解析：文件名 = " + fileName);
        if (fileName == null || (!fileName.toLowerCase().endsWith(".xls") && !fileName.toLowerCase().endsWith(".xlsx"))) {
            throw new Exception("文件格式错误");
        }
        try(InputStream fileStream = new BufferedInputStream(excel.getInputStream())) {
            GeneralExcelListener<T> excelListener = new GeneralExcelListener<>();
            EasyExcel.read(fileStream, clazz, excelListener)
                    .autoTrim(true)
                    .sheet()
                    .doRead();
            return excelListener.getDatas();
        } catch (Exception e) {
            log.error("导入失败, 请检查导入数据的准确性", e);
            throw new Exception("导入失败, 请检查导入数据的准确性");
        }
    }

    /**
     * 获取MultipartFile文件
     *
     * @param picPath
     * @return
     */
    private static MultipartFile getMulFileByPath(String picPath) {
        FileItem fileItem = createFileItem(picPath);
        MultipartFile mfile = new CommonsMultipartFile(fileItem);
        return mfile;
    }

    private static FileItem createFileItem(String filePath) {
        FileItemFactory factory = new DiskFileItemFactory(16, null);
        String textFieldName = "textField";
        int num = filePath.lastIndexOf(".");
        String extFile = filePath.substring(num);
        FileItem item = factory.createItem(textFieldName, "text/plain", true,
                "MyFileName" + extFile);
        File newfile = new File(filePath);
        int bytesRead = 0;
        byte[] buffer = new byte[8192];
        try {
            FileInputStream fis = new FileInputStream(newfile);
            OutputStream os = item.getOutputStream();
            while ((bytesRead = fis.read(buffer, 0, 8192))
                    != -1) {
                os.write(buffer, 0, bytesRead);
            }
            os.close();
            fis.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return item;
    }

    public static void main(String[] args) throws Exception {
        List<TenementPlaceCompanyCodeVO> list =  EasyExcelUtil2.readerExcel(getMulFileByPath("test2.xlsx"), TenementPlaceCompanyCodeVO.class);
        System.out.println(list);
    }


}



@Data
class GeneralExcelListener<T> extends AnalysisEventListener<T> {

    private ArrayList<T> datas = new ArrayList<>();

    private Map<Integer, String> headMap;

    /**
     * 读取表头信息
     *
     * @param headMap 表头信息
     * @param context
     */
    @Override
    public void invokeHeadMap(Map<Integer, String> headMap, AnalysisContext context) {
        this.headMap = headMap;
    }

    /**
     * 一行一行去读取excle内容
     *
     * @param data 当前行数据
     */
    @Override
    public void invoke(T data, AnalysisContext context) {
        datas.add(data);
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext context) {
        //do something
    }

}