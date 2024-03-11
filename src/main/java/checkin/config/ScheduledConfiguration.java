package checkin.config;

import checkin.service.CheckInService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.Trigger;
import org.springframework.scheduling.TriggerContext;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;
import org.springframework.scheduling.support.CronTrigger;


@Configuration
@EnableScheduling
@Slf4j
public class ScheduledConfiguration implements SchedulingConfigurer {

    @Autowired
    CheckInService checkInService;

    @Value("${cron.exp}")
    String cronExp;

    @Override
    public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
        log.info("ScheduledConfiguration: {}", cronExp);
        ThreadPoolTaskScheduler threadPoolTaskScheduler = new ThreadPoolTaskScheduler();
        threadPoolTaskScheduler.setPoolSize(1);
        threadPoolTaskScheduler.setThreadNamePrefix("CheckIn");
        threadPoolTaskScheduler.initialize();
        threadPoolTaskScheduler.schedule(() -> checkInService.checkin(),
                new CronTrigger(cronExp));
    }
}
