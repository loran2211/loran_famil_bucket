package com.loran.nettyhttp.server;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpContentCompressor;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpRequestDecoder;
import io.netty.handler.codec.http.HttpResponseEncoder;
import io.netty.handler.ssl.SslContext;

/**
 * @Author: luol
 * @Date: 2020/11/10 14:33
 * @Description:
 */
public class ServerHandlerInit extends ChannelInitializer<SocketChannel> {
    private final SslContext sslCtx;

    public ServerHandlerInit(SslContext sslCtx) {
        this.sslCtx = sslCtx;
    }

    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        ChannelPipeline ph = ch.pipeline();
        if (sslCtx != null) {
            ph.addLast(sslCtx.newHandler(ch.alloc()));
        }
        //请求报文解码
        ph.addLast("decoder", new HttpRequestDecoder());
        //响应报文编码
        ph.addLast("encoder", new HttpResponseEncoder());
        //聚合http为一个完整的报文
        ph.addLast("aggregator", new HttpObjectAggregator(10 * 1024 * 1024));
        //把应答报文压缩 非必要
        ph.addLast("compressor", new HttpContentCompressor());
        ph.addLast(new BasicHandler());
    }
}
