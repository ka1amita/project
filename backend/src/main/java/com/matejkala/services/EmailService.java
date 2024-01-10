package com.matejkala.services;

import javax.mail.MessagingException;

public interface EmailService {
    void sendSimpleMessage(String to, String subject, String text);

    void sendMimeMessage(String to, String subject, String messageText, String code, String url) throws MessagingException;

    void registerConfirmationEmail(String to, String username, String code) throws MessagingException;
    void registerConfirmationEmail(String to, String username, String code, String url) throws MessagingException;

    void resetPasswordEmail(String to, String username, String code, String frontendUrl) throws MessagingException;
}
