package com.pancm.test.servletTest;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 
* Title: ServletHttpTest
* Description:
* servlet http服务测试 
* Version:1.0.0  
* @author pancm
* @date 2018年3月1日
 */
public class ServletHttpTest extends HttpServlet{
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException{
		String name= req.getParameter("name");
		PrintWriter pw=resp.getWriter();
		System.out.println("name:"+name);
		pw.write("返回...");
	}
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException{
		String name= req.getParameter("name");
		PrintWriter pw=resp.getWriter();
		System.out.println("name:"+name);
		pw.write("返回...");
	}
}
