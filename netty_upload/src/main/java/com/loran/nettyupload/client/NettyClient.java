package com.loran.nettyupload.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import lombok.Data;

/**
 * @Author: luol
 * @Date: 2020/11/12 14:38
 * @Description:netty客户端启动类
 */
@Data
public class NettyClient {

    private Channel channel;

    public ChannelFuture init(String ip, int port) {
        EventLoopGroup group = new NioEventLoopGroup();
        ChannelFuture future=null;
        try {
            Bootstrap b = new Bootstrap();
            b.group(group).
                    channel(NioSocketChannel.class).
                    option(ChannelOption.AUTO_READ, true).
                    handler(new MyClientChannelInitializer());
            future = b.connect(ip, port).syncUninterruptibly();
            channel = future.channel();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return future;
    }
}
