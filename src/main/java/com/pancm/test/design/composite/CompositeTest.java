package com.pancm.test.design.composite;

import java.util.ArrayList;
import java.util.List;

/**
 * The type Composite test.
 *
 * @author pancm
 * @Title: CompositeTest
 * @Description: 组合模式  将对象组合成树形结构以表示"部分-整体"的层次结构。组合模式使得用户对单个对象和组合对象的使用具有一致性。
 * @Version:1.0.0
 * @date 2018年8月8日
 */
public class CompositeTest {

    /**
     * The entry point of application.
     *
     * @param args the input arguments
     */
    public static void main(String[] args) {
		/*
		 *  建立一个学生类，包含学生姓名和职位
		 *  
		 */
		Student studentLeader=new Student("小明","学生会主席");

		Student committeeMember=new Student("小刚","学生会委员");
		
		Student student=new Student("小红","学生");
		
		committeeMember.add(student);
		studentLeader.add(committeeMember);
		
		System.out.println("-"+studentLeader);
		studentLeader.get().forEach(sl->{
			System.out.println("--"+sl);
			sl.get().forEach(cm->{
				System.out.println("---"+cm);
			});
		});
		
		/*
		 * -Student [name=小明, position=学生会主席]
			--Student [name=小刚, position=学生会委员]
			---Student [name=小红, position=学生]
		 */
	}

}

/**
 * The type Student.
 */
class Student{
	private String name;
	
	private String position;
	
	private List<Student> students;

    /**
     * Instantiates a new Student.
     *
     * @param name     the name
     * @param position the position
     */
    public Student(String name, String position) {
		this.name = name;
		this.position = position;
		students=new ArrayList<Student>();
	}


    /**
     * Add.
     *
     * @param student the student
     */
    public void add(Student student){
		students.add(student);
	}

    /**
     * Remove.
     *
     * @param student the student
     */
    public void remove(Student student){
		students.remove(student);
	}

    /**
     * Get list.
     *
     * @return the list
     */
    public List<Student> get(){
		return students;
	}


	/** 
	 * 
	 */
	@Override
	public String toString() {
		return "Student [name=" + name + ", position=" + position + "]";
	}
	
	
}