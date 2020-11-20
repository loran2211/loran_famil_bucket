package com.loran.repnettyclient.remote;


import com.loran.repnettyclient.remote.vo.UserInfo;

/**
 * @Author: luol
 * @Date: 2020/11/20 15:33
 * @Description:短信息发送接口
 */
public interface SendSms {
    /*发送短信*/
    boolean sendMail(UserInfo user);

}
