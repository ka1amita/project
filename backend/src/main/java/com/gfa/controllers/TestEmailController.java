package com.gfa.controllers;

import com.gfa.services.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.mail.MessagingException;

@RestController
public class TestEmailController {

    @Autowired
    private final EmailService emailService;

    public TestEmailController(EmailService emailService) {
        this.emailService = emailService;
    }

    @GetMapping("sendTestMail")
    public String sendTestMail() throws MessagingException {
        emailService.resetPasswordEmail("test@email", "daniel", "1234");
        emailService.registerConfirmationEmail("test@email", "daniel", "12345");
        return "e-mails have been sent";
    }
}
