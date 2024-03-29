/**
 * 
 */
package com.pancm.test.googleTest.commons;

import com.google.common.base.Function;
import com.google.common.collect.Lists;
import com.pancm.test.pojoTest.User;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * The type Google test.
 *
 * @author pancm
 * @Title: googleTest
 * @Description:
 * @Version:1.0.0
 * @date 2018年5月14日
 */
public class googleTest {

    /**
     * The entry point of application.
     *
     * @param args the input arguments
     */
    public static void main(String[] args) {
		test();
	}

	/**
	 * 数组里所有对象的某个属性的值改变
	 */
	private static void test(){
		List<User> userList=new ArrayList<>();
		User user=new User();
		user.setId(1);
		user.setName("张三");
		userList.add(user);
		User user2=new User();
		user2.setId(2);
		user2.setName("李四");
		userList.add(user2);
		System.out.println("更改之前的数据:"+userList);
	    userList=Lists.transform(userList,new Function<User, User>() {
			@Override
			public User apply(User user) {
				user.setName("王五");
				return user;
			}
		});
		System.out.println("更改之后的数据:"+userList);

		String str = " xu wu jing ";
		System.out.println("去掉所有空格:"+StringUtils.trimAllWhitespace(str));
	}
}
