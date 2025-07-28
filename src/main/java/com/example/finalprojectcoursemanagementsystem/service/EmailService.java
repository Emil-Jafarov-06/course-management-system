package com.example.finalprojectcoursemanagementsystem.service;

import lombok.RequiredArgsConstructor;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final MailSender mailSender;

    public void sendSimpleEmail(String to, String subject, String text) {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom("");
            message.setTo(to);
            message.setSubject(subject);
            message.setText(text);

            mailSender.send(message);
    }

}
