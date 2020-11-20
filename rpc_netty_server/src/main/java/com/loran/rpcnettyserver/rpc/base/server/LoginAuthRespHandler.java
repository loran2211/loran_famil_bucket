package com.loran.rpcnettyserver.rpc.base.server;

import com.loran.rpcnettyserver.rpc.base.vo.MessageType;
import com.loran.rpcnettyserver.rpc.base.vo.MyHeader;
import com.loran.rpcnettyserver.rpc.base.vo.MyMessage;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.ReferenceCountUtil;
import lombok.extern.slf4j.Slf4j;

import java.net.InetSocketAddress;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Author: luol
 * @Date: 2020/11/20 10:21
 * @Description:
 */
@Slf4j
public class LoginAuthRespHandler extends ChannelInboundHandlerAdapter {
    //用以检查用户是否重复登录的缓存
    private static Map<String, Boolean> nodeCheck = new ConcurrentHashMap<String, Boolean>();
    //用户登录的白名单
    private String[] whitekList = {"127.0.0.1"};

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        MyMessage message = (MyMessage) msg;
        /*是不是握手认证请求*/
        if (message.getMyHeader() != null && message.getMyHeader().getType() == MessageType.LOGIN_REQ.value()) {
            String nodeIndex = ctx.channel().remoteAddress().toString();
            MyMessage loginResp = null;
            /*重复登陆判断，拒绝*/
            if (nodeCheck.containsKey(nodeIndex)) {
                buildResponse((byte) -1);
            } else {
                /*检查用户是否在白名单中，在则允许登陆，并写入缓存*/
                InetSocketAddress address = (InetSocketAddress) ctx.channel().remoteAddress();
                String ip = address.getAddress().getHostAddress();
                boolean isOK = false;
                for (String WIP : whitekList) {
                    if (WIP.equals(ip)) {
                        isOK = true;
                        break;
                    }
                }
                loginResp = isOK ? buildResponse((byte) 0) : buildResponse((byte) -1);
                if (isOK) {
                    nodeCheck.put(nodeIndex, true);
                }
                ctx.writeAndFlush(loginResp);
                ReferenceCountUtil.release(msg);
            }
        } else {
            ctx.fireChannelRead(msg);
        }
    }


    private MyMessage buildResponse(byte result) {
        MyMessage message = new MyMessage();
        MyHeader myHeader = new MyHeader();
        myHeader.setType(MessageType.LOGIN_RESP.value());
        message.setMyHeader(myHeader);
        message.setBody(result);
        return message;
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        //cause.printStackTrace();
        // 删除缓存
        nodeCheck.remove(ctx.channel().remoteAddress().toString());
        ctx.close();
        ctx.fireExceptionCaught(cause);
    }
}
