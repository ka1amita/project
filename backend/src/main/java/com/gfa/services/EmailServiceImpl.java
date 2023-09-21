package com.gfa.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

@Service
public class EmailServiceImpl implements EmailService {

    @Autowired
    public JavaMailSender javaMailSender;
    @Autowired
    private SpringTemplateEngine templateEngine;

    @Override
    public void sendSimpleMessage(String to, String subject, String text) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);
        javaMailSender.send(message);
    }

    @Override
    public void sendMimeMessage(String to, String subject, String messageText, String code) throws MessagingException {
        try {
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
            Context context = new Context();
            context.setVariable("subject", subject);
            context.setVariable("message", messageText);
            context.setVariable("code", code);
            String html = templateEngine.process("email-template", context);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(html, true);
            javaMailSender.send(mimeMessage);
        } catch (MessagingException e) {
            throw new MessagingException("Unable to create your e-mail message.");
        }
    }

    @Override
    public void registerConfirmationEmail(String to, String username, String code) throws MessagingException {
        sendMimeMessage(to,
                "Please confirm your registration",
                String.format("Welcome to our To Do application %s! Thank you for registering, " +
                        "please confirm your registration with the following code.", username), code);
    }

    @Override
    public void resetPasswordEmail(String to, String username, String code) throws MessagingException {
        sendMimeMessage(to,
                "Password reset requested",
                String.format("Dear %s, we received a password reset from your e-mail address. If this was you please use the following code to reset your password." +
                        "If you didn't request a password reset please contact our customer support.", username), code);
    }
}