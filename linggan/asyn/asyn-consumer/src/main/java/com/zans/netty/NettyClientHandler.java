
package com.zans.netty;

import com.zans.vo.AsynReqVo;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.EventLoop;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.util.ReferenceCountUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.zans.config.AsynConstants.PRIORITY_QUEUE;
import static com.zans.config.AsynConstants.STATE_HEARTBEAT;

/**
 * @author pancm
 * @Title: NettyClientHandler
 * @Description: 客户端业务逻辑实现
 * @Version:1.0.0
 * @date 2017年10月8日
 */


public class NettyClientHandler extends ChannelInboundHandlerAdapter {

    private Logger log = LoggerFactory.getLogger(this.getClass());

    /**
     * 循环次数
     */
    private int fount = 0;

    /**
     * 建立连接时
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) {

        AsynReqVo.AsynReqVoMsg build = AsynReqVo.AsynReqVoMsg.newBuilder()
                .setName(AsynConsumerClient.name)
                .setState(STATE_HEARTBEAT)
                .build();
        ctx.channel().writeAndFlush(build);
        ctx.fireChannelActive();
        log.info("与服务端建立连接成功！本客户端唯一编号:{}", AsynConsumerClient.name);
    }

    /**
     * 关闭连接时
     */
    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        log.warn("服务端关闭了连接！");
        final EventLoop eventLoop = ctx.channel().eventLoop();
        AsynConsumerClient nettyClient = new AsynConsumerClient(AsynConsumerClient.host, AsynConsumerClient.port, AsynConsumerClient.name);
        nettyClient.doConnect(new Bootstrap(), eventLoop);
        super.channelInactive(ctx);
    }

    /**
     * 心跳请求处理 每4秒发送一次心跳请求;
     */
    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object obj) {
        if (obj instanceof IdleStateEvent) {
            IdleStateEvent event = (IdleStateEvent) obj;
            // 如果写通道处于空闲状态,就发送心跳命令
            if (IdleState.WRITER_IDLE.equals(event.state())) {
                AsynReqVo.AsynReqVoMsg build = AsynReqVo.AsynReqVoMsg.newBuilder()
                        .setName(AsynConsumerClient.name)
                        .setState(STATE_HEARTBEAT)
                        .build();
                ctx.channel().writeAndFlush(build);
                if (fount % 100 == 0) {
                    log.info("编号:{},已完成心跳发送...", AsynConsumerClient.name);
                }
                fount++;
            }
        }
    }

    /**
     * 业务逻辑处理
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        if(!(msg instanceof AsynReqVo.AsynReqVoMsg)){
            log.warn("未知数据:{}!",msg);
            return;
        }
        try {
            AsynReqVo.AsynReqVoMsg reqVoMsg =(AsynReqVo.AsynReqVoMsg)msg;
            log.info("收到服务端发来的消息:{}", reqVoMsg);
            PRIORITY_QUEUE.add(reqVoMsg);
        } catch (Exception e) {
            log.error("编号:{},消息:{},处理服务端消息出现了异常!", AsynConsumerClient.name, msg, e);
        } finally {
            ReferenceCountUtil.release(msg);
        }
    }


}

