package br.com.newtonpaiva.fastpass.service;

import br.com.newtonpaiva.fastpass.enums.EmailStatus;
import br.com.newtonpaiva.fastpass.model.Email;
import br.com.newtonpaiva.fastpass.repository.EmailRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
@Slf4j
public class MailSenderService {

    private static final String MAIL_FROM = "noreply@fastpass.com.br";

    @Autowired
    private JavaMailSender emailSender;
    @Autowired
    private EmailRepository emailRepository;

    public void sendSimpleMessage(Email email) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(MAIL_FROM);
            message.setTo(email.getTo().getEmail());
            message.setText(email.getText());
            message.setSentDate(new Date());
            emailSender.send(message);
            email.setStatus(EmailStatus.SEND);
        } catch (MailException e) {
            email.setStatus(EmailStatus.FAILED);
            log.error(String.format("Error on send message: %s", e.getMessage()));
        } finally {
            emailRepository.saveAndFlush(email);
        }
    }
}
