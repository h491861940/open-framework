package com.open.framework.file.web;

import com.open.framework.commmon.exceptions.PlatformException;
import com.open.framework.commmon.web.JsonResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.IIOException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;

/**
 * @description: 通用的上传下载类
 * @author: hsj
 * @date: 2019-08-23 17:53:51
 */
@RestController
@RequestMapping("/file")
public  class FileController {
    /**
     * @Author hsj
     * @Description 文件的下载,filePath为具体的文件路径,包括文件名,例如http://xxx/file/download?filePath=E://logs//q.png
     * @Date  2019-08-23 17:55:36
     * @Params filePath
     * @Returns
     **/
    @GetMapping("/download")
    public void download(@RequestParam("filePath")String filePath,HttpServletRequest request,
                         HttpServletResponse response) throws UnsupportedEncodingException {
        File file = new File(filePath);
        // 如果文件名存在，则进行下载
        if (file.exists()) {
            // 配置文件下载
            response.setHeader("content-type", "application/octet-stream");
            response.setContentType("application/octet-stream");
            // 下载文件能正常显示中文
            response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(file.getName(), "UTF-8"));
            // 实现文件下载
            byte[] buffer = new byte[1024];
            FileInputStream fis = null;
            BufferedInputStream bis = null;
            try {
                fis = new FileInputStream(file);
                bis = new BufferedInputStream(fis);
                OutputStream os = response.getOutputStream();
                int i = bis.read(buffer);
                while (i != -1) {
                    os.write(buffer, 0, i);
                    i = bis.read(buffer);
                }
                System.out.println("Download the song successfully!");
            }
            catch (Exception e) {
                System.out.println("Download the song failed!");
            }
            finally {
                if (bis != null) {
                    try {
                        bis.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if (fis != null) {
                    try {
                        fis.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }else{
            throw new PlatformException(filePath+"文件不存在");
        }

    }
    /**
     * @Author hsj
     * @Description 文件的上传,filePath为具体的文件路径,例如http://xxx/file/upload?filePath=E://logs//
     * @Date  2019-08-23 17:55:36
     * @Params filePath 文件存放路径,files多个文件
     * @Returns
     **/
    @PostMapping("/upload")
    public JsonResult upload(@RequestParam("files") MultipartFile[] files, @RequestParam("filePath")String filePath) throws IOException {
        MultipartFile file =files[0];
        //文件后缀名
        //String suffix = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf("."));
        //上传文件名
        String filename = file.getOriginalFilename();
        //服务器端保存的文件对象
        File serverFile = new File(filePath + filename);
        //将上传的文件写入到服务器端文件内
        file.transferTo(serverFile);
        return  JsonResult.success();
    }
    /**
     * @Author hsj
     * @Description 文件的显示,目前支持jpg,png,文本,filePath为具体的文件路径,包括文件名,例如http://xxx/file/fileShow?filePath=E://logs//q.png
     * @Date  2019-08-23 17:55:36
     * @Params filePath
     * @Returns
     **/
    @GetMapping("/fileShow")
    public void fileShow(@RequestParam("filePath")String filePath,HttpServletRequest request, HttpServletResponse response) throws IOException {
        File file =new File(filePath);
        if (file.exists()) {
            response.setDateHeader("Expires", 0);
            response.setHeader("Cache-Control", "no-store, no-cache, must-revalidate");
            response.addHeader("Cache-Control", "post-check=0, pre-check=0");
            response.setHeader("Pragma", "no-cache");
            String type = file.getAbsolutePath().substring(file.getAbsolutePath().lastIndexOf(".")+1);
            if ("jpg".equals(type) || "JPG".equals(type)) {
                response.setContentType("image/jpeg");
            } else if ("png".equals(type) || "PNG".equals(type)) {
                response.setContentType("image/png");
            } else {
                response.setContentType("text/html;charset=UTF-8");
            }
            ServletOutputStream out = response.getOutputStream();
            FileInputStream fis = new FileInputStream(file);
            InputStreamReader read = new InputStreamReader(new FileInputStream(file), "UTF-8");
            BufferedInputStream bis = new BufferedInputStream(fis);
            BufferedOutputStream bos = new BufferedOutputStream(out);
            try {
                byte[] buff = new byte[2048];
                int bytesRead;
                while (-1 != (bytesRead = bis.read(buff, 0, buff.length))) {
                    bos.write(buff, 0, bytesRead);
                }
            } catch (IIOException e) {
                throw new PlatformException("读取失败");
            } finally {
                try {
                    bos.flush();
                } finally {
                    bos.close();
                    fis.close();
                }
            }
        }else{
            throw new PlatformException(filePath+"文件不存在");
        }
    }
}
