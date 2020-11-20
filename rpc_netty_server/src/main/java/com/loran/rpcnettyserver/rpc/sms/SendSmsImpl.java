package com.loran.rpcnettyserver.rpc.sms;


import com.loran.rpcnettyserver.remote.SendSms;
import com.loran.rpcnettyserver.remote.vo.UserInfo;

/**
 * @Author: luol
 * @Date: 2020/11/20 15:14
 * @Description:短信息发送服务的实现
 */
public class SendSmsImpl implements SendSms {

    @Override
    public boolean sendMail(UserInfo user) {
        try {
            Thread.sleep(50);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("已发送短信息给：" + user.getName() + "到【" + user.getPhone() + "】");
        return true;
    }
}
