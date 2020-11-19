package com.loran.nettyws.server;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.group.ChannelGroup;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.codec.http.websocketx.extensions.compression.WebSocketServerCompressionHandler;
import io.netty.handler.ssl.SslContext;
import io.netty.channel.socket.SocketChannel;


/**
 * @Author: luol
 * @Date: 2020/11/19 13:57
 * @Description:增加handler
 */
public class WebSocketServerInitializer extends ChannelInitializer<SocketChannel> {
    private final ChannelGroup group;
    private final SslContext sslCtx;
    private static final String WEBSOCKET_PATH = "/websocket";

    public WebSocketServerInitializer(SslContext sslCtx, ChannelGroup group) {
        this.group = group;
        this.sslCtx = sslCtx;
    }

    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();
        if (sslCtx != null) {
            pipeline.addLast(sslCtx.newHandler(ch.alloc()));
        }
        pipeline.addLast(new HttpServerCodec());
        pipeline.addLast(new HttpObjectAggregator(65536));

        /*支持ws数据的压缩传输*/
        pipeline.addLast(new WebSocketServerCompressionHandler());

        pipeline.addLast(new WebSocketServerProtocolHandler(WEBSOCKET_PATH,
                null, true));

        /*浏览器访问展示index界面*/
        pipeline.addLast(new ProcessWsIndexPageHandler(WEBSOCKET_PATH));
        /*对webSocke进行处理*/
        pipeline.addLast(new ProcessWsFrameHandler(group));
    }
}
