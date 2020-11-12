package com.loran.nettyupload.codec;

import com.loran.nettyupload.utils.SerializationUtil;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

/**
 * @Author: luol
 * @Date: 2020/11/12 14:32
 * @Description:自定义编码器
 */
public class CustomEncoder extends MessageToByteEncoder {

    private Class<?> genericClass;

    @Override
    protected void encode(ChannelHandlerContext ctx, Object in, ByteBuf out)  {
        if (genericClass.isInstance(in)) {
            byte[] data = SerializationUtil.serialize(in);
            out.writeInt(data.length);
            out.writeBytes(data);
        }
    }

    public CustomEncoder(Class<?> genericClass) {
        this.genericClass = genericClass;
    }
}