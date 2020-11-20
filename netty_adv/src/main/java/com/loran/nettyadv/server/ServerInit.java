package com.loran.nettyadv.server;

import com.loran.nettyadv.kryocodec.KryoDecoder;
import com.loran.nettyadv.kryocodec.KryoEncoder;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import io.netty.handler.timeout.ReadTimeoutHandler;

/**
 * @Author: luol
 * @Date: 2020/11/20 10:21
 * @Description: 服务端初始化
 */
public class ServerInit extends ChannelInitializer<SocketChannel> {
    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        /*粘包半包问题*/
        ch.pipeline().addLast(new LengthFieldBasedFrameDecoder(65535,
                0, 2,
                0, 2));
        ch.pipeline().addLast(new LengthFieldPrepender(2));

        /*序列化相关*/
        ch.pipeline().addLast(new KryoDecoder());
        ch.pipeline().addLast(new KryoEncoder());

        /*处理心跳超时*/
        ch.pipeline().addLast(new ReadTimeoutHandler(15));

        /*处理登陆验证*/
        ch.pipeline().addLast(new LoginAuthRespHandler());

        /*处理心跳心跳*/
        ch.pipeline().addLast(new HeartBeatRespHandler());

        /*处理业务*/
        ch.pipeline().addLast(new ServerBusiHandler());

    }
}