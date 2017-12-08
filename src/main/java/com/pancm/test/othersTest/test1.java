/**
 * 
 */
package com.pancm.test.othersTest;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;

/**
 * @author lurenjia
 *
 */
public class test1 {

	/**
	 * @param args
	 */
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
         Map aa=new HashMap();
         Map<String,Object> aa1=new HashMap<String, Object>();
         List <Object> ls=new ArrayList <Object>();
         aa.put("aa", "11");
         aa.put("bb", "22");
         aa.put("CC", "33");
         aa.put("dD", "Ac");
         aa.put("dD", "Ac");
         ls.add(aa);
        // aa.put(11, "dd");
       
         aa1.put("ee", "fd");
         aa1.put("LB", ls);
         JSONObject jo=(JSONObject) JSON.toJSON(aa1);
         String et=aa.toString();
         String et1=JSON.toJSONString(aa);
         JSONObject jo1= (JSONObject) JSON.toJSON(aa1);
         Object lk=JSON.toJSON(aa1);
        /* instanceof运算符用法  
         * 运算符是双目运算符,左面的操作元是一个对象,右面是一个类.当  
         * 左面的对象是右面的类创建的对象时,该运算符运算的结果是true,否则是false */
         System.out.println(lk instanceof JSONObject);
         System.out.println(lk instanceof Object);
         System.out.println(lk instanceof String);
         System.out.println(lk instanceof Integer);
         System.out.println("jo:"+jo+",jo1"+jo1+",et:"+et+",et1:"+et1);
         JSONArray ja1=jo.getJSONArray("LB");
         System.out.println("ja:"+ja1);
         String a1=aa.toString().toUpperCase(); //所有的值改为大写
         System.out.println("a1:"+JSON.toJSON(a1));
         //改变map中的key大小写 up改成大写, low 改成小写 否则不改变
         JSONObject dd =changeUPorLOW(aa,"all","low");
        //改变map中list节点名的大小写
         Map qwe=new HashMap();
         for(String fg:aa1.keySet()){
        	 qwe.put(fg.toLowerCase(), aa1.get(fg));
        	 System.out.println("aa1:"+aa1+",qwe:"+qwe);
    	 
         }
         
         String str="abc";
         String str2= str.concat("123");//将参数连接到字符串的末尾
         int asc= str.compareTo(str2);    //比较前后两个字符串的asc码的差值
         System.out.println("str:"+str+",str2:"+str2+",asc:"+asc);
       
         //判断长度 成立返回true(第一个) 失败返回false(第二个)
         System.out.println("str长度:"+str.length()+",str2长度:"+str2.length());
         System.out.println((str.length()>str2.length())?true:false);
         System.out.println((str2.length()<str.length())?str:str2); //字段进行判断 返回值大的数据
         System.out.println("返回大的数据:"+bj(3,4)+",返回大的数据1:"+bj1(3,4));
         
         String q3=" abc ";//去空格
         System.out.println(q3.length()+"," +q3.trim().length());
         
         String q4="abcde";//判断开头和结尾数据
         System.out.println(q4.startsWith("a")+","+q4.startsWith("b", 1)+","+q4.endsWith("e"));
         System.out.println(q4.startsWith("b")+","+q4.startsWith("bc", 1)+","+q4.startsWith("c", 1)+","+q4.endsWith("d"));
         split(q4,"c");
         System.out.println("分割数据:"+ split(q4,"c"));
         DecimalFormat    df   = new DecimalFormat("#.00"); //定义格式
         double db=8.3400;
         String dbl= df.format(db);//你要格式化的数字
         System.out.println("格式化后的数据:"+dbl);
	 	 //8.34
         
         
      // 初始化list
         List<String> listqw = new ArrayList<String>();
         listqw.add("Jhon");
         listqw.add("Jency");
         listqw.add("Mike");
         listqw.add("Dmitri");
         listqw.add("Mike");
         // 利用set不允许元素重复的性质去掉相同的元素
         Set<String> checkDuplicates = new HashSet<String>();
         for (int i = 0; i < listqw.size(); i++) {
             String items = listqw.get(i);
             System.out.println("items:"+items);
             if (!checkDuplicates.add(items)) {
                 // 打印重复的元素
                 System.out.println("重复的数据: " + items);
             }
         }
         System.out.println("listqw:"+listqw);
         
         Map<String,Object> qq1=new HashMap<String, Object>();
         Map<String,Object> qq2=new HashMap<String, Object>();
         HashMap<String,Object> qq3=new HashMap<String, Object>();
         List  lq=new ArrayList ();
         qq1.put("qw1", "1");
         qq1.put("qw2", "2");
         qq2.put("qw1", "1");
         qq2.put("qw2", "1");
         lq.add(qq1);
         lq.add(qq2);
         System.out.println("Map qq1:"+qq1+",Map qq2:"+qq2+",List lq:"+lq);
      
         
         HashMap map=new HashMap();
         map.put("key1", "value1");
         map.put("key2", "value2");
         map.put("key3", "value3");
         TreeMap tm = new TreeMap();
         tm.putAll(map);
         System.out.println("Hash码排序:"+map.toString());//不按顺序 hashmap 按照Hash码来排序
         System.out.println("按照排序输出:"+tm.toString()); // 按顺序输出
         
         
         String sd = "abcde";
         byte dst[] = new byte[5]; //定义字节
         try {
         dst = sd.getBytes("GB2312"); 
         } catch (Exception e) {
         e.printStackTrace();
         }
         System.out.println("dst长度:"+dst.length);
         for (int i = 0; i < dst.length; i++) {//循环这个数据
        	 System.out.print(i==4?dst[i]+";":dst[i]+",");
        	 }
        	
         
         List<String> list=new ArrayList<String>();
         list.add("a");
         list.add("b");
         list.add("c");
          for (String strb : list) {// 循环list 并定义一个String接受
              System.out.println(strb);
          }
          
          int[] arr = new int[3];
          arr[0] = 1;
          arr[1] =2;
          arr[2] = 3;
          for(int i : arr){
               System.out.println(i);
          }
         
         List l = new ArrayList();
         l.add("aa");
         l.add("bb");
         l.add("cc");
         //迭代器用于for循环
         for (Iterator iter = l.iterator(); iter.hasNext();) {
          String stra = (String)iter.next();
          System.out.println("for "+stra);
         }
         //迭代器用于while循环
         Iterator iter = l.iterator();
         while(iter.hasNext()){
          String stra = (String) iter.next();
          System.out.println("while "+stra);
         }
  
         String ss= JSONUtil.string2json(aa.toString());
        
         JSONArray ja=  JSONUtil.getJSONArray(aa);
         int []d={1,2,3,4};
        int s= sind(d,1,4,2);
         System.out.println(aa+" "+ss+"  "+ja+" "+s + " "+dd);
        
	}
	
	 //判断是否一致 折半查询 索引 (数组，查找的下标,数组长度,数组下标的结果)
	public static   int sind(int []ary,int index,int len,int value)
    {
        if(len==1)//最后一个元素
        {     System.out.println("ary[index]:"+ary[index]+"  value:"+value);
            if (ary[index]==value)
			 {
				return index;//成功查询返回索引
			}
            return -1;//失败，返回-1
        }
        //如果长度大于1，进行折半递归查询
        int half=len/2;
        //检查被查值是否大于上半部分最后一个值，如果是则递归查询后半部分
        if(value>ary[index+half-1]) {
			return sind(ary,index+half,half,value);
		}
        //否则递归查询上半部分
        return sind(ary,index,half,value);
    }
	
	@SuppressWarnings("unchecked")
	public static Map<String,Object> json2Map(String json){
	return JSON.parseObject(json, Map.class);
	}
	
	public static JSONObject toJO(String json) {
		if (json == null || json.equals("")) {
			return new JSONObject();
		}
		if (!(json.startsWith("{") && json.endsWith("}"))) {
		//	logger.debug(json);
			throw new RuntimeException("NOT JSONObject string");
		}
		return toJsonObject(json);
	}
	
	public static JSONObject toJO(Object object) {
		if (object == null || object.equals("")) {
			return new JSONObject();
		}
		if (object instanceof JSONObject) {
			return (JSONObject) object;
		}
		return toJsonObject(object);
	}

	
	@SuppressWarnings("unchecked")
	public static <T> T toJsonObject(Object object) {
		if (object instanceof String) {
			return JSON.parseObject((String) object, new TypeReference<T>() {
			});
		} else {
			return (T) JSON.toJSON(object);
		}
	}
	public static <T> T toJsonObject(String json) {
		return JSON.parseObject(json, new TypeReference<T>() {
		});
	}
	
	public static int bj(int i,int j){	
		return ((i>j)?i:j);	
	}
	
	public static int bj1(int i,int j){	
		if(i>j) {
			return i;
		}
		return j;	
	}
	
	 //改变json格式中的大小写
		@SuppressWarnings("rawtypes")
		public static JSONObject changeUPorLOW(Map aa,String format,String lx) {
			 JSONObject cc=new JSONObject();
				 Set keys = aa.entrySet();  //创建set映射
		         if(keys!=null){
		          Iterator it=keys.iterator(); //定义迭代器
		          while(it.hasNext()){ //判断元素是否存在 若存在 则继续迭代
		        	  Map.Entry  er= (Entry) it.next(); //返回迭代的下一个元素
		        	  String k =null;
		        	  String v=null;  
		        	if("key".equals(format)){//如果是改变json的key的大小写
		        		 if("up".equals(lx)){//如果是改成大写
			        		  k =((String) er.getKey()).toUpperCase();
			        		  v=(String) er.getValue();//获取map中的value值
			        	  }else if("low".equals(lx)){//如果是改成小写
			        		  k =((String) er.getKey()).toLowerCase();
			        		  v=(String) er.getValue();//获取map中的value值
			        	  }else{//否则就不改变
			        		  k =(String) er.getKey();//获取map中的key值
			        		  v=(String) er.getValue();//获取map中的value值
			        	  }
		        	}else if("value".equals(format)) {//如果是改变json中的value的大小写
		        		 if("up".equals(lx)){
			        		  k =(String) er.getKey();
			        		  v=((String) er.getValue()).toUpperCase();//获取map中的value值
			        	  }else if("low".equals(lx)){
			        		  k =(String) er.getKey();
			        		  v=((String) er.getValue()).toUpperCase();//获取map中的value值
			        	  }else{
			        		  k =(String) er.getKey();
			        		  v=(String) er.getValue();//获取map中的value值
			        	  }
		        	}else if("all".equals(format)){//如果是key和value改变大小写
		        		 if("up".equals(lx)){
			        		  k =((String) er.getKey()).toUpperCase();
			        		  v=((String) er.getValue()).toUpperCase();//获取map中的value值
			        	  }else if("low".equals(lx)){
			        		  k =((String) er.getKey()).toLowerCase();
			        		  v=((String) er.getValue()).toLowerCase();//获取map中的value值
			        	  }else{
			        		  k =(String) er.getKey();
			        		  v=(String) er.getValue();//获取map中的value值
			        	  }
		        	}else{//否则就不做改变
		        		 k =(String) er.getKey(); //获取map中的key值
		        		 v=(String) er.getValue();//获取map中的value值
		        	}
		        	  cc.put(k, v);
		        	  System.out.println("返回的参数:"+cc);
		          }
		         }
			return cc;
		}

		//分割数据
		public static String split(String demo,String lx){
			String[] array = demo.split(lx);
			int len = array.length;
			String aa="";
			System.out.print("\"" + demo + "\" 分割后的长度为：" + len);
			if(len >= 0)
			{
				System.out.print(",分割后的结果为：");
				for(int i=0; i<len; i++)
				{
					System.out.print(" \""+array[i]+"\"");
					aa+=array[i];
					System.out.print(" "+aa+"  ");
				}			
			}
	
			return aa;
		}
		
}
