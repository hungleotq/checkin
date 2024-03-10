package checkin.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.validation.ObjectError;
import org.springframework.web.client.RestTemplate;

import java.util.Objects;

@Service
public class CheckInService {

    final static String loginUrl = "https://tms.blueeye.ai/api/user/login";
    final static String infoUrl = "https://tms.blueeye.ai/api/user/get-user-info";
    final static String checkinUrl = "https://tms.blueeye.ai/api/timesheet/checkin_online";

    public boolean checkin() {
        boolean isChecked = false;
        try {
            RestTemplate restTemplate = new RestTemplate();
            ObjectMapper mapper = new ObjectMapper();
            ObjectNode body = mapper.createObjectNode();
            body.put("badgeNumber", "6888");
            body.put("password", "seta@123");
            ResponseEntity<ObjectNode> response = restTemplate.postForEntity(loginUrl, body, ObjectNode.class);
            System.out.println(response.getBody().toString());
            System.out.println(response.getHeaders().get("Set-Cookie").get(0));

            HttpHeaders headers = new HttpHeaders();
            headers.add("Cookie", response.getHeaders().get("Set-Cookie").get(0) );

            HttpEntity<String> entity = new HttpEntity<>("{\"comment\":\"\"}", headers);

            response = restTemplate.exchange(checkinUrl, HttpMethod.POST, entity, ObjectNode.class);
            String checkIn = response.getBody().path("data").path("checkIn").asText("");
            System.out.println(checkIn);
            if (!"".equals(checkIn)) {
                isChecked = true;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return isChecked;
    }

}
