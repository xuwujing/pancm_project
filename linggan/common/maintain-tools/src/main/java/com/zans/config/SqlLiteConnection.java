package com.zans.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sqlite.SQLiteConnection;
import org.sqlite.jdbc4.JDBC4Statement;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.*;


/**
 * Title: sqlLiteTest
 * Description: sql测试
 * Version:1.0.0
 *
 * @author pancm
 * @date 2017年9月25日
 */
public class SqlLiteConnection {
	private static Logger logger = LoggerFactory.getLogger(SqlLiteConnection.class);
	private static Connection connection = null;
	private static SQLiteConnection sqlite = null;
	private final static String jdbc="org.sqlite.JDBC"; //连接
	private final static String memory="jdbc:sqlite::memory:"; //纯内存模式
	private final static String db="jdbc:sqlite:file_info.db"; //普通数据库模式
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


    /**
     * Gets connection.
     *
     * @return the connection
     */
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

    /**
     * Close connetion boolean.
     *
     * @return the boolean
     */
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
     *
     * @return lite connection
     */
    public synchronized static Connection getLiteConnection() {
		try {
			if(null== sqlite || sqlite.isClosed()){  //如果为空或者连接关闭
				try {
					sqlite = (SQLiteConnection) DriverManager.getConnection(db,null,null);//不使用用户名和密码
				} catch (SQLException e) {
					logger.error("获取数据库连接失败！",e);
				}
			}
		} catch (SQLException e) {
			logger.error("失败！",e);
		}
		return sqlite;
	}

    /**
     * The entry point of application.
     *
     * @param args the input arguments
     */
    public static void main(String[] args) {
    	String tableName = "file_info";
		creatTable();
		Map<String, String> valueMap  =  new HashMap<String, String>();
		valueMap.put("type_name","mysql");
		valueMap.put("num","1");
		valueMap.put("file_path","D://d1");
		valueMap.put("backup_path","D://bak//d1");
		valueMap.put("update_time", LocalDateTime.now().toString());
		insertTable(valueMap);

	}

	/**
	 * 创建表
	 */
	public static void creatTable(){
		String str="CREATE TABLE  IF NOT EXISTS \"file_info\" (\n" +
				"  \"id\" integer NOT NULL PRIMARY KEY AUTOINCREMENT,\n" +
				"  \"type_name\" text(32),\n" +
				"  \"num\" integer(11),\n" +
				"  \"file_path\" text(512),\n" +
				"  \"backup_path\" text(512),\n" +
				"  \"file_size\" text(32),\n" +
				"  \"update_time\" text\n" +
				");\n" +
				"CREATE INDEX  IF NOT EXISTS \"inx\"\n" +
				"ON \"file_info\" (\n" +
				"  \"type_name\" ASC,\n" +
				"  \"num\" ASC\n" +
				");\n";
		try {
			j4stmt=(JDBC4Statement) getLiteConnection().createStatement();
			j4stmt.executeUpdate(str);
			logger.info("成功创建表!");
		} catch (SQLException e) {
			logger.error("失败！",e);
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
			logger.error("失败！",e);
		}
	}

	/**
	 * 插入数据
	 */
	public static void insertTable(Map<String, String> valueMap){
		try {
			String tableName ="file_info";
			String str = getSql(tableName,valueMap);
			SQLiteConnection connection = (SQLiteConnection) getLiteConnection();
			connection.setAutoCommit(false);
			j4stmt=(JDBC4Statement) connection.createStatement();
		    j4stmt.executeUpdate(str);
			connection.commit();
			logger.info("写入成功!");
		} catch (SQLException e1) {
			try {
				sqlite.rollback();
			} catch (SQLException e) {
				logger.error("失败！",e);
			}
			logger.error("失败！",e1);
		}finally{
			try {
				j4stmt.close();
			} catch (SQLException e) {
				logger.error("失败！",e);
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
				sqlite.rollback();
			} catch (SQLException e) {
				logger.error("失败！",e);
			}
			logger.error("失败！",e1);
		}finally{
			try {
				j4stmt.close();
				System.out.println("成功插入数据!");
			} catch (SQLException e) {
				logger.error("失败！",e);
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
				sqlite.rollback();
			} catch (SQLException e) {
				logger.error("失败！",e);
			}
			e1.printStackTrace();
		}finally{
			try {
				j4stmt.close();
				System.out.println("成功插入数据!");
			} catch (SQLException e) {
				logger.error("失败！",e);
			}
		}
	}

	/**
	* @Author beixing
	* @Description  获取sql
	* @Date  2021/12/27
	* @Param
	* @return
	**/
	private static String getSql(String tableName, Map<String, String> valueMap){
		/** 获取数据库插入的Map的键值对的值 **/
		Set<String> keySet = valueMap.keySet();
		Iterator<String> iterator = keySet.iterator();
		/** 要插入的字段sql，其实就是用key拼起来的 **/
		StringBuilder columnSql = new StringBuilder();
		StringBuilder valueSql = new StringBuilder();
		/** 要插入的字段值，其实就是？ **/
		StringBuilder unknownMarkSql = new StringBuilder();
		Object[] bindArgs = new Object[valueMap.size()];
		int i = 0;
		while (iterator.hasNext()) {
			String key = iterator.next();
			columnSql.append(i == 0 ? "" : ",");
			columnSql.append(key);
			valueSql.append(i == 0 ? "" : ",");
			valueSql.append("'"+valueMap.get(key)+"'");
			unknownMarkSql.append(i == 0 ? "" : ",");
			unknownMarkSql.append("?");
			bindArgs[i] = valueMap.get(key);
			i++;
		}
		/** 开始拼插入的sql语句 **/
		StringBuilder sql = new StringBuilder();
		sql.append("INSERT INTO ");
		sql.append(tableName);
		sql.append(" (");
		sql.append(columnSql);
		sql.append(" )  VALUES (");
		sql.append(valueSql);
		sql.append(" )");
		return sql.toString();
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
			logger.error("失败！",e);
		}finally{
			try {
				j4stmt.close();
				rs.close();

			} catch (SQLException e) {
				// TODO Auto-generated catch block
				logger.error("失败！",e);
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
				sqlite.rollback();
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			logger.error("失败！",e);
		}finally{
			try {
				j4stmt.close();
				System.out.println("成功释放");
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				logger.error("失败！",e);
			}
		}
	}

    /**
     * 将查询的数据转换成List类型
     *
     * @param rs the rs
     * @return list
     * @throws SQLException the sql exception
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
