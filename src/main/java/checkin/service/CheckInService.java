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
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

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

    public void checkin() {
        try {
            log.info("Start checkin : {}", new Date());
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

            mailService.sendMail("Checked in at " + checkIn);
        } catch (Exception ex) {
            log.error("Error: {}", ex.getMessage());
            ex.printStackTrace();
        }
    }

}
