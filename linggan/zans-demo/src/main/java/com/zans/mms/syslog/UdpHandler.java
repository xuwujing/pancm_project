package com.zans.mms.syslog;

/**
 * @author xv
 * @since 2022/6/29 9:17
 */
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;


@Slf4j
@Service
public class UdpHandler extends ChannelInboundHandlerAdapter {

    @Value("${syslog.filter.keyword:Interface GigabitEthernet0/0/}")
    private String filterKeyword;

    @Value("${syslog.filter.ip:127.0.0.1}")
    private String filterIp;

    @Autowired
    private ISyslogProcessor syslogProcessor;

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        //得到消息后，可根据消息类型分发给不同的service去处理数据
        SyslogEntity syslogEntity = (SyslogEntity)msg;
        try {

            syslogProcessor.processMessage(syslogEntity);
        }catch (Exception e){
            log.error("syslog业务处理失败!syslogEntity:{}",syslogEntity,e);
        }
//        ctx.writeAndFlush("hello word"); //返回数据给UDP Client
    }

    @Override
    public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
        ctx.fireChannelRegistered();
    }

    @Override
    public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
        ctx.fireChannelUnregistered();
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        ctx.fireChannelActive();
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        ctx.fireChannelInactive();
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.fireChannelReadComplete();
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        log.info("userEventTriggered");
        ctx.fireUserEventTriggered(evt);
    }

    @Override
    public void channelWritabilityChanged(ChannelHandlerContext ctx) throws Exception {
        log.info("channelWritabilityChanged");
        ctx.fireChannelWritabilityChanged();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
            throws Exception {
        log.info("exceptionCaught");
        ctx.fireExceptionCaught(cause);
    }
}

