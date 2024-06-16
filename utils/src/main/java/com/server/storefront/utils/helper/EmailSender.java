package com.server.storefront.utils.helper;

import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

@Component
@Data
public class EmailSender {

    @Autowired
    JavaMailSender emailSender;

    @Value("${mail.sender.from}")
    private static String MAIL_SENDER;

    public void sendEmailNotification(String recipient, String subject, String content) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(MAIL_SENDER);
        message.setTo(recipient);
        message.setSubject(subject);
        message.setText(content);
        emailSender.send(message);
    }
}
