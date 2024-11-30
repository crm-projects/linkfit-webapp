package com.server.storefront.configs;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.Properties;

@Configuration
public class JavaMailConfig {

    private static final int GMAIL_SMTP_PORT = 0;

    @Value("${spring.mail.host}")
    private static String host;

    @Value("${spring.mail.username}")
    private static String userName;

    @Value("${spring.mail.password}")
    private static String password;

    @Bean
    public JavaMailSender javaMailSender() {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost(host);
        mailSender.setUsername(userName);
        mailSender.setPassword(password);
        mailSender.setPort(GMAIL_SMTP_PORT);

        Properties properties = mailSender.getJavaMailProperties();
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.smtp.starttls.required", "true");
        return mailSender;
    }
}
