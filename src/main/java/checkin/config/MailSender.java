package checkin.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.stereotype.Component;

import java.util.Properties;

@Configuration
@Slf4j
public class MailSender {

    @Value("${spring.mail.host}")
    String host;
    @Value("${spring.mail.port}")
    int port;
    @Value("${spring.mail.username}")
    String username;
    @Value("${spring.mail.password}")
    String password;

    @Bean
    public JavaMailSender getMailSender() {
        log.info("Configure mail service");
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost(host);
        mailSender.setPort(port);

        mailSender.setUsername(username);
        mailSender.setPassword(password);

        Properties props = mailSender.getJavaMailProperties();
        props.put("mail.transport.protocol", "smtp");
        props.put("mail.smtp.auth", true);
        props.put("mail.smtp.starttls.enable", true);
        props.put("mail.debug", "true");

        return mailSender;
    }
}
