package com.loran.rpcnettyserver.rpc.base.server;

import com.loran.rpcnettyserver.rpc.base.RegisterService;
import com.loran.rpcnettyserver.rpc.base.vo.MessageType;
import com.loran.rpcnettyserver.rpc.base.vo.MyHeader;
import com.loran.rpcnettyserver.rpc.base.vo.MyMessage;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author: luol
 * @Date: 2020/11/20 10:21
 * @Description:
 */
@Slf4j
@Service
@ChannelHandler.Sharable
public class ServerBusiHandler extends SimpleChannelInboundHandler<MyMessage> {
    @Autowired
    private RegisterService registerService;

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, MyMessage msg)
            throws Exception {
        log.info(String.valueOf(msg));
        MyMessage message = new MyMessage();
        MyHeader myHeader = new MyHeader();
        myHeader.setSessionID(msg.getMyHeader().getSessionID());
        myHeader.setType(MessageType.SERVICE_RESP.value());
        message.setMyHeader(myHeader);
        Map<String, Object> content = (HashMap<String, Object>) msg.getBody();
        /*方法所在类名接口名*/
        String serviceName = (String) content.get("siName");
        /*方法的名字*/
        String methodName = (String) content.get("methodName");
        /*方法的入参类型*/
        Class<?>[] paramTypes = (Class<?>[]) content.get("paraTypes");
        /*方法的入参的值*/
        Object[] args = (Object[]) content.get("args");
        /*从容器中拿到服务的Class对象*/
        Class serviceClass = registerService.getLocalService(serviceName);
        if (serviceClass == null) {
            throw new ClassNotFoundException(serviceName + " not found");
        }

        /*通过反射，执行实际的服务*/
        Method method = serviceClass.getMethod(methodName, paramTypes);
        boolean result = (boolean) method.invoke(serviceClass.newInstance(), args);
        message.setBody(result);
        ctx.writeAndFlush(message);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx)
            throws Exception {
        log.info(ctx.channel().remoteAddress() + " 主动断开了连接!");
    }

}
