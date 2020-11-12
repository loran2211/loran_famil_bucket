package com.loran.nettyupload.utils;

import com.loran.nettyupload.model.FileBurstInstruct;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Author: luol
 * @Date: 2020/11/12 14:22
 * @Description:
 */
public class CacheUtil {
    public static Map<String, FileBurstInstruct> burstDataMap = new ConcurrentHashMap<>();
}
