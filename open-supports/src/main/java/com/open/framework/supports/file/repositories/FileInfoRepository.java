package com.open.framework.supports.file.repositories;



import com.open.framework.dao.repository.base.BaseRepository;
import com.open.framework.supports.file.entity.FileInfo;

import java.util.List;
import java.util.stream.Stream;

/**
 * @author DuanWei mailto:<duan_w@neusoft.com>
 * @version 1.0
 * @Description: 文件基本信息服务
 * @date 2018/8/22 13:34
 */
public interface FileInfoRepository extends BaseRepository<FileInfo, String> {
    /**
     * 查询文件基本信息集合
     *
     * @param ids ID集合
     * @return
     */
    Stream<FileInfo> findByGidIn(List<String> ids);
}
