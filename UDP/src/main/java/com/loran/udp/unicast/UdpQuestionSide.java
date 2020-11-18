package com.loran.udp.unicast;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.DatagramPacket;
import io.netty.channel.socket.nio.NioDatagramChannel;
import io.netty.util.CharsetUtil;

import java.net.InetSocketAddress;

/**
 * @Author: luol
 * @Date: 2020/11/18 15:17
 * @Description:发送端
 */
public class UdpQuestionSide {
    public final static String QUESTION = "告诉我一句古诗";


    public static void main(String[] args) throws InterruptedException {
        new UdpQuestionSide().run(UdpAnswerSide.ANSWER_PORT);
    }

    private void run(int port) throws InterruptedException {
        EventLoopGroup group = new NioEventLoopGroup();
        try {
            Bootstrap b = new Bootstrap();
            b.group(group)
                    .channel(NioDatagramChannel.class)
                    .handler(new QuestoinHandler());
            Channel channel = b.bind().sync().channel();
            channel.writeAndFlush(
                    new DatagramPacket(
                            Unpooled.copiedBuffer(QUESTION, CharsetUtil.UTF_8),
                            new InetSocketAddress("127.0.0.1", port)
                    )
            ).sync();
            if (channel.closeFuture().await(15000)) {
                System.out.println("等待超时");
            }
        } finally {
            group.shutdownGracefully();
        }
    }
}
