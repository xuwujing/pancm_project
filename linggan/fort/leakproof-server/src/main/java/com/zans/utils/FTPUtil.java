package com.zans.utils;

import com.zans.vo.NetDiskFile;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.SocketException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * 简单操作FTP工具类 ,此工具类支持中文文件名，不支持中文目录
 * 如果需要支持中文目录，需要 new String(path.getBytes("UTF-8"),"ISO-8859-1") 对目录进行转码
 * @author WZH
 *
 */
@Slf4j
public class FTPUtil {

    /**
     * 获取FTPClient对象
     * @param ftpHost 服务器IP
     * @param ftpPort 服务器端口号
     * @param ftpUserName 用户名
     * @param ftpPassword 密码
     * @return FTPClient
     */
    public FTPClient getFTPClient(String ftpHost, int ftpPort,
            String ftpUserName, String ftpPassword) {

        FTPClient ftp = null;
        try {
            ftp = new FTPClient();
            // 连接FPT服务器,设置IP及端口
            ftp.connect(ftpHost, ftpPort);
            // 设置用户名和密码
            ftp.login(ftpUserName, ftpPassword);
            // 设置连接超时时间,5000毫秒
            ftp.setConnectTimeout(50000);
            // 设置中文编码集，防止中文乱码
            ftp.setControlEncoding("GB18030");
            if (!FTPReply.isPositiveCompletion(ftp.getReplyCode())) {
                log.info("未连接到FTP，用户名或密码错误");
                ftp.disconnect();
            } else {
                log.info("FTP连接成功");
            }

        } catch (SocketException e) {
            e.printStackTrace();
            log.info("FTP的IP地址可能错误，请正确配置");
        } catch (IOException e) {
            e.printStackTrace();
            log.info("FTP的端口错误,请正确配置");
        }
        return ftp;
    }

    public static boolean testFtp(String ip, int port, String user, String pwd) throws IOException {
        FTPClient ftpClient = new FTPClient();
        ftpClient.connect(ip, port);//连接ftp
        ftpClient.login(user, pwd);//登陆ftp
        return FTPReply.isPositiveCompletion(ftpClient.getReplyCode());
    }

    /**
     * 关闭FTP方法
     * @param ftp
     * @return
     */
    public boolean closeFTP(FTPClient ftp){

        try {
            ftp.logout();
        } catch (Exception e) {
            log.error("FTP关闭失败");
        }finally{
            if (ftp.isConnected()) {
                try {
                    ftp.disconnect();
                } catch (IOException ioe) {
                    log.error("FTP关闭失败");
                }
            }
        }

        return false;

    }

    protected void setCors(HttpServletRequest request,
                           HttpServletResponse response) {
        String origin = request.getHeader("Origin");
        if (StringUtils.isEmpty(origin)) {
            origin = "*";
        }
        String rh = request.getHeader("Access-Control-Request-Headers");
        if (StringUtils.isEmpty(origin)) {
            rh = "DNT,X-Mx-ReqToken,Keep-Alive,User-Agent,X-Requested-With,If-Modified-Since,Cache-Control," +
                    "Content-Type,Authorization,SessionToken,Content-Disposition";
        }
        // 解决跨域后，axios 无法获得其它 Response Header，默认只有 Content-Language，Content-Type，Expires，Last-Modified，Pragma
        response.setHeader("Access-Control-Expose-Headers", "Content-Disposition,Error-Code,Error-Message");
        response.setHeader("Access-Control-Allow-Origin", origin);
        response.setHeader("Access-Control-Allow-Credentials", "true");
        response.setHeader("Access-Control-Allow-Headers", rh);
    }


    /**
     * 下载FTP下指定文件
     * @param ftp FTPClient对象
     * @param filePath FTP文件路径
     * @param fileName 文件名
     * @param downPath 下载保存的目录
     * @return
     */
    public void downLoadFTP(FTPClient ftp, String filePath, String fileName,HttpServletRequest request, HttpServletResponse response) {
        // 默认失败
        boolean flag = false;

        try {
            // 跳转到文件目录
            ftp.changeWorkingDirectory(filePath);
            // 获取目录下文件集合
            ftp.enterLocalPassiveMode();
            FTPFile[] files = ftp.listFiles();
            for (FTPFile file : files) {
                // 取得指定文件并下载
                if (file.getName().equals(fileName)) {
                    File downFile = new File("/"
                            + file.getName());
                    InputStream is = new FileInputStream(downFile);
                    response.reset();
                    this.setCors(request, response);
                    response.setContentType("application/octet-stream");
                    response.setCharacterEncoding("utf-8");
                    response.setHeader("Content-Disposition", "attachment;filename=" + fileName);
                    response.addHeader("Content-Length", String.valueOf(downFile.length()));
                    byte[] buff = new byte[1024];
                    BufferedInputStream bis = null;
                    OutputStream os = null;
                    try {
                        os = response.getOutputStream();
                        bis = new BufferedInputStream(is);
                        int i = 0;
                        while ((i = bis.read(buff)) != -1) {
                            os.write(buff, 0, i);
                            os.flush();
                        }
                    } finally {
                        if (bis != null) {
                            bis.close();
                        }
                    }
                }
            }

        } catch (Exception e) {
            log.error("下载失败,error:{}",e);
        }
    }

    /**
     * FTP文件上传工具类
     * @param ftp
     * @param filePath
     * @param ftpPath
     * @return
     */
    public boolean uploadFile(FTPClient ftp,String filePath,String ftpPath){
        boolean flag = false;
        InputStream in = null;
        try {
         // 设置PassiveMode传输
            ftp.enterLocalPassiveMode();
            //设置二进制传输，使用BINARY_FILE_TYPE，ASC容易造成文件损坏
            ftp.setFileType(FTPClient.BINARY_FILE_TYPE);
            //判断FPT目标文件夹时候存在不存在则创建
            if(!ftp.changeWorkingDirectory(ftpPath)){
                ftp.makeDirectory(ftpPath);
            }
            //跳转目标目录
            ftp.changeWorkingDirectory(ftpPath);

            //上传文件
            File file = new File(filePath);
            in = new FileInputStream(file);
            String tempName = ftpPath+File.separator+file.getName();
            flag = ftp.storeFile(new String (tempName.getBytes("UTF-8"),"ISO-8859-1"),in);
            if(flag){
                log.info("上传成功");
            }else{
                log.error("上传失败");
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.error("上传失败");
        }finally{
            try {
                in.close();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        return flag;
    }

    /**
     * FPT上文件的复制
     * @param ftp  FTPClient对象
     * @param olePath 原文件地址
     * @param newPath 新保存地址
     * @param fileName 文件名
     * @return
     */
    public boolean copyFile(FTPClient ftp, String olePath, String newPath,String fileName) {
        boolean flag = false;

        try {
            // 跳转到文件目录
            ftp.changeWorkingDirectory(olePath);
            //设置连接模式，不设置会获取为空
            ftp.enterLocalPassiveMode();
            // 获取目录下文件集合
            FTPFile[] files = ftp.listFiles();
            ByteArrayInputStream  in = null;
            ByteArrayOutputStream out = null;
            for (FTPFile file : files) {
                // 取得指定文件并下载 
                if (file.getName().equals(fileName)) {

                    //读取文件，使用下载文件的方法把文件写入内存,绑定到out流上
                    out = new ByteArrayOutputStream();
                    ftp.retrieveFile(new String(file.getName().getBytes("UTF-8"),"ISO-8859-1"), out);
                    in = new ByteArrayInputStream(out.toByteArray());
                    //创建新目录
                    ftp.makeDirectory(newPath);
                    //文件复制，先读，再写
                    //二进制
                    ftp.setFileType(FTPClient.BINARY_FILE_TYPE);
                    flag = ftp.storeFile(newPath+File.separator+(new String(file.getName().getBytes("UTF-8"),"ISO-8859-1")),in);
                    out.flush();
                    out.close();
                    in.close();
                    if(flag){
                        log.info("转存成功");
                    }else{
                        log.error("复制失败");
                    }


                }
            }
        } catch (Exception e) {
            log.error("复制失败");
        }
        return flag;
    }

    /**
     * 实现文件的移动，这里做的是一个文件夹下的所有内容移动到新的文件，
     * 如果要做指定文件移动，加个判断判断文件名
     * 如果不需要移动，只是需要文件重命名，可以使用ftp.rename(oleName,newName)
     * @param ftp
     * @param oldPath
     * @param newPath
     * @return
     */
    public boolean moveFile(FTPClient ftp,String oldPath,String newPath){
        boolean flag = false;

        try {
            ftp.changeWorkingDirectory(oldPath);
            ftp.enterLocalPassiveMode();
            //获取文件数组
            FTPFile[] files = ftp.listFiles();
            //新文件夹不存在则创建
            if(!ftp.changeWorkingDirectory(newPath)){
                ftp.makeDirectory(newPath);
            }
            //回到原有工作目录
            ftp.changeWorkingDirectory(oldPath);
            for (FTPFile file : files) {

                //转存目录
                flag = ftp.rename(new String(file.getName().getBytes("UTF-8"),"ISO-8859-1"), newPath+File.separator+new String(file.getName().getBytes("UTF-8"),"ISO-8859-1"));
                if(flag){
                    log.info(file.getName()+"移动成功");
                }else{
                    log.error(file.getName()+"移动失败");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.error("移动文件失败");
        }
        return flag;
    }

    /**
     * 删除FTP上指定文件夹下文件及其子文件方法，添加了对中文目录的支持
     * @param ftp FTPClient对象
     * @param FtpFolder 需要删除的文件夹
     * @return
     */
    public boolean deleteByFolder(FTPClient ftp,String FtpFolder){
        boolean flag = false;
        try {
            ftp.changeWorkingDirectory(new String(FtpFolder.getBytes("UTF-8"),"ISO-8859-1"));
            ftp.enterLocalPassiveMode();
            FTPFile[] files = ftp.listFiles();
            for (FTPFile file : files) {
                //判断为文件则删除
                if(file.isFile()){
                    ftp.deleteFile(new String(file.getName().getBytes("UTF-8"),"ISO-8859-1"));
                }
                //判断是文件夹
                if(file.isDirectory()){
                    String childPath = FtpFolder + File.separator+file.getName();
                    //递归删除子文件夹
                    deleteByFolder(ftp,childPath);
                }
            }
            //循环完成后删除文件夹
            flag = ftp.removeDirectory(new String(FtpFolder.getBytes("UTF-8"),"ISO-8859-1"));
            if(flag){
                log.info(FtpFolder+"文件夹删除成功");
            }else{
                log.error(FtpFolder+"文件夹删除成功");
            }

        } catch (Exception e) {
            e.printStackTrace();
            log.error("删除失败");
        }
        return flag;

    }

    /**
     * 遍历解析文件夹下所有文件
     * @param folderPath 需要解析的的文件夹
     * @param ftp FTPClient对象
     * @return
     */
    public static List<NetDiskFile> readFileByFolder(FTPClient ftp,String folderPath){
        List<NetDiskFile> data = new ArrayList<>();
        boolean flage = false;
        try {
            ftp.changeWorkingDirectory(new String(folderPath.getBytes("UTF-8"),"ISO-8859-1"));
            //设置FTP连接模式
            ftp.enterLocalPassiveMode();
            //获取指定目录下文件文件对象集合
            FTPFile files[] = ftp.listFiles();
            InputStream in = null;
            BufferedReader reader = null;
            for (FTPFile file : files) {
                //判断为txt文件则解析
                if(file.isFile()){
                    String fileName = file.getName();
                    Calendar timestamp = file.getTimestamp();
                    Date time = timestamp.getTime();
                    long size = file.getSize();
                    String format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(time);
                    NetDiskFile netDiskFile = new NetDiskFile();
                    String fileSize = "";
                    if (size < 1024){
                        fileSize = size+"b";
                    }else if (size / 1024 < 1024){
                        size = size / 1024;
                        fileSize = size +"k";
                    }else {
                        fileSize = size / 1024 / 1024 +"M";
                    }
                    netDiskFile.setFileName(fileName);
                    netDiskFile.setUpdateTime(format);
                    netDiskFile.setFileSize(fileSize);
                    data.add(netDiskFile);
//                    System.out.println(time);
//                    if(fileName.endsWith(".txt")){
//                        in = ftp.retrieveFileStream(new String(file.getName().getBytes("UTF-8"),"ISO-8859-1"));
//                        reader = new BufferedReader(new InputStreamReader(in, "UTF-8"));
//                        String temp;
//                        StringBuffer buffer = new StringBuffer();
//                        while((temp = reader.readLine())!=null){
//                            buffer.append(temp);
//                        }
//                        if(reader!=null){
//                            reader.close();
//                        }
//                        if(in!=null){
//                            in.close();
//                        }
//                        //ftp.retrieveFileStream使用了流，需要释放一下，不然会返回空指针
//                        ftp.completePendingCommand();
                        //这里就把一个txt文件完整解析成了个字符串，就可以调用实际需要操作的方法
//                    }
                }
                //判断为文件夹，递归
                if(file.isDirectory()){
                    String path = folderPath+File.separator+file.getName();
                    readFileByFolder(ftp, path);
                }
            }


        } catch (Exception e) {
            e.printStackTrace();
            log.error("文件解析失败");
        }
        return data;

    }

    public static void main(String[] args) {
        FTPUtil test = new FTPUtil();
        FTPClient ftp = test.getFTPClient("192.168.10.90", 21, "root","lgwy@2020");
        try {
            boolean root = testFtp("192.168.10.90", 21, "root", "lgwy@2020");
            System.out.println(root);
        } catch (IOException e) {
            e.printStackTrace();
        }
//        List<NetDiskFile> netDiskFiles = readFileByFolder(ftp, "/home/userfile/admin");
//        for (NetDiskFile netDiskFile : netDiskFiles) {
//            System.out.println(netDiskFile);
//        }
//        System.exit(0);
    }
}
