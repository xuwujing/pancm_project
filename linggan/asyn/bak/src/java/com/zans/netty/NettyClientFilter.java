package com.zans.netty;

import com.zans.vo.AsynReqVo;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.protobuf.ProtobufDecoder;
import io.netty.handler.codec.protobuf.ProtobufEncoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32FrameDecoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32LengthFieldPrepender;
import io.netty.handler.timeout.IdleStateHandler;

import java.util.concurrent.TimeUnit;
/**
 *
* @Title: NettyClientFilter
* @Description: Netty客户端 过滤器
* @Version:1.0.0
* @author pancm
* @date 2017年10月8日
 */
public class NettyClientFilter extends ChannelInitializer<SocketChannel> {


    @Override
    protected void initChannel(SocketChannel ch)  {
        ChannelPipeline ph = ch.pipeline();
        /*
         * 解码和编码，应和服务端一致
         * */
        //入参说明: 读超时时间、写超时时间、所有类型的超时时间、时间格式
        ph.addLast(new IdleStateHandler(0, 4, 0, TimeUnit.SECONDS));
        ph.addLast(new ProtobufVarint32FrameDecoder());
        ph.addLast(new ProtobufDecoder(AsynReqVo.AsynReqVoMsg.getDefaultInstance()));
        ph.addLast(new ProtobufVarint32LengthFieldPrepender());
        ph.addLast(new ProtobufEncoder());


        //业务逻辑实现类
        ph.addLast("nettyClientHandler",new com.zans.netty.NettyClientHandler());


    }
}
