package com.loran.rpcnettyserver.rpc.base.kryocodec;

import com.loran.rpcnettyserver.rpc.base.vo.MyMessage;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

/**
 * @Author: luol
 * @Date: 2020/11/20 10:18
 * @Description：序列化的Handler
 */
public class KryoEncoder  extends MessageToByteEncoder<MyMessage> {

    @Override
    protected void encode(ChannelHandlerContext ctx, MyMessage message,
                          ByteBuf out) throws Exception {
        KryoSerializer.serialize(message, out);
        ctx.flush();
    }
}
