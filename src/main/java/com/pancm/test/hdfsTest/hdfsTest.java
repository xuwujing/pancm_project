package com.pancm.test.hdfsTest;

import java.io.FileNotFoundException;
import java.io.IOException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.BlockLocation;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FSDataOutputStream;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;

/**
 * 
* Title: hdfsTest
* Description: HDFS测试
* Version:1.0.0  
* @author pancm
* @date 2017年11月8日
 */
public class hdfsTest {
		/** 文件夹路径  */
	  	private static final String srcPath="D:\\HadoopTest\\Hdfs";
	  	/** 文件路径 */
	  	private static final String srcFile="D:\\HadoopTest\\Hdfs\\hdfs.txt";
	  	/** 要更改的文件名*/
	  	private static final String srcFile1="D:\\HadoopTest\\Hdfs\\HDFS.txt";
	  	
	  	/**tomcat目录 */
	  	private static final String testPath="D:\\tomcat";
	  	
	  	/**主目录 */
	  	private static final String  srcPath1="D:\\HadoopTest";
	  	
	  	static Configuration conf = new Configuration();  
	 
	  	public static void main(String[] args) throws IOException {  
	  		 FileSystem fs = FileSystem.get(conf);  
			  makeDir(fs,new Path(srcPath));
			  deleteDir(fs,new Path(srcPath));
			  writeFile(fs,new Path(srcFile));
			  readFile(fs,new Path(srcFile));
	          getAllChildFile(fs,new Path(testPath));  
	          uploadFile(fs,new Path(srcFile),new Path(srcPath1));
	          downloadFile(fs,new Path(srcFile),new Path(srcPath1));
	          
	          getFileBlockLocations(fs,new Path(srcFile));
	          
	          rename(fs,new Path(srcFile),new Path(srcFile1));
	    } 
	  
	  /**
	   * 文件夹创建
	   * @throws IOException
	   */
	 private static void makeDir(FileSystem fs,Path path) throws IOException{
	        if(!fs.exists(path)){
	            fs.mkdirs(path);
	            System.out.println("文件夹创建成功!");
	        }
	        fs.close(); 
	 }
	 
	 /**
	  * 文件夹删除
	  * @throws IOException
	  */
	 private static void deleteDir(FileSystem fs,Path path) throws IOException{
	        fs.deleteOnExit(path);
	        fs.close();  
	        System.out.println("文件夹删除成功！");
	 }
	 
	 /**
	  * 文件写入
	 * @throws IOException 
	  */
	 private static void writeFile(FileSystem fs,Path path) throws IOException{
    	 FSDataOutputStream out = fs.create(path);  
         out.writeUTF("Hello,Hadoop!");  
         System.out.println("写入成功！");
	     fs.close();  
	 }
	 
	 /**
	  * 文件读取
	 * @throws IOException 
	  */
	 private static void readFile(FileSystem fs,Path path) throws IOException{
	        if(fs.exists(path)){  
	            FSDataInputStream is = fs.open(path);  
	            FileStatus status = fs.getFileStatus(path);  
	            byte[] buffer = new byte[Integer.parseInt(String.valueOf(status.getLen()))];  
	            is.readFully(0, buffer);  
	            is.close();  
	            fs.close();  
	            String str=new String(buffer);
	            System.out.println("文本中的信息:"+str);  
	        } 
	 }
	 
	 /**
	  * 获取给定目录下的所有子目录以及子文件 
	  * @param path
	  * @param fs
	  * @throws FileNotFoundException
	  * @throws IOException
	  */
	 private static void getAllChildFile(FileSystem fs,Path path) throws FileNotFoundException, IOException{
		 FileStatus[] fileStatus = fs.listStatus(path);  
	        int len=fileStatus.length;
	        for(int i=0;i<len;i++){  
	            if(fileStatus[i].isDirectory()){  
	                Path p = new Path(fileStatus[i].getPath().toString());  
	                getAllChildFile(fs,p);  
	            }else{  
	                System.out.println(fileStatus[i].getPath().toString());  
	            }  
	        }  
	        fs.close();  
	 }
	 
	 /**
	  * 上传文件 
	  * @param fs
	  * @param path
	  * @throws IOException
	  */
	 private static void uploadFile(FileSystem fs,Path path,Path dst) throws IOException{
	        fs.copyFromLocalFile(path, dst);  
	        fs.close();  
	        System.out.println("上传成功！");
	 }
	 
	 /**
	  * 下载文件 
	  * @param fs
	  * @param path
	  * @throws IOException
	  */
	 private static void downloadFile(FileSystem fs,Path path,Path dst) throws IOException{
	        fs.copyFromLocalFile(path, dst);  
	        fs.close();  
	        System.out.println("下载成功！");
	 }
	 
	 /**
	  * 查找某个文件在 HDFS集群的位置
	  * @param fs
	  * @param path
	  * @throws IOException
	  */
	 private static void getFileBlockLocations(FileSystem fs,Path path) throws IOException{
		  // 文件块位置列表
		  BlockLocation[] blkLocations = new BlockLocation[0];
		  // 获取文件目录 
		  FileStatus filestatus = fs.getFileStatus(path);
		  //获取文件块位置列表
		  blkLocations = fs.getFileBlockLocations(filestatus, 0, filestatus.getLen());
		  fs.close();  
		  System.out.println("集群的位置:"+blkLocations);
	 }
	 
	 /**
	  * 重命名
	  * @param fs
	  * @param path
	  * @throws IOException 
	  */
	 private static void rename(FileSystem fs,Path path,Path path1) throws IOException{
			 fs.rename(path, path1);
			 fs.close();  
			 System.out.println("重命名成功！");
	 }
	 
}
