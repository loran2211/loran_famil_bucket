package com.loran.nettybasic.splicing.delimiter;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.CharsetUtil;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @Author: luol
 * @Date: 2020/11/5 11:24
 * @Description:
 */
public class EchoClientHandler extends SimpleChannelInboundHandler<ByteBuf> {
    private AtomicInteger counter = new AtomicInteger(0);

    //读取到网络数据后进行业务处理
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ByteBuf msg) throws Exception {
        System.out.println("client Accept[" + msg.toString(CharsetUtil.UTF_8)
                + "] and the counter is:" + counter.incrementAndGet());
    }

    //channel活跃后，做业务处理
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        ByteBuf msg = null;
        String request = "Mark,Lison,King,James,Deer"
                + EchoServer.DELIMITER_SYMBOL;
        for (int i = 0; i < 10; i++) {
            msg = Unpooled.buffer(request.length());
            msg.writeBytes(request.getBytes());
            ctx.writeAndFlush(msg);
        }
    }

    /**
     * 发生异常后的处理
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
