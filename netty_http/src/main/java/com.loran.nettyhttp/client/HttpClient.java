package com.loran.nettyhttp.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.http.*;

/**
 * @Author: luol
 * @Date: 2020/11/10 14:32
 * @Description:
 */
public class HttpClient {
    public static final String HOST = "127.0.0.1";
    private static final boolean SSL = false;

    private void connect(String host, int port) throws Exception {
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            Bootstrap b = new Bootstrap();
            b.group(workerGroup);
            b.channel(NioSocketChannel.class);
            b.option(ChannelOption.SO_KEEPALIVE, true);
            b.handler(new ChannelInitializer<SocketChannel>() {
                @Override
                protected void initChannel(SocketChannel ch) throws Exception {
                    ch.pipeline().addLast(new HttpClientCodec());
                    //聚合http为一个完整的报文
                    ch.pipeline().addLast("aggregator", new HttpObjectAggregator(10 * 1024 * 1024));
                    //把应答报文解压缩
                    ch.pipeline().addLast("decompressor", new HttpContentDecompressor());
                    ch.pipeline().addLast(new HttpClientInboundHandler());

                }
            });
            ChannelFuture f = b.connect(host, port).sync();
            f.channel().closeFuture().sync();
        } finally {
            workerGroup.shutdownGracefully();
        }
    }

    public static void main(String[] args) throws Exception {
        HttpClient client = new HttpClient();
        client.connect("127.0.0.1", 6789);
    }

}
