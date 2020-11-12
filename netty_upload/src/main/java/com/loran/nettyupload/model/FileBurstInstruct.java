package com.loran.nettyupload.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author: luol
 * @Date: 2020/11/12 14:26
 * @Description:文件分片指令
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class FileBurstInstruct {
    /**
     * Constants.FileStatus ｛0开始、1中间、2结尾、3完成｝
     */
    private Integer status;
    /**
     * 客户端文件URL
     */
    private String clientFileUrl;
    /**
     * 读取位置
     */
    private Integer readPosition;

    public FileBurstInstruct(Integer status) {
        this.status = status;
    }

}
