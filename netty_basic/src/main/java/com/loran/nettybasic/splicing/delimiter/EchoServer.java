package com.loran.nettybasic.splicing.delimiter;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;

import java.net.InetSocketAddress;

/**
 * @Author: luol
 * @Date: 2020/11/5 10:41
 * @Description:使用特殊字符分割进行传输
 */
public class EchoServer {
    public static final String DELIMITER_SYMBOL = "&_";
    private final int port;

    public EchoServer(int port) {
        this.port = port;
    }

    public static void main(String[] args) throws InterruptedException {
        int port = 9996;
        System.out.println("服务端即将启动");
        new EchoServer(port).start();
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
          /*  ch.pipeline().addLast(new LineBasedFrameDecoder(1024));
            ch.pipeline().addLast(new EchoServerHandler());*/

            ByteBuf delimiter = Unpooled.copiedBuffer(DELIMITER_SYMBOL.getBytes());
            ch.pipeline().addLast(new DelimiterBasedFrameDecoder(1024, delimiter));
            ch.pipeline().addLast(new EchoServerHandler());
        }
    }
}
