package com.zans.netty;

import com.zans.task.ManageTask;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

/**
 *
 * @Title: NettyServer
 * @Description: Netty服务端
 * @Version:1.0.0
 * @author pancm
 * @date 2017年10月8日
 */
@Service("nettyServer")
@Slf4j
public class NettyServer {
	@Value("${spring.netty.port:52314}")
	private  int port = 52314;
	@Value("${spring.netty.boss_threads:1}")
	private  int bossThreads ;

	@Value("${spring.netty.worker_threads:5}")
	private  int workerThreads;

	private  EventLoopGroup boss = new NioEventLoopGroup(bossThreads); // 通过nio方式来接收连接和处理连接
	private  EventLoopGroup work = new NioEventLoopGroup(workerThreads); // 通过nio方式来接收连接和处理连接
	private  ServerBootstrap b = new ServerBootstrap();



	@Autowired
	private NettyServerFilter nettyServerFilter;


	@Async("taskExecutor")
	public void nettyServerRun() {
		try {
			b.group(boss, work);
			b.channel(NioServerSocketChannel.class);
			b.childHandler(nettyServerFilter); // 设置过滤器
			// 服务器绑定端口监听
			ChannelFuture f = b.bind(port).sync();
			log.info("Netty服务端启动成功,端口是:" + port);
			new ManageTask().start();
			// 监听服务器关闭监听
			f.channel().closeFuture().sync();
		} catch (InterruptedException e) {
			log.error("netty服务端启动失败!",e);
		} finally {
			// 关闭EventLoopGroup，释放掉所有资源包括创建的线程
			work.shutdownGracefully();
			boss.shutdownGracefully();
		}
	}

}
