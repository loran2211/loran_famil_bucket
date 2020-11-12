package com.loran.nettyupload.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author: luol
 * @Date: 2020/11/12 14:27
 * @Description:文件描述信息
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class FileDescInfo {
    /**
     * 文件地址
     */
    private String fileUrl;
    /**
     * 文件名
     */
    private String fileName;
    /**
     * 文件大小
     */
    private Long fileSize;

}
