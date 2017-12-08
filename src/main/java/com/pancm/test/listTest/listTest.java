package com.pancm.test.listTest;

import java.util.ArrayList;
import java.util.List;

/**
 * 
* Title: listTest
* Description:关于list测试 
* Version:1.0.0  
* @author pancm
* @date 2017年10月13日
 */
public class listTest {

	public static void main(String[] args) {
        List<Integer> list=initData(100);
		System.out.println("list:"+list);
		System.out.println("removeList:"+removeList(list,10,20));
		list.subList(10, 20).clear();
		System.out.println("subList:"+list);
		
		List<String> ls1=new ArrayList<String>();
		List<String> ls2=new ArrayList<String>();
		ls1.add("a");
		ls1.add("b");
		ls1.add("c");
		ls2.add("a");
		ls2.add("d");
		ls2.add("e");
        System.out.println("合集:"+addAll(ls1,ls2));			
        System.out.println("交集 :"+retainAll(ls1,ls2));		
        System.out.println("差集 :"+removeAll(ls1,ls2));		
        System.out.println("并集 :"+andAll(ls1,ls2));			
        
	}
	 
	/**
	 * for循环移除指定数据
	 * @param list 
	 * @param s  要移除的起始位置
	 * @param d  要移除的最后位置 
	 */
	private static List<Integer> removeList(List<Integer> list,int s,int d){
		for(int i=0,j=list.size();i<j;i++){
			if(i>=s&&i<d){
				list.remove(i);
			}
		}
		return list;
	}
    
	/**
	 * 获取数组数据
	 * @param j
	 * @return
	 */
	private static List<Integer> initData(int j){
		 List<Integer> list=new ArrayList<Integer>();  
		 for(int i=1;i<=j;i++){
			 list.add(i);
		 }
		return list;
	}
	
	/**
	 * 数组合集
	 * @param ls1
	 * @param ls2
	 * @return
	 */
	private static List<String> addAll(List<String> ls1,List<String>ls2){
		ls1.addAll(ls2);
		return ls1;
	}
	
	/**
	 * 数组交集 （retainAll 会删除 ls1在ls2中没有的元素）
	 * @param ls1
	 * @param ls2
	 * @return
	 */
	private static List<String> retainAll(List<String> ls1,List<String>ls2){
		ls1.retainAll(ls2);
		System.out.println(ls1+";ls2:"+ls2);
		return ls1;
	}
	
	/**
	 * 差集 (删除ls2中没有ls1中的元素)
	 * @param ls1
	 * @param ls2
	 * @return
	 */
	private static List<String> removeAll(List<String> ls1,List<String>ls2){
		ls1.removeAll(ls2);
		System.out.println(ls1+";ls2:"+ls2);
		return ls1;
	}
	
	/**
	 * 无重复的并集 (ls1和ls2中并集，并无重复)
	 * @param ls1
	 * @param ls2
	 * @return
	 */
	private static List<String> andAll(List<String> ls1,List<String>ls2){
		//删除在ls1中出现的元素
		ls2.removeAll(ls1);
		//将剩余的ls2中的元素添加到ls1中
		ls1.addAll(ls2);
		System.out.println(ls1+";ls2:"+ls2);
		return ls1;
	}
} 
