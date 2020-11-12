package com.loran.nettybasic.splicing.delimiter;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.util.CharsetUtil;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @Author: luol
 * @Date: 2020/11/5 10:59
 * @Description:
 */
@ChannelHandler.Sharable//多个线程之间共享
public class EchoServerHandler extends ChannelInboundHandlerAdapter {
    private AtomicInteger counter = new AtomicInteger(0);

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("客户端，【" + ctx.channel().remoteAddress() + "已连接。。。。。。。】");
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf in = (ByteBuf) msg;
        String request = in.toString(CharsetUtil.UTF_8);
        System.out.println("Server Accept[" + request + "] and the counter is :" + counter.incrementAndGet());
        String resp = "Hello," + request + ". welcome to netty world!"
                + EchoServer.DELIMITER_SYMBOL;
        ctx.writeAndFlush(Unpooled.copiedBuffer(resp.getBytes()));
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("客户端，【" + ctx.channel().remoteAddress() + "即将关闭。。。。。。。】");
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }

    private static class ChannelInitializerImp extends ChannelInitializer<Channel> {
        @Override
        protected void initChannel(Channel ch) throws Exception {
          /*  ch.pipeline().addLast(new LineBasedFrameDecoder(1024));
            ch.pipeline().addLast(new EchoServerHandler());*/

            ByteBuf delimiter = Unpooled.copiedBuffer(EchoServer.DELIMITER_SYMBOL.getBytes());
            ch.pipeline().addLast(new DelimiterBasedFrameDecoder(1024, delimiter));
            ch.pipeline().addLast(new EchoServerHandler());
        }
    }
}
