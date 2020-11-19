package com.loran.nettyws.server;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import lombok.extern.slf4j.Slf4j;

import java.util.Locale;

/**
 * @Author: luol
 * @Date: 2020/11/19 13:56
 * @Description:
 */
@Slf4j
public class ProcessWsFrameHandler extends SimpleChannelInboundHandler<WebSocketFrame> {
    private final ChannelGroup group;

    public ProcessWsFrameHandler(ChannelGroup group) {
        this.group = group;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, WebSocketFrame frame) throws Exception {
        //文本帧
        if (frame instanceof TextWebSocketFrame) {
            String request = ((TextWebSocketFrame) frame).text();
            ctx.channel().writeAndFlush(new TextWebSocketFrame(request.toUpperCase(Locale.CHINA)));
            group.writeAndFlush(
                    new TextWebSocketFrame(
                            "Client:" + ctx.channel() + ",say:" + request.toUpperCase(Locale.CHINA)
                    ));
        } else {
            throw new UnsupportedOperationException("unsupport data frame");
        }
    }

    /*重写userEventTriggered（）方法以处理自定义事件*/
    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        /*检测事件类型，如果是ws握手成功实践，群发通知*/
        if (evt == WebSocketServerProtocolHandler.ServerHandshakeStateEvent.HANDSHAKE_COMPLETE) {
            group.writeAndFlush(
                    new TextWebSocketFrame("Client" + ctx.channel() + "joined")
            );
            group.add(ctx.channel());
        }
    }
}
