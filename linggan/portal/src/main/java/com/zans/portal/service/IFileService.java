package com.zans.portal.service;

import com.zans.base.office.excel.ExcelCell;
import com.zans.base.office.excel.ExcelEntity;
import com.zans.base.office.excel.ExcelSheetReader;
import com.zans.base.office.excel.ExportConfig;
import com.zans.portal.model.IpAlloc;
import com.zans.portal.vo.file.IpAllocFile;
import com.zans.portal.vo.network.ProjectVO;

import java.io.File;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

/**
 * @author xv
 * @since 2020/3/20 14:44
 */
public interface IFileService {

    /**
     * 校验文件格式
     * @param fileName
     * @return
     */
    boolean validateAllocFile(String fileName);

    /**
     * 读取 Excel 文件
     * @param fileName 文件名
     * @param is  输入流
     * @return IP分配对象
     */
    IpAllocFile readIpAllocFile(String fileName, InputStream is) throws Exception;

    /**
     * 读取 Excel 文件
     * @param fileName 文件名
     * @param file 输入文件
     * @param reader excel配置
     * @return excel数据容器
     * @throws Exception
     */
    ExcelEntity readFile(String fileName, File file, ExcelSheetReader reader) throws Exception;

    /**
     * 检测 Excel文件
     * @param fileName 文件名
     * @param is 输入流
     * @param outFilePath 新文件的绝对路径
     * @return IP分配对象
     */
    IpAllocFile checkFile(String fileName, InputStream is, String outFilePath) throws Exception;
 /**
     * 检测 Excel文件的ip是否合法
     * @param fileName 文件名
     * @param is 输入流
     * @param outFilePath 新文件的绝对路径
     * @return IP分配对象
     */
    IpAllocFile checkFileIp(String fileName, InputStream is, String outFilePath) throws Exception;

    /**
     * 检测 Excel文件
     * @param fileName 文件名
     * @param file 输入流
     * @param outFilePath 新文件的绝对路径
     * @param reader excel配置
     * @return IP分配对象
     */
    ExcelEntity checkFile(String fileName, File file, String outFilePath, ExcelSheetReader reader) throws Exception;


    /**
     * 分配 IP
     * @param ipAlloc 有deviceType, area 信息
     * @param fileName  文件名
     * @param is 输入流
     * @param outFilePath  新文件的绝对路径
     * @return IP分配对象
     */
    IpAllocFile assignIp(IpAlloc ipAlloc, String fileName, InputStream is, String outFilePath) throws Exception;

    /**
     * 手工分配 IP 只做存储，不做实际分配
     * @param ipAlloc 有deviceType, area 信息
     * @param fileName  文件名
     * @param is 输入流
     * @param outFilePath  新文件的绝对路径
     * @return IP分配对象
     */
    IpAllocFile handAssignIp(IpAlloc ipAlloc, String fileName, InputStream is, String outFilePath) throws Exception;

    /**
     * 根据模板，生成文件
     * @param fileName  文件名，带区域、设备类型
     * @param is 文件流
     * @param outFilePath 新文件的路径
     * @param valueMap 需要写入的字段
     * @return 新生成的文件路径；有问题返回null
     * @throws Exception
     */
    String generateFileByTemplate(String fileName, InputStream is, String outFilePath, Map<String, Object> valueMap) throws Exception;

    /**
     * 更新指定的cell
     * @param fileName
     * @param file
     * @param outFilePath
     * @param valueMap
     * @return
     * @throws Exception
     */
    String generateFileByTemplate(String fileName, File file, String outFilePath, Map<ExcelCell, Object> valueMap) throws Exception;

    /**
     * 生成复杂的文件
     * @param fileName
     * @param file 导出模板
     * @param outFilePath
     * @param project
     * @param exportConfig
     * @return
     * @throws Exception
     */
    String generateFile(String fileName, File file, String outFilePath, ProjectVO project, ExportConfig exportConfig) throws Exception;
    /**
     * 根据 ExcelProperty 注解，生成excel文件
     * @param data 数据
     * @param sheetName 名称
     * @param filePath 导出文件的物理路径
     * @param exportConfig 导出参数
     * @return
     */
    boolean exportExcelFile(List data, String sheetName, String filePath, ExportConfig exportConfig);

    /**
     * 根据已有文件名，生成<日期>_<UUID>.<后缀>格式的文件名
     * @param fileName 文件名
     * @return 新文件名
     */
    public String getNewFileName(String fileName);
}
