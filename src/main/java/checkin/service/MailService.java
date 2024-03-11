package checkin.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class MailService {

    @Autowired
    JavaMailSender javaMailSender;

    @Value("${mail.to}")
    String to;

    @Value("${spring.mail.username}")
    String from;

    public void sendMail(String body) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(from);
        message.setTo(to);
        message.setCc(from);
        message.setSubject("Checkin");
        message.setText(body);
        javaMailSender.send(message);
        log.info("Sent email to {} with body {}", to, body);
    }
}
