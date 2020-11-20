package com.loran.rpcnettyserver.rpc.base;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Author: luol
 * @Date: 2020/11/20 15:22
 * @Description:
 */
@Service
@Slf4j
public class RegisterService {
    /*本地可以提供服务的一个容器*/
    private static final Map<String, Class> serviceCache = new ConcurrentHashMap<>();

    /*注册本服务*/
    public void regService(String serviceName, Class impl) {
        serviceCache.put(serviceName, impl);
        log.info(impl.getClass().getName() + "注册成功！，一共：" + serviceCache.size() + "个服务！");
    }

    /*获取服务*/
    public Class getLocalService(String serviceName) {
        log.info("获取" + serviceName + "服务");
        return serviceCache.get(serviceName);
    }
}
