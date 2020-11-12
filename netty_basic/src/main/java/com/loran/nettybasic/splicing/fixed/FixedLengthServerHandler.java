package com.loran.nettybasic.splicing.fixed;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @Author: luol
 * @Date: 2020/11/5 10:59
 * @Description:
 */
@ChannelHandler.Sharable//多个线程之间共享
public class FixedLengthServerHandler extends ChannelInboundHandlerAdapter {
    private AtomicInteger counter = new AtomicInteger(0);

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf in = (ByteBuf) msg;
        String request = in.toString(CharsetUtil.UTF_8);
        System.out.println("Server Accept[" + request + "] and the counter is :" + counter.incrementAndGet());
        ctx.writeAndFlush(Unpooled.copiedBuffer(FixedLengthServer.RESPONSE.getBytes()));
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
