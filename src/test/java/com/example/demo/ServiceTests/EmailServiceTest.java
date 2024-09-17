package com.example.demo.ServiceTests;

import com.example.demo.services.impl.EmailService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class EmailServiceTest {

    @Mock
    private JavaMailSender javaMailSender;

    @InjectMocks
    private EmailService emailService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void sendEmailThrowsExceptionWhenEmailIsNull() {
        assertThrows(RuntimeException.class, () -> emailService.sendEmail(null, "Subject", "Message"));
    }

    @Test
    void sendEmailThrowsExceptionWhenSubjectIsNull() {
        assertThrows(RuntimeException.class, () -> emailService.sendEmail("valid@example.com", null, "Message"));
    }

    @Test
    void sendEmailThrowsExceptionWhenMessageIsNull() {
        assertThrows(RuntimeException.class, () -> emailService.sendEmail("valid@example.com", "Subject", null));
    }

    @Test
    void sendEmailThrowsExceptionWhenEmailIsEmpty() {
        assertThrows(RuntimeException.class, () -> emailService.sendEmail("", "Subject", "Message"));
    }

    @Test
    void sendEmailThrowsExceptionWhenSubjectIsEmpty() {
        assertThrows(RuntimeException.class, () -> emailService.sendEmail("valid@example.com", "", "Message"));
    }

    @Test
    void sendEmailThrowsExceptionWhenMessageIsEmpty() {
        assertThrows(RuntimeException.class, () -> emailService.sendEmail("valid@example.com", "Subject", ""));
    }

    @Test
    void sendEmailThrowsExceptionWhenEmailIsInvalidFormat() {
        assertThrows(RuntimeException.class, () -> emailService.sendEmail("invalid-email-format", "Subject", "Message"));
    }

    @Test
    void sendRegistrationEmailThrowsExceptionWhenEmailIsInvalid() {
        assertThrows(RuntimeException.class, () -> emailService.sendRegistrationEmail("invalid-email"));
    }

    @Test
    void sendRegistrationEmailThrowsExceptionWhenEmailIsNull() {
        assertThrows(RuntimeException.class, () -> emailService.sendRegistrationEmail(null));
    }

    @Test
    void sendRegistrationEmailThrowsExceptionWhenEmailIsEmpty() {
        assertThrows(RuntimeException.class, () -> emailService.sendRegistrationEmail(""));
    }
}