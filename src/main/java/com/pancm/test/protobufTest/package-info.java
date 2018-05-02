/**
 * 
 */
/**
 * @Title: package-info
 * @Description: 
 * 谷歌的protobuf传输协议测试
 * Protocol Buffers（简称protobuf）是谷歌的一项技术，用于将结构化的数据序列化、反序列化，经常用于网络传输。
 * 类似JSON、xml 性能方面较高，但是可阅读性较差，因为protobuf生成的是字节码。
 * 使用需要protobuf-java-x.x.x.jar和protoc.exe。
 * protoc.exe 生产java代码的工具，需要使用命令生产代码。
 * 例如，新建一个后缀名为.proto的类
 * 内容为:
  
 
 message Person {  
      
    // ID（必需）  
    required int32 id = 1;  
      
    // 姓名（必需）  
    required string name = 2;  
      
    // email（可选）  
    optional string email = 3;  
  
    // 朋友（集合）  
    repeated string friends = 4;  
}  
 
 
  
 * 然后再打开dos界面，到在同级目录输入：protoc.exe --java_out=E:\proto PersonMsg.proto
 * 就会生成文件PersonMsg.java
 * @Version:1.0.0  
 * @author pancm
 * @date 2018年5月2日
 */
package com.pancm.test.protobufTest;