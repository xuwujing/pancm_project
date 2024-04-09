package com.pancm.test.oss;


import com.qcloud.cos.COSClient;
import com.qcloud.cos.ClientConfig;
import com.qcloud.cos.auth.BasicCOSCredentials;
import com.qcloud.cos.auth.COSCredentials;
import com.qcloud.cos.model.CannedAccessControlList;
import com.qcloud.cos.model.PutObjectRequest;
import com.qcloud.cos.model.PutObjectResult;
import com.qcloud.cos.region.Region;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.springframework.http.MediaType;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import java.io.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

/**
 * @Description: 腾讯云 css 上传工具类(高依赖版)
 * @Date: 2024/3/11
 * @author: pcm
 */
@Slf4j
public class CosBootUtil {

    private static String region;
    private static String accessKeyId;
    private static String accessKeySecret;
    private static String bucketName;
    private static String url;

    public static void setRegion(String region) {
        CosBootUtil.region = region;
    }

    public static void setAccessKeyId(String accessKeyId) {
        CosBootUtil.accessKeyId = accessKeyId;
    }

    public static void setAccessKeySecret(String accessKeySecret) {
        CosBootUtil.accessKeySecret = accessKeySecret;
    }

    public static void setBucketName(String bucketName) {
        CosBootUtil.bucketName = bucketName;
    }

    public static void setUrl(String url) {
        CosBootUtil.url = url;
    }

    public static String getUrl() {
        return url;
    }

    public static String getRegion() {
        return region;
    }

    public static String getAccessKeyId() {
        return accessKeyId;
    }

    public static String getAccessKeySecret() {
        return accessKeySecret;
    }

    public static String getBucketName() {
        return bucketName;
    }

    public static COSClient getCosClient() {
        return cosClient;
    }

    /**
     * oss 工具客户端
     */
    private static COSClient cosClient = null;


    /**
     * 初始化 cos 客户端
     *
     * @return
     */
    private static COSClient initCos(String region, String secretId, String secretKey) {
        if (cosClient == null) {
            // 1 初始化用户身份信息（secretId, secretKey）。
            COSCredentials cred = new BasicCOSCredentials(secretId, secretKey);
            // 2 设置 bucket 的区域, COS 地域的简称请参照
            Region region1 = new Region(region);
            ClientConfig clientConfig = new ClientConfig(region1);
            // 3 生成 cos 客户端。
            cosClient = new COSClient(cred, clientConfig);
        }
        return cosClient;
    }

    /**
     * 上传文件至腾讯云  COS
     * 文件上传成功,返回文件完整访问路径
     * 文件上传失败,返回 null
     *
     * @param file    待上传文件
     * @param fileDir 文件保存目录
     * @return oss 中的相对文件路径
     */
    public static String upload(MultipartFile file, String fileDir) throws Exception {

        initCos(region, accessKeyId, accessKeySecret);
        StringBuilder fileUrl = new StringBuilder();
        try {
            if (StringUtils.isEmpty(fileDir)) {
                fileDir = "2024/";
            }
            // 获取文件名
            String fileName = file.getOriginalFilename();
            if ("".equals(fileName)) {
                fileName = file.getName();
            }

            fileName = !fileName.contains(".")
                    ? fileName + "_" + System.currentTimeMillis()
                    : fileName.substring(0, fileName.lastIndexOf(".")) + "_" + System.currentTimeMillis() + fileName.substring(fileName.lastIndexOf("."));
            //过滤上传文件夹名特殊字符，防止攻击
            fileDir = filter(fileDir);
            fileUrl = fileUrl.append(fileDir + fileName);
            PutObjectResult result = cosClient.putObject(new PutObjectRequest(bucketName, fileUrl.toString(), file.getInputStream(), null));
            // 设置权限(公开读)
            cosClient.setBucketAcl(bucketName, CannedAccessControlList.PublicRead);
            if (result != null) {
                log.info("------文件上传成功------" + fileUrl);
            }
        } catch (IOException e) {
            log.error(e.getMessage(), e);
            return null;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return null;
        }
        return url.concat(fileUrl.toString());
    }

    private static String filter(String str) throws PatternSyntaxException {
        // 清除掉所有特殊字符
        String regEx = "[`_《》~!@#$%^&*()+=|{}':;',\\[\\].<>?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]";
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(str);
        return m.replaceAll("").trim();
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

     //获取MultipartFile类型的文件
    public MultipartFile getMultipartFile(InputStream inputStream, String fileName) {
        //创建一个FileItem对象
        FileItem fileItem = createFileItem(inputStream, fileName);
        //使用CommonsMultipartFile对FileItem对象进行封装
        return new CommonsMultipartFile(fileItem);

    }


    /**
     * FileItem类对象创建
     * @param inputStream inputStream
     * @param fileName    fileName
     * @return FileItem
     */
    public FileItem createFileItem(InputStream inputStream, String fileName) {
        FileItemFactory factory = new DiskFileItemFactory(16, null);
        String textFieldName = "file";
        FileItem item = factory.createItem(textFieldName, MediaType.MULTIPART_FORM_DATA_VALUE, true, fileName);
        int bytesRead = 0;
        byte[] buffer = new byte[10 * 1024 * 1024];
        OutputStream os = null;
        //使用输出流输出输入流的字节
        try {
            os = item.getOutputStream();
            while ((bytesRead = inputStream.read(buffer, 0, 8192)) != -1) {
                os.write(buffer, 0, bytesRead);
            }
            inputStream.close();
        } catch (IOException e) {
            log.error("Stream copy exception", e);
            throw new IllegalArgumentException("文件上传失败");
        } finally {
            if (os != null) {
                try {
                    os.close();
                } catch (IOException e) {
                    log.error("Stream close exception", e);
                }
            }
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    log.error("Stream close exception", e);
                }
            }
        }
        return item;

    }


    /**
     * 创建一个FileItem对象
     * @param filePath 文件路径
     * @return FileItem对象
     */
    private static FileItem createFileItem(String filePath) {
        // 创建一个文件项工厂
        FileItemFactory factory = new DiskFileItemFactory(16, null);
        // 设置文件项的名称
        String textFieldName = "textField";
        // 获取文件扩展名
        int num = filePath.lastIndexOf(".");
        String extFile = filePath.substring(num);
        // 使用文件项工厂创建一个文件项
        FileItem item = factory.createItem(textFieldName, "text/plain", true,
                "MyFileName" + extFile);
        // 创建一个文件对象
        File newfile = new File(filePath);
        // 读取文件的次数
        int bytesRead = 0;
        // 字节数组
        byte[] buffer = new byte[8192];
        try {
            // 创建一个文件输入流
            FileInputStream fis = new FileInputStream(newfile);
            // 创建一个输出流
            OutputStream os = item.getOutputStream();
            // 循环读取文件
            while ((bytesRead = fis.read(buffer, 0, 8192))
                    != -1) {
                // 将字节写入输出流
                os.write(buffer, 0, bytesRead);
            }
            // 关闭输出流
            os.close();
            // 关闭文件输入流
            fis.close();
        } catch (IOException e) {
            // 打印异常
            e.printStackTrace();
        }
        // 返回文件项
        return item;
    }

    public static void main(String[] args) throws Exception {
        // 设置accessKeyId
        CosBootUtil.setAccessKeyId("xxx");
        // 设置accessKeySecret
        CosBootUtil.setAccessKeySecret("xxx");
        // 设置bucketName
        CosBootUtil.setBucketName("test-1307462009");
        // 设置region
        CosBootUtil.setRegion("ap-guangzhou");
        // 设置url
        CosBootUtil.setUrl("202403/");
        // 设置文件路径
        String filePath = "D:\\image\\1.png";
        // 通过路径获取文件
        MultipartFile file = getMulFileByPath(filePath);
        // 上传文件
        String url = upload(file, null);
        // 打印上传后的url
        System.out.println(url);
    }
}