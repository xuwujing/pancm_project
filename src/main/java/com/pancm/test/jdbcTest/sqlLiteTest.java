package com.pancm.test.jdbcTest;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sqlite.SQLiteConnection;
import org.sqlite.jdbc4.JDBC4Statement;


/**
 * 
* Title: sqlLiteTest
* Description: sql测试
* Version:1.0.0  
* @author pancm
* @date 2017年9月25日
 */
public class sqlLiteTest {
	private static Logger logger = LoggerFactory.getLogger(sqlLiteTest.class);
	private static Connection connection = null;  
	private static SQLiteConnection sqllite = null;  
	private final static String jdbc="org.sqlite.JDBC"; //连接
	private final static String memory="jdbc:sqlite::memory:"; //纯内存模式
	private final static String db="jdbc:sqlite:d:/test.db"; //普通数据库模式
	private static JDBC4Statement j4stmt=null; 
	private static Statement stmt=null; 

	/***
	 * 静态加载Sqlite驱动类
	 */
	static {
		try {
			Class.forName(jdbc);
		} catch (ClassNotFoundException e) {
			logger.error("静态加载驱动失败！",e);
		}			
	}	
	
	
	public synchronized static Connection getConnection() {
		if(null==connection){
			try {
				connection = DriverManager.getConnection(memory,null,null);//不适用用户名和密码	
			} catch (SQLException e) {
				logger.error("获取数据库连接失败！",e);
			}			
		}
		return connection;
	}
	
	public synchronized static  boolean closeConnetion(){
		try {
			if(null!=connection){
				connection.close();
			}
		} catch (Exception e) {
			logger.error("关闭数据库连接失败！",e);
		}
    	return true;
	}
	
	
	/**
	 * sqlLite
	 * @return
	 */
	public synchronized static Connection getLiteConnection() {
		try { 
			if(null==sqllite||sqllite.isClosed()){  //如果为空或者连接关闭
				try {
					sqllite = (SQLiteConnection) DriverManager.getConnection(db,null,null);//不使用用户名和密码	
				} catch (SQLException e) {
					logger.error("获取数据库连接失败！",e);
				}			
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return sqllite;
	}
	
	public static void main(String[] args) {
		creatTable();//创建有主键表     
		creatTable1(); //创建无主键的表
		long starTime=System.currentTimeMillis(); 
		insertTable(100); //在有主键的表中, 插入100万数据
		long endTime=System.currentTimeMillis(); 
		System.out.println("在有主键的表插入100数据总共使用时间为:"+(endTime-starTime)+"毫秒");
	
		long starTime1=System.currentTimeMillis(); 
		insertTable1(100); //在没有主键的表中, 插入100万数据
		long endTime1=System.currentTimeMillis(); 
		System.out.println("在没有有主键的表插入100万数据总共使用时间为:"+(endTime1-starTime1)+"毫秒");
		
		long starTime2=System.currentTimeMillis(); 
		selectTable("test"); //查询有主键表的所有数据
		long endTime2=System.currentTimeMillis(); 
		System.out.println("查询有主键表的所有数据总共使用时间为:"+(endTime2-starTime2)+"毫秒");
		
		long starTime3=System.currentTimeMillis(); 
		selectTable("test1"); //查询没有主键表的所有数据
		long endTime3=System.currentTimeMillis(); 
		System.out.println("查询没有主键表的所有数据总共使用时间为:"+(endTime3-starTime3)+"毫秒");
		
	}
	
	/**
	 * 创建表
	 */
	private static void creatTable(){
		String sql="DROP TABLE IF EXISTS test ";//存在则删除
		String str="create table test(id integer primary key autoincrement, name varchar(20),age int);";
		try {
//			stmt=getConnection().createStatement();
			j4stmt=(JDBC4Statement) getLiteConnection().createStatement();
			j4stmt.executeUpdate(sql);
			j4stmt.executeUpdate(str);
			System.out.println("成功创建表!");
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	
	/**
	 * 创建没有主键的表
	 */
	private static void creatTable1(){
		String sql="DROP TABLE IF EXISTS test1 ";//存在则删除
		String str="create table test1(name varchar(20),age int,id long);";
		try {
//			stmt=getConnection().createStatement();
			j4stmt=(JDBC4Statement) getLiteConnection().createStatement();
			j4stmt.executeUpdate(sql);
			j4stmt.executeUpdate(str);
			System.out.println("成功创建没有主键的表!");
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 插入数据
	 */
	private static void insertTable(int j){
		try {
			j4stmt=(JDBC4Statement) getLiteConnection().createStatement();
			for(int i=1;i<=j;i++){
				String str="insert or ignore into test ( name,age,id) values('张三','20',"+i+");";
//				String str="insert  into test ( name,age) values('张三',"+i+") where not exists(select 1 from test where id=2) ;";
				 j4stmt.executeUpdate(str);
				}
		} catch (SQLException e1) {
			try {
				sqllite.rollback();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			e1.printStackTrace(); 
		}finally{
			try {
				j4stmt.close();
				System.out.println("成功插入数据!");
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * 插入数据
	 */
	private static void insertTable1(int j){
		try {
			j4stmt=(JDBC4Statement) getLiteConnection().createStatement();
			for(int i=1;i<=j;i++){
				String str="insert or ignore into test1 ( name,age,id) values('张三','30',"+i+");";
				 j4stmt.executeUpdate(str);
				}
		} catch (SQLException e1) {
			try {
				sqllite.rollback();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			e1.printStackTrace();
		}finally{
			try {
				j4stmt.close();
				System.out.println("成功插入数据!");
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * 插入数据使用ignore
	 */
	private static void insertTableIgnore(int j){
		try {
			j4stmt=(JDBC4Statement) getLiteConnection().createStatement();
			for(int i=1;i<j;i++){
				String str="ignore into test ( name,age,id) values('张三','20',"+i+");";
				 j4stmt.executeUpdate(str);
				}
		} catch (SQLException e1) {
			try {
				sqllite.rollback();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			e1.printStackTrace();
		}finally{
			try {
				j4stmt.close();
				System.out.println("成功插入数据!");
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	
	
	/**
	 * 查询表
	 */
	private static void selectTable(String tableName){
		String str="select * from "+tableName+";";
		ResultSet rs=null;
		 int count =1;
		try {
//			stmt=getConnection().createStatement();
			j4stmt=(JDBC4Statement) getLiteConnection().createStatement();

			rs=j4stmt.executeQuery(str);
		   List list = convertList(rs);
		   if(list!=null&&list.size()>0){
			   for(int i=0,j=list.size();i<j;i++){
				   Map map=(Map) list.get(i);
//					   System.out.println("条数:"+ count +","+map);
//					   count++;
			   }
		   }
		   System.out.println("count:"+list.size());
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			try {
				j4stmt.close();
				rs.close();
				
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	private static String queryTable(String tableName,String param){
		String str="select * from "+tableName+";";
		
		return str;	
	}
	
	
	
	/**
	 * 删除表数据
	 */
	private static void deleteTable(String tableName){
		String str="delete from "+tableName+";";
		try {
//			stmt=getConnection().createStatement();
			j4stmt=(JDBC4Statement) getLiteConnection().createStatement();
			j4stmt.executeUpdate(str);
		   System.out.println("成功删除数据!");
		} catch (SQLException e) {
			try {
				sqllite.rollback();
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			e.printStackTrace();
		}finally{
			try {
				j4stmt.close();
				System.out.println("成功释放");
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * 将查询的数据转换成List类型
	 * @param rs
	 * @return
	 * @throws SQLException
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static List convertList(ResultSet rs) throws SQLException {
        if(null==rs){
        	return null;
        }
		List list = new ArrayList();
        ResultSetMetaData md = rs.getMetaData();
        int columnCount = md.getColumnCount();
        while (rs.next()) {
            Map rowData = new HashMap();
            for (int i = 1; i <= columnCount; i++) {
                rowData.put(md.getColumnName(i), rs.getObject(i));
            }
            list.add(rowData);
        }
        return list;
	}
}
