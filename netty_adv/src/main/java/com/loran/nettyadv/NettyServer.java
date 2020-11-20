package com.loran.nettyadv;

import com.loran.nettyadv.server.ServerInit;
import com.loran.nettyadv.vo.NettyConstant;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import lombok.extern.slf4j.Slf4j;

/**
 * @Author: luol
 * @Date: 2020/11/20 10:18
 * @Description:服务端的主入口
 */
@Slf4j
public class NettyServer {
    private static void bind() throws Exception {
        // 配置服务端的NIO线程组
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workGroup = new NioEventLoopGroup();
        ServerBootstrap b = new ServerBootstrap();
        b.group(bossGroup, workGroup)
                .channel(NioServerSocketChannel.class)
                .option(ChannelOption.SO_BACKLOG, 1024)
                .childHandler(new ServerInit());

        // 绑定端口，同步等待成功
        b.bind(NettyConstant.SERVER_PORT).sync();
        log.info("Netty server start : " + (NettyConstant.SERVER_IP + " : " + NettyConstant.SERVER_PORT));
    }

    public static void main(String[] args) throws Exception {
        bind();
    }
}
