package com.loran.nettybasic;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import io.netty.handler.codec.TooLongFrameException;

import java.util.List;

/**
 * @Author: luol
 * @Date: 2020/11/10 14:21
 * @Description:
 */
public class TooLongExSample extends ByteToMessageDecoder {
    private static final int MAX_SIZE = 1024;

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf byteBuf, List<Object> out) throws Exception {
        int readable = byteBuf.readableBytes();
        if (readable > MAX_SIZE) {
            ctx.close();
            throw new TooLongFrameException("传入的数据太多");
        }
    }
}
