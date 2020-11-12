package com.loran.nettybasic.splicing.linebase;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.LineBasedFrameDecoder;

import java.net.InetSocketAddress;

/**
 * @Author: luol
 * @Date: 2020/11/5 10:41
 * @Description:
 */
public class LinebaseEchoServer {
    private final int port;

    public LinebaseEchoServer(int port) {
        this.port = port;
    }

    public static void main(String[] args) throws InterruptedException {
        int port = 9998;
        LinebaseEchoServer echoServer = new LinebaseEchoServer(port);
        System.out.println("服务端即将启动");
        echoServer.start();
        System.out.println("服务端关闭");
    }

    public void start() throws InterruptedException {
        final LinebaseServerHandler echoServerHandler = new LinebaseServerHandler();
        //线程组
        EventLoopGroup group = new NioEventLoopGroup();
        //服务端启动必备
        try {
            ServerBootstrap b = new ServerBootstrap();
            b.group(group)
                    .channel(NioServerSocketChannel.class)//指定使用NIO的通讯模式
                    .localAddress(new InetSocketAddress(port))//指定监听端口
                    .childHandler(new ChannelInitializerImp());
            ChannelFuture f = b.bind().sync();//异步绑定到服务器，sync()会阻测到完成
            f.channel().closeFuture().sync();//阻塞当前线程，直到服务器的ServerChannel被关闭
        } finally {
            group.shutdownGracefully().sync();
        }
    }

    private static class ChannelInitializerImp extends ChannelInitializer<Channel> {

        @Override
        protected void initChannel(Channel ch) throws Exception {
            ch.pipeline().addLast(new LineBasedFrameDecoder(1024));
            ch.pipeline().addLast(new LinebaseServerHandler());
        }
    }
}
