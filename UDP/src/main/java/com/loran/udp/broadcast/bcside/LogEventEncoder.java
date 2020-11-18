package com.loran.udp.broadcast.bcside;

import com.loran.udp.broadcast.LogMsg;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.socket.DatagramPacket;
import io.netty.handler.codec.MessageToMessageEncoder;
import io.netty.util.CharsetUtil;

import java.net.InetSocketAddress;
import java.util.List;

/**
 * @Author: luol
 * @Date: 2020/11/18 15:36
 * @Description:编码，将实际的日志实体类编码为DatagramPacket
 */
public class LogEventEncoder extends MessageToMessageEncoder<LogMsg> {
    private final InetSocketAddress remoteAddress;

    //LogEventEncoder 创建了即将被发送到指定的 InetSocketAddress
    // 的 DatagramPacket 消息
    public LogEventEncoder(InetSocketAddress remoteAddress) {
        this.remoteAddress = remoteAddress;
    }

    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext,
                          LogMsg logMsg, List<Object> out) throws Exception {
        byte[] bytes = logMsg.getMsg().getBytes(CharsetUtil.UTF_8);
        ByteBuf buf = channelHandlerContext.alloc().buffer(
                8*2+bytes.length+1);
        buf.writeLong(logMsg.getTime());
        buf.writeLong(logMsg.getMsgId());
        buf.writeByte(LogMsg.SEPARATOR);
        buf.writeBytes(bytes);
        out.add(new DatagramPacket(buf,remoteAddress));

    }
}
