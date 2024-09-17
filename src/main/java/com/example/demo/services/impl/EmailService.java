package com.example.demo.services.impl;

import org.apache.commons.mail.DefaultAuthenticator;
import org.apache.commons.mail.Email;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.SimpleEmail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {
    private final JavaMailSender javaMailSender;
    @Value("${spring.mail.username}")
    public String emailUsername;

    @Value("${spring.mail.password}")
    public String emailPassword;

    @Autowired
    public EmailService(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

    public void sendEmail(String to, String subject, String text) {
        Email email = new SimpleEmail();
        email.setHostName("smtp.gmail.com");
        email.setSmtpPort(587);
        email.setAuthenticator(new DefaultAuthenticator(emailUsername, emailPassword));
        email.setSSLOnConnect(true);

        try {
            email.setFrom("info@doubledcontest.com");
        } catch (EmailException e) {
            throw new RuntimeException(e);
        }
        email.setSubject(subject);
        try {
            email.setMsg(text);
        } catch (EmailException e) {
            throw new RuntimeException(e);
        }
        try {
            email.addTo(to);
        } catch (EmailException e) {
            throw new RuntimeException(e);
        }
        try {
            email.send();
        } catch (EmailException e) {
            throw new RuntimeException(e);
        }
    }
    public void sendContactEmail(String to, String subject, String text, String email, String name, String phone) {
        text = "From  Name: " + name + "\n" + "Email: " + email + "\n" + "Phone: " + phone + "\n" + text;

        sendEmail(to, subject, text);
    }

    public void sendRegistrationEmail(String to) {
        String subject = "Welcome to the Forum!";
        String text = "Thank you for registering at our forum. We are excited to have you!";
        sendEmail(to, subject, text);
    }
}

