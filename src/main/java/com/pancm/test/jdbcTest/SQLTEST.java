package com.pancm.test.jdbcTest;

import com.pancm.util.*;
import org.apache.storm.command.list;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author pancm
 * @Title: pancm_project
 * @Description:
 * @Version:1.0.0
 * @Since:jdk1.8
 * @date 2019/10/23
 */
public class SQLTEST {


    public static void main(String[] args) {
        try {
            initDb();
            //新增平台
//            test1();
            //新增订阅服务
//            test2();
            //创建表
//            test3();
            //创建存储过程
//            test4();
            //查询数据
//            test5();
            //清除表
//            test6();
            //查询条数
            test7();
            //修改数据
//            test8();
//            test7();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void initDb() {
        int dbNum = Integer.valueOf(GetProperties.getAppSettings().get("sql.num").trim());
        for (int i = 1; i <= dbNum; i++) {
            new ConnectionManager2(i);
        }
    }

    private  static  void test1() throws SQLException {
        String tb="Platform";
         int k =50;
        int ptid= 105;
        for (int i = 1; i <= k; i++) {
            String t="syntest";
            Map<String,Object> map = new HashMap<>();
            t+=i;
            ptid++;
            map.put("ptcode",t);
            map.put("platname",t);
            map.put("svradd","123456");
            map.put("CorpFLag",1);
            map.put("SynFlag",1);
            map.put("IsVerify",1);
            map.put("dt_userid",t);
            map.put("dt_pwd",123456);
            map.put("SubKey","775047B2F2925858");
            map.put("ResType",1);
            map.put("SUBURL","111");
            map.put("ptid",ptid);
            map.put("PLATVALUE","00000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000");
            map.put("datafg",1);
            map.put("SVRTYPE",1);
            map.put("PLATCLASS",4);
            map.put("PLATTYPE",3);
            DBUtil1.insert(tb,map);
            System.out.println("插入数据成功！");
        }
        System.out.println("在表:"+tb+"插入条:"+k+"数据成功！");
    }


    private  static  void test2() throws SQLException {
        String tb="ms_syn_book";
        int k =50;
        for (int i = 3; i <= k; i++) {
            String t="syntest";
            Map<String,Object> map = new HashMap<>();
            t+=i;
            map.put("ptcode",t);
            map.put("SVROBJ","corpadccpnosvr");
            map.put("ISACTIVE",1);
            map.put("SYNTYPE",128);
            map.put("OBJECTFLAG",2);
            map.put("SVRTBNAME","CORP_ADCCPNO_SVR");
            map.put("REMARK",t);
            DBUtil1.insert(tb,map);
            System.out.println("插入数据成功！");
        }
        System.out.println("在表:"+tb+"插入条:"+k+"数据成功！");
    }


    private  static  void test3() throws SQLException {

        int k =50;
        for (int i = 1; i <= k; i++) {
            String tb="CORP_ADCCPNO_SVR";
            tb+=i;
            String sql=" CREATE TABLE [dbo]."+tb +"(\n" +
                    "[ID] int NOT NULL ,\n" +
                    "[Spgate] varchar(21) COLLATE Chinese_PRC_CI_AS NOT NULL ,\n" +
                    "[AdcCpno] varchar(21) COLLATE Chinese_PRC_CI_AS NOT NULL ,\n" +
                    "[Spisuncm] int NOT NULL ,\n" +
                    "[Status] tinyint NOT NULL ,\n" +
                    "[ServiceFlag] tinyint NOT NULL ,\n" +
                    "[Ecid] int NOT NULL DEFAULT ((0)) ,\n" +
                    "[PROTYPE] int NOT NULL DEFAULT ((1)) ,\n" +
                    "[SIGN_ID] varchar(64) COLLATE Chinese_PRC_CI_AS NOT NULL DEFAULT '' ,\n" +
                    "CONSTRAINT [PK_"+tb+"]PRIMARY KEY ([Spgate], [AdcCpno], [Spisuncm])\n" +
                    ")\n" +
                    "ON [PRIMARY]\n" +
                    "GO\n" +
                    "\n" +
                    "CREATE UNIQUE INDEX [IX_"+tb+"_ID] ON [dbo].[CORP_ADCCPNO_SVR19]\n" +
                    "([ID] ASC) \n" +
                    "WITH (IGNORE_DUP_KEY = ON)\n" +
                    "ON [PRIMARY]\n" +
                    "GO";
            System.out.println(sql);

//            DBUtil.executeUpdate(sql);
//            System.out.println("创建表数据成功！");
        }
        System.out.println("创建:"+k+"个表成功！");
    }


    private  static  void test4() throws SQLException {

        int k =50;
        for (int i = 1; i <= k; i++) {
            String tb="CORP_ADCCPNO_SVR";
            String tb2="SYNCHRONOUSCORPADCCPNO";
            tb+=i;
            tb2+=i;
            String sql=" CREATE PROCEDURE [dbo].["+tb2+"]\n" +
                    "\t@OPTYPE\t TINYINT,\n" +
                    "\t@ID INT,\n" +
                    "\t@SPGATE VARCHAR(20),\n" +
                    "\t@ADCCPNO VARCHAR(20),\n" +
                    "\t@SPISUNCM INT,\n" +
                    "\t@STATUS TINYINT,\n" +
                    "\t@SPSTATUS TINYINT,\n" +
                    "\t@SERVICEFLAG TINYINT,\n" +
                    "\t@ECID INT,\n" +
                    "    @PROTYPE          INT=0,  \n" +
                    "    @SIGN_ID      VARCHAR(64)='',\n" +
                    "\t@NEED_CMD_SYN    INT=1\n" +
                    "AS\n" +
                    "BEGIN\n" +
                    "\tSET NOCOUNT ON;\n" +
                    "    IF(@OPTYPE <>2)\n" +
                    "    BEGIN             \n" +
                    "                SET @OPTYPE = 0;      \n" +
                    "\t\t\t\tIF(EXISTS(SELECT TOP 1 ID FROM ["+tb+"] A WITH(NOLOCK)  WHERE A.ID=@ID))\n" +
                    "\t\t\t\t\tSET @OPTYPE = 1;\n" +
                    "\t\t\t\t\n" +
                    "\t\t\t\tIF(@SPSTATUS = 0  AND @STATUS = 0) \n" +
                    "\t\t\t\tBEGIN\n" +
                    "\t\t\t\t\tSET @STATUS = 0;\n" +
                    "\t\t\t\tEND\n" +
                    "\t\t\t\tELSE\n" +
                    "\t\t\t\tBEGIN\n" +
                    "\t\t\t\t\tSET @STATUS = 1;\n" +
                    "\t\t\t\tEND\n" +
                    "\t\t\t\t\n" +
                    "\t\t\t\tIF(@OPTYPE=0)\n" +
                    "\t\t\t\tBEGIN\n" +
                    "\t\t\t\t\t INSERT INTO  [DBO].["+tb+"]([ID],[SPGATE],[ADCCPNO],[SPISUNCM],[STATUS],[SERVICEFLAG],[ECID],[PROTYPE],[SIGN_ID])\n" +
                    "\t\t\t\t\t\t\t\t\t\t\t\t    VALUES(@ID, @SPGATE, @ADCCPNO, @SPISUNCM, @STATUS, @SERVICEFLAG, @ECID, @PROTYPE, @SIGN_ID)\n" +
                    "\t\t\t\t\t IF @@ERROR<>0 OR @@ROWCOUNT=0\n" +
                    "\t\t\t\t\t\tBEGIN  \n" +
                    "\t\t\t\t\t\t\tRAISERROR ('添加ADC子号失败（来自远程）', 16, 1);\t \t\n" +
                    "\t\t\t\t\t\t\tRETURN;\n" +
                    "\t\t\t\t\t\tEND\n" +
                    "\t\t\t\tEND;\n" +
                    "\t\t\t\t ELSE IF(@OPTYPE=1)\n" +
                    "\t\t\t\t BEGIN\n" +
                    "\t\t\t\t\t UPDATE  [DBO].["+tb+"]\n" +
                    "\t\t\t\t\t SET\n" +
                    "\t\t\t\t\t \t[SPGATE]      = @SPGATE,\n" +
                    "\t\t\t\t\t \t[ADCCPNO]     = @ADCCPNO,\n" +
                    "\t\t\t\t\t \t[SPISUNCM]    = @SPISUNCM,\n" +
                    "\t\t\t\t\t \t[STATUS]      = @STATUS,\n" +
                    "\t\t\t\t\t \t[SERVICEFLAG] = @SERVICEFLAG,\n" +
                    "\t\t\t\t\t \t[ECID]        = @ECID,\n" +
                    "\t\t\t\t\t \t[PROTYPE]     = @PROTYPE,\n" +
                    "                        [SIGN_ID]     = @SIGN_ID\n" +
                    "\t\t\t\t\t\t WHERE ID     = @ID\n" +
                    "\t\t\t\t\t\tIF @@ERROR<>0 OR @@ROWCOUNT=0\n" +
                    "\t\t\t\t\t\tBEGIN  \n" +
                    "\t\t\t\t\t\t\tRAISERROR ('更新ADC子号失败（来自远程）', 16, 1);\t \t\n" +
                    "\t\t\t\t\t\t\tRETURN;\n" +
                    "\t\t\t\t\t\tEND\n" +
                    "\t\t\t\t END;\n" +
                    "     END;\n" +
                    " \n" +
                    "     ELSE IF(@OPTYPE=2)\n" +
                    "     BEGIN--{\n" +
                    "          DELETE FROM [DBO].["+tb+"] WHERE ID=@ID;\n" +
                    "          IF @@ERROR<>0\n" +
                    "\t\t\tBEGIN  \n" +
                    "\t\t\t\tRAISERROR ('删除ADC子号失败（来自远程）', 16, 1);\t \n" +
                    "\t\t\t\tRETURN;\t\n" +
                    "\t\t\tEND\n" +
                    "     END;\n" +
                    " \t IF(@NEED_CMD_SYN=1)\n" +
                    "\t BEGIN\n" +
                    "\t\t\t INSERT INTO [DBO].[CMD_TASK]([CMDTYPE],[OPTYPE],[CMDTEXT]) VALUES(8,7,''); \n" +
                    "\t\t\n" +
                    "\t\t\tINSERT INTO [DBO].[CMD_TASK]([CMDTYPE],[OPTYPE],[CMDTEXT],[OPTIME],[OPUSER],[REMARKS])\n" +
                    "\t\t\t\t VALUES(14,7,'',GETDATE(),'','ADC子号,操作类型='+CAST(@OPTYPE AS VARCHAR(10))+',ID='+CAST(@ID AS VARCHAR(20))+',SPGATE='+@SPGATE+',ADCCPNO='+@ADCCPNO+',SPISUNCM='+CAST(@SPISUNCM AS VARCHAR(10))+',STATUS='+CAST(@STATUS AS VARCHAR(10))+',SERVICEFLAG='+CAST(@SERVICEFLAG AS VARCHAR(10))+',ECID='+CAST(@ECID AS VARCHAR(20)));\n" +
                    "\t    END;\n" +
                    "\n" +
                    "     INSERT INTO  [DBO].[CMD_TASK_HIS]([ID],[CMDTYPE],[OPTYPE],[CMDTEXT],[OPTIME],[REMARKS])\n" +
                    "                           SELECT [ID],[CMDTYPE],[OPTYPE],[CMDTEXT],[OPTIME],[REMARKS]\n" +
                    "     FROM  [DBO].[CMD_TASK] WHERE [OPTIME]<(GETDATE()-3);\n" +
                    "     DELETE FROM [DBO].[CMD_TASK] WHERE [OPTIME]<(GETDATE()-3);\n" +
                    "END\n" +
                    "\n" +
                    "GO";
            System.out.println(sql);

//            DBUtil.executeUpdate(sql);
//            System.out.println("创建表数据成功！");
        }
        System.out.println("创建:"+k+"个存储成功！");
    }


    private  static  void test5() throws SQLException {
        int k =50;
        for (int i = 1; i <= k; i++) {
            String tb="CORP_ADCCPNO_SVR";
            tb+=i;
//            String sql="SELECT COUNT(1) AS COUNT FROM "+tb;
            String sql="SELECT COUNT(1) AS COUNT FROM "+tb +" where status=1";
            System.out.println(sql);
        }
    }




    private  static  void test6() throws SQLException {
        int k =50;
        for (int i = 1; i <= k; i++) {
            String tb="CORP_ADCCPNO_SVR";
            tb+=i;
            String sql="TRUNCATE TABLE "+tb;
            System.out.println(sql);
        }
    }


    private  static  void test7() throws SQLException {
        int k =50;
        DBUtil dbUtil = new DBUtil(2);
        for (int i = 1; i <= k; i++) {
            String tb="CORP_ADCCPNO_SVR";
            tb+=i;
//            String sql="SELECT COUNT(1) AS COUNT FROM "+tb;
            String sql="SELECT COUNT(1) AS COUNT FROM "+tb +" ";
             sql="SELECT COUNT(1) AS COUNT FROM "+tb +" where status=0 ";
            List<Map<String,Object>> list=  dbUtil.executeQuery(sql);
            System.out.println(tb+":status = 0 "+list.get(0).get("COUNT"));
             sql="SELECT COUNT(1) AS COUNT FROM "+tb +" where status=1 ";
            List<Map<String,Object>> list2=  dbUtil.executeQuery(sql);
            System.out.println(tb+":status = 1 "+list2.get(0).get("COUNT"));
        }
    }


    private  static  void test8() throws SQLException {
        int k =10;
        DBUtil dbUtil = new DBUtil(2);
        for (int i = 1; i <= k; i++) {
            String tb="CORP_ADCCPNO_SVR";
            tb+=i;
//            String sql="SELECT COUNT(1) AS COUNT FROM "+tb;
            String sql="update "+tb +" set status = 1 ";
            dbUtil.executeUpdate(sql);
        }
    }
}
