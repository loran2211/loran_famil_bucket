package com.loran.nettyupload.server;

import com.loran.nettyupload.model.FileTransferProtocol;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * @Author: luol
 * @Date: 2020/11/12 14:20
 * @Description:
 */
public class MyServerHandler extends ChannelInboundHandlerAdapter {
    /**
     * 文件存放地址
     */
    private String path = "D:\\Coding\\Loran_Code\\loran_famil_bucket\\netty_upload\\testFile";

    /**
     * 通道有消息触发
     *
     * @param ctx
     * @param msg
     * @throws Exception
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        //数据格式验证
        if (!(msg instanceof FileTransferProtocol)) {
            return;
        }
    }
}
