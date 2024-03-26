package com.pancm.test.ioTest.fileTest;

import java.io.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;


public class FileZip {
    /**
     * zip文件压缩
     *
     * @param inputFile  待压缩文件夹/文件名
     * @param outputFile 生成的压缩包名字
     */

    public static void ZipCompress(String inputFile, String outputFile) throws Exception {
        //创建zip输出流
        ZipOutputStream out = new ZipOutputStream(new FileOutputStream(outputFile));
        //创建缓冲输出流
        BufferedOutputStream bos = new BufferedOutputStream(out);
        File input = new File(inputFile);
        compress(out, bos, input, null);
        bos.close();
        out.close();
    }

    /**
     * @param name 压缩文件名，可以写为null保持默认
     */
    //递归压缩
    public static void compress(ZipOutputStream out, BufferedOutputStream bos, File input, String name) throws IOException {
        if (name == null) {
            name = input.getName();
        }
        //如果路径为目录（文件夹）
        if (input.isDirectory()) {
            //取出文件夹中的文件（或子文件夹）
            File[] flist = input.listFiles();

            if (flist.length == 0)//如果文件夹为空，则只需在目的地zip文件中写入一个目录进入
            {
                out.putNextEntry(new ZipEntry(name + "/"));
            } else//如果文件夹不为空，则递归调用compress，文件夹中的每一个文件（或文件夹）进行压缩
            {
                for (int i = 0; i < flist.length; i++) {
                    compress(out, bos, flist[i], name + "/" + flist[i].getName());
                }
            }
        } else//如果不是目录（文件夹），即为文件，则先写入目录进入点，之后将文件写入zip文件中
        {
            out.putNextEntry(new ZipEntry(name));
            FileInputStream fos = new FileInputStream(input);
            BufferedInputStream bis = new BufferedInputStream(fos);
            int len;
            //将源文件写入到zip文件中
            byte[] buf = new byte[1024];
            while ((len = bis.read(buf)) != -1) {
                bos.write(buf, 0, len);
            }
            //图片压缩需要，否则出现问题
            bos.flush();
            bis.close();
            fos.close();
        }
    }

    public static void main(String[] args) {
        try {
            String src = "D:\\home\\release";
            String target = "D:\\home\\release.zip";
            ZipCompress(src, target);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}


