package com.open.framework.workflow.controller;

import com.open.framework.workflow.service.ActModelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.*;

/**
 * 工作流controller
 */
@RestController
@RequestMapping("/workFlow")
public class WorkFlowController {

    @Autowired
    private ActModelService actModelService;

    /**
     * 部署工作流
     * @param files
     * @throws IOException
     */
    @PostMapping("/deployFile")
    public String upload(@RequestParam("files") MultipartFile[] files) throws IOException {
       if (null != files && files.length > 0) {
           return  actModelService.deployByFile(files[0].getInputStream(), files[0].getOriginalFilename());
       }
       return "";
      // return AjaxDddResult.fail("部署失败");
    }

    /**
     *  根据部署id查询流程图片
     * @param deploymentId
     * @param request
     * @param response
     * @throws IOException
     */
    @GetMapping("/showPng")
    public void showPng(@RequestParam(value = "deploymentId") String deploymentId, HttpServletRequest request,
                         HttpServletResponse response) throws IOException {
        BufferedImage image = actModelService.showPng(deploymentId);
        getImg(image, response);
    }
    /**
     * 查询动态的流程图,高亮当前节点
     * @param processInsId
     * @param request
     * @param response
     * @throws IOException
     */
    @GetMapping("/showFlow")
    public void showFlow(@RequestParam(value = "processInsId") String processInsId, HttpServletRequest request,
                             HttpServletResponse response)  throws IOException{
        BufferedImage image = actModelService.showFlow(processInsId);
        getImg(image, response);
    }

    private void getImg(BufferedImage image,HttpServletResponse response) throws IOException{
        response.setHeader("Pragma", "no-cache");
        response.setHeader("Cache-Control", "no-cache");
        response.setDateHeader("Expires", 0);
        ImageIO.write(image, "png", response.getOutputStream());
    }
}
