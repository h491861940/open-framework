package com.open.framework.demo.web;

import com.open.framework.commmon.email.EmailService;

import javax.annotation.Resource;
 
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(value="/email") 
public class DemoEmailController  {

	@Resource
	private EmailService emailService;

	/**
	 * 测试邮件发送
	 */
	@RequestMapping(value = "/getTestDemoEmail", method = RequestMethod.GET)
	public  void getEntityById() throws Exception {
		String sendTo = "491861940@qq.com";
		String titel = "测试邮件标题";
		String content = "测试邮件内容";
		emailService.sendSimpleMail(sendTo, titel, content);
	}
	/**
	 * 测试邮件发送
	 */
	@RequestMapping(value = "/getTestDemoEmail1", method = RequestMethod.GET)
	public  void getEntityById2() throws Exception {
		String sendTo = "491861940@qq.com";
		String titel = "测试邮件标题";
		String content = "测试邮件内容";
		File file=new File("E:\\收据.jpg");
		List list=new ArrayList();
		list.add(file);
		emailService.sendAttachmentsMail(sendTo, titel, content,list);
	}
}