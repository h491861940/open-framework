package com.open.framework.supports.file.logic;


import com.google.common.util.concurrent.ThreadFactoryBuilder;
import com.open.framework.commmon.exceptions.PlatformException;
import com.open.framework.supports.file.entity.FileInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.PreDestroy;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.Callable;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;

/**
 * @author copy antoinem
 * @version 1.0
 * @Description: java类作用描述
 * @date 2018/8/20 21:34
 */
@Component
public class UploadAsyncProcessor {

    private static final Logger log = LoggerFactory.getLogger(UploadAsyncProcessor.class);

    private static final int SIZE_OF_THE_BUFFER_IN_BYTES = 8192;

    private static final long WAIT_TIME_OUT = 1;

    private ThreadFactory namedThreadFactory = new ThreadFactoryBuilder().setNameFormat("upload-pool-%d").build();

    private ScheduledThreadPoolExecutor uploadWorkersPool = new ScheduledThreadPoolExecutor(10, namedThreadFactory);

    @PreDestroy
    private void destroy() throws InterruptedException {
        log.debug("destroying executor");
        uploadWorkersPool.shutdown();
        if (!uploadWorkersPool.awaitTermination(WAIT_TIME_OUT, TimeUnit.MINUTES)) {
            log.error("executor timed out");
            List<Runnable> shutdownNow = uploadWorkersPool.shutdownNow();
            for (Runnable runnable : shutdownNow) {
                log.error(runnable + "has not been terminated");
            }
        }
    }


    public void process(FileInfo fileInfo,
                        WriteChunkCompletionListener completionListener) {
        try {
            File outDir = new File(fileInfo.getPath());
            File outFile = new File(outDir, fileInfo.getName());
            if (!outDir.exists() && !outDir.mkdirs()) {
                throw new PlatformException("文件目录:" + outDir + "创建失败");
            }
            synchronized (this) {
                if (outFile.exists()) {
                    throw new PlatformException("文件:" + outFile + "已存在");
                }
                if (!outFile.createNewFile()) {
                    throw new PlatformException("文件:" + outFile + "创建失败");
                }
            }
            FileOutputStream outputStream = new FileOutputStream(outFile, true);
            final WriteChunkToFileTask task =
                    new WriteChunkToFileTask(fileInfo,
                            outputStream, completionListener);
            uploadWorkersPool.submit(task);
        } catch (Exception e) {
            completionListener.error(e);
        }


    }

    public interface WriteChunkCompletionListener {
        /**
         * 异常回调处理
         *
         * @param exception
         */
        void error(Exception exception);

        /**
         * 成功回到处理
         */
        void success();
    }

    public class WriteChunkToFileTask
            implements Callable<Void> {


        private final InputStream inputStream;
        private final FileOutputStream outputStream;
        private final UUID fileId;

        private final WriteChunkCompletionListener completionListener;


        private long byteProcessed;


        WriteChunkToFileTask(FileInfo fileInfo,
                             FileOutputStream outputStream, WriteChunkCompletionListener completionListener) {
            this.fileId = fileInfo.getFileId();
            this.inputStream = fileInfo.getInputStream();
            this.outputStream = outputStream;
            this.completionListener = completionListener;
        }

        @Override
        public Void call() {
            try {
                write();
            } catch (Exception e) {
                completeWithError(e);
            }
            return null;
        }

        private void write()
                throws IOException {
            byte[] buffer = new byte[SIZE_OF_THE_BUFFER_IN_BYTES];
            int bytesCount = inputStream.read(buffer);
            if (bytesCount != -1) {
                log.trace("Processed bytes {} of request ({})", (byteProcessed += bytesCount), fileId);
                outputStream.write(buffer, 0, bytesCount);
                uploadWorkersPool.submit(this);
            } else {
                closeFileStream();
                success();
            }
        }

        void completeWithError(Exception e) {
            completionListener.error(e);
        }

        void success() {
            closeFileStream();
            completionListener.success();
        }

        private void closeFileStream() {
            log.debug("Closing FileOutputStream of " + fileId);
            try {
                outputStream.close();
            } catch (Exception e) {
                log.error("Error closing file output stream for id " + fileId + ": " + e.getMessage());
            }
        }

    }

}
