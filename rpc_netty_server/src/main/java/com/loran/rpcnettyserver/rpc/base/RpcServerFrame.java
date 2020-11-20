package com.loran.rpcnettyserver.rpc.base;

import com.loran.rpcnettyserver.remote.SendSms;
import com.loran.rpcnettyserver.rpc.base.server.ServerInit;
import com.loran.rpcnettyserver.rpc.base.vo.NettyConstant;
import com.loran.rpcnettyserver.rpc.sms.SendSmsImpl;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.io.IOException;
import java.nio.CharBuffer;

/**
 * @Author: luol
 * @Date: 2020/11/20 15:22
 * @Description:
 */
@Service
@Slf4j
public class RpcServerFrame implements Runnable {
    @Autowired
    private RegisterService registerService;
    @Autowired
    private ServerInit serverInit;


    private EventLoopGroup bossGroup = new NioEventLoopGroup();
    private EventLoopGroup workerGroup = new NioEventLoopGroup();


    public void bind() throws Exception {
        ServerBootstrap b = new ServerBootstrap();
        b.group(bossGroup, workerGroup).channel(NioServerSocketChannel.class)
                .option(ChannelOption.SO_BACKLOG, 1024)
                .childHandler(serverInit);

        // 绑定端口，同步等待成功
        b.bind(NettyConstant.SERVER_PORT).sync();
        log.info("网络服务已准备好，可以进行业务操作了....... : "
                + (NettyConstant.SERVER_IP + " : "
                + NettyConstant.SERVER_PORT));
    }

    @PostConstruct
    public void startNet() throws Exception {
        registerService.regService(SendSms.class.getName(), SendSmsImpl.class);
        new Thread(this).start();
    }


    @PreDestroy
    public void stopNet() throws InterruptedException {
        bossGroup.shutdownGracefully().sync();
        workerGroup.shutdownGracefully().sync();
    }

    @Override
    public void run() {
        try {
            bind();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
