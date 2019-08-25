package com.open.framework.supports.file.services.impl;

import com.open.framework.commmon.exceptions.PlatformException;
import com.open.framework.commmon.utils.FileUtil;
import com.open.framework.commmon.utils.StringUtil;
import com.open.framework.supports.file.entity.FileInfo;
import com.open.framework.supports.file.enums.FileStatus;
import com.open.framework.supports.file.logic.UploadAsyncProcessor;
import com.open.framework.supports.file.repositories.FileInfoRepository;
import com.open.framework.supports.file.services.FileStore;
import com.open.framework.supports.file.utils.FileMd5Util;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.PostConstruct;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author DuanWei mailto:<duan_w@neusoft.com>
 * @version 1.0
 * @Description: 文件存储默认实现，单节点服务服务器保存
 * @date 2018/8/20 17:44
 */
@Component
public class DefaultFileStoreImpl implements FileStore {

    private static final Logger log = LoggerFactory.getLogger(DefaultFileStoreImpl.class);

    @Value("${app.file.path:#{systemProperties['user.dir']+'/fileUploader'}}")
    private String configUploadPath;

    private static final String ROOT_DIR = System.getProperty("user.dir");

    private static final String TEMP_DIR = ROOT_DIR.concat(File.separator).concat("temp");


    @Autowired
    private UploadAsyncProcessor uploadAsyncProcessor;

    @Autowired
    private FileInfoRepository fileInfoRepository;


    private boolean isEffectivePath;


    @PostConstruct
    public void init() {
        convertEnvironmentalPath(configUploadPath);
        isEffectivePath = FileMd5Util.checkEffectivePath(configUploadPath);
    }


    @Override
    public List<String> store(List<FileInfo> fileInfos) {

        //校验文件路径
        if (!isEffectivePath) {
            throw new PlatformException("配置路径错误,PATH：" + configUploadPath);
        }
        //补全文件路径,生成临时文件名
        fileInfos.forEach(info -> {
            info.setName(info.getMd5().concat("_%").concat(info.getName()));
            info.setPath(TEMP_DIR);
        });

        List resultHolders = new ArrayList<>();
        //重复校验
        checkInfo(fileInfos, resultHolders);
        if (CollectionUtils.isEmpty(fileInfos)) {
            return resultHolders;
        }
        AtomicInteger count = new AtomicInteger(fileInfos.size());
        for (FileInfo info : fileInfos) {

            //执行上传
            uploadAsyncProcessor.process(info,
                    new UploadAsyncProcessor.WriteChunkCompletionListener() {

                        @Override
                        public void success() {
                            info.setStatus(FileStatus.DONE.getCode());
                            //保存文件基本信息表
                            move2fileRepository(info);
                            fileInfoRepository.save(info);
                            resultHolders.add(info.getName()+":上传成功!");
                            count.getAndDecrement();
                        }

                        @Override
                        public void error(Exception exception) {
                            //上传到temp目录失败，删除该文件
                            FileMd5Util.deleteFile(new File(info.getPath().concat(File.separator).concat(info.getName())));
                            resultHolders.add(info.getName()+":"+ exception.getMessage());
                            log.error("fileUpload", exception);
                            count.getAndDecrement();

                        }

                    });
        }
        //等待异步线程执行完毕
        while (count.get() != 0) {
            try {
                TimeUnit.SECONDS.sleep(3);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return resultHolders;

    }

    /**
     * 把新上传到temp目录的文件，移到最终的目录
     *
     * @param fileInfo
     */
    private void move2fileRepository(FileInfo fileInfo) {
        Date date = new Date();
        String dir = configUploadPath + File.separator + new SimpleDateFormat("yyyy/MM/dd/").format(date);
        //如果不存在,创建文件夹
        File f = new File(dir);
        if (!f.exists()) {
            f.mkdirs();
        }

        String newFileName = fileInfo.getName().substring(fileInfo.getName().lastIndexOf("_%") + 2);
        FileUtil.moveFile(fileInfo.getPath().concat(File.separator).concat(fileInfo.getName()), dir, newFileName);
        fileInfo.setPath(dir);
        fileInfo.setName(newFileName);
    }

    private void checkInfo(List<FileInfo> fileInfos, List results) {
        List<String> md5s = new ArrayList<>();
        Iterator<FileInfo> it = fileInfos.iterator();
        while (it.hasNext()) {
            FileInfo info = it.next();
            FileInfo condition = new FileInfo();
            condition.setMd5(info.getMd5());
            fileInfoRepository.findOne(Example.of(condition)).ifPresent(f -> {
                it.remove();
                results.add(f.getName()+ ":已经存在!");
            });
            if (md5s.contains(info.getMd5())) {
                it.remove();
                info.setName(info.getName().substring(info.getName().lastIndexOf("_%") + 2));
                results.add(info.getName()+ ":文件内容已经存在!");
            } else {
                md5s.add(info.getMd5());
            }
        }
        md5s=null;
    }


    /**
     * 处理路径中文件分隔符
     *
     * @param defaultUploadFolder 默认上传文件路径
     * @return
     */
    private void convertEnvironmentalPath(String defaultUploadFolder) {
        if (StringUtil.isEmpty(defaultUploadFolder)) {
            return;
        }
        String[] split = defaultUploadFolder.split("/+|\\\\+");
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < split.length; i++) {
            String str = split[i];
            if (StringUtil.isEmpty(str)) {
                sb.append("/");
                continue;
            }
            //处理起始相对路径问题
            if (i == 0) {
                if (".".equals(str)) {
                    sb.append(ROOT_DIR);
                } else if ("..".equals(str)) {
                    sb.append(ROOT_DIR, 0, ROOT_DIR.lastIndexOf(File.separator));
                } else {
                    sb.append(str);
                }
            } else {
                sb.append(str);
            }
            sb.append(File.separator);
        }
        //处理相对路径问题

        configUploadPath = sb.toString();
    }


}
