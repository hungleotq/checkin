package checkin;

import checkin.service.CheckInService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.TaskScheduler;

@SpringBootApplication
public class CheckinApplication {
    @Autowired
    CheckInService checkInService;

    public static void main(String[] args) {
        CheckInService checkInService  = new CheckInService();
        checkInService.checkin();
    }

}
