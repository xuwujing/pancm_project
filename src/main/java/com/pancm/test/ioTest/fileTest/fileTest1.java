package com.pancm.test.ioTest.fileTest;

import java.io.File;
import java.io.IOException;

/**
 * 
* Title: fileTest2
* Description:  关于File类中的文件夹测试
* Version:1.0.0  
* @author panchengming
 */
public class fileTest1 {
	
	private final static String path="e:"+File.separator;  //通用路径
	public static void main(String[] args) {
		createNewMkdir(path+"test"); //创建一个test文件夹
		searchFile(path+"tomcat7");  //查询tomcat7文件夹下的文件
		searchFilePath(path+"tomcat7"); //查询tomcat7文件夹下的文件及路径
		printAllFile(path+"tomcat7");//用递归输出tomcat7文件的所有文件
	}
	
	/**
	 * 创建文件夹
	 * @param path
	 * getAbsolutePath():返回的是定义时的路径对应的相对路径，但不会处理“.”和“..”的情况
		getCanonicalPath():返回的是规范化的绝对路径，相当于将getAbsolutePath()中的“.”和“..”解析成对应的正确的路径
	 */
	public static void createNewMkdir(String path){
		File file=new File(path); //实例化File文件
		file.mkdir(); //创建文件夹
		File file1=new File(".\\test.txt");
		System.out.println("getAbsoluteFile:"+file1.getAbsolutePath()); //getAbsoluteFile:D:\WorkSpace\TestMain\.\test.txt
		try {
			System.out.println("getCanonicalPath:"+file1.getCanonicalPath());//getCanonicalPath:D:\WorkSpace\TestMain\test.txt
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("文件夹创建成功");
	}
	
	/**
	 * 查询文件夹的目录
	 * @param path
	 */
	public static void searchFile(String path){
		File file=new File(path); //实例化File文件
		String list[]=file.list();	//获取该目录
		if(list!=null){
		for(int i=0;i<list.length;i++){
			System.out.println(list[i]);
		}
		}
	}
	
	/**
	 * 查询文件夹的目录路径
	 * @param path
	 */
	public static void searchFilePath(String path){
		File file=new File(path); 		//实例化File文件
		File list[]=file.listFiles();	//列出目录路径,注意需要一个files对象数组接受这个返回数组。
		if(list!=null){
		for(int i=0;i<list.length;i++){
			System.out.println(list[i]);
		}
		}
	}
	
	/**
	 * 列出指定目录的所有的内容，包括子文件夹下的文件。
	 * 首先使用File中的 isDirectory()方法判断是不是目录，
	 * 如果是目录，则列出所有文件。
	 * 因为存在空文件夹的情况,所以需要判断文件夹是否为空。
	 * 还有文件夹包含子文件，那么利用递归利于列出所有文件。
	 * @param path
	 */
	public static void printAllFile(String path){
		File file=new File(path); 		//实例化File文件
		print(file);
	}
	
	/**
	 * 利用递归输出文件
	 * @param file
	 */
	private static void print(File file){
		if(file!=null){ 
			if(file.isDirectory()){ //如果是目录,则继续进行;否则输出该文件
				File list[]=file.listFiles();
				if(list!=null){  //如果该文件夹不是空文件夹，则继续。
					for(int i=0;i<list.length;i++){
						print(list[i]);
					}
				}
			}else{
				System.out.println(file);
			}
		}
	}
	
	
}
