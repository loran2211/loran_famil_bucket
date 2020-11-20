package com.loran.repnettyclient.config;

import org.springframework.context.ApplicationContext;

/**
 * @Author: luol
 * @Date: 2020/11/20 15:34
 * @Description:
 */
public class SpringUtil {
    private static ApplicationContext applicationContext;

    public static ApplicationContext getApplicationContext() {
        return SpringUtil.applicationContext;
    }

    public static void setApplicationContext(ApplicationContext applicationContext) {
        SpringUtil.applicationContext = applicationContext;
    }
}
