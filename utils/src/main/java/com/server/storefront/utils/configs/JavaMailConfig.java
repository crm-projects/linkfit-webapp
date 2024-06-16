package com.server.storefront.utils.configs;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.Properties;

/**
 * Configuration class for {@link JavaMailSender}
 *
 * {@link JavaMailSender}
 * @author Vas
 * @version 1.0
 */
@Configuration
public class JavaMailConfig {

    private final static int GMAIL_SMTP_PORT = 0;

    @Value("${spring.mail.host}")
    private static String MAIL_HOST;

    @Value("${spring.mail.username}")
    private static String MAIL_USERNAME;

    @Value("${spring.mail.password}")
    private static String MAIL_PASSWORD;

    @Bean
    public JavaMailSender javaMailSender() {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost(MAIL_HOST);
        mailSender.setUsername(MAIL_USERNAME);
        mailSender.setPassword(MAIL_PASSWORD);
        mailSender.setPort(GMAIL_SMTP_PORT);

        Properties properties = mailSender.getJavaMailProperties();
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.smtp.starttls.required", "true");
        return mailSender;
    }
}
