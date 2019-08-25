package com.open.framework.supports.file.utils;

import com.alibaba.fastjson.JSON;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import javax.servlet.ServletOutputStream;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;
import javax.swing.filechooser.FileSystemView;
import java.io.*;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.List;

/**
 * @author DuanWei mailto:<duan_w@neusoft.com>
 * @version 1.0
 * @Description: 文件MD5工具类
 * @date 2018/8/21 18:31
 */
public class FileMd5Util {
    private static final String KEY_MD5 = "MD5";
    private static final int SIZE = 1024;

    private static final String WIN = "Windows";
    private static final String UNI = "Linux";
    private static final String MAC = "Mac";
    private static final String OS = System.getProperty("os.name");
    private static final String WIN_DESCRIPTION = "本地磁盘";

    private static final FileSystemView SYS = FileSystemView.getFileSystemView();


    /**
     * Get MD5 of one file:hex string,test OK!
     *
     * @param file
     * @return
     */
    private static String getFileMD5(File file) {
        if (!file.exists() || !file.isFile()) {
            return null;
        }
        try (FileInputStream in = new FileInputStream(file)) {
            return getFileMD5(in);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    /***
     * Get MD5 of one file！test ok!
     *
     * @param filepath
     * @return
     */
    public static String getFileMD5(String filepath) {
        File file = new File(filepath);
        return getFileMD5(file);
    }

    /**
     * MD5 encrypt,test ok
     *
     * @param data
     * @return byte[]
     * @throws Exception
     */
    public static byte[] encryptMD5(byte[] data) throws Exception {

        MessageDigest md5 = MessageDigest.getInstance(KEY_MD5);
        md5.update(data);
        return md5.digest();
    }


    /***
     * compare two file by Md5
     *
     * @param file1
     * @param file2
     * @return
     */
    public static boolean isSameMd5(File file1, File file2) {
        String md5File1 = FileMd5Util.getFileMD5(file1);
        String md5File2 = FileMd5Util.getFileMD5(file2);
        return md5File1 != null && md5File1.equals(md5File2);
    }

    /***
     * compare two file by Md5
     *
     * @param filepath1
     * @param filepath2
     * @return
     */
    public static boolean isSameMd5(String filepath1, String filepath2) {
        File file1 = new File(filepath1);
        File file2 = new File(filepath2);
        return isSameMd5(file1, file2);
    }


    /**
     * 删除单个文件
     *
     * @param sPath 被删除文件path
     * @return 删除成功返回true，否则返回false
     */
    private static boolean deleteFile(String sPath) {
        File file = new File(sPath);
        // 路径为文件且不为空则进行删除
        if (file.isFile() && file.exists()) {
            return file.delete();
        }
        return false;
    }

    /**
     * 删除目录以及目录下的文件
     *
     * @param sPath 被删除目录的路径
     * @return 目录删除成功返回true，否则返回false
     */
    public static boolean deleteDirectory(String sPath) {
        //如果sPath不以文件分隔符结尾，自动添加文件分隔符
        if (!sPath.endsWith(File.separator)) {
            sPath = sPath + File.separator;
        }
        File dirFile = new File(sPath);
        //如果dir对应的文件不存在，或者不是一个目录，则退出
        if (!dirFile.exists() || !dirFile.isDirectory()) {
            return false;
        }
        boolean flag = true;
        //删除文件夹下的所有文件(包括子目录)
        File[] files = dirFile.listFiles();
        if (!ObjectUtils.isEmpty(files)) {
            for (File file : files) {
                //删除子文件
                if (file.isFile()) {
                    flag = deleteFile(file.getAbsolutePath());
                    if (!flag) {
                        break;
                    }
                } //删除子目录
                else {
                    flag = deleteDirectory(file.getAbsolutePath());
                    if (!flag) {
                        break;
                    }
                }
            }
        }

        if (!flag) {
            return false;
        }
        return dirFile.delete();
    }

    public static String getFileMD5(InputStream in) {

        try {
            byte[] buffer = new byte[1024];
            int len;
            MessageDigest digest = MessageDigest.getInstance("MD5");
            while ((len = in.read(buffer, 0, SIZE)) != -1) {
                digest.update(buffer, 0, len);
            }
            BigInteger bigInt = new BigInteger(1, digest.digest());
            return bigInt.toString(16);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }


    }

    public static void writeToResponse(Object jsonObject, ServletResponse servletResponse) {

        try {
            servletResponse.setContentType("application/json");
            servletResponse.getWriter().print(JSON.toJSONString(jsonObject));
            servletResponse.getWriter().close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static void writeExceptionToResponse(final Exception e, ServletResponse servletResponse) {
        writeToResponse(e.getMessage(), servletResponse);
    }

    public static void writeToPage(HttpServletResponse response, File file, boolean delete, String fileName) {
        response.reset();
        try (ServletOutputStream out = response.getOutputStream();
             FileInputStream fis = new FileInputStream(file);
             BufferedInputStream bis = new BufferedInputStream(fis);
             BufferedOutputStream bos = new BufferedOutputStream(out)) {
            // 设置response参数，可以打开下载页面
            response.setContentType("application/vnd.ms-excel;charset=utf-8");
            response.setHeader("Content-Disposition",
                    "attachment;filename=" + new String((fileName).getBytes("utf-8"), "iso-8859-1"));
            response.setCharacterEncoding("utf-8");

            byte[] buff = new byte[2048];
            int bytesRead;
            while (-1 != (bytesRead = bis.read(buff, 0, buff.length))) {
                bos.write(buff, 0, bytesRead);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (delete) {
                deleteFile(file);
            }
        }
    }

    public static void deleteFile(File file) {
        // 判断文件是否存在
        if (file.exists()) {
            // 判断是否是文件
            if (file.isFile()) {
                // 删除文件
                file.delete();
                // 否则如果它是一个目录
            } else if (file.isDirectory()) {
                // 声明目录下所有的文件 files[];
                File[] files = file.listFiles();
                // 遍历目录下所有的文件
                for (int i = 0; i < files.length; i++) {
                    // 把每个文件用这个方法进行迭代
                    deleteFile(files[i]);
                }
                // 删除文件夹
                file.delete();
            }
        }
    }

    /**
     * 校验是否有效绝对路径
     *
     * @param path 配置路径
     * @return
     */
    public static boolean checkEffectivePath(String path) {
        if (OS.startsWith(WIN)) {
            for (String rootPath : listRootPathName(WIN_DESCRIPTION)) {
                if (path.startsWith(rootPath)) {
                    return true;
                }
            }
            return false;
        } else if (OS.startsWith(UNI) || OS.startsWith(MAC)) {
            return path.startsWith("/");
        }
        return true;
    }

    public static List<String> listRootPathName(String type) {
        List<String> paths = new ArrayList<>();
        File[] files = File.listRoots();
        boolean isEmpty = StringUtils.isEmpty(type);
        for (File file : files) {
            if (isEmpty) {
                paths.add(file.getPath());
            } else if (WIN_DESCRIPTION.equals(SYS.getSystemTypeDescription(file))) {
                paths.add(file.getPath());
            }
        }
        return paths;
    }
}
