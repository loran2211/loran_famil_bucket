package com.loran.nettyadv.server;

import com.loran.nettyadv.vo.MyMessage;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;

/**
 * @Author: luol
 * @Date: 2020/11/20 10:21
 * @Description:
 */
@Slf4j
public class ServerBusiHandler extends SimpleChannelInboundHandler<MyMessage> {
    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, MyMessage myMessage) throws Exception {
        log.info(String.valueOf(myMessage));
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        log.info(ctx.channel().remoteAddress().toString() + "主动断开了连接！");
    }
}
