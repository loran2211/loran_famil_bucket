package com.loran.nettyupload.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author: luol
 * @Date: 2020/11/12 14:28
 * @Description:文件传输协议
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class FileTransferProtocol {
    /**
     * 0请求传输文件、1文件传输指令、2文件传输数据
     */
    private Integer transferType;
    /**
     *数据对象；(0)FileDescInfo、(1)FileBurstInstruct、(2)FileBurstData
     */
    private Object transferObj;

}
