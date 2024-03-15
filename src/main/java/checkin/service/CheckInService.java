package checkin.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

@Service
@Slf4j
public class CheckInService {

    @Value("${tms.login.url}")
    String loginUrl;

    @Value("${tms.info.url}")
    String infoUrl;

    @Value("${tms.checkin.url}")
    String checkinUrl;

    @Autowired
    MailService mailService;

    private final static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Retryable(retryFor = Exception.class,
            backoff = @Backoff(delay = 1000))
    public void checkin() {
        log.info("Start checkin : {}", LocalDateTime.now().format(formatter));
        RestTemplate restTemplate = new RestTemplate();
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode body = mapper.createObjectNode();
        body.put("badgeNumber", "6888");
        body.put("password", "seta@123");
        ResponseEntity<ObjectNode> response = restTemplate.postForEntity(loginUrl, body, ObjectNode.class);
        log.info("Login: {}\n {}\n {}", loginUrl, body, response);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Cookie", response.getHeaders().get("Set-Cookie").get(0) );

        body = mapper.createObjectNode();
        body.put("comment", "");
        HttpEntity<ObjectNode> entity = new HttpEntity<ObjectNode>(body, headers);

        response = restTemplate.exchange(checkinUrl, HttpMethod.POST, entity, ObjectNode.class);
        String checkIn = response.getBody().path("data").path("checkIn").asText("");

        log.info("CheckIn: {}\n {}\n {}", checkinUrl, body, response);

        mailService.sendMail("Checked in at " + LocalDateTime.now().format(formatter));
    }

    @Recover
    public void recover(Exception e) {
        log.error("Error: {}", e.getMessage());
        mailService.sendMail("Failed to checkin at " + LocalDateTime.now().format(formatter) + " cause by " + e.getMessage());
    }
}
