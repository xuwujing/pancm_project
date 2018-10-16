package com.pancm.test.jdbcTest;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSONObject;

/**
 * 
 * Title: sqTest Description:sql语句的一些测试 Version:1.0.0
 * 
 * @author pancm
 * @date 2017年10月19日
 */
public class sqTest {
	private static Logger logger = LoggerFactory.getLogger(sqTest.class);
	private static Connection connection = null;
	private final static String jdbc = "org.sqlite.JDBC"; // 连接
	private final static String memory = "jdbc:sqlite::memory:"; // 纯内存模式
	private static Statement stmt = null;

	/** 创建待审数据表 */
	private final static String SQL_CREATE_TB_WAIT2 = "CREATE TABLE MT_VERIFY_WAIT2 ("
			+ "SID INTEGER PRIMARY KEY AUTOINCREMENT,"
			+ //
			"PTCODE CHAR(10) NOT NULL DEFAULT '' COLLATE NOCASE ,"
			+ "ID BIGINT NOT NULL DEFAULT((0)) ,"
			+ "UID INT NOT NULL DEFAULT((0)),"
			+ "FEEFLAG TINYINT NOT NULL DEFAULT((0)),"
			+ "SENDLEVEL TINYINT NOT NULL DEFAULT((0)),"
			+ "USERID VARCHAR(11) NOT NULL DEFAULT '' COLLATE NOCASE,"
			+ "MSGTYPE TINYINT NOT NULL DEFAULT ((0)) ,"
			+ "ACK2 TINYINT NOT NULL DEFAULT ((1)) ,"
			+ "LOCKSTATUS TINYINT NOT NULL DEFAULT ((1)) ,"
			+ "SENDSTATUS TINYINT NOT NULL DEFAULT((0)),"
			+ "ASSIGNSTATUS TINYINT NOT NULL DEFAULT ((0)) ,"
			+ "ASSIGNTIME DATETIME NOT NULL DEFAULT (datetime(CURRENT_TIMESTAMP,'localtime')) ,"
			+ "CHECKUSER VARCHAR(128) NOT NULL DEFAULT '' COLLATE NOCASE,"
			+ "MINID BIGINT NOT NULL DEFAULT((0)),"
			+ "MAXID BIGINT NOT NULL DEFAULT((0)),"
			+ "PHONECOUNT INT NOT NULL DEFAULT((0)),"
			+ "RECCOUNT INT NOT NULL DEFAULT((0)),"
			+ "TOTALCOUNT INT NOT NULL DEFAULT((0)),"
			+ "SENDTIME DATETIME NOT NULL DEFAULT (datetime(CURRENT_TIMESTAMP,'localtime')),"
			+ "MESSAGE NVARCHAR(3000) NOT NULL DEFAULT (datetime(CURRENT_TIMESTAMP,'localtime')) COLLATE NOCASE,"
			+ "OPUSER VARCHAR(32) NOT NULL DEFAULT '' COLLATE NOCASE,"
			+ "LOCKUSER VARCHAR(32) NOT NULL DEFAULT ''COLLATE NOCASE ,"
			+ "VERIFYTIME DATETIME NOT NULL DEFAULT (datetime(CURRENT_TIMESTAMP,'localtime')) ,"
			+ "INWAITTIME DATETIME NOT NULL DEFAULT (datetime(CURRENT_TIMESTAMP,'localtime')) ,"
			+ "INWAIT2TIME DATETIME NOT NULL DEFAULT (datetime(CURRENT_TIMESTAMP,'localtime')) ,"
			+ "VERIFYTIME2 DATETIME NOT NULL DEFAULT (datetime(CURRENT_TIMESTAMP,'localtime')) )";

	/** 添加待审数据 */
	public final static String SQL_INSERT_VERIFY = "INSERT INTO MT_VERIFY_WAIT2(ID,UID,PHONECOUNT,TOTALCOUNT,SENDSTATUS,MSGTYPE,SENDTIME,"
			+ "SENDLEVEL,USERID,MESSAGE,INWAITTIME,INWAIT2TIME,PTCODE,FEEFLAG,ACK2,LOCKSTATUS,ASSIGNSTATUS,ASSIGNTIME,CHECKUSER,MINID,MAXID,RECCOUNT,OPUSER,VERIFYTIME,VERIFYTIME2)"
			+ "VALUES(%d,%d,%d,%d,%d,%d,'%s',%d,'%s','%s','%s','%s','%s',%d,%d,'%s',%d,'%s','%s',%d,%d,%d,'%s','%s','%s')";

	/** 添加新的待审数据  */
	public final static String SQL_INSERTOR_VERIFY = "INSERT INTO MT_VERIFY_WAIT2(ID,UID,PHONECOUNT,TOTALCOUNT,SENDSTATUS,MSGTYPE,SENDTIME,"
			+ "SENDLEVEL,USERID,MESSAGE,INWAITTIME,INWAIT2TIME,PTCODE,FEEFLAG,ACK2,LOCKSTATUS,ASSIGNSTATUS,ASSIGNTIME,CHECKUSER,MINID,MAXID,RECCOUNT,OPUSER,VERIFYTIME,VERIFYTIME2)"
			+ " SELECT %d,%d,%d,%d,%d,%d,'%s',%d,'%s','%s','%s','%s','%s',%d,%d,'%s',%d,'%s','%s',%d,%d,%d,'%s','%s','%s'  WHERE NOT EXISTS (SELECT 1 FROM MT_VERIFY_WAIT2 WHERE PTCODE='%s' AND ID=%d)";

	/** 添加待审数据 */
	public final static String SQL_INSERTORIGNORE_VERIFY = "INSERT OR IGNORE INTO MT_VERIFY_WAIT2(ID,UID,PHONECOUNT,TOTALCOUNT,SENDSTATUS,MSGTYPE,SENDTIME,"
			+ "SENDLEVEL,USERID,MESSAGE,INWAITTIME,INWAIT2TIME,PTCODE,FEEFLAG,ACK2,LOCKSTATUS,ASSIGNSTATUS,ASSIGNTIME,CHECKUSER,MINID,MAXID,RECCOUNT,OPUSER,VERIFYTIME,VERIFYTIME2)"
			+ "VALUES(%d,%d,%d,%d,%d,%d,'%s',%d,'%s','%s','%s','%s','%s',%d,%d,'%s',%d,'%s','%s',%d,%d,%d,'%s','%s','%s')";

	/** 合并待审审数据 */
	public final static String SQL_MERGE_VERIFY = "UPDATE MT_VERIFY_WAIT2 SET MAXID = %d,PHONECOUNT =%d,TOTALCOUNT = %d,"+
														"RECCOUNT= %d,INWAITTIME='%s',INWAIT2TIME='%s'  WHERE PTCODE='%s' AND ID=%d ";	
	
	
	/** 待审数据索引 */
	public final static String SQL_CREATE_WAIT2_ACK2_IDX = "CREATE INDEX IDX_ACK2_WAIT2 ON MT_VERIFY_WAIT2(ACK2);";
	public final static String SQL_CREATE_WAIT2_LOCKSTATUS_IDX = "CREATE INDEX IDX_LOCKSTATUS_WAIT2 ON MT_VERIFY_WAIT2(LOCKSTATUS);";
	public final static String SQL_CREATE_WAIT2_PTCODE_IDX = "CREATE INDEX IDX_PTCODE_WAIT2 ON MT_VERIFY_WAIT2(PTCODE);";
	public final static String SQL_CREATE_WAIT2_USERID_IDX = "CREATE INDEX IDX_USERID_WAIT2 ON MT_VERIFY_WAIT2(USERID);";
	public final static String SQL_CREATE_WAIT2_ASSIGNSTATUS_IDX = "CREATE INDEX IDX_ASSIGNSTATUS_WAIT2 ON MT_VERIFY_WAIT2(ASSIGNSTATUS);";
	public final static String SQL_CREATE_WAIT2_ID_IDX = "CREATE INDEX IDX_ID_WAIT2 ON MT_VERIFY_WAIT2(ID);";
	
	/***
	 * 静态加载Sqlite驱动类
	 */
	static {
		try {
			Class.forName(jdbc);
		} catch (ClassNotFoundException e) {
			logger.error("静态加载驱动失败！", e);
		}
	}

	public synchronized static Connection getConnection() {
		if (null == connection) {
			try {
				connection = DriverManager.getConnection(memory, null, null);// 不适用用户名和密码
			} catch (SQLException e) {
				logger.error("获取数据库连接失败！", e);
			}
		}
		return connection;
	}

	public synchronized static boolean closeConnetion() {
		try {
			if (null != connection) {
				connection.close();
			}
		} catch (Exception e) {
			logger.error("关闭数据库连接失败！", e);
		}
		return true;
	}

	
	
	public static void main(String[] args) {
		creatTable();	   //创建表
		initialTableIndex(); //创建索引
		long starTime=System.currentTimeMillis(); 
		insertData(2); //插入100万条数据
		long endTime=System.currentTimeMillis(); 
		System.out.println("插入100万条数据总共使用时间为:"+(endTime-starTime)+"毫秒");
		select("MT_VERIFY_WAIT2");
		List list=test(2);//产生2万条数据
		long starTime1=System.currentTimeMillis(); 
		insertIgnore(list);
		select("MT_VERIFY_WAIT2");
//		insertFor(list); //使用for循环插入
//		insertSQL(list); //使用SQL批量执行插入
//		insertIfFor(list); //使用if/else将sql拆开测试
		long endTime1=System.currentTimeMillis(); 
		System.out.println("总共使用时间为:"+(endTime1-starTime1)+"毫秒");
		long starTime2=System.currentTimeMillis(); 
		selectByPtCode("pt100",10);
		long endTime2=System.currentTimeMillis(); 
		System.out.println("总共使用时间为:"+(endTime2-starTime2)+"毫秒");
		select("MT_VERIFY_WAIT2");
	}
	
	
	
	/**
	 * 创建待审数据索引
	 */
	private static  void initialTableIndex() {
		List<String> sqls = new ArrayList<String>();
		sqls.add(SQL_CREATE_WAIT2_PTCODE_IDX);
		sqls.add(SQL_CREATE_WAIT2_LOCKSTATUS_IDX);
		sqls.add(SQL_CREATE_WAIT2_ASSIGNSTATUS_IDX);
		sqls.add(SQL_CREATE_WAIT2_USERID_IDX);
		sqls.add(SQL_CREATE_WAIT2_ACK2_IDX);
		sqls.add(SQL_CREATE_WAIT2_ID_IDX);
		executeBatch(sqls);
		logger.info("MT_VERIFY_WAIT2 索引初始化完成！");
	}
	
	
	/**
	 *  插入数据
	 */
	private static void insertData(int j) {
		String sql="";
		int code=10;
		String pt="";
		try{
			for(int i=1;i<=j;i++){
				code=i*10;
				pt="pt"+code;
			//	(%d,%d,%d,%d,%d,%d,'%s',%d,'%s','%s','%s','%s','%s',%d,%d,'%s',%d,'%s','%s',%d,%d,%d,'%s','%s','%s')"
				sql = String.format(SQL_INSERT_VERIFY, i,
						1, 1, 1, 1, 1, "", 1, "", "", "", "",
						pt, 1, 1, "", 1, "", "", 1, 1, 1,
						"", "", "");
				insertTable(sql);// 插入数据
			}
			System.out.println("插入数据成功!");
		}catch(Exception e){
			System.out.println("插入数据失败!");
			e.printStackTrace();
		}

	}
	
	/**
	 * 模拟测试数据并发送
	 * @param j
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private static List test(int j){
		List list=new ArrayList<>();
		int id=1;
		int code=10;
		String pt="";
		for(int i=1;i<=j;i++){
			JSONObject json=new JSONObject();
			id=i*2;
			code=i*20;
			pt="pt"+code;
			json.put("ID", id);
			json.put("PTCODE", pt);
			list.add(json);
		}
		System.out.println("成功产生:"+list.size()+" 条数据");
		return list;
	}
	
	
	/**
	 * 判断语句是否存在，不存在则新增，否之；
	 * 
	 */
	@SuppressWarnings("rawtypes")
	private static void insertIgnore(List list){
		String sql="";
		for(int i=0,j=list.size();i<j;i++){
			JSONObject json=(JSONObject) list.get(i);
				sql=getInsertSql2(json);
				insertTable(sql);
		}
	}
	
	/**
	 * 判断语句是否存在，不存在则新增，否之；
	 * 
	 */
	@SuppressWarnings("rawtypes")
	private static void insertIfFor(List list){
		String str="select 1 from MT_VERIFY_WAIT2 WHERE PTCODE='%s' and ID=%d";
		String sql="";
		int count=0;
		for(int i=0,j=list.size();i<j;i++){
			JSONObject json=(JSONObject) list.get(i);
			sql=String.format(str, json.getString("PTCODE"),json.getInteger("ID"));
			final boolean fals=selectTable(sql);
			if(!fals){
				sql=getInsertSql1(json);
				insertTable(sql);
				count++;
			}
		}
		System.out.println("总共新增 "+count+" 条数据");
	}
	
	/**
	 * 判断语句是否存在，不存在则新增，否之；
	 * 
	 */
	@SuppressWarnings("rawtypes")
	private static void insertFor(List list){
		insertTable1(list);// 插入数据
	}
	
	/**
	 * 判断语句是否存在，不存在则新增，否之；
	 * 
	 */
	@SuppressWarnings({ "rawtypes" })
	private static void insertSQL(List list){
		String sql="";
		List<String> result=new ArrayList<String>();
		for(int i=0,j=list.size();i<j;i++){
			JSONObject json=(JSONObject) list.get(i);
			sql=getInsertSql(json);
			result.add(sql);
		}
		if(null!=result&&result.size()>0){
			executeBatch(result);
			System.out.println("执行成功！");
		}
	}
	
	/**
	 * 格式化数据
	 * @return
	 */
	private static String getInsertSql1(JSONObject json){
		String sql = String.format(SQL_INSERT_VERIFY, json.getInteger("ID"),
				1, 1, 1, 1, 1, "", 1, "", "", "", "",
				json.getString("PTCODE"), 1, 1, "", 1, "", "", 1, 1, 1,
				"", "", "");
		return sql;
	}
	
	/**
	 * 格式化数据
	 * @return
	 */
	private static String getInsertSql2(JSONObject json){
		String sql = String.format(SQL_MERGE_VERIFY, 2,
				2, 2, 2, "2017-10-20 15:20:30", "2017-10-20 15:20:30", 
				json.getString("PTCODE"),json.getInteger("ID") );
		return sql;
	}
	/**
	 * 格式化数据
	 * @return
	 */
	private static String getInsertSql(JSONObject json){
		String sql = String.format(SQL_INSERTOR_VERIFY, json.getInteger("ID"),
				1, 1, 1, 1, 1, "", 1, "", "", "", "",
				json.getString("PTCODE"), 1, 1, "", 1, "", "", 1, 1, 1,
				"", "", "",json.getString("PTCODE"),json.getInteger("ID"));
		return sql;
	}
	
	/**
	 * 创建表
	 */
	private static void creatTable(){
		String sql="DROP TABLE IF EXISTS MT_VERIFY_WAIT2 ";//存在则删除
		String str=SQL_CREATE_TB_WAIT2;
		try {
			stmt=getConnection().createStatement();
			stmt.executeUpdate(sql);
			stmt.executeUpdate(str);
			System.out.println("成功创建表!");
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 插入数据
	 */
	private static void insertTable(String sql){
		try {
			if(stmt==null||stmt.isClosed()){
				stmt=getConnection().createStatement();
			}
			stmt.executeUpdate(sql);
			
		} catch (SQLException e1) {
			try {
				connection.rollback();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			e1.printStackTrace();
		}finally{
			try {
				stmt.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * 插入数据
	 */
	private static void insertTable1(List list){
		String sql="";
		try {
			if(stmt==null||stmt.isClosed()){
				stmt=getConnection().createStatement();
			}
			for(int i=0,j=list.size();i<j;i++){
					sql = getInsertSql((JSONObject) list.get(i));
					stmt.executeUpdate(sql);
			}
			
		} catch (SQLException e1) {
			try {
				connection.rollback();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			e1.printStackTrace();
		}finally{
			try {
				stmt.close();
				System.out.println("成功插入 数据");
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * 查询表
	 */
	@SuppressWarnings("rawtypes")
	private static boolean selectTable(String sql){
		ResultSet rs=null;
		List list=null;
		try {
			if(stmt==null||stmt.isClosed()){
				stmt=getConnection().createStatement();
			}
			rs=stmt.executeQuery(sql);
			 list=sqlLiteTest.convertList(rs);
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			try {
				stmt.close();
				rs.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return (list==null||list.size()==0)?false:true;
	}
	
	/***
	 * Sql语句批量执行
	 * @param sqls
	 * @return
	 */
	private static int[] executeBatch(List<String> sqls){
		int[] result;
			try {
				if(stmt==null||stmt.isClosed()){
					stmt=getConnection().createStatement();
				}
				for (int i = 0,j=sqls.size(); i < j; i++) { 
					stmt.addBatch(sqls.get(i));					
				}  
				result = stmt.executeBatch();
				return result;
			} catch (Exception e) {
				logger.error("Sql语句批量执行失败",e);
			}finally{
				if(stmt != null){
					try{
						stmt.close();
					}catch(Exception e){
						logger.error("Sql语句批量执行失败",e);
					}
					stmt = null;
				}
			} 		
		return null;		
	}
	
	private static void select(String tableName){
		String str="select * from "+tableName+";";
		ResultSet rs=null;
		try {
			if(stmt==null||stmt.isClosed()){
				stmt=getConnection().createStatement();
			}
			rs=stmt.executeQuery(str);
		   List list = sqlLiteTest.convertList(rs);
		   System.out.println("数据库总计数据:"+list.size());
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			try {
				stmt.close();
				rs.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * 根据平台编号和ID查询
	 * @param ptcode
	 * @param id
	 */
	private static void selectByPtCode(String ptcode,int id){
		String str="select 1 from MT_VERIFY_WAIT2 WHERE PTCODE='%s' and ID=%d;";
		String sql=String.format(str, ptcode,id);
		ResultSet rs=null;
		try {
			if(stmt==null||stmt.isClosed()){
				stmt=getConnection().createStatement();
			}
			rs=stmt.executeQuery(sql);
		   List list = sqlLiteTest.convertList(rs);
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			try {
				stmt.close();
				rs.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
}
