package com.loran.nettybasic.protobuf;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * @Author: luol
 * @Date: 2020/11/11 14:50
 * @Description:
 */
public class ProtoBufClientHandler extends ChannelInboundHandlerAdapter {
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("Prepare to make data........");
        PersonProto.Person.Builder builder = PersonProto.Person.newBuilder();
        builder.setName("loran");
        builder.setId(100);
        builder.setEmail("loran@163.com");
        System.out.println("send data........");
        ctx.writeAndFlush(builder.build());
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
