package com.loran.nettybasic.protobuf;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.protobuf.ProtobufDecoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32FrameDecoder;

/**
 * @Author: luol
 * @Date: 2020/11/11 14:56
 * @Description:
 */
public class ProtoBufServer {


    public static void main(String[] args) throws Exception {
        int port = 9991;
        new ProtoBufServer().bind(port);
    }

    private void bind(int port) throws Exception {
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap b = new ServerBootstrap();
            b.group(bossGroup, workGroup)
                    .channel(NioServerSocketChannel.class)
                    .option(ChannelOption.SO_BACKLOG, 1000)
                    .childHandler(new ChannelInitializer<SocketChannel>() {

                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            /*去除消息长度部分，同时*/
                            ch.pipeline().addLast(
                                    new ProtobufVarint32FrameDecoder()
                            );
                            ch.pipeline().addLast(new ProtobufDecoder(
                                    PersonProto.Person.getDefaultInstance()
                            ));
                            ch.pipeline().addLast(new ProtoBufServerHandler());
                        }
                    });

            ChannelFuture f = b.bind(port).sync();
            System.out.println("init start");
            f.channel().closeFuture().sync();
        } finally {
            bossGroup.shutdownGracefully();
            workGroup.shutdownGracefully();
        }

    }
}
