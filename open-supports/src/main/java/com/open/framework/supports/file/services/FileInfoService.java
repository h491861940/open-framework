package com.open.framework.supports.file.services;

import com.open.framework.commmon.web.PageBean;
import com.open.framework.commmon.web.QueryParam;
import com.open.framework.supports.file.entity.FileInfo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.List;

/**
 * @author DuanWei mailto:<duan_w@neusoft.com>
 * @version 1.0
 * @Description: 文件基本信息服务
 * @date 2018/8/20 16:29
 */
public interface FileInfoService {

    /**
     * 文件上传处理
     *
     * @param fileInfos 上传你文件信息
     * @return 成功列表
     */
    List<String> upload(List<FileInfo> fileInfos);

    /**
     * 文件下载
     *
     * @param fileInfoId 文件基本信息主键
     */
    void download(String fileInfoId);

    /**
     * 文件批量删除
     *
     * @param ids 文件基本信息Id集合
     */
    void delete(List<String> ids);

    Object query(QueryParam queryParam);
}
