package com.loran.nettyhttp.server;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.*;
import io.netty.util.CharsetUtil;

/**
 * @Author: luol
 * @Date: 2020/11/10 14:32
 * @Description:
 */
public class BasicHandler extends ChannelInboundHandlerAdapter {

    /***
     * 发送的返回值
     * @param ctx
     * @param context
     * @param status
     */
    private void send(ChannelHandlerContext ctx, String context, HttpResponseStatus status) {
        FullHttpResponse response = new DefaultFullHttpResponse(
                HttpVersion.HTTP_1_1, status,
                Unpooled.copiedBuffer(context, CharsetUtil.UTF_8)
        );
        response.headers().set(HttpHeaderNames.CONTENT_TYPE, "text/plain;charset=UTF-8");
        ctx.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        String result = "";
        FullHttpRequest httpRequest = (FullHttpRequest) msg;
        System.out.println(httpRequest.headers());
        try {
            //获取路径
            String path = httpRequest.uri();
            //获取body
            String body = httpRequest.content().toString(CharsetUtil.UTF_8);
            //获取请求方法
            HttpMethod method = httpRequest.method();
            System.out.println("接收到：" + method + " 请求");
            if (!"/test".equalsIgnoreCase(path)) {
                result = "非法请求！" + path;
                send(ctx, result, HttpResponseStatus.BAD_REQUEST);
                return;
            }
            //如果时GET请求
            if (HttpMethod.GET.equals(method)) {
                System.out.println("body:" + body);
                result = "GET请求，应答：" + RespConstant.getNews();
                send(ctx, result, HttpResponseStatus.OK);
                return;
            }
            if (HttpMethod.POST.equals(method)) {
                return;
            }
        } catch (Exception e) {
            System.out.println("处理请求失败");
            e.printStackTrace();
        } finally {
            //释放请求
            httpRequest.release();
        }
    }

    /***
     * 建立连接时，返回消息
     * @param ctx
     * @throws Exception
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("连接的客户端地址：" + ctx.channel().remoteAddress());
    }
}
