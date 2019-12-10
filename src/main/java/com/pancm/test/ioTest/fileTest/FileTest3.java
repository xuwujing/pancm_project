package com.pancm.test.ioTest.fileTest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * @author pancm
 * @Title: pancm_project
 * @Description:
 * @Version:1.0.0
 * @Since:jdk1.8
 * @date 2019/12/10
 */
public class FileTest3 {
    private static final Logger LOG = LoggerFactory.getLogger(FileTest3.class);


    private static long count=0;
    private static long count2=0;
    private static long costTime=0;

    private static int l = 0;
    private static int l2 = 0;
    private static int intervalTime = 60;
    private static long fileCount;
    private static String fileName;
    private static String fileSuffix="txt";
    private static  String path="txtFile";

    /** 配置文件名称 */
    protected static final String fileName2 = "application.properties";
    protected static final String PHONE = "phone";
    protected static final String TIME = "endTime";


    public static boolean writeToFile(List<Map<String, Object>> lists) {
        StringBuffer sb = new StringBuffer();
        // String tableName = MyTools.isEmpty(conf.get("tableName")) ?
        // Constant.PA_TABLE_NAME : conf.get("tableName");
        // 拼接sql
        lists.forEach(m -> {
            // 如果达到了一亿条就换一个文件
            if (count % fileCount == 0) {
                l++;
                LOG.info("开始第{}个文件写入！",l);
            }
            count++;

            String sql = packageTxt(m);
            // 换行
            sb.append(sql).append(System.getProperty("line.separator"));
        });
        // 写入文件中
        try {
            writeFile(sb.toString());
            // logger.info("写入成功!");
            return true;
        } catch (IOException e) {
            LOG.error("写入失败!原因是:", e);
        }
        return false;
    }

    /**
     * 写文件
     * @param data
     * @throws IOException
     */
    private static void writeFile(String data) throws IOException {
        FileWriter fw = null;
        BufferedWriter bw = null;
        // 创建要操作的文件路径和名称
        try {
            String fileName2 = path + File.separator + fileName + "_" + l + "." + fileSuffix;
            fw = new FileWriter(fileName2, true);
            bw = new BufferedWriter(fw);
            bw.write(data);
        } finally {
            if (bw != null) {
                bw.close();
            }
            if (fw != null) {
                fw.close();
            }
        }
    }
    /**
     * @param valueMap
     * @return
     */
    public static String packageTxt(Map<String, Object> valueMap) {
        /** 获取数据库插入的Map的键值对的值 **/
        /** 要插入的字段值 **/
        StringBuilder unknownMarkSql = new StringBuilder();
        unknownMarkSql.append(valueMap.get("phone"));
        return unknownMarkSql.toString();
    }

}
