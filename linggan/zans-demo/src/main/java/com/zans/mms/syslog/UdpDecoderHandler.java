package com.zans.mms.syslog;

/**
 * @author xv
 * @since 2022/6/29 9:18
 */
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.socket.DatagramPacket;
import io.netty.handler.codec.MessageToMessageDecoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.util.List;

@Service
public class UdpDecoderHandler extends MessageToMessageDecoder<DatagramPacket>  {
    private static final Logger log = LoggerFactory.getLogger(UdpDecoderHandler.class);

    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, DatagramPacket datagramPacket, List<Object> out) throws Exception {
        ByteBuf byteBuf = datagramPacket.content();
        byte[] data = new byte[byteBuf.readableBytes()];
        byteBuf.readBytes(data);
        String msg = new String(data);

        //获取IP地址
        InetSocketAddress ia = datagramPacket.sender();
        String ip = ia.getAddress().getHostAddress();
        int port = ia.getPort();
        //将数据传入下一个handler
        out.add(SyslogEntity.builder().message(msg).sourceIp(ip).port(port).build());
    }
}
