package com.zans.netty;

import com.zans.config.AsynConstants;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoop;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

/**
 *
 * @Title: NettyClient
 * @Description: Netty客户端
 * @Version:1.0.0
 * @author pancm
 * @date 2017年10月8日
 */

public class AsynConsumerClient {
	private Logger log = LoggerFactory.getLogger(this.getClass());

	public static String host;

	public static int port;

	public static String name;


	public AsynConsumerClient(String host, int port, String serverName){
		this.host = host;
		this.port = port;
		this.name =  serverName;
	}

	private NettyClientFilter nettyClientFilter = new NettyClientFilter();
	// 通过nio方式来接收连接和处理连接
	private EventLoopGroup group = new NioEventLoopGroup();
	/**唯一标记 */
	private boolean initFlag =true;

	/**
	 * Netty创建全部都是实现自AbstractBootstrap。 客户端的是Bootstrap，服务端的则是 ServerBootstrap。
	 **/
	public void run() {
		doConnect(new Bootstrap(), group);
	}

	/**
	 * 重连
	 */
	public void doConnect(Bootstrap bootstrap, EventLoopGroup eventLoopGroup) {
		ChannelFuture f = null;
		try {
			if (bootstrap != null) {
				bootstrap.group(eventLoopGroup);
				bootstrap.channel(NioSocketChannel.class);
				bootstrap.option(ChannelOption.SO_KEEPALIVE, true);
				bootstrap.handler(nettyClientFilter);
				bootstrap.remoteAddress(host, port);
				f = bootstrap.connect().addListener((ChannelFuture futureListener) -> {
					final EventLoop eventLoop = futureListener.channel().eventLoop();
					if (!futureListener.isSuccess()) {
						log.info("与服务端断开连接!在10s之后准备尝试重连!");
						eventLoop.schedule(() -> doConnect(new Bootstrap(), eventLoop), 10, TimeUnit.SECONDS);
					}
				});
				if(initFlag){
					log.info("Netty客户端启动成功!");
					initFlag =false;
				}
				AsynConstants.channel = f.channel();
				// 阻塞
				f.channel().closeFuture().sync();
			}
		} catch (Exception e) {
			log.error("客户端连接失败!",e);
		}
	}

	public static void main(String[] args) {
		String host= "127.0.0.1";
		int port= 52314;
		String name= "alert_api";
		AsynConsumerClient nettyClient = new AsynConsumerClient(host,port,name);
		nettyClient.run();
	}
}
