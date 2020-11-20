package com.loran.rpcnettyserver.remote;


import com.loran.rpcnettyserver.remote.vo.UserInfo;

/**
 * @Author: luol
 * @Date: 2020/11/20 15:14
 * @Description: 短信息发送接口
 */
public interface SendSms {
    /*发送短信*/
    boolean sendMail(UserInfo user);

}
