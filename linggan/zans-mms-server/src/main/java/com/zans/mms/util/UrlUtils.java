package com.zans.mms.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

/**
 * @author beixing
 * @Title: img_down_tool
 * @Description:
 * @Version:1.0.0
 * @Since:jdk1.8
 * @date 2021/4/23
 */
public class UrlUtils {

    private static final Logger logger = LoggerFactory.getLogger(UrlUtils.class);


    public static byte[] readImg(String urlOrPath, String fileName) throws Exception {

        byte[] imgBytes = new byte[1024 * 1024];

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        BufferedInputStream reader = null;
        InputStream in = null;
        URLConnection conn = null;
        File temFile = null;

        try {
            if (!urlOrPath.startsWith("http:")) {
                File imgFile = new File(urlOrPath);
                if (!imgFile.isFile() || !imgFile.exists() || !imgFile.canRead()) {
                    logger.info("图片不存在或不可读");
                    throw new IOException("图片不存在或不可读");
//                    return new byte[0];
                }
                in = new FileInputStream(imgFile);
            } else {
                URL imgUrl = new URL(urlOrPath);
                conn = imgUrl.openConnection();
                conn.setConnectTimeout(5 * 1000);
                temFile = new File(fileName);
                FileOutputStream tem = new FileOutputStream(temFile);
                BufferedImage image = ImageIO.read(conn.getInputStream());
                ImageIO.write(image, "jpg", tem);
                in = new FileInputStream(temFile);
            }
            reader = new BufferedInputStream(in);
            byte[] buffer = new byte[1024];
            while (reader.read(buffer) != -1) {
                out.write(buffer);
            }
            imgBytes = out.toByteArray();

        } catch (Exception e) {
            logger.error("读取图片发生异常", e);
            throw new Exception("读取图片发生异常");
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    logger.error("读取图片发生异常", e);
                }
            }
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    logger.error("读取图片发生异常", e);
                }
            }
            try {
                out.close();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                logger.error("读取图片发生异常", e);
            }
            if (temFile != null) {
                temFile.delete();
            }
        }
        return imgBytes;
    }

    public static void download(String urlString, String filename, String savePath) throws Exception {
        // 构造URL
        URL url = new URL(urlString);
        // 打开连接
        URLConnection con = url.openConnection();
        //设置请求超时为5s
        con.setConnectTimeout(5 * 1000);
        // 输入流
        InputStream is = con.getInputStream();

        // 1K的数据缓冲
        byte[] bs = new byte[1024];
        // 读取到的数据长度
        int len;
        // 输出的文件流
        File sf = new File(savePath);
        if (!sf.exists()) {
            sf.mkdirs();
        }
        String extensionName = filename.substring(filename.lastIndexOf(".") + 1);
        // 新的图片文件名 = 编号 +"."图片扩展名
        String newFileName = "123" + "." + extensionName;
        OutputStream os = new FileOutputStream(sf.getPath() + "\\" + newFileName);
        // 开始读取
        while ((len = is.read(bs)) != -1) {
            os.write(bs, 0, len);
        }
        // 完毕，关闭所有链接
        os.close();
        is.close();
    }

    public static void downloadPicture(String urlList, String path) {
        URL url = null;
        try {
            url = new URL(urlList);
            DataInputStream dataInputStream = new DataInputStream(url.openStream());

            FileOutputStream fileOutputStream = new FileOutputStream(new File(path));
            ByteArrayOutputStream output = new ByteArrayOutputStream();

            byte[] buffer = new byte[1024];
            int length;

            while ((length = dataInputStream.read(buffer)) > 0) {
                output.write(buffer, 0, length);
            }
            fileOutputStream.write(output.toByteArray());
            dataInputStream.close();
            fileOutputStream.close();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static void main(String[] args) throws Exception {
//        String url = "http://192.168.9.51/img/raw/2021/4/22/GD/123456_06f59b749a9548cb8562964a46396035.png";
//        String url = "http://192.168.9.51/img/raw/2021/4/21/GD/1_9c6cefdbf5ae49a580976a58fb541714.jpg";
        String url = "http://192.168.2.5:8888/diagnosis/2021/5/31/18/34020000001320000004/34020000001320000004_20210531181046_00013.jpg";
        String desUrl = "D:\\home\\release\\diagnosis\\2021\\6\\1\\9\\34020000001320000002\\222" + ".jpg";
        String desUrl2 = "D:\\home\\release\\diagnosis\\2021\\6\\1\\9\\34020000001320000002\\2223" + ".jpg";
        readImg(url, desUrl);
        downloadPicture(url, desUrl2);

//        String url2 = "/home/\\2021\\5\\24\\17\\34020000001320000001\\34020000001320000001_20210524175006_00002.jpg";
//        System.out.println(url2.replace("\\","/"));
    }
}
