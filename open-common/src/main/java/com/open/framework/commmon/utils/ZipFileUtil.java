package com.open.framework.commmon.utils;

import java.io.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * Zip文件工具类
 * 
 * @author
 */
public class ZipFileUtil {

    /**
     * 压缩文件
     * 
     * @param sourceDir 原文件目录
     * @param zipFile 压缩后的文件名称
     * @throws Exception
     */
    public static void doZip(File sourceDir, File zipFile) throws Exception {
        OutputStream os = new FileOutputStream(zipFile);
        BufferedOutputStream bos = new BufferedOutputStream(os);
        ZipOutputStream zos = new ZipOutputStream(bos);
        String basePath = null;
        if (sourceDir.isDirectory()) {
            basePath = sourceDir.getPath();
        } else {
            basePath = sourceDir.getParent();
        }
        zipFile(sourceDir, basePath, zos);
        zos.closeEntry();
        zos.close();
    }

    /**
     * 压缩文件
     * 
     * @param source
     * @param basePath
     * @param zos
     * @throws Exception
     */
    private static void zipFile(File source, String basePath, ZipOutputStream zos) throws Exception {
        File[] files = new File[0];
        if (source.isDirectory()) {
            files = source.listFiles();
        } else {
            files = new File[1];
            files[0] = source;
        }
        String pathName;
        byte[] buf = new byte[1024];
        int length = 0;
        for (File file : files) {
            if (file.isDirectory()) {
                pathName = file.getPath().substring(basePath.length() + 1) + "/";
                zos.putNextEntry(new ZipEntry(pathName));
                zipFile(file, basePath, zos);
            } else {
                pathName = file.getPath().substring(basePath.length() + 1);
                InputStream is = new FileInputStream(file);
                BufferedInputStream bis = new BufferedInputStream(is);
                zos.putNextEntry(new ZipEntry(pathName));
                while ((length = bis.read(buf)) > 0) {
                    zos.write(buf, 0, length);
                }
                is.close();
            }
        }
    }

}
