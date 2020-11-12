package com.loran.nettyupload.codec;

import com.loran.nettyupload.utils.SerializationUtil;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

/**
 * @Author: luol
 * @Date: 2020/11/12 14:30
 * @Description:自定义解码器
 */
public class CustomDecoder extends ByteToMessageDecoder {

    private Class<?> genericClass;

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) {
        if (in.readableBytes() < 4) {
            return;
        }
        in.markReaderIndex();
        int dataLength = in.readInt();
        if (in.readableBytes() < dataLength) {
            in.resetReaderIndex();
            return;
        }
        byte[] data = new byte[dataLength];
        in.readBytes(data);
        out.add(SerializationUtil.deserialize(data, genericClass));
    }

    public CustomDecoder(Class<?> genericClass) {
        this.genericClass = genericClass;
    }

}
