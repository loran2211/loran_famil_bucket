package com.loran.nettyupload;

import com.loran.nettyupload.client.NettyClient;
import com.loran.nettyupload.model.FileTransferProtocol;
import com.loran.nettyupload.utils.MsgUtil;
import io.netty.channel.ChannelFuture;

import java.io.File;

/**
 * @Author: luol
 * @Date: 2020/11/12 14:46
 * @Description:
 */
public class FileUploading {

    public static void main(String[] args) {
        ChannelFuture channelFuture = new NettyClient().init("127.0.0.1", 1100);
        File file=new File("D:\\000101020092511856-00.pdf");
        FileTransferProtocol fileTransferProtocol = MsgUtil.buildRequestTransferFile(file.getAbsolutePath(), file.getName(), file.length());
        channelFuture.channel().writeAndFlush(fileTransferProtocol);
    }
}
