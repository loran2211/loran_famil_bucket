package com.loran.repnettyclient.remote.vo;

import java.io.Serializable;

/**
 * @Author: luol
 * @Date: 2020/11/20 15:33
 * @Description:用户的实体类，已实现序列化
 */
public class UserInfo implements Serializable {

    private String name;
    private String phone;

    public UserInfo() {
    }

    public UserInfo(String name, String phone) {
        this.name = name;
        this.phone = phone;
    }

    public String getName() {
        return name;
    }

    public String getPhone() {
        return phone;
    }
}
