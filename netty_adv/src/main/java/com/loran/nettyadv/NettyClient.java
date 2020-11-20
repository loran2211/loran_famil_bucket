package com.loran.nettyadv;

import com.loran.nettyadv.client.ClientInit;
import com.loran.nettyadv.vo.MessageType;
import com.loran.nettyadv.vo.MyHeader;
import com.loran.nettyadv.vo.MyMessage;
import com.loran.nettyadv.vo.NettyConstant;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import lombok.SneakyThrows;

import java.net.InetSocketAddress;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * @Author: luol
 * @Date: 2020/11/20 10:18
 * @Description: Netty客户端的主入口
 */
public class NettyClient implements Runnable {
    /*负责重连的线程池*/
    private ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
    private Channel channel;
    private EventLoopGroup group = new NioEventLoopGroup();

    /*是否用户主动关闭连接的标志值*/
    private volatile boolean userClose = false;
    /*连接是否成功关闭的标志值*/
    private volatile boolean connected = false;

    public boolean isConnected() {
        return connected;
    }

    /*连接服务器*/
    public void connect(int port, String host) throws InterruptedException {
        try {
            /*客户端启动必备*/
            Bootstrap b = new Bootstrap();
            b.group(group)
                    .handler(new ClientInit())
                    .channel(NioSocketChannel.class);
            ChannelFuture future = b.connect(new InetSocketAddress(host, port)).sync();
            channel = future.sync().channel();
            synchronized (this) {
                this.connected = true;
                this.notifyAll();
            }
            future.channel().closeFuture().sync();
        } finally {
            if (!userClose) {
                /*非正常关闭，有可能发生了网络问题，进行重连*/
                System.out.println("需要进行重连");
                executor.execute(() -> {
                    /*给操作系统足够的时间，去释放相关的资源*/
                    try {
                        TimeUnit.SECONDS.sleep(1);
                        connect(NettyConstant.SERVER_PORT, NettyConstant.SERVER_IP);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
            } else {
                /*正常关闭*/
                channel = null;
                group.shutdownGracefully().sync();
                synchronized (this) {
                    this.connected = false;
                    this.notifyAll();
                }
            }

        }
    }

    @SneakyThrows
    @Override
    public void run() {
        connect(NettyConstant.SERVER_PORT, NettyConstant.SERVER_IP);
    }

    /*------------以下方法供业务方使用--------------------------*/
    public void send(Object message) {
        if (channel == null || !channel.isActive()) {
            throw new IllegalStateException("和服务器还未未建立起有效连接！请稍后再试！！");
        }
        MyMessage msg = new MyMessage();
        MyHeader myHeader = new MyHeader();
        myHeader.setType(MessageType.SERVICE_REQ.value());
        msg.setMyHeader(myHeader);
        msg.setBody(message);
        channel.writeAndFlush(msg);
    }

    public void close() {
        userClose = true;
        channel.close();
    }

    /*------------测试NettyClient--------------------------*/
    public static void main(String[] args) throws Exception {
        NettyClient nettyClient = new NettyClient();
        nettyClient.connect(NettyConstant.SERVER_PORT
                , NettyConstant.SERVER_IP);
    }
}
