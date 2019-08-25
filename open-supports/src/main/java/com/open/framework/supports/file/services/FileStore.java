package com.open.framework.supports.file.services;


import com.open.framework.supports.file.entity.FileInfo;

import java.util.List;

/**
 * @author DuanWei mailto:<duan_w@neusoft.com>
 * @version 1.0
 * @Description: 文件存储服务
 * @date 2018/8/20 16:31
 */
public interface FileStore {
    /**
     * 文件存储
     *
     * @param fileInfos 上传文件信息
     * @return 上传结果及失败信息
     */

    List<String> store(List<FileInfo> fileInfos);
}
