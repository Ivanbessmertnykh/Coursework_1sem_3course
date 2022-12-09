package ru.ivan.coursework.schedule;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import ru.ivan.coursework.entity.Res;
import ru.ivan.coursework.repository.ResRepository;
import ru.ivan.coursework.service.ResService;

import javax.annotation.PostConstruct;
import java.util.Calendar;

@Component
public class Scheduler {
    @Autowired
    private ResRepository resRepository;
    @Autowired
    private ResService resService;

    @PostConstruct
    public void prepare(){
        Res time = resRepository.findResByNumberOfTimeGreaterThan(100);
        System.out.println(Calendar.getInstance().get(Calendar.DAY_OF_MONTH));
        if (time == null) {
            resService.nextDay();
            return;
        }
        if (time.getNumberOfTime() - 100 != Calendar.getInstance().get(Calendar.DAY_OF_MONTH))
            resService.nextDay();
    }

    @Scheduled(cron = "0 0 0 * * *")
    public void reload(){
        prepare();
    }
}
