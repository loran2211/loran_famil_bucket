package com.loran.nettyhttp.client;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.*;
import io.netty.util.CharsetUtil;

import java.net.URI;

/**
 * @Author: luol
 * @Date: 2020/11/10 14:32
 * @Description:
 */
public class HttpClientInboundHandler extends ChannelInboundHandlerAdapter {
    /**
     * 读取应答报文
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        FullHttpResponse httpResponse = (FullHttpResponse) msg;
        System.out.println(httpResponse.status());
        System.out.println(httpResponse.headers());
        System.out.println(httpResponse.protocolVersion());
        ByteBuf buf = httpResponse.content();
        System.out.println(buf.toString(CharsetUtil.UTF_8));
        //释放ByteBuf
        httpResponse.release();
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        URI uri = new URI("/test");
        String msg = "Hello";
        DefaultFullHttpRequest request = new DefaultFullHttpRequest(HttpVersion.HTTP_1_1,
                HttpMethod.GET,
                uri.toASCIIString(),
                Unpooled.wrappedBuffer(msg.getBytes("UTF-8")));
        //构建http请求
        request.headers().set(HttpHeaderNames.HOST, HttpClient.HOST);
        request.headers().set(HttpHeaderNames.CONNECTION, HttpHeaderValues.KEEP_ALIVE);
        request.headers().set(HttpHeaderNames.CONTENT_LENGTH, request.content().readableBytes());
        //发送http请求
        ctx.writeAndFlush(request);
    }
}
