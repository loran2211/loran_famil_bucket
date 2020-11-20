package com.loran.nettyadv.client;

import com.loran.nettyadv.vo.MessageType;
import com.loran.nettyadv.vo.MyHeader;
import com.loran.nettyadv.vo.MyMessage;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.ReferenceCountUtil;
import io.netty.util.concurrent.ScheduledFuture;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;

/**
 * @Author: luol
 * @Date: 2020/11/20 10:52
 * @Description:
 */
@Slf4j
public class HeartBeatReqHandler extends ChannelInboundHandlerAdapter {
    private volatile ScheduledFuture<?> heartBeat;

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        MyMessage message = (MyMessage) msg;
        /*是不是握手认证成功的应答*/
        if (message.getMyHeader() != null && MessageType.LOGIN_RESP.value() == message.getMyHeader().getType()) {
            /*Netty已经提供了定时机制，定时发出心跳请求*/
            ctx.executor().scheduleAtFixedRate(
                    new HeartBeatTask(ctx), 0,
                    5000, TimeUnit.MILLISECONDS);
            ReferenceCountUtil.release(msg);
        } else if (message.getMyHeader() != null && MessageType.HEARTBEAT_RESP.value() == message.getMyHeader().getType()) {
            log.info("收到服务器心跳应答");
            ReferenceCountUtil.release(msg);
        } else {
            ctx.fireChannelRead(msg);
        }
    }

    /*心跳请求的任务类*/
    private class HeartBeatTask implements Runnable {
        private final ChannelHandlerContext ctx;

        private HeartBeatTask(ChannelHandlerContext ctx) {
            this.ctx = ctx;
        }

        @Override
        public void run() {
            MyMessage heartBeat = buildHeatBeat();
            ctx.writeAndFlush(heartBeat);
        }

        private MyMessage buildHeatBeat() {
            MyMessage message = new MyMessage();
            MyHeader myHeader = new MyHeader();
            myHeader.setType(MessageType.HEARTBEAT_REQ.value());
            message.setMyHeader(myHeader);
            return message;
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
            throws Exception {
        cause.printStackTrace();
        if (heartBeat != null) {
            heartBeat.cancel(true);
            heartBeat = null;
        }
        ctx.fireExceptionCaught(cause);
    }
}
