package com.loran.nettybasic.echo;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.sctp.nio.NioSctpServerChannel;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

import java.net.InetSocketAddress;
import java.net.Socket;

/**
 * @Author: luol
 * @Date: 2020/11/5 10:41
 * @Description:
 */
public class EchoServer {
    private final int port;

    public EchoServer(int port) {
        this.port = port;
    }

    public static void main(String[] args) throws InterruptedException {
        int port = 9999;
        EchoServer echoServer = new EchoServer(port);
        System.out.println("服务端即将启动");
        echoServer.start();
        System.out.println("服务端关闭");
    }

    public void start() throws InterruptedException {
        final EchoServerHandler echoServerHandler = new EchoServerHandler();
        //线程组
        EventLoopGroup group = new NioEventLoopGroup();
        //服务端启动必备
        try {
            ServerBootstrap b = new ServerBootstrap();
            b.group(group)
                    .channel(NioServerSocketChannel.class)//指定使用NIO的通讯模式
                    .localAddress(new InetSocketAddress(port))//指定监听端口
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ch.pipeline().addLast(echoServerHandler);
                        }
                    });
            ChannelFuture f = b.bind().sync();//异步绑定到服务器，sync()会阻测到完成
            f.channel().closeFuture().sync();//阻塞当前线程，直到服务器的ServerChannel被关闭
        } finally {
            group.shutdownGracefully().sync();
        }
    }
}
