package com.pancm.test.nettyTest.demo4;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;

import java.io.IOException;

/**
 * Title: NettyClientDemo4
 * Description: Netty客户端
 * Version:1.0.0
 *
 * @author pancm
 * @date 2017年9月21日
 */
public class NettyClientDemo4 {
    /**
     * The constant host.
     */
    public static String host = "127.0.0.1";  //ip地址
    /**
     * The constant port.
     */
    public static int port = 4567;			//端口
	    /// 通过nio方式来接收连接和处理连接   
	    private static EventLoopGroup group = new NioEventLoopGroup(); 
	    private static  Bootstrap b = new Bootstrap();
	    private static Channel ch=null;

    /**
     * Netty创建全部都是实现自AbstractBootstrap。
     * 客户端的是Bootstrap，服务端的则是ServerBootstrap。
     *
     * @param args the input arguments
     * @throws InterruptedException the interrupted exception
     * @throws IOException          the io exception
     */
    public static void main(String[] args) throws InterruptedException, IOException {
	        	 b.group(group)  
	             .channel(NioSocketChannel.class)  
	             .option(ChannelOption.TCP_NODELAY,true)  
	             .handler(new ChannelInitializer<SocketChannel>() {  
	                 @Override  
	                 public void initChannel(SocketChannel ch) throws Exception {  
	                     ChannelPipeline p = ch.pipeline();  
	                     p.addLast(new StringDecoder());     //绑定自定义编码器
	                     p.addLast(new NettyClientHandlerDemo4());   //绑定自定义业务 
	                 }  
	             });  
	            // 连接服务端
	            ch = b.connect(host, port).sync().channel();
	            System.out.println("客户端成功启动...");
	            //发送消息
	            star();
	    }

    /**
     * Star.
     *
     * @throws IOException the io exception
     */
    public static void star() throws IOException{
	    	NettySendBody nsb=new NettySendBody();
	    	nsb.put("Netty","Hello");
	    	nsb.put("JAVA","从入门到入土");
	    	nsb.put("SQL","从删库到跑路");
	        ch.writeAndFlush(nsb); 
	    	System.out.println("客户端发送数据:"+nsb.toString());
	   } 
}
