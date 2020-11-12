package com.loran.nettybasic.echo;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.net.InetSocketAddress;

/**
 * @Author: luol
 * @Date: 2020/11/5 10:41
 * @Description:
 */
public class EchoClient {
    private final int port;
    private final String host;

    public EchoClient(int port, String host) {
        this.port = port;
        this.host = host;
    }

    public void start() throws InterruptedException {
        //线程组
        EventLoopGroup group = new NioEventLoopGroup();
        //服务端启动必备
        try {
            Bootstrap b = new Bootstrap();
            b.group(group)
                    .channel(NioSocketChannel.class)//指定使用NIO的通讯模式
                    .remoteAddress(new InetSocketAddress(host, port))//指定服务器的ip和端口
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ch.pipeline().addLast(new EchoClientHandler());
                        }
                    });
            ChannelFuture f = b.connect().sync();//异步连接到服务器，sync()会阻测到完成
            f.channel().closeFuture().sync();//阻塞当前线程，直到客户端的Channel被关闭
        } finally {
            group.shutdownGracefully().sync();
        }
    }

    public static void main(String[] args) throws InterruptedException {
        new EchoClient(9999, "127.0.0.1").start();
    }
}
