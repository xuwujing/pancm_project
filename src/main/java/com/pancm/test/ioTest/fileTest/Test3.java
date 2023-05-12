package com.pancm.test.ioTest.fileTest;

import java.io.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;


/**
 * @author pancm
 * @Title: pancm_project
 * @Description:
 * @Version:1.0.0
 * @Since:jdk1.8
 * @date 2023/4/26
 */
public class Test3 {

    public static void main(String[] args) {
        List<String> fileUrls = new ArrayList<>();
        fileUrls.add("https://example.com/file1.txt");
        fileUrls.add("https://example.com/file2.txt");
        fileUrls.add("https://example.com/file3.txt");

        String zipFileName = "files.zip";

        try {
            File zipFile = new File(zipFileName);
            FileOutputStream fos = new FileOutputStream(zipFile);
            ZipOutputStream zos = new ZipOutputStream(fos);

            for (String fileUrl : fileUrls) {
                // 获取文件名
                String fileName = fileUrl.substring(fileUrl.lastIndexOf("/") + 1);
                // 下载文件
                URL url = new URL(fileUrl);
                InputStream is = url.openStream();
                OutputStream os = new FileOutputStream(fileName);
                byte[] buffer = new byte[4096];
                int length;
                while ((length = is.read(buffer)) > 0) {
                    os.write(buffer, 0, length);
                }
                is.close();
                os.close();
                // 添加文件到zip压缩包
                ZipEntry ze = new ZipEntry(fileName);
                zos.putNextEntry(ze);
                FileInputStream in = new FileInputStream(fileName);
                byte[] b = new byte[1024];
                int count;
                while ((count = in.read(b)) > 0) {
                    zos.write(b, 0, count);
                }
                in.close();
                zos.closeEntry();
                // 删除下载的文件
                File file = new File(fileName);
                file.delete();
            }

            zos.close();
            fos.close();

            System.out.println("文件下载并压缩成功！");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
