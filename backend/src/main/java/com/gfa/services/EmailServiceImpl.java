package com.gfa.services;

import com.gfa.utils.Endpoint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.util.Locale;
import javax.servlet.http.HttpServletRequest;

@Service
public class EmailServiceImpl implements EmailService {

    @Autowired
    public JavaMailSender javaMailSender;
    @Autowired
    private SpringTemplateEngine templateEngine;
    @Autowired
    private MessageSource messageSource;
    @Autowired
    private HttpServletRequest httpServletRequest;

    @Override
    public void sendSimpleMessage(String to, String subject, String text) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);
        javaMailSender.send(message);
    }

    @Override
    public void sendMimeMessage(String to, String subject, String messageText, String code, String url) throws MessagingException {
        Locale currentLocale = LocaleContextHolder.getLocale();
        try {
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
            Context context = new Context();
            context.setVariable("subject", subject);
            context.setVariable("message", messageText);
            context.setVariable("code", code);
            context.setVariable("url", url);
            String html = templateEngine.process("email-template", context);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(html, true);
            javaMailSender.send(mimeMessage);
        } catch (MessagingException e) {
            throw new MessagingException(messageSource.getMessage("error.messaging.exception", null, currentLocale));
        }
    }

    @Override
    public void registerConfirmationEmail(String to, String username, String code) throws MessagingException {
        Locale currentLocale = LocaleContextHolder.getLocale();
        String subject = messageSource.getMessage("email.subject", null, currentLocale);
        String message = messageSource.getMessage("email.body", new Object[]{username}, currentLocale);
        String url = httpServletRequest.getScheme() + "://" + httpServletRequest.getServerName() + ":" + httpServletRequest.getServerPort() + Endpoint.CONFIRM_WITH_CODE + "/";
        sendMimeMessage(to, subject, message, code, url);
    }

    @Override
    public void resetPasswordEmail(String to, String username, String code, String frontendUrl) throws MessagingException {
        Locale currentLocale = LocaleContextHolder.getLocale();
        String subject = messageSource.getMessage("email.reset.subject", null, currentLocale);
        String body = messageSource.getMessage("email.reset.body", new Object[]{username}, currentLocale);
        sendMimeMessage(to, subject, body, code, frontendUrl);
    }
}