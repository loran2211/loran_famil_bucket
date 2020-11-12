package com.loran.nettyupload.client;

import com.loran.nettyupload.model.Constants;
import com.loran.nettyupload.model.FileBurstData;
import com.loran.nettyupload.model.FileBurstInstruct;
import com.loran.nettyupload.model.FileTransferProtocol;
import com.loran.nettyupload.utils.FileUtil;
import com.loran.nettyupload.utils.MsgUtil;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * @Author: luol
 * @Date: 2020/11/12 14:40
 * @Description:
 */
public class MyClientHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        //数据格式验证
        if (!(msg instanceof FileTransferProtocol)) {
            return;
        }

        FileTransferProtocol fileTransferProtocol = (FileTransferProtocol) msg;
        //0传输文件'请求'、1文件传输'指令'、2文件传输'数据'
        switch (fileTransferProtocol.getTransferType()) {
            case 1:
                FileBurstInstruct fileBurstInstruct = (FileBurstInstruct) fileTransferProtocol.getTransferObj();
                //Constants.FileStatus ｛0开始、1中间、2结尾、3完成｝
                if (Constants.FileStatus.COMPLETE == fileBurstInstruct.getStatus()) {
                    ctx.flush();
                    ctx.close();
                    System.exit(-1);
                    return;
                }
                FileBurstData fileBurstData = FileUtil.readFile(fileBurstInstruct.getClientFileUrl(), fileBurstInstruct.getReadPosition());
                ctx.writeAndFlush(MsgUtil.buildTransferData(fileBurstData));
                System.out.println( " 客户端传输文件信息。 file：" + fileBurstData.getFileName() + " SIZE(byte)：" + (fileBurstData.getEndPos() - fileBurstData.getBeginPos()));
                break;
            default:
                break;
        }
    }

    /**
     * 当客户端主动断开服务端的链接后，这个通道就是不活跃的。也就是说客户端与服务端的关闭了通信通道并且不可以传输数据
     */
    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("===================" + ctx.channel().localAddress().toString() + " 断开连接===================");
        super.channelInactive(ctx);
    }

    /**
     * 抓住异常，当发生异常的时候，可以做一些相应的处理，比如打印日志、关闭链接
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.close();
        System.out.println("发现异常：\r\n" + cause.getMessage());
    }
}
