package com.loran.rpcnettyserver.rpc.base.vo;

/**
 * @Author: luol
 * @Date: 2020/11/20 10:18
 * @Description: 消息实体类
 */
public final class MyMessage {

    private MyHeader myHeader;

    private Object body;

    public final MyHeader getMyHeader() {
    	return myHeader;
    }

    public final void setMyHeader(MyHeader myHeader) {
    	this.myHeader = myHeader;
    }

    public final Object getBody() {
    	return body;
    }

    public final void setBody(Object body) {
    	this.body = body;
    }

    @Override
    public String toString() {
    	return "MyMessage [myHeader=" + myHeader + "][body="+body+"]";
    }
}
