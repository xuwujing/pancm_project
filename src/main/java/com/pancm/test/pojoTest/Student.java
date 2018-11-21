package com.pancm.test.pojoTest;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

/**
 * 
* @Title: Student
* @Description: 
* 学生信息表
* 里面的属性通过lombok实现
* @Version:1.0.0  
* @author pancm
* @date 2018年1月11日
 */
/*@Data   ：注解在类上；提供类所有属性的 getting 和 setting 方法，此外还提供了equals、canEqual、hashCode、toString 方法
@Setter：注解在属性上；为属性提供 setting 方法
@Getter：注解在属性上；为属性提供 getting 方法
@Log4j ：注解在类上；为类提供一个 属性名为log 的 log4j 日志对象
@Slf4j ：注解在类上；为类提供一个 属性名为log 的 Slf4j 日志对象
@NoArgsConstructor：注解在类上；为类提供一个无参的构造方法
@AllArgsConstructor：注解在类上；为类提供一个全参的构造方法
@ToString：注解在类上；为类提供一个toString的方法，可以指定属性进行排除
*/
@Data
@Slf4j
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = {"id","classId"})
public class Student {
    /** 学生id */
	private int id;
	/** 学生姓名 */
	private String name;
	/** 班级ID  */
	private int  classId;
	
	public static void main(String[] args) {
		Student student=new Student();
		student.setId(1);
		student.setName("xuwujing");
		student.setClassId(1);
		log.info("id:{},姓名:{},班级:{}",student.getId(),student.getName(),student.getClassId());
		log.info("学生信息:{}",student.toString());
		
	}
}
