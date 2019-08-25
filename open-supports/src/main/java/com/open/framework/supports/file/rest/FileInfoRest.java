package com.open.framework.supports.file.rest;

import com.open.framework.commmon.BaseConstant;
import com.open.framework.commmon.utils.StringUtil;
import com.open.framework.commmon.web.JsonResult;
import com.open.framework.commmon.web.PageBean;
import com.open.framework.commmon.web.QueryParam;
import com.open.framework.supports.file.entity.FileInfo;
import com.open.framework.supports.file.enums.FileStatus;
import com.open.framework.supports.file.enums.FileType;
import com.open.framework.supports.file.services.FileInfoService;
import com.open.framework.supports.file.utils.FileMd5Util;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;

/**
 * @author DuanWei mailto:<duan_w@neusoft.com>
 * @version 1.0
 * @Description: 文件基本信息应用服务
 * @date 2018/8/20 16:36
 */
@RestController
@RequestMapping("/file")
public class FileInfoRest {

    private static final String DELIMITER = ".";

    @Autowired
    private FileInfoService service;

    @PostMapping("/upload")
    public JsonResult upload(@RequestParam("files") MultipartFile[] files) {
        List<FileInfo> fileInfos = extractFileUploadInfos(files);
        List<String> list=service.upload(fileInfos);
        return JsonResult.success(list);
    }

    @GetMapping("/download")
    public void download(@RequestParam(value = "gid") String gid) {
        service.download(gid);
    }


    @DeleteMapping
    public void delete(@RequestBody List<String> ids) {
        service.delete(ids);
    }

    @RequestMapping(value = "/query", method = RequestMethod.POST)
    @ApiOperation(value = "根据条件获取当页数据")
    public JsonResult query(@RequestBody QueryParam queryParam) {
        Object obj = service.query(queryParam);
        if (obj instanceof PageBean) {
            return JsonResult.successPage((PageBean) obj);
        } else {
            return JsonResult.success(obj);
        }
    }
    /**
     * @param files 上传文件数组
     * @return 文件基本信息集合
     * @throws IOException
     */
    private List<FileInfo> extractFileUploadInfos(MultipartFile[] files) {
        List<FileInfo> fileInfos = new ArrayList<>();
        Arrays.asList(files).parallelStream().forEach(file -> {
            final FileInfo fileInfo = new FileInfo();
            fileInfo.setFileId(UUID.randomUUID());
            try {
                fileInfo.setInputStream(file.getInputStream());
                fileInfo.setMd5(FileMd5Util.getFileMD5(file.getInputStream()));
            } catch (IOException e) {
                e.printStackTrace();
            }
            String fileName = file.getOriginalFilename();
            fileInfo.setName(fileName);
            if (Objects.requireNonNull(fileName).contains(DELIMITER)) {
                fileInfo.setSuffix(fileName.substring(fileName.indexOf(DELIMITER)));
                fileInfo.setType(FileType.getNameBySuffix(fileInfo.getSuffix()));
            } else {
                fileInfo.setType(FileType.OTHER.toString());
            }
            fileInfo.setOriginalSize(file.getSize());
            fileInfo.setStatus(FileStatus.UNDONE.getCode());

            fileInfos.add(fileInfo);
        });
        return fileInfos;
    }
}
