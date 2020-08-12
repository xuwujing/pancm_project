package com.pancm.test.othersTest;

import ch.ethz.ssh2.*;

import java.io.*;

/**
 * @Author pancm
 * @Description 自动化部署之文件上传
 * @Date  2020/8/12
 * @Param
 * @return
 **/
public class ScpClient {

    private static ScpClient instance;

    private String ip;

    private int port;

    private String name;

    private String password;

    /**
     * 私有化默认构造函数
     * 实例化对象只能通过getInstance
     */
    private ScpClient(){

    }

    /**
     * 私有化有参构造函数
     * @param ip 服务器ip
     * @param port 服务器端口 22
     * @param name 登录名
     * @param password 登录密码
     */
    private ScpClient(String ip,int port,String name,String password){
        this.ip = ip ;
        this.port = port;
        this.name = name;
        this.password = password;
    }

    /**
     * download
     * @param remoteFile 服务器上的文件名
     * @param remoteTargetDirectory 服务器上文件的所在路径
     * @param newPath 下载文件的路径
     */
    public void downloadFile(String remoteFile, String remoteTargetDirectory,String newPath){
        Connection connection = new Connection(ip,port);

        try {
            connection.connect();
            boolean isAuthenticated = connection.authenticateWithPassword(name,password);
            if(isAuthenticated){
                SCPClient scpClient = connection.createSCPClient();
                SCPInputStream sis = scpClient.get(remoteTargetDirectory + "/" + remoteFile);
                File f = new File(newPath);
                if(!f.exists()){
                    f.mkdirs();
                }
                File newFile = new File(newPath + remoteFile);
                FileOutputStream fos = new FileOutputStream(newFile);
                byte[] b = new byte[4096];
                int i;
                while ((i = sis.read(b)) != -1){
                    fos.write(b,0, i);
                }
                fos.flush();
                fos.close();
                sis.close();
                connection.close();
                System.out.println("download ok");
            }else{
                System.out.println("连接建立失败");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     *  获取服务器上相应文件的流
     * @param remoteFile 文件名
     * @param remoteTargetDirectory 文件路径
     * @return
     * @throws IOException
     */
    public SCPInputStream getStream(String remoteFile, String remoteTargetDirectory) throws IOException {
        Connection connection = new Connection(ip,port);
        connection.connect();
        boolean isAuthenticated = connection.authenticateWithPassword(name,password);
        if(!isAuthenticated){
            System.out.println("连接建立失败");
            return null;
        }
        SCPClient scpClient = connection.createSCPClient();
        return scpClient.get(remoteTargetDirectory + "/" + remoteFile);
    }

    /**
          * 解析脚本执行返回的结果集
          * 
          * @param in
          *            输入流对象
          * @param charset
          *            编码
          * @return 以纯文本的格式返回
          */
    private static String processStdout(InputStream in, String charset) {
        InputStream stdout = new StreamGobbler(in);
        StringBuffer buffer = new StringBuffer();
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(stdout, charset));
            String line = null;
            while ((line = br.readLine()) != null) {
                buffer.append(line + "\n");
                System.out.println(line);
            }
            br.close();
        } catch (UnsupportedEncodingException e) {
            System.out.println("解析脚本出错：" + e.getMessage());
            e.printStackTrace();
        } catch (IOException e) {
            System.out.println("解析脚本出错：" + e.getMessage());
            e.printStackTrace();
        }
        return buffer.toString();
    }


    /**
          * 远程执行shll脚本或者命令
          * 
          * @param cmd
          *            即将执行的命令
          * @return 命令执行完后返回的结果值
          */
    private  String execmd( String cmd) {
        Connection connection = new Connection(ip,port);
        String result = "";
        try{
            connection.connect();
            boolean isAuthenticated = connection.authenticateWithPassword(name,password);
            if(!isAuthenticated){
                System.out.println("连接建立失败");
                return "";
            }
            if (connection != null) {
                Session session = connection.openSession();// 打开一个会话
                session.execCommand(cmd);// 执行命令
                result = processStdout(session.getStdout(), "UTF-8");
                System.out.println(result);
                // 如果为得到标准输出为空，说明脚本执行出错了
			/*if (StringUtils.isBlank(result)) {
			                    System.out.println("得到标准输出为空,链接conn:" + connection + ",执行的命令：" + cmd);
			                    result = processStdout(session.getStderr(), DEFAULTCHART);
			                } else {
			                    System.out.println("执行命令成功,链接conn:" + connection + ",执行的命令：" + cmd);
			                }*/
                connection.close();
                session.close();
            }
        } catch (IOException e) {
            System.out.println("执行命令失败,链接conn:" + connection + ",执行的命令：" + cmd + "  " + e);
            e.printStackTrace();
        }
        return result;

    }


    /**
     * 上传文件到服务器
     * @param f 文件对象
     * @param remoteTargetDirectory 上传路径
     * @param mode 默认为null
     */
    public void uploadFile(File f, String remoteTargetDirectory, String mode) {
        Connection connection = new Connection(ip,port);

        try {
            connection.connect();
            boolean isAuthenticated = connection.authenticateWithPassword(name,password);
            if(!isAuthenticated){
                System.out.println("连接建立失败");
                return ;
            }
            SCPClient scpClient = new SCPClient(connection);
            SCPOutputStream os = scpClient.put(f.getName(),f.length(),remoteTargetDirectory,mode);
            byte[] b = new byte[4096];
            FileInputStream fis = new FileInputStream(f);
            int i;
            while ((i = fis.read(b)) != -1) {
                os.write(b, 0, i);
            }
            os.flush();
            fis.close();
            os.close();
            connection.close();
            System.out.println("upload ok");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * 单例模式
     * 懒汉式
     * 线程安全
     * @return
     */
    public static ScpClient getInstance(){
        if(null == instance){
            synchronized (ScpClient.class){
                if(null == instance){
                    instance = new ScpClient();
                }
            }
        }
        return instance;
    }


    public static ScpClient getInstance(String ip,int port,String name,String password){
        if(null == instance){
            synchronized (ScpClient.class){
                if(null == instance){
                    instance = new ScpClient(ip,port,name,password);
                }
            }
        }
        return instance;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }


    public  static  void  main(String[] args){

        File  f1=new File("C:/Users/nijun/Desktop/QQ截图20181013162821副本.jpg");

       ScpClient.getInstance("192.168.40.200",22, "root","xiangzun@11").uploadFile(f1, "/home/temp/", null);

        ScpClient.getInstance("192.168.40.200",22, "root","xiangzun@11").downloadFile("hosts","/etc/ansible","D:/");

       // ScpClient.getInstance("192.168.40.200",22, "root","xiangzun@11").execmd("sh /etc/ansible/test.sh  192.168.40.201  xiangzun@11");
    }


}

