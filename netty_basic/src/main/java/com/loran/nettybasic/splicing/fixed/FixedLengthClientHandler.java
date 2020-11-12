package com.loran.nettybasic.splicing.fixed;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.CharsetUtil;

/**
 * @Author: luol
 * @Date: 2020/11/5 11:24
 * @Description:
 */
public class FixedLengthClientHandler extends SimpleChannelInboundHandler<ByteBuf> {
    //读取到网络数据后进行业务处理
    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, ByteBuf msg) throws Exception {
        System.out.println("client accept:" + msg.toString(CharsetUtil.UTF_8));
    }

    //channel活跃后，做业务处理
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        ByteBuf msg = null;
        for (int i = 0; i < 10; i++) {
            msg = Unpooled.buffer(FixedLengthClient.REQUEST.length());
            msg.writeBytes(FixedLengthClient.REQUEST.getBytes());
            ctx.writeAndFlush(msg);
        }
    }
}
