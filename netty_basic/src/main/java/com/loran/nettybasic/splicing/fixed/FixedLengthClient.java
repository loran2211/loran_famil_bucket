package com.loran.nettybasic.splicing.fixed;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.FixedLengthFrameDecoder;

import java.net.InetSocketAddress;

/**
 * @Author: luol
 * @Date: 2020/11/5 10:41
 * @Description:固定长度进行传输
 */
public class FixedLengthClient {
    public final static String REQUEST = "Mark,Lison,Peter,James,Deer";
    private final int port;
    private final String host;

    public FixedLengthClient(int port, String host) {
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
                    .handler(new ChannelInitializerImp());
            ChannelFuture f = b.connect().sync();//异步连接到服务器，sync()会阻测到完成
            f.channel().closeFuture().sync();//阻塞当前线程，直到客户端的Channel被关闭
        } finally {
            group.shutdownGracefully().sync();
        }
    }

    private static class ChannelInitializerImp extends ChannelInitializer<Channel> {

        @Override
        protected void initChannel(Channel ch) throws Exception {
            ch.pipeline().addLast(new FixedLengthFrameDecoder(FixedLengthServer.RESPONSE.length()));
            ch.pipeline().addLast(new FixedLengthClientHandler());

        }
    }

    public static void main(String[] args) throws InterruptedException {
        new FixedLengthClient(9997, "127.0.0.1").start();
    }
}
