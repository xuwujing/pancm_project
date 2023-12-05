package com.pancm.test.ioTest;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Properties;

/**
 * The type Test.
 *
 * @author pancm
 * @Title: Test
 * @Description: io字符流和字节流测试
 * @Version:1.0.0
 * @date 2018年6月11日
 */
public class Test {

    /**
     * The entry point of application.
     *
     * @param args the input arguments
     */
    public static void main(String[] args)  {
		try {
//			test();
//			test2();
//			test3();
//			test4();
//			test5();
//			test6();
			test7();
			
			/*
			 在使用System.getProperty("user.dir")时：
			如果是在IDE中启动，则获得的路径为D:\xxxx\projectName,包括项目名；
			
			如果是以Jar包方式启动，得到该jar包所在的路径。如project.jar在D:\xxxx下，获得的路径就是D:\xxxx
			
			但是如果是以war包方式启动获得的是：D:\apache-tomcat-9.0.7\bin
			
			所以此方法适合不依赖Tomcat容器（或者内嵌Tomcat如SpringBoot）的项目。
			 */
			String path=System.getProperty("user.dir");
			System.out.println("path:"+path);
			String os=System.getProperty("os.name").toLowerCase();
			System.out.println("os:"+os);
			//获取tomcat的目录
			String path2=System.getProperty("catalina.home");

			//获取文件的行数
			String paths =path+File.separator+"pom.xml";
			long lines = Files.lines(Paths.get(new File(paths).getPath())).count();
			System.out.println("行数:"+lines);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	

	/**
	 * 字符流
	 * @throws IOException
	 */
	private static void test() throws IOException {
		   String str;
		    // 使用 System.in 创建 BufferedReader 
		    BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		    System.out.println("输入字符, 输入 'quit' 退出。");
		    // 读取字符
		    do {
		       str=br.readLine();
		       System.out.println("您输入的字符是:"+str);
		    } while(!str.equals("quit"));
		    br.close();
	}
	
	/**
	 * 字节流
	 * 创建一个文件并新增一条记录
	 * @throws IOException
	 */
	private static void test2() throws IOException {
		String path="E:/test/hello.txt";
		String str="hello world";
		//创建一个文件并向文件中写数据 需要文件夹存在
		OutputStream output = new FileOutputStream(path);
		output.write(str.getBytes());
		output.close();
	}	
	
	/**
	 * 写入和读取文件
	 * @throws IOException
	 */
	private static void test3() throws IOException {
		//创建要操作的文件路径和名称  
        String path ="E:/test/hello.txt";
        String str="你好!";
        FileWriter fw = new FileWriter(path);  
        BufferedWriter bw=new BufferedWriter(fw);
        bw.write(str);  
        bw.close();
        fw.close();  
        
        FileReader fr = new FileReader(path);  
        BufferedReader br=new BufferedReader(fr);
        StringBuffer sb=new StringBuffer();
  		while(br.ready()){
  			sb.append((char)br.read());
  		}
        System.out.println("输出:"+sb.toString());
        br.close();
        fr.close();
	}	
	
	
	
	
	/**
	 * 字节流
	 * 创建一个文件并读取记录 防止乱码
	 * @throws IOException
	 */
	private static void test4() throws IOException {
		String path="E:/test/hello.txt";
		String path2="E:/test/你好.txt";
		String str="你好!";
		//从文件读取数据
		InputStream input = new FileInputStream(path);
		InputStreamReader reader = new InputStreamReader(input, "UTF-8");
	    StringBuffer sb=new StringBuffer();
		while(reader.ready()){
			sb.append((char)reader.read());
		}
		
		input.close();
		reader.close();
		
		//创建一个文件并向文件中写数据
		OutputStream output = new FileOutputStream(path2);
		OutputStreamWriter writer = new OutputStreamWriter(output, "UTF-8");
		writer.write(sb+str);
		
		writer.close();
		output.close();
		
		//从文件读取数据
		InputStream input2 = new FileInputStream(path2);
		InputStreamReader reader2 = new InputStreamReader(input2, "UTF-8");
	    StringBuffer sb2=new StringBuffer();
		while(reader2.ready()){
			sb2.append((char)reader2.read());
		}
		System.out.println("输出:"+sb2);
		input2.close();
		reader2.close();
	}	
	
	
	
	/**
	 * 字符流
	 * 写入和读取文件
	 * @throws IOException
	 */
	private static void test6() throws IOException {
		//创建要操作的文件路径和名称  
        //其中，File.separator表示系统相关的分隔符，Linux下为：/  Windows下为：\\  
        String path ="E:/test2/hello.txt";
        String str="hello world";
        FileWriter fw = new FileWriter(path);  
        //以path为路径创建一个新的FileWriter对象  
        //如果需要追加数据，而不是覆盖，则使用FileWriter（path，true）构造方法  
        //将字符串写入到流中，\r\n表示换行想有好的  
        fw.write(str);  
        fw.close();  
        
        FileReader fr = new FileReader(path);  
        StringBuffer sb=new StringBuffer();
  		while(fr.ready()){
  			sb.append((char)fr.read());
  		}
        System.out.println("输出:"+sb.toString());
        fr.close();
	}
	
	
	/**
	 * 字符流
	 * 写入和读取文件
	 * @throws IOException
	 */
	private static void test7() throws IOException {
		//创建要操作的文件路径和名称  
        String path ="E:/test2/hello.txt";
        Properties prop=new Properties();
        prop.setProperty("name", "zz");
        FileWriter fw = new FileWriter(path);  
        BufferedWriter bw=new BufferedWriter(fw);
        bw.write(prop.toString());  
        bw.close();
        fw.close();  
        
        FileReader fr = new FileReader(path);  
        BufferedReader br=new BufferedReader(fr);
        StringBuffer sb=new StringBuffer();
  		while(br.ready()){
  			sb.append((char)br.read());
  		}
  		String str=null;
		while ((str = br.readLine()) != null) {
			System.out.println("读取一行数据:"+str);
		}

        System.out.println("输出:"+sb.toString());
        br.close();
        fr.close();
	}
	
	
	
	/**
	 * 新建文件夹和文件
	 * @throws IOException
	 */
	private static void test5() throws IOException {
		String path="E:/test/test2";
		String path2="E:/test/test3/test3";
		String path3="E:/test/test2/test2.txt";
		File f = new File(path);
		File f2 = new File(path2);
		File f3 = new File(path3);
		//创建文件夹
		System.out.println("="+f.mkdir());
		//创建文件夹和所有父文件夹
		System.out.println("=="+f2.mkdirs());
		//创建一个文本
		System.out.println("==="+f3.createNewFile());
		//获取名称
		System.out.println("==="+f3.getName());
		//获取父级名称
		System.out.println("==="+f3.getParent());
		//获取当前路径
		System.out.println("==="+f3.getPath());
		//判断是否是目录
		System.out.println("=="+f2.isDirectory());
		System.out.println("==="+f3.isDirectory());
		//删除该文件
		System.out.println("==="+f3.delete());
		
	}	
	
}
