package com.loran.nettyupload.client;

import com.loran.nettyupload.codec.CustomDecoder;
import com.loran.nettyupload.codec.CustomEncoder;
import com.loran.nettyupload.model.FileTransferProtocol;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;

/**
 * @Author: luol
 * @Date: 2020/11/12 14:39
 * @Description:
 */
public class MyClientChannelInitializer extends ChannelInitializer<SocketChannel> {
    /**
     * 这个方法在Channel被注册到EventLoop的时候会被调用
     * @param socketChannel
     * @throws Exception
     */
    @Override
    protected void initChannel(SocketChannel socketChannel) throws Exception {
        System.out.println("=========连接到服务端=========");
        System.out.println("channelId："+socketChannel.id());
        //对象传输处理
        socketChannel.pipeline().addLast(new CustomDecoder(FileTransferProtocol.class));
        socketChannel.pipeline().addLast(new CustomEncoder(FileTransferProtocol.class));
        socketChannel.pipeline().addLast(new MyClientHandler());
    }
}