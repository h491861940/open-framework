package com.open.framework.commmon.email.impl;

import java.io.File;
import java.util.List;
import java.util.Map;

import com.open.framework.commmon.email.EmailConfig;
import com.open.framework.commmon.email.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.internet.MimeMessage;

@Service
public class EmailServiceImpl implements EmailService {

    @Autowired
    private EmailConfig emailConfig;
    @Autowired
    private JavaMailSender mailSender;

    public void sendSimpleMail(String sendTo, String titel, String content) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(emailConfig.getEmailFrom());
        message.setTo(sendTo);
        message.setSubject(titel);
        message.setText(content);
        mailSender.send(message);
    }

    public void sendAttachmentsMail(String sendTo, String titel, String content, List<File> attachments) {
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
            helper.setFrom(emailConfig.getEmailFrom());
            helper.setTo(sendTo);
            helper.setSubject(titel);
            helper.setText(content);
            for (File file : attachments) {
                helper.addAttachment(file.getName(), file);
            }

        } catch (Exception e) {
        }

        mailSender.send(mimeMessage);
    }

    public void sendTemplateMail(String sendTo, String titel, Map<String, Object> content, List<File> attachments) {
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
            helper.setFrom(emailConfig.getEmailFrom());
            helper.setTo(sendTo);
            helper.setSubject(titel);
            String text = content + "";//TODO 模版处理content,用的时候做
            helper.setText(text, true);
            for (File file : attachments) {
                helper.addAttachment(file.getName(), file);
            }
        } catch (Exception e) {
        }

        mailSender.send(mimeMessage);
    }
}