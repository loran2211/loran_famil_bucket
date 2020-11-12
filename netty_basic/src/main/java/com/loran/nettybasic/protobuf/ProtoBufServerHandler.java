package com.loran.nettybasic.protobuf;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * @Author: luol
 * @Date: 2020/11/11 15:01
 * @Description:
 */
public class ProtoBufServerHandler extends ChannelInboundHandlerAdapter {
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        PersonProto.Person req = (PersonProto.Person) msg;
        System.out.println("get data name=" + req.getName() + ",id=" + req.getId() + ",email_=" + req.getEmail());
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}

