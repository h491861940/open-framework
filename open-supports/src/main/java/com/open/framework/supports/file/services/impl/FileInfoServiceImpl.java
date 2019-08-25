package com.open.framework.supports.file.services.impl;

import com.open.framework.commmon.exceptions.PlatformException;
import com.open.framework.commmon.utils.RequestHolder;
import com.open.framework.commmon.web.PageBean;
import com.open.framework.commmon.web.QueryParam;
import com.open.framework.core.dao.impl.BaseDao;
import com.open.framework.supports.file.entity.FileInfo;
import com.open.framework.supports.file.repositories.FileInfoRepository;
import com.open.framework.supports.file.services.FileInfoService;
import com.open.framework.supports.file.services.FileStore;
import com.open.framework.supports.file.utils.FileMd5Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.util.List;
import java.util.stream.Stream;

/**
 * @author DuanWei mailto:<duan_w@neusoft.com>
 * @version 1.0
 * @Description: 文件基本信息服务实现
 * @date 2018/8/20 16:34
 */
@Service
public class FileInfoServiceImpl implements FileInfoService {

    @Autowired
    private FileInfoRepository fileInfoRepository;

    @Autowired
    private FileStore fileStore;
    @Override
    public List<String> upload(List<FileInfo> fileInfos) {
        return fileStore.store(fileInfos);
    }

    @Override
    public void download(String fileInfoId) {
        FileInfo info = fileInfoRepository.findById(fileInfoId).orElseGet(() -> {
            throw new PlatformException("文件ID:" + fileInfoId + "不存在！");
        });
        File file = new File(info.getPath() + info.getName());
        if (!file.exists() || !file.isFile()) {
            throw new PlatformException("文件:" + info.getPath() + info.getName() + "不存在！");
        }
        HttpServletResponse response = RequestHolder.getResponse();
        FileMd5Util.writeToPage(response, file, false, info.getName());
    }

    @Override
    @Transactional
    public void delete(List<String> ids) {
        if (!CollectionUtils.isEmpty(ids)) {
            try (Stream<FileInfo> infoStream = fileInfoRepository.findByGidIn(ids)) {
                infoStream.peek(info -> fileInfoRepository.deleteById(info.getGid())).forEach(info -> {
                    if (!FileMd5Util.deleteDirectory(info.getPath())) {
                        throw new PlatformException("文件目录:" + info.getPath() + "删除失败！");
                    }
                });
            }
        }
    }

    @Override
    public Object query(QueryParam queryParam) {
        return fileInfoRepository.findAll(queryParam);
    }

}
