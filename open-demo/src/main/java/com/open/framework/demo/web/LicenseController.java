package com.open.framework.demo.web;

import com.open.framework.commmon.license.CheckLicenseUtil;
import com.open.framework.commmon.license.LicenseDTO;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;


@Controller
@RequestMapping("/license")
public class LicenseController {

    @GetMapping
    public String index() {
        return "/license";
    }

    @PostMapping("/generate")
    public void generate(LicenseDTO licenseDTO, HttpServletResponse response) throws Exception {
        byte[] encodedData= CheckLicenseUtil.generate(licenseDTO);
        response.setContentType("application/octet-stream");
        response.setHeader("Content-disposition", "attachment;filename=license");
        response.getOutputStream().write(encodedData);
    }
    @GetMapping("/getInfo")
    @ResponseBody
    public String getInfo() {
        return CheckLicenseUtil.getAllInfo();
    }
}
