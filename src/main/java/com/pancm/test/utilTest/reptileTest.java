package com.pancm.test.utilTest;

import com.geccocrawler.gecco.GeccoEngine;
import com.geccocrawler.gecco.annotation.Gecco;
import com.geccocrawler.gecco.annotation.Html;
import com.geccocrawler.gecco.annotation.HtmlField;
import com.geccocrawler.gecco.annotation.RequestParameter;
import com.geccocrawler.gecco.annotation.Text;
import com.geccocrawler.gecco.spider.HtmlBean;

/**
 * 
* Title: reptileTest
* Description:
* 爬虫测试 
* Version:1.0.0  
* @author pancm
* @date 2018年3月6日
 */
@Gecco(matchUrl="https://github.com/{user}/{project}", pipelines="consolePipeline")
public class reptileTest implements HtmlBean{

	    private static final long serialVersionUID = -7127412585200687225L;
	    
	    @RequestParameter("user")
	    private String user;//url中的{user}值
	    
	    @RequestParameter("project")
	    private String project;//url中的{project}值
	    
	    @Text
	    @HtmlField(cssPath=".repository-meta-content")
	    private String title;//抽取页面中的title
	    
	    @Text
	    @HtmlField(cssPath=".pagehead-actions li:nth-child(2) .social-count")
	    private int star;//抽取页面中的star
	    
	    @Text
	    @HtmlField(cssPath=".pagehead-actions li:nth-child(3) .social-count")
	    private int fork;//抽取页面中的fork
	    
	    @Html
	    @HtmlField(cssPath=".entry-content")
	    private String readme;//抽取页面中的readme

	    public String getReadme() {
	        return readme;
	    }

	    public void setReadme(String readme) {
	        this.readme = readme;
	    }

	    public String getUser() {
	        return user;
	    }

	    public void setUser(String user) {
	        this.user = user;
	    }

	    public String getProject() {
	        return project;
	    }

	    public void setProject(String project) {
	        this.project = project;
	    }

	    public String getTitle() {
	        return title;
	    }

	    public void setTitle(String title) {
	        this.title = title;
	    }

	    public int getStar() {
	        return star;
	    }

	    public void setStar(int star) {
	        this.star = star;
	    }

	    public int getFork() {
	        return fork;
	    }

	    public void setFork(int fork) {
	        this.fork = fork;
	    }
	    
	    public static void main(String[] args) {
	        GeccoEngine.create()
	        //工程的包路径
	        .classpath("com.pancm.test.utilTest")
	        //开始抓取的页面地址
	        .start("https://github.com/xuwujing/Netty-study")
	        //开启几个爬虫线程
	        .thread(1)
	        //单个爬虫每次抓取完一个请求后的间隔时间
	        .interval(2000)
	        //循环抓取
	        .loop(true)
	        //使用pc端userAgent
	        .mobile(false)
	        //非阻塞方式运行
	        .start();
	    }
	
}
