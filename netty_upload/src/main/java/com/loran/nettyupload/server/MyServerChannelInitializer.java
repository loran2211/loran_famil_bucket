package com.loran.nettyupload.server;

import com.loran.nettyupload.codec.CustomDecoder;
import com.loran.nettyupload.codec.CustomEncoder;
import com.loran.nettyupload.model.FileTransferProtocol;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import org.springframework.stereotype.Component;

/**
 * @Author: luol
 * @Date: 2020/11/12 14:18
 * @Description:
 */
@Component
public class MyServerChannelInitializer extends ChannelInitializer<SocketChannel> {
    /**
     * 这个方法在Channel被注册到EventLoop的时候会被调用
     *
     * @param socketChannel
     * @throws Exception
     */
    @Override
    protected void initChannel(SocketChannel socketChannel) throws Exception {
        System.out.println("=========有客户端连接服务器=========");
        System.out.println("ip:" + socketChannel.localAddress().getHostString() + "         port:" + socketChannel.localAddress().getPort());
        //对象传输处理
        socketChannel.pipeline().addLast(new CustomDecoder(FileTransferProtocol.class));
        socketChannel.pipeline().addLast(new CustomEncoder(FileTransferProtocol.class));
        socketChannel.pipeline().addLast(new MyServerHandler());
    }
}
