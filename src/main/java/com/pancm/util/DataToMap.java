package com.pancm.util;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author pancm
 * @Title: ec_join_store
 * @Description: 网关数据的topic数据转map的工具类
 * @Version:1.0.0
 * @Since:jdk1.8
 * @date 2019/5/24
 */
public class DataToMap {


    /*字段严格按照顺序，只可在末尾添加字段*/
    private static final String[] mt_rvok = {"ABANDON","USERID","USERUID","LOGINUID","DESTUID",
            "ECID","PREGATENO","LOCALGATENO","NEXTGATENO","SRCGATENO",
            "NEXTGATENOTYPE","PTMSGID","USERMSGID","SUPPMSGID","RECVMTTIME",
            "GATEIDBIND","SPGATEBIND","CPNOBIND","CPNO","ORDERCPNO",
            "SVRTYPE","PHONE","MOBILEAREA","MOBILETYPE","FEEFLAG",
            "RETFLAG","PASSTHROUGH","JTYPE","SENDLEVEL","SENDSTATUS",
            "SENDRESULT","RESENDCNT","TPUDHI","TPPID","PKTOTAL",
            "PKNUMBER","MSGFMT","LONGMSGSEQ","MESSAGE","MOBILECOUNTRY",
            "NETWORKCODE","NETWORKID","CHARGETYPE","PRICE","SMSTYPE",
            "JPTCODE","PTCODE","DTTYPE","CUSTID","USEREXDATA",
            "USERSVRTYPE","SEQID","AGENTLOGINUID","PROTYPE","RCHGTYPE",
            "MSGSRCIP","TMPLID","SPTMPLID","MSGTYPE","ACCTTYPE",
            "PTRCHGID","CHARGOBJ","SIGNATURE"};
    //,"ECPRIVILEGE","BINDTYPE","GTGROUPID","CHGRADE","VALIDTM"};


    /*mt_sdok网关生成报文顺序
     *字段严格按照顺序，只可在末尾添加字段*/
    private static final String[] mt_sdok = {"ABANDON","USERID","USERUID","PTMSGID","SPMSGID",
            "USERMSGID","SUPPMSGID","PREGATENO","LOCALGATENO","NEXTGATENO",
            "SRCGATENO","NEXTGATETYPE","SENDSTATUS","SENDRESULT","SENDERRCODE",
            "GATEIDSEND","SPGATESEND","SPNUMBER","SPID","PHONE",
            "MOBILEAREA","MOBILETYPE","PACKNUM","PACKPOS","SENDLEVEL",
            "NETERRCNT","ERRRESENDCNT","PASSTHROUGH","TPUDHI","TPPID",
            "MSGFMT","PKTOTAL","PKNUMBER","LONGMSGSEQ","TRANSMTTIME",
            "MTSUBMITTIME","SENDTIME","PRETRANSMTTM","MSG","SEQID",
            "IPADDR","VFY_INWAITTM","VFY_DISPALYTM","VFY_OPTOKTM","VFY_RDTM",
            "MOBILECOUNTRY","DTTYPE","ECID"};
    //,"ECPRIVILEGE"};


    /*rpt_rvok网关生成报文顺序
     *字段严格按照顺序，只可在末尾添加字段*/
    private static final String[] rpt_rvok = {"ABANDON","USERID","USERUID","ORGUID","PTMSGID",
            "SPMSGID","SUPPMSGID","USERMSGID","PREGATENO","LOCALGATENO",
            "NEXTGATENO","SRCGATENO","GATEID","SPGATE","SPNUMBER",
            "EXNO","PHONE","ERRORCODE","MOBILEAREA","MOBILETYPE",
            "SENDSTATUS","SUBMITDATE","DONEDATE","RECVTIME","TRANSRPTTIME",
            "MTSENDTIME","SUBMITTIME","DONETIME","JPTCODE","CUSTID",
            "USEREXDATA","PKNUM","PKTOTAL","ENDTRANSRPTTM","AGENTLOGINUID",
            "MOBILECOUNTRY","DTTYPE","ERRORCODE2","RPTEXFLAG","SENDFLAG","ECID"};
    //,"ECPRIVILEGE"};


    /*rpt_sdok网关生成报文顺序
     *字段严格按照顺序，只可在末尾添加字段*/
    private static final String[] rpt_sdok = {"ABANDON","USERID","USERUID","PTMSGID","PREGATENO",
            "LOCALGATENO","NEXTGATENO","SRCGATENO","SPNUMBER","PHONE",
            "ERRORCODE","SENDSTATUS","SENDFLAG","SENDRPTTIME","PUSHRPTTIME",
            "JPTCODE","AGENTLOGINUID","DESTIPPORT","SUPPSNDCNT","MOBILEAREA",
            "MOBILECOUNTRY","MOBILETYPE","DTTYPE","MSGFLAG","ECID"};
    //,"ECPRIVILEGE"};


    /*补发表rpt_suppok网关生成报文顺序
     *字段严格按照顺序，只可在末尾添加字段*/
    private static final String[] rpt_suppok = {"ABANDON","PTMSGID","ERRORCODE","SENDFLAG","SUPPSNDCNT","ECID"};
    //,"ECPRIVILEGE"};


    /*富信表网关生成报文顺序
     *字段严格按照顺序，只可在末尾添加字段*/
    private static final String[] fuxin = {"ABANDON","USERID","USERUID","PTMSGID","USERMSGID",
            "LOCALGATENO","NEXTGATENO","SPNUMBER","PHONE","ERRORCODE",
            "MOBILEAREA","MOBILETYPE","SENDSTATUS","FIRSTDOWNTM","ENDDOWNTM",
            "RDNRPTOKTM","RDNTRANSRPTTM","RECVRDNRPTTM","MOBILECOUNTRY","ERRORCODE2",
            "RPTEXFLAG","SENDFLAG","ECID"};
    //,"ECPRIVILEGE"};


    /* 上行短信网关生成报文顺序
     *字段严格按照顺序，只可在末尾添加字段*/
    /** 2019-8-13 按照系统一部刘欢 说的进更改 */
    private static final String[] mo_rvok =
            {"ABANDON","PTCODE","USERID","ISTRANSIT","USERUID",
            "ECID","ORGUID","PTMSGID","PREGATENO","LOCALGATENO",
            "NEXTGATENO","SRCGATENO","GATEID","SPGATE","SPNUMBER",
            "EXNO","PHONE","MOBILECOUNTRY","MOBILEAREA","MOBILETYPE",
            "SENDLEVEL","SENDSTATUS","PASSTHROUGH","TPUDHI", "TPPID",
             "PKTOTAL","PKNUMBER","LONGMSGSEQ", "MSGFMT","MESSAGE",
             "DELIVERTIME","RECVTIME", "TRANSMOTIME","ENDTRANSMOTM"
//            ,"DTFLAG"
                    ,"DTTYPE","ECPRIVILEGE","RECVWGNO","SIMUID","IMID"
    };

    /** mo_rvok排除的字段 */
    private static final String[] mo_rvok_exclude={"DTFLAG","ECPRIVILEGE","RECVWGNO","SIMUID","SENDSTATUS","IMID"};


    /* 上行短信网关状态报告生成报文顺序
     *字段严格按照顺序，只可在末尾添加字段*/
    private static final String[] mo_sdok = {"ABANDON",
            "PTCODE","USERID","USERUID","ECID","PTMSGID",
            "PREGATENO", "LOCALGATENO","NEXTGATENO","SRCGATENO","SPNUMBER",
            "PHONE", "SENDSTATUS","RESENDCNT","SENDMOTIME","PUSHMOTIME",
            "AGENTLOGINUID", "DESTIPPORT","MOBILEAREA","MOBILECOUNTRY","MOBILETYPE",
            "DTTYPE","ECPRIVILEGE"
    };

    /** mo_skok排除的字段 */
    private static final String[] mo_sdok_exclude={
            "PTCODE","USERID","USERUID","ECID","PREGATENO",
             "LOCALGATENO","NEXTGATENO","SRCGATENO","SPNUMBER","PHONE",
            "MOBILEAREA","MOBILECOUNTRY","MOBILETYPE","DTTYPE", "ECPRIVILEGE"};

    /** mo_skok包含的字段 */
    private static final String[] mo_sdok_include={
            "PTMSGID",
            "SENDSTATUS","RESENDCNT","SENDMOTIME","AGENTLOGINUID","PUSHMOTIME","DESTIPPORT"};


    /**
     * 解析mt_rvok_msg报文
     * @param msg
     * @return
     */
    public static Map<String, Object> analysisMt_rvok_msg(String msg){
        return getStringObjectMap(msg, mt_rvok);
    }


    /**
     * 解析mt_sdok_msg报文
     * @param msg
     * @return
     */
    public static Map<String, Object> analysisMt_sdok_msg(String msg){
        return getStringObjectMap(msg, mt_sdok);
    }
    /**
     * 解析rpt_rvok_msg报文
     * @param msg
     * @return
     */
    public static Map<String, Object> analysisRpt_rvok_msg(String msg){
        return getStringObjectMap(msg, rpt_rvok);
    }
    /**
     * 解析rpt_sdok_msg报文
     * @param msg
     * @return
     */
    public static Map<String, Object> analysisRpt_sdok_msg(String msg){
        return getStringObjectMap(msg, rpt_sdok);
    }


    /**
     * 解析rpt_suppok_msg报文
     * @param msg
     * @return
     */
    public static Map<String, Object> analysisRpt_suppok_msg(String msg){
        return getStringObjectMap(msg, rpt_suppok);
    }
    /**
     * 解析fuxin_msg报文
     * @param msg
     * @return
     */
    public static Map<String, Object> analysisFuxin_msg(String msg){
        return getStringObjectMap(msg, fuxin);
    }


    public static Map<String, Object> analysisMo_rvok_msg(String msg){
        return getStringObjectMap(msg, mo_rvok);
    }

    public static Map<String, Object> analysisFilterMo_rvok_msg(String msg){
        return getFilterObjectMap(getStringObjectMap(msg, mo_rvok),null,mo_rvok_exclude);
    }

    public static Map<String, Object> analysisMo_sdok_msg(String msg){
        return getFilterObjectMap(getStringObjectMap(msg, mo_sdok),null,mo_sdok_exclude);
    }



    private static Map<String, Object> getStringObjectMap(String msg, String[] strings) {
        if(null==msg||msg.length()<=0){
            return null;
        }
        Map<String, Object> map = new HashMap<String, Object>();
        String[] temp = msg.split(",");
        for(int index = 1; index< strings.length&&index<temp.length; index++){
            map.put(strings[index].toLowerCase(), temp[index]);
        }
        return map;
    }




    /**
     * @Author pancm
     * @Description 对map数据进行包含或排除
     * @Date  2019/8/9
     * @Param [map, include, exclude]
     * @return java.util.Map<java.lang.String,java.lang.Object>
     **/
    private static  Map<String, Object> getFilterObjectMap(Map<String, Object> map,String[] include,String[] exclude){
        //如果是包含的话
        if(include!=null&&include.length>0){
            Map<String, Object> map2 = new HashMap<>();
            for(int i=0;i<include.length;i++){
                String key = exclude[i].toLowerCase();
                Object value = map.get(key);
                map2.put(key,value);
            }
            return map2;
        }

        //如果是排除的话
        if(exclude!=null&&exclude.length>0){
            for(int i=0;i<exclude.length;i++){
                map.remove(exclude[i].toLowerCase());
            }
            return map;
        }
        return  map;
    }


    public static void main(String[] args) throws SQLException {
        String msg ="\n" +
                "0,FXTTD1,101152,100061,101066,600024,0,5891,0,0,0,-8626946899980582883,0,0,2019-08-16 17:50:58.309,9062164," +
                "20190625112,1001,123,,,18635552100,31,1,2,1,0,0,2,2,1,0,0,0,1,1,8,0,ADgAMAAwADAAMwA1ADgANQA1,86,0,0,0,0.000000,2," +
                "3020547,46,1,MjAwMDM=,ZXhkYXRh,MTEx,28615,100061,11,1,192.169.2.175:46293,800035855,8989898989,14,4,0,0,3,0,0,0,24," +
                "-8626875702614163453,0.000000,0,,-8626875702614163453,0,1,3932160,546,546," +
                "eyJmbWN0cyI6IjEiLCJmbWluZm9zIjpbeyJmbW5vIjoiMCIsImlzcGFyYSI6IjEiLCJ0eHQiOnsicDEiOiJhYmMiLCJwMiI6ImVkZiJ9LCJ0eXBlIjoiMCJ9XX0=,0,0,,0,-8626946899980582883";

        Map<String,Object> map = analysisMt_rvok_msg(msg);


    }
}
