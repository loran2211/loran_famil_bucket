package com.loran.mq;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

/**
 * @Author: luol
 * @Date: 2020/10/19 16:40
 * @Description:
 */
@SpringBootApplication
public class LoranMqProviderApplication {
    public static void main(String[] args) {
        SpringApplication.run(LoranMqProviderApplication.class, args);
    }

    @Bean
    @LoadBalanced  // 没有加这个的话没有办法使用服务名来调用接口
    public RestTemplate getRestTemplate(){
        return new RestTemplate();
    }
}
