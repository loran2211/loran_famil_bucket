package com.loran.nettyadv.server;

import com.loran.nettyadv.kryocodec.KryoDecoder;
import com.loran.nettyadv.kryocodec.KryoEncoder;
import com.loran.nettyadv.vo.MessageType;
import com.loran.nettyadv.vo.MyHeader;
import com.loran.nettyadv.vo.MyMessage;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.util.ReferenceCountUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * @Author: luol
 * @Date: 2020/11/20 10:21
 * @Description: 心跳处理完成
 */
@Slf4j
public class HeartBeatRespHandler extends ChannelInboundHandlerAdapter {
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        MyMessage message = (MyMessage) msg;
        /*是不是心跳请求*/
        if (message.getMyHeader() != null && MessageType.HEARTBEAT_REQ.value() == message.getMyHeader().getType()) {
            /*心跳应答报文*/
            MyMessage heartBeatResp = buildHeatBeat();
            ctx.writeAndFlush(heartBeatResp);
            //释放消息
            ReferenceCountUtil.release(msg);
        } else {
            ctx.fireChannelRead(msg);
        }
    }

    private MyMessage buildHeatBeat() {
        MyMessage message = new MyMessage();
        MyHeader myHeader = new MyHeader();
        myHeader.setType(MessageType.HEARTBEAT_RESP.value());
        message.setMyHeader(myHeader);
        return message;
    }
}
