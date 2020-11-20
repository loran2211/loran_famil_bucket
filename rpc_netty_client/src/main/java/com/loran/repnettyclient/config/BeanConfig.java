package com.loran.repnettyclient.config;

import com.loran.repnettyclient.remote.SendSms;
import com.loran.repnettyclient.rpc.RpcClientFrame;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Author: luol
 * @Date: 2020/11/20 15:33
 * @Description:
 */
@Configuration
public class BeanConfig {
    @Autowired
    private RpcClientFrame rpcClientFrame;

    @Bean
    public SendSms getSmsService() throws Exception {
        return rpcClientFrame.getRemoteProxyObject(SendSms.class);
    }
}
